package com.aluracursos.literalura.principal;

import com.aluracursos.literalura.model.*;
import com.aluracursos.literalura.repository.AutorRepository;
import com.aluracursos.literalura.repository.LibroRepository;
import com.aluracursos.literalura.service.ConsumoAPI;
import com.aluracursos.literalura.service.ConvierteDatos;
import org.springframework.stereotype.Component;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Component
public class Principal {
    private Scanner teclado = new Scanner(System.in);
    private ConsumoAPI consumoAPI = new ConsumoAPI();
    private static final String URL_BASE = "https://gutendex.com/books/";
    private ConvierteDatos conversor = new ConvierteDatos();
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
                    Elija una opción:
                    1 - Buscar libros
                    2 - Lista de libros guardados
                    3 - Lista de autores guardados
                    4 - Lista de autores por año
                    5 - Lista de libros por idioma

                    0 - Salir
                    """;
            System.out.println(menu);
            try {
                opcion = teclado.nextInt();
                teclado.nextLine();

                switch (opcion) {
                    case 1:
                        buscarLibro();
                        break;
                    case 2:
                        listaDeLibros();
                        break;
                    case 3:
                        listaDeAutores();
                        break;
                    case 4:
                        listaDeAutoresPorAno();
                        break;
                    case 5:
                        listaDeLibrosPorIdioma();
                        break;
                    case 0:
                        System.out.println("Cerrando la aplicación...");
                        break;
                    default:
                        System.out.println("Elija una opción válida\n");
                        break;
                }
            }catch (InputMismatchException e) {
                System.out.println("Entrada no válida, por favor ingrese un número\n");
                teclado.nextLine();
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
            System.out.println("Autor no asociado\n");
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

    private void listaDeLibros() {
        libros = libroRepositorio.findAll();

        libros.stream()
                .sorted(Comparator.comparing(Libro::getTitulo))
                .forEach(System.out::println);
    }

    private void listaDeAutores() {
        autores = autorRepositorio.findAll();

        autores.stream()
                .sorted(Comparator.comparing(Autor::getNombre))
                .forEach(autor -> System.out.println(autor.getDatosAutorCompletos()));
    }

    private void listaDeAutoresPorAno() {
        System.out.println("¿Qué año desea consultar?");
        var autorVivo = teclado.nextInt();
        teclado.nextLine();
        List<Autor> filtroAutores = autorRepositorio.listaDeAutoresPorAno(autorVivo);
        System.out.println("*******Autores vivos en el año: " + autorVivo + "*******\n");
        filtroAutores.forEach(autor ->
                System.out.println(autor.getDatosAutorCompletos()));

    }

    private void listaDeLibrosPorIdioma() {
        System.out.println("""
                Elija entre los siguientes idiomas:
                es - español
                en - inglés
                """);
        var eleccionIdioma = teclado.nextLine().trim().toLowerCase();

        if (!eleccionIdioma.equals("es") && !eleccionIdioma.equals("en")){
            System.out.println("Idioma no válido. Intente nuevamente");
            return;
        }
        libros = libroRepositorio.findAll();

        List<Libro> librosFiltrados = libros.stream()
                .filter(l -> l.getIdioma() != null && l.getIdioma().contains(eleccionIdioma))
                .collect(Collectors.toList());
        if (librosFiltrados.isEmpty()){
            System.out.println("No se encontraron libros en el idioma buscado");
        } else {
            librosFiltrados.forEach(System.out::println);
        }

//        List<Libro> librosPorIdioma = libroRepositorio.listaDeLibrosPorIdioma(eleccionIdioma);
//        if (librosPorIdioma.isEmpty()) {
//            System.out.println("No se econtraron libros en el idioma seleccionado");
//            return;
//        }
//        System.out.println("********Libros por idioma********");
//        librosPorIdioma.forEach(System.out::println);

    }
}