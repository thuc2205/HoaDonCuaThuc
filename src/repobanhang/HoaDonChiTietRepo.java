/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package repobanhang;

import Entity.DanhMuc;
import Entity.Giay;
import Entity.GiayChiTiet;
import Entity.Hang;
import Entity.HoaDon;
import Entity.HoaDonChiTiet;
import Entity.KichCo;
import Entity.KieuDang;
import Entity.MauSac;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import util.DbConText;
import java.sql.*;

/**
 *
 * @author Admin
 */
public class HoaDonChiTietRepo {

    public List<HoaDonChiTiet> getAllHoaDon() {
        List<HoaDonChiTiet> listkd = new ArrayList<>();
        String sql = "SELECT HOADONCHITIET.*, GIAYCHITIET.*, GIAY.* ,HANG.*,KIEUDANG.*,DANHMUC.*,MAUSAC.*,KICHCO.*\n"
                + "                 FROM HOADONCHITIET\n"
                + "                 INNER JOIN GIAYCHITIET ON HOADONCHITIET.ID_GIAYCT = GIAYCHITIET.ID \n"
                + "                 INNER JOIN GIAY ON GIAYCHITIET.ID_GIAY = GIAY.ID\n"
                + "				 INNER JOIN HANG ON GIAYCHITIET.ID_HANG = HANG.ID\n"
                + "				 INNER JOIN KIEUDANG ON GIAYCHITIET.ID_KIEUDANG = KIEUDANG.ID\n"
                + "				 INNER JOIN DANHMUC ON GIAYCHITIET.ID_DANHMUC = DANHMUC.ID\n"
                + "				 INNER JOIN MAUSAC ON GIAYCHITIET.ID_MAUSAC = MAUSAC.ID\n"
                + "				 INNER JOIN KICHCO ON GIAYCHITIET.ID_KICHCO = KICHCO.ID";
        try (Connection con = DbConText.getConnection(); Statement stm = con.createStatement();) {

            ResultSet rs = stm.executeQuery(sql);
            while (rs.next()) {
                Giay g = new Giay(rs.getString("ID_GIAY"), rs.getString(20), rs.getString(21));
                Hang h = new Hang(rs.getString("ID_HANG"), rs.getString(23));
                KieuDang k = new KieuDang(rs.getString("ID_KIEUDANG"), rs.getString(25));
                DanhMuc m = new DanhMuc(rs.getString("ID_DANHMUC"), rs.getString(27));
                MauSac ms = new MauSac(rs.getString("ID_MAUSAC"), rs.getString(29));
                KichCo size = new KichCo(rs.getString("ID_KICHCO"), rs.getInt(31));
                GiayChiTiet giayCt = new GiayChiTiet(rs.getString(7),
                        g, h, k, m, ms, size,
                        rs.getString(14), rs.getBigDecimal(15), rs.getInt(16),
                        rs.getString(17), rs.getString(18));
                listkd.add(new HoaDonChiTiet(rs.getString(1), giayCt, rs.getBigDecimal(4), rs.getInt(5), rs.getString(6)));
            }

        } catch (Exception e) {
            e.printStackTrace();

        }
        return listkd;
    }

    public Integer creatGiHang(String id, String id_HoaDon, BigDecimal gia, int soluong) {
        try {
            String sql = "INSERT INTO HOADONCHITIET(ID,ID_GIAYCT,ID_HOADON,GIA,SOLUONG) VALUES(newid(),?,?,?,?)";
            Connection con = DbConText.getConnection();
            PreparedStatement pstm = con.prepareCall(sql);
            pstm.setObject(1, id);
            pstm.setObject(2, id_HoaDon);
            pstm.setObject(3, gia);
            pstm.setObject(4, soluong);

            return pstm.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }

    }

    public Integer delete(String id) {
        try {
            String sql = "DELETE FROM HOADONCHITIET WHERE ID = ?";
            Connection con = DbConText.getConnection();
            PreparedStatement pstm = con.prepareCall(sql);
            pstm.setObject(1, id);
            return pstm.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }

    }

//    public Integer updateIdHoaDon(String h) {
//        String sql = "update HOADONCHITIET set ID_HOADON = ? where ID_HOADON is NULL;";
//        try {
//            Connection con = DbConText.getConnection();
//            PreparedStatement pstm = con.prepareCall(sql);
//
//            pstm.setObject(1, h);
//            return pstm.executeUpdate();
//        } catch (Exception e) {
//            e.printStackTrace();
//            System.out.println(e);
//            return 0;
//        }
    public Integer updateSoLuong(int sl, String id) {
        String sql = "update HOADONCHITIET set SOLUONG = ? where ID_GIAYCT =?;";
        try {
            Connection con = DbConText.getConnection();
            PreparedStatement pstm = con.prepareStatement(sql);

            pstm.setObject(1, sl);
            pstm.setObject(2, id);
            return pstm.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
            return 0;
        }
    }

