package com.aluracursos.literalura.repository;

import com.aluracursos.literalura.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AutorRepository extends JpaRepository<Autor, Long> {
    boolean existsById(Long author_id);
    Optional<Autor> findByNombre(String autorNombre);
    @Query("SELECT a FROM Autor a WHERE :autorVivo BETWEEN a.anoDeNacimiento AND a.anoDeMuerte")
    List<Autor> listaDeAutoresPorAno(@Param("autorVivo") int autorVivo);
}
