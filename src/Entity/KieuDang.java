/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entity;

/**
 *
 * @author Admin
 */
public class KieuDang {
     private String iDKieuGiang,name;

    public KieuDang() {
    }

    public KieuDang(String iDKieuGiang, String name) {
        this.iDKieuGiang = iDKieuGiang;
        this.name = name;
    }
    

    public String getiDKieuGiang() {
        return iDKieuGiang;
    }

    public void setiDKieuGiang(String iDKieuGiang) {
        this.iDKieuGiang = iDKieuGiang;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return  name ;
    }
    
    
}
