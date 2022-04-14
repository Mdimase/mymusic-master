package ar.edu.unnoba.pdyc.mymusic.service;

import ar.edu.unnoba.pdyc.mymusic.model.Genre;
import ar.edu.unnoba.pdyc.mymusic.model.Song;

import javax.ws.rs.BadRequestException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface SongService {
    //metodo asincronico
    CompletableFuture<List<Song>> getSongsAsync(String author, Genre genre);
    CompletableFuture<List<Song>> getSongsAsync(String author);
    CompletableFuture<List<Song>> getSongsAsync(Genre genre);
    CompletableFuture<List<Song>> getSongsAsync();

    /**********************************
    ejemplo de clase
     public void create(Song song,String ownerEmail);
    public void update(long id,Song song, String loggedEmail) throws Exception;
    void delete(long id,String loggedEmail) throws Exception;
     ************************************************/
}
