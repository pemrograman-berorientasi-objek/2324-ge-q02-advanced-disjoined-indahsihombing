package academic.model;
import java.util.List;

/**
 * @author NIM Nama
 * @author NIM Nama
 */
public class CourseOpening {

    private String courseCode;
    private String academicYear;
    private String semester;
    private List<Lecturer> lecturers;

    public CourseOpening(String courseCode, String academicYear, String semester, List<Lecturer> lecturers) {
        this.courseCode = courseCode;
        this.academicYear = academicYear;
        this.semester = semester;
        this.lecturers = lecturers;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public String getAcademicYear() {
        return academicYear;
    }

    public String getSemester() {
        return semester;
    }

    public List<Lecturer> getLecturers() {
        return lecturers;
    }
}