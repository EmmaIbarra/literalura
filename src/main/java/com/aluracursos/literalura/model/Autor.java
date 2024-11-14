package com.aluracursos.literalura.model;

import jakarta.persistence.*;

@Entity
@Table(name = "autores")
public class Autor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    private String nombre;
    private Integer anoDeNacimiento;
    private Integer anoDeMuerte;
    @ManyToOne
    private Libro libro;

    public Autor(){}

    public Autor(DatosAutor d) {
        this.nombre = d.nombre();
        this.anoDeNacimiento = d.anoDeNacimiento();
        this.anoDeMuerte = d.anoDeMuerte();
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getAnoDeNacimiento() {
        return anoDeNacimiento;
    }

    public void setAnoDeNacimiento(Integer anoDeNacimiento) {
        this.anoDeNacimiento = anoDeNacimiento;
    }

    public Integer getAnoDeMuerte() {
        return anoDeMuerte;
    }

    public void setAnoDeMuerte(Integer anoDeMuerte) {
        this.anoDeMuerte = anoDeMuerte;
    }

    public Libro getLibro() {
        return libro;
    }

    public void setLibro(Libro libro) {
        this.libro = libro;
    }

    @Override
    public String toString() {
        return "****Autor****" +
                "Nombre: " + nombre + '\'' +
                "Año de nacimiento: " + anoDeNacimiento + '\'' +
                "Año de muerte: " + anoDeMuerte + '\'' +
                "Libros: " + libro + '\'' +
                "************";
    }
}
