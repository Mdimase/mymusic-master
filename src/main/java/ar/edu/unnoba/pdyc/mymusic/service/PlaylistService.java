package ar.edu.unnoba.pdyc.mymusic.service;

import ar.edu.unnoba.pdyc.mymusic.dto.PlaylistDTO;
import ar.edu.unnoba.pdyc.mymusic.model.Playlist;
import ar.edu.unnoba.pdyc.mymusic.model.Song;
import org.springframework.scheduling.annotation.Async;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public interface PlaylistService {
    public List<Playlist>getPlaylists();
    public List<Song> getSongsByPlaylistId(long id);
    public String getNameById(long id);
    public long getOwner(long id);
    public void deletePlaylist (long id);
    public long getIdByPlaylistIdAndSongId(long plId, long sId);
    public void create(PlaylistDTO playlistDTO, String loggedEmail);
    public void update(long id, PlaylistDTO playlistDTO,String loggedEmail) throws Exception;
    public void addSong(long idPlaylist,Long idSong,String loggedEmail) throws Exception;
    public void deleteSong(long idPlaylist, long idSong, String loggedEmail) throws Exception;
    public void delete(long idPlaylist,String loggedEmail) throws Exception;
    //metodo asincronico
    public CompletableFuture<List<Playlist>> getPlaylistsAsync();
}
