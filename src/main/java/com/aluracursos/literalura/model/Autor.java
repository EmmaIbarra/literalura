package com.aluracursos.literalura.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "autores")
public class Autor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    @Column(nullable = false)
    private String nombre;
    private Integer anoDeNacimiento;
    private Integer anoDeMuerte;
    @OneToMany(mappedBy = "autor", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Libro> libros = new ArrayList<>();

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

    public List<Libro> getLibros() {
        return libros;
    }

    public void setLibros(List<Libro> libros) {
        this.libros = libros;
        if (libros != null) {
            libros.forEach(libro -> libro.setAutor(this));
        }
    }

    @Override
    public String toString() {
        return nombre;
    }

    public String getDatosAutorCompletos(){
        return "****Autor****\n" +
                "Nombre: " + nombre + '\n' +
                "Año de nacimiento: " + (anoDeNacimiento == null ? "Fecha desconocida" : anoDeNacimiento) + '\n' +
                "Año de muerte: " + (anoDeMuerte == null ? "Fecha desconocida" : anoDeMuerte) + '\n' +
                "************\n";
    }
}
