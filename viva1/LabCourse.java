package viva1;

public class LabCourse implements Course {
    private String courseCode;
    private String courseTitle;
    private String instructorName;
    private double labHoursPerWeek;
    private int numberOfSessions;

    public LabCourse(String courseCode, String courseTitle, String instructorName, double labHoursPerWeek, int numberOfSessions) {
        this.courseCode = courseCode;
        this.courseTitle = courseTitle;
        this.instructorName = instructorName;
        this.labHoursPerWeek = labHoursPerWeek;
        this.numberOfSessions = numberOfSessions;
    }

    @Override
    public String getCourseCode() {
        return courseCode;
    }

    @Override
    public String getCourseTitle() {
        return courseTitle;
    }

    // Task 4
    @Override
    public String getInstructorName() {
        return instructorName;
    }

    @Override
    public int calculateTotalWorkload() {
        return (int) (labHoursPerWeek * numberOfSessions * 14);
    }

    @Override
    public void printCourseDetails() {
        System.out.println("Lab Course: " + courseTitle + " (" + courseCode + ")");
        System.out.println("  Instructor: " + instructorName);
        System.out.println("  Lab Hours/Week: " + labHoursPerWeek);
        System.out.println("  Number of Sessions: " + numberOfSessions);
        System.out.println("  Total Workload (Semester): " + calculateTotalWorkload() + " hours");
    }
}