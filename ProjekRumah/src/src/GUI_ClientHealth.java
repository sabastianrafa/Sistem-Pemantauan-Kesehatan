package src;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class GUI_ClientHealth extends javax.swing.JFrame {
    String nama, berat, tinggi, bmi, statusbmi;

    public GUI_ClientHealth() {
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
        tabelhead.addColumn("Berat Badan");
        tabelhead.addColumn("Tinggi Badan");
        tabelhead.addColumn("BMI");
        tabelhead.addColumn("Status BMI");

        try {
            koneksi();
            String sql = "SELECT * FROM tb_clienthealth";
            Statement stat = conn.createStatement();
            ResultSet res = stat.executeQuery(sql);
            while (res.next()) {
                tabelhead.addRow(new Object[]{
                    res.getString("nama"),
                    res.getString("berat"),
                    res.getString("tinggi"),
                    res.getString("bmi"),
                    res.getString("statusbmi")});
            }
            jTable1.setModel(tabelhead);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "BELUM TERKONEKSI: " + e.getMessage());
        }
    }

    public void refresh() {
        InNama.setText("");
        InBerat.setText("");
        InTinggi.setText("");
        tampil(); // panggil ulang tampil()
    }

    public void insert() {
        try {
            // Ambil dan validasi input
            String nama = InNama.getText().trim();
            if (nama.isEmpty()) throw new IllegalArgumentException("Nama tidak boleh kosong.");

            double berat = Double.parseDouble(InBerat.getText().trim());
            double tinggi = Double.parseDouble(InTinggi.getText().trim());

            // Buat objek ClientHealth dan hitung BMI serta status
            ClientHealth user = new ClientHealth(nama, berat, tinggi);
            double bmi = user.hitungBMI();
            String status = user.getInfo();

            // Simpan ke database
            koneksi();
            Statement statement = conn.createStatement();
            statement.executeUpdate("INSERT INTO tb_clienthealth (nama, berat, tinggi, bmi, statusbmi) "
                    + "VALUES('" + nama + "', '" + berat + "', '" + tinggi + "', '" + bmi + "', '" + status + "')");
            statement.close();

            // Tampilkan pesan sukses
            JOptionPane.showMessageDialog(null, "Berhasil Memasukkan Data!");

            // Perbarui tampilan tabel
            tampil();

            // Reset input
            InNama.setText("");
            InBerat.setText("");
            InTinggi.setText("");
            InNama.requestFocus();

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Berat dan Tinggi harus berupa angka!", "Input Error", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Input Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Terjadi Kesalahan Input!\n" + e.getMessage());
        }
    }
    
    //event update
    
    public void update() {
        int selectedRow = jTable1.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih data yang ingin diupdate pada tabel!");
            return;
        }

        String namaLama = jTable1.getValueAt(selectedRow, 0).toString(); // kolom 0 = nama
        String namaBaru = InNama.getText().trim();
        String beratBaruStr = InBerat.getText().trim();
        String tinggiBaruStr = InTinggi.getText().trim();

        if (namaBaru.isEmpty() || beratBaruStr.isEmpty() || tinggiBaruStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Semua field harus diisi!");
            return;
        }

        try {
            double beratBaru = Double.parseDouble(beratBaruStr);
            double tinggiBaru = Double.parseDouble(tinggiBaruStr);

            ClientHealth user = new ClientHealth(namaBaru, beratBaru, tinggiBaru);
            double bmiBaru = user.hitungBMI();
            String statusBaru = user.getInfo();

            koneksi();
            Statement stat = conn.createStatement();

            String sql = "UPDATE tb_clienthealth SET "
                       + "nama='" + namaBaru + "', "
                       + "berat='" + beratBaru + "', "
                       + "tinggi='" + tinggiBaru + "', "
                       + "bmi='" + bmiBaru + "', "
                       + "statusbmi='" + statusBaru + "' "
                       + "WHERE nama='" + namaLama + "'";

            stat.executeUpdate(sql);
            stat.close();

            JOptionPane.showMessageDialog(this, "Data berhasil diperbarui!");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Berat dan Tinggi harus berupa angka!", "Input Error", JOptionPane.ERROR_MESSAGE);
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
                Statement stat = conn.createStatement();
                String sql = "DELETE FROM tb_clienthealth WHERE nama='" + nama + "'";
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
        InBerat = new javax.swing.JTextField();
        InTinggi = new javax.swing.JTextField();
        Submit = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        Update = new javax.swing.JToggleButton();
        Delete = new javax.swing.JToggleButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        InBerat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                InBeratActionPerformed(evt);
            }
        });

        Submit.setText("Submit");
        Submit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SubmitActionPerformed(evt);
            }
        });

        jLabel1.setText("Nama");

        jLabel3.setText("Berat Badan");

        jLabel4.setText("Tinggi Badan");

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 48)); // NOI18N
        jLabel5.setText("Health");

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Nama", "Berat Badan", "Tinggi Badan", "BMI", "Status BMI"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Object.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
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
                        .addComponent(jLabel1)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel5)
                                .addGap(190, 190, 190))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(97, 97, 97)
                                .addComponent(InNama, javax.swing.GroupLayout.PREFERRED_SIZE, 317, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel3)
                                    .addComponent(jLabel4))
                                .addGap(59, 59, 59)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(InBerat)
                                    .addComponent(InTinggi, javax.swing.GroupLayout.PREFERRED_SIZE, 318, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addComponent(Submit)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(jScrollPane1))
                        .addContainerGap())))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(Update)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(Delete)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(InNama, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(InBerat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(InTinggi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Submit))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Update)
                    .addComponent(Delete))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void InBeratActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_InBeratActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_InBeratActionPerformed

    private void SubmitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SubmitActionPerformed
        insert();
    }//GEN-LAST:event_SubmitActionPerformed

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
            java.util.logging.Logger.getLogger(GUI_ClientHealth.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(GUI_ClientHealth.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(GUI_ClientHealth.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GUI_ClientHealth.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
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
                new GUI_ClientHealth().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JToggleButton Delete;
    private javax.swing.JTextField InBerat;
    private javax.swing.JTextField InNama;
    private javax.swing.JTextField InTinggi;
    private javax.swing.ButtonGroup JenisKelamin;
    private javax.swing.JButton Submit;
    private javax.swing.JToggleButton Update;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables
}
