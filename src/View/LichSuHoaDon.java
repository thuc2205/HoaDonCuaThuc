package View;

import Entity.HoaDon;
import repobanhang.HoaDnRepo;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import javax.swing.table.DefaultTableModel;

public class LichSuHoaDon extends javax.swing.JFrame {

    DefaultTableModel modelLichSuHoaDon;
    DefaultTableModel modelLichSuMuaHang;
    HoaDnRepo hdrepo = new HoaDnRepo();
    List<HoaDon> listHoaDon = new ArrayList<>();

    public LichSuHoaDon() {
        setLocationRelativeTo(null);
        initComponents();
        listHoaDon = hdrepo.getAllHoaDon();
        modelLichSuHoaDon = (DefaultTableModel) tblLichSuHoaDon.getModel();
        modelLichSuMuaHang = (DefaultTableModel) tblLichSuDatHang.getModel();
        rdoHuy.addActionListener(e -> showDaTAHoaDon());
        rdoDaThanhToan.addActionListener(e -> showDaTAHoaDon());
        chkShowAll.addActionListener(e -> showAllDataHD());
        showAllDataHD();
        showDaTAHoaDon();
    }

    private void showDaTAHoaDon() {
        modelLichSuHoaDon.setRowCount(0);

        List<String> trangThai = hdrepo.selectAllTrangThaiHoaDon();

        for (HoaDon h : listHoaDon) {
            String trangThaiHienTai = h.getTrangThai();

            if (rdoHuy.isSelected() && trangThai.contains(trangThaiHienTai) && trangThaiHienTai.equalsIgnoreCase("Huỷ")) {
                fillTable(h);
                chkShowAll.setSelected(false);
            } else if (rdoDaThanhToan.isSelected() && trangThai.contains(trangThaiHienTai) && trangThaiHienTai.equalsIgnoreCase("Đã Thanh Toán")) {
                fillTable(h);
                chkShowAll.setSelected(false);
            }
        }

        modelLichSuHoaDon.setColumnIdentifiers(new String[]{"STT ", "Mã HĐ", "Mã NV", "Mã KH", "Ngày tạo", "Tổng tiền", "Tiền khách đưa", "Tiền thừa", "Thanh Toán", "Trạng thái"});
    }

    final void showAllDataHD() {

        modelLichSuHoaDon.setRowCount(0);

        List<String> trangThai = hdrepo.selectAllTrangThaiHoaDon();

        for (HoaDon h : listHoaDon) {
            String trangThaiHienTai = h.getTrangThai();
            boolean showAll = chkShowAll.isSelected();
            if (showAll || (trangThai.contains(trangThaiHienTai) && trangThaiHienTai.equalsIgnoreCase("Huỷ"))
                    || (trangThai.contains(trangThaiHienTai) && trangThaiHienTai.equalsIgnoreCase("Đã Thanh Toán"))
                    || (trangThai.contains(trangThaiHienTai) && trangThaiHienTai.equalsIgnoreCase("Chờ Thanh Toán"))) {
                fillTable(h);
                buttonGroup1.clearSelection();
            } 
        }
        modelLichSuHoaDon.setColumnIdentifiers(new String[]{"STT ", "Mã HĐ", "Mã NV", "Mã KH", "Ngày tạo", "Tổng tiền", "Tiền khách đưa", "Tiền thừa", "Thanh Toán", "Trạng thái"});
    }

    private void fillTable(HoaDon h) {
        // Thêm chi tiết hóa đơn vào mô hình hiển thị
        Vector<Object> rowData = new Vector<>();
        rowData.add(modelLichSuHoaDon.getRowCount() + 1);
        rowData.add(h.getMaHoaDon());
        rowData.add(h.getNhanVien().getMa());
        rowData.add(h.getKhachHang().getMa());
        rowData.add(h.getNgayTao());
        rowData.add(h.getTongTien());
        rowData.add(h.getTienKhachDua());
        rowData.add(h.getTienThua());
        rowData.add(h.getHinhThucThanhToan());
        rowData.add(h.getTrangThai());
        modelLichSuHoaDon.addRow(rowData);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblLichSuHoaDon = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblLichSuDatHang = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        rdoHuy = new javax.swing.JRadioButton();
        rdoDaThanhToan = new javax.swing.JRadioButton();
        jComboBox2 = new javax.swing.JComboBox<>();
        jRadioButton3 = new javax.swing.JRadioButton();
        jRadioButton4 = new javax.swing.JRadioButton();
        jLabel3 = new javax.swing.JLabel();
        chkShowAll = new javax.swing.JCheckBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        tblLichSuHoaDon.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        tblLichSuHoaDon.setModel(new javax.swing.table.DefaultTableModel(
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
        tblLichSuHoaDon.setRowHeight(30);
        jScrollPane1.setViewportView(tblLichSuHoaDon);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 153, 51));
        jLabel1.setText("Hóa Đơn Chi Tiết");

        tblLichSuDatHang.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        tblLichSuDatHang.setModel(new javax.swing.table.DefaultTableModel(
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
        tblLichSuDatHang.setRowHeight(30);
        jScrollPane2.setViewportView(tblLichSuDatHang);

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 153, 51));
        jLabel2.setText("LỊCH SỬ HOÁ ĐƠN");

        buttonGroup1.add(rdoHuy);
        rdoHuy.setText("Huỷ ");

        buttonGroup1.add(rdoDaThanhToan);
        rdoDaThanhToan.setText("Đã Thanh Toán");

        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jRadioButton3.setText("trang thai 1");

        jRadioButton4.setText("trang thai 2");

        jLabel3.setFont(new java.awt.Font("Times New Roman", 3, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 0, 0));
        jLabel3.setText("Chọn trạng thái hóa đơn");

        chkShowAll.setText("Xem tất cả ?");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(chkShowAll, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(rdoHuy)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(rdoDaThanhToan))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(94, 94, 94)
                        .addComponent(jComboBox2, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(jRadioButton3)
                        .addGap(18, 18, 18)
                        .addComponent(jRadioButton4))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 890, Short.MAX_VALUE)
                    .addComponent(jScrollPane1))
                .addContainerGap(29, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(35, 35, 35)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(rdoDaThanhToan)
                            .addComponent(rdoHuy)
                            .addComponent(chkShowAll)
                            .addComponent(jLabel3))
                        .addGap(12, 12, 12))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel2)
                        .addGap(18, 18, 18)))
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 227, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 49, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jComboBox2)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jRadioButton3)
                        .addComponent(jRadioButton4)))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 291, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(41, 41, 41))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

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
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(LichSuHoaDon.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(LichSuHoaDon.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(LichSuHoaDon.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(LichSuHoaDon.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new LichSuHoaDon().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JCheckBox chkShowAll;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JRadioButton jRadioButton3;
    private javax.swing.JRadioButton jRadioButton4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JRadioButton rdoDaThanhToan;
    private javax.swing.JRadioButton rdoHuy;
    private javax.swing.JTable tblLichSuDatHang;
    private javax.swing.JTable tblLichSuHoaDon;
    // End of variables declaration//GEN-END:variables
}
