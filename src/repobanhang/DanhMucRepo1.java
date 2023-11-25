/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package repobanhang;

import Entity.DanhMuc;
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
public class DanhMucRepo1 {
     public List<DanhMuc> getAllDanhMuc() {
        List<DanhMuc> listkd = new ArrayList<>();
        String sql = "select * from DANHMUC";
        try {
            Connection con = DbConText.getConnection();
            Statement stm = con.createStatement();
            ResultSet rs = stm.executeQuery(sql);
            while (rs.next()) {
                listkd.add(new DanhMuc(rs.getString(1), rs.getString(2)));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return listkd;
    }
    
}
