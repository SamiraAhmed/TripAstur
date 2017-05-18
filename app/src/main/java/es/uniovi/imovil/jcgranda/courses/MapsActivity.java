package es.uniovi.imovil.jcgranda.courses;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;
import java.util.Map;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,  GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private GoogleMap gMap;
    private Location miLocalizacion;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest locationRequest;
    private List<OficinasTurismo> listaOficinas;
    private Map<Integer, OficinasTurismo> listaOficinasMap;
    private OficinasTurismo oficinasT;
    private final int ZOOM_CERCANO = 14;
    private final int ZOOM_ASTURIAS = 7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)
                .setFastestInterval(1 * 1000);

        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;

        oficinasT = (OficinasTurismo) getIntent().getExtras().get("oficinasT");
        listaOficinasMap = (Map<Integer, OficinasTurismo>) getIntent().getExtras().get("listaOficinasMap");
        listaOficinas = (List<OficinasTurismo>) getIntent().getExtras().get("listaOficinas");
        if (oficinasT != null) {
            LatLng latLngOficinaVO = new LatLng(oficinasT.getMapaX(), oficinasT.getMapaY());
            gMap.addMarker(new MarkerOptions().position(latLngOficinaVO).title(oficinasT.getNombre()));
            gMap.moveCamera(CameraUpdateFactory.newLatLng(latLngOficinaVO));
            gMap.animateCamera(CameraUpdateFactory.zoomTo(ZOOM_CERCANO));
        }

        //Initialize Google Play Services
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                gMap.setMyLocationEnabled(true);
                gMap.animateCamera(CameraUpdateFactory.zoomTo(ZOOM_CERCANO));
            } else {
                if (oficinasT == null) {
                    gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(43.360384, -5.845033), ZOOM_ASTURIAS));
                } else {
                    gMap.animateCamera(CameraUpdateFactory.zoomTo(ZOOM_CERCANO));
                }

            }
        } else {
            buildGoogleApiClient();
            gMap.setMyLocationEnabled(true);
        }

        if (listaOficinas != null) {
            for (int i=0; i<listaOficinas.size(); i++) {
                OficinasTurismo oficinaVO = listaOficinas.get(i);
                LatLng latLngOficinaVO = new LatLng(oficinaVO.getMapaX(), oficinaVO.getMapaY());
                gMap.addMarker(new MarkerOptions().position(latLngOficinaVO).title(oficinaVO.getNombre()));
            }
        }
    }


    protected synchronized void buildGoogleApiClient() {
        try {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
            mGoogleApiClient.connect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(R.string.pedirPermiso).setTitle(R.string.tituloPedirPermiso);
            builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User clicked OK button
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
            return;
        }
        miLocalizacion = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (miLocalizacion == null) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, locationRequest, (com.google.android.gms.location.LocationListener) this);
        } else {
            //no lo coge aún dándole permiso por el ritmo
            gMap.animateCamera(CameraUpdateFactory.zoomTo(ZOOM_CERCANO));
        }

        //si no hay oficinaVO es que viene a verlas todas
        if (oficinasT == null) {
            oficinasT = calcularOficinaMasCercana(miLocalizacion);
        }
        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(oficinasT.getMapaX(), oficinasT.getMapaY()), ZOOM_CERCANO));
    }

    private OficinasTurismo calcularOficinaMasCercana(Location miLocalizacion) {
        float distanciaMinima = 0;
        OficinasTurismo oficinaMasCercanaVO = null;
        for (int i=0; i<listaOficinas.size(); i++) {
            oficinasT = listaOficinas.get(i);
            Location targetLocation = new Location("");
            targetLocation.setLatitude(oficinasT.getMapaX());
            targetLocation.setLongitude(oficinasT.getMapaY());

            float distanciaActual = miLocalizacion.distanceTo(targetLocation);
            if (distanciaMinima == 0) {
                distanciaMinima = distanciaActual;
                oficinaMasCercanaVO = oficinasT;
            } else if (distanciaMinima > distanciaActual) {
                distanciaMinima = distanciaActual;
                oficinaMasCercanaVO = oficinasT;
            }
        }
        return oficinaMasCercanaVO;
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;


    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Asking user if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }
}
