/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package View;

import Entity.GiayChiTiet;
import Entity.HoaDon;
import Entity.HoaDonChiTiet;
import Entity.KhachHang;
import Repository.GiayChiTietRepo;
import Repository.HoaDnRepo;
import Repository.HoaDonChiTietRepo;
import Repository.KhachHangRepo;
import java.awt.CardLayout;
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

    CardLayout card;

    HoaDonChiTietRepo hdctrepo = new HoaDonChiTietRepo();
    GiayChiTietRepo gct = new GiayChiTietRepo();
    HoaDnRepo hdrepo = new HoaDnRepo();
    KhachHangRepo khrp = new KhachHangRepo();

    List<GiayChiTiet> listGiayChiTiet = new ArrayList<>();
    List<HoaDonChiTiet> listHoaDonChiTiet = new ArrayList<>();
    List<HoaDon> listHoaDon = new ArrayList<>();
    List<KhachHang> listKhachHang = new ArrayList<>();

    DefaultTableModel model;
    DefaultTableModel modelListGioHang;
    DefaultTableModel modelListHoaDon;

    /**
     * Creates new form HoaDonForm
     */
    public HoaDonForm() {

        initComponents();
        card = (CardLayout) pnCardGoc.getLayout();
        card.show(pnCardGoc, "CardMuaHang");
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
        comBoMaGiay();

    }

    void comBoMaGiay() {
        listGiayChiTiet = gct.getAllGiay();
        for (GiayChiTiet g : listGiayChiTiet) {
            cboMa.addItem(g.getGiay().getMa());
        }
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
            v.add(g.getSoLuong() > 0 ? "Còn Hàng" : "Hết Hàng");
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
            Logger.getLogger(HoaDonForm.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void showDaTAHoaDon() {
        listHoaDon = hdrepo.getAllHoaDon();
        modelListHoaDon.setRowCount(0);

        List<String> trangThai = hdrepo.selectAllTrangThaiHoaDon();

        for (HoaDon h : listHoaDon) {
            if (trangThai.contains(h.getTrangThai()) && h.getTrangThai().equalsIgnoreCase("Chờ Thanh Toán")) {
                Vector<Object> rowData = new Vector<>();
                rowData.add(modelListHoaDon.getRowCount() + 1);
                rowData.add(h.getMaHoaDon());
                rowData.add(h.getNgayTao());
                rowData.add(h.getNhanVien().getMa());
                rowData.add(h.getTrangThai());
                modelListHoaDon.addRow(rowData);
            }
        }

    }

    private void selectmaHoaDon() {
        String maHoaDon = hdrepo.selectMaHoaDon();
        lblMaHoaDon.setText(maHoaDon);
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
        model.setColumnIdentifiers(new String[]{"Ma SP", "Ten SP ", "Hang", "MauSac", "Kich Co", "SoLuong", "DonGia", "Trạng Thái"});
    }

    private void column1() {
        modelListGioHang.setColumnIdentifiers(new String[]{"STT", "MA SP", "TEN SP", "SO LUONG", "DON GIA", "TONG GIA"});
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

        jLabel7 = new javax.swing.JLabel();
        jPanel10 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblDanhSachSp = new javax.swing.JTable();
        jTextField1 = new javax.swing.JTextField();
        lblError = new javax.swing.JLabel();
        cboMa = new javax.swing.JComboBox<>();
        jPanel11 = new javax.swing.JPanel();
        btnDelete = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblGioHangCho = new javax.swing.JTable();
        btnDeleteAll = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblListHoaDon = new javax.swing.JTable();
        jLabel16 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        pnCardGoc = new javax.swing.JPanel();
        cardDatHang = new javax.swing.JPanel();
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
        btnTroVeBanHang = new javax.swing.JButton();
        CardMuaHang = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        btnThanhToan = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        btnHuy = new javax.swing.JButton();
        btnHoaDon = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        choHinhThucTT = new javax.swing.JComboBox<>();
        lblMaHoaDon = new javax.swing.JLabel();
        lblNhanVien = new javax.swing.JLabel();
        lblTongTien = new javax.swing.JLabel();
        lblTienThua = new javax.swing.JLabel();
        txtTienKhachDua = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        lblKiemTraDiem = new javax.swing.JLabel();
        btnTra = new javax.swing.JButton();
        btnSuDungDien = new javax.swing.JButton();
        lblErrKiemTraDiem = new javax.swing.JLabel();
        btnVaoDatHang = new javax.swing.JButton();
        lblErrTienKhachDua = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        txtMaKhach = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        btnFinKhach = new javax.swing.JButton();
        lblKhachHang = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        lblErrKhach = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel7.setText("Danh Sách Sản Phẩm");

        jPanel10.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 0, 0), null));

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

        jTextField1.setText("Tìm Kiếm ");

        lblError.setFont(new java.awt.Font("Serif", 1, 14)); // NOI18N
        lblError.setForeground(new java.awt.Color(204, 0, 0));

        cboMa.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Tất Cả Mã Giày" }));

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addGap(41, 41, 41)
                        .addComponent(cboMa, javax.swing.GroupLayout.PREFERRED_SIZE, 304, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(41, 41, 41)
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 326, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 732, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(lblError, javax.swing.GroupLayout.PREFERRED_SIZE, 389, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(50, Short.MAX_VALUE))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cboMa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(8, 8, 8)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblError, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(10, Short.MAX_VALUE))
        );

        jPanel11.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 0, 0), null));

        btnDelete.setBackground(new java.awt.Color(0, 0, 0));
        btnDelete.setForeground(new java.awt.Color(255, 255, 255));
        btnDelete.setText("Bỏ Sản Phẩm");
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });

        tblGioHangCho.setBorder(javax.swing.BorderFactory.createEtchedBorder());
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

        btnDeleteAll.setBackground(new java.awt.Color(0, 0, 0));
        btnDeleteAll.setForeground(new java.awt.Color(255, 255, 255));
        btnDeleteAll.setText("Bỏ Hết Sản Phẩm");
        btnDeleteAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteAllActionPerformed(evt);
            }
        });

        jButton5.setBackground(new java.awt.Color(0, 0, 0));
        jButton5.setForeground(new java.awt.Color(255, 255, 255));
        jButton5.setText("Quét Mã QR");

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addComponent(btnDeleteAll)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnDelete)
                        .addGap(18, 18, 18)
                        .addComponent(jButton5)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 193, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnDelete)
                    .addComponent(btnDeleteAll)
                    .addComponent(jButton5))
                .addContainerGap(17, Short.MAX_VALUE))
        );

        jPanel5.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 0, 0), null));

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
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 759, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 187, Short.MAX_VALUE)
        );

        jLabel16.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel16.setText("Danh Sách Hoá Đơn Chờ");

        jLabel11.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel11.setText("Giỏ Hàng");

        pnCardGoc.setLayout(new java.awt.CardLayout());

        cardDatHang.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 0, 0), null));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel3.setText("Đặt Hàng");

        jButton4.setBackground(new java.awt.Color(0, 0, 0));
        jButton4.setForeground(new java.awt.Color(255, 255, 255));
        jButton4.setText("Đặt Hàng");

        jPanel14.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 0, 0), null));

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 22, Short.MAX_VALUE)
        );

        jLabel8.setText("Mã Hoá Đơn");

        jPanel15.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 0, 0), null));
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
            .addComponent(lblTongTien1)
        );

        jPanel16.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 0, 0), null));

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

        jPanel17.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 0, 0), null));

        javax.swing.GroupLayout jPanel17Layout = new javax.swing.GroupLayout(jPanel17);
        jPanel17.setLayout(jPanel17Layout);
        jPanel17Layout.setHorizontalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel17Layout.setVerticalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 17, Short.MAX_VALUE)
        );

        jLabel14.setText("jLabel14");

        jLabel17.setText("jLabel17");

        btnTroVeBanHang.setText("Bán Hàng");
        btnTroVeBanHang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTroVeBanHangActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout cardDatHangLayout = new javax.swing.GroupLayout(cardDatHang);
        cardDatHang.setLayout(cardDatHangLayout);
        cardDatHangLayout.setHorizontalGroup(
            cardDatHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cardDatHangLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(cardDatHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel17, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel16, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(cardDatHangLayout.createSequentialGroup()
                        .addGroup(cardDatHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(cardDatHangLayout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 282, Short.MAX_VALUE)
                                .addComponent(btnTroVeBanHang))
                            .addComponent(jButton4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jSeparator2, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jPanel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(cardDatHangLayout.createSequentialGroup()
                                .addGroup(cardDatHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel14)
                                    .addComponent(jLabel17)
                                    .addComponent(jLabel8)
                                    .addComponent(jLabel13))
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addContainerGap())))
        );
        cardDatHangLayout.setVerticalGroup(
            cardDatHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cardDatHangLayout.createSequentialGroup()
                .addGroup(cardDatHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(cardDatHangLayout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnTroVeBanHang)
                        .addGap(8, 8, 8))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, cardDatHangLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel3)
                        .addGap(18, 18, 18)))
                .addComponent(jLabel8)
                .addGap(18, 18, 18)
                .addComponent(jPanel16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel13)
                .addGap(26, 26, 26)
                .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(31, 31, 31)
                .addComponent(jLabel14)
                .addGap(2, 2, 2)
                .addComponent(jPanel17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(31, 31, 31)
                .addComponent(jLabel17)
                .addGap(17, 17, 17)
                .addComponent(jPanel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(91, 91, 91))
        );

        pnCardGoc.add(cardDatHang, "cardDatHang");
        cardDatHang.getAccessibleContext().setAccessibleName("");
        cardDatHang.getAccessibleContext().setAccessibleDescription("");

        CardMuaHang.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 0, 0), null));
        CardMuaHang.setName("Tạo Hoá Đơn"); // NOI18N

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setText("Đơn Hàng");

        jLabel2.setText("Mã Hoá Đơn");

        jLabel5.setText("Nhân Viên");

        jLabel6.setText("Tổng tiền Phải Trả");

        btnThanhToan.setBackground(new java.awt.Color(0, 0, 0));
        btnThanhToan.setForeground(new java.awt.Color(255, 255, 255));
        btnThanhToan.setText("Thanh Toán");
        btnThanhToan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThanhToanActionPerformed(evt);
            }
        });

        jLabel9.setText("Tiền Khách Đưa");

        jLabel10.setText("Tiền Thừa");

        jLabel12.setText("Hình Thức TT");

        btnHuy.setBackground(new java.awt.Color(0, 0, 0));
        btnHuy.setForeground(new java.awt.Color(255, 255, 255));
        btnHuy.setText("Huỷ Hoá Đơn");
        btnHuy.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnHuyMouseClicked(evt);
            }
        });
        btnHuy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHuyActionPerformed(evt);
            }
        });

        btnHoaDon.setBackground(new java.awt.Color(0, 0, 0));
        btnHoaDon.setForeground(new java.awt.Color(255, 255, 255));
        btnHoaDon.setText("Tạo Hoá Đơn");
        btnHoaDon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHoaDonActionPerformed(evt);
            }
        });

        choHinhThucTT.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Tiền Mặt", "Thẻ " }));
        choHinhThucTT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                choHinhThucTTActionPerformed(evt);
            }
        });

        lblTongTien.setText("0");

        lblTienThua.setText("0");

        txtTienKhachDua.setText("0");
        txtTienKhachDua.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtTienKhachDuaKeyReleased(evt);
            }
        });

        jLabel18.setText("Kiểm Tra Điểm");

        lblKiemTraDiem.setText("0");

        btnTra.setBackground(new java.awt.Color(0, 0, 0));
        btnTra.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnTra.setForeground(new java.awt.Color(255, 255, 255));
        btnTra.setText("Tra");
        btnTra.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTraActionPerformed(evt);
            }
        });

        btnSuDungDien.setBackground(new java.awt.Color(0, 0, 0));
        btnSuDungDien.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnSuDungDien.setForeground(new java.awt.Color(255, 255, 255));
        btnSuDungDien.setText("Dùng");
        btnSuDungDien.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnSuDungDienMouseClicked(evt);
            }
        });
        btnSuDungDien.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSuDungDienActionPerformed(evt);
            }
        });

        lblErrKiemTraDiem.setForeground(new java.awt.Color(204, 0, 0));

        btnVaoDatHang.setBackground(new java.awt.Color(0, 0, 0));
        btnVaoDatHang.setForeground(new java.awt.Color(255, 255, 255));
        btnVaoDatHang.setText("Đặt Hàng");
        btnVaoDatHang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVaoDatHangActionPerformed(evt);
            }
        });

        lblErrTienKhachDua.setForeground(new java.awt.Color(204, 0, 0));

        javax.swing.GroupLayout CardMuaHangLayout = new javax.swing.GroupLayout(CardMuaHang);
        CardMuaHang.setLayout(CardMuaHangLayout);
        CardMuaHangLayout.setHorizontalGroup(
            CardMuaHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(CardMuaHangLayout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(btnThanhToan, javax.swing.GroupLayout.PREFERRED_SIZE, 398, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(CardMuaHangLayout.createSequentialGroup()
                .addGroup(CardMuaHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(CardMuaHangLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(CardMuaHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(CardMuaHangLayout.createSequentialGroup()
                                .addComponent(jLabel12)
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addGroup(CardMuaHangLayout.createSequentialGroup()
                        .addGroup(CardMuaHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(CardMuaHangLayout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addComponent(jLabel5))
                            .addComponent(jLabel2)
                            .addComponent(jLabel10)
                            .addComponent(jLabel9)
                            .addComponent(jLabel6)
                            .addComponent(jLabel18))
                        .addGroup(CardMuaHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, CardMuaHangLayout.createSequentialGroup()
                                .addGap(96, 96, 96)
                                .addGroup(CardMuaHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(lblMaHoaDon, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblNhanVien, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(168, 168, 168))
                            .addGroup(CardMuaHangLayout.createSequentialGroup()
                                .addGap(70, 70, 70)
                                .addGroup(CardMuaHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(choHinhThucTT, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblTongTien, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblTienThua, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(CardMuaHangLayout.createSequentialGroup()
                                        .addComponent(txtTienKhachDua, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(43, 43, 43)
                                        .addComponent(lblErrTienKhachDua, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, CardMuaHangLayout.createSequentialGroup()
                        .addGroup(CardMuaHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(CardMuaHangLayout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addComponent(jLabel1)
                                .addGap(127, 127, 127))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, CardMuaHangLayout.createSequentialGroup()
                                .addGap(113, 113, 113)
                                .addComponent(btnHoaDon)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnHuy)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnVaoDatHang, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, CardMuaHangLayout.createSequentialGroup()
                        .addGroup(CardMuaHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(CardMuaHangLayout.createSequentialGroup()
                                .addComponent(lblKiemTraDiem, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnTra))
                            .addComponent(lblErrKiemTraDiem, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnSuDungDien)
                        .addGap(30, 30, 30)))
                .addContainerGap())
        );

        CardMuaHangLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {btnHoaDon, btnHuy});

        CardMuaHangLayout.setVerticalGroup(
            CardMuaHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(CardMuaHangLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(CardMuaHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(CardMuaHangLayout.createSequentialGroup()
                        .addGap(54, 54, 54)
                        .addComponent(lblNhanVien, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(lblMaHoaDon, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(CardMuaHangLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(CardMuaHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(btnHoaDon, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnHuy, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnVaoDatHang))
                        .addGap(23, 23, 23)
                        .addComponent(jLabel5)
                        .addGap(38, 38, 38)
                        .addComponent(jLabel2)))
                .addGap(18, 18, 18)
                .addGroup(CardMuaHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(CardMuaHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnTra)
                        .addComponent(btnSuDungDien, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblKiemTraDiem, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(CardMuaHangLayout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(jLabel18)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblErrKiemTraDiem, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(CardMuaHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(lblTongTien, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(CardMuaHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtTienKhachDua, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9)
                    .addComponent(lblErrTienKhachDua, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(CardMuaHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblTienThua, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10))
                .addGap(18, 18, 18)
                .addGroup(CardMuaHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(choHinhThucTT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12))
                .addGap(41, 41, 41)
                .addComponent(btnThanhToan, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(61, 61, 61))
        );

        CardMuaHangLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {btnSuDungDien, btnTra});

        CardMuaHangLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {btnHoaDon, btnHuy, btnVaoDatHang});

        pnCardGoc.add(CardMuaHang, "CardMuaHang");

        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 0, 0), null));

        jLabel4.setText("Tên Khách");

        jLabel15.setText("Mã Khách :");

        btnFinKhach.setBackground(new java.awt.Color(0, 0, 0));
        btnFinKhach.setForeground(new java.awt.Color(255, 255, 255));
        btnFinKhach.setText("Tìm");
        btnFinKhach.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFinKhachActionPerformed(evt);
            }
        });

        lblKhachHang.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jButton3.setBackground(new java.awt.Color(0, 0, 0));
        jButton3.setForeground(new java.awt.Color(255, 255, 255));
        jButton3.setText("Đăng Ký Thành Viên");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        lblErrKhach.setForeground(new java.awt.Color(204, 0, 0));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel15)
                        .addGap(18, 18, 18)
                        .addComponent(txtMaKhach, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addGap(22, 22, 22)
                        .addComponent(lblKhachHang, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(lblErrKhach, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnFinKhach, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton3))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtMaKhach, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel15)
                            .addComponent(btnFinKhach))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblKhachHang, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jButton3)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblErrKhach, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel16)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel11))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addComponent(jLabel7)))))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(pnCardGoc, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(pnCardGoc, javax.swing.GroupLayout.PREFERRED_SIZE, 510, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel16)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tblDanhSachSpMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDanhSachSpMouseEntered

    }//GEN-LAST:event_tblDanhSachSpMouseEntered

    private void tblDanhSachSpMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDanhSachSpMouseClicked

        int check = JOptionPane.showConfirmDialog(this, "Bỏ Vào Giỏ ! Mua");

        int indexDanhSachSp = tblDanhSachSp.getSelectedRow();

        listGiayChiTiet = gct.getAllGiay();
        listHoaDon = hdrepo.getAllHoaDon();

        if (check == JOptionPane.YES_OPTION) {
            if (!lblMaHoaDon.getText().isEmpty()) {
                if (indexDanhSachSp >= 0) {
                    Integer soLuongGoc = Integer.parseInt(tblDanhSachSp.getValueAt(indexDanhSachSp, 5).toString());
                    if (soLuongGoc >= 0) {

                        String soLuong = JOptionPane.showInputDialog(null, "Nhập Số Lượng");
                        try {
                            if (Integer.parseInt(soLuong) <= soLuongGoc) {

                                if (soLuong != null && !soLuong.isEmpty()) {
                                    int selectedRow = tblListHoaDon.getSelectedRow();
                                    HoaDon indexHoaDon = listHoaDon.get(selectedRow);//Lý Do
                                    String idHoaDonz = indexHoaDon.getId().toString();
                                    GiayChiTiet indexGiay = listGiayChiTiet.get(indexDanhSachSp);
                                    String donGia = tblDanhSachSp.getValueAt(indexDanhSachSp, 6).toString();
                                    int soluongGioHang = Integer.parseInt(soLuong);
                                    Integer soLuongGocGioHang = hdctrepo.selectSoLuongGioHangGoc(idHoaDonz, indexGiay.getiD());
                                    Integer soLuongGiHangThayDoi = soluongGioHang + soLuongGocGioHang;
                                    Integer idGiayCtTonTai = hdrepo.selectIdSanPhamTrongGioHang(indexGiay.getiD(), idHoaDonz);
                                    if (selectedRow >= 0) {
                                        if (idGiayCtTonTai == 0) {
                                            if (hdctrepo.creatGiHang(indexGiay.getiD(), idHoaDonz, new BigDecimal(donGia), soluongGioHang) != null) {
                                                updateProductQuantity(indexDanhSachSp, soluongGioHang);
                                                showDataSanPham();
                                                showDataGoHang(idHoaDonz);
                                                JOptionPane.showMessageDialog(this, "Bỏ Thành Công Vào Giỏ");
                                            }
                                        } else {
                                            if (hdctrepo.updateSoLuong(soLuongGiHangThayDoi, indexGiay.getiD()) != null) {
                                                updateProductQuantity(indexDanhSachSp, soluongGioHang);// trừ số lượng ở sản phẩm                                   
                                                showDataGoHang(idHoaDonz);
                                                showDataSanPham();
                                                JOptionPane.showMessageDialog(this, "Thay Đổi Số Lượng ");
                                            }

                                        }
                                        showDataGoHang(idHoaDonz);
                                        BigDecimal tongtien = tinhVaThemTongTien(5);

                                    }

                                }

                            } else {
                                lblError.setText("Xin Lỗi ! Chúng Tôi Không Có Đủ Số Lượng ");

                                return;
                            }
                        } catch (NumberFormatException e) {
                            JOptionPane.showMessageDialog(this, "Số Lượng không Đúng Định Dạng Số");
                        }

                    } else {

                        lblError.setText("HẾT HÀNG");
                        return;
                    }

                } else {
                    JOptionPane.showMessageDialog(this, "Chọn một sản phẩm trước khi thêm vào giỏ nha!!!^^");
                }
            } else {
                lblError.setText("Xin Lỗi ! Bạn Chưa Chọn Hoặc Tạo Hoá Đơn ");

                return;
            }
        }


    }//GEN-LAST:event_tblDanhSachSpMouseClicked

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed

        int check = JOptionPane.showConfirmDialog(this, "Xoá Khỏi Giỏ ? Xoá ");

        String idHoaDon = hdrepo.selectiIdHoaDon();
        try {
            int indexDelete = tblGioHangCho.getSelectedRow();
            if (indexDelete >= 0) {
                if (check == JOptionPane.YES_OPTION) {
                    HoaDonChiTiet hoaDonChiTiet = listHoaDonChiTiet.get(indexDelete);

                    if (hdctrepo.delete(hoaDonChiTiet.getId()) != null) {
                        Integer soLuongGioHang = Integer.parseInt(tblGioHangCho.getValueAt(indexDelete, 3).toString());
                        String soLuongSanPham = hdctrepo.selectSoLuongSanPham(hoaDonChiTiet.getGiayChiTiet().getiD());
                        Integer soLuongCapNhat = soLuongGioHang + Integer.parseInt(soLuongSanPham);

                        if (gct.UpdateSo(hoaDonChiTiet.getGiayChiTiet().getiD(), soLuongCapNhat) != null) {
                            int index = tblListHoaDon.getSelectedRow();
                            HoaDon hoaDon = listHoaDon.get(index);

                            showDataSanPham();
                            showDataGoHang(hoaDon.getId());
                            BigDecimal tongTien = tinhVaThemTongTien(5);

                            JOptionPane.showMessageDialog(this, "Xoá Thành Công");
                            return;
                        }
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
        try {
            if (check == JOptionPane.YES_OPTION) {
                String idHoaDon = hdrepo.selectiIdHoaDon();
                HoaDon h = getForm();
                if (h != null) {
                    if (hdrepo.creatHoaDon(h) != null) {
                        showDaTAHoaDon();
                        tblListHoaDon.setRowSelectionInterval(0, 0);

                        selectmaHoaDon();
                        JOptionPane.showMessageDialog(this, "Tạo Hóa Đơn Thành Công");

                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }//GEN-LAST:event_btnHoaDonActionPerformed

    private void btnThanhToanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThanhToanActionPerformed

    }//GEN-LAST:event_btnThanhToanActionPerformed

    private void tblListHoaDonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblListHoaDonMouseClicked

        int selectedRow = tblListHoaDon.getSelectedRow();
        lblError.setText(null);
        if (selectedRow != -1) {
            if(tblGioHangCho.getRowCount()>=0){
            lblMaHoaDon.setText((String) tblListHoaDon.getValueAt(selectedRow, 1));

            listHoaDon = hdrepo.getAllHoaDon();
            HoaDon h = listHoaDon.get(selectedRow);

            String idHoaDon = h.getId().toString();

            showDataGoHang(idHoaDon);
            BigDecimal tongtien = tinhVaThemTongTien(5);
            lblKiemTraDiem.setForeground(java.awt.Color.BLACK);
            }else{
                lblTongTien.setText("0");
            }

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
            HoaDonChiTiet h = listHoaDonChiTiet.get(i);
            String soLuongSanPham = hdctrepo.selectSoLuongSanPham(h.getGiayChiTiet().getiD());
            HoaDon hoaDon = listHoaDon.get(index);

            int code = JOptionPane.showOptionDialog(this, "BẠN MUỐN LÀM GÌ ?", "LỰA CHỌN", 0, messegertype, null, obtion, "Số Lượng");
            switch (code) {
                case 0:
                    String soLuongGioHangGoc = tblGioHangCho.getValueAt(i, 3).toString();
                    String soLuongNhap = JOptionPane.showInputDialog("Nhập Số Lượng");

                    try {
                        if (Integer.parseInt(soLuongNhap) < Integer.parseInt(soLuongGioHangGoc)) {
                            if (soLuongNhap != null && !soLuongNhap.isEmpty()) {
                                Integer soLuongNhapEpKieu = Integer.parseInt(soLuongNhap);

                                if (soLuongNhapEpKieu >= 0) {

                                    Integer soLuongCapNhatsp = Integer.parseInt(soLuongSanPham) + soLuongNhapEpKieu;

                                    if (gct.UpdateSo(h.getGiayChiTiet().getiD(), soLuongCapNhatsp) != null) {
                                        Integer soLuongCapNhatGioHang = Integer.parseInt(soLuongGioHangGoc) - soLuongNhapEpKieu;
                                        hdctrepo.updateSoLuong(soLuongCapNhatGioHang, h.getGiayChiTiet().getiD());
                                        showDataGoHang(hoaDon.getId());
                                        showDataSanPham();
                                        BigDecimal tongtien = tinhVaThemTongTien(5);

                                    }
                                } else {
                                    JOptionPane.showMessageDialog(this, "Số Lượng Phải Là Số Dương");
                                }
                            } else {
                                JOptionPane.showMessageDialog(this, "Vui lòng nhập Số Lượng");
                            }
                        } else {
                            JOptionPane.showMessageDialog(this, "Số Lượng trong giỏ không đủ");
                        }
                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(this, "Số Lượng Phải Là Số Nguyên");
                    }

                    break;
                case 1:

                    Integer soLuongGioHang = (Integer) tblGioHangCho.getValueAt(i, 3);

                    Integer soLuongCapNhat = soLuongGioHang + Integer.parseInt(soLuongSanPham);

                    if (hdctrepo.delete(h.getId()) != null) {

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

    private void btnHuyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHuyActionPerformed

    }//GEN-LAST:event_btnHuyActionPerformed

    private void choHinhThucTTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_choHinhThucTTActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_choHinhThucTTActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        DangKyThanhVienForm t = new DangKyThanhVienForm();
        t.setVisible(true);
    }//GEN-LAST:event_jButton3ActionPerformed

    private void btnFinKhachActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFinKhachActionPerformed

        listKhachHang = khrp.getKhachHang();
        for (KhachHang k : listKhachHang) {
            if (txtMaKhach.getText().equalsIgnoreCase(k.getMa())) {
                lblKhachHang.setText(k.getName());
                lblErrKhach.setText(null);
                return;
            } else {
                lblErrKhach.setText("không Có Khách Hàng Này");
            }

        }
    }//GEN-LAST:event_btnFinKhachActionPerformed

    private void btnDeleteAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteAllActionPerformed

        int check = JOptionPane.showConfirmDialog(this, "Xoá Hết Sản Phẩm Khỏi Giỏ");
        if (check == JOptionPane.YES_OPTION) {
            int indexHoaDon = tblListHoaDon.getSelectedRow();
            HoaDon hoaDon = listHoaDon.get(indexHoaDon);
            if (hdctrepo.deleteAllHoaDonChiTiet(hoaDon.getId()) != null) {
                showDataSanPham();
                showDataGoHang(hoaDon.getId());
            }

        }
    }//GEN-LAST:event_btnDeleteAllActionPerformed

    private void btnTraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTraActionPerformed
        String ma = txtMaKhach.getText().trim();
        lblErrKiemTraDiem.setText(null);
        lblKiemTraDiem.setForeground(java.awt.Color.BLACK);
        if (ma.isBlank()) {
            JOptionPane.showMessageDialog(this, "Chưa Nhập Mã Khách Hàng");
        } else {
            BigDecimal Diem = khrp.selectTichDiem(ma);
            if (khrp.selectTichDiem(ma) != null) {
                lblKiemTraDiem.setText(Diem.toString());
                tinhVaThemTongTien(5).subtract(Diem);

            }
        }
    }//GEN-LAST:event_btnTraActionPerformed

    private void btnSuDungDienMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSuDungDienMouseClicked
        if (evt.getClickCount() == 2) {
            int check = JOptionPane.showConfirmDialog(this, "Hủy Sử Dụng Điểm");
            if(check== JOptionPane.YES_OPTION){
                lblKiemTraDiem.setForeground(java.awt.Color.BLACK);
                BigDecimal result = tinhVaThemTongTien(5);
            }
        }
    }//GEN-LAST:event_btnSuDungDienMouseClicked

    private void btnSuDungDienActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSuDungDienActionPerformed

        if (!lblMaHoaDon.getText().isEmpty()) {
            if (!lblTongTien.getText().equals("0")) {
                lblKiemTraDiem.setForeground(java.awt.Color.RED);
                BigDecimal dungDien = new BigDecimal(lblKiemTraDiem.getText().trim());
                BigDecimal result = tinhVaThemTongTien(5).subtract(dungDien);
                lblTongTien.setText(result.toString());
            } else {
                lblErrKiemTraDiem.setText("không thể sử dụng");
            }

        } else {
            lblErrKiemTraDiem.setText("Chưa Có Hoá Đơn");
        }

    }//GEN-LAST:event_btnSuDungDienActionPerformed

    private void btnHuyMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnHuyMouseClicked
        int i = tblListHoaDon.getSelectedRow();
        listHoaDon = hdrepo.getAllHoaDon();

        if (i >= 0) {
            int check = JOptionPane.showConfirmDialog(this, "Huỷ Hoá Đơn");

            if (check == JOptionPane.YES_OPTION) {
                HoaDon selectedHoaDon = listHoaDon.get(i);
                BigDecimal totalAmount = new BigDecimal(lblTongTien.getText().trim());

                try {
                    if (hdrepo.updateTrangThaiHoaDon("Huỷ", totalAmount, selectedHoaDon.getId()) != null) {

                        showDaTAHoaDon();
                        showDataSanPham();
                        lblTongTien.setText("0");
                        lblMaHoaDon.setText(null);
                        txtMaHoaDon2.setText("0");

                        JOptionPane.showMessageDialog(this, "Huỷ Thành Công");
                    }
                } catch (Exception ex) {
                    ex.printStackTrace(); // Log the exception for debugging
                    JOptionPane.showMessageDialog(this, "An error occurred while cancelling the invoice.");
                }
            }
        }
    }//GEN-LAST:event_btnHuyMouseClicked

    private void txtTienKhachDuaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTienKhachDuaKeyReleased
        String tienKhachDua = txtTienKhachDua.getText().trim();
        BigDecimal tongTien = new BigDecimal(lblTongTien.getText().trim());
        if (!tienKhachDua.equalsIgnoreCase("0") || !tongTien.equals("0")) {
            
            if (new BigDecimal(tienKhachDua).compareTo(tongTien) == 1 || new BigDecimal(tienKhachDua).compareTo(tongTien) == 0) {
                BigDecimal tienthua = new BigDecimal(tienKhachDua).subtract(tongTien);
                lblTienThua.setText(tienthua.toString());
                lblErrTienKhachDua.setText(null);
            }else{
                lblErrTienKhachDua.setText("Chưa Đủ Tiền");
            }
        }
    }//GEN-LAST:event_txtTienKhachDuaKeyReleased

    private void btnVaoDatHangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVaoDatHangActionPerformed
        card.show(pnCardGoc, "cardDatHang");
    }//GEN-LAST:event_btnVaoDatHangActionPerformed

    private void btnTroVeBanHangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTroVeBanHangActionPerformed
        card.show(pnCardGoc, "CardMuaHang");
    }//GEN-LAST:event_btnTroVeBanHangActionPerformed

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
            java.util.logging.Logger.getLogger(HoaDonForm.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(HoaDonForm.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(HoaDonForm.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(HoaDonForm.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
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
    private javax.swing.JPanel CardMuaHang;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnDeleteAll;
    private javax.swing.JButton btnFinKhach;
    private javax.swing.JButton btnHoaDon;
    private javax.swing.JButton btnHuy;
    private javax.swing.JButton btnSuDungDien;
    private javax.swing.JButton btnThanhToan;
    private javax.swing.JButton btnTra;
    private javax.swing.JButton btnTroVeBanHang;
    private javax.swing.JButton btnVaoDatHang;
    private javax.swing.JPanel cardDatHang;
    private javax.swing.JComboBox<String> cboMa;
    private javax.swing.JComboBox<String> choHinhThucTT;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JLabel lblErrKhach;
    private javax.swing.JLabel lblErrKiemTraDiem;
    private javax.swing.JLabel lblErrTienKhachDua;
    private javax.swing.JLabel lblError;
    private javax.swing.JLabel lblKhachHang;
    private javax.swing.JLabel lblKiemTraDiem;
    private javax.swing.JLabel lblMaHoaDon;
    private javax.swing.JLabel lblNhanVien;
    private javax.swing.JLabel lblTienThua;
    private javax.swing.JLabel lblTongTien;
    private javax.swing.JLabel lblTongTien1;
    private javax.swing.JPanel pnCardGoc;
    private javax.swing.JTable tblDanhSachSp;
    private javax.swing.JTable tblGioHangCho;
    private javax.swing.JTable tblListHoaDon;
    private javax.swing.JTextField txtMaHoaDon2;
    private javax.swing.JTextField txtMaKhach;
    private javax.swing.JTextField txtTienKhachDua;
    // End of variables declaration//GEN-END:variables
}
