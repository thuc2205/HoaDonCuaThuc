package Repository;

import Entity.HoaDon;
import Entity.KhachHang;
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
        String sql = "SELECT "
                + "    HOADON.ID AS HOADON_ID, HOADON.MA AS HOADON_MA, HOADON.NGAYTAO AS HOADON_NGAYTAO,"
                + "    HOADON.TEN_NGUOINHAN AS HOADON_TEN_NGUOINHAN, HOADON.SDT AS HOADON_SDT,"
                + "    HOADON.DIACHI AS HOADON_DIACHI, HOADON.PHISHIP AS HOADON_PHISHIP,"
                + "    HOADON.TONGTIEN AS HOADON_TONGTIEN, HOADON.TRANGTHAI AS HOADON_TRANGTHAI,"
                + "    HOADON.TIENKHACHDUA AS HOADON_TIENKHACHDUA, HOADON.TIENTHUA AS HOADON_TIENTHUA, HOADON.HINHTHUCTHANHTOAN AS HOADON_HINHTHUCTHANHTOAN,"
                + "    NHANVIEN.ID AS NHANVIEN_ID, NHANVIEN.MA AS NHANVIEN_MA, NHANVIEN.TEN AS NHANVIEN_TEN,"
                + "    NHANVIEN.GIOITINH AS NHANVIEN_GIOITINH, NHANVIEN.SDT AS NHANVIEN_SDT,"
                + "    NHANVIEN.DIACHI AS NHANVIEN_DIACHI, NHANVIEN.NGAYSINH AS NHANVIEN_NGAYSINH,"
                + "    NHANVIEN.MATKHAU AS NHANVIEN_MATKHAU, NHANVIEN.VAITRO AS NHANVIEN_VAITRO,"
                + "    NHANVIEN.TRANGTHAI AS NHANVIEN_TRANGTHAI,"
                + "    KHACHHANG.ID AS KHACHHANG_ID, KHACHHANG.MA AS KHACHHANG_MA,"
                + "    KHACHHANG.TEN AS KHACHHANG_TEN, KHACHHANG.GIOITINH AS KHACHHANG_GIOITINH,"
                + "    KHACHHANG.SDT AS KHACHHANG_SDT, KHACHHANG.DIACHI AS KHACHHANG_DIACHI,KHACHHANG.ID_TICHDIEM AS ID_TICHDIEM"
                + " FROM HOADON"
                + " LEFT JOIN NHANVIEN ON HOADON.ID_NHANVIEN = NHANVIEN.ID"
                + " LEFT JOIN KHACHHANG ON HOADON.ID_KHACHHANG = KHACHHANG.ID"
                + " ORDER BY HOADON_MA DESC"; // Sắp xếp theo HOADON_ID tăng dần (ASC)
        try (Connection con = DbConText.getConnection(); Statement stm = con.createStatement();) {
            
            ResultSet rs = stm.executeQuery(sql);
            while (rs.next()) {
                NhanVien n = new NhanVien(rs.getString("NHANVIEN_ID"), rs.getString("NHANVIEN_MA"), rs.getString("NHANVIEN_TEN"), rs.getBoolean("NHANVIEN_GIOITINH"),
                        rs.getString("NHANVIEN_SDT"), rs.getString("NHANVIEN_DIACHI"),
                        rs.getDate("NHANVIEN_NGAYSINH"), rs.getString("NHANVIEN_MATKHAU"), rs.getString("NHANVIEN_VAITRO"),
                        rs.getString("NHANVIEN_TRANGTHAI"));
                KhachHang k = new KhachHang(rs.getString("KHACHHANG_ID"), rs.getString("KHACHHANG_MA"), rs.getString("KHACHHANG_TEN"), rs.getBoolean("KHACHHANG_GIOITINH"),
                        rs.getString("KHACHHANG_SDT"), rs.getString("KHACHHANG_DIACHI"), rs.getString("ID_TICHDIEM"));
                listkd.add(new HoaDon(rs.getString("HOADON_ID"), rs.getString("HOADON_MA"), n, k, rs.getDate("HOADON_NGAYTAO"),
                        rs.getString("HOADON_TEN_NGUOINHAN"), rs.getString("HOADON_SDT"), rs.getString("HOADON_DIACHI"),
                        rs.getBigDecimal("HOADON_PHISHIP"), rs.getBigDecimal("HOADON_TONGTIEN"), rs.getString("HOADON_TRANGTHAI"), rs.getBigDecimal("HOADON_TIENKHACHDUA"),
                        rs.getBigDecimal("HOADON_TIENTHUA"), rs.getString("HOADON_HINHTHUCTHANHTOAN")));
                
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
        
        String sql = "SELECT TOP 1 * FROM HOADON ORDER BY MA DESC";
        
        try (Connection con = DbConText.getConnection(); Statement statement = con.createStatement(); ResultSet resultSet = statement.executeQuery(sql)) {
            
            if (resultSet.next()) {
                resultSet.getString("ID");
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("LỖI SELECTID HOA DON");
            
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

    public Integer updateHDByMa(String tt, BigDecimal tienKH, BigDecimal tienThua, String hinhThuc, String maHD, String KH) {
        String sql = "update HOADON set TRANGTHAI = ?,TIENKHACHDUA=?,TIENTHUA=?,HINHTHUCTHANHTOAN=?,ID_KHACHHANG=?  where MA=?";
        try (Connection con = DbConText.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setObject(1, tt);
            ps.setObject(2, tienKH);
            ps.setObject(3, tienThua);
            ps.setObject(4, hinhThuc);
            ps.setObject(5, KH);
            ps.setObject(6, maHD);
            ps.executeUpdate();
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Lỗi UPDATE HÓA ĐƠN "+e.getMessage());
        }
        return 1;
    }
    
    public Integer selectIdSanPhamTrongGioHang(String idGiay, String idHoaDon) {
        String sql = "SELECT COUNT(*) AS SoLuong FROM HOADONCHITIET WHERE ID_GIAYCT = ? AND ID_HOADON = ?";
        
        try (Connection con = DbConText.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, idGiay);
            ps.setString(2, idHoaDon);
            
            ResultSet resultSet = ps.executeQuery();
            
            if (resultSet.next()) {
                int soLuong = resultSet.getInt("SoLuong");
                return soLuong;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            
        }
        return 0; // Trả về giá trị mặc định nếu không có kết quả hoặc có lỗi
    }
    
    public String updateTrangThaiHoaDon(String trangThai, BigDecimal tongTien, String idHoaDon) {
        String sql = "UPDATE HOADON SET TRANGTHAI = ?, TONGTIEN = ? WHERE ID = ?";
        try (Connection con = DbConText.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, trangThai);
            ps.setBigDecimal(2, tongTien);
            ps.setString(3, idHoaDon);
            
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                con.commit(); // Commit the changes if successful
                return "Update successful";
            } else {
                return "No records updated";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error: " + e.getMessage());
            // You might also want to roll back the transaction here in case of an error.
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("General error: " + e.getMessage());
        }
        return null;
    }
    
    public List<String> selectAllTrangThaiHoaDon() {
        List<String> trangThaiList = new ArrayList<>();
        String sql = "SELECT TRANGTHAI FROM HOADON";
        try (Connection con = DbConText.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                trangThaiList.add(rs.getString("TRANGTHAI"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Lỗi khi chọn Trạng Thái");
        }
        return trangThaiList;
    }
    
}
