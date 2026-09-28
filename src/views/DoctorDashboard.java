/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package views;
import javax.swing.table.DefaultTableModel;
import java.util.List;

/**
 *
 * @author Tei Yuan Hong
 */
public class DoctorDashboard extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(DoctorDashboard.class.getName());
    private models.Doctor currentDoctor;
    
    /**
     * Creates new form DoctorDashboard
     */
    public DoctorDashboard() {
        initComponents();
    }
    
    public DoctorDashboard(models.Doctor doctor) {
    this.currentDoctor = doctor;
    initComponents();
    this.pack();
    
    lblHeaderDoctor.setText("Weicome back " + currentDoctor.getName() + "  ID: " + currentDoctor.getID());
    loadAppointments("All");
    loadLabRequests();
    loadProfile();
    
    }
    private void loadAppointments(String filterStatus) 
    {
        javax.swing.table.DefaultTableModel model = (javax.swing.table.DefaultTableModel) tblAppointments.getModel();
        model.setRowCount(0);  //empthy row
        
        List<String> lines = services.FileHelper.readFile ("data/appointments.txt");
        for (String line : lines) 
        {
            String[] data = line.split(",");
            
            if (data.length < 7) 
            {
                continue;
            }
            
            String appointmentId = data[0].trim();
            String patientId = data[1].trim();
            String patientName = data[2].trim();
            String doctorId = data[3].trim();
            String dateTime = data[4].trim();
            String reason = data[5].trim();
            String status = data[6].trim();
            
            if (!doctorId.equalsIgnoreCase(currentDoctor.getID()))  
            {
                continue;
            }
            
            if ("Today".equalsIgnoreCase(filterStatus)) 
            {
                String today = java.time.LocalDate.now().toString();
                if (today.equalsIgnoreCase(dateTime))
                {
                    model.addRow(new Object[] { appointmentId, patientId, patientName, dateTime, reason, status});
                }
            }
            
            else if ("Pending".equalsIgnoreCase(filterStatus))
            {
                if ("Pending".equalsIgnoreCase(status))
                {
                    model.addRow(new Object[] { appointmentId, patientId, patientName, dateTime, reason, status});
                }
            }
            
            else if ("Completed".equalsIgnoreCase(filterStatus)) 
            {
                if ("Completed".equalsIgnoreCase(status))
                {
                    model.addRow(new Object[] { appointmentId, patientId, patientName, dateTime, reason, status});
                }
            }
            
            else if ("Cancelled".equalsIgnoreCase(filterStatus)) 
            {
                if ("Cancelled".equalsIgnoreCase(status))
                {
                    model.addRow(new Object[] { appointmentId, patientId, patientName, dateTime, reason, status});
                }
            }
            
            else
            {
                model.addRow(new Object[] { appointmentId, patientId, patientName, dateTime, reason, status});
            }
            
        }
        
    }
    
    private void loadLabRequests() 
    {
        javax.swing.table.DefaultTableModel model = (javax.swing.table.DefaultTableModel) tblLab.getModel();
        model.setRowCount(0);
        
        List<String> lines = services.FileHelper.readFile("data/lab_tests.txt");
        for (String line : lines) 
        {
            String[] data = line.split(",");
            if (data.length < 7) 
            {
                continue;
            }
            
            String testId = data[0].trim();
            String patientId = data[1].trim();
            String doctorId = data[2].trim();
            String date = data[3].trim();
            String testType = data[4].trim();
            String result = data[5].trim();
            String status = data[6].trim();
            
            if (doctorId.equalsIgnoreCase(currentDoctor.getID())) 
            {
                model.addRow(new Object[] { testId, patientId, date, testType, status, result });
            }
        }
    }
    
    private void loadProfile() 
    {
        txtProID.setText(currentDoctor.getID());
        txtProName.setText(currentDoctor.getName());
        txtProUserName.setText(currentDoctor.getUsername());
        txtProRole.setText(currentDoctor.getRole());
        txtProPhone.setText(currentDoctor.getPhone());
        txtProEmail.setText(currentDoctor.getEmail());
        txtProSpe.setText(currentDoctor.getSpecialization());
        txtProRoom.setText(currentDoctor.getRoomNumber());
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        jPanel11 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        lblHeaderDoctor = new javax.swing.JLabel();
        btnLogOut = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        tabDoctor = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblAppointments = new javax.swing.JTable();
        lblFilterStatus = new javax.swing.JLabel();
        cboxFilterStatus = new javax.swing.JComboBox<>();
        btnCancelAppointment = new javax.swing.JButton();
        btnStartConsultation = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        lblAppID = new javax.swing.JLabel();
        lblPAID = new javax.swing.JLabel();
        lblPAName = new javax.swing.JLabel();
        lblSymptoms = new javax.swing.JLabel();
        txtAppID = new javax.swing.JTextField();
        txtPAID = new javax.swing.JTextField();
        txtPAName = new javax.swing.JTextField();
        jScrollPane3 = new javax.swing.JScrollPane();
        txtSymptoms = new javax.swing.JTextArea();
        jPanel7 = new javax.swing.JPanel();
        lblVitalSigns = new javax.swing.JLabel();
        lblBP = new javax.swing.JLabel();
        txtBP = new javax.swing.JTextField();
        lblTemp = new javax.swing.JLabel();
        txtTemp = new javax.swing.JTextField();
        lblConNote = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        txtConNote = new javax.swing.JTextArea();
        jPanel8 = new javax.swing.JPanel();
        lblMEName = new javax.swing.JLabel();
        txtMEName = new javax.swing.JTextField();
        lblInstruction = new javax.swing.JLabel();
        txtInstruction = new javax.swing.JTextField();
        lblRemark = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        txtRemark = new javax.swing.JTextArea();
        btnRequireLab = new javax.swing.JButton();
        lblRequireLab = new javax.swing.JLabel();
        jPanel12 = new javax.swing.JPanel();
        btnSaveCon = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        tblLab = new javax.swing.JTable();
        jPanel9 = new javax.swing.JPanel();
        lblLabPAID = new javax.swing.JLabel();
        lblLabPAName = new javax.swing.JLabel();
        lblTestType = new javax.swing.JLabel();
        txtLabPAID = new javax.swing.JTextField();
        txtLabPAName = new javax.swing.JTextField();
        cboxTestType = new javax.swing.JComboBox<>();
        btnSubmit = new javax.swing.JButton();
        btnClear = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jPanel10 = new javax.swing.JPanel();
        lblProID = new javax.swing.JLabel();
        txtProPhone = new javax.swing.JTextField();
        txtProEmail = new javax.swing.JTextField();
        lblProRole = new javax.swing.JLabel();
        txtProRole = new javax.swing.JTextField();
        lblProSpecialization = new javax.swing.JLabel();
        txtProSpe = new javax.swing.JTextField();
        lblProPhone = new javax.swing.JLabel();
        txtProRoom = new javax.swing.JTextField();
        lblProRoom = new javax.swing.JLabel();
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

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        jLabel2.setText("jLabel2");

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(0, 102, 102));

        lblHeaderDoctor.setFont(new java.awt.Font("Segoe UI", 2, 14)); // NOI18N
        lblHeaderDoctor.setForeground(new java.awt.Color(255, 255, 255));
        lblHeaderDoctor.setText("Welcome Dr. Tei Yuan Hong  ID: TP083451");
        lblHeaderDoctor.setToolTipText("");
        lblHeaderDoctor.setName("lblWelcome"); // NOI18N

        btnLogOut.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnLogOut.setText("Log Out");
        btnLogOut.addActionListener(this::btnLogOutActionPerformed);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Doctor Dashboard - APU Medical Centre");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblHeaderDoctor, javax.swing.GroupLayout.PREFERRED_SIZE, 312, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnLogOut, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(16, 16, 16))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(14, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnLogOut, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblHeaderDoctor))
                .addGap(15, 15, 15))
        );

        tabDoctor.setBackground(new java.awt.Color(153, 153, 153));
        tabDoctor.setFont(new java.awt.Font("Microsoft JhengHei", 0, 12)); // NOI18N

        jPanel2.setBackground(new java.awt.Color(204, 204, 204));

        tblAppointments.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Appointment ID", "Patient ID", "Patient Name", "Date & Time", "Symptoms", "Status"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(tblAppointments);

        lblFilterStatus.setText("Filter Status: ");
        lblFilterStatus.setName("lblFilterStatus"); // NOI18N

        cboxFilterStatus.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "All", "Pending", "Completed", "Today" }));
        cboxFilterStatus.setName("cboxFilterStatus"); // NOI18N
        cboxFilterStatus.addActionListener(this::cboxFilterStatusActionPerformed);

        btnCancelAppointment.setText("Cancel Appointment");
        btnCancelAppointment.setName("btnCancelAppointment"); // NOI18N
        btnCancelAppointment.addActionListener(this::btnCancelAppointmentActionPerformed);

        btnStartConsultation.setText("Start Consultation");
        btnStartConsultation.setName("btnStartConsultation"); // NOI18N
        btnStartConsultation.addActionListener(this::btnStartConsultationActionPerformed);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 633, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(lblFilterStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cboxFilterStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(123, 123, 123)
                        .addComponent(btnCancelAppointment, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(66, 66, 66)
                        .addComponent(btnStartConsultation, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(52, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(8, 8, 8)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblFilterStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboxFilterStatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 333, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCancelAppointment)
                    .addComponent(btnStartConsultation))
                .addContainerGap(73, Short.MAX_VALUE))
        );

        tabDoctor.addTab("Appointments", jPanel2);

        jPanel3.setBackground(new java.awt.Color(204, 204, 204));

        jPanel6.setBackground(new java.awt.Color(175, 175, 175));

        lblAppID.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblAppID.setText("Appointment ID: ");

        lblPAID.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblPAID.setText("Patient ID: ");

        lblPAName.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblPAName.setText("Name: ");

        lblSymptoms.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblSymptoms.setText("Symptoms: ");

        txtAppID.setEditable(false);

        txtPAID.setEditable(false);

        txtPAName.setEditable(false);

        txtSymptoms.setEditable(false);
        txtSymptoms.setColumns(20);
        txtSymptoms.setRows(5);
        jScrollPane3.setViewportView(txtSymptoms);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblPAID)
                    .addComponent(lblPAName)
                    .addComponent(lblAppID))
                .addGap(18, 18, 18)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtPAID, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtAppID, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lblSymptoms))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(txtPAName, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(47, 47, 47))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(11, 11, 11)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblAppID)
                            .addComponent(txtAppID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblSymptoms))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblPAID)
                            .addComponent(txtPAID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(3, 3, 3)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblPAName)
                            .addComponent(txtPAName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel7.setBackground(new java.awt.Color(175, 175, 175));

        lblVitalSigns.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        lblVitalSigns.setText("Vital Signs:");

        lblBP.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblBP.setText("BP: ");

        lblTemp.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblTemp.setText("Temp");

        lblConNote.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblConNote.setText("Consultation Notes : ");

        txtConNote.setColumns(20);
        txtConNote.setRows(5);
        jScrollPane4.setViewportView(txtConNote);

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(lblBP)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtBP, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblTemp)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtTemp, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(lblConNote, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 316, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblVitalSigns, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(14, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(lblVitalSigns)
                .addGap(18, 18, 18)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblBP)
                    .addComponent(txtBP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblTemp)
                    .addComponent(txtTemp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(28, 28, 28)
                .addComponent(lblConNote)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel8.setBackground(new java.awt.Color(175, 175, 175));

        lblMEName.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblMEName.setText("Medicine Name: ");

        lblInstruction.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblInstruction.setText("Dosage & Instructions: ");

        lblRemark.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblRemark.setText("Special Remarks: ");

        txtRemark.setColumns(20);
        txtRemark.setRows(5);
        jScrollPane5.setViewportView(txtRemark);

        btnRequireLab.setText("Click Here");
        btnRequireLab.addActionListener(this::btnRequireLabActionPerformed);

        lblRequireLab.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblRequireLab.setText("Require Lab or Imaging: ");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(lblRemark)
                        .addComponent(lblInstruction)
                        .addComponent(lblMEName)
                        .addComponent(txtMEName)
                        .addComponent(txtInstruction)
                        .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 271, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btnRequireLab, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblRequireLab, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(45, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(lblMEName)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtMEName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(21, 21, 21)
                .addComponent(lblInstruction)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtInstruction, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(lblRemark)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 7, Short.MAX_VALUE)
                .addComponent(lblRequireLab)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnRequireLab)
                .addGap(22, 22, 22))
        );

        jPanel12.setBackground(new java.awt.Color(175, 175, 175));

        btnSaveCon.setText("Save & Complete Consultation");
        btnSaveCon.addActionListener(this::btnSaveConActionPerformed);

        jButton2.setText("Clear");
        jButton2.addActionListener(this::jButton2ActionPerformed);

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addGap(136, 136, 136)
                .addComponent(btnSaveCon)
                .addGap(42, 42, 42)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 193, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel12Layout.createSequentialGroup()
                .addContainerGap(19, Short.MAX_VALUE)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSaveCon)
                    .addComponent(jButton2))
                .addGap(14, 14, 14))
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 12, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(22, Short.MAX_VALUE))
        );

        tabDoctor.addTab("Consultation & Records", jPanel3);

        jPanel4.setBackground(new java.awt.Color(204, 204, 204));

        tblLab.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Request ID", "Patient ID", "Date", "Test Type", "Status", "Result"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane6.setViewportView(tblLab);

        jPanel9.setBackground(new java.awt.Color(175, 175, 175));

        lblLabPAID.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblLabPAID.setText("Patient ID: ");

        lblLabPAName.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblLabPAName.setText("Patient Name: ");

        lblTestType.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblTestType.setText("Test Type:");

        cboxTestType.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Chest X-Ray", "Blood Test (Full Blood Count)", "Urine Analysis", "CT Scan (Abdomen)", "MRI Scan", "Ultrasound" }));

        btnSubmit.setText("Submit Request to Admin");
        btnSubmit.addActionListener(this::btnSubmitActionPerformed);

        btnClear.setText("Clear");
        btnClear.addActionListener(this::btnClearActionPerformed);

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblLabPAName)
                    .addComponent(lblLabPAID))
                .addGap(18, 18, 18)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addComponent(btnSubmit, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(28, 28, 28)
                        .addComponent(btnClear, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtLabPAName, javax.swing.GroupLayout.DEFAULT_SIZE, 168, Short.MAX_VALUE)
                            .addComponent(txtLabPAID))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 76, Short.MAX_VALUE)
                        .addComponent(lblTestType, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(32, 32, 32)
                        .addComponent(cboxTestType, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(74, 74, 74))))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblLabPAID)
                    .addComponent(lblTestType)
                    .addComponent(txtLabPAID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboxTestType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(12, 12, 12)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblLabPAName)
                    .addComponent(txtLabPAName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(92, 92, 92)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSubmit)
                    .addComponent(btnClear))
                .addContainerGap(15, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane6)
                    .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 245, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tabDoctor.addTab("Lab & Imaging Requests", jPanel4);

        jPanel5.setBackground(new java.awt.Color(204, 204, 204));

        lblProID.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblProID.setText("Doctor ID: ");

        lblProRole.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblProRole.setText("Role: ");

        txtProRole.setEditable(false);

        lblProSpecialization.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblProSpecialization.setText("Specialization: ");

        txtProSpe.setEditable(false);

        lblProPhone.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblProPhone.setText("Phone: ");

        txtProRoom.setEditable(false);

        lblProRoom.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblProRoom.setText("Room Number: ");

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

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                .addContainerGap(91, Short.MAX_VALUE)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addGap(44, 44, 44)
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblProName, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel10Layout.createSequentialGroup()
                                .addGap(124, 124, 124)
                                .addComponent(txtProUserName, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel10Layout.createSequentialGroup()
                                .addComponent(lblProID)
                                .addGap(63, 63, 63)
                                .addComponent(txtProID, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(lblProName1)
                            .addGroup(jPanel10Layout.createSequentialGroup()
                                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(jPanel10Layout.createSequentialGroup()
                                        .addComponent(lblProRoom)
                                        .addGap(35, 35, 35)
                                        .addComponent(txtProRoom))
                                    .addGroup(jPanel10Layout.createSequentialGroup()
                                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(lblProPhone)
                                            .addComponent(lblProEmail)
                                            .addComponent(lblProRole)
                                            .addComponent(lblProSpecialization)
                                            .addComponent(lblProPassword))
                                        .addGap(39, 39, 39)
                                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(txtProSpe)
                                            .addComponent(txtProRole)
                                            .addComponent(txtProEmail)
                                            .addComponent(txtProPhone)
                                            .addComponent(txtProPassword)
                                            .addComponent(txtProName, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                .addGap(18, 18, 18)
                                .addComponent(btnChangePass))))
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addComponent(btnUpdateProfile, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(55, 55, 55)
                        .addComponent(btnSetAsDefalt, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(66, 66, 66))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblProID)
                    .addComponent(txtProID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblProName)
                    .addComponent(txtProUserName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblProName1)
                    .addComponent(txtProName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblProPassword)
                    .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtProPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnChangePass)))
                .addGap(11, 11, 11)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblProPhone)
                    .addComponent(txtProPhone, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblProEmail)
                    .addComponent(txtProEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblProRole)
                    .addComponent(txtProRole, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblProSpecialization)
                    .addComponent(txtProSpe, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblProRoom)
                    .addComponent(txtProRoom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 31, Short.MAX_VALUE)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
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
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(59, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(26, Short.MAX_VALUE))
        );

        tabDoctor.addTab("My Profile", jPanel5);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tabDoctor))
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tabDoctor)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cboxFilterStatusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboxFilterStatusActionPerformed
        // TODO add your handling code here:
        if (cboxFilterStatus.getSelectedItem() != null) 
        {
            String selected = cboxFilterStatus.getSelectedItem().toString();
            loadAppointments(selected);
        }
    }//GEN-LAST:event_cboxFilterStatusActionPerformed

    private void btnCancelAppointmentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelAppointmentActionPerformed
        // TODO add your handling code here:
        int selectedRow = tblAppointments.getSelectedRow();
        if (selectedRow == -1) 
        {
            javax.swing.JOptionPane.showMessageDialog(this, "Please select an appointment from the table to cancel", "No Selection", javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String appId = tblAppointments.getValueAt(selectedRow, 0).toString();
        int statusCol = 5;
        String currentStatus = tblAppointments.getValueAt(selectedRow, statusCol).toString();
        
        if ("Completed".equalsIgnoreCase(currentStatus) || "Cancelled".equalsIgnoreCase(currentStatus)) 
        {
            javax.swing.JOptionPane.showMessageDialog(this, "Cannot cancel appointment that mark canceled or completed", "Warning", javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int confirm = javax.swing.JOptionPane.showConfirmDialog(this, "Are you sure you want to cancel appointment " + appId + "?", "Confirm Cancellation", javax.swing.JOptionPane.YES_NO_OPTION);
        
        if (confirm != javax.swing.JOptionPane.YES_OPTION)
        {
            return;
        }
        
        boolean success = services.FileHelper.updateAppointmentContent(appId, 6, "Cancelled");
        if (success) 
        {
            javax.swing.JOptionPane.showMessageDialog(this, "Appointment (" + appId + ") cancelled successfully!");
            loadAppointments(cboxFilterStatus.getSelectedItem().toString());
        }
        else 
        {
            javax.swing.JOptionPane.showMessageDialog(this, "Failed to cancel appointment.", "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
        }

    }//GEN-LAST:event_btnCancelAppointmentActionPerformed

    private void btnStartConsultationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnStartConsultationActionPerformed
        // TODO add your handling code here:
        int selectedRow = tblAppointments.getSelectedRow();
        if (selectedRow == -1) 
        {
            javax.swing.JOptionPane.showMessageDialog(this, "Please select an appointment from the table to continue", "No Selection", javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String appId = tblAppointments.getValueAt(selectedRow, 0).toString();
        int statusCol = 5;
        String currentStatus = tblAppointments.getValueAt(selectedRow, statusCol).toString();
        
        if ("Completed".equalsIgnoreCase(currentStatus) || "Cancelled".equalsIgnoreCase(currentStatus)) 
        {
            javax.swing.JOptionPane.showMessageDialog(this, "You can only start consultation for 'Pending' apponntments! This appointment have been " + currentStatus, "Warning", javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String patientId = tblAppointments.getValueAt(selectedRow, 1).toString();
        String patientName = tblAppointments.getValueAt(selectedRow, 2).toString();
        String symptoms = tblAppointments.getValueAt(selectedRow, 4).toString();
        
        
        txtAppID.setText(appId);
        txtPAID.setText(patientId);
        txtPAName.setText(patientName);
        txtSymptoms.setText(symptoms);
        
        tabDoctor.setSelectedIndex(1);
    }//GEN-LAST:event_btnStartConsultationActionPerformed

    private void btnSaveConActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveConActionPerformed
        // TODO add your handling code here:
        String appId = txtAppID.getText().trim();
        String patientId = txtPAID.getText().trim();
        String bp = txtBP.getText().trim();
        String temp = txtTemp.getText().trim();
        String vitals = "BP: " + bp + ". Temp: " + temp;
        
        String notes = txtConNote.getText().trim();
        
        String medicine = txtMEName.getText().trim();
        String dosage = txtInstruction.getText().trim();
        String remark = txtRemark.getText().trim();
        
        
        if (patientId.isEmpty()) 
        {
            javax.swing.JOptionPane.showMessageDialog(this, "Pls select a appointment first!", "Incomplete Information", javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        else if (bp.isEmpty() || temp.isEmpty() || notes.isEmpty()) 
        {
            javax.swing.JOptionPane.showMessageDialog(this, "Please fill in BP, Temperature and Consultation Notes!", "Incomplete Information", javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        currentDoctor.logVitals(patientId, vitals, notes);
        
        if (!medicine.isEmpty()) 
        {
            if (!remark.isEmpty()) 
            {
                dosage += "(Remarks: " + remark + ")";
            }
            
            currentDoctor.issuePrescription(patientId, medicine, dosage);
        }
        
        boolean updated = services.FileHelper.updateAppointmentContent(appId, 6, "Completed");
        
        if (updated) 
        {
            javax.swing.JOptionPane.showMessageDialog(this, "Consultation completed and saved successfully", "Success", javax.swing.JOptionPane.INFORMATION_MESSAGE);
            txtBP.setText("");
            txtTemp.setText("");
            txtConNote.setText("");
            txtMEName.setText("");
            txtInstruction.setText("");
            txtRemark.setText("");
            txtAppID.setText("");
            txtPAID.setText("");
            txtPAName.setText("");
            txtSymptoms.setText("");
            
            tabDoctor.setSelectedIndex(0);
            loadAppointments(cboxFilterStatus.getSelectedItem().toString());
        } else {
            javax.swing.JOptionPane.showMessageDialog(this, "Failed to update appointment status in file.", "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
            
        }
    }//GEN-LAST:event_btnSaveConActionPerformed

    private void btnRequireLabActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRequireLabActionPerformed
        // TODO add your handling code here:
        String patientId = txtPAID.getText().trim();
        String patientName = txtPAName.getText().trim();
        if (patientId.isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(this, "No active patient selected!", "Warning", javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        txtLabPAID.setText(patientId);
        txtLabPAName.setText(patientName);
        
        tabDoctor.setSelectedIndex(2);
    }//GEN-LAST:event_btnRequireLabActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        txtBP.setText("");
        txtTemp.setText("");
        txtConNote.setText("");
        txtMEName.setText("");
        txtInstruction.setText("");
        txtRemark.setText("");
    }//GEN-LAST:event_jButton2ActionPerformed

    private void btnSubmitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSubmitActionPerformed
        // TODO add your handling code here:
        String patientId = txtLabPAID.getText().trim();
        String testType = cboxTestType.getSelectedItem().toString();

        
        if (patientId.isEmpty()) 
        {
            javax.swing.JOptionPane.showMessageDialog(this, "Please fill in or select a patient from Consultation tab first!", "No Patient Selected", javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String findPatientName = services.FileHelper.findNameBasedID(patientId, "Patient");
        if (findPatientName == null) 
        {
            javax.swing.JOptionPane.showMessageDialog(this, "Patient ID not found in system. Please check again.", "Invalid Patient ID", javax.swing.JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        txtLabPAName.setText(findPatientName);
        
        
        currentDoctor.requestsTest(patientId, testType);
        
        javax.swing.JOptionPane.showMessageDialog(this, "Request submitted to Admin successfully", "Sucucess", javax.swing.JOptionPane.INFORMATION_MESSAGE);
        
        txtLabPAID.setText("");
        txtLabPAName.setText("");

        
        loadLabRequests();
        
        
    }//GEN-LAST:event_btnSubmitActionPerformed

    private void btnClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearActionPerformed
        // TODO add your handling code here:
        txtLabPAID.setText("");
        txtLabPAName.setText("");
    }//GEN-LAST:event_btnClearActionPerformed

    private void btnUpdateProfileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateProfileActionPerformed
        // TODO add your handling code here:
        String newPhone = txtProPhone.getText().trim();
        String newEmail = txtProEmail.getText().trim();
        String newName = txtProName.getText().trim();
        
        if (newPhone.isEmpty() || newEmail.isEmpty() || newName.isEmpty()) 
        {
            javax.swing.JOptionPane.showMessageDialog(this, "Please do not leave Name, Phone or Email empty!", "Invalid Input", javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int confirm = javax.swing.JOptionPane.showConfirmDialog(this, "Are you sure you want update profile? Name: "  + newName + " Phone: " + newPhone + " Email: " + newEmail, "Confirmation", javax.swing.JOptionPane.YES_NO_OPTION);
        
        if (confirm !=javax.swing.JOptionPane.YES_OPTION) 
        {
            loadProfile();
            return;
        }
        
        services.UserService userService = new services.UserService();
        
        boolean success = userService.updateUserProfile(currentDoctor.getID(), newName, newPhone, newEmail);
        
        if (success) 
        {
            currentDoctor.updateProfile(newName, newPhone, newEmail);
            
            lblHeaderDoctor.setText("Welcome Dr " + currentDoctor.getName() + " ID: " + currentDoctor.getID());
            
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
        new ChangePassword(currentDoctor.getID()).setVisible(true);
    }//GEN-LAST:event_btnChangePassActionPerformed

    private void btnLogOutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLogOutActionPerformed
        // TODO add your handling code here:
        int confirm = javax.swing.JOptionPane.showConfirmDialog(this, "Are you sure you want to log out?", "Logout Confirmation", javax.swing.JOptionPane.YES_NO_OPTION);
        if (confirm == javax.swing.JOptionPane.YES_OPTION) {
        new Login().setVisible(true);
        this.dispose();
        }
    }//GEN-LAST:event_btnLogOutActionPerformed

    /**
     * @param args the command line arguments
     */
public static void main(String args[]) {
        try {
            // 设置为当前电脑系统的原生现代皮肤（Windows 扁平风格）
            javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(DoctorDashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new DoctorDashboard().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancelAppointment;
    private javax.swing.JButton btnChangePass;
    private javax.swing.JButton btnClear;
    private javax.swing.JButton btnLogOut;
    private javax.swing.JButton btnRequireLab;
    private javax.swing.JButton btnSaveCon;
    private javax.swing.JButton btnSetAsDefalt;
    private javax.swing.JButton btnStartConsultation;
    private javax.swing.JButton btnSubmit;
    private javax.swing.JButton btnUpdateProfile;
    private javax.swing.JComboBox<String> cboxFilterStatus;
    private javax.swing.JComboBox<String> cboxTestType;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JTable jTable1;
    private javax.swing.JLabel lblAppID;
    private javax.swing.JLabel lblBP;
    private javax.swing.JLabel lblConNote;
    private javax.swing.JLabel lblFilterStatus;
    private javax.swing.JLabel lblHeaderDoctor;
    private javax.swing.JLabel lblInstruction;
    private javax.swing.JLabel lblLabPAID;
    private javax.swing.JLabel lblLabPAName;
    private javax.swing.JLabel lblMEName;
    private javax.swing.JLabel lblPAID;
    private javax.swing.JLabel lblPAName;
    private javax.swing.JLabel lblProEmail;
    private javax.swing.JLabel lblProID;
    private javax.swing.JLabel lblProName;
    private javax.swing.JLabel lblProName1;
    private javax.swing.JLabel lblProPassword;
    private javax.swing.JLabel lblProPhone;
    private javax.swing.JLabel lblProRole;
    private javax.swing.JLabel lblProRoom;
    private javax.swing.JLabel lblProSpecialization;
    private javax.swing.JLabel lblRemark;
    private javax.swing.JLabel lblRequireLab;
    private javax.swing.JLabel lblSymptoms;
    private javax.swing.JLabel lblTemp;
    private javax.swing.JLabel lblTestType;
    private javax.swing.JLabel lblVitalSigns;
    private javax.swing.JTabbedPane tabDoctor;
    private javax.swing.JTable tblAppointments;
    private javax.swing.JTable tblLab;
    private javax.swing.JTextField txtAppID;
    private javax.swing.JTextField txtBP;
    private javax.swing.JTextArea txtConNote;
    private javax.swing.JTextField txtInstruction;
    private javax.swing.JTextField txtLabPAID;
    private javax.swing.JTextField txtLabPAName;
    private javax.swing.JTextField txtMEName;
    private javax.swing.JTextField txtPAID;
    private javax.swing.JTextField txtPAName;
    private javax.swing.JTextField txtProEmail;
    private javax.swing.JTextField txtProID;
    private javax.swing.JTextField txtProName;
    private javax.swing.JPasswordField txtProPassword;
    private javax.swing.JTextField txtProPhone;
    private javax.swing.JTextField txtProRole;
    private javax.swing.JTextField txtProRoom;
    private javax.swing.JTextField txtProSpe;
    private javax.swing.JTextField txtProUserName;
    private javax.swing.JTextArea txtRemark;
    private javax.swing.JTextArea txtSymptoms;
    private javax.swing.JTextField txtTemp;
    // End of variables declaration//GEN-END:variables
}
