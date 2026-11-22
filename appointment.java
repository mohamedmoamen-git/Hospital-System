package hospital.model;

public class Appointment {
    private String appointmentId;
    private String patientId;
    private String doctorId;
    private String patientName;
    private String doctorName;
    private String date;
    private String timeSlot;
    private String status; // SCHEDULED, COMPLETED, CANCELLED
    private String notes;

    public Appointment(String appointmentId, String patientId, String doctorId,
                       String patientName, String doctorName, String date, String timeSlot) {
        this.appointmentId = appointmentId;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.patientName = patientName;
        this.doctorName = doctorName;
        this.date = date;
        this.timeSlot = timeSlot;
        this.status = "SCHEDULED";
        this.notes = "";
    }

    public String getAppointmentId() { return appointmentId; }
    public String getPatientId() { return patientId; }
    public String getDoctorId() { return doctorId; }
    public String getPatientName() { return patientName; }
    public String getDoctorName() { return doctorName; }
    public String getDate() { return date; }
    public String getTimeSlot() { return timeSlot; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    @Override
    public String toString() {
        return String.format("Appt #%s | Patient: %s | Dr. %s | %s %s | %s",
                appointmentId, patientName, doctorName, date, timeSlot, status);
    }
}
