package src;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class GUI_ClientMonitor extends javax.swing.JFrame {
    String nama, konsumsikalori, diet, status;
    
    public GUI_ClientMonitor() {
        initComponents();
        tampil();
    }

    public Connection conn;

    public void koneksi() throws SQLException {
        try {
            conn = null;
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/db_kesehatan_java?serverTimezone=UTC",
                    "root",
                    "");
            System.out.println("Koneksi berhasil!");
        } catch (ClassNotFoundException ex) {
            System.out.println("Driver tidak ditemukan.");
            ex.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Gagal koneksi ke database.");
            e.printStackTrace();
        } catch (Exception es) {
            System.out.println("Terjadi kesalahan umum.");
            es.printStackTrace();
        }
    }

    public void tampil() {
        DefaultTableModel tabelhead = new DefaultTableModel();
        tabelhead.addColumn("Nama");
        tabelhead.addColumn("Konsumsi Kalori");
        tabelhead.addColumn("Diet");
        tabelhead.addColumn("Status");

        try {
            koneksi();
            String sql = "SELECT * FROM tb_clientmonitoring";
            Statement stat = conn.createStatement();
            ResultSet res = stat.executeQuery(sql);
            while (res.next()) {
                tabelhead.addRow(new Object[]{
                    res.getString("nama"),
                    res.getString("konsumsikalori"),
                    res.getString("diet"),
                    res.getString("status")});
            }
            jTable1.setModel(tabelhead);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "BELUM TERKONEKSI: " + e.getMessage());
        }
    }

    public void refresh() {
        InNama.setText("");
        InKosumsiKalori.setText("");
        tampil();
    }

    public void insert() {
        String Nama = InNama.getText();
        String Kaori = InKosumsiKalori.getText();
        boolean isDiet = CheckBoxDiet.isSelected();
        String Diet;
        
        if (isDiet == true){
            Diet = "Ya";
        } else {
            Diet = "Tidak";
        }
        
        ClientMonitor user = new ClientMonitor(Nama, Integer.parseInt(Kaori));
        String status = user.evaluasiKalori(Integer.parseInt(Kaori), isDiet);

        try {
            koneksi();
            Statement statement = conn.createStatement();
            statement.executeUpdate("INSERT INTO tb_clientmonitoring (nama, konsumsikalori, diet, status) "
                    + "VALUES('" + Nama + "', '" + Kaori + "', '" + Diet + "', '" + status + "')");
            statement.close();
            JOptionPane.showMessageDialog(null, "Berhasil Memasukkan Data!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Terjadi Kesalahan Input!\n" + e.getMessage());
        }

        refresh();
    }
    
    public void update() {
        int selectedRow = jTable1.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih data yang ingin diupdate pada tabel!");
            return;
        }

        String namaLama = jTable1.getValueAt(selectedRow, 0).toString();
        String namaBaru = InNama.getText().trim();
        String kaloriStr = InKosumsiKalori.getText().trim();
        boolean isDiet = CheckBoxDiet.isSelected();
        String dietBaru = isDiet ? "Ya" : "Tidak";

        // Validasi input
        if (namaBaru.isEmpty() || kaloriStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nama dan Kalori tidak boleh kosong!");
            return;
        }

        try {
            int kaloriBaru = Integer.parseInt(kaloriStr);

            ClientMonitor user = new ClientMonitor(namaBaru, kaloriBaru);
            user.setDiet(isDiet);
            String statusBaru = user.evaluasiKalori(kaloriBaru, isDiet);

            koneksi();
            String sql = "UPDATE tb_clientmonitoring SET "
                       + "nama='" + namaBaru + "', "
                       + "konsumsikalori='" + kaloriBaru + "', "
                       + "diet='" + dietBaru + "', "
                       + "status='" + statusBaru + "' "
                       + "WHERE nama='" + namaLama + "'";
            Statement stat = conn.createStatement();
            stat.executeUpdate(sql);
            stat.close();

            JOptionPane.showMessageDialog(this, "Data berhasil diperbarui!");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Kalori harus berupa angka!", "Input Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Terjadi kesalahan saat update: " + e.getMessage());
        }

        refresh();
    }
    
    public void delete() {
        int selectedRow = jTable1.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih data yang ingin dihapus dari tabel!");
            return;
        }

        String nama = jTable1.getValueAt(selectedRow, 0).toString();

        int konfirmasi = JOptionPane.showConfirmDialog(
            this,
            "Apakah kamu yakin ingin menghapus data dengan nama \"" + nama + "\"?",
            "Konfirmasi Hapus",
            JOptionPane.YES_NO_OPTION
        );

        if (konfirmasi == JOptionPane.YES_OPTION) {
            try {
                koneksi();
                String sql = "DELETE FROM tb_clientmonitoring WHERE nama='" + nama + "'";
                Statement stat = conn.createStatement();
                stat.executeUpdate(sql);
                stat.close();

                JOptionPane.showMessageDialog(this, "Data berhasil dihapus!");
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Terjadi kesalahan saat menghapus: " + e.getMessage());
            }

            refresh();
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        JenisKelamin = new javax.swing.ButtonGroup();
        InNama = new javax.swing.JTextField();
        InKosumsiKalori = new javax.swing.JTextField();
        Submit = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        CheckBoxDiet = new javax.swing.JCheckBox();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        Update = new javax.swing.JToggleButton();
        Delete = new javax.swing.JToggleButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        InNama.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                InNamaActionPerformed(evt);
            }
        });

        InKosumsiKalori.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                InKosumsiKaloriActionPerformed(evt);
            }
        });

        Submit.setText("Submit");
        Submit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SubmitActionPerformed(evt);
            }
        });

        jLabel1.setText("Nama");

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 48)); // NOI18N
        jLabel5.setText("Monitoring");

        jLabel3.setText("Konsumsi Kalori");

        CheckBoxDiet.setText("Diet");
        CheckBoxDiet.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CheckBoxDietActionPerformed(evt);
            }
        });

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Nama", "Kosumsi Kalori", "Diet", "Status"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        Update.setText("Upadte");
        Update.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                UpdateActionPerformed(evt);
            }
        });

        Delete.setText("Delete");
        Delete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DeleteActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jScrollPane1)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel1)
                                    .addComponent(jLabel3))
                                .addGap(50, 50, 50)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(InKosumsiKalori, javax.swing.GroupLayout.PREFERRED_SIZE, 316, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(Submit))
                                    .addComponent(InNama, javax.swing.GroupLayout.PREFERRED_SIZE, 316, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(CheckBoxDiet, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(0, 8, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addGap(143, 143, 143))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(Update)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(Delete)
                                .addContainerGap())))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(InNama, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(CheckBoxDiet)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(InKosumsiKalori, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(Submit))
                .addGap(24, 24, 24)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Update)
                    .addComponent(Delete))
                .addContainerGap(15, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void SubmitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SubmitActionPerformed
        insert();
    }//GEN-LAST:event_SubmitActionPerformed

    private void InNamaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_InNamaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_InNamaActionPerformed

    private void InKosumsiKaloriActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_InKosumsiKaloriActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_InKosumsiKaloriActionPerformed

    private void CheckBoxDietActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CheckBoxDietActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_CheckBoxDietActionPerformed

    private void UpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_UpdateActionPerformed
        // TODO add your handling code here:
        update();
    }//GEN-LAST:event_UpdateActionPerformed

    private void DeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DeleteActionPerformed
        // TODO add your handling code here:
        delete();
    }//GEN-LAST:event_DeleteActionPerformed

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
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(GUI_ClientMonitor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(GUI_ClientMonitor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(GUI_ClientMonitor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GUI_ClientMonitor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new GUI_ClientMonitor().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox CheckBoxDiet;
    private javax.swing.JToggleButton Delete;
    private javax.swing.JTextField InKosumsiKalori;
    private javax.swing.JTextField InNama;
    private javax.swing.ButtonGroup JenisKelamin;
    private javax.swing.JButton Submit;
    private javax.swing.JToggleButton Update;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables
}
