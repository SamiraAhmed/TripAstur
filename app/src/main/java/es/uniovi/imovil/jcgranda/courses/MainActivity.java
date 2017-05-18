package es.uniovi.imovil.jcgranda.courses;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.io.Serializable;


public class MainActivity extends AppCompatActivity implements OficinaListFragment.Callbacks {
	

	private boolean mTwoPanes = false;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);


		
		if (findViewById(R.id.oficina_details_container) != null) {
			mTwoPanes = true;
		}
	}	

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {


		getMenuInflater().inflate(R.menu.main, menu);

		MenuItem searchItem = menu.findItem(R.id.action_search_oficina);
		SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);



		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
			    
	    switch (item.getItemId()) {
	        case R.id.action_search_oficina:
	        	/*FragmentManager fragmentManager = getSupportFragmentManager();
	        	OficinaListFragment fragment = (OficinaListFragment) fragmentManager.findFragmentById(R.id.oficina_list_frag);
	        	String name = String.format(getString(R.string.default_course_format), ++mCourseCount);
	    		String teacher = String.format(getString(R.string.default_teacher_format), mCourseCount);
	    		Course course = new Course(name, teacher, null);
	            fragment.addCourse(course);*/
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}



	public void onOficinaSelected(OficinasTurismo Oficina) {
		
		if (mTwoPanes) {
			FragmentManager fragmentManager = getSupportFragmentManager();	        	
        	OficinaDetailsFragment fragment = (OficinaDetailsFragment) fragmentManager.findFragmentById(R.id.oficina_details_frag);
            fragment.setOficina(Oficina);
		} else {
			Intent intent = new Intent(this, OficinaDetailsActivity.class);
		    intent.putExtra(OficinaDetailsActivity.OFICINA, (Serializable) Oficina);
		    startActivity(intent);			
		}		
	}	

}
