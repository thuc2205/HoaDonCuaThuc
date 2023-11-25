/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package repobanhang;

import Entity.Giay;
import Entity.KhachHang;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import util.DbConText;

/**
 *
 * @author Admin
 */
public class KhachHangRepo {

    public List<KhachHang> getKhachHang() {
        List<KhachHang> listkd = new ArrayList<>();
        String sql = "select*from KHACHHANG order by MA desc";
        try {
            Connection con = DbConText.getConnection();
            Statement stm = con.createStatement();
            ResultSet rs = stm.executeQuery(sql);
            while (rs.next()) {
                listkd.add(new KhachHang(rs.getString(1), rs.getString(2), rs.getString(3),
                        rs.getBoolean(4), rs.getString(5), rs.getString(6), rs.getString(7)));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return listkd;
    }

    public String selectMaKhachHang() {
        String sql = "SELECT TOP 1 * FROM KHACHHANG ORDER BY MA DESC";

        try (Connection con = DbConText.getConnection(); Statement statement = con.createStatement(); ResultSet resultSet = statement.executeQuery(sql)) {

            if (resultSet.next()) {
                return resultSet.getString(2);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("SELECT MA KHACH LỖI");

        }

        return null;
    }

    public Integer congTichDiem(BigDecimal diem) {
        String sql = "INSERT INTO TICHDIEN(ID_TICHDIEM,DIEM) VALUES (newid(),?)";
        try (Connection con = DbConText.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setBigDecimal(1, diem);
            return ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public KhachHang createKhach(KhachHang h) {
        String sql = "INSERT INTO KHACHHANG(TEN, GIOITINH, SDT,ID_TICHDIEM) VALUES (?, ?, ?,?)";
        String sqlSelectTichDiem = "SELECT TOP 1 ID_TICHDIEM FROM TICHDIEN";
        try (Connection con = DbConText.getConnection(); PreparedStatement ps = con.prepareStatement(sql); Statement stm = con.createStatement()) {
            ps.setObject(1, h.getName());
            ps.setObject(2, h.getGioiTinh());
            ps.setObject(3, h.getSdt());
            ResultSet rs = stm.executeQuery(sqlSelectTichDiem);
            String idTichDiem = null;
            if (rs.next()) {
                idTichDiem = rs.getString("ID_TICHDIEM");
            }

            if (idTichDiem != null) {

                ps.setObject(1, h.getName());
                ps.setObject(2, h.getGioiTinh());
                ps.setObject(3, h.getSdt());
                ps.setObject(4, idTichDiem);
                ps.executeUpdate();
            } else {
                System.out.println("Không tìm thấy ID_TICHDIEM để liên kết.");
            }

        } catch (Exception e) {
            System.out.println("LỖI CREAT KHACH");
            e.printStackTrace();
        }
        return h;
    }

    public KhachHang updateKhach(KhachHang h) {
        String sql = "update KHACHHANG set TEN =? ,GIOITINH=?, SDT=? where ID =?";
        try (Connection con = DbConText.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setObject(1, h.getName());
            ps.setObject(2, h.getGioiTinh());
            ps.setObject(3, h.getSdt());
            ps.setObject(4, h.getId());
            ps.executeUpdate();

        } catch (Exception e) {
            System.out.println("LỖI UPDATE KHACH");
            e.printStackTrace();
        }
        return h;

    }

    public BigDecimal selectTichDiem(String ma) {
        String sql = "SELECT t.DIEM\n"
                + "FROM KHACHHANG k\n"
                + "JOIN TICHDIEN t ON k.ID_TICHDIEM = t.ID_TICHDIEM\n"
                + "WHERE k.MA = ?";

        try (Connection con = DbConText.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, ma);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getBigDecimal("DIEM");
            }
        } catch (Exception e) {
            System.out.println("LỖI SELECT TICHDIEM");
            e.printStackTrace();
        }

        return BigDecimal.ZERO;
    }

}
