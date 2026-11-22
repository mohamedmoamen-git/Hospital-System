package hospital.model;

public class Bill {
    private String billId;
    private String patientId;
    private String patientName;
    private String doctorName;
    private String appointmentId;
    private String date;
    private double consultationFee;
    private double medicationCost;
    private double totalAmount;
    private String status; // PENDING, PAID

    public Bill(String billId, String patientId, String patientName, String doctorName,
                String appointmentId, String date, double consultationFee, double medicationCost) {
        this.billId = billId;
        this.patientId = patientId;
        this.patientName = patientName;
        this.doctorName = doctorName;
        this.appointmentId = appointmentId;
        this.date = date;
        this.consultationFee = consultationFee;
        this.medicationCost = medicationCost;
        this.totalAmount = consultationFee + medicationCost;
        this.status = "PENDING";
    }

    public String getBillId() { return billId; }
    public String getPatientId() { return patientId; }
    public String getPatientName() { return patientName; }
    public String getDoctorName() { return doctorName; }
    public String getAppointmentId() { return appointmentId; }
    public String getDate() { return date; }
    public double getConsultationFee() { return consultationFee; }
    public double getMedicationCost() { return medicationCost; }
    public double getTotalAmount() { return totalAmount; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    @Override
    public String toString() {
        return String.format("Bill #%s | %s | Dr. %s | Total: $%.2f | %s",
                billId, patientName, doctorName, totalAmount, status);
    }
}
