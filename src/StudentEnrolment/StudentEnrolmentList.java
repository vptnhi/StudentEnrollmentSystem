package StudentEnrolment;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class StudentEnrolmentList implements StudentEnrolmentManager {

    static private int enrolmentIDCounter = 0;
    private ArrayList<StudentEnrolment> enrolmentList;
    private StudentList studentList;
    private CourseList courseList;

    public StudentEnrolmentList(String filename, StudentList studentList, CourseList courseList) throws IOException, ParseException {
        this.studentList = studentList;
        this.courseList = courseList;
        this.enrolmentList = new ArrayList<>();
        SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        String line;
        BufferedReader br = new BufferedReader(new FileReader(filename));
        while ((line = br.readLine()) != null) {
            String[] split = line.split(",", 7); // Retrieve each part of the student information , assuming each field is separated with a "," and each line is new entry.
            Student s = new Student(split[0], split[1], df.parse(split[2]));
            Course c = new Course(split[3], split[4], Integer.parseInt(split[5]));
            if (this.studentList.findByID(split[0]) == null) {
                this.studentList.addStudent(s);
            }
            if (this.courseList.findByID(split[3]) == null) {
                this.courseList.addCourse(c);
            }
//            if (!this.studentList.contains(s)) {
//              this.studentList.addStudent(new Student(split[0], split[1], df.parse(split[2])));
//            }
//            this.courseList.addCourse(c);
            enrolmentIDCounter++;
            this.enrolmentList.add(new StudentEnrolment(enrolmentIDCounter, s, c, split[6]));
//            System.out.println("0 " + split[0] + " 1 " + split[1] + " 2 " + split[2] + " 3 " +
//            split[3] + " 4 " + split[4] + " 5 " + split[5] + " 6 " + split[6]);
        }
    }

//    public StudentEnrolmentList(CourseList courseList, StudentList studentList) {
////        this.enrolmentList = new ArrayList<>();
//        this.courseList = courseList;
//        this.studentList = studentList;
//    }

    public static int getEnrolmentIDCounter() {
        return enrolmentIDCounter;
    }

    public static void setEnrolmentIDCounter(int enrolmentIDCounter) {
        StudentEnrolmentList.enrolmentIDCounter = enrolmentIDCounter;
    }

    public void addStudent(Student s) {
        this.studentList.addStudent(s);
    }

    public void addCourse(Course c) {
        this.courseList.addCourse(c);
    }

    public Student findStudentByID(String id) {
        return this.studentList.findByID(id);
    }

    public Course findCourseByID(String id) {
        return this.courseList.findByID(id);
    }

    public boolean isDuplicate(String studentID, String courseID) {
        for (StudentEnrolment enrolment : enrolmentList) {
            if (enrolment.getStudent().getStudentID().equals(studentID)
                    && enrolment.getCourse().getCourseID().equals(courseID)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean add(Student student, Course course, String semester) {
        enrolmentIDCounter++;
        StudentEnrolment enrolment = new StudentEnrolment(enrolmentIDCounter, student, course, semester);
        enrolmentList.add(enrolment);
        return true;
    }

    @Override
    public boolean update(String studentID, String courseID, String semester) {
        StudentEnrolment enrolment = getOne(studentID, courseID);
        if (isDuplicate(studentID, courseID)) {
            return false;
        }
        enrolment.setStudent(findStudentByID(studentID));
        enrolment.setCourse(findCourseByID(courseID));
        enrolment.setSemester(semester);
        return true;
    }

    @Override
    public boolean delete(int id) {
        for (StudentEnrolment enrolment : enrolmentList) {
            if (enrolment.getId() == id) {
                enrolmentList.remove(enrolment);
                return true;
            }
        }
        return false;

    }

    @Override
    public StudentEnrolment getOne(String studentID, String courseID) {
        for (StudentEnrolment enrolment : enrolmentList) {
            if (enrolment.getStudent().getStudentID().equals(studentID)
                    && enrolment.getCourse().getCourseID().equals(courseID)) {
                return enrolment;
            }
        }
        return null;
    }

    @Override
    public void getAll() {
        for (StudentEnrolment enrolment : enrolmentList) {
            System.out.println(enrolment.toString());
        }
    }

    public ArrayList<StudentEnrolment> getEnrolmentList() {
        return enrolmentList;
    }

    public void setEnrolmentList(ArrayList<StudentEnrolment> enrolmentList) {
        this.enrolmentList = enrolmentList;
    }

    public void getStudentList() {
        this.studentList.getStudentList();
    }

    public void getCourseList() {
        this.courseList.getCourseList();
    }
}
