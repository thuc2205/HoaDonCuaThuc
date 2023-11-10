/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entity;

import java.math.BigDecimal;

/**
 *
 * @author Admin
 */
public class GiayChiTiet {
    private String iD;
    private Giay giay;
    private Hang hang;
    private KieuDang kieuDang;
    private DanhMuc danhMuc;
    private MauSac mauSac;
    private KichCo kichCo;
    private String hinhAnh;
    private BigDecimal gia;
    private int soLuong;
    private String trangThai,moTa;

    public String getiD() {
        return iD;
    }

    public void setiD(String iD) {
        this.iD = iD;
    }

    public Giay getGiay() {
        return giay;
    }

    public void setGiay(Giay giay) {
        this.giay = giay;
    }

    public Hang getHang() {
        return hang;
    }

    public void setHang(Hang hang) {
        this.hang = hang;
    }

    public KieuDang getKieuDang() {
        return kieuDang;
    }

    public void setKieuDang(KieuDang kieuDang) {
        this.kieuDang = kieuDang;
    }

    public DanhMuc getDanhMuc() {
        return danhMuc;
    }

    public void setDanhMuc(DanhMuc danhMuc) {
        this.danhMuc = danhMuc;
    }

    public MauSac getMauSac() {
        return mauSac;
    }

    public void setMauSac(MauSac mauSac) {
        this.mauSac = mauSac;
    }

    public KichCo getKichCo() {
        return kichCo;
    }

    public void setKichCo(KichCo kichCo) {
        this.kichCo = kichCo;
    }

    public BigDecimal getGia() {
        return gia;
    }

    public void setGia(BigDecimal gia) {
        this.gia = gia;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public GiayChiTiet() {
    }

    public GiayChiTiet(String iD, Giay giay, Hang hang, KieuDang kieuDang, DanhMuc danhMuc, MauSac mauSac, KichCo kichCo, String hinhAnh, BigDecimal gia, int soLuong, String trangThai, String moTa) {
        this.iD = iD;
        this.giay = giay;
        this.hang = hang;
        this.kieuDang = kieuDang;
        this.danhMuc = danhMuc;
        this.mauSac = mauSac;
        this.kichCo = kichCo;
        this.hinhAnh = hinhAnh;
        this.gia = gia;
        this.soLuong = soLuong;
        this.trangThai = trangThai;
        this.moTa = moTa;
    }

    public String getHinhAnh() {
        return hinhAnh;
    }

    public void setHinhAnh(String hinhAnh) {
        this.hinhAnh = hinhAnh;
    }

  
    
    
}
