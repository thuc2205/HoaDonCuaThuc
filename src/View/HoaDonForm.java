/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package View;

import Entity.GiayChiTiet;
import Entity.HoaDon;
import Entity.HoaDonChiTiet;
import Repository.GiayChiTietRepo;
import Repository.HoaDnRepo;
import Repository.HoaDonChiTietRepo;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Admin
 */
public class HoaDonForm extends javax.swing.JFrame {

    HoaDonChiTietRepo hdctrepo = new HoaDonChiTietRepo();
    GiayChiTietRepo gct = new GiayChiTietRepo();
    HoaDnRepo hdrepo = new HoaDnRepo();

    List<GiayChiTiet> listGiayChiTiet = new ArrayList<>();
    List<HoaDonChiTiet> listHoaDonChiTiet = new ArrayList<>();
    List<HoaDon> listHoaDon = new ArrayList<>();

    DefaultTableModel model;
    DefaultTableModel modelListGioHang;
    DefaultTableModel modelListHoaDon;

    /**
     * Creates new form HoaDonForm
     */
    public HoaDonForm() {

        initComponents();
        listGiayChiTiet = gct.getAllGiay();
        listHoaDonChiTiet = hdctrepo.getAllHoaDon();
        listHoaDon = hdrepo.getAllHoaDon();

        setTitle("Hoá Đơn");
        model = (DefaultTableModel) tblDanhSachSp.getModel();
        modelListGioHang = (DefaultTableModel) tblGioHangCho.getModel();
        modelListHoaDon = (DefaultTableModel) tblListHoaDon.getModel();
        modelListGioHang.setRowCount(0);

        column();
        column1();
        column2();

        showDataSanPham();
        showDaTAHoaDon();

    }

    private void showDataSanPham() {
        listGiayChiTiet = gct.getAllGiay();
        model.setRowCount(0);
        for (GiayChiTiet g : listGiayChiTiet) {
            Vector<Object> v = new Vector<>();
            v.add(g.getGiay().getMa());
            v.add(g.getGiay().getName());
            v.add(g.getHang().getName());
            v.add(g.getMauSac().getName());
            v.add(g.getKichCo().getSize());
            v.add(g.getSoLuong());
            v.add(g.getGia());
            model.addRow(v);

        }

    }

    private BigDecimal tinhVaThemTongTien(int columnIndex) {
        BigDecimal tongTien = BigDecimal.ZERO;
        int rowCount = tblGioHangCho.getRowCount();

        for (int i = 0; i < rowCount; i++) {
            BigDecimal giaTien = new BigDecimal(tblGioHangCho.getValueAt(i, columnIndex).toString());
            tongTien = tongTien.add(giaTien);
            lblTongTien.setText(tongTien.toString());
            lblTongTien1.setText(tongTien.toString());
        }

        return tongTien;
    }

