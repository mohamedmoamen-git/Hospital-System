package hospital.service;

import hospital.model.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class HospitalService {
    private List<Admin> admins = new ArrayList<>();
    private List<Doctor> doctors = new ArrayList<>();
    private List<Patient> patients = new ArrayList<>();
    private List<Appointment> appointments = new ArrayList<>();
    private List<MedicalRecord> medicalRecords = new ArrayList<>();
    private List<Bill> bills = new ArrayList<>();

    private int doctorCounter = 1;
    private int patientCounter = 1;
    private int appointmentCounter = 1;
    private int recordCounter = 1;
    private int billCounter = 1;

    public HospitalService() {
        seedData();
    }

    // ─── Seed Demo Data ──────────────────────────────────────────────────────────
    private void seedData() {
        admins.add(new Admin("A001", "Admin", "admin@hospital.com", "admin123", "555-0000"));

        Doctor d1 = new Doctor("D001", "Sarah Mitchell", "smitchell@hospital.com", "doc123",
                "555-0101", "Cardiology", 150.0);
        d1.addSlot("09:00 AM"); d1.addSlot("10:00 AM"); d1.addSlot("02:00 PM");
        doctors.add(d1);

        Doctor d2 = new Doctor("D002", "James Reyes", "jreyes@hospital.com", "doc123",
                "555-0202", "Neurology", 180.0);
        d2.addSlot("11:00 AM"); d2.addSlot("03:00 PM");
        doctors.add(d2);

        Doctor d3 = new Doctor("D003", "Priya Nair", "pnair@hospital.com", "doc123",
                "555-0303", "Pediatrics", 120.0);
        d3.addSlot("08:00 AM"); d3.addSlot("01:00 PM"); d3.addSlot("04:00 PM");
        doctors.add(d3);

        Patient p1 = new Patient("P001", "John Smith", "jsmith@email.com", "pat123",
                "555-1001", 42, "O+", "123 Main St");
        p1.addMedicalHistory("Hypertension diagnosed 2020");
        patients.add(p1);

        Patient p2 = new Patient("P002", "Emily Johnson", "ejohnson@email.com", "pat123",
                "555-1002", 29, "A-", "456 Oak Ave");
        patients.add(p2);

        patientCounter = 3;
        doctorCounter = 4;
    }

    // ─── Auth ────────────────────────────────────────────────────────────────────
    public User login(String email, String password) {
        for (Admin a : admins)
            if (a.getEmail().equals(email) && a.getPassword().equals(password)) return a;
        for (Doctor d : doctors)
            if (d.getEmail().equals(email) && d.getPassword().equals(password)) return d;
        for (Patient p : patients)
            if (p.getEmail().equals(email) && p.getPassword().equals(password)) return p;
        return null;
    }

    // ─── Doctor Management ───────────────────────────────────────────────────────
    public Doctor registerDoctor(String name, String email, String password, String phone,
                                  String specialization, double fee) {
        String id = String.format("D%03d", doctorCounter++);
        Doctor doc = new Doctor(id, name, email, password, phone, specialization, fee);
        doctors.add(doc);
        return doc;
    }

    public boolean removeDoctor(String doctorId) {
        return doctors.removeIf(d -> d.getId().equals(doctorId));
    }

    public List<Doctor> getAllDoctors() { return new ArrayList<>(doctors); }

    public Doctor getDoctorById(String id) {
        return doctors.stream().filter(d -> d.getId().equals(id)).findFirst().orElse(null);
    }

    // ─── Patient Management ──────────────────────────────────────────────────────
    public Patient registerPatient(String name, String email, String password, String phone,
                                    int age, String bloodGroup, String address) {
        String id = String.format("P%03d", patientCounter++);
        Patient patient = new Patient(id, name, email, password, phone, age, bloodGroup, address);
        patients.add(patient);
        return patient;
    }

    public boolean removePatient(String patientId) {
        return patients.removeIf(p -> p.getId().equals(patientId));
    }

    public List<Patient> getAllPatients() { return new ArrayList<>(patients); }

    public Patient getPatientById(String id) {
        return patients.stream().filter(p -> p.getId().equals(id)).findFirst().orElse(null);
    }

    // ─── Appointment Scheduling ──────────────────────────────────────────────────
    public Appointment bookAppointment(String patientId, String doctorId, String date, String timeSlot) {
        Patient patient = getPatientById(patientId);
        Doctor doctor = getDoctorById(doctorId);
        if (patient == null || doctor == null) return null;

        String id = String.format("APT%04d", appointmentCounter++);
        Appointment appt = new Appointment(id, patientId, doctorId,
                patient.getName(), doctor.getName(), date, timeSlot);
        appointments.add(appt);
        doctor.removeSlot(timeSlot);
        return appt;
    }

    public boolean cancelAppointment(String appointmentId) {
        for (Appointment a : appointments) {
            if (a.getAppointmentId().equals(appointmentId)) {
                a.setStatus("CANCELLED");
                Doctor doc = getDoctorById(a.getDoctorId());
                if (doc != null) doc.addSlot(a.getTimeSlot());
                return true;
            }
        }
        return false;
    }

    public boolean completeAppointment(String appointmentId) {
        for (Appointment a : appointments) {
            if (a.getAppointmentId().equals(appointmentId) && a.getStatus().equals("SCHEDULED")) {
                a.setStatus("COMPLETED");
                return true;
            }
        }
        return false;
    }

    public List<Appointment> getAllAppointments() { return new ArrayList<>(appointments); }

    public List<Appointment> getAppointmentsByPatient(String patientId) {
        return appointments.stream().filter(a -> a.getPatientId().equals(patientId))
                .collect(Collectors.toList());
    }

    public List<Appointment> getAppointmentsByDoctor(String doctorId) {
        return appointments.stream().filter(a -> a.getDoctorId().equals(doctorId))
                .collect(Collectors.toList());
    }

    // ─── Medical Records ─────────────────────────────────────────────────────────
    public MedicalRecord addMedicalRecord(String patientId, String doctorId,
                                           String date, String diagnosis,
                                           String treatment, String prescription) {
        Patient patient = getPatientById(patientId);
        Doctor doctor = getDoctorById(doctorId);
        if (patient == null || doctor == null) return null;

        String id = String.format("REC%04d", recordCounter++);
        MedicalRecord record = new MedicalRecord(id, patientId, doctorId,
                patient.getName(), doctor.getName(), date, diagnosis, treatment, prescription);
        medicalRecords.add(record);
        patient.addMedicalHistory(date + " — " + diagnosis);
        return record;
    }

    public List<MedicalRecord> getMedicalRecordsByPatient(String patientId) {
        return medicalRecords.stream().filter(r -> r.getPatientId().equals(patientId))
                .collect(Collectors.toList());
    }

    public List<MedicalRecord> getAllMedicalRecords() { return new ArrayList<>(medicalRecords); }

    // ─── Billing ─────────────────────────────────────────────────────────────────
    public Bill generateBill(String patientId, String appointmentId, double medicationCost) {
        Patient patient = getPatientById(patientId);
        Appointment appt = appointments.stream()
                .filter(a -> a.getAppointmentId().equals(appointmentId))
                .findFirst().orElse(null);
        if (patient == null || appt == null) return null;

        Doctor doctor = getDoctorById(appt.getDoctorId());
        double consultFee = doctor != null ? doctor.getConsultationFee() : 100.0;

        String id = String.format("BILL%04d", billCounter++);
        String date = java.time.LocalDate.now().toString();
        Bill bill = new Bill(id, patientId, patient.getName(), appt.getDoctorName(),
                appointmentId, date, consultFee, medicationCost);
        bills.add(bill);
        return bill;
    }

    public boolean markBillPaid(String billId) {
        for (Bill b : bills) {
            if (b.getBillId().equals(billId)) { b.setStatus("PAID"); return true; }
        }
        return false;
    }

    public List<Bill> getAllBills() { return new ArrayList<>(bills); }

    public List<Bill> getBillsByPatient(String patientId) {
        return bills.stream().filter(b -> b.getPatientId().equals(patientId))
                .collect(Collectors.toList());
    }

    // ─── Stats ───────────────────────────────────────────────────────────────────
    public int getTotalDoctors() { return doctors.size(); }
    public int getTotalPatients() { return patients.size(); }
    public int getTotalAppointments() { return appointments.size(); }
    public long getPendingBillsCount() { return bills.stream().filter(b -> b.getStatus().equals("PENDING")).count(); }
    public double getTotalRevenue() { return bills.stream().filter(b -> b.getStatus().equals("PAID"))
            .mapToDouble(Bill::getTotalAmount).sum(); }
}
