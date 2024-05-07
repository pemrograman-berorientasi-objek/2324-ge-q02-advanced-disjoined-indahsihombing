package academic.model;

/**
 * @author NIM Nama
 * @author NIM Nama
 */

public class BestStudent {
    private String academicYear;
    private String semester;

    public BestStudent(String academicYear, String semester) {
        this.academicYear = academicYear;
        this.semester = semester;

}

public String getAcademicYear() {
    return academicYear;
}


public String getSemester() {
    return semester;
}

}