    private void showDataGoHang(String id) {
        try {
            listHoaDonChiTiet = hdctrepo.getAllHoaDonChiTietByHoaDonID(id);

            modelListGioHang.setRowCount(0);

            for (HoaDonChiTiet h : listHoaDonChiTiet) {
                String g = h.getiDhoaDon();
                // Kiểm tra xem chi tiết hoá đơn có thuộc vào ID hoá đơn cần tìm hay không
                if (g.equals(id)) {
                    modelListGioHang.addRow(new Object[]{
                        modelListGioHang.getRowCount() + 1,
                        h.getGiayChiTiet().getGiay().getMa(),
                        h.getGiayChiTiet().getGiay().getName(),
                        h.getSoLuong(),
                        h.getGia(),
                        h.tongTien()
                    });
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(HoaDonForm.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void showDaTAHoaDon() {
        listHoaDon = hdrepo.getAllHoaDon();
        modelListHoaDon.setRowCount(0);
        for (HoaDon h : listHoaDon) {
            Vector<Object> rowData = new Vector<>();
            rowData.add(modelListHoaDon.getRowCount() + 1);
            rowData.add(h.getMaHoaDon());
            rowData.add(h.getNgayTao());
            rowData.add(h.getNhanVien().getMa());
            rowData.add(h.getTrangThai());
            modelListHoaDon.addRow(rowData);

        }

    }

    private void selectmaHoaDon() {
        String maHoaDon = hdrepo.selectMaHoaDon();
        txtMaHoaDon.setText(maHoaDon);
        txtMaHoaDon2.setText(maHoaDon);
    }

    private HoaDon getForm() {

        HoaDon h = new HoaDon();
        h.setTrangThai("Chờ Thanh Toán");
        h.setTongTien(new BigDecimal(lblTongTien.getText()));
        listHoaDon.add(h);
        return h;

    }

    private void column() {
        model.setColumnIdentifiers(new String[]{"Ma SP", "Ten SP ", "Hang", "MauSac", "Kich Co", "SoLuong", "DonGia"});
    }

    private void column1() {
        modelListGioHang.setColumnIdentifiers(new String[]{"STT", "MA SP", "TEN SP", "SO LUONG", "DON GIA", "THANH TIEN"});
    }

    private void column2() {
        modelListHoaDon.setColumnIdentifiers(new String[]{"STT", "MA HOA DOn ", "NGAY TAO", "MANV", "TRẠNG THAI"});
    }

    private void updateProductQuantityGoHang(int i, int quantity) {
        System.out.println("i: " + i);
        System.out.println("listGiayChiTiet size: " + listGiayChiTiet.size());
        Object q = modelListGioHang.getValueAt(i, 3);
        if (i >= 0 && i < listGiayChiTiet.size()) {
            int quantityInRow = Integer.parseInt(q.toString());
            int updatedQuantity = quantityInRow + quantity;
            gct.UpdateSo(listHoaDonChiTiet.get(i).getGiayChiTiet().getiD(), updatedQuantity);
        }

    }

    private void updateProductQuantity(int i, int quantity) {
        int quantityInRow = Integer.parseInt(tblDanhSachSp.getValueAt(i, 5).toString());
        int updatedQuantity = quantityInRow - quantity;
        gct.UpdateSo(listGiayChiTiet.get(i).getiD(), updatedQuantity);

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        txtMaHoaDon = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        lblMaNhanVien = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        lblTongTien = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jTextField6 = new javax.swing.JTextField();
        jPanel8 = new javax.swing.JPanel();
        jTextField5 = new javax.swing.JTextField();
        jPanel9 = new javax.swing.JPanel();
        choHinhThucTT = new javax.swing.JComboBox<>();
        btnThanhToan = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        btnHoaDon = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        jPanel12 = new javax.swing.JPanel();
        jTextField3 = new javax.swing.JTextField();
        jPanel13 = new javax.swing.JPanel();
        jButton3 = new javax.swing.JButton();
        jLabel15 = new javax.swing.JLabel();
        jRadioButton1 = new javax.swing.JRadioButton();
        jRadioButton2 = new javax.swing.JRadioButton();
        jRadioButton3 = new javax.swing.JRadioButton();
        jLabel4 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jPanel10 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblDanhSachSp = new javax.swing.JTable();
        jPanel11 = new javax.swing.JPanel();
        btnDelete = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblGioHangCho = new javax.swing.JTable();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblListHoaDon = new javax.swing.JTable();
        jLabel16 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        jButton4 = new javax.swing.JButton();
        jPanel14 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jPanel15 = new javax.swing.JPanel();
        lblTongTien1 = new javax.swing.JLabel();
        jPanel16 = new javax.swing.JPanel();
        txtMaHoaDon2 = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jPanel17 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 153, 51), null));
        jPanel1.setName("Tạo Hoá Đơn"); // NOI18N

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(51, 102, 0));
        jLabel1.setText("Hoá Đơn");

        jLabel2.setText("Mã Hoá Đơn");

        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 102, 0), null));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtMaHoaDon, javax.swing.GroupLayout.DEFAULT_SIZE, 180, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtMaHoaDon, javax.swing.GroupLayout.DEFAULT_SIZE, 46, Short.MAX_VALUE)
                .addContainerGap())
        );

        jLabel5.setText("Nhân Viên");

        jPanel4.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 102, 0), null));
        jPanel4.setForeground(new java.awt.Color(0, 102, 0));

        lblMaNhanVien.setText("Chưa Biết Làm");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblMaNhanVien, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblMaNhanVien)
                .addContainerGap(10, Short.MAX_VALUE))
        );

        jLabel6.setText("Tổng tiền");

        jPanel6.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 102, 0), null));
        jPanel6.setForeground(new java.awt.Color(0, 102, 0));

        lblTongTien.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblTongTien.setForeground(new java.awt.Color(0, 102, 0));
        lblTongTien.setText("0");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblTongTien, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblTongTien, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(14, 14, 14))
        );

        jPanel7.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 102, 0), null));
        jPanel7.setForeground(new java.awt.Color(0, 102, 0));

        jTextField6.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jTextField6.setForeground(new java.awt.Color(0, 102, 0));
        jTextField6.setText("0");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTextField6)
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTextField6, javax.swing.GroupLayout.DEFAULT_SIZE, 46, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel8.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 102, 0), null));
        jPanel8.setForeground(new java.awt.Color(0, 102, 0));

        jTextField5.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jTextField5.setForeground(new java.awt.Color(0, 102, 0));
        jTextField5.setText("0");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTextField5)
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTextField5, javax.swing.GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel9.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 102, 0), null));
        jPanel9.setForeground(new java.awt.Color(0, 102, 0));

        choHinhThucTT.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Tiền Mặt", "Thẻ ", " " }));

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(choHinhThucTT, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(choHinhThucTT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(20, Short.MAX_VALUE))
        );

        btnThanhToan.setText("Thanh Toán");
        btnThanhToan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThanhToanActionPerformed(evt);
            }
        });

        jLabel9.setText("Tiền Khách Đưa");

        jLabel10.setText("Tiền Thừa");

        jLabel12.setText("Hình Thức TT");

        jButton2.setText("Huỷ");

        btnHoaDon.setText("Tạo Hoá Đơn");
        btnHoaDon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHoaDonActionPerformed(evt);
            }
        });

        jPanel12.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 102, 0), null));

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTextField3)
                .addContainerGap())
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel12Layout.createSequentialGroup()
                .addContainerGap(8, Short.MAX_VALUE)
                .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel13.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 102, 0), null));

        jButton3.setText("Tìm");

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 218, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel13Layout.createSequentialGroup()
                .addContainerGap(8, Short.MAX_VALUE)
                .addComponent(jButton3)
                .addContainerGap())
        );

        jLabel15.setText("Tên KH");

        jRadioButton1.setText("Chờ Thanh Toán");

        jRadioButton2.setText("Huỷ");

        jRadioButton3.setText("Đã Thanh Toán");

        jLabel4.setText("Trạng Thái");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addGap(84, 84, 84))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(btnHoaDon, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 38, Short.MAX_VALUE)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btnThanhToan, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jSeparator1)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(jLabel15)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel9, javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jLabel10))
                            .addComponent(jLabel5)
                            .addComponent(jLabel2))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel12)
                        .addGap(25, 25, 25)
                        .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jRadioButton1)
                    .addComponent(jLabel4))
                .addGap(43, 43, 43)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jRadioButton3)
                    .addComponent(jRadioButton2))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(31, 31, 31)
                        .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(41, 41, 41)
                        .addComponent(jLabel15)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 25, Short.MAX_VALUE)
                .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(4, 4, 4)
                .addComponent(jLabel1)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(16, 16, 16)
                        .addComponent(jLabel2))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(7, 7, 7)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel5)
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9))
                .addGap(14, 14, 14)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel10)
                    .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(23, 23, 23)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jRadioButton2)
                    .addComponent(jLabel4))
                .addGap(15, 15, 15)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jRadioButton3)
                    .addComponent(jRadioButton1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnThanhToan, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnHoaDon, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(21, 21, 21))
        );

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(0, 102, 0));
        jLabel7.setText("Danh Sách Sản Phẩm");

        jPanel10.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 102, 0), null));

        tblDanhSachSp.setModel(new javax.swing.table.DefaultTableModel(
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
        tblDanhSachSp.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblDanhSachSpMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                tblDanhSachSpMouseEntered(evt);
            }
        });
        jScrollPane1.setViewportView(tblDanhSachSp);

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 857, Short.MAX_VALUE)
                .addGap(14, 14, 14))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGap(42, 42, 42)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 237, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(26, Short.MAX_VALUE))
        );

        jPanel11.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 102, 0), null));

        btnDelete.setText("XOÁ");
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });

        tblGioHangCho.setModel(new javax.swing.table.DefaultTableModel(
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
        tblGioHangCho.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblGioHangChoMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                tblGioHangChoMouseEntered(evt);
            }
        });
        jScrollPane3.setViewportView(tblGioHangCho);

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(btnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(746, Short.MAX_VALUE))
            .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel11Layout.createSequentialGroup()
                    .addGap(156, 156, 156)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 709, Short.MAX_VALUE)
                    .addContainerGap()))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(btnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(140, Short.MAX_VALUE))
            .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel11Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 180, Short.MAX_VALUE)
                    .addGap(9, 9, 9)))
        );

        jPanel5.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 102, 0), null));

        tblListHoaDon.setModel(new javax.swing.table.DefaultTableModel(
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
        tblListHoaDon.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblListHoaDonMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                tblListHoaDonMouseEntered(evt);
            }
        });
        jScrollPane2.setViewportView(tblListHoaDon);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 875, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 187, Short.MAX_VALUE)
        );

        jLabel16.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(0, 102, 0));
        jLabel16.setText("Danh Sách Hoá Đơn");

        jLabel11.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(0, 102, 0));
        jLabel11.setText("Giỏ Hàng");

        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 102, 0), null));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(0, 102, 0));
        jLabel3.setText("Đặt Hàng");

        jButton4.setText("Đặt Hàng");

        jPanel14.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 102, 0), null));

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 47, Short.MAX_VALUE)
        );

        jLabel8.setText("Mã Hoá Đơn");

        jPanel15.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 102, 0), null));
        jPanel15.setForeground(new java.awt.Color(0, 102, 0));

        lblTongTien1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblTongTien1.setForeground(new java.awt.Color(0, 102, 0));
        lblTongTien1.setText("0");

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblTongTien1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblTongTien1, javax.swing.GroupLayout.DEFAULT_SIZE, 44, Short.MAX_VALUE)
        );

        jPanel16.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 102, 0), null));

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtMaHoaDon2)
                .addContainerGap())
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtMaHoaDon2, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                .addContainerGap())
        );

        jLabel13.setText("jLabel13");

        jPanel17.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 102, 0), null));

        javax.swing.GroupLayout jPanel17Layout = new javax.swing.GroupLayout(jPanel17);
        jPanel17.setLayout(jPanel17Layout);
        jPanel17Layout.setHorizontalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel17Layout.setVerticalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 42, Short.MAX_VALUE)
        );

        jLabel14.setText("jLabel14");

        jLabel17.setText("jLabel17");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(16, 16, 16)
                                .addComponent(jLabel8))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel13)))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jSeparator2, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jPanel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel17, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel16, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(96, 96, 96)
                        .addComponent(jLabel3))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel14))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel17)))
                .addGap(0, 101, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(124, 124, 124)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel8)
                .addGap(18, 18, 18)
                .addComponent(jPanel16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel13)
                .addGap(26, 26, 26)
                .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel14)
                .addGap(2, 2, 2)
                .addComponent(jPanel17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel17)
                .addGap(17, 17, 17)
                .addComponent(jPanel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(91, 91, 91))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, 875, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addComponent(jLabel11))
                            .addComponent(jLabel7)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addComponent(jLabel16))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(1, 1, 1)
                .addComponent(jLabel16)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel11)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel7)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addContainerGap())
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tblDanhSachSpMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDanhSachSpMouseEntered

    }//GEN-LAST:event_tblDanhSachSpMouseEntered

    private void tblDanhSachSpMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDanhSachSpMouseClicked
        int check = JOptionPane.showConfirmDialog(this, "Bỏ Vào Giỏ ! Mua");
        int i = tblDanhSachSp.getSelectedRow();
        String maHoaDon = txtMaHoaDon.getText().trim();
        listGiayChiTiet = gct.getAllGiay();

        if (check == JOptionPane.YES_OPTION) {
            //if (tongTien >0 ) {

            Integer s = Integer.valueOf(tblDanhSachSp.getValueAt(i, 5).toString());
            if (s >= 0) {

                if (i >= 0) {
                    String soLuong = JOptionPane.showInputDialog(null, "Nhập Số Lượng");
                    if (soLuong != null && !soLuong.isEmpty()) {
                        int selectedRow = tblListHoaDon.getSelectedRow();

                        if (selectedRow >= 0) {
                            listHoaDon = hdrepo.getAllHoaDon();
                            HoaDon h = listHoaDon.get(selectedRow);

                            String idHoaDonz = h.getId().toString();
                            GiayChiTiet g = listGiayChiTiet.get(i);
                            String a = tblDanhSachSp.getValueAt(i, 6).toString();
                            int soluongGioHang = Integer.parseInt(soLuong);
                            hdctrepo.creatGiHang(g.getiD(), idHoaDonz, new BigDecimal(a), soluongGioHang);

                            updateProductQuantity(i, soluongGioHang);

                            showDataSanPham();

                            JOptionPane.showMessageDialog(this, "Thêm Thành Công Vào Giỏ");

                            showDataGoHang(idHoaDonz);
                            BigDecimal tongtien = tinhVaThemTongTien(5);

                        } else {

                            GiayChiTiet g = listGiayChiTiet.get(i);
                            String h = tblDanhSachSp.getValueAt(i, 6).toString();
                            int soluongGioHang = Integer.parseInt(soLuong);
                            String idHoaDon = hdrepo.selectiIdHoaDon();
                            hdctrepo.creatGiHang(g.getiD(), idHoaDon, new BigDecimal(h), soluongGioHang);
                            showDataGoHang(idHoaDon);
                            updateProductQuantity(i, soluongGioHang);

                            showDataSanPham();

                            JOptionPane.showMessageDialog(this, "Thêm Thành Công Vào Giỏ");
                            BigDecimal tongtien = tinhVaThemTongTien(5);

                        }

                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Chọn một sản phẩm trước khi thêm vào giỏ.");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Hết Hàng");
                return;
            }
            //else {
//                JOptionPane.showMessageDialog(this, "Chưa Tạo hoá Đơn");
//                return;
//            }
        }


    }//GEN-LAST:event_tblDanhSachSpMouseClicked

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        listHoaDonChiTiet = hdctrepo.getAllHoaDon();
        int check = JOptionPane.showConfirmDialog(this, "Xoá Khỏi Giỏ ? ");
        int i = tblGioHangCho.getSelectedRow();
        int index = tblListHoaDon.getSelectedRow();
        try {
            if (i >= 0) {
                if (check == JOptionPane.YES_OPTION) {
                    HoaDon hoaDon = listHoaDon.get(index);
                    HoaDonChiTiet h = listHoaDonChiTiet.get(i);

                    if (hdctrepo.delete(h.getId()) != null) {
                        Integer soLuongGioHang = (Integer) tblGioHangCho.getValueAt(i, 3);
                        String soLuongSanPham = hdctrepo.selectSoLuongSanPham(h.getGiayChiTiet().getiD());
                        Integer soLuongCapNhat = soLuongGioHang + Integer.parseInt(soLuongSanPham);
                       
                        gct.UpdateSo(h.getGiayChiTiet().getiD(), soLuongCapNhat);
                        showDataSanPham();
                        

                        showDataGoHang(hoaDon.getId());
                        BigDecimal tongTien = tinhVaThemTongTien(5);
                        JOptionPane.showMessageDialog(this, "Xoá Thành Công");

                    }

                }
            } else {
                JOptionPane.showMessageDialog(this, "Chưa Chọn Mà Xoá");
                return;

            }
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void btnHoaDonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHoaDonActionPerformed
        int check = JOptionPane.showConfirmDialog(this, "Tạo Hoá Đơn");
        if (check == JOptionPane.YES_OPTION) {
            try {
                lblTongTien.setText("0");
                lblTongTien1.setText("0");
                HoaDon h = getForm();
                HoaDon createHoaDon = hdrepo.creatHoaDon(h);
                String idHoaDon = hdrepo.selectiIdHoaDon();
                if (createHoaDon != null) {
                    selectmaHoaDon();
                    showDaTAHoaDon();
                    showDataGoHang(idHoaDon);
                    showDataSanPham();
                    JOptionPane.showMessageDialog(this, "Tạo Hoá Đơn Thành Công");

                }
            } catch (Exception e) {
                e.printStackTrace();
                // Show an error message if an exception occurs
                JOptionPane.showMessageDialog(this, "Đã xảy ra lỗi trong quá trình tạo Hóa đơn.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }


    }//GEN-LAST:event_btnHoaDonActionPerformed

    private void btnThanhToanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThanhToanActionPerformed

    }//GEN-LAST:event_btnThanhToanActionPerformed

    private void tblListHoaDonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblListHoaDonMouseClicked
        int selectedRow = tblListHoaDon.getSelectedRow();

        if (selectedRow != -1) {
            txtMaHoaDon.setText((String) tblListHoaDon.getValueAt(selectedRow, 1));

            listHoaDon = hdrepo.getAllHoaDon();
            HoaDon h = listHoaDon.get(selectedRow);

            String idHoaDon = h.getId().toString();

            showDataGoHang(idHoaDon);
            BigDecimal tongtien = tinhVaThemTongTien(5);

        }
    }//GEN-LAST:event_tblListHoaDonMouseClicked

    private void tblListHoaDonMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblListHoaDonMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_tblListHoaDonMouseEntered

    private void tblGioHangChoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblGioHangChoMouseClicked
        if (evt.getClickCount() == 2) {
            int i = tblGioHangCho.getSelectedRow();
            int index = tblListHoaDon.getSelectedRow();
            int messegertype = JOptionPane.QUESTION_MESSAGE;
            String[] obtion = {"Số Lượng", "Xoá", "Huỷ"};
            int code = JOptionPane.showOptionDialog(this, "BẠN MUỐN LÀM GÌ ?", "LỰA CHỌN", 0, messegertype, null, obtion, "Số Lượng");
            switch (code) {
                case 0:
                    JOptionPane.showMessageDialog(this, "Chọn Sửa số lượng");

                    break;
                case 1:
                    JOptionPane.showMessageDialog(this, "Chọn Xoá");
                    HoaDonChiTiet h = listHoaDonChiTiet.get(i);
                    HoaDon hoaDon = listHoaDon.get(index);
                    if (hdctrepo.delete(h.getId()) != null) {
                        Integer soLuongGioHang = (Integer) tblGioHangCho.getValueAt(i, 3);
                        String soLuongSanPham = hdctrepo.selectSoLuongSanPham(h.getGiayChiTiet().getiD());
                        Integer soLuongCapNhat = soLuongGioHang + Integer.parseInt(soLuongSanPham);

                        gct.UpdateSo(h.getGiayChiTiet().getiD(), soLuongCapNhat);
                        showDataSanPham();
                        JOptionPane.showMessageDialog(this, "Xoá Thành Công");

                        showDataGoHang(hoaDon.getId());
                        BigDecimal tongtien = tinhVaThemTongTien(5);

                        break;

                    }
            }
        }


    }//GEN-LAST:event_tblGioHangChoMouseClicked

    private void tblGioHangChoMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblGioHangChoMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_tblGioHangChoMouseEntered

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
                if ("Window".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(HoaDonForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(HoaDonForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(HoaDonForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(HoaDonForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new HoaDonForm().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnHoaDon;
    private javax.swing.JButton btnThanhToan;
    private javax.swing.JComboBox<String> choHinhThucTT;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JRadioButton jRadioButton2;
    private javax.swing.JRadioButton jRadioButton3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JLabel lblMaNhanVien;
    private javax.swing.JLabel lblTongTien;
    private javax.swing.JLabel lblTongTien1;
    private javax.swing.JTable tblDanhSachSp;
    private javax.swing.JTable tblGioHangCho;
    private javax.swing.JTable tblListHoaDon;
    private javax.swing.JTextField txtMaHoaDon;
    private javax.swing.JTextField txtMaHoaDon2;
    // End of variables declaration//GEN-END:variables
}
