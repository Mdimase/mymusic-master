package ar.edu.unnoba.pdyc.mymusic.dto;

import ar.edu.unnoba.pdyc.mymusic.model.Song;

import java.util.List;

public class PlaylistWithSongsDTO {

    private String playlistName;
    private List<SongDTO> songs;

    public PlaylistWithSongsDTO() {

    }

    public List<SongDTO> getSongs() {
        return songs;
    }

    public PlaylistWithSongsDTO(String playlistName, List<SongDTO> songs) {
        this.playlistName = playlistName;
        this.songs = songs;
    }

    public void setSongs(List<SongDTO> songs) {
        this.songs = songs;
    }

    public String getPlaylistName() {
        return playlistName;
    }

    public void setPlaylistName(String playlistName) {
        this.playlistName = playlistName;
    }

}
