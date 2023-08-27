package ar.edu.unnoba.pdyc.mymusic.service;

import ar.edu.unnoba.pdyc.mymusic.dto.PlaylistDTO;
import ar.edu.unnoba.pdyc.mymusic.dto.PlaylistUpdateDTO;
import ar.edu.unnoba.pdyc.mymusic.exception.NotFoundException;
import ar.edu.unnoba.pdyc.mymusic.exception.UnauthorizedException;
import ar.edu.unnoba.pdyc.mymusic.model.Playlist;
import ar.edu.unnoba.pdyc.mymusic.model.Song;
import ar.edu.unnoba.pdyc.mymusic.model.User;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface PlaylistService {
    User getOwner(Playlist playlist)throws NotFoundException;
    boolean isOwner(Playlist playlist, User userLogged);
    void deletePlaylist(long id);
    Long getIdByPlaylistIdAndSongId(long plId, long sId);
    //metodo asincronico
    CompletableFuture<List<Playlist>> getPlaylistsAsync(User userLogged);
    CompletableFuture<Playlist> createAsync(PlaylistDTO playlistDTO, String loggedEmail);
    CompletableFuture<List<Song>> getSongsByPlaylistIdAsync(long id)throws NotFoundException;
    CompletableFuture<Playlist> updateAsync(long id, PlaylistUpdateDTO playlistUpdateDTO, User userLogged)throws NotFoundException,UnauthorizedException;
    CompletableFuture<String> getNameByIdAsync(long id)throws NotFoundException;
    CompletableFuture<Song> addSongAsync(long idPlaylist, long idSong,User userLogged)throws NotFoundException,UnauthorizedException, DataIntegrityViolationException;
    CompletableFuture<Song> deleteSongAsync(long idPlaylist, long idSong, User userLogged)throws NotFoundException,UnauthorizedException;
    CompletableFuture<Playlist> deletePlaylistAsync(long idPlaylist,User userLogged)throws NotFoundException,UnauthorizedException;
}
