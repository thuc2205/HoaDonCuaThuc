/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entity;

/**
 *
 * @author Admin
 */
public class DanhMuc {
     private String iDDanhMuc,name;

    public DanhMuc() {
    }

    public DanhMuc(String iDDanhMuc, String name) {
        this.iDDanhMuc = iDDanhMuc;
        this.name = name;
    }

    public String getiDDanhMuc() {
        return iDDanhMuc;
    }

    public void setiDDanhMuc(String iDDanhMuc) {
        this.iDDanhMuc = iDDanhMuc;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name ;
    }
     
    
}
