package hospital.model;

import java.util.ArrayList;
import java.util.List;

public class Patient extends User {
    private int age;
    private String bloodGroup;
    private String address;
    private List<String> medicalHistory;

    public Patient(String id, String name, String email, String password, String phone,
                   int age, String bloodGroup, String address) {
        super(id, name, email, password, phone);
        this.age = age;
        this.bloodGroup = bloodGroup;
        this.address = address;
        this.medicalHistory = new ArrayList<>();
    }

    @Override
    public String getRole() { return "Patient"; }

    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }
    public String getBloodGroup() { return bloodGroup; }
    public void setBloodGroup(String bloodGroup) { this.bloodGroup = bloodGroup; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public List<String> getMedicalHistory() { return medicalHistory; }
    public void addMedicalHistory(String entry) { medicalHistory.add(entry); }

    @Override
    public String toString() {
        return String.format("%s | Age: %d | Blood: %s", name, age, bloodGroup);
    }
}
