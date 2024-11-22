package com.aluracursos.literalura.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

public record DatosAutor(
        @JsonAlias("name") String nombre,
        @JsonAlias("birth_year") Integer anoDeNacimiento,
        @JsonAlias("death_year") Integer anoDeMuerte) {

}
