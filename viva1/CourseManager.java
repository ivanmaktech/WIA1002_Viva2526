package viva1;

import java.util.ArrayList;

public class CourseManager<T extends Course> {
    private ArrayList<T> courses;

    public CourseManager() {
        this.courses = new ArrayList<>();
    }

    // Add a course to the manager
    public void addCourse(T course) {
        courses.add(course);
    }

    // Remove a course by its unique course code
    public void removeCourse(String courseCode) {
        for (int i=0; i < courses.size(); i++) {
            if (courses.get(i).getCourseCode().equals(courseCode)) {
                courses.remove(i);
                break; // Stop searching after removing (assuming course codes are unique)
            }
        }
    }

    public T getCourseWithHighestWorkload() {
        if (courses.isEmpty()) {
            return null;
        }
        
        // Initialize the highest workload course as the first course in the list
        T highest = courses.get(0);

        // If one of the courses has a higher workload than the current highest workload, update the highest variable
        for (T course : courses) {
            if (course.calculateTotalWorkload() > highest.calculateTotalWorkload()) {
                highest = course;
            }
        }
        return highest;
    }

    public void sortCoursesByWorkload() {
        // Use bubble sort method 
        int n = courses.size();
    
        for (int i=0; i < n-1; i++) {

            for (int j=0; j < n-i-1; j++) {
                int workload1 = courses.get(j).calculateTotalWorkload();
                int workload2 = courses.get(j + 1).calculateTotalWorkload();
                
                // Swap them if the current course (workload1) has a higher workload than the next one (workload2)
                if (workload1 > workload2) {
                    T temp = courses.get(j);
                    courses.set(j, courses.get(j + 1));
                    courses.set(j + 1, temp);
                }
            }
        }
    }

    public void printAllCourses() {
        for (T course : courses) {
            course.printCourseDetails();
            System.out.println();
        }
    }
}
