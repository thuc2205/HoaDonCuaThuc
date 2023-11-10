/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Repository;

import Entity.HoaDon;
import Entity.KichCo;
import Entity.NhanVien;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import util.DbConText;

/**
 *
 * @author Admin
 */
public class HoaDnRepo {

    public List<HoaDon> getAllHoaDon() {
        List<HoaDon> listkd = new ArrayList<>();
        String sql = "SELECT *\n"
                + "FROM HOADON\n"
                + "INNER JOIN NHANVIEN ON HOADON.ID_NHANVIEN = NHANVIEN.ID\n"
                + "INNER JOIN HOADONCHITIET ON HOADON.ID = HOADONCHITIET.ID_HOADON\n"
                + "INNER JOIN KHACHHANG ON HOADON.ID_KHACHHANG = KHACHHANG.ID;";
        try {
            Connection con = DbConText.getConnection();
            Statement stm = con.createStatement();
            ResultSet rs = stm.executeQuery(sql);
            while (rs.next()) {
             
               
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return listkd;
    }

}
