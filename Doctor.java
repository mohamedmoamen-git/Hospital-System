package hospital.model;

import java.util.ArrayList;
import java.util.List;

public class Doctor extends User {
    private String specialization;
    private List<String> availableSlots;
    private double consultationFee;

    public Doctor(String id, String name, String email, String password, String phone,
                  String specialization, double consultationFee) {
        super(id, name, email, password, phone);
        this.specialization = specialization;
        this.consultationFee = consultationFee;
        this.availableSlots = new ArrayList<>();
    }

    @Override
    public String getRole() { return "Doctor"; }

    public String getSpecialization() { return specialization; }
    public void setSpecialization(String specialization) { this.specialization = specialization; }
    public double getConsultationFee() { return consultationFee; }
    public void setConsultationFee(double consultationFee) { this.consultationFee = consultationFee; }
    public List<String> getAvailableSlots() { return availableSlots; }

    public void addSlot(String slot) { availableSlots.add(slot); }
    public void removeSlot(String slot) { availableSlots.remove(slot); }

    @Override
    public String toString() {
        return String.format("Dr. %s | %s | Fee: $%.2f", name, specialization, consultationFee);
    }
}
