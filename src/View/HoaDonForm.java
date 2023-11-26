package View;

import Entity.GiayChiTiet;
import Entity.HoaDon;
import Entity.HoaDonChiTiet;
import Entity.KhachHang;
import repobanhang.GiayChiTietRepo;
import repobanhang.HoaDnRepo;
import repobanhang.HoaDonChiTietRepo;
import repobanhang.KhachHangRepo;
import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;
import com.github.sarxos.webcam.WebcamResolution;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import repobanhang.TichDienRepo;

/**
 *
 * @author Admin
 */
public final class HoaDonForm extends javax.swing.JFrame implements Runnable, ThreadFactory {

    CardLayout card;

    private Webcam webcam = null;
    private WebcamPanel panel = null;
    private final Executor executor = Executors.newSingleThreadExecutor(this);
    HoaDonChiTietRepo hdctrepo = new HoaDonChiTietRepo();
    GiayChiTietRepo gct = new GiayChiTietRepo();
    HoaDnRepo hdrepo = new HoaDnRepo();
    KhachHangRepo khrp = new KhachHangRepo();
    TichDienRepo tdrp = new TichDienRepo();

    List<GiayChiTiet> listGiayChiTiet = new ArrayList<>();
    List<HoaDonChiTiet> listHoaDonChiTiet = new ArrayList<>();
    List<HoaDon> listHoaDon = new ArrayList<>();
    List<KhachHang> listKhachHang = new ArrayList<>();

    DefaultTableModel model;
    DefaultTableModel modelListGioHang;
    DefaultTableModel modelListHoaDon;

    public HoaDonForm() {
        initComponents();
        initWebcam();
        card = (CardLayout) pnlBanHang.getLayout();
        card.show(pnlBanHang, "CardMuaHang");
        listGiayChiTiet = gct.getAllGiay();
        listHoaDonChiTiet = hdctrepo.getAllHoaDon();
        listHoaDon = hdrepo.getAllHoaDon();

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

    private void initWebcam() {

        Dimension size = WebcamResolution.QVGA.getSize();
        webcam = Webcam.getWebcams().get(0);
        webcam.setViewSize(size);
        panel = new WebcamPanel(webcam);
        panel.setPreferredSize(size);
        panel.setFPSDisplayed(true);

        jpnQR.add(panel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 223, 174));

        executor.execute(this);
    }

