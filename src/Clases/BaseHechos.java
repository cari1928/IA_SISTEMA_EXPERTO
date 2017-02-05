/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Clases;

import java.util.ArrayList;

/**
 *
 * @author Decker
 */
public class BaseHechos {
     ArrayList<String> base_hechos;
    String meta;

    public BaseHechos() {
    }

    public BaseHechos(ArrayList<String> base_hechos, String meta) {
        this.base_hechos = base_hechos;
        this.meta = meta;
    }

    public ArrayList<String> getBase_hechos() {
        return base_hechos;
    }

    public void setBase_hechos(ArrayList<String> base_hechos) {
        this.base_hechos = base_hechos;
    }

    public String getMeta() {
        return meta;
    }

    public void setMeta(String meta) {
        this.meta = meta;
    }
}
