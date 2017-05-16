package es.uniovi.imovil.jcgranda.courses;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;


public class MainActivity extends ActionBarActivity implements CourseListFragment.Callbacks {
	
	private int mCourseCount = 0;
	private boolean mTwoPanes = false;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		if (findViewById(R.id.course_details_container) != null) {
			mTwoPanes = true;
		}
	}	

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {


		getMenuInflater().inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
			    
	    switch (item.getItemId()) {
	        case R.id.action_add_course:
	        	FragmentManager fragmentManager = getSupportFragmentManager();	        	
	        	CourseListFragment fragment = (CourseListFragment) fragmentManager.findFragmentById(R.id.course_list_frag);
	        	String name = String.format(getString(R.string.default_course_format), ++mCourseCount);
	    		String teacher = String.format(getString(R.string.default_teacher_format), mCourseCount);
	    		Course course = new Course(name, teacher, null);
	            fragment.addCourse(course);
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}

	@Override
	public void onCourseSelected(Course course) {
		
		if (mTwoPanes) {
			FragmentManager fragmentManager = getSupportFragmentManager();	        	
        	CourseDetailsFragment fragment = (CourseDetailsFragment) fragmentManager.findFragmentById(R.id.course_details_frag);        	
            fragment.setDescription(course.getDescription());
		} else {
			Intent intent = new Intent(this, CourseDetailsActivity.class);
		    intent.putExtra(CourseDetailsActivity.DESCRIPTION, course.getDescription());
		    startActivity(intent);			
		}		
	}	

}