    @Override
    public void run() {
        do {
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {

            }

            Result result = null;
            BufferedImage image = null;
            if (webcam.open()) {
                if ((image = webcam.getImage()) == null) {
                    continue;
                }
            }
            LuminanceSource source = new BufferedImageLuminanceSource(image);
            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
            try {
                result = new MultiFormatReader().decode(bitmap);
            } catch (NotFoundException ex) {

            }

            if (result != null) {
                int indexDanhSachSp = 0;
                for (GiayChiTiet giayChiTiet : listGiayChiTiet) {

                    if (giayChiTiet.getiD().equals(result.toString())) {
                        indexDanhSachSp = listGiayChiTiet.indexOf(giayChiTiet);
                    }
                }
                int check = JOptionPane.showConfirmDialog(this, "Bỏ Vào Giỏ ! Mua");

                listGiayChiTiet = gct.getAllGiay();
                listHoaDon = hdrepo.getAllHoaDon();

                if (check == JOptionPane.YES_OPTION) {
                    if (!lblMaHoaDon.getText().isEmpty()) {
                        if (indexDanhSachSp >= 0) {
                            Integer soLuongGoc = Integer.valueOf(tblDanhSachSp.getValueAt(indexDanhSachSp, 5).toString());
                            if (soLuongGoc >= 0) {

                                String soLuong = JOptionPane.showInputDialog(null, "Nhập Số Lượng");
                                try {
                                    if (Integer.valueOf(soLuong) <= soLuongGoc) {

                                        if (soLuong != null && !soLuong.isEmpty()) {
                                            int selectedRow = tblListHoaDon.getSelectedRow();
                                            HoaDon indexHoaDon = listHoaDon.get(selectedRow);//Lý Do
                                            String idHoaDonz = indexHoaDon.getId();
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
            }
        } while (true);
    }

    @Override
    public Thread newThread(Runnable r) {
        Thread t = new Thread(r, "My Thread");
        t.setDaemon(true);
        return t;
    }

    void showDataMaGiay() {
        model.setRowCount(0);
        listGiayChiTiet = gct.getAllGiay();
        String comboMaGiay = (String) cboMa.getSelectedItem();
        for (GiayChiTiet g : listGiayChiTiet) {
            if (comboMaGiay.equalsIgnoreCase("Tất cả mã giày")) {
                showDataSanPham();
            } else if (comboMaGiay.equalsIgnoreCase(g.getGiay().getMa())) {
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
            } else {

            }

        }

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
//            int row =tblGioHangCho.getSelectedRow() ;
//            int tongT = Integer.parseInt(tblGioHangCho.getValueAt(row, 5).toString());
            listHoaDonChiTiet = hdctrepo.getAllHoaDonChiTietByHoaDonID(id);

            modelListGioHang.setRowCount(0);
            if (modelListGioHang.getRowCount() < 0) {
                lblTongTien.setText("0");
            } else {
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
        model.setColumnIdentifiers(new String[]{"Mã giày", "Tên giày ", "Hãng", "Màu", "Kích cỡ", "Số lượng", "Đơn giá", "Trạng thái"});
    }

    private void column1() {
        modelListGioHang.setColumnIdentifiers(new String[]{"STT", "Mã giày", "Tên giày ", "Số lượng", "Đơn giá", "Tổng giá"});
    }

    private void column2() {
        modelListHoaDon.setColumnIdentifiers(new String[]{"STT", "Mã hóa đơn ", "Ngày tạo", "Mã NV", "Trạng thái"});
    }

    void resetThanhToan() {
        lblMaHoaDon.setText(null);
        txtTienKhachDua.setText("0");
        lblTongTien.setText("0");
        lblErrKiemTraDiem.setText(null);
        lblKiemTraDiem.setForeground(java.awt.Color.BLACK);
        modelListGioHang.setRowCount(0);
        lblTienThua.setText("0");
        lblKiemTraDiem.setText(null);
        txtMaKhach.setText("");
        lblKhachHang.setText(null);
        cboHinhThucTT.setSelectedIndex(0);
    }

    private void updateProductQuantity(int i, int quantity) {
        int quantityInRow = Integer.parseInt(tblDanhSachSp.getValueAt(i, 5).toString());
        int updatedQuantity = quantityInRow - quantity;
        gct.UpdateSo(listGiayChiTiet.get(i).getiD(), updatedQuantity);

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlDSSP = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblDanhSachSp = new javax.swing.JTable();
        lblError = new javax.swing.JLabel();
        cboMa = new javax.swing.JComboBox<>();
        pnlGioHang = new javax.swing.JPanel();
        btnDelete = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblGioHangCho = new javax.swing.JTable();
        btnDeleteAll = new javax.swing.JButton();
        pnlDSHD = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblListHoaDon = new javax.swing.JTable();
        pnlBanHang = new javax.swing.JPanel();
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
        jLabel2 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        btnThanhToan = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        btnHuy = new javax.swing.JButton();
        btnHoaDon = new javax.swing.JButton();
        cboHinhThucTT = new javax.swing.JComboBox<>();
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
        jSeparator1 = new javax.swing.JSeparator();
        jLabel19 = new javax.swing.JLabel();
        pnlThanhVien = new javax.swing.JPanel();
        txtMaKhach = new javax.swing.JTextField();
        lblKhachHang = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        lblErrKhach = new javax.swing.JLabel();
        jpnQR = new javax.swing.JPanel();
        jSeparator3 = new javax.swing.JSeparator();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        pnlDSSP.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Danh Sách Sản Phẩm", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Times New Roman", 1, 18))); // NOI18N

        tblDanhSachSp.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
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
        tblDanhSachSp.setRowHeight(25);
        tblDanhSachSp.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblDanhSachSpMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                tblDanhSachSpMouseEntered(evt);
            }
        });
        jScrollPane1.setViewportView(tblDanhSachSp);

        lblError.setFont(new java.awt.Font("Serif", 1, 14)); // NOI18N
        lblError.setForeground(new java.awt.Color(204, 0, 0));

        cboMa.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Tất Cả Mã Giày" }));
        cboMa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboMaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlDSSPLayout = new javax.swing.GroupLayout(pnlDSSP);
        pnlDSSP.setLayout(pnlDSSPLayout);
        pnlDSSPLayout.setHorizontalGroup(
            pnlDSSPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlDSSPLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cboMa, javax.swing.GroupLayout.PREFERRED_SIZE, 304, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblError, javax.swing.GroupLayout.PREFERRED_SIZE, 320, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jScrollPane1)
        );
        pnlDSSPLayout.setVerticalGroup(
            pnlDSSPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlDSSPLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(pnlDSSPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cboMa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblError, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(8, 8, 8)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(48, 48, 48))
        );

        pnlGioHang.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Giỏ Hàng", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Times New Roman", 1, 18))); // NOI18N

        btnDelete.setBackground(new java.awt.Color(0, 0, 0));
        btnDelete.setForeground(new java.awt.Color(255, 255, 255));
        btnDelete.setText("Bỏ Sản Phẩm");
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });

        tblGioHangCho.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        tblGioHangCho.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
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
        tblGioHangCho.setRowHeight(25);
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

        javax.swing.GroupLayout pnlGioHangLayout = new javax.swing.GroupLayout(pnlGioHang);
        pnlGioHang.setLayout(pnlGioHangLayout);
        pnlGioHangLayout.setHorizontalGroup(
            pnlGioHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlGioHangLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlGioHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3)
                    .addGroup(pnlGioHangLayout.createSequentialGroup()
                        .addComponent(btnDeleteAll)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnDelete)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        pnlGioHangLayout.setVerticalGroup(
            pnlGioHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlGioHangLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlGioHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnDeleteAll)
                    .addComponent(btnDelete))
                .addGap(39, 39, 39))
        );

        pnlDSHD.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Hóa Đơn Chờ", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Times New Roman", 1, 18))); // NOI18N

        tblListHoaDon.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        tblListHoaDon.setForeground(new java.awt.Color(51, 51, 51));
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
        tblListHoaDon.setRowHeight(25);
        tblListHoaDon.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblListHoaDonMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tblListHoaDon);

        javax.swing.GroupLayout pnlDSHDLayout = new javax.swing.GroupLayout(pnlDSHD);
        pnlDSHD.setLayout(pnlDSHDLayout);
        pnlDSHDLayout.setHorizontalGroup(
            pnlDSHDLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 515, Short.MAX_VALUE)
        );
        pnlDSHDLayout.setVerticalGroup(
            pnlDSHDLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
        );

        pnlBanHang.setLayout(new java.awt.CardLayout());

        cardDatHang.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 0, 0), null));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel3.setText("Đặt Hàng");

        jButton4.setBackground(new java.awt.Color(0, 0, 0));
        jButton4.setForeground(new java.awt.Color(255, 255, 255));
        jButton4.setText("Đặt Hàng");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

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
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 249, Short.MAX_VALUE)
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

        pnlBanHang.add(cardDatHang, "cardDatHang");
        cardDatHang.getAccessibleContext().setAccessibleName("");
        cardDatHang.getAccessibleContext().setAccessibleDescription("");

        CardMuaHang.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Đơn hàng", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Times New Roman", 1, 18))); // NOI18N
        CardMuaHang.setName("Tạo Hoá Đơn"); // NOI18N

        jLabel2.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel2.setText("Mã Hoá Đơn");

        jLabel5.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel5.setText("Nhân Viên");

        btnThanhToan.setBackground(new java.awt.Color(0, 0, 0));
        btnThanhToan.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        btnThanhToan.setForeground(new java.awt.Color(255, 255, 255));
        btnThanhToan.setText("Thanh Toán");
        btnThanhToan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThanhToanActionPerformed(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel9.setText("Tiền Khách Đưa");

        jLabel10.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel10.setText("Tiền Thừa");

        jLabel12.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel12.setText("Hình Thức TT");

        btnHuy.setBackground(new java.awt.Color(0, 0, 0));
        btnHuy.setForeground(new java.awt.Color(255, 255, 255));
        btnHuy.setText("Huỷ Hoá Đơn");
        btnHuy.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnHuyMouseClicked(evt);
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

        cboHinhThucTT.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Tiền Mặt", "Thẻ " }));
        cboHinhThucTT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboHinhThucTTActionPerformed(evt);
            }
        });

        lblMaHoaDon.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N

        lblNhanVien.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        lblNhanVien.setText("Chưa Làm Rùi");

        lblTongTien.setText("0");

        lblTienThua.setText("0");

        txtTienKhachDua.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtTienKhachDuaKeyReleased(evt);
            }
        });

        jLabel18.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
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

        jLabel19.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel19.setText("Tổng tiền");

        javax.swing.GroupLayout CardMuaHangLayout = new javax.swing.GroupLayout(CardMuaHang);
        CardMuaHang.setLayout(CardMuaHangLayout);
        CardMuaHangLayout.setHorizontalGroup(
            CardMuaHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(CardMuaHangLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(CardMuaHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnHoaDon)
                    .addGroup(CardMuaHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jLabel9, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel18, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel19, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 111, Short.MAX_VALUE)
                        .addComponent(jLabel10, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel12, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 3, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(CardMuaHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(CardMuaHangLayout.createSequentialGroup()
                        .addGroup(CardMuaHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(CardMuaHangLayout.createSequentialGroup()
                                .addComponent(txtTienKhachDua, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblErrTienKhachDua, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(btnThanhToan, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(CardMuaHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(lblMaHoaDon, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(lblNhanVien, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 92, Short.MAX_VALUE))
                            .addGroup(CardMuaHangLayout.createSequentialGroup()
                                .addGroup(CardMuaHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(lblKiemTraDiem, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(lblTongTien, javax.swing.GroupLayout.DEFAULT_SIZE, 93, Short.MAX_VALUE))
                                .addGap(11, 11, 11)
                                .addComponent(btnTra)
                                .addGap(18, 18, 18)
                                .addComponent(btnSuDungDien))
                            .addComponent(lblTienThua, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cboHinhThucTT, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblErrKiemTraDiem, javax.swing.GroupLayout.PREFERRED_SIZE, 258, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(CardMuaHangLayout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addComponent(btnHuy)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnVaoDatHang, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(17, 17, 17))))
        );

        CardMuaHangLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {btnHoaDon, btnHuy});

        CardMuaHangLayout.setVerticalGroup(
            CardMuaHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(CardMuaHangLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(CardMuaHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnHuy, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnVaoDatHang)
                    .addComponent(btnHoaDon, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(CardMuaHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(CardMuaHangLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(CardMuaHangLayout.createSequentialGroup()
                        .addGap(11, 11, 11)
                        .addGroup(CardMuaHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(CardMuaHangLayout.createSequentialGroup()
                                .addComponent(lblNhanVien, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(10, 10, 10)
                                .addComponent(lblMaHoaDon, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(10, 10, 10)
                                .addGroup(CardMuaHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(lblKiemTraDiem, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(btnTra)
                                    .addComponent(btnSuDungDien, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(10, 10, 10)
                                .addComponent(lblTongTien, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(10, 10, 10)
                                .addGroup(CardMuaHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtTienKhachDua, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblErrTienKhachDua, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(10, 10, 10)
                                .addComponent(lblTienThua, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(cboHinhThucTT, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jSeparator1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblErrKiemTraDiem, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnThanhToan, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(100, 100, 100))))
        );

        CardMuaHangLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {btnSuDungDien, btnTra});

        CardMuaHangLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {btnHoaDon, btnHuy, btnVaoDatHang});

        pnlBanHang.add(CardMuaHang, "CardMuaHang");

        pnlThanhVien.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Thành viên", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Times New Roman", 1, 18))); // NOI18N

        txtMaKhach.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Nhập mã khách hàng", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Times New Roman", 3, 14))); // NOI18N
        txtMaKhach.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtMaKhachKeyReleased(evt);
            }
        });

        lblKhachHang.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Tên khách hàng", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Times New Roman", 3, 12))); // NOI18N

        jButton3.setBackground(new java.awt.Color(0, 0, 0));
        jButton3.setForeground(new java.awt.Color(255, 255, 255));
        jButton3.setText("Đăng Ký Thành Viên");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        lblErrKhach.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        lblErrKhach.setForeground(new java.awt.Color(204, 0, 0));

        javax.swing.GroupLayout pnlThanhVienLayout = new javax.swing.GroupLayout(pnlThanhVien);
        pnlThanhVien.setLayout(pnlThanhVienLayout);
        pnlThanhVienLayout.setHorizontalGroup(
            pnlThanhVienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlThanhVienLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlThanhVienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblErrKhach, javax.swing.GroupLayout.PREFERRED_SIZE, 241, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(pnlThanhVienLayout.createSequentialGroup()
                        .addGroup(pnlThanhVienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtMaKhach, javax.swing.GroupLayout.DEFAULT_SIZE, 241, Short.MAX_VALUE)
                            .addComponent(lblKhachHang, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton3)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlThanhVienLayout.setVerticalGroup(
            pnlThanhVienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlThanhVienLayout.createSequentialGroup()
                .addGroup(pnlThanhVienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlThanhVienLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(txtMaKhach, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblKhachHang, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlThanhVienLayout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblErrKhach, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(30, Short.MAX_VALUE))
        );

        jpnQR.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "QR Camera", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Times New Roman", 1, 18))); // NOI18N
        jpnQR.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnlGioHang, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnlDSSP, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(pnlDSHD, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jpnQR, javax.swing.GroupLayout.PREFERRED_SIZE, 223, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jSeparator3))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnlThanhVien, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(pnlBanHang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jpnQR, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnlDSHD, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnlThanhVien, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(pnlGioHang, javax.swing.GroupLayout.PREFERRED_SIZE, 218, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(28, 28, 28)
                        .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pnlDSSP, javax.swing.GroupLayout.PREFERRED_SIZE, 222, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(pnlBanHang, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
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
                    Integer soLuongGoc = Integer.valueOf(tblDanhSachSp.getValueAt(indexDanhSachSp, 5).toString());
                    if (soLuongGoc >= 0) {

                        String soLuong = JOptionPane.showInputDialog(null, "Nhập Số Lượng");
                        try {
                            if (Integer.valueOf(soLuong) <= soLuongGoc) {

                                if (soLuong != null && !soLuong.isEmpty()) {
                                    int selectedRow = tblListHoaDon.getSelectedRow();
                                    HoaDon indexHoaDon = listHoaDon.get(selectedRow);//Lý Do
                                    String idHoaDonz = indexHoaDon.getId();
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
                        lblKiemTraDiem.setText("0");
                        lblTienThua.setText("0");
                        lblTongTien.setText("0");
                        lblTongTien1.setText("0");
                        txtTienKhachDua.setText("0");
                        lblError.setText(null);

                        showDataGoHang(idHoaDon);
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
        if (!lblMaHoaDon.getText().trim().isEmpty()) {
            
            int check = JOptionPane.showConfirmDialog(this, "Thanh Toán ?");
            if (check == JOptionPane.YES_OPTION) {
                BigDecimal tienKH = new BigDecimal(txtTienKhachDua.getText().trim());
                System.out.println("Tiền khách đưa:" + tienKH);
                BigDecimal tienThua = new BigDecimal(lblTienThua.getText().trim());
                String trangThai = "Đã thanh toán";
                String maHD = tblListHoaDon.getValueAt(tblListHoaDon.getSelectedRow(), 1).toString();
                BigDecimal tongT = new BigDecimal(lblTongTien.getText().trim());
                listKhachHang = khrp.getKhachHang();
                for (KhachHang k : listKhachHang) {
                    if (k.getMa().equalsIgnoreCase(txtMaKhach.getText().trim())) {
                        String idKH = k.getId();
                        BigDecimal diem = new BigDecimal(lblKiemTraDiem.getText().trim());
                        tdrp.tichDiem(diem, idKH);
                        int ok = hdrepo.updateHDByMa(trangThai, tienKH, tienThua, cboHinhThucTT.getSelectedItem().toString(), idKH, tongT, maHD);
                        if (ok == 0) {
                            showDaTAHoaDon();
                            resetThanhToan();
                            JOptionPane.showMessageDialog(this, "Thanh toán thành công !");
                        } else {
                            JOptionPane.showMessageDialog(this, "Thanh toán thất bại !");
                        }
                    }
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Chưa tạo hóa đơn!");
        }

    }//GEN-LAST:event_btnThanhToanActionPerformed

    private void tblListHoaDonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblListHoaDonMouseClicked

        int selectedRow = tblListHoaDon.getSelectedRow();
        lblError.setText(null);
        if (selectedRow != -1) {
            if (tblGioHangCho.getRowCount() >= 0) {
                lblMaHoaDon.setText((String) tblListHoaDon.getValueAt(selectedRow, 1));

                listHoaDon = hdrepo.getAllHoaDon();
                HoaDon h = listHoaDon.get(selectedRow);

                String idHoaDon = h.getId();

                showDataGoHang(idHoaDon);
                BigDecimal tongTien = BigDecimal.ZERO;
                int rowCount = tblGioHangCho.getRowCount();
                if (rowCount <= 0) {
                    lblTongTien.setText("0");
                    lblKiemTraDiem.setText("0");
                    lblKiemTraDiem.setForeground(Color.black);
                    lblTienThua.setText("0");
                } else {
                    for (int i = 0; i < rowCount; i++) {
                        String tongTienGio = tblGioHangCho.getValueAt(i, 5).toString();

                        BigDecimal giaTien = new BigDecimal(tongTienGio);

                        tongTien = tongTien.add(giaTien);
                        lblTongTien.setText(tongTien.toString());
                        lblKiemTraDiem.setForeground(java.awt.Color.BLACK);
                        lblError.setText(null);
                    }

                }
            } else {
                lblTongTien.setText("0");
            }
            if (!lblMaHoaDon.getText().isEmpty()) {
                lblErrKiemTraDiem.setText(null);
            }
        }
    }//GEN-LAST:event_tblListHoaDonMouseClicked

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

    }//GEN-LAST:event_tblGioHangChoMouseEntered

    private void cboHinhThucTTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboHinhThucTTActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cboHinhThucTTActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        DangKyThanhVienForm t = new DangKyThanhVienForm();
        t.setVisible(true);
    }//GEN-LAST:event_jButton3ActionPerformed

    private void btnDeleteAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteAllActionPerformed

        int check = JOptionPane.showConfirmDialog(this, "Xoá Hết Sản Phẩm Khỏi Giỏ");
        if (check == JOptionPane.YES_OPTION) {
            int indexHoaDon = tblListHoaDon.getSelectedRow();
            HoaDon hoaDon = listHoaDon.get(indexHoaDon);
            if (hdrepo.deleteAllHoaDonChiTiet(hoaDon.getId()) != null) {
                showDataSanPham();
                showDataGoHang(hoaDon.getId());
            }

        }
    }//GEN-LAST:event_btnDeleteAllActionPerformed

    private void btnTraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTraActionPerformed
        if (!lblMaHoaDon.getText().trim().isEmpty()) {
            String ma = txtMaKhach.getText().trim();
            lblErrKiemTraDiem.setText(null);
            lblKiemTraDiem.setForeground(java.awt.Color.BLACK);
            if (ma.isBlank()) {
                JOptionPane.showMessageDialog(this, "Chưa nhập mã khách hàng");

            } else {
                BigDecimal Diem = khrp.selectTichDiem(ma);
                if (khrp.selectTichDiem(ma) != null) {
                    lblKiemTraDiem.setText(Diem.toString());
                    tinhVaThemTongTien(5).subtract(Diem);
                    btnTra.setEnabled(false);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Chưa tạo hóa đơn!");
        }
    }//GEN-LAST:event_btnTraActionPerformed

    private void btnSuDungDienMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSuDungDienMouseClicked

        String ma = txtMaKhach.getText().trim();
        if (ma.isBlank()) {
            JOptionPane.showMessageDialog(this, "Chưa nhập mã khách hàng");
        } else {
//2 lần click hủy dùng điểm
            if (evt.getClickCount() == 2) {
                int check = JOptionPane.showConfirmDialog(this, "Hủy Sử Dụng Điểm");
                if (check == JOptionPane.YES_OPTION) {
                    lblKiemTraDiem.setForeground(java.awt.Color.BLACK);

                    BigDecimal tongTien = new BigDecimal(lblTongTien.getText().trim());
                    BigDecimal dungDien = new BigDecimal(lblKiemTraDiem.getText().trim());

                    BigDecimal tongTienHuy = tongTien.add(dungDien);
                    lblTongTien.setText(tongTienHuy.toString());

                    BigDecimal tienKhachDua = new BigDecimal(txtTienKhachDua.getText().trim());
                    if (tienKhachDua != null && !tienKhachDua.equals(BigDecimal.ZERO)) {
                        lblTienThua.setText(tienKhachDua.subtract(tongTienHuy).toString());
                    }
                }
            }

            // 1 lần click dùng điểm
        }
    }//GEN-LAST:event_btnSuDungDienMouseClicked

    private void btnSuDungDienActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSuDungDienActionPerformed
        String ma = txtMaKhach.getText().trim();
        if (!lblMaHoaDon.getText().isEmpty()) {
            if (ma.isBlank()) {
                JOptionPane.showMessageDialog(this, "Không tìm thấy khách hàng");
            } else {
                try {
                    BigDecimal tongTien = new BigDecimal(lblTongTien.getText().trim());
                    BigDecimal tienKhachDua = new BigDecimal(txtTienKhachDua.getText().trim());
                    if (!tongTien.equals(BigDecimal.ZERO) || !lblTongTien.getText().isEmpty() || lblTongTien != null
                            || !tienKhachDua.equals(BigDecimal.ZERO) || !txtTienKhachDua.getText().isEmpty() || tienKhachDua != null) {
                        if (tienKhachDua.compareTo(BigDecimal.ZERO) == 0 || tienKhachDua.compareTo(BigDecimal.ZERO) == -1) {
                            lblTienThua.setText("0");
                            lblErrKiemTraDiem.setText("Vui lòng nhập tiền khách đưa !");
                        } else {
                            BigDecimal diem = new BigDecimal(lblKiemTraDiem.getText().trim());
                            BigDecimal result = tinhVaThemTongTien(5).subtract(diem);
                            BigDecimal diemConlai = diem.subtract(diem);
                            lblTongTien.setText(result.toString());
                            BigDecimal tienThua = tienKhachDua.subtract(result);
                            lblTienThua.setText(tienThua.toString());
                            lblKiemTraDiem.setForeground(java.awt.Color.RED);
                            lblKiemTraDiem.setText(diemConlai.toString());
                        }
                    } else {
                        lblErrKiemTraDiem.setText("Không thể sử dụng");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Error:" + e.getMessage());
                    JOptionPane.showMessageDialog(this, "Chưa có sản phẩm nào trong giỏ!");
                }
            }

        } else {
            JOptionPane.showMessageDialog(this, "Chưa tạo hóa đơn!");
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
        } else {
            JOptionPane.showMessageDialog(this, "Muốn hủy hóa đơn nào cơ ?");
        }
    }//GEN-LAST:event_btnHuyMouseClicked

    private void txtTienKhachDuaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTienKhachDuaKeyReleased
        String tienKhachDua = txtTienKhachDua.getText().trim();
        try {
            BigDecimal tienKhachDuaDecimal = new BigDecimal(tienKhachDua);

            BigDecimal tongTien = new BigDecimal(lblTongTien.getText().trim());
            if (!tienKhachDua.equalsIgnoreCase("0") || !tienKhachDua.isEmpty() || tongTien != null) {

                if (tienKhachDuaDecimal.compareTo(tongTien) == 1 || tienKhachDuaDecimal.compareTo(tongTien) == 0) {
                    BigDecimal tienthua = tienKhachDuaDecimal.subtract(tongTien);
                    lblTienThua.setText(tienthua.toString());
                    lblErrTienKhachDua.setText(null);
                } else {
                    lblErrKiemTraDiem.setText(null);
                    lblErrTienKhachDua.setText("Chưa Đủ Tiền");
                    lblTienThua.setText("0");
                }
            } else {
                lblErrTienKhachDua.setText("Nhập tiền khách đưa");
            }
        } catch (NumberFormatException e) {
            lblErrTienKhachDua.setText("Tiền Phải Là Số");
            lblTienThua.setText("0");
        }
    }//GEN-LAST:event_txtTienKhachDuaKeyReleased

    private void btnVaoDatHangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVaoDatHangActionPerformed
        card.show(pnlBanHang, "cardDatHang");
    }//GEN-LAST:event_btnVaoDatHangActionPerformed

    private void btnTroVeBanHangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTroVeBanHangActionPerformed
        card.show(pnlBanHang, "CardMuaHang");
    }//GEN-LAST:event_btnTroVeBanHangActionPerformed

    private void cboMaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboMaActionPerformed
        showDataMaGiay();
    }//GEN-LAST:event_cboMaActionPerformed

    private void txtMaKhachKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtMaKhachKeyReleased
        listKhachHang = khrp.getKhachHang();
        for (KhachHang k : listKhachHang) {
            if (txtMaKhach.getText().equalsIgnoreCase(k.getMa())) {
                lblKhachHang.setText(k.getName());
                lblErrKhach.setText(null);
                btnTra.setEnabled(true);
                lblKiemTraDiem.setText("0");
                lblKiemTraDiem.setForeground(getForeground());
                lblTienThua.setText("0");
                txtTienKhachDua.setText("");
                lblTongTien.setText(tinhVaThemTongTien(5).toString());
//                txtTienKhachDua.setText("0");
                return;
            } else {
                lblErrKhach.setText("Không tìm thấy khách hàng tương ứng!");
            }
        }
    }//GEN-LAST:event_txtMaKhachKeyReleased

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed

    }//GEN-LAST:event_jButton4ActionPerformed

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
    private javax.swing.JButton btnHoaDon;
    private javax.swing.JButton btnHuy;
    private javax.swing.JButton btnSuDungDien;
    private javax.swing.JButton btnThanhToan;
    private javax.swing.JButton btnTra;
    private javax.swing.JButton btnTroVeBanHang;
    private javax.swing.JButton btnVaoDatHang;
    private javax.swing.JPanel cardDatHang;
    private javax.swing.JComboBox<String> cboHinhThucTT;
    private javax.swing.JComboBox<String> cboMa;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JPanel jpnQR;
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
    private javax.swing.JPanel pnlBanHang;
    private javax.swing.JPanel pnlDSHD;
    private javax.swing.JPanel pnlDSSP;
    private javax.swing.JPanel pnlGioHang;
    private javax.swing.JPanel pnlThanhVien;
    private javax.swing.JTable tblDanhSachSp;
    private javax.swing.JTable tblGioHangCho;
    private javax.swing.JTable tblListHoaDon;
    private javax.swing.JTextField txtMaHoaDon2;
    private javax.swing.JTextField txtMaKhach;
    private javax.swing.JTextField txtTienKhachDua;
    // End of variables declaration//GEN-END:variables
}
