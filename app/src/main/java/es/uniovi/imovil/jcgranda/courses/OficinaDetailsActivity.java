package es.uniovi.imovil.jcgranda.courses;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;

public class OficinaDetailsActivity extends AppCompatActivity {
	


	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.oficina_details_activity);
		
		// Existe el contenedor del fragmento?
		if (findViewById(R.id.fragment_container) != null) {

			// Si estamos restaurando desde un estado previo no hacemos nada
			if (savedInstanceState != null) {
				return;
			}

			// Crear el fragmento pasandole el parmetro
			Intent intent = getIntent();
			OficinasTurismo oficina = intent.getExtras().getClass(OficinasTurismo);

			OficinaDetailsFragment fragment =
				OficinaDetailsFragment.newInstance(oficina);
			
			// Aadir el fragmento al contenedor 'fragment_container'
			getSupportFragmentManager().beginTransaction()
					.add(R.id.fragment_container, fragment).commit();
		}
	}
}
