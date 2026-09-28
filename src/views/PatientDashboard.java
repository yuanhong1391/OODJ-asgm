/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package views;
import java.awt.Color;
import javax.swing.table.DefaultTableModel;
import java.util.List;

/**
 *
 * @author Tei Yuan Hong
 */
public class PatientDashboard extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(PatientDashboard.class.getName());
    private models.Patient currentPatient;
    /**
     * Creates new form PatientDashboard
     */
    public PatientDashboard() {
        initComponents();
        setLocationRelativeTo(null);
    }
    
    public PatientDashboard(models.Patient patient) 
    {
        this.currentPatient = patient;
        initComponents();
        setLocationRelativeTo(null);
        
        if (currentPatient != null) 
        {
            lblHeaderPatient.setText("Welcome, " + currentPatient.getName() + "  Patient ID: " + currentPatient.getID());
        }
        
        loadDoctorsToCbox();
        loadDateToCbox();
        loadTableAppointment();
        loadTableConPres();
        loadTableFeed();
        loadProfile();
        
        updateSlotStatus();
        
    }
    
    private void loadDateToCbox() 
    {
        cboxAppDate.removeAllItems();
        if (cboxAppSelectDoc.getSelectedItem() == null) 
        {
            return;
        }
        
        String docId = cboxAppSelectDoc.getSelectedItem().toString().split("-")[0].trim();
        
        for (String slot : services.FileHelper.getRosterSlots(docId))
        {
            cboxAppDate.addItem(slot);
        }
            
    }
    
    private void loadDoctorsToCbox() 
    {
        cboxAppSelectDoc.removeAllItems();
        cboxFeedSelectDoc.removeAllItems();
        
        services.UserService userService = new services.UserService();
        List<models.User> users = userService.loadAllUsers();
        
        for (models.User u : users) 
        {
            if (u.getRole().equalsIgnoreCase("Doctor")) 
            {
                String item = u.getID() + "-" + u.getName();
                
                if (u instanceof models.Doctor ) 
                {
                    models.Doctor doc = (models.Doctor) u;
                    item += " (" + doc.getSpecialization() + ")";
                }
                
                cboxAppSelectDoc.addItem(item);
                cboxFeedSelectDoc.addItem(item);
                
            }
        }
    }
    
    private void loadTableAppointment() 
    {
        javax.swing.table.DefaultTableModel model = (javax.swing.table.DefaultTableModel) tblAppointment.getModel();
        model.setRowCount(0);
        
        List<String> lines = services.FileHelper.readFile("data/appointments.txt");
        for (String line : lines) 
        {
            String[] data = line.split(",");
            if (data.length < 7) 
            {
                continue;
            }
            
            String appId = data[0].trim();
            String patientId = data[1].trim();
            String docId = data[3].trim();
            String date = data[4].trim();
            String status = data[6].trim();
            
            if (patientId.equalsIgnoreCase(currentPatient.getID())) 
            {
                String docName = services.FileHelper.findNameBasedID(docId, "Doctor");
                if (docName == null) 
                {
                    docName = "Not Found";
                }
                
                model.addRow(new Object[] {appId, docName, date, status});
            }
        }
    }
    
    private void loadTableConPres() 
    {
        javax.swing.table.DefaultTableModel vitalsModel = (javax.swing.table.DefaultTableModel) tblCon.getModel();
        vitalsModel.setRowCount(0);
        
        List<String> vitalsLines = services.FileHelper.readFile("data/vitals.txt");
        for (String line : vitalsLines) {
            String[] data = line.split(",");
            if (data.length < 6) 
            {
                continue;
            }
            
            String vitalId = data[0].trim();
            String patientId = data[1].trim();
            String docId = data[2].trim();
            String date = data[3].trim();
            String vitals = data[4].trim();
            String note = data[5].trim();
            
            if (patientId.equalsIgnoreCase(currentPatient.getID())) 
            {
                String docName = services.FileHelper.findNameBasedID(docId, "Doctor");
                if (docName == null) {docName = "Not found";}
                vitalsModel.addRow(new Object[] {vitalId, docName, date, vitals, note});
            }
            
            
        }
        
        javax.swing.table.DefaultTableModel presModel = (javax.swing.table.DefaultTableModel) tblPres.getModel();
        presModel.setRowCount(0);

        List<String> presLines = services.FileHelper.readFile("data/prescriptions.txt");
        for (String line : presLines) 
        {
            String[] data = line.split(",");
            if (data.length < 7) 
            {
                continue;
            }
            String presId = data[0].trim();
            String patientId =data[1].trim();
            String date =data[3].trim();
            String medicine =data[4].trim();
            String dosage =data[5].trim();
            
            if (patientId.equalsIgnoreCase(currentPatient.getID())) 
            {
                presModel.addRow(new Object[] {presId, date, medicine, dosage});
            }
        }
    }
    
