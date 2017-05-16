package es.uniovi.imovil.jcgranda.courses;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.TextView;

public class CourseDetailsActivity extends ActionBarActivity {
	
	public static final String DESCRIPTION = "es.uniovi.imovil.jcgranda.courses.DESCRIPTION";

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.course_details_activity);
		
		// Existe el contenedor del fragmento?
		if (findViewById(R.id.fragment_container) != null) {

			// Si estamos restaurando desde un estado previo no hacemos nada
			if (savedInstanceState != null) {
				return;
			}

			// Crear el fragmento pasndole el parmetro
			Intent intent = getIntent();
		    String description = intent.getStringExtra(DESCRIPTION);
			CourseDetailsFragment fragment =
				CourseDetailsFragment.newInstance(description);
			
			// Aadir el fragmento al contenedor 'fragment_container'
			getSupportFragmentManager().beginTransaction()
					.add(R.id.fragment_container, fragment).commit();
		}
	}
}
