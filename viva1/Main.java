package viva1;

public class Main {
    public static void main(String[] args) {
        
        //1. Create two LectureCourse objects and two LabCourse objects
        LectureCourse lec1 = new LectureCourse("WIX1001", "Computing Mathematics", "Dr. Ali", 4);
        LectureCourse lec2 = new LectureCourse("WIX1002", "Fundamentals of Programming", "Prof. Asmizah", 5);
        
        LabCourse lab1 = new LabCourse("WIX1002b", "Fundamentals of Programming Lab","Prof. Akmal", 2.0, 2);
        LabCourse lab2 = new LabCourse("WIX1003", "Computer System and Organisation", "Dr. Brown", 3.0, 1);

        //2. Create a CourseManager instance
        CourseManager<Course> manager = new CourseManager<>();

        //3. Add all the created courses to the manager
        System.out.println("Adding Courses...");
        manager.addCourse(lec1);
        manager.addCourse(lec2);
        manager.addCourse(lab1);
        manager.addCourse(lab2);
        System.out.println("Successfully added all courses to the manager.");
        System.out.println();

        //4. Print the details of the course with the highest total workload
        System.out.println("Course with Highest Workload: ");
        Course highest = manager.getCourseWithHighestWorkload();
        if (highest != null) {
            highest.printCourseDetails(); }
        System.out.println();

        //5. Sort the courses by their total workload and print all course details
        System.out.println("All Courses Sorted by Workload (Ascending): ");
        manager.sortCoursesByWorkload();
        manager.printAllCourses();

        //6. Remove a course by its course code and print the remaining course details
        System.out.println("Removing Course WIX1001");
        manager.removeCourse("WIX1001");
        System.out.println("Remaining Courses:");
        manager.printAllCourses();
    }
}