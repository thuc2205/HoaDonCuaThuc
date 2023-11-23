package Entity;

import java.math.BigDecimal;


public class TichDien {
    private String id_tichdiem;
    private BigDecimal diem;

    public TichDien(String id_tichdiem, BigDecimal diem) {
        this.id_tichdiem = id_tichdiem;
        this.diem = diem;
    }

    public String getId_tichdiem() {
        return id_tichdiem;
    }

    public void setId_tichdiem(String id_tichdiem) {
        this.id_tichdiem = id_tichdiem;
    }

    public BigDecimal getDiem() {
        return diem;
    }

    public void setDiem(BigDecimal diem) {
        this.diem = diem;
    }
    
    
}
