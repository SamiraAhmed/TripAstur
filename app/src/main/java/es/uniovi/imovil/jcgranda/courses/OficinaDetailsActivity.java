package es.uniovi.imovil.jcgranda.courses;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OficinaDetailsActivity extends AppCompatActivity {


	private List<OficinasTurismo> listaOficinas = new ArrayList<OficinasTurismo>();
	private Map<Integer, OficinasTurismo> listaOficinasMap  = new HashMap<Integer, OficinasTurismo>();
	public static final String OFICINA = "Oficina";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.oficina_details_activity);

		FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
		fab.setOnClickListener(new View.OnClickListener() {
								   @Override
								   public void onClick(View v) {
									   Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
									   intent.putExtra("listaOficinasMap", (Serializable) listaOficinasMap);
									   intent.putExtra("listaOficinas", (Serializable) listaOficinas);
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

			// Crear el fragmento pasandole el parmetro
			Intent intent = getIntent();
			OficinasTurismo oficina = (OficinasTurismo) intent.getSerializableExtra(OficinaDetailsActivity.OFICINA);

			OficinaDetailsFragment fragment =
				OficinaDetailsFragment.newInstance(oficina);
			
			// Aadir el fragmento al contenedor 'fragment_container'
			getSupportFragmentManager().beginTransaction()
					.add(R.id.fragment_container, fragment).commit();
		}
	}
}
