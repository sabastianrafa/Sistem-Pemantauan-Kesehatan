package src;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class GUI_Client extends javax.swing.JFrame {
    String nama, usia, jeniskelamin;

    public GUI_Client() {
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
        tabelhead.addColumn("Usia");
        tabelhead.addColumn("Jenis Kelamin");

        try {
            koneksi();
            String sql = "SELECT * FROM tb_biodata";
            Statement stat = conn.createStatement();
            ResultSet res = stat.executeQuery(sql);
            while (res.next()) {
                tabelhead.addRow(new Object[]{
                    res.getString("nama"),
                    res.getString("usia"),
                    res.getString("jeniskelamin"),});
            }
            jTable1.setModel(tabelhead);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "BELUM TERKONEKSI: " + e.getMessage());
        }
    }

    public void refresh() {
        InNama.setText("");
        InUsia.setText("");
        JenisKelamin.clearSelection(); // ButtonGroup dari RbLaki dan RbPerempuan
        tampil(); // panggil ulang tampil()
    }

    public void insert() {
        String Nama = InNama.getText();
        String Usia = InUsia.getText();
        String JenisKelamin = "";

        // Ambil nilai dari radio button
        if (RbLaki.isSelected()) {
            JenisKelamin = "Laki-laki";
        } else if (RbPerempuan.isSelected()) {
            JenisKelamin = "Perempuan";
        }

        // Validasi setelah nilai di-set
        if (Nama.isEmpty() || Usia.isEmpty() || JenisKelamin.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Semua field harus diisi!");
            return;
        }

        try {
            koneksi();
            Statement statement = conn.createStatement();
            statement.executeUpdate("INSERT INTO tb_biodata (nama, usia, jeniskelamin) "
                    + "VALUES('" + Nama + "','" + Usia + "','" + JenisKelamin + "')");
            statement.close();
            JOptionPane.showMessageDialog(null, "Berhasil Memasukkan Data!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Terjadi Kesalahan Input!\n" + e.getMessage());
        }

        refresh();
    }
    
    //event update
    public void update() {
        int selectedRow = jTable1.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih data yang ingin diupdate pada tabel!");
            return;
        }

        String namaLama = jTable1.getValueAt(selectedRow, 0).toString(); // kolom ke-0 = nama
        String namaBaru = InNama.getText();
        String usiaBaru = InUsia.getText();
        String jenisKelaminBaru = "";

        if (RbLaki.isSelected()) {
            jenisKelaminBaru = "Laki-laki";
        } else if (RbPerempuan.isSelected()) {
            jenisKelaminBaru = "Perempuan";
        }

        if (namaBaru.isEmpty() || usiaBaru.isEmpty() || jenisKelaminBaru.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Semua field harus diisi!");
            return;
        }

        try {
            koneksi();
            Statement stat = conn.createStatement();
            String sql = "UPDATE tb_biodata SET "
                       + "nama='" + namaBaru + "', "
                       + "usia='" + usiaBaru + "', "
                       + "jeniskelamin='" + jenisKelaminBaru + "' "
                       + "WHERE nama='" + namaLama + "'";
            stat.executeUpdate(sql);
            stat.close();

            JOptionPane.showMessageDialog(this, "Data berhasil diperbarui!");
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

        // Ambil nama dari baris yang dipilih (asumsi kolom 0 = nama)
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
                String sql = "DELETE FROM tb_biodata WHERE nama='" + nama + "'";
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
        InUsia = new javax.swing.JTextField();
        RbLaki = new javax.swing.JRadioButton();
        RbPerempuan = new javax.swing.JRadioButton();
        Submit = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        Update = new javax.swing.JToggleButton();
        Delete = new javax.swing.JToggleButton();
        jLabel3 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        InNama.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                InNamaActionPerformed(evt);
            }
        });

        InUsia.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                InUsiaActionPerformed(evt);
            }
        });

        JenisKelamin.add(RbLaki);
        RbLaki.setText("Laki-laki");
        RbLaki.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RbLakiActionPerformed(evt);
            }
        });

        JenisKelamin.add(RbPerempuan);
        RbPerempuan.setText("Perempuan");

        Submit.setText("Submit");
        Submit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SubmitActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 48)); // NOI18N
        jLabel1.setText("Data User");

        jLabel2.setText("Usia");

        jLabel6.setText("Jenis Kelamin");

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Nama", "Usia", "Jenis Kelamin"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Integer.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(jTable1);
        if (jTable1.getColumnModel().getColumnCount() > 0) {
            jTable1.getColumnModel().getColumn(0).setResizable(false);
            jTable1.getColumnModel().getColumn(1).setResizable(false);
        }

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

        jLabel3.setText("Nama");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 563, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(Update)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(Delete))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.LEADING))
                        .addGap(97, 97, 97)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(InUsia)
                            .addComponent(InNama)))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(jLabel6)
                                .addGap(226, 226, 226))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(RbLaki)
                                .addGap(18, 18, 18)
                                .addComponent(RbPerempuan)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(Submit, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addGap(175, 175, 175))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(10, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(InNama, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(19, 19, 19)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(InUsia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(RbLaki)
                    .addComponent(RbPerempuan)
                    .addComponent(jLabel6)
                    .addComponent(Submit))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Update)
                    .addComponent(Delete))
                .addGap(12, 12, 12))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void SubmitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SubmitActionPerformed
        insert(); // Simpan ke DB
    }//GEN-LAST:event_SubmitActionPerformed

    private void RbLakiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RbLakiActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_RbLakiActionPerformed

    private void InNamaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_InNamaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_InNamaActionPerformed

    private void InUsiaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_InUsiaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_InUsiaActionPerformed

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
            java.util.logging.Logger.getLogger(GUI_Client.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(GUI_Client.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(GUI_Client.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GUI_Client.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new GUI_Client().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JToggleButton Delete;
    private javax.swing.JTextField InNama;
    private javax.swing.JTextField InUsia;
    private javax.swing.ButtonGroup JenisKelamin;
    private javax.swing.JRadioButton RbLaki;
    private javax.swing.JRadioButton RbPerempuan;
    private javax.swing.JButton Submit;
    private javax.swing.JToggleButton Update;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables
}
