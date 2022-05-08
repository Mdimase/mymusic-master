package ar.edu.unnoba.pdyc.mymusic.dto;

import ar.edu.unnoba.pdyc.mymusic.model.Genre;

public class SongDTO {
    private String id;
    private String name;
    private String author;
    private Genre genre;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }
}