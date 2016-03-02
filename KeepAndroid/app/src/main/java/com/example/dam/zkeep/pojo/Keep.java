package com.example.dam.zkeep.pojo;

public class Keep {
    private int id;
    private String contenido;
    private boolean estado;

    public Keep() {
    }

    public Keep(int id, String contenido, boolean estado) {
        this.id = id;
        this.contenido = contenido;
        this.estado = estado;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "{id=" + id + ", contenido='" + contenido + ", estado=" + estado +'}';
    }
}
