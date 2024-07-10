package com.aluracursos.literAlura;

import com.aluracursos.literAlura.model.AuthorData;
import com.aluracursos.literAlura.model.BooksData;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.List;
import java.util.OptionalDouble;

@Entity

@Table(name = "books")

public class InformationBooks {

    @jakarta.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Column(unique = true)
    private String title;
    @JdbcTypeCode(SqlTypes.JSON)
    private List<AuthorData> author;
    @JdbcTypeCode(SqlTypes.JSON)
    private List<String> languages;
    private Double downloadNumber;

    public InformationBooks() {
    }

    public InformationBooks(BooksData booksData) {
        this.title = booksData.title();
        this.author = booksData.author();
        this.languages = booksData.languages();
        if(booksData.downloadNumber() == null){
            this.downloadNumber = 0.0;
        }else{
            this.downloadNumber = OptionalDouble.of(booksData.downloadNumber()).orElse(0);
        }

        //this.genero = Categoria.fromString(datosSerie.genero().split(",")[0].trim());
        //this.sinopsis = ConsultaChatGPT.obtenerTraduccion(datosSerie.sinopsis()) ;
    }

    @Override
    public String toString() {
        return
                "Id=" + Id +
                        ", title='" + title + '\'' +
                        ", author=" + author +
                        ", languages=" + languages +
                        ", downloadNumber=" + downloadNumber;
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<AuthorData> getAuthor() {
        return author;
    }

    public void setAuthor(List<AuthorData> author) {
        this.author = author;
    }

    public List<String> getLanguages() {
        return languages;
    }

    public void setLanguages(List<String> languages) {
        this.languages = languages;
    }

    public Double getDownloadNumber() {
        return downloadNumber;
    }

    public void setDownloadNumber(Double downloadNumber) {
        this.downloadNumber = downloadNumber;
    }
}