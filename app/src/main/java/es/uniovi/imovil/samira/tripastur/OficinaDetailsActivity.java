package es.uniovi.imovil.samira.tripastur;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OficinaDetailsActivity extends AppCompatActivity {


	//Tag para pasar el bundle del fragmento entre las vistas
	public static final String OFICINA = "Oficina";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.oficina_details_activity);

		//instanciar el toolbar y el boton de vuelta a listado oficinas
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarDetalleActivity);
		setSupportActionBar(toolbar);
		setTitle(getTitle());

        if (getSupportActionBar() != null) // Habilitar up button
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
		fab.setOnClickListener(new View.OnClickListener() {
								   @Override
								   public void onClick(View v) {
									   Intent intent = new Intent(getApplication(), MapsActivity.class);
									   intent.putExtra(OficinaDetailsActivity.OFICINA, OficinaDetailsActivity.this.getIntent().getSerializableExtra(OficinaDetailsActivity.OFICINA));
									   startActivity(intent);
								   }
							   }

		);
		
		// Existe el contenedor del fragmento?
		if (findViewById(R.id.fragment_container) != null) {

			// Si estamos restaurando desde un estado previo no hacemos nada
			if (savedInstanceState != null) {

				return;
			}


			// Crear el fragmento pasandole el parametro
			Intent intent = getIntent();
			Oficina oficina = (Oficina) intent.getSerializableExtra(OficinaDetailsActivity.OFICINA);

			OficinaDetailsFragment fragment =
				OficinaDetailsFragment.newInstance(oficina);
			
			// Aadir el fragmento al contenedor 'fragment_container'
			getSupportFragmentManager().beginTransaction()
					.add(R.id.fragment_container, fragment).commit();
		}
	}


}
