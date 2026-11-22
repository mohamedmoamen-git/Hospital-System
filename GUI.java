package hospital.gui;

import hospital.model.*;
import hospital.service.HospitalService;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class HospitalApp extends JFrame {

    // ── Palette ──────────────────────────────────────────────────────────────────
    static final Color BG = new Color(0xF0F4F8);
    static final Color SIDEBAR_BG = new Color(0x1A2E4A);
    static final Color SIDEBAR_SEL = new Color(0x2E4D7A);
    static final Color ACCENT = new Color(0x2196F3);
    static final Color ACCENT2 = new Color(0x00BCD4);
    static final Color SUCCESS = new Color(0x4CAF50);
    static final Color WARNING = new Color(0xFF9800);
    static final Color DANGER = new Color(0xF44336);
    static final Color TEXT_DARK = new Color(0x1A2E4A);
    static final Color TEXT_MID = new Color(0x546E7A);
    static final Color TEXT_LIGHT = new Color(0xECEFF1);
    static final Color CARD_BG = Color.WHITE;
    static final Color TABLE_HEAD = new Color(0xE3F2FD);

    static final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 22);
    static final Font FONT_HEADER = new Font("Segoe UI", Font.BOLD, 14);
    static final Font FONT_BODY = new Font("Segoe UI", Font.PLAIN, 13);
    static final Font FONT_SMALL = new Font("Segoe UI", Font.PLAIN, 11);
    static final Font FONT_MONO = new Font("Consolas", Font.PLAIN, 12);

    private HospitalService service = new HospitalService();
    private User currentUser;

    // Layout areas
    private JPanel sidebarPanel;
    private JPanel contentPanel;
    private CardLayout contentLayout;

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            /* ignore */ }
        SwingUtilities.invokeLater(() -> new HospitalApp().showLogin());
    }

    public HospitalApp() {
        setTitle("MediCare — Hospital Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 720);
        setMinimumSize(new Dimension(900, 600));
        setLocationRelativeTo(null);
    }

    // ════════════════════════════════════════════════════════════════════════════
    // LOGIN
    // ════════════════════════════════════════════════════════════════════════════
    void showLogin() {
        getContentPane().removeAll();
        setTitle("MediCare — Login");

        JPanel root = new JPanel(new GridBagLayout());
        root.setBackground(new Color(0x0D1B2A));

        // Background subtle pattern
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(0xF8FAFB));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
            }
        };
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setOpaque(false);
        card.setBorder(BorderFactory.createEmptyBorder(40, 50, 40, 50));
        card.setPreferredSize(new Dimension(420, 480));

        // Logo area
        JLabel logo = new JLabel("🏥 MediCare", SwingConstants.CENTER);
        logo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        logo.setForeground(ACCENT);
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel sub = new JLabel("Hospital Management System", SwingConstants.CENTER);
        sub.setFont(FONT_BODY);
        sub.setForeground(TEXT_MID);
        sub.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel loginLabel = new JLabel("Sign in to your account");
        loginLabel.setFont(FONT_HEADER);
        loginLabel.setForeground(TEXT_DARK);
        loginLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JTextField emailField = styledTextField("Email address");
        JPasswordField passField = new JPasswordField();
        passField.setFont(FONT_BODY);
        passField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0xCFD8DC), 1, true),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)));
        passField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        JLabel passLabel = new JLabel("Password");
        passLabel.setFont(FONT_SMALL);
        passLabel.setForeground(TEXT_MID);
        passLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel emailLabel = new JLabel("Email");
        emailLabel.setFont(FONT_SMALL);
        emailLabel.setForeground(TEXT_MID);
        emailLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton loginBtn = accentButton("Login", ACCENT);
        loginBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));

        JLabel hint = new JLabel(
                "Demo: admin@hospital.com / admin123  |  doc / pat: ...@hospital.com / doc123 / pat123");
        hint.setFont(new Font("Segoe UI", Font.ITALIC, 10));
        hint.setForeground(TEXT_MID);
        hint.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel errLabel = new JLabel(" ");
        errLabel.setFont(FONT_SMALL);
        errLabel.setForeground(DANGER);
        errLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        loginBtn.addActionListener(e -> {
            String email = emailField.getText().trim();
            String pass = new String(passField.getPassword());
            User user = service.login(email, pass);
            if (user == null) {
                errLabel.setText("Invalid email or password.");
            } else {
                currentUser = user;
                showDashboard();
            }
        });

        // Allow Enter key
        ActionListener loginAction = e -> loginBtn.doClick();
        emailField.addActionListener(loginAction);
        passField.addActionListener(loginAction);

        card.add(logo);
        card.add(Box.createVerticalStrut(4));
        card.add(sub);
        card.add(Box.createVerticalStrut(30));
        card.add(loginLabel);
        card.add(Box.createVerticalStrut(20));
        card.add(emailLabel);
        card.add(Box.createVerticalStrut(4));
        card.add(emailField);
        card.add(Box.createVerticalStrut(14));
        card.add(passLabel);
        card.add(Box.createVerticalStrut(4));
        card.add(passField);
        card.add(Box.createVerticalStrut(20));
        card.add(loginBtn);
        card.add(Box.createVerticalStrut(10));
        card.add(errLabel);
        card.add(Box.createVerticalStrut(16));
        card.add(hint);

        root.add(card);
        setContentPane(root);
        revalidate();
        repaint();
        setVisible(true);
    }

    // ════════════════════════════════════════════════════════════════════════════
    // MAIN DASHBOARD SHELL
    // ════════════════════════════════════════════════════════════════════════════
    void showDashboard() {
        setTitle("MediCare — " + currentUser.getRole() + " Dashboard");
        getContentPane().removeAll();

        JPanel root = new JPanel(new BorderLayout());

        // Top bar
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(CARD_BG);
        topBar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(0xE0E0E0)),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)));
        topBar.setPreferredSize(new Dimension(0, 55));

        JLabel titleLabel = new JLabel("MediCare");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(ACCENT);

        JPanel userInfo = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        userInfo.setOpaque(false);
        JLabel userLabel = new JLabel(currentUser.getName() + " (" + currentUser.getRole() + ")");
        userLabel.setFont(FONT_BODY);
        userLabel.setForeground(TEXT_DARK);
        JButton logoutBtn = new JButton("Logout");
        logoutBtn.setFont(FONT_SMALL);
        logoutBtn.setForeground(DANGER);
        logoutBtn.setBackground(Color.WHITE);
        logoutBtn.setBorder(BorderFactory.createLineBorder(DANGER, 1, true));
        logoutBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        logoutBtn.addActionListener(e -> {
            currentUser = null;
            showLogin();
        });
        userInfo.add(userLabel);
        userInfo.add(logoutBtn);

        topBar.add(titleLabel, BorderLayout.WEST);
        topBar.add(userInfo, BorderLayout.EAST);

        // Sidebar
        sidebarPanel = new JPanel();
        sidebarPanel.setLayout(new BoxLayout(sidebarPanel, BoxLayout.Y_AXIS));
        sidebarPanel.setBackground(SIDEBAR_BG);
        sidebarPanel.setPreferredSize(new Dimension(200, 0));
        sidebarPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        // Content
        contentLayout = new CardLayout();
        contentPanel = new JPanel(contentLayout);
        contentPanel.setBackground(BG);

        // Build panels
        buildPanels();

        // Sidebar items
        String role = currentUser.getRole();
        String[] items;
        if (role.equals("Admin")) {
            items = new String[] { "Dashboard", "Doctors", "Patients", "Appointments", "Records", "Billing" };
        } else if (role.equals("Doctor")) {
            items = new String[] { "Dashboard", "My Appointments", "Medical Records", "Prescriptions" };
        } else {
            items = new String[] { "Dashboard", "Book Appointment", "My Appointments", "My Records", "My Bills" };
        }

        JLabel[] sideItems = new JLabel[items.length];
        for (int i = 0; i < items.length; i++) {
            final String item = items[i];
            JLabel lbl = new JLabel(sidebarIcon(item) + "  " + item);
            lbl.setFont(FONT_BODY);
            lbl.setForeground(TEXT_LIGHT);
            lbl.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));
            lbl.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
            lbl.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            lbl.setOpaque(true);
            lbl.setBackground(i == 0 ? SIDEBAR_SEL : SIDEBAR_BG);
            sideItems[i] = lbl;

            lbl.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    for (JLabel s : sideItems)
                        s.setBackground(SIDEBAR_BG);
                    lbl.setBackground(SIDEBAR_SEL);
                    contentLayout.show(contentPanel, item);
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    if (!lbl.getBackground().equals(SIDEBAR_SEL))
                        lbl.setBackground(new Color(0x243D5E));
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    if (!lbl.getBackground().equals(SIDEBAR_SEL))
                        lbl.setBackground(SIDEBAR_BG);
                }
            });
            sidebarPanel.add(lbl);
        }

        JPanel body = new JPanel(new BorderLayout());
        body.add(sidebarPanel, BorderLayout.WEST);
        body.add(contentPanel, BorderLayout.CENTER);

        root.add(topBar, BorderLayout.NORTH);
        root.add(body, BorderLayout.CENTER);

        setContentPane(root);
        revalidate();
        repaint();
    }

    // ════════════════════════════════════════════════════════════════════════════
    // BUILD ALL CONTENT PANELS
    // ════════════════════════════════════════════════════════════════════════════
    private void buildPanels() {
        String role = currentUser.getRole();

        if (role.equals("Admin")) {
            contentPanel.add(buildAdminDashboard(), "Dashboard");
            contentPanel.add(buildDoctorsPanel(), "Doctors");
            contentPanel.add(buildPatientsPanel(), "Patients");
            contentPanel.add(buildAppointmentsPanel(), "Appointments");
            contentPanel.add(buildMedicalRecordsPanel(), "Records");
            contentPanel.add(buildBillingPanel(), "Billing");
        } else if (role.equals("Doctor")) {
            contentPanel.add(buildDoctorDashboard(), "Dashboard");
            contentPanel.add(buildDoctorAppointmentsPanel(), "My Appointments");
            contentPanel.add(buildDoctorRecordsPanel(), "Medical Records");
            contentPanel.add(buildPrescriptionsPanel(), "Prescriptions");
        } else {
            contentPanel.add(buildPatientDashboard(), "Dashboard");
            contentPanel.add(buildBookAppointmentPanel(), "Book Appointment");
            contentPanel.add(buildPatientAppointmentsPanel(), "My Appointments");
            contentPanel.add(buildPatientRecordsPanel(), "My Records");
            contentPanel.add(buildPatientBillsPanel(), "My Bills");
        }

        contentLayout.show(contentPanel, "Dashboard");
    }

    // ════════════════════════════════════════════════════════════════════════════
    // ADMIN DASHBOARD
    // ════════════════════════════════════════════════════════════════════════════
    private JPanel buildAdminDashboard() {
        JPanel p = scrollPane();
        JPanel inner = new JPanel();
        inner.setLayout(new BoxLayout(inner, BoxLayout.Y_AXIS));
        inner.setOpaque(false);
        inner.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        inner.add(sectionHeader("Admin Dashboard"));
        inner.add(Box.createVerticalStrut(20));

        // Stats row
        JPanel stats = new JPanel(new GridLayout(1, 4, 16, 0));
        stats.setOpaque(false);
        stats.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        stats.add(statCard("Doctors", String.valueOf(service.getTotalDoctors()), ACCENT));
        stats.add(statCard("Patients", String.valueOf(service.getTotalPatients()), SUCCESS));
        stats.add(statCard("Appointments", String.valueOf(service.getTotalAppointments()), WARNING));
        stats.add(statCard("Revenue", String.format("$%.0f", service.getTotalRevenue()), ACCENT2));
        inner.add(stats);
        inner.add(Box.createVerticalStrut(30));

        // Recent appointments table
        inner.add(sectionHeader("Recent Appointments"));
        inner.add(Box.createVerticalStrut(10));
        String[] cols = { "ID", "Patient", "Doctor", "Date", "Time", "Status" };
        List<Appointment> appts = service.getAllAppointments();
        Object[][] data = new Object[appts.size()][6];
        for (int i = 0; i < appts.size(); i++) {
            Appointment a = appts.get(i);
            data[i] = new Object[] { a.getAppointmentId(), a.getPatientName(),
                    "Dr. " + a.getDoctorName(), a.getDate(), a.getTimeSlot(), a.getStatus() };
        }
        inner.add(styledTable(cols, data));

        p.add(inner);
        return p;
    }

    // ════════════════════════════════════════════════════════════════════════════
    // DOCTORS PANEL (ADMIN)
    // ════════════════════════════════════════════════════════════════════════════
    private JPanel buildDoctorsPanel() {
        JPanel p = scrollPane();
        JPanel inner = new JPanel();
        inner.setLayout(new BoxLayout(inner, BoxLayout.Y_AXIS));
        inner.setOpaque(false);
        inner.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        inner.add(sectionHeader("Doctor Management"));
        inner.add(Box.createVerticalStrut(14));

        // Add doctor form
        JPanel formCard = card("Register New Doctor");
        JPanel form = new JPanel(new GridLayout(3, 4, 10, 10));
        form.setOpaque(false);
        JTextField nameF = styledTextField("Full Name");
        JTextField emailF = styledTextField("Email");
        JTextField passF = styledTextField("Password");
        JTextField phoneF = styledTextField("Phone");
        JTextField specF = styledTextField("Specialization");
        JTextField feeF = styledTextField("Consultation Fee");
        JButton addBtn = accentButton("Add Doctor", SUCCESS);
        addBtn.addActionListener(e -> {
            try {
                Doctor doc = service.registerDoctor(nameF.getText(), emailF.getText(),
                        passF.getText(), phoneF.getText(), specF.getText(),
                        Double.parseDouble(feeF.getText()));
                JOptionPane.showMessageDialog(this, "Doctor registered: " + doc.getId(), "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                refreshPanel("Doctors");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });
        form.add(nameF);
        form.add(emailF);
        form.add(passF);
        form.add(phoneF);
        form.add(specF);
        form.add(feeF);
        form.add(new JLabel());
        form.add(addBtn);
        formCard.add(form, BorderLayout.CENTER);
        inner.add(formCard);
        inner.add(Box.createVerticalStrut(20));

        // Table
        inner.add(sectionHeader("All Doctors"));
        inner.add(Box.createVerticalStrut(10));
        String[] cols = { "ID", "Name", "Specialization", "Email", "Phone", "Fee", "Slots" };
        List<Doctor> docs = service.getAllDoctors();
        Object[][] data = new Object[docs.size()][7];
        for (int i = 0; i < docs.size(); i++) {
            Doctor d = docs.get(i);
            data[i] = new Object[] { d.getId(), "Dr. " + d.getName(), d.getSpecialization(),
                    d.getEmail(), d.getPhone(), "$" + d.getConsultationFee(),
                    d.getAvailableSlots().size() + " available" };
        }
        inner.add(styledTable(cols, data));
        p.add(inner);
        return p;
    }

    // ════════════════════════════════════════════════════════════════════════════
    // PATIENTS PANEL (ADMIN)
    // ════════════════════════════════════════════════════════════════════════════
    private JPanel buildPatientsPanel() {
        JPanel p = scrollPane();
        JPanel inner = new JPanel();
        inner.setLayout(new BoxLayout(inner, BoxLayout.Y_AXIS));
        inner.setOpaque(false);
        inner.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        inner.add(sectionHeader("Patient Management"));
        inner.add(Box.createVerticalStrut(14));

        JPanel formCard = card("Register New Patient");
        JPanel form = new JPanel(new GridLayout(3, 4, 10, 10));
        form.setOpaque(false);
        JTextField nameF = styledTextField("Full Name");
        JTextField emailF = styledTextField("Email");
        JTextField passF = styledTextField("Password");
        JTextField phoneF = styledTextField("Phone");
        JTextField ageF = styledTextField("Age");
        JTextField bgF = styledTextField("Blood Group");
        JTextField addrF = styledTextField("Address");
        JButton addBtn = accentButton("Register Patient", SUCCESS);
        addBtn.addActionListener(e -> {
            try {
                Patient pat = service.registerPatient(nameF.getText(), emailF.getText(),
                        passF.getText(), phoneF.getText(), Integer.parseInt(ageF.getText()),
                        bgF.getText(), addrF.getText());
                JOptionPane.showMessageDialog(this, "Patient registered: " + pat.getId(), "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                refreshPanel("Patients");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        form.add(nameF);
        form.add(emailF);
        form.add(passF);
        form.add(phoneF);
        form.add(ageF);
        form.add(bgF);
        form.add(addrF);
        form.add(addBtn);
        formCard.add(form, BorderLayout.CENTER);
        inner.add(formCard);
        inner.add(Box.createVerticalStrut(20));

        inner.add(sectionHeader("All Patients"));
        inner.add(Box.createVerticalStrut(10));
        String[] cols = { "ID", "Name", "Age", "Blood Group", "Email", "Phone", "Address" };
        List<Patient> patients = service.getAllPatients();
        Object[][] data = new Object[patients.size()][7];
        for (int i = 0; i < patients.size(); i++) {
            Patient pt = patients.get(i);
            data[i] = new Object[] { pt.getId(), pt.getName(), pt.getAge(), pt.getBloodGroup(),
                    pt.getEmail(), pt.getPhone(), pt.getAddress() };
        }
        inner.add(styledTable(cols, data));
        p.add(inner);
        return p;
    }

    // ════════════════════════════════════════════════════════════════════════════
    // APPOINTMENTS PANEL (ADMIN)
    // ════════════════════════════════════════════════════════════════════════════
    private JPanel buildAppointmentsPanel() {
        JPanel p = scrollPane();
        JPanel inner = new JPanel();
        inner.setLayout(new BoxLayout(inner, BoxLayout.Y_AXIS));
        inner.setOpaque(false);
        inner.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        inner.add(sectionHeader("Appointment Management"));
        inner.add(Box.createVerticalStrut(14));

        // Action card
        JPanel actionCard = card("Actions");
        JPanel actionRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        actionRow.setOpaque(false);
        JTextField apptIdField = styledTextField("Appointment ID");
        apptIdField.setPreferredSize(new Dimension(160, 36));
        JButton completeBtn = accentButton("Mark Complete", SUCCESS);
        JButton cancelBtn = accentButton("Cancel Appt", DANGER);
        completeBtn.addActionListener(e -> {
            boolean ok = service.completeAppointment(apptIdField.getText().trim());
            JOptionPane.showMessageDialog(this, ok ? "Appointment completed." : "Not found / already done.",
                    ok ? "Done" : "Error", ok ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE);
            refreshPanel("Appointments");
        });
        cancelBtn.addActionListener(e -> {
            boolean ok = service.cancelAppointment(apptIdField.getText().trim());
            JOptionPane.showMessageDialog(this, ok ? "Appointment cancelled." : "Not found.",
                    ok ? "Done" : "Error", ok ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE);
            refreshPanel("Appointments");
        });
        actionRow.add(new JLabel("Appt ID:"));
        actionRow.add(apptIdField);
        actionRow.add(completeBtn);
        actionRow.add(cancelBtn);
        actionCard.add(actionRow, BorderLayout.CENTER);
        inner.add(actionCard);
        inner.add(Box.createVerticalStrut(20));

        String[] cols = { "ID", "Patient", "Doctor", "Date", "Time Slot", "Status", "Notes" };
        List<Appointment> appts = service.getAllAppointments();
        Object[][] data = new Object[appts.size()][7];
        for (int i = 0; i < appts.size(); i++) {
            Appointment a = appts.get(i);
            data[i] = new Object[] { a.getAppointmentId(), a.getPatientName(),
                    "Dr. " + a.getDoctorName(), a.getDate(), a.getTimeSlot(), a.getStatus(), a.getNotes() };
        }
        inner.add(styledTable(cols, data));
        p.add(inner);
        return p;
    }

    // ════════════════════════════════════════════════════════════════════════════
    // MEDICAL RECORDS PANEL (ADMIN)
    // ════════════════════════════════════════════════════════════════════════════
    private JPanel buildMedicalRecordsPanel() {
        JPanel p = scrollPane();
        JPanel inner = new JPanel();
        inner.setLayout(new BoxLayout(inner, BoxLayout.Y_AXIS));
        inner.setOpaque(false);
        inner.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        inner.add(sectionHeader("Medical Records"));
        inner.add(Box.createVerticalStrut(14));

        String[] cols = { "Record ID", "Patient", "Doctor", "Date", "Diagnosis", "Treatment" };
        List<MedicalRecord> recs = service.getAllMedicalRecords();
        Object[][] data = new Object[recs.size()][6];
        for (int i = 0; i < recs.size(); i++) {
            MedicalRecord r = recs.get(i);
            data[i] = new Object[] { r.getRecordId(), r.getPatientName(), "Dr. " + r.getDoctorName(),
                    r.getDate(), r.getDiagnosis(), r.getTreatment() };
        }
        inner.add(styledTable(cols, data));
        p.add(inner);
        return p;
    }

    // ════════════════════════════════════════════════════════════════════════════
    // BILLING PANEL (ADMIN)
    // ════════════════════════════════════════════════════════════════════════════
    private JPanel buildBillingPanel() {
        JPanel p = scrollPane();
        JPanel inner = new JPanel();
        inner.setLayout(new BoxLayout(inner, BoxLayout.Y_AXIS));
        inner.setOpaque(false);
        inner.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        inner.add(sectionHeader("Billing & Invoices"));
        inner.add(Box.createVerticalStrut(14));

        // Generate bill form
        JPanel formCard = card("Generate Invoice");
        JPanel form = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        form.setOpaque(false);
        JTextField patIdF = styledTextField("Patient ID");
        JTextField apptIdF = styledTextField("Appointment ID");
        JTextField medCostF = styledTextField("Medication Cost");
        JButton genBtn = accentButton("Generate Bill", ACCENT);
        genBtn.addActionListener(e -> {
            try {
                Bill b = service.generateBill(patIdF.getText().trim(), apptIdF.getText().trim(),
                        Double.parseDouble(medCostF.getText().trim()));
                if (b == null)
                    JOptionPane.showMessageDialog(this, "Patient or Appointment not found.", "Error",
                            JOptionPane.ERROR_MESSAGE);
                else {
                    JOptionPane.showMessageDialog(this, "Bill generated: " + b.getBillId() +
                            "\nTotal: $" + b.getTotalAmount(), "Invoice Created", JOptionPane.INFORMATION_MESSAGE);
                    refreshPanel("Billing");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        JTextField billIdF = styledTextField("Bill ID to mark paid");
        JButton payBtn = accentButton("Mark Paid", SUCCESS);
        payBtn.addActionListener(e -> {
            boolean ok = service.markBillPaid(billIdF.getText().trim());
            JOptionPane.showMessageDialog(this, ok ? "Bill marked as PAID." : "Bill not found.",
                    ok ? "Done" : "Error", ok ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE);
            refreshPanel("Billing");
        });

        form.add(new JLabel("Patient ID:"));
        form.add(patIdF);
        form.add(new JLabel("Appt ID:"));
        form.add(apptIdF);
        form.add(new JLabel("Med Cost:"));
        form.add(medCostF);
        form.add(genBtn);
        form.add(Box.createHorizontalStrut(20));
        form.add(new JLabel("Bill ID:"));
        form.add(billIdF);
        form.add(payBtn);
        formCard.add(form, BorderLayout.CENTER);
        inner.add(formCard);
        inner.add(Box.createVerticalStrut(20));

        String[] cols = { "Bill ID", "Patient", "Doctor", "Appt ID", "Date", "Consult Fee", "Med Cost", "Total",
                "Status" };
        List<Bill> bills = service.getAllBills();
        Object[][] data = new Object[bills.size()][9];
        for (int i = 0; i < bills.size(); i++) {
            Bill b = bills.get(i);
            data[i] = new Object[] { b.getBillId(), b.getPatientName(), "Dr. " + b.getDoctorName(),
                    b.getAppointmentId(), b.getDate(),
                    "$" + b.getConsultationFee(), "$" + b.getMedicationCost(),
                    "$" + b.getTotalAmount(), b.getStatus() };
        }
        inner.add(styledTable(cols, data));
        p.add(inner);
        return p;
    }

    // ════════════════════════════════════════════════════════════════════════════
    // DOCTOR PANELS
    // ════════════════════════════════════════════════════════════════════════════
    private JPanel buildDoctorDashboard() {
        JPanel p = scrollPane();
        JPanel inner = new JPanel();
        inner.setLayout(new BoxLayout(inner, BoxLayout.Y_AXIS));
        inner.setOpaque(false);
        inner.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        Doctor doc = (Doctor) currentUser;
        inner.add(sectionHeader("Welcome, Dr. " + doc.getName()));
        inner.add(Box.createVerticalStrut(20));

        JPanel stats = new JPanel(new GridLayout(1, 3, 16, 0));
        stats.setOpaque(false);
        stats.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        long myAppts = service.getAppointmentsByDoctor(doc.getId()).size();
        long myPatients = service.getAppointmentsByDoctor(doc.getId()).stream()
                .map(Appointment::getPatientId).distinct().count();
        stats.add(statCard("My Appointments", String.valueOf(myAppts), ACCENT));
        stats.add(statCard("Unique Patients", String.valueOf(myPatients), SUCCESS));
        stats.add(statCard("Available Slots", String.valueOf(doc.getAvailableSlots().size()), WARNING));
        inner.add(stats);
        inner.add(Box.createVerticalStrut(30));

        inner.add(sectionHeader("Profile"));
        inner.add(Box.createVerticalStrut(10));
        JPanel profileCard = card(null);
        JPanel grid = new JPanel(new GridLayout(4, 2, 12, 8));
        grid.setOpaque(false);
        grid.add(boldLabel("Specialization:"));
        grid.add(new JLabel(doc.getSpecialization()));
        grid.add(boldLabel("Email:"));
        grid.add(new JLabel(doc.getEmail()));
        grid.add(boldLabel("Phone:"));
        grid.add(new JLabel(doc.getPhone()));
        grid.add(boldLabel("Consultation Fee:"));
        grid.add(new JLabel("$" + doc.getConsultationFee()));
        profileCard.add(grid, BorderLayout.CENTER);
        inner.add(profileCard);

        p.add(inner);
        return p;
    }

    private JPanel buildDoctorAppointmentsPanel() {
        JPanel p = scrollPane();
        JPanel inner = new JPanel();
        inner.setLayout(new BoxLayout(inner, BoxLayout.Y_AXIS));
        inner.setOpaque(false);
        inner.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        inner.add(sectionHeader("My Appointments"));
        inner.add(Box.createVerticalStrut(14));

        String[] cols = { "Appt ID", "Patient", "Date", "Time", "Status" };
        List<Appointment> appts = service.getAppointmentsByDoctor(currentUser.getId());
        Object[][] data = new Object[appts.size()][5];
        for (int i = 0; i < appts.size(); i++) {
            Appointment a = appts.get(i);
            data[i] = new Object[] { a.getAppointmentId(), a.getPatientName(),
                    a.getDate(), a.getTimeSlot(), a.getStatus() };
        }
        inner.add(styledTable(cols, data));
        p.add(inner);
        return p;
    }

    private JPanel buildDoctorRecordsPanel() {
        JPanel p = scrollPane();
        JPanel inner = new JPanel();
        inner.setLayout(new BoxLayout(inner, BoxLayout.Y_AXIS));
        inner.setOpaque(false);
        inner.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        inner.add(sectionHeader("Medical Records"));
        inner.add(Box.createVerticalStrut(14));

        JPanel formCard = card("Add Medical Record");
        JPanel form = new JPanel(new GridLayout(3, 4, 10, 10));
        form.setOpaque(false);
        JTextField patIdF = styledTextField("Patient ID");
        JTextField dateF = styledTextField("Date (YYYY-MM-DD)");
        JTextField diagF = styledTextField("Diagnosis");
        JTextField treatF = styledTextField("Treatment");
        JTextField prescF = styledTextField("Prescription");
        JButton addBtn = accentButton("Add Record", SUCCESS);
        addBtn.addActionListener(e -> {
            MedicalRecord rec = service.addMedicalRecord(patIdF.getText(), currentUser.getId(),
                    dateF.getText(), diagF.getText(), treatF.getText(), prescF.getText());
            if (rec == null)
                JOptionPane.showMessageDialog(this, "Patient not found.", "Error", JOptionPane.ERROR_MESSAGE);
            else {
                JOptionPane.showMessageDialog(this, "Record added: " + rec.getRecordId());
                refreshPanel("Medical Records");
            }
        });
        form.add(patIdF);
        form.add(dateF);
        form.add(diagF);
        form.add(treatF);
        form.add(prescF);
        form.add(new JLabel());
        form.add(new JLabel());
        form.add(addBtn);
        formCard.add(form, BorderLayout.CENTER);
        inner.add(formCard);
        inner.add(Box.createVerticalStrut(20));

        inner.add(sectionHeader("All Patient Records"));
        inner.add(Box.createVerticalStrut(10));
        String[] cols = { "Record ID", "Patient", "Date", "Diagnosis", "Treatment", "Prescription" };
        List<MedicalRecord> recs = service.getAllMedicalRecords();
        Object[][] data = new Object[recs.size()][6];
        for (int i = 0; i < recs.size(); i++) {
            MedicalRecord r = recs.get(i);
            data[i] = new Object[] { r.getRecordId(), r.getPatientName(), r.getDate(),
                    r.getDiagnosis(), r.getTreatment(), r.getPrescription() };
        }
        inner.add(styledTable(cols, data));
        p.add(inner);
        return p;
    }

    private JPanel buildPrescriptionsPanel() {
        JPanel p = scrollPane();
        JPanel inner = new JPanel();
        inner.setLayout(new BoxLayout(inner, BoxLayout.Y_AXIS));
        inner.setOpaque(false);
        inner.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        inner.add(sectionHeader("Prescriptions"));
        inner.add(Box.createVerticalStrut(14));

        String[] cols = { "Record ID", "Patient", "Date", "Diagnosis", "Prescription" };
        List<MedicalRecord> recs = service.getAllMedicalRecords();
        Object[][] data = new Object[recs.size()][5];
        for (int i = 0; i < recs.size(); i++) {
            MedicalRecord r = recs.get(i);
            data[i] = new Object[] { r.getRecordId(), r.getPatientName(), r.getDate(),
                    r.getDiagnosis(), r.getPrescription() };
        }
        inner.add(styledTable(cols, data));
        p.add(inner);
        return p;
    }

    // ════════════════════════════════════════════════════════════════════════════
    // PATIENT PANELS
    // ════════════════════════════════════════════════════════════════════════════
    private JPanel buildPatientDashboard() {
        JPanel p = scrollPane();
        JPanel inner = new JPanel();
        inner.setLayout(new BoxLayout(inner, BoxLayout.Y_AXIS));
        inner.setOpaque(false);
        inner.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        Patient pat = (Patient) currentUser;
        inner.add(sectionHeader("Welcome, " + pat.getName()));
        inner.add(Box.createVerticalStrut(20));

        JPanel stats = new JPanel(new GridLayout(1, 3, 16, 0));
        stats.setOpaque(false);
        stats.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        stats.add(
                statCard("Appointments", String.valueOf(service.getAppointmentsByPatient(pat.getId()).size()), ACCENT));
        stats.add(statCard("Medical Records", String.valueOf(service.getMedicalRecordsByPatient(pat.getId()).size()),
                SUCCESS));
        stats.add(statCard("Pending Bills", String.valueOf(
                service.getBillsByPatient(pat.getId()).stream().filter(b -> b.getStatus().equals("PENDING")).count()),
                WARNING));
        inner.add(stats);
        inner.add(Box.createVerticalStrut(30));

        inner.add(sectionHeader("My Profile"));
        inner.add(Box.createVerticalStrut(10));
        JPanel profileCard = card(null);
        JPanel grid = new JPanel(new GridLayout(4, 2, 12, 8));
        grid.setOpaque(false);
        grid.add(boldLabel("Patient ID:"));
        grid.add(new JLabel(pat.getId()));
        grid.add(boldLabel("Age:"));
        grid.add(new JLabel(String.valueOf(pat.getAge())));
        grid.add(boldLabel("Blood Group:"));
        grid.add(new JLabel(pat.getBloodGroup()));
        grid.add(boldLabel("Address:"));
        grid.add(new JLabel(pat.getAddress()));
        profileCard.add(grid, BorderLayout.CENTER);
        inner.add(profileCard);
        inner.add(Box.createVerticalStrut(20));

        inner.add(sectionHeader("Medical History"));
        inner.add(Box.createVerticalStrut(10));
        JPanel histCard = card(null);
        JTextArea histArea = new JTextArea();
        histArea.setFont(FONT_BODY);
        histArea.setEditable(false);
        histArea.setBackground(CARD_BG);
        for (String h : pat.getMedicalHistory())
            histArea.append("• " + h + "\n");
        if (pat.getMedicalHistory().isEmpty())
            histArea.setText("No history recorded.");
        histCard.add(histArea, BorderLayout.CENTER);
        inner.add(histCard);

        p.add(inner);
        return p;
    }

    private JPanel buildBookAppointmentPanel() {
        JPanel p = scrollPane();
        JPanel inner = new JPanel();
        inner.setLayout(new BoxLayout(inner, BoxLayout.Y_AXIS));
        inner.setOpaque(false);
        inner.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        inner.add(sectionHeader("Book an Appointment"));
        inner.add(Box.createVerticalStrut(14));

        // Doctor table for reference
        inner.add(sectionHeader("Available Doctors"));
        inner.add(Box.createVerticalStrut(10));
        String[] docCols = { "ID", "Name", "Specialization", "Fee", "Available Slots" };
        List<Doctor> docs = service.getAllDoctors();
        Object[][] docData = new Object[docs.size()][5];
        for (int i = 0; i < docs.size(); i++) {
            Doctor d = docs.get(i);
            docData[i] = new Object[] { d.getId(), "Dr. " + d.getName(), d.getSpecialization(),
                    "$" + d.getConsultationFee(), String.join(", ", d.getAvailableSlots()) };
        }
        inner.add(styledTable(docCols, docData));
        inner.add(Box.createVerticalStrut(20));

        // Booking form
        JPanel formCard = card("Confirm Booking");
        JPanel form = new JPanel(new GridLayout(2, 4, 10, 10));
        form.setOpaque(false);
        JTextField docIdF = styledTextField("Doctor ID (e.g. D001)");
        JTextField dateF = styledTextField("Date (YYYY-MM-DD)");
        JTextField slotF = styledTextField("Time Slot (e.g. 09:00 AM)");
        JButton bookBtn = accentButton("Book Now", ACCENT);
        bookBtn.addActionListener(e -> {
            Appointment appt = service.bookAppointment(currentUser.getId(),
                    docIdF.getText().trim(), dateF.getText().trim(), slotF.getText().trim());
            if (appt == null)
                JOptionPane.showMessageDialog(this, "Booking failed. Check Doctor ID and slot.", "Error",
                        JOptionPane.ERROR_MESSAGE);
            else {
                JOptionPane.showMessageDialog(this, "Appointment booked!\nID: " + appt.getAppointmentId(), "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                refreshPanel("Book Appointment");
                refreshPanel("My Appointments");
            }
        });
        form.add(docIdF);
        form.add(dateF);
        form.add(slotF);
        form.add(bookBtn);
        formCard.add(form, BorderLayout.CENTER);
        inner.add(formCard);

        p.add(inner);
        return p;
    }

    private JPanel buildPatientAppointmentsPanel() {
        JPanel p = scrollPane();
        JPanel inner = new JPanel();
        inner.setLayout(new BoxLayout(inner, BoxLayout.Y_AXIS));
        inner.setOpaque(false);
        inner.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        inner.add(sectionHeader("My Appointments"));
        inner.add(Box.createVerticalStrut(14));

        // Cancel form
        JPanel actionCard = card("Cancel an Appointment");
        JPanel form = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        form.setOpaque(false);
        JTextField apptIdF = styledTextField("Appointment ID");
        JButton cancelBtn = accentButton("Cancel", DANGER);
        cancelBtn.addActionListener(e -> {
            boolean ok = service.cancelAppointment(apptIdF.getText().trim());
            JOptionPane.showMessageDialog(this, ok ? "Appointment cancelled." : "Not found.",
                    ok ? "Done" : "Error", ok ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE);
            refreshPanel("My Appointments");
        });
        form.add(new JLabel("Appt ID:"));
        form.add(apptIdF);
        form.add(cancelBtn);
        actionCard.add(form, BorderLayout.CENTER);
        inner.add(actionCard);
        inner.add(Box.createVerticalStrut(20));

        String[] cols = { "Appt ID", "Doctor", "Date", "Time", "Status" };
        List<Appointment> appts = service.getAppointmentsByPatient(currentUser.getId());
        Object[][] data = new Object[appts.size()][5];
        for (int i = 0; i < appts.size(); i++) {
            Appointment a = appts.get(i);
            data[i] = new Object[] { a.getAppointmentId(), "Dr. " + a.getDoctorName(),
                    a.getDate(), a.getTimeSlot(), a.getStatus() };
        }
        inner.add(styledTable(cols, data));
        p.add(inner);
        return p;
    }

    private JPanel buildPatientRecordsPanel() {
        JPanel p = scrollPane();
        JPanel inner = new JPanel();
        inner.setLayout(new BoxLayout(inner, BoxLayout.Y_AXIS));
        inner.setOpaque(false);
        inner.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        inner.add(sectionHeader("My Medical Records"));
        inner.add(Box.createVerticalStrut(14));

        String[] cols = { "Record ID", "Doctor", "Date", "Diagnosis", "Treatment", "Prescription" };
        List<MedicalRecord> recs = service.getMedicalRecordsByPatient(currentUser.getId());
        Object[][] data = new Object[recs.size()][6];
        for (int i = 0; i < recs.size(); i++) {
            MedicalRecord r = recs.get(i);
            data[i] = new Object[] { r.getRecordId(), "Dr. " + r.getDoctorName(), r.getDate(),
                    r.getDiagnosis(), r.getTreatment(), r.getPrescription() };
        }
        inner.add(styledTable(cols, data));
        p.add(inner);
        return p;
    }

    private JPanel buildPatientBillsPanel() {
        JPanel p = scrollPane();
        JPanel inner = new JPanel();
        inner.setLayout(new BoxLayout(inner, BoxLayout.Y_AXIS));
        inner.setOpaque(false);
        inner.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        inner.add(sectionHeader("My Bills"));
        inner.add(Box.createVerticalStrut(14));

        String[] cols = { "Bill ID", "Doctor", "Date", "Consult Fee", "Med Cost", "Total", "Status" };
        List<Bill> bills = service.getBillsByPatient(currentUser.getId());
        Object[][] data = new Object[bills.size()][7];
        for (int i = 0; i < bills.size(); i++) {
            Bill b = bills.get(i);
            data[i] = new Object[] { b.getBillId(), "Dr. " + b.getDoctorName(), b.getDate(),
                    "$" + b.getConsultationFee(), "$" + b.getMedicationCost(),
                    "$" + b.getTotalAmount(), b.getStatus() };
        }
        inner.add(styledTable(cols, data));
        p.add(inner);
        return p;
    }

    // ════════════════════════════════════════════════════════════════════════════
    // REFRESH HELPER
    // ════════════════════════════════════════════════════════════════════════════
    private void refreshPanel(String name) {
        // Remove old panel and rebuild
        for (Component c : contentPanel.getComponents()) {
            if (c instanceof JPanel) {
                ContentPanelWrapper w = getWrapperByName(name);
                if (w != null) {
                    contentPanel.remove(w.panel);
                    break;
                }
            }
        }
        JPanel rebuilt = rebuildPanel(name);
        if (rebuilt != null) {
            contentPanel.add(rebuilt, name);
            contentLayout.show(contentPanel, name);
        }
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private ContentPanelWrapper getWrapperByName(String name) {
        return null;
    }

    private JPanel rebuildPanel(String name) {
        return switch (name) {
            case "Dashboard" -> currentUser.getRole().equals("Admin") ? buildAdminDashboard()
                    : currentUser.getRole().equals("Doctor") ? buildDoctorDashboard() : buildPatientDashboard();
            case "Doctors" -> buildDoctorsPanel();
            case "Patients" -> buildPatientsPanel();
            case "Appointments" -> buildAppointmentsPanel();
            case "Records", "Medical Records" -> currentUser.getRole().equals("Doctor")
                    ? buildDoctorRecordsPanel()
                    : buildMedicalRecordsPanel();
            case "Billing" -> buildBillingPanel();
            case "My Appointments" -> currentUser.getRole().equals("Doctor")
                    ? buildDoctorAppointmentsPanel()
                    : buildPatientAppointmentsPanel();
            case "Book Appointment" -> buildBookAppointmentPanel();
            case "My Records" -> buildPatientRecordsPanel();
            case "My Bills" -> buildPatientBillsPanel();
            case "Prescriptions" -> buildPrescriptionsPanel();
            default -> null;
        };
    }

    static class ContentPanelWrapper {
        JPanel panel;
        String name;
    }

    // ════════════════════════════════════════════════════════════════════════════
    // UI HELPERS
    // ════════════════════════════════════════════════════════════════════════════
    private JPanel scrollPane() {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(BG);
        return wrapper;
    }

    private JLabel sectionHeader(String title) {
        JLabel lbl = new JLabel(title);
        lbl.setFont(FONT_TITLE);
        lbl.setForeground(TEXT_DARK);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        return lbl;
    }

    private JLabel boldLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(FONT_HEADER);
        lbl.setForeground(TEXT_MID);
        return lbl;
    }

    private JPanel statCard(String label, String value, Color color) {
        JPanel card = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(CARD_BG);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.setColor(color);
                g2.fillRoundRect(0, getHeight() - 4, getWidth(), 4, 0, 0);
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setBorder(BorderFactory.createEmptyBorder(16, 20, 16, 20));

        JLabel valLbl = new JLabel(value);
        valLbl.setFont(new Font("Segoe UI", Font.BOLD, 32));
        valLbl.setForeground(color);

        JLabel lblLbl = new JLabel(label);
        lblLbl.setFont(FONT_BODY);
        lblLbl.setForeground(TEXT_MID);

        card.add(valLbl, BorderLayout.CENTER);
        card.add(lblLbl, BorderLayout.SOUTH);
        return card;
    }

    private JPanel card(String title) {
        JPanel card = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(CARD_BG);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setBorder(BorderFactory.createEmptyBorder(16, 20, 16, 20));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));
        card.setAlignmentX(Component.LEFT_ALIGNMENT);
        if (title != null) {
            JLabel titleLbl = new JLabel(title);
            titleLbl.setFont(FONT_HEADER);
            titleLbl.setForeground(TEXT_DARK);
            titleLbl.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
            card.add(titleLbl, BorderLayout.NORTH);
        }
        return card;
    }

    private JScrollPane styledTable(String[] cols, Object[][] data) {
        JTable table = new JTable(data, cols) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        table.setFont(FONT_BODY);
        table.setRowHeight(34);
        table.setGridColor(new Color(0xECEFF1));
        table.setShowHorizontalLines(true);
        table.setShowVerticalLines(false);
        table.setIntercellSpacing(new Dimension(0, 1));
        table.setSelectionBackground(new Color(0xE3F2FD));
        table.setSelectionForeground(TEXT_DARK);

        JTableHeader header = table.getTableHeader();
        header.setFont(FONT_HEADER);
        header.setBackground(TABLE_HEAD);
        header.setForeground(TEXT_DARK);
        header.setBorder(BorderFactory.createEmptyBorder());

        // Alternating rows
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object v,
                    boolean sel, boolean foc, int row, int col) {
                Component c = super.getTableCellRendererComponent(t, v, sel, foc, row, col);
                if (!sel)
                    c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(0xFAFCFF));
                setBorder(BorderFactory.createEmptyBorder(0, 12, 0, 12));
                return c;
            }
        });

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(0xE0E0E0), 1, true));
        scroll.setMaximumSize(new Dimension(Integer.MAX_VALUE, 300));
        scroll.setAlignmentX(Component.LEFT_ALIGNMENT);
        return scroll;
    }

    private JTextField styledTextField(String placeholder) {
        JTextField f = new JTextField() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (getText().isEmpty() && !isFocusOwner()) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setFont(getFont().deriveFont(Font.ITALIC));
                    g2.setColor(new Color(0xBDBDBD));
                    g2.drawString(placeholder, 12, getHeight() / 2 + 5);
                    g2.dispose();
                }
            }
        };
        f.setFont(FONT_BODY);
        f.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0xCFD8DC), 1, true),
                BorderFactory.createEmptyBorder(6, 10, 6, 10)));
        f.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        return f;
    }

    private JButton accentButton(String text, Color color) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(
                        getModel().isPressed() ? color.darker() : getModel().isRollover() ? color.brighter() : color);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(FONT_BODY);
        btn.setForeground(Color.WHITE);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(8, 18, 8, 18));
        btn.setOpaque(false);
        return btn;
    }

    private String sidebarIcon(String item) {
        return switch (item) {
            case "Dashboard" -> "◉";
            case "Doctors", "My Appointments" -> "👨‍⚕️";
            case "Patients" -> "🏥";
            case "Appointments", "Book Appointment" -> "📅";
            case "Records", "Medical Records", "My Records" -> "📋";
            case "Billing", "My Bills" -> "💳";
            case "Prescriptions" -> "💊";
            default -> "•";
        };
    }
}
