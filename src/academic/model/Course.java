package academic.model;

/**
 * @author NIM Nama
 * @author NIM Nama
 */
public class Course {

    private String code;
    private String name;
    private int credits;
    private String passingGrade;

    // Konstruktor sesuai permintaan
    public Course(String code, String name, int credits, String passingGrade) {
        this.code = code;
        this.name = name;
        this.credits = credits;
        this.passingGrade = passingGrade;
    }

    // Getter untuk setiap atribut
    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public int getCredits() {
        return credits;
    }

    public String getPassingGrade() {
        return passingGrade;
    }
}