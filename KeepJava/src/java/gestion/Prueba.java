/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gestion;

import hibernate.Keep;

/**
 *
 * @author 2dam
 */
public class Prueba {
    public static void main(String[] args) {
        Keep k = new Keep(null, Integer.MIN_VALUE, "Mensaje", "ruta", "estable");
        GestorKeep.addKeep(k, "pepe");
    }
}
