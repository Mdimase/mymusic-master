package ar.edu.unnoba.pdyc.mymusic.service;

import ar.edu.unnoba.pdyc.mymusic.model.Genre;
import ar.edu.unnoba.pdyc.mymusic.model.Song;
import ar.edu.unnoba.pdyc.mymusic.repository.SongRepository;
import ar.edu.unnoba.pdyc.mymusic.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.ws.rs.BadRequestException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class SongServiceImp implements SongService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SongRepository songRepository;

    //implementacion metodo asincronico para obtener todas las canciones
    @Override
    @Async("taskExecutor")
    public CompletableFuture<List<Song>> getSongsAsync(String author, Genre genre){
        return CompletableFuture.supplyAsync(()-> songRepository.findByAuthorAndGenre(author,genre));
    }

    @Override
    public CompletableFuture<List<Song>> getSongsAsync(String author){
        return CompletableFuture.supplyAsync(()-> songRepository.findByAuthor(author));
    }

    @Override
    public CompletableFuture<List<Song>> getSongsAsync(Genre genre) {
        return CompletableFuture.supplyAsync(()-> songRepository.findByGenre(genre));
    }

    @Override
    @Async("taskExecutor")
    public CompletableFuture<List<Song>> getSongsAsync(){
        return CompletableFuture.supplyAsync(()-> songRepository.findAll());
    }

    // testing code scene
    public static void test(int a, int b, int c, int d, int e, int f){
        System.out.println("Testing");
    }

    /************************************************************
    ejemplo de clase
    @Override
    public void create(Song song,String ownerEmail) {
        song.setOwner(userRepository.findByEmail(ownerEmail));
        songRepository.save(song);
    }

    @Override
    public void update(long id,Song song, String loggedEmail) throws Exception {
        User userLogged = userRepository.findByEmail(loggedEmail);
        Song songDB = songRepository.findById(id).get();    //el get esta por optional, la consulta devuelve el objeto o nada(o esta en la BD)
        if(songDB.getOwner().equals(userLogged)){
            songDB.setAuthor(song.getAuthor());
            songDB.setName(song.getName());
            songRepository.save(songDB);
        } else {
            throw new Exception("no podes modificar una cancion de la que no sos el dueño");
        }
    }

    @Override
    public void delete(long id,String loggedEmail) throws Exception {
        User userLogged = userRepository.findByEmail(loggedEmail);
        Song songDB = songRepository.findById(id).get();
        if(songDB.getOwner().equals(userLogged)){
            songRepository.delete(songDB);
        } else {
            throw new Exception("no podes borrar una cancion de la que no sos dueño");
        }
    }

    **************************************************************/

}
