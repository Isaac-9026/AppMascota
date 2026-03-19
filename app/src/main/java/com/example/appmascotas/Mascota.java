package com.example.appmascotas;

public class Mascota {
    private int id;
    private String tipo;
    private String nombre;
    private String color;
    private double pesokg;

    public Mascota(int id, String tipo, String nombre, String color, double pesokg) {
        this.id = id;
        this.tipo = tipo;
        this.nombre = nombre;
        this.color  = color;
        this.pesokg = pesokg;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public double getPesokg() {
        return pesokg;
    }

    public void setPesokg(double pesokg) {
        this.pesokg = pesokg;
    }
}
