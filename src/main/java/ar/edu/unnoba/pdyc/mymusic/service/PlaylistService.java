package ar.edu.unnoba.pdyc.mymusic.service;

import ar.edu.unnoba.pdyc.mymusic.dto.PlaylistDTO;
import ar.edu.unnoba.pdyc.mymusic.model.Playlist;
import ar.edu.unnoba.pdyc.mymusic.model.Song;
import org.springframework.scheduling.annotation.Async;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public interface PlaylistService {

    List<Playlist>getPlaylists();
    List<Song> getSongsByPlaylistId(long id);
    String getNameById(long id);
    long getOwner(long id);
    void deletePlaylist(long id);
    long getIdByPlaylistIdAndSongId(long plId, long sId);
    Playlist create(PlaylistDTO playlistDTO, String loggedEmail);
    Playlist update(long id, PlaylistDTO playlistDTO, String loggedEmail) throws Exception;
    Song addSong(long idPlaylist, Long idSong, String loggedEmail) throws Exception;
    Song deleteSong(long idPlaylist, long idSong, String loggedEmail) throws Exception;
    Playlist delete(long idPlaylist, String loggedEmail) throws Exception;
    //metodo asincronico
    CompletableFuture<List<Playlist>> getPlaylistsAsync();
    CompletableFuture<Playlist> createAsync(PlaylistDTO playlistDTO, String loggedEmail);
    CompletableFuture<List<Song>> getSongsByPlaylistIdAsync(long id);
    CompletableFuture<Playlist> updateAsync(long id, PlaylistDTO playlistDTO, String loggedEmail)throws Exception;
    CompletableFuture<String> getNameByIdAsync(long id);
    CompletableFuture<Song> addSongAsync(long idPlaylist, Long idSong, String loggedEmail) throws Exception;
    CompletableFuture<Song> deleteSongAsync(long idPlaylist, Long idSong, String loggedEmail) throws Exception;
    CompletableFuture<Playlist> deletePlaylistAsync(long idPlaylist, String loggedEmail) throws Exception;
}
