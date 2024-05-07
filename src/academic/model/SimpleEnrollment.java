package academic.model;

public class SimpleEnrollment implements Enrollment {
    // deklarasi
    private String courseCode;
    private String studentId;
    private String academicYear;
    private String semester;
    private String grade;
    private String remedial;
    private String remedialGrade;
    private String status;

    public String getRemedial() {
        return this.remedial;
    }
    public void setGrade(String grade) {
        this.grade = grade;
    }
    public void setRemedial(String remedial) {
        this.remedial = remedial;
    }

    public SimpleEnrollment(String courseCode, String studentId, String academicYear, String semester, String grade) {
        this.courseCode = courseCode;
        this.studentId = studentId;
        this.academicYear = academicYear;
        this.semester = semester;
        this.grade = grade;
        this.remedial = "None";
        this.status = "available";
    }

    @Override
    public String getCourseCode() {
        return courseCode;
    }

    @Override
    public String getStudentId() {
        return studentId;
    }

    @Override
    public String getAcademicYear() {
        return academicYear;
    }

    @Override
    public String getSemester() {
        return semester;
    }

    @Override
    public String getGrade() {
        return grade;
    }

    // @Override
    // public void setGrade(String grade) {
    //     this.grade = grade;
    // }

    // @Override
    // public void setRemedial(String remedial) {
    //     this.remedial = remedial;
    // }

    // @Override
    // public String getRemedial() {
    //     return remedial;
    // }

    @Override
    public String getStatus() {
        return status;
    }

    @Override
    public void setStatus(String status) {
        this.status = status;
    }
    @Override
    public String getRemedialGrade() {
        return remedialGrade;
    }

    @Override
    public boolean hasRemedial() {
        return remedialGrade != null && !remedialGrade.isEmpty();
    }
}