private void loadTableFeed() {
    javax.swing.table.DefaultTableModel model = (javax.swing.table.DefaultTableModel) tblFeed.getModel();
    model.setRowCount(0);

    List<String> lines = services.FileHelper.readFile("data/feedbacks.txt");
    for (String line : lines) {
        String[] data = line.split(",");
        if (data.length < 6) continue;

        String feedId = data[0].trim();
        String patientId = data[1].trim();
        String doctorId = data[2].trim();
        String rating = data[3].trim();
        String comment = data[4].trim();
        String date = data[5].trim();

        if (patientId.equalsIgnoreCase(currentPatient.getID())) {
            String docName = services.FileHelper.findNameBasedID(doctorId, "Doctor");
            model.addRow(new Object[] { feedId, docName, rating + " ★", date, comment });
        }
    }
}

private void loadProfile() 
{
    txtProID.setText(currentPatient.getID());
    txtProUserName.setText(currentPatient.getUsername());
    txtProRole.setText(currentPatient.getRole());
    txtProBloodType.setText(currentPatient.getBloodType());
    txtProName.setText(currentPatient.getName());
    txtProPhone.setText(currentPatient.getPhone());
    txtProEmail.setText(currentPatient.getEmail());
    txtEmerCon.setText(currentPatient.getEmergencyContact());
}

private int getRemainingSlots(String doctorId, String date) 
{
    List<String> lines = services.FileHelper.readFile("data/appointments.txt");
    int bookedCount = 0;
    
    for (String line : lines) 
    {
        String[] data = line.split(",");
        if (data.length < 7) 
        {
            continue;
        }
        
        String linesDocId = data[3].trim();
        String linesDate = data[4].trim();
        String linesStatus = data[6].trim();
        
        if (linesDocId.equalsIgnoreCase(doctorId) && linesDate.equalsIgnoreCase(date)) 
        {
            if(!linesStatus.equalsIgnoreCase("Cancelled")) 
            {
                bookedCount++;
            }
        }
        
    }
    int remaining = 3 - bookedCount;
    return remaining;
}

