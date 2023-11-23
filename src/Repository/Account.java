package Repository;

import Entity.NhanVien;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import util.DbConText;

public class Account {

    public NhanVien checkLogin(String ma, String matkhau, String vaitro) {
        String sql = "SELECT * FROM NHANVIEN WHERE ma = ? AND matkhau = ? and vaitro = ?";

        try (Connection con = DbConText.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, ma);
            ps.setString(2, matkhau);
            ps.setString(3, vaitro);

            try (ResultSet rs = ps.executeQuery()) {
                 if (rs.next()) {
                return new NhanVien(
                    rs.getString("id"),
                    rs.getString("ma"),
                    rs.getString("ten"),
                    rs.getBoolean("gioitinh"),
                    rs.getString("sdt"),
                    rs.getString("diachi"),
                    rs.getDate("ngaysinh"),
                    rs.getString("matkhau"),
                    rs.getString("vaitro"),
                    rs.getString("trangthai")
                );
            }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Integer InsertNV(NhanVien nv) {
        String sql = "Insert into NhanVien (id,ma,ten,gioitinh,sdt,diachi,ngaysinh,matkhau,vaitro,trangthai) values (newid(),dbo.AUTO_MaNV(),?,?,?,?,?,?,?,?)";
        try (Connection con = DbConText.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nv.getName());
            ps.setBoolean(2, nv.getGioiTinh());
            ps.setString(3, nv.getSdt());
            ps.setString(4, nv.getDiaChi());
            ps.setDate(5, new java.sql.Date(nv.getNgaySinh().getTime()));
            ps.setString(6, nv.getMatKhau());
            ps.setString(7, nv.getVaiTro());
            ps.setString(8, nv.getTrangThai());
            return ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 1;
    }

    public ArrayList<NhanVien> getNV() {
        ArrayList<NhanVien> list = new ArrayList<>();
        String sql = "Select * from NhanVien";

        try {
            ResultSet rs = JDBCHelper.executeQuery(sql);
            while (rs.next()) {
                list.add(new NhanVien(
                         rs.getString("id"),
                    rs.getString("ma"),
                    rs.getString("ten"),
                    rs.getBoolean("gioitinh"),
                    rs.getString("sdt"),
                    rs.getString("diachi"),
                    rs.getDate("ngaysinh"),
                    rs.getString("matkhau"),
                    rs.getString("vaitro"),
                    rs.getString("trangthai")
                ));
            }
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
