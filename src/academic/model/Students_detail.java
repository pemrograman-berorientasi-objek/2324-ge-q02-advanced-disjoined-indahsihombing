package academic.model;

/**
 * @author 12S22041 Indah Elisa Sihombing
 * 
 */
public class Students_detail extends Student {
    // deklarasi atribut
    
    private float gpa;
    private int credit;

    // constructor 
    // membuat objek Student baru dengan memberikan nilai awal untuk setiap atribut sesuai dengan parameter yang diberikan.
    public Students_detail(String id, String name, int year, String major,float gpa,int credit) {
        super(id, name, year, major);
        this.gpa= gpa;
        this.credit = credit;
    }

    // metode-metode akses (getter) yang digunakan untuk mengambil nilai dari masing-masing atribut Student.
    public float getGpa() {
        return gpa;
    }
    public int getcredit() {
        return credit;
    }
}

