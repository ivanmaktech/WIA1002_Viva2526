package viva1;

/**Represents a generic university course ADT */

public interface Course {

    /**
     * Gets the unique code for the course.
     * @return String representing the course unique code.  
     */
    String getCourseCode();

    /**
     * Gets the title of the course.
     * @return String representing the course title.  
     */
    String getCourseTitle();

    /**
     * Calculates the total workload hours for the course per semester.
     * @return int representing the total workload hours.
     */
    int calculateTotalWorkload();

    /**
     * Gets the name of the assigned instructor.
     * @return String representing the instructor's name.  
     */
    String getInstructorName();

    /**
     * Prints all relevant information about the course to the console.
     */
    void printCourseDetails();

}