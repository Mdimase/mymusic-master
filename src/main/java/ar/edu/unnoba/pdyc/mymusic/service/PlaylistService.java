package ar.edu.unnoba.pdyc.mymusic.service;

import ar.edu.unnoba.pdyc.mymusic.dto.PlaylistDTO;
import ar.edu.unnoba.pdyc.mymusic.model.Playlist;
import ar.edu.unnoba.pdyc.mymusic.model.Song;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface PlaylistService {
    void deletePlaylist(long id);
    Long getIdByPlaylistIdAndSongId(long plId, long sId);
    //metodo asincronico
    CompletableFuture<List<Playlist>> getPlaylistsAsync();
    CompletableFuture<Playlist> createAsync(PlaylistDTO playlistDTO, String loggedEmail);
    CompletableFuture<List<Song>> getSongsByPlaylistIdAsync(long id);
    CompletableFuture<Playlist> updateAsync(long id, PlaylistDTO playlistDTO, String loggedEmail);
    CompletableFuture<String> getNameByIdAsync(long id);
    CompletableFuture<Song> addSongAsync(long idPlaylist, Long idSong, String loggedEmail) throws Exception;
    CompletableFuture<Song> deleteSongAsync(long idPlaylist, Long idSong, String loggedEmail) throws Exception;
    CompletableFuture<Playlist> deletePlaylistAsync(long idPlaylist, String loggedEmail) throws Exception;
}
