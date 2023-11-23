package Entity;

public class KhachHang {

    private String id, ma,name;
    private Boolean gioiTinh;
    private String sdt;
    private String diaChi;
    private String idTichDiem;

    public KhachHang() {
    }

    public KhachHang(String id, String ma, String name, Boolean gioiTinh, String sdt, String diaChi, String idTichDiem) {
        this.id = id;
        this.ma = ma;
        this.name = name;
        this.gioiTinh = gioiTinh;
        this.sdt = sdt;
        this.diaChi = diaChi;
        this.idTichDiem = idTichDiem;
    }

    public String getIdTichDiem() {
        return idTichDiem;
    }

    public void setIdTichDiem(String idTichDiem) {
        this.idTichDiem = idTichDiem;
    }

    
    

    

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getGioiTinh() {
        return gioiTinh;
    }

    public void setGioiTinh(Boolean gioiTinh) {
        this.gioiTinh = gioiTinh;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    public String getMa() {
        return ma;
    }

    public void setMa(String ma) {
        this.ma = ma;
    }
     
    

}
