package viva1;

  public class LectureCourse implements Course { 
        private String courseCode; 
        private String courseTitle; 
        private String instructorName; 
        private int lectureHoursPerWeek; 
 
        public LectureCourse(String courseCode, String courseTitle, String instructorName, int 
                             lectureHoursPerWeek)  
       { 
            this.courseCode = courseCode; 
            this.courseTitle = courseTitle; 
            this.instructorName = instructorName; 
            this.lectureHoursPerWeek = lectureHoursPerWeek; 
        } 
 
        @Override 
        public String getCourseCode() { 
            return courseCode; 
        } 
 
        @Override 
        public String getCourseTitle() { 
            return courseTitle; 
        } 
 
        @Override 
        public int calculateTotalWorkload() { 
            return lectureHoursPerWeek * 14; 
        } 
 
        @Override 
        public String getInstructorName() { 
            return instructorName; 
        } 
 
        @Override 
        public void printCourseDetails() { 
            System.out.println("Lecture Course: " + courseTitle + " (" + courseCode + ")"); 
            System.out.println("  Instructor: " + instructorName); 
            System.out.println("  Lecture Hours/Week: " + lectureHoursPerWeek); 
            System.out.println("  Total Workload (Semester): " + calculateTotalWorkload() + "hours"); 
        } 
    }