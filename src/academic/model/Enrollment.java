package academic.model;

public interface Enrollment {
    // methods
    String getCourseCode();
    String getStudentId();
    String getAcademicYear();
    String getSemester();
    String getGrade();
    void setGrade(String grade);
    void setRemedial(String remedial);
    String getRemedial();
    String getStatus();
    void setStatus(String status);
    String getRemedialGrade();
    boolean hasRemedial();
}
