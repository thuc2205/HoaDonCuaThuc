package repobanhang;

import Entity.Hang;
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
public class HangRepo {
     public List<Hang> getAllHang() {
        List<Hang> listkd = new ArrayList<>();
        String sql = "select * from HANG";
        try {
            Connection con = DbConText.getConnection();
            Statement stm = con.createStatement();
            ResultSet rs = stm.executeQuery(sql);
            while (rs.next()) {
                listkd.add(new Hang(rs.getString(1), rs.getString(2)));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return listkd;
    }
    
}
