package com.aluracursos.literAlura;

import com.aluracursos.literAlura.model.BooksData;
import com.aluracursos.literAlura.model.Data;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Principal {

    private static final String URL_BASE ="https://gutendex.com/books/";

    private HttpClient newClient = new HttpClient();

    private DataConversion conversor = new DataConversion();

    private Scanner input = new Scanner(System.in);

    public List<InformationBooks> controlBooksList = new ArrayList<>();

    public BookRepository repository;

    public Principal(){}

    public Principal(BookRepository repository) {

        this.repository = repository;
    }

    Scanner sc = new Scanner(System.in);

    public void displayMenu(){

        var option = -1;
        while (option != 0) {

            System.out.println("\n Books search recently \n");
            controlBooksList.stream()
                    .forEach(System.out::println);
            var menu = """
                    \n Menu\n
                    1 - Search Book 
                    2 - Insert Book
                    3 - Books in DB
                    4 - Search Author
                    5 - Book authors
                    6 - Best Sellers
                 
                                 
                    0 - Exit
                    """;
            System.out.println(menu);
            option = sc.nextInt();
            sc.nextLine();

            switch (option) {
                case 1:
                    try {
                        searchBook();
                    }catch (Exception e){
                        System.out.println("Error");}

                    break;
                case 2:
                    try {
                    insertQueryToDB();
                    }catch (Exception e){
                        System.out.println("Error book exists in DB");}

                    break;
                case 3:
                    try {
                    displayDB();
                    }catch (Exception e){
                     System.out.println("Error");}

                case 4:
                    try{
                        searchAuthor();
                    }catch (Exception e){
                        System.out.println("Error");}

                case 5:
                    try{
                        BooksAuthors();
                    }catch (Exception e){
                        System.out.println("Invalid option, please try again");}

                case 6:
                    try {
                        displayBestSellers();
                    }catch (Exception e){
                        System.out.println("Error");}


        break;
                case 0:
                    System.out.println("Closing the APP...");
                    break;
                default:
                    System.out.println("Invalid option");
            }
        }

    }

    private void BooksAuthors() {

        var json = newClient.clientConnection(URL_BASE);

        var data = conversor.dataQuery(json, Data.class);

        System.out.println("List of Authors");

       data.results().stream()
               .forEach(a -> a.author().forEach(e -> System.out.println(e.name())));

        System.out.println("Enter a year to know which authors are alive: ");

        int yearDecision = Integer.parseInt(sc.nextLine());

        AtomicInteger aux = new AtomicInteger();

        var ref = new Object() {
            int count = 0;
        };

        System.out.println("\nAlive Authors after, year: " + yearDecision);
        data.results().stream()
                .forEach(a -> a.author().forEach(e -> {
                            aux.set(Integer.parseInt(e.deathDate()));
                            if(aux.get() > yearDecision){
                                System.out.println(e.name());
                                ref.count = ref.count +1;
                            }
                }));

        if (ref.count == 0) {
            System.out.println("All the Authors are dead");
        }
    }

    private void searchAuthor() {

        System.out.println("Insert the Author that you want to search ");

        var authorName = input.nextLine();

        var json  = newClient.clientConnection(URL_BASE + "?search=" + authorName.replace(" ", "+"));

        var dataSerch = conversor.dataQuery(json, Data.class);

        Optional<BooksData> serchBook = dataSerch.results().stream()
                .filter(l -> l.title().toUpperCase().contains(authorName.toUpperCase()))
                .findFirst();

        if (serchBook.isPresent()) {
            System.out.println("Author found ");
            //System.out.println(serchBook.get());
            System.out.println("Books of the Author:");
            dataSerch.results().stream()
                    .forEach(t -> System.out.println(t.title()));
        }else {
            System.out.println("Not found Author");
        }

        }

    private void searchBook() {

        // Busqueda de libros por nombre

        System.out.println("Insert the books name that you want to search ");

        var bookName = input.nextLine();

        var json  = newClient.clientConnection(URL_BASE + "?search=" + bookName.replace(" ", "+"));

        var dataSerch = conversor.dataQuery(json, Data.class);

        Optional<BooksData> serchBook = dataSerch.results().stream()
                .filter(l -> l.title().toUpperCase().contains(bookName.toUpperCase()))
                .findFirst();

        if (serchBook.isPresent()) {
            System.out.println("Book found ");
            System.out.println(serchBook.get());
            InformationBooks informationBooks = new InformationBooks(serchBook.get());

            boolean bol = false;


            if (controlBooksList.size()>0){

                for (InformationBooks books : controlBooksList) {

                    if (books.getTitle().equals(informationBooks.getTitle())) {
                        bol = true;
                    }

                }
                if(bol==false){
                    controlBooksList.add(informationBooks);
                }
            }else{
                controlBooksList.add(informationBooks);
            }


        } else {
            System.out.println("Not found book");
        }
    }

    private void displayBestSellers() {


        var json = newClient.clientConnection(URL_BASE);

        //System.out.println("json: " + json);

        var data = conversor.dataQuery(json, Data.class);

        //System.out.println("data: " + data);


        // TOP 10 libros mas descargados

        System.out.println("Top 10 libros mas descargados");

        data.results().stream()
                .sorted(Comparator.comparing(BooksData::downloadNumber).reversed())
                .limit(10)
                .map(l -> l.title().toUpperCase())
                .forEach(System.out::println);
    }

    private void displayDB() {

        System.out.println("Books in DB:");

        repository.findAll().stream()
                .forEach(System.out::println);
    }

    public void insertQueryToDB() {

        // Busqueda de libros por nombre

        System.out.println("Insert the name of the book that you want to add to DB ");

        var bookName = input.nextLine();

        var json  = newClient.clientConnection(URL_BASE + "?search=" + bookName.replace(" ", "+"));

        var dataSerch = conversor.dataQuery(json, Data.class);


        Optional<BooksData> serchBook = dataSerch.results().stream()
                .filter(l -> l.title().toUpperCase().contains(bookName.toUpperCase()))
                .findFirst();

        if (serchBook.isPresent()) {
            System.out.println("Book found ");

            InformationBooks informationBooks = new InformationBooks(serchBook.get());

            System.out.println(serchBook.get());
            repository.save(informationBooks);

        } else {
            System.out.println("Not found book");
        }


    }



    }



