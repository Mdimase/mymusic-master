package ar.edu.unnoba.pdyc.mymusic.service;

import ar.edu.unnoba.pdyc.mymusic.model.Genre;
import ar.edu.unnoba.pdyc.mymusic.model.Playlist;
import ar.edu.unnoba.pdyc.mymusic.model.Song;
import ar.edu.unnoba.pdyc.mymusic.model.User;
import ar.edu.unnoba.pdyc.mymusic.repository.SongRepository;
import ar.edu.unnoba.pdyc.mymusic.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class SongServiceImp implements SongService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SongRepository songRepository;

    @Override
    public List<Song> getSongs() {
        return songRepository.findAll();
    }

    @Override
    public List<Song> getSongsByAuthorGenre(String author, Genre genre) {return songRepository.findByAuthorAndGenre(author,genre);}

    //implementacion metodo asincronico para obtener todas las canciones
    @Override
    @Async("taskExecutor")
    public CompletableFuture<List<Song>> getSongsAsync(String author, Genre genre) {
        return CompletableFuture.completedFuture(getSongsByAuthorGenre(author,genre));
    }

    @Override
    @Async("taskExecutor")
    public CompletableFuture<List<Song>> getSongsAsync() {
        return CompletableFuture.completedFuture(getSongs());
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
