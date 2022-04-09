package ar.edu.unnoba.pdyc.mymusic.model;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "songs")
public class Song {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String author;

    @Enumerated(value = EnumType.STRING)
    private Genre genre;

    @OneToMany(mappedBy = "song")
    private List<PlaylistsSongs> playlistsSongs;


    /********************************************
     ejemplo de clase

    //@ManyToOne
    //@JoinColumn(name = "user_id")
    private User owner;

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    *********************************************/

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    public List<PlaylistsSongs> getPlaylistsSongs() {
        return playlistsSongs;
    }

    public void setPlaylistsSongs(List<PlaylistsSongs> playlistsSongs) {
        this.playlistsSongs = playlistsSongs;
    }
}
