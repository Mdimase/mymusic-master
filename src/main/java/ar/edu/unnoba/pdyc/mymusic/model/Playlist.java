package ar.edu.unnoba.pdyc.mymusic.model;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="playlists")
public class Playlist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne(fetch=FetchType.EAGER)
    private User user;

    @OneToMany(mappedBy = "playlist",fetch=FetchType.EAGER) //cascade = CascadeType.REMOVE, orphanRemoval = true
    private List<PlaylistsSongs> playlistsSongs;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<PlaylistsSongs> getPlaylistsSongs() {
        return playlistsSongs;
    }

    public void setPlaylistsSongs(List<PlaylistsSongs> playlistsSongs) {
        this.playlistsSongs = playlistsSongs;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object obj){
        if(obj == null){
            return false;
        } else //mismos id en BD -> mismo objeto
            if (!(obj instanceof Playlist)){
            return false;
        } else return ((Playlist) obj).id.equals(this.id);
    }
}
