package com.aluracursos.literalura.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;
@JsonIgnoreProperties(ignoreUnknown = true)
public record DatosLibro(
        @JsonAlias("title") String titulo,
        @JsonAlias("authors") List<DatosAutor> autores,
        @JsonAlias("languages") List<String> idioma,
        @JsonAlias("download_count") Integer numeroDeDescargas) {

    @Override
    public String toString() {
        String autor = (autores == null || autores.isEmpty()) ? "Autor desconocido" :
                autores.get(0).nombre();
        return  "****Libro****\n" +
                "Titulo: " + titulo + '\n' +
                "Autor: " + autor + '\n' +
                "Idioma: " + idioma.get(0) + '\n' +
                "Número de descargas: " + numeroDeDescargas + '\n' +
                "*************" + '\n';
    }

}
