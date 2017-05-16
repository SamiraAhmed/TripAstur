package es.uniovi.imovil.jcgranda.courses;

public class Course {
	
	private String mName;
	private String mTeacher;
	private String mDescription;
	
	public Course(String name, String teacher, String description) {
		
		if (name == null || teacher == null || name.isEmpty() || teacher.isEmpty()) {
			throw new IllegalArgumentException();
		}
		
		mName = name;
		mTeacher = teacher;
		mDescription = description;
	}

	public String getName() {
		
		return mName;
	}

	public String getTeacher() {
		
		return mTeacher;
	}
	
	public String getDescription() {
		
		return mDescription;
	}	
}
