package repobanhang;

import Entity.KhachHang;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import util.DbConText;

public class TichDienRepo {

    public Integer tichDiem(BigDecimal diem ,String maKH) {
        String sql = "UPDATE TICHDIEN\n"
                + "SET DIEM = ?\n"
                + "WHERE ID_TICHDIEM = (SELECT ID_TICHDIEM FROM KHACHHANG WHERE ID = ?); ";
        try(Connection con=DbConText.getConnection();PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setBigDecimal(1, diem);
            ps.setString(2, maKH);
           ps.executeUpdate();
            return 0;
        } catch (Exception e) {
        }
        return 1;
    }
    public BigDecimal selectDiem(String maKH) {
        String sql = "Select DIEM\n"
                + "From TICHDIEN\n"
                + "WHERE ID_TICHDIEM = (SELECT ID_TICHDIEM FROM KHACHHANG WHERE MA = ?); ";
        try(Connection con=DbConText.getConnection();PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, maKH);
           try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getBigDecimal("DIEM");
            }
           }
        } catch (Exception e) {
        }
        return BigDecimal.ZERO;
    }
}
