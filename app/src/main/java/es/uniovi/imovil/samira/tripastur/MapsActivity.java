package es.uniovi.imovil.samira.tripastur;

import android.Manifest;
import android.app.Service;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

//GoogleApiClient.ConnectionCallbacks: determina si el cliente está conectado (onConnected()) o desconectado (onConnectionSuspended()).
//GoogleApiClient.OnConnectionFailedListener: procesa los posibles errores de conexión (onConnectionFailed()) del cliente al servicio.

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,  GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private GoogleMap gMap;
    private Location miLocalizacion;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest locationRequest;
    private ArrayList<Oficina> listaOficinas;
    private Map<Integer, Oficina> listaOficinasMap;
    private Oficina oficinasT;
    private final int ZOOM_CERCANO = 14;
    private final int ZOOM_ASTURIAS = 7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }

        /*
        Ver si la ubicación esta activada
        LocationManager lm=(LocationManager) getSystemService(Service.LOCATION_SERVICE);
        List<String> pr = lm.getProviders(true);
        Iterator<String> ite=pr.iterator();
        while(ite.hasNext()){
            Log.d("providers",ite.next());
        }*/


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);


        locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)
                .setFastestInterval(1 * 1000);

        mapFragment.getMapAsync(this);
    }

    //Introducimos los marcadores
    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;


        oficinasT = (Oficina) getIntent().getExtras().get(OficinaDetailsActivity.OFICINA);

        listaOficinasMap = (Map<Integer, Oficina>) getIntent().getExtras().get("listaOficinasMap");
        listaOficinas = (ArrayList<Oficina>) getIntent().getExtras().get("listaOficinas");

        if (listaOficinas == null) {
            //Log.d(this.getClass().getCanonicalName(),"lista oficina es nula");
            LatLng latLngOficina = new LatLng(oficinasT.getMapaX(), oficinasT.getMapaY());
            gMap.addMarker(new MarkerOptions().position(latLngOficina).title(oficinasT.getNombre()));
            gMap.moveCamera(CameraUpdateFactory.newLatLng(latLngOficina));
            gMap.animateCamera(CameraUpdateFactory.zoomTo(ZOOM_CERCANO));
        }

        if (oficinasT == null) {
            //Log.d(this.getClass().getCanonicalName(),"oficina es nula");
            for (int i=0; i<listaOficinas.size(); i++) {
                Oficina oficina = listaOficinas.get(i);
                LatLng latLngOficina = new LatLng(oficina.getMapaX(), oficina.getMapaY());
                gMap.addMarker(new MarkerOptions().position(latLngOficina).title(oficina.getNombre()));
            }
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


    }


    // generar la instancia
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

    //Pedir al usuario la ubicación y obtenerla mediante la ultima localización
    //Permisos en tiempo de ejecución
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
        //Obtener mi localización
        miLocalizacion = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (miLocalizacion == null) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, locationRequest, this);
        } else {
            //no lo coge aún dándole permiso por el ritmo
            gMap.animateCamera(CameraUpdateFactory.zoomTo(ZOOM_CERCANO));
        }

        //si no hay oficinaT es que viene a verlas todas
        if (oficinasT == null) {
            oficinasT = calcularOficinaMasCercana(miLocalizacion);
        }
        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(oficinasT.getMapaX(), oficinasT.getMapaY()), ZOOM_CERCANO));
    }

    //Metodo para obtener la oficinas cercanas a tu ubicación
    private Oficina calcularOficinaMasCercana(Location miLocalizacion) {
        float distanciaMinima = 0;
        Oficina oficinaMasCercana = null;
        for (int i=0; i<listaOficinas.size(); i++) {
            oficinasT = listaOficinas.get(i);
            Location targetLocation = new Location("");
            targetLocation.setLatitude(oficinasT.getMapaX());
            targetLocation.setLongitude(oficinasT.getMapaY());

            float distanciaActual = miLocalizacion.distanceTo(targetLocation);
            if (distanciaMinima == 0) {
                distanciaMinima = distanciaActual;
                oficinaMasCercana = oficinasT;
            } else if (distanciaMinima > distanciaActual) {
                distanciaMinima = distanciaActual;
                oficinaMasCercana = oficinasT;
            }
        }
        return oficinaMasCercana;
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

    //Verificar la asignación de permisos antes de usar la API de ubicación
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
