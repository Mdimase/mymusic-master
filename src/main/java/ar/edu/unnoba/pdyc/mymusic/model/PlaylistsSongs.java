package ar.edu.unnoba.pdyc.mymusic.model;

import javax.persistence.*;

@Entity
@Table(name="playlists_songs", uniqueConstraints={@UniqueConstraint(columnNames={"playlist_id","song_id"})})
public class PlaylistsSongs {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="playlist_id")
    private Playlist playlist;

    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="song_id")
    private Song song;

    public Playlist getPlaylist() {
        return playlist;
    }

    public void setPlaylist(Playlist playlist) {
        this.playlist = playlist;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Song getSong() {
        return song;
    }

    public void setSong(Song song) {
        this.song = song;
    }
}
