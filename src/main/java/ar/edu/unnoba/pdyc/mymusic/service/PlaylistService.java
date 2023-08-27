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
    void deletePlaylist(Long id);
    Long getIdByPlaylistIdAndSongId(Long plId, Long sId);
    //metodo asincronico
    CompletableFuture<List<Playlist>> getPlaylistsAsync(User userLogged);
    CompletableFuture<Playlist> createAsync(PlaylistDTO playlistDTO, String loggedEmail);
    CompletableFuture<List<Song>> getSongsByPlaylistIdAsync(Long id)throws NotFoundException;
    CompletableFuture<Playlist> updateAsync(Long id, PlaylistUpdateDTO playlistUpdateDTO, User userLogged)throws NotFoundException,UnauthorizedException;
    CompletableFuture<String> getNameByIdAsync(Long id)throws NotFoundException;
    CompletableFuture<Song> addSongAsync(Long idPlaylist, Long idSong,User userLogged)throws NotFoundException,UnauthorizedException, DataIntegrityViolationException;
    CompletableFuture<Song> deleteSongAsync(Long idPlaylist, Long idSong, User userLogged)throws NotFoundException,UnauthorizedException;
    CompletableFuture<Playlist> deletePlaylistAsync(Long idPlaylist,User userLogged)throws NotFoundException,UnauthorizedException;
}
