package com.aluracursos.literalura.principal;

import com.aluracursos.literalura.model.*;
import com.aluracursos.literalura.repository.AutorRepository;
import com.aluracursos.literalura.repository.LibroRepository;
import com.aluracursos.literalura.service.ConsumoAPI;
import com.aluracursos.literalura.service.ConvierteDatos;
import org.springframework.stereotype.Component;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collector;
@Component
public class Principal {
    private Scanner teclado = new Scanner(System.in);
    private ConsumoAPI consumoAPI = new ConsumoAPI();
    private static final String URL_BASE = "https://gutendex.com/books/";
    private ConvierteDatos conversor = new ConvierteDatos();
    private List<DatosLibro> datosLibros = new ArrayList<>();
    private List<DatosAutor> datosAutores = new ArrayList<>();
    private final LibroRepository libroRepositorio;
    private final AutorRepository autorRepositorio;
    private List<Libro> libros;
    private List<Autor> autores;

    public Principal(LibroRepository repository, AutorRepository autorRepository) {
        this.libroRepositorio = repository;
        this.autorRepositorio = autorRepository;
    }


    public void muestraElMenu() {
        var opcion = -1;
        while (opcion != 0) {
            var menu = """
                    1 - Buscar libros

                    0 - Salir
                    """;
            System.out.println(menu);
            opcion = teclado.nextInt();
            teclado.nextLine();

            switch (opcion) {
                case 1:
                    buscarLibro();
                    break;
                case 0:
                    System.out.println("Cerrando la aplicación...");
                    break;
                default:
                    System.out.println("Elija una opción válida");
            }
        }
    }


    private void buscarLibro() {
        System.out.println("¿Qué libro deseas buscar?");
        var nombreLibro = teclado.nextLine();
        var json = consumoAPI.obtenerDatos(URL_BASE + "?search=" + nombreLibro.replace(" ", "+"));
        var datos = conversor.obtenerDatos(json, Datos.class);

        Optional<DatosLibro> libroBuscado = datos.resultados().stream()
                .filter(l -> l.titulo().toUpperCase().contains(nombreLibro.toUpperCase()))
                .findFirst();

        if (libroBuscado.isPresent()){
            DatosLibro libro = libroBuscado.get();
            Libro nuevoLibro = manejarLibroEncontrado(libro);
            System.out.println(nuevoLibro);
        } else {
            System.out.println("No se encontró el libro\n");
        }
    }

    private Libro manejarLibroEncontrado(DatosLibro libro) {
        Optional<Libro> libroExistente = libroRepositorio.findByTituloContainingIgnoreCase(libro.titulo());

        Libro nuevoLibro;
        if (libroExistente.isPresent()) {
            nuevoLibro = libroExistente.get();
            System.out.println("El libro ya se encuentra en la base de datos\n");
        } else{
            nuevoLibro = new Libro(libro);
            System.out.println(nuevoLibro);
            System.out.println("Libro añadido con éxito\n");
        }

        asignarAutorAlLibro(nuevoLibro, libro );
        return nuevoLibro;
    }

    private void asignarAutorAlLibro(Libro libro, DatosLibro datosLibro ) {
        if (datosLibro.autores() == null || datosLibro.autores().isEmpty()){
            System.out.println("Autor no asociado");
            return;
        }
        DatosAutor datosAutor = datosLibro.autores().get(0);
        if (datosAutor.nombre() == null || datosAutor.nombre().isEmpty()){
            System.out.println("Desconocido\n");
            return;
        }

        Autor autor = obtenerORegistrarAutor(datosAutor);
        libro.setAutor(autor);
        libroRepositorio.save(libro);

    }

    private Autor obtenerORegistrarAutor(DatosAutor datosAutor){
        String nombreAutor = datosAutor.nombre();
        Integer autorFechaNacimiento = datosAutor.anoDeNacimiento() == null ? null :
                datosAutor.anoDeNacimiento();
        Integer autorFechaDefuncion = datosAutor.anoDeMuerte() == null ? null :
                datosAutor.anoDeMuerte();

        Optional<Autor> autorExistente = autorRepositorio.findByNombre(nombreAutor);

        if (autorExistente.isPresent()){
            return autorExistente.get();
        } else {
            DatosAutor datosAutorCompleto = new DatosAutor(nombreAutor, autorFechaNacimiento, autorFechaDefuncion);
            Autor nuevoAutor = new Autor(datosAutorCompleto);
            autorRepositorio.save(nuevoAutor);
            return nuevoAutor;
        }
    }





}