/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package repobanhang;

import Entity.DanhMuc;
import Entity.Giay;
import Entity.GiayChiTiet;
import Entity.Hang;

import Entity.KichCo;
import Entity.KieuDang;
import Entity.MauSac;
import java.math.BigDecimal;
import java.util.List;
import java.sql.*;
import java.util.ArrayList;
import util.DbConText;

/**
 *
 * @author Admin
 */
public class GiayChiTietRepo {

    public List<GiayChiTiet> getAllGiay() {
        List<GiayChiTiet> listGiay = new ArrayList<>();
        String sql = "SELECT *FROM GIAYCHITIET\n"
                + "INNER JOIN DANHMUC ON GIAYCHITIET.ID_DANHMUC = DANHMUC.ID\n"
                + "INNER JOIN HANG ON GIAYCHITIET.ID_HANG = HANG.ID\n"
                + "INNER JOIN MAUSAC ON GIAYCHITIET.ID_MAUSAC = MAUSAC.ID\n"
                + "INNER JOIN GIAY ON GIAYCHITIET.ID_GIAY = GIAY.ID\n"
                + "INNER JOIN KICHCO ON GIAYCHITIET.ID_KICHCO = KICHCO.ID\n"
                + "INNER JOIN KIEUDANG ON GIAYCHITIET.ID_KIEUDANG = KIEUDANG.ID\n";

        try (Connection con = DbConText.getConnection(); Statement stm = con.createStatement();) {

            ResultSet rs = stm.executeQuery(sql);
            while (rs.next()) {
                Giay giay = new Giay(rs.getString("ID_GIAY"), rs.getString("MA_GIAY"), rs.getString(21));
                Hang hang = new Hang(rs.getString("ID_HANG"), rs.getString(16));
                KieuDang kieuDang = new KieuDang(rs.getString("ID_KIEUDANG"), rs.getString(25));
                DanhMuc danhmuc = new DanhMuc(rs.getString("ID_DANHMUC"), rs.getString(14));
                MauSac mauSac = new MauSac(rs.getString("ID_MAUSAC"), rs.getString(18));
                KichCo kichCo = new KichCo(rs.getString("ID_KICHCO"), rs.getInt("SIZE"));

                listGiay.add(new GiayChiTiet(rs.getString("ID"), giay, hang, kieuDang, danhmuc, mauSac, kichCo, rs.getString(8),
                        rs.getBigDecimal(9), rs.getInt(10), rs.getString(11), rs.getString(12)));

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listGiay;
    }

    public Integer UpdateSo(String id_GIAYCT, int soLuong) {
        try {
            String sql = "update GIAYCHITIET set SOLUONG = ? WHERE ID = ?";
            Connection con = DbConText.getConnection();
            PreparedStatement pstm = con.prepareStatement(sql);
            pstm.setObject(1, soLuong);
            pstm.setObject(2, id_GIAYCT);

            return pstm.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public String UpdateSoLuong(String idHoaDonCt, Integer soLuong) {
        try {
            String sql = "update HOADONCHITIET set SOLUONG = ? WHERE ID = ?";
            Connection con = DbConText.getConnection();
            PreparedStatement pstm = con.prepareStatement(sql);
            pstm.setObject(1, soLuong);
            pstm.setObject(2, idHoaDonCt);

            pstm.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();

        }
        return null;

    }

}
