package es.uniovi.imovil.samira.tripastur;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.util.ArraySet;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class MainActivity extends AppCompatActivity implements OficinaListFragment.Callbacks,  MenuItemCompat.OnActionExpandListener,  NavigationView.OnNavigationItemSelectedListener {

	private boolean mTwoPanes = false;
	public static SearchView searchView;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//Obtener del fichero las oficinas favoritas guardadas
		this.restoreList();

		//instancio el layout y los elementos que quiero mostrar en la vista inicial
		setContentView(R.layout.main);

		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarPrincipal);
		setSupportActionBar(toolbar);
		setTitle(getTitle());

		DrawerLayout drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
		NavigationView navView = (NavigationView)findViewById(R.id.navview);

		ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
				this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
		drawerLayout.setDrawerListener(toggle);
		toggle.syncState();


		navView.setNavigationItemSelectedListener(this);

		if (findViewById(R.id.oficina_details_container) != null){
			mTwoPanes = true;
		}
	}

	@Override
	protected void onStop() {
		super.onStop();

	}

	//Metodo para que el drawer aparezca y desaparezca
	@Override
	public void onBackPressed() {
		DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		if (drawer.isDrawerOpen(GravityCompat.START)) {
			drawer.closeDrawer(GravityCompat.START);
		} else {
			super.onBackPressed();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {


		getMenuInflater().inflate(R.menu.main, menu);

		//Utilizo un searchview para realizar la búsqueda de las oficinas
		MenuItem searchItem = menu.findItem(R.id.action_search_oficina);
		MainActivity.searchView = (SearchView) MenuItemCompat.getActionView(searchItem);

		//creamos el elemnto fragment que usará el searchview y hacemos la conexion entre metodo y lista
		FragmentManager fragmentManager = getSupportFragmentManager();
		OficinaListFragment fragment = (OficinaListFragment) fragmentManager.findFragmentById(R.id.oficina_list_frag);

        searchView.setOnQueryTextListener(fragment.getmAdapter());
		MenuItemCompat.setOnActionExpandListener(searchItem, this);


		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

	    switch (item.getItemId()) {
	        case R.id.action_search_oficina:

	            return true;

	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}

	//Pasamos la oficina seleccionada ya sea en movil o tablet
	public void onOficinaSelected(Oficina Oficina) {
		
		if (mTwoPanes ) {
			FragmentManager fragmentManager = getSupportFragmentManager();
			OficinaDetailsFragment fragment = (OficinaDetailsFragment) fragmentManager.findFragmentById(R.id.oficina_details_frag);
            fragment.setOficina(Oficina);
		} else {
			Intent intent = new Intent(this, OficinaDetailsActivity.class);
			intent.putExtra(OficinaDetailsActivity.OFICINA, (Serializable) Oficina);
			startActivity(intent);
		}		
	}

	@Override
	public boolean onMenuItemActionExpand(MenuItem item) {
		return true;
	}

	@Override
	public boolean onMenuItemActionCollapse(MenuItem item) {
		return true;
	}

	//Implementamos las opciones del drawer, en este caso pasamos las listas de las oficinas para obtener las coordenadas
	@SuppressWarnings("StatementWithEmptyBody")
	@Override
	public boolean onNavigationItemSelected(@NonNull MenuItem item) {
		// Handle navigation view item clicks here.
		int id = item.getItemId();

		if (id == R.id.mapa) {
			FragmentManager fragmentManager = getSupportFragmentManager();
			OficinaListFragment fragment = (OficinaListFragment) fragmentManager.findFragmentById(R.id.oficina_list_frag);
			Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
			intent.putExtra("listaOficinasMap", (Serializable) fragment.getListaOficinasMap());
			intent.putExtra("listaOficinas", (Serializable) fragment.getListaOficinas());
			startActivity(intent);

		}

		DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		drawer.closeDrawer(GravityCompat.START);
		return true;
	}

	//Metodo para guardar las oficinas favoritas a un fichero en el almacenamiento interno
	private void saveList () {
		FileOutputStream file = null;
		OutputStream buffer = null;
		ObjectOutput output = null;

		try {
			file = this.openFileOutput("lista-favoritas", Context.MODE_PRIVATE);

			buffer = new BufferedOutputStream(file);
			output = new ObjectOutputStream(buffer);

			//Recorro la lista de oficinas y si se ha marcado como favorita la añado a una nueva lista favoritas que se almacenara en el fichero
			ArrayList<Oficina> favoritas=new ArrayList<Oficina>();
			for (Oficina favorita: OficinaListFragment.listaOficinas) {
				if(favorita.isOficinaFavorita()){
					Log.d("pasando a memoria",favorita.getId());
					favoritas.add(favorita);
				}
			}
			output.writeObject(favoritas);
			Log.d("d","escribiendo datos");
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				output.close();
			} catch (IOException e) {
			}
		}
	}

	//Leemos del fichero almacenado, e introducimos la favorita si es que no estaba ya
	private boolean restoreList () {
		InputStream buffer = null;
		ObjectInput input = null;
		Log.d("data","recuperando datos");
		try {

			buffer = new BufferedInputStream(
					this.openFileInput("lista-favoritas"));

			input = new ObjectInputStream(buffer);
			ArrayList<Oficina> favoritas = (ArrayList<Oficina>) input.readObject();
			OficinaListFragment.listaOficinas=new ArrayList<Oficina>();
			for (Oficina favorita: favoritas) {
				if(OficinaListFragment.listaOficinas.contains(favorita)){
					Log.d("d","Ya esta");
				}else{
					Log.d("añadiendo",favorita.getId());
					OficinaListFragment.listaOficinas.add(favorita);
				}

			}

			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				input.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return false;
	}

	//Guardamos la lista en el onPause, e sdecir, cuando la aplicación va a pasar a onstop y  puede perderse el estado
	@Override
	public void onPause() {
		super.onPause();
		this.saveList();

	}

}
