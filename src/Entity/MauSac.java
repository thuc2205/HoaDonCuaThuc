/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entity;

/**
 *
 * @author Admin
 */
public class MauSac {
     private String iDMauSac,name;

    public MauSac() {
    }

    public MauSac(String iDMauSac, String name) {
        this.iDMauSac = iDMauSac;
        this.name = name;
    }

    public String getiDMauSac() {
        return iDMauSac;
    }

    public void setiDMauSac(String iDMauSac) {
        this.iDMauSac = iDMauSac;
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
