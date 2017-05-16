package es.uniovi.imovil.jcgranda.courses;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class CourseDetailsFragment extends Fragment {
	
	private static final String DESCRIPTION_ARG = "description";

	public static CourseDetailsFragment newInstance(String desc) {
		
		CourseDetailsFragment fragment = new CourseDetailsFragment();
		
		Bundle args = new Bundle();
        args.putString(DESCRIPTION_ARG, desc);
        fragment.setArguments(args);
        
		return fragment;
	}
	
	public CourseDetailsFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View rootView;
		rootView = inflater.inflate(R.layout.course_details_fragment, container, false);			
		
		// Si estamos restaurando desde un estado previo no hacemos nada
		if (savedInstanceState != null) {
			return rootView;
		}
					
		Bundle args = getArguments();
		TextView tvDescription = (TextView) rootView.findViewById(R.id.textViewDesc);
		if (args != null) {
			String description = args.getString(DESCRIPTION_ARG);	    
			tvDescription.setText(description);
		} else {
			tvDescription.setText(null);
		}
		return rootView;
	}

	public void setDescription(String description) {
		
		TextView tvDescription = (TextView) getView().findViewById(R.id.textViewDesc);

		tvDescription.setText(description);		
	}
}
