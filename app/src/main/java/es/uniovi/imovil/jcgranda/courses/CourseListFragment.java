package es.uniovi.imovil.jcgranda.courses;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

public class CourseListFragment extends Fragment implements AdapterView.OnItemClickListener {

	public interface Callbacks {
		public void onCourseSelected(Course course);
	}
	
	private CourseAdapter mAdapter = null;
	private Callbacks mCallback = null;

	
	public static CourseListFragment newInstance() {
		
		CourseListFragment fragment = new CourseListFragment();
		return fragment;
	}
	
	public CourseListFragment() {
	}

	@Override
	public void onAttach(Activity activity) {
		
        super.onAttach(activity);
        try {
        	mCallback = (Callbacks) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement Callbacks");
        }
    }
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View rootView;
		rootView = inflater.inflate(R.layout.course_list_fragment, container, false);			
		
		// Configurar la lista
		ListView lvItems = (ListView) rootView.findViewById(R.id.list_view_courses);
		String [] courses = getResources().getStringArray(R.array.courses);
		String [] teachers = getResources().getStringArray(R.array.teachers);
		String [] descriptions = getResources().getStringArray(R.array.descriptions);
		mAdapter = new CourseAdapter(getActivity(), createCourseList(courses, teachers, descriptions));
		lvItems.setAdapter(mAdapter);
		lvItems.setOnItemClickListener(this);
		
		return rootView;
	}

	private List<Course> createCourseList(String[] names, String[] teachers, String[] descriptions) {
		
		if (names.length != teachers.length || names.length != descriptions.length) {
			throw new IllegalStateException();
		}
			
		ArrayList<Course> courses = new ArrayList<Course>(names.length);
		for (int i = 0; i < names.length; i++) {
			courses.add(new Course(names[i], teachers[i], descriptions[i]));
		}
		return courses;
	}
	
	public void addCourse(Course course) {

		mAdapter.addCourse(course);		
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		
		Course course = (Course) parent.getItemAtPosition(position);
		mCallback.onCourseSelected(course);
	}
	
}
