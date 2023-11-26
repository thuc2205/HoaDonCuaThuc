package repobanhang;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import util.DbConText;

public class TichDienRepo {

    public Integer tichDiem(BigDecimal diem ,String idKH) {
        String sql = "UPDATE TICHDIEN\n"
                + "SET DIEM = ?\n"
                + "WHERE ID_TICHDIEM = (SELECT ID_TICHDIEM FROM KHACHHANG WHERE ID = ?); ";
        try(Connection con=DbConText.getConnection();PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setBigDecimal(1, diem);
            ps.setString(2, idKH);
            return ps.executeUpdate();
        } catch (Exception e) {
        }
        return 1;
    }
}
