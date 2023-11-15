/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Repository;

import Entity.HoaDon;
import Entity.KhachHang;
import Entity.KichCo;
import Entity.NhanVien;
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
public class HoaDnRepo {

    public List<HoaDon> getAllHoaDon() {
        List<HoaDon> listkd = new ArrayList<>();
        String sql = "SELECT \n"
                + "    HOADON.ID AS HOADON_ID, HOADON.MA AS HOADON_MA, HOADON.NGAYTAO AS HOADON_NGAYTAO,\n"
                + "    HOADON.TEN_NGUOINHAN AS HOADON_TEN_NGUOINHAN, HOADON.SDT AS HOADON_SDT,\n"
                + "    HOADON.DIACHI AS HOADON_DIACHI, HOADON.PHISHIP AS HOADON_PHISHIP,\n"
                + "    HOADON.TONGTIEN AS HOADON_TONGTIEN, HOADON.TRANGTHAI AS HOADON_TRANGTHAI,\n"
                + "\n"
                + "    NHANVIEN.ID AS NHANVIEN_ID, NHANVIEN.MA AS NHANVIEN_MA, NHANVIEN.TEN AS NHANVIEN_TEN,\n"
                + "    NHANVIEN.GIOITINH AS NHANVIEN_GIOITINH, NHANVIEN.SDT AS NHANVIEN_SDT,\n"
                + "    NHANVIEN.DIACHI AS NHANVIEN_DIACHI, NHANVIEN.NGAYSINH AS NHANVIEN_NGAYSINH,\n"
                + "    NHANVIEN.MATKHAU AS NHANVIEN_MATKHAU, NHANVIEN.VAITRO AS NHANVIEN_VAITRO,\n"
                + "    NHANVIEN.TRANGTHAI AS NHANVIEN_TRANGTHAI,\n"
                + "\n"
                + "    KHACHHANG.ID AS KHACHHANG_ID, KHACHHANG.MA AS KHACHHANG_MA,\n"
                + "    KHACHHANG.TEN AS KHACHHANG_TEN, KHACHHANG.GIOITINH AS KHACHHANG_GIOITINH,\n"
                + "    KHACHHANG.SDT AS KHACHHANG_SDT, KHACHHANG.DIACHI AS KHACHHANG_DIACHI\n"
                + "\n"
                + "FROM HOADON\n"
                + "LEFT JOIN NHANVIEN ON HOADON.ID_NHANVIEN = NHANVIEN.ID\n"
                + "LEFT JOIN KHACHHANG ON HOADON.ID_KHACHHANG = KHACHHANG.ID;";
        try (Connection con = DbConText.getConnection();) {

            Statement stm = con.createStatement();
            ResultSet rs = stm.executeQuery(sql);
            while (rs.next()) {
                NhanVien n = new NhanVien(rs.getString("NHANVIEN_ID"), rs.getString("NHANVIEN_MA"), rs.getString("NHANVIEN_TEN"), rs.getBoolean("NHANVIEN_GIOITINH"),
                        rs.getString("NHANVIEN_SDT"), rs.getString("NHANVIEN_DIACHI"),
                        rs.getDate("NHANVIEN_NGAYSINH"), rs.getString("NHANVIEN_MATKHAU"), rs.getString("NHANVIEN_VAITRO"),
                        rs.getString("NHANVIEN_TRANGTHAI"));
                KhachHang k = new KhachHang(rs.getString("KHACHHANG_ID"), rs.getString("KHACHHANG_TEN"), rs.getBoolean("KHACHHANG_GIOITINH"),
                        rs.getString("KHACHHANG_SDT"), rs.getString("KHACHHANG_DIACHI"));
                listkd.add(new HoaDon(rs.getString("HOADON_ID"), rs.getString("HOADON_MA"), n, k, rs.getDate("HOADON_NGAYTAO"),
                        rs.getString("HOADON_TEN_NGUOINHAN"), rs.getString("HOADON_SDT"), rs.getString("HOADON_DIACHI"),
                        rs.getBigDecimal("HOADON_PHISHIP"), rs.getBigDecimal("HOADON_TONGTIEN"), rs.getString("HOADON_TRANGTHAI")));

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return listkd;
    }

    public HoaDon creatHoaDon(HoaDon h) {
        String sql = "INSERT INTO HOADON (ID,MA, ID_NHANVIEN, ID_KHACHHANG, TEN_NGUOINHAN, SDT, DIACHI, PHISHIP, TONGTIEN, TRANGTHAI)\n"
                + "        VALUES (newid(),dbo.AUTO_MaHD(), NULL, NULL, ?, ?, ?, ?, ?, ?)";
        try (Connection con = DbConText.getConnection();) {
            PreparedStatement pstm = con.prepareStatement(sql);

            pstm.setObject(1, h.getTenNguoiNhan());
            pstm.setObject(2, h.getSdt());
            pstm.setObject(3, h.getDiaChi());
            pstm.setObject(4, h.getPhiShip());
            pstm.setObject(5, h.getTongTien());
            pstm.setObject(6, h.getTrangThai());
            pstm.executeUpdate();

        } catch (Exception e) {
            System.out.println("CREATE HOA DON BI LOI");
            e.printStackTrace();

        }
        return h;
    }

    public String selectMaHoaDon() {
        String sql = "SELECT TOP 1 * FROM HoaDon ORDER BY MA DESC";

        try (Connection con = DbConText.getConnection(); Statement statement = con.createStatement(); ResultSet resultSet = statement.executeQuery(sql)) {

            if (resultSet.next()) {
                return resultSet.getString(2);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("SELECT MA HOA DON LỖI");

        }

        return null;
    }

    public String selectiIdHoaDon() {
        String sql = "SELECT TOP 1 * FROM HoaDon ORDER BY MA DESC";

        try (Connection con = DbConText.getConnection(); Statement statement = con.createStatement(); ResultSet resultSet = statement.executeQuery(sql)) {

            if (resultSet.next()) {
                return resultSet.getString(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("LỖI SELECTID HOA DON");
            // Handle SQL exception if necessary
        }

        return null;
    }

    public String updateTrangThi(String trangThai, String idHoaDOn) {
        String sql = "update HOADON set TRANGTHAI =? where ID =? ";
        try (Connection con = DbConText.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setObject(1, trangThai);
            ps.setObject(2, idHoaDOn);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Lỗi UPDATE TRANG THAI");
        }
        return null;
    }
}
