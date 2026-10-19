package student;

public class Student {

    private final String studentId;
    private final String firstName;
    private final String surname;
    private final double score;
    private final String grade;

    public Student(String studentId, String firstName, String surname, double score) {
        this.studentId = studentId;
        this.firstName = firstName;
        this.surname = surname;
        this.score = score;
        this.grade = calculateGrade(score);
    }

    
    public static String calculateGrade(double score) {
        if (score >= 80 && score <= 100) {
            return "A";
        } else if (score >= 70) {
            return "B";
        } else if (score >= 60) {
            return "C";
        } else if (score >= 50) {
            return "D";
        } else {
            return "F";
        }
    }

    public String getStudentId() {
        return studentId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getSurname() {
        return surname;
    }

    public double getScore() {
        return score;
    }

    public String getGrade() {
        return grade;
    }

    
    public String toCsvLine() {
        return studentId + "," + firstName + "," + surname + "," + score + "," + grade;
    }

    @Override
    public String toString() {
        return toCsvLine();
    }
}