    public String selectSoLuongSanPham(String id) {
        String sql = "SELECT SOLUONG FROM GIAYCHITIET WHERE ID = ?";

        try (Connection con = DbConText.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, id);
            ResultSet resultSet = ps.executeQuery();

            if (resultSet.next()) {
                return resultSet.getString("SOLUONG");
            }

        } catch (SQLException e) {
            e.printStackTrace();

        }

        return null;
    }

    public List<HoaDonChiTiet> getAllHoaDonChiTietByHoaDonID(String id) throws SQLException {
        String sql = "SELECT HOADONCHITIET.*, GIAYCHITIET.*, GIAY.* ,HANG.*,KIEUDANG.*,DANHMUC.*,MAUSAC.*,KICHCO.*\n"
                + "                 FROM HOADONCHITIET\n"
                + "                 INNER JOIN GIAYCHITIET ON HOADONCHITIET.ID_GIAYCT = GIAYCHITIET.ID \n"
                + "                 INNER JOIN GIAY ON GIAYCHITIET.ID_GIAY = GIAY.ID\n"
                + "				 INNER JOIN HANG ON GIAYCHITIET.ID_HANG = HANG.ID\n"
                + "				 INNER JOIN KIEUDANG ON GIAYCHITIET.ID_KIEUDANG = KIEUDANG.ID\n"
                + "				 INNER JOIN DANHMUC ON GIAYCHITIET.ID_DANHMUC = DANHMUC.ID\n"
                + "				 INNER JOIN MAUSAC ON GIAYCHITIET.ID_MAUSAC = MAUSAC.ID\n"
                + "				 INNER JOIN KICHCO ON GIAYCHITIET.ID_KICHCO = KICHCO.ID\n"
                + " WHERE HOADONCHITIET.ID_HOADON =?";
        List<HoaDonChiTiet> listHoaDonChiTiet = new ArrayList<>();
        try (Connection con = DbConText.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, id);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Giay g = new Giay(rs.getString("ID_GIAY"), rs.getString(20), rs.getString(21));
                Hang h = new Hang(rs.getString("ID_HANG"), rs.getString(23));
                KieuDang k = new KieuDang(rs.getString("ID_KIEUDANG"), rs.getString(25));
                DanhMuc m = new DanhMuc(rs.getString("ID_DANHMUC"), rs.getString(27));
                MauSac ms = new MauSac(rs.getString("ID_MAUSAC"), rs.getString(29));
                KichCo size = new KichCo(rs.getString("ID_KICHCO"), rs.getInt(31));
                GiayChiTiet giayCt = new GiayChiTiet(rs.getString(7),
                        g, h, k, m, ms, size,
                        rs.getString(14), rs.getBigDecimal(15), rs.getInt(16),
                        rs.getString(17), rs.getString(18));

                listHoaDonChiTiet.add(new HoaDonChiTiet(rs.getString(1), giayCt, rs.getString("ID_HOADON"), rs.getBigDecimal(4), rs.getInt(5), rs.getString(6)));

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return listHoaDonChiTiet;
    }

    public Integer selectSoLuongGioHangGoc(String idHoaDon, String idGiayCt) {
        String sql = "SELECT SOLUONG FROM HOADONCHITIET WHERE ID_HOADON = ? AND ID_GIAYCT = ?";
        int soLuong = 0;

        try (Connection con = DbConText.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, idHoaDon);
            ps.setString(2, idGiayCt);
            ResultSet resultSet = ps.executeQuery();

            if (resultSet.next()) {
                soLuong = resultSet.getInt("SOLUONG");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return soLuong;
    }

    public Integer deleteAllHoaDonChiTiet(String idHoaDon) {
        String sql = "delete from HOADONCHITIET WHERE ID_HOADON = ?";

        try (Connection con = DbConText.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, idHoaDon);
            return ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

}