private void updateSlotStatus() 
{
    if (cboxAppSelectDoc.getSelectedItem() == null || cboxAppDate.getSelectedItem() == null) 
    {
        return; 
    }
    String selectedDoc = cboxAppSelectDoc.getSelectedItem().toString();
    String docId = selectedDoc.split("-")[0].trim();
    String date = cboxAppDate.getSelectedItem().toString().trim();
    
    int remaining = getRemainingSlots(docId, date);
    
    if (remaining > 0) 
    {
        lblAppAvailable.setText("*Available (Remaining: " + remaining + "/3 slots");
        lblAppAvailable.setForeground(Color.green);
    }
    else 
    {
        lblAppAvailable.setText("*Fully Booked (0/3 slots left)");
        lblAppAvailable.setForeground(Color.red);
    }
}

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup7 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        lblHeader = new javax.swing.JLabel();
        lblHeaderPatient = new javax.swing.JLabel();
        btnLogOut = new javax.swing.JButton();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblAppointment = new javax.swing.JTable();
        jPanel7 = new javax.swing.JPanel();
        lblBookApp = new javax.swing.JLabel();
        lblAppSelectDoc = new javax.swing.JLabel();
        cboxAppSelectDoc = new javax.swing.JComboBox<>();
        lblAppDate = new javax.swing.JLabel();
        cboxAppDate = new javax.swing.JComboBox<>();
        lblAppSymptom = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtSymptom = new javax.swing.JTextArea();
        btnBookApp = new javax.swing.JButton();
        lblAppAvailable = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        btnAppReschedule = new javax.swing.JButton();
        btnAppCancel = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        jPanel10 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tblCon = new javax.swing.JTable();
        lblConsultation = new javax.swing.JLabel();
        jPanel11 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblPres = new javax.swing.JTable();
        lblPres = new javax.swing.JLabel();
        jPanel13 = new javax.swing.JPanel();
        btnConPresRefresh = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jPanel12 = new javax.swing.JPanel();
        lblReview = new javax.swing.JLabel();
        lblFeedSelectDoc = new javax.swing.JLabel();
        cboxFeedSelectDoc = new javax.swing.JComboBox<>();
        jLabel12 = new javax.swing.JLabel();
        cboxFeedRating = new javax.swing.JComboBox<>();
        lblFeed = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        txtFeed = new javax.swing.JTextArea();
        btnSubmitFeed = new javax.swing.JButton();
        jScrollPane6 = new javax.swing.JScrollPane();
        tblFeed = new javax.swing.JTable();
        jLabel14 = new javax.swing.JLabel();
        btnFeedRefresh = new javax.swing.JButton();
        btnDeleteFeed = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jPanel14 = new javax.swing.JPanel();
        lblProID = new javax.swing.JLabel();
        txtProPhone = new javax.swing.JTextField();
        txtProEmail = new javax.swing.JTextField();
        lblProRole = new javax.swing.JLabel();
        txtProRole = new javax.swing.JTextField();
        lblProBloodType = new javax.swing.JLabel();
        txtProBloodType = new javax.swing.JTextField();
        lblProPhone = new javax.swing.JLabel();
        txtEmerCon = new javax.swing.JTextField();
        lblEmerCon = new javax.swing.JLabel();
        txtProPassword = new javax.swing.JPasswordField();
        lblProEmail = new javax.swing.JLabel();
        lblProPassword = new javax.swing.JLabel();
        txtProID = new javax.swing.JTextField();
        txtProName = new javax.swing.JTextField();
        lblProName = new javax.swing.JLabel();
        btnUpdateProfile = new javax.swing.JButton();
        btnSetAsDefalt = new javax.swing.JButton();
        btnChangePass = new javax.swing.JButton();
        lblProName1 = new javax.swing.JLabel();
        txtProUserName = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(0, 102, 102));

        lblHeader.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblHeader.setForeground(new java.awt.Color(255, 255, 255));
        lblHeader.setText("Patient portal -APU Medical Centre");

        lblHeaderPatient.setFont(new java.awt.Font("Segoe UI", 2, 14)); // NOI18N
        lblHeaderPatient.setForeground(new java.awt.Color(255, 255, 255));
        lblHeaderPatient.setText("Welcome, John Doe  Patient ID: P001");

        btnLogOut.setText("Log Out");
        btnLogOut.addActionListener(this::btnLogOutActionPerformed);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblHeaderPatient)
                    .addComponent(lblHeader))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnLogOut)
                .addGap(39, 39, 39))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(lblHeader)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblHeaderPatient))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addComponent(btnLogOut)))
                .addContainerGap(12, Short.MAX_VALUE))
        );

        jTabbedPane1.setBackground(new java.awt.Color(153, 153, 153));

        jPanel2.setBackground(new java.awt.Color(204, 204, 204));

        tblAppointment.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Appoitment ID", "Doctor", "Date", "Status"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tblAppointment);

        jPanel7.setBackground(new java.awt.Color(175, 175, 175));

        lblBookApp.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        lblBookApp.setText("Book New Appointment: ");

        lblAppSelectDoc.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblAppSelectDoc.setText("Select Doctor: ");

        cboxAppSelectDoc.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cboxAppSelectDoc.addActionListener(this::cboxAppSelectDocActionPerformed);

        lblAppDate.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblAppDate.setText("Appointment Date: ");

        cboxAppDate.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cboxAppDate.addActionListener(this::cboxAppDateActionPerformed);

        lblAppSymptom.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblAppSymptom.setText("Sysptoms / Reason for Visit: ");

        txtSymptom.setColumns(20);
        txtSymptom.setRows(5);
        jScrollPane2.setViewportView(txtSymptom);

        btnBookApp.setText("Book Appointment");
        btnBookApp.addActionListener(this::btnBookAppActionPerformed);

        lblAppAvailable.setBackground(new java.awt.Color(255, 255, 255));
        lblAppAvailable.setFont(new java.awt.Font("Segoe UI", 2, 12)); // NOI18N
        lblAppAvailable.setForeground(new java.awt.Color(0, 153, 76));
        lblAppAvailable.setText("* Available (Remaining: 5/8 slots)");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGap(16, 16, 16)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblBookApp)
                            .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 260, Short.MAX_VALUE)
                                .addComponent(lblAppSymptom))
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblAppDate)
                                    .addComponent(lblAppSelectDoc))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(cboxAppSelectDoc, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(cboxAppDate, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblAppAvailable, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGap(88, 88, 88)
                        .addComponent(btnBookApp, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(lblBookApp)
                .addGap(33, 33, 33)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblAppSelectDoc)
                    .addComponent(cboxAppSelectDoc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(32, 32, 32)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblAppDate)
                    .addComponent(cboxAppDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblAppAvailable)
                .addGap(37, 37, 37)
                .addComponent(lblAppSymptom)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 136, Short.MAX_VALUE)
                .addComponent(btnBookApp)
                .addContainerGap())
        );

        jPanel8.setBackground(new java.awt.Color(175, 175, 175));

        btnAppReschedule.setText("Reschedule");
        btnAppReschedule.addActionListener(this::btnAppRescheduleActionPerformed);

        btnAppCancel.setText("Cancel");
        btnAppCancel.addActionListener(this::btnAppCancelActionPerformed);

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnAppReschedule, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 35, Short.MAX_VALUE)
                .addComponent(btnAppCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addContainerGap(11, Short.MAX_VALUE)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAppReschedule)
                    .addComponent(btnAppCancel))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(jScrollPane1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(19, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Appointments", jPanel2);

        jPanel10.setBackground(new java.awt.Color(175, 175, 175));

        tblCon.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Record ID", "Doctor Name", "Date", "Vitals (BP/Temp)", "Notes"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane4.setViewportView(tblCon);

        lblConsultation.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblConsultation.setText("Consultation & Vital Signs Records: ");

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 678, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(lblConsultation)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                .addContainerGap(13, Short.MAX_VALUE)
                .addComponent(lblConsultation)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel11.setBackground(new java.awt.Color(175, 175, 175));

        tblPres.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Prescription ID", "Date", "Medicine Name", "Dosage & Instruction"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane3.setViewportView(tblPres);

        lblPres.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblPres.setText("My Prescriptions: ");

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3)
                .addContainerGap())
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(lblPres)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                .addContainerGap(13, Short.MAX_VALUE)
                .addComponent(lblPres)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel13.setBackground(new java.awt.Color(175, 175, 175));

        btnConPresRefresh.setText("Refresh");
        btnConPresRefresh.addActionListener(this::btnConPresRefreshActionPerformed);

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel13Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnConPresRefresh, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29))
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(btnConPresRefresh)
                .addContainerGap(11, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Medical Records", jPanel3);

        jPanel12.setBackground(new java.awt.Color(175, 175, 175));

        lblReview.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        lblReview.setText("Rate & Review Doctor");

        lblFeedSelectDoc.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblFeedSelectDoc.setText("Select Doctor: ");

        cboxFeedSelectDoc.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel12.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel12.setText("Rating: ");

        cboxFeedRating.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "5 - Excellent (* * * * *)", "4 - Good (* * * *)", "3 - Average (* * *)", "2 - Poor (* *)", "1 - Terrible (*)" }));

        lblFeed.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblFeed.setText("Your Feedback / Comments: ");

        txtFeed.setColumns(20);
        txtFeed.setRows(5);
        jScrollPane5.setViewportView(txtFeed);

        btnSubmitFeed.setText("Submit FeedBack");
        btnSubmitFeed.addActionListener(this::btnSubmitFeedActionPerformed);

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel12Layout.createSequentialGroup()
                .addContainerGap(24, Short.MAX_VALUE)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 302, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblFeed))
                .addGap(18, 18, 18))
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblReview)
                            .addGroup(jPanel12Layout.createSequentialGroup()
                                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblFeedSelectDoc)
                                    .addComponent(jLabel12))
                                .addGap(39, 39, 39)
                                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(cboxFeedSelectDoc, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(cboxFeedRating, 0, 178, Short.MAX_VALUE)))))
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addGap(70, 70, 70)
                        .addComponent(btnSubmitFeed, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addComponent(lblReview)
                .addGap(60, 60, 60)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addComponent(lblFeedSelectDoc)
                        .addGap(30, 30, 30)
                        .addComponent(jLabel12))
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addComponent(cboxFeedSelectDoc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(24, 24, 24)
                        .addComponent(cboxFeedRating, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(35, 35, 35)
                .addComponent(lblFeed)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26)
                .addComponent(btnSubmitFeed)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tblFeed.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "ID", "Doctor", "Rating", "Date", "Comments"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane6.setViewportView(tblFeed);

        jLabel14.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel14.setText("My Review History");

        btnFeedRefresh.setText("Refresh");
        btnFeedRefresh.addActionListener(this::btnFeedRefreshActionPerformed);

        btnDeleteFeed.setText("Delete");
        btnDeleteFeed.setToolTipText("");
        btnDeleteFeed.addActionListener(this::btnDeleteFeedActionPerformed);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel14)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addComponent(btnDeleteFeed, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnFeedRefresh, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 322, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(jLabel14)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 371, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnFeedRefresh)
                    .addComponent(btnDeleteFeed))
                .addGap(0, 55, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Feedback & Rating", jPanel4);

        lblProID.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblProID.setText("Patient ID: ");

        lblProRole.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblProRole.setText("Role: ");

        txtProRole.setEditable(false);

        lblProBloodType.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblProBloodType.setText("Blood Type");

        txtProBloodType.setEditable(false);

        lblProPhone.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblProPhone.setText("Phone: ");

        lblEmerCon.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblEmerCon.setText("Emergency Contact");

        txtProPassword.setEditable(false);
        txtProPassword.setText("1234567890");

        lblProEmail.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblProEmail.setText("Email: ");

        lblProPassword.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblProPassword.setText("Password: ");

        txtProID.setEditable(false);

        lblProName.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblProName.setText("Username: ");

        btnUpdateProfile.setText("Update Profile");
        btnUpdateProfile.addActionListener(this::btnUpdateProfileActionPerformed);

        btnSetAsDefalt.setText("Set as Default");
        btnSetAsDefalt.addActionListener(this::btnSetAsDefaltActionPerformed);

        btnChangePass.setText("Change");
        btnChangePass.addActionListener(this::btnChangePassActionPerformed);

        lblProName1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblProName1.setText("Name");

        txtProUserName.setEditable(false);

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel14Layout.createSequentialGroup()
                .addContainerGap(91, Short.MAX_VALUE)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addGap(44, 44, 44)
                        .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel14Layout.createSequentialGroup()
                                .addComponent(lblEmerCon)
                                .addGap(18, 18, 18))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel14Layout.createSequentialGroup()
                                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblProName, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblProID)
                                    .addComponent(lblProName1)
                                    .addComponent(lblProPhone)
                                    .addComponent(lblProEmail)
                                    .addComponent(lblProRole)
                                    .addComponent(lblProBloodType)
                                    .addComponent(lblProPassword))
                                .addGap(55, 55, 55)))
                        .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel14Layout.createSequentialGroup()
                                .addGap(2, 2, 2)
                                .addComponent(txtProID, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(txtProName, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtProUserName, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel14Layout.createSequentialGroup()
                                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(txtEmerCon, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtProBloodType, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtProRole, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtProEmail, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtProPhone, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtProPassword, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 201, Short.MAX_VALUE))
                                .addGap(18, 18, 18)
                                .addComponent(btnChangePass))))
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addComponent(btnUpdateProfile, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(55, 55, 55)
                        .addComponent(btnSetAsDefalt, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(42, 42, 42))
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblProID)
                    .addComponent(txtProID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblProName)
                    .addComponent(txtProUserName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblProName1)
                    .addComponent(txtProName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblProPassword)
                    .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtProPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnChangePass)))
                .addGap(11, 11, 11)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblProPhone)
                    .addComponent(txtProPhone, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblProEmail)
                    .addComponent(txtProEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblProRole)
                    .addComponent(txtProRole, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblProBloodType)
                    .addComponent(txtProBloodType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblEmerCon)
                    .addComponent(txtEmerCon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 45, Short.MAX_VALUE)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnUpdateProfile)
                    .addComponent(btnSetAsDefalt))
                .addGap(30, 30, 30))
        );

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(65, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(46, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("My Profile", jPanel5);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane1)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnUpdateProfileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateProfileActionPerformed
        // TODO add your handling code here:
        String newPhone = txtProPhone.getText().trim();
        String newEmail = txtProEmail.getText().trim();
        String newName = txtProName.getText().trim();
        String newEmer = txtEmerCon.getText().trim();

        if (newPhone.isEmpty() || newEmail.isEmpty() || newName.isEmpty() || newEmer.isEmpty())
        {
            javax.swing.JOptionPane.showMessageDialog(this, "Please do not leave Name, Phone, Email or Emergency Contact empty!", "Invalid Input", javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = javax.swing.JOptionPane.showConfirmDialog(this, "Are you sure you want update profile? Name: "  + newName + " Phone: " + newPhone + " Email: " + newEmail, "Confirmation", javax.swing.JOptionPane.YES_NO_OPTION);

        if (confirm !=javax.swing.JOptionPane.YES_OPTION)
        {
            loadProfile();
            return;
        }

        services.UserService userService = new services.UserService();
        List<models.User> list = userService.loadAllUsers();
        boolean found = false;
        
        for (models.User u : list) {
            if (u.getID().equalsIgnoreCase(currentPatient.getID())) 
            {
                if (u instanceof models.Patient) 
                {
                    models.Patient p = (models.Patient) u;
                    p.updateProfile(newName, newPhone, newEmail, newEmer);
                    found = true;
                    break;
                }
            }
        }
        

        if (found && userService.saveAllUsers(list))
        {
            currentPatient.updateProfile(newName, newPhone, newEmail, newEmer);

            lblHeaderPatient.setText("Welcome, " + currentPatient.getName() + " ID: " + currentPatient.getID());

            javax.swing.JOptionPane.showMessageDialog(this, "Profile updated sucessfully", "Sucess", javax.swing.JOptionPane.INFORMATION_MESSAGE);
        }

        else
        {
            javax.swing.JOptionPane.showMessageDialog(this, "Failed to update profile. Please try again later", "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
            loadProfile();
        }
    }//GEN-LAST:event_btnUpdateProfileActionPerformed

    private void btnSetAsDefaltActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSetAsDefaltActionPerformed
        // TODO add your handling code here:
        loadProfile();
    }//GEN-LAST:event_btnSetAsDefaltActionPerformed

    private void btnChangePassActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnChangePassActionPerformed
        // TODO add your handling code here:
        new ChangePassword(currentPatient.getID()).setVisible(true);
    }//GEN-LAST:event_btnChangePassActionPerformed

    private void btnBookAppActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBookAppActionPerformed
        // TODO add your handling code here:
        if (cboxAppSelectDoc.getSelectedItem() == null || cboxAppDate.getSelectedItem() == null) {
        javax.swing.JOptionPane.showMessageDialog(this, "Please select both a Doctor and an Appointment Date!", "Input Error", javax.swing.JOptionPane.WARNING_MESSAGE);
        return;
        }
        
        String selectedDoc = cboxAppSelectDoc.getSelectedItem().toString();
        String doctorId = selectedDoc.split("-")[0].trim();
        String bookDate = cboxAppDate.getSelectedItem().toString().trim();
        String reason = txtSymptom.getText().trim().replace(",", ";");
        
        if (reason.isEmpty()) 
        {
            javax.swing.JOptionPane.showMessageDialog(this, "Please describe your symptoms or reason for visit!", "Input Required", javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int remaining = getRemainingSlots(doctorId, bookDate);
        if (remaining <= 0) 
        {
            javax.swing.JOptionPane.showMessageDialog(this, "Sorry, this doctor is fully booked on " + bookDate + " (0/3)! Please select another date or doctor.", "Slot Unavailable", javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        currentPatient.bookAppointment(doctorId, bookDate, reason);
        
        javax.swing.JOptionPane.showMessageDialog(this, "Appoinment booked successfully for " + bookDate, "Success", javax.swing.JOptionPane.INFORMATION_MESSAGE);
        
        txtSymptom.setText("");
        loadTableAppointment();
        updateSlotStatus();
    }//GEN-LAST:event_btnBookAppActionPerformed

    private void btnAppCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAppCancelActionPerformed
        // TODO add your handling code here:
        int selectedRow =  tblAppointment.getSelectedRow();
        if (selectedRow == -1) 
        {
            javax.swing.JOptionPane.showMessageDialog(this, "Please select an appointment from the table to cancel!", "No Selection", javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String appId = tblAppointment.getValueAt(selectedRow, 0).toString().trim();
        String status = tblAppointment.getValueAt(selectedRow, 3).toString().trim();
        
        if ("Completed".equalsIgnoreCase(status) || "cancelled".equalsIgnoreCase(status)) 
        {
            javax.swing.JOptionPane.showMessageDialog(this, "Cannot cancel an appointment that is already Completed or Cancelled!", "Warning", javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int confirm = javax.swing.JOptionPane.showConfirmDialog(this, "Are you sure you want to cancel appointment " + appId + "?", "Confirm Cancel", javax.swing.JOptionPane.YES_NO_OPTION);
        if (confirm == javax.swing.JOptionPane.YES_OPTION)
        {
            boolean success = services.FileHelper.updateAppointmentContent(appId, 6, "Cancelled");
            if (success) 
            {
                javax.swing.JOptionPane.showMessageDialog(this, "Appointment canceled sucessfuly", "Sucess", javax.swing.JOptionPane.INFORMATION_MESSAGE);
                loadTableAppointment();
                updateSlotStatus();
            }
        }
        
    }//GEN-LAST:event_btnAppCancelActionPerformed

    private void cboxAppSelectDocActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboxAppSelectDocActionPerformed
        // TODO add your handling code here:
        loadDateToCbox();
        updateSlotStatus();
    }//GEN-LAST:event_cboxAppSelectDocActionPerformed

    private void cboxAppDateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboxAppDateActionPerformed
        // TODO add your handling code here:
        updateSlotStatus();
    }//GEN-LAST:event_cboxAppDateActionPerformed

    private void btnAppRescheduleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAppRescheduleActionPerformed
        // TODO add your handling code here:
        int selectedRow = tblAppointment.getSelectedRow();
        if (selectedRow == -1) {
            javax.swing.JOptionPane.showMessageDialog(this, "Please select an appointment to reschedule!", "No Selection", javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }

        String apptId = tblAppointment.getValueAt(selectedRow, 0).toString().trim();
        String status = tblAppointment.getValueAt(selectedRow, 3).toString().trim();


        if (!"Pending".equalsIgnoreCase(status)) {
            javax.swing.JOptionPane.showMessageDialog(this, "You can only reschedule 'Pending' appointments!", "Invalid Action", javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }

        String doctorId = "";
        List<String> lines = services.FileHelper.readFile("data/appointments.txt");
        for (String line : lines) {
            String[] data = line.split(",");
            if (data.length >= 7 && data[0].trim().equalsIgnoreCase(apptId)) {
                doctorId = data[3].trim(); 
                break;
            }
        }


        List<String> rosterSlots = services.FileHelper.getRosterSlots(doctorId);
        if (rosterSlots.isEmpty()) 
        {
            javax.swing.JOptionPane.showMessageDialog(this, "This doctor has no upcoming roster.", "No Roster", javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String[] availableDates = rosterSlots.toArray(new String[0]);


        Object selectedNewDate = javax.swing.JOptionPane.showInputDialog(
            this,
            "Select new date for appointment (" + apptId + "):",
            "Reschedule Appointment",
            javax.swing.JOptionPane.QUESTION_MESSAGE,
            null,
            availableDates,      
            availableDates[0]    
        );


        if (selectedNewDate == null) {
            return;
        }

        String newDate = selectedNewDate.toString();

 
        int remaining = getRemainingSlots(doctorId, newDate);
        if (remaining <= 0) {
            javax.swing.JOptionPane.showMessageDialog(this, 
                "Sorry, the doctor is already fully booked on " + newDate + " (3/3)! Please choose another day.", 
                "Date Unavailable", 
                javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }


        boolean success = services.FileHelper.updateAppointmentContent(apptId, 4, newDate);
        if (success) {
            javax.swing.JOptionPane.showMessageDialog(this, 
                "Appointment " + apptId + " successfully rescheduled to " + newDate + "!", 
                "Success", 
                javax.swing.JOptionPane.INFORMATION_MESSAGE);
            loadTableAppointment(); 
            updateSlotStatus();   
        } else {
            javax.swing.JOptionPane.showMessageDialog(this, "Failed to reschedule appointment.", "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnAppRescheduleActionPerformed

    private void btnLogOutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLogOutActionPerformed
        // TODO add your handling code here:
        int confirm = javax.swing.JOptionPane.showConfirmDialog(this, "Are you sure you want to log out?", "Logout Confirmation", javax.swing.JOptionPane.YES_NO_OPTION);
        if (confirm == javax.swing.JOptionPane.YES_OPTION) {
        new Login().setVisible(true);
        this.dispose();}
    }//GEN-LAST:event_btnLogOutActionPerformed

    private void btnSubmitFeedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSubmitFeedActionPerformed
        // TODO add your handling code here:
        if (cboxFeedSelectDoc.getSelectedItem() == null) 
        {
            javax.swing.JOptionPane.showMessageDialog(this, "Please selct a doctor to review", "Warning", javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }
        String selectedDoc = cboxFeedSelectDoc.getSelectedItem().toString().trim();
        String doctorId = selectedDoc.split("-")[0].trim();
        
        int rating = 5;
        String ratingStr = cboxFeedRating.getSelectedItem().toString().trim();
        rating = Integer.parseInt(ratingStr.substring(0,1));
        
        String comment = txtFeed.getText().trim().replace(",", ";");
        if (comment.isEmpty()) 
        {
            javax.swing.JOptionPane.showMessageDialog(this, "Please write your feedback or comments!", "Input Required", javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        currentPatient.submitFeedback(doctorId, rating, comment);
        
        javax.swing.JOptionPane.showMessageDialog(this, "Thank you for your feedback", "Success", javax.swing.JOptionPane.INFORMATION_MESSAGE);
        
        txtFeed.setText("");
        loadTableFeed();
    }//GEN-LAST:event_btnSubmitFeedActionPerformed

    private void btnDeleteFeedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteFeedActionPerformed
        // TODO add your handling code here:
        int selectedRow = tblFeed.getSelectedRow();
        if (selectedRow == -1) {
        javax.swing.JOptionPane.showMessageDialog(this, "Please select a review from the table to delete!", "No Selection", javax.swing.JOptionPane.WARNING_MESSAGE);
        return;
        }
        
        int confirm = javax.swing.JOptionPane.showConfirmDialog(this, "Are you sure you want to delet this feedback?", "Confirm Delete", javax.swing.JOptionPane.YES_NO_OPTION);
        
        if (confirm != javax.swing.JOptionPane.YES_OPTION) 
        {
            return;
        }
        
        String feedbackID = tblFeed.getValueAt(selectedRow, 0).toString().trim();

        
        List<String> lines = services.FileHelper.readFile("data/feedbacks.txt");
        boolean removed = false;
        
        for (int i = 0; i < lines.size(); i++) 
        {
            String[] data = lines.get(i).split(",");
            
            if(data.length >= 6) 
            {
                String lineId = data[0].trim();
                
                if (lineId.equalsIgnoreCase(feedbackID)) 
                {
                    lines.remove(i);
                    removed = true;
                    break;
                }
            }
        }
        if (removed) 
        {
            services.FileHelper.writeFile("data/feedbacks.txt", lines);
            javax.swing.JOptionPane.showMessageDialog(this, "Feedback deleted successfully!");
            loadTableFeed();
        }
        else 
        {
            javax.swing.JOptionPane.showMessageDialog(this, "Failed to delete feedback.", "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnDeleteFeedActionPerformed

    private void btnFeedRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFeedRefreshActionPerformed
        // TODO add your handling code here:
        loadTableFeed();
    }//GEN-LAST:event_btnFeedRefreshActionPerformed

    private void btnConPresRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnConPresRefreshActionPerformed
        // TODO add your handling code here:
        loadTableConPres();
    }//GEN-LAST:event_btnConPresRefreshActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new PatientDashboard().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAppCancel;
    private javax.swing.JButton btnAppReschedule;
    private javax.swing.JButton btnBookApp;
    private javax.swing.JButton btnChangePass;
    private javax.swing.JButton btnConPresRefresh;
    private javax.swing.JButton btnDeleteFeed;
    private javax.swing.JButton btnFeedRefresh;
    private javax.swing.JButton btnLogOut;
    private javax.swing.JButton btnSetAsDefalt;
    private javax.swing.JButton btnSubmitFeed;
    private javax.swing.JButton btnUpdateProfile;
    private javax.swing.ButtonGroup buttonGroup7;
    private javax.swing.JComboBox<String> cboxAppDate;
    private javax.swing.JComboBox<String> cboxAppSelectDoc;
    private javax.swing.JComboBox<String> cboxFeedRating;
    private javax.swing.JComboBox<String> cboxFeedSelectDoc;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JLabel lblAppAvailable;
    private javax.swing.JLabel lblAppDate;
    private javax.swing.JLabel lblAppSelectDoc;
    private javax.swing.JLabel lblAppSymptom;
    private javax.swing.JLabel lblBookApp;
    private javax.swing.JLabel lblConsultation;
    private javax.swing.JLabel lblEmerCon;
    private javax.swing.JLabel lblFeed;
    private javax.swing.JLabel lblFeedSelectDoc;
    private javax.swing.JLabel lblHeader;
    private javax.swing.JLabel lblHeaderPatient;
    private javax.swing.JLabel lblPres;
    private javax.swing.JLabel lblProBloodType;
    private javax.swing.JLabel lblProEmail;
    private javax.swing.JLabel lblProID;
    private javax.swing.JLabel lblProName;
    private javax.swing.JLabel lblProName1;
    private javax.swing.JLabel lblProPassword;
    private javax.swing.JLabel lblProPhone;
    private javax.swing.JLabel lblProRole;
    private javax.swing.JLabel lblReview;
    private javax.swing.JTable tblAppointment;
    private javax.swing.JTable tblCon;
    private javax.swing.JTable tblFeed;
    private javax.swing.JTable tblPres;
    private javax.swing.JTextField txtEmerCon;
    private javax.swing.JTextArea txtFeed;
    private javax.swing.JTextField txtProBloodType;
    private javax.swing.JTextField txtProEmail;
    private javax.swing.JTextField txtProID;
    private javax.swing.JTextField txtProName;
    private javax.swing.JPasswordField txtProPassword;
    private javax.swing.JTextField txtProPhone;
    private javax.swing.JTextField txtProRole;
    private javax.swing.JTextField txtProUserName;
    private javax.swing.JTextArea txtSymptom;
    // End of variables declaration//GEN-END:variables
}
