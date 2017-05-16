package es.uniovi.imovil.jcgranda.courses;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CourseAdapter extends BaseAdapter {
	
	static class ViewHolder {
		public TextView mCourseName;
		public TextView mTeacherName;
	}
	
	private final List<Course> mCourses;
	public LayoutInflater mInflater;

	
	public CourseAdapter(Context context, List<Course> courses) {

		if (context == null || courses == null ) {
			throw new IllegalArgumentException();
		}
			
		this.mCourses = courses;
		this.mInflater = LayoutInflater.from(context);
	}
		
	@Override
	public int getCount() {

		return mCourses.size();
	}

	@Override
	public Object getItem(int position) {
		
		return mCourses.get(position);
	}

	@Override
	public long getItemId(int position) {

		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		View rowView = convertView;
		ViewHolder viewHolder;
		if (rowView == null) {
			rowView = mInflater.inflate(R.layout.list_item_course2, parent, false);
			viewHolder = new ViewHolder();
			viewHolder.mCourseName = (TextView) rowView.findViewById(R.id.nameTextView);
			viewHolder.mTeacherName = (TextView) rowView.findViewById(R.id.teacherTextView);
			rowView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) rowView.getTag();
		}
		
		Course course = (Course) getItem(position);
		viewHolder.mCourseName.setText(course.getName());
		viewHolder.mTeacherName.setText(course.getTeacher());
		
		return rowView;
	}
	
	public void addCourse(Course course) {
		
		if (course == null) {
			throw new IllegalArgumentException();			
		}
		
		mCourses.add(course);
		
		// Importante: notificar que ha cambiado el dataset
		notifyDataSetChanged();
	}
}
