package hospital.model;

public class MedicalRecord {
    private String recordId;
    private String patientId;
    private String doctorId;
    private String patientName;
    private String doctorName;
    private String date;
    private String diagnosis;
    private String treatment;
    private String prescription;

    public MedicalRecord(String recordId, String patientId, String doctorId,
                         String patientName, String doctorName, String date,
                         String diagnosis, String treatment, String prescription) {
        this.recordId = recordId;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.patientName = patientName;
        this.doctorName = doctorName;
        this.date = date;
        this.diagnosis = diagnosis;
        this.treatment = treatment;
        this.prescription = prescription;
    }

    public String getRecordId() { return recordId; }
    public String getPatientId() { return patientId; }
    public String getDoctorId() { return doctorId; }
    public String getPatientName() { return patientName; }
    public String getDoctorName() { return doctorName; }
    public String getDate() { return date; }
    public String getDiagnosis() { return diagnosis; }
    public void setDiagnosis(String diagnosis) { this.diagnosis = diagnosis; }
    public String getTreatment() { return treatment; }
    public void setTreatment(String treatment) { this.treatment = treatment; }
    public String getPrescription() { return prescription; }
    public void setPrescription(String prescription) { this.prescription = prescription; }

    @Override
    public String toString() {
        return String.format("Record #%s | Patient: %s | Dr. %s | %s | %s",
                recordId, patientName, doctorName, date, diagnosis);
    }
}
