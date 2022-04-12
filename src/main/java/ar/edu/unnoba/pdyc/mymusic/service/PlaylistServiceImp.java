package ar.edu.unnoba.pdyc.mymusic.service;

import ar.edu.unnoba.pdyc.mymusic.dto.PlaylistDTO;
import ar.edu.unnoba.pdyc.mymusic.exception.NotFoundException;
import ar.edu.unnoba.pdyc.mymusic.exception.UnauthorizedException;
import ar.edu.unnoba.pdyc.mymusic.model.Playlist;
import ar.edu.unnoba.pdyc.mymusic.model.PlaylistsSongs;
import ar.edu.unnoba.pdyc.mymusic.model.Song;
import ar.edu.unnoba.pdyc.mymusic.model.User;
import ar.edu.unnoba.pdyc.mymusic.repository.PlaylistRepository;
import ar.edu.unnoba.pdyc.mymusic.repository.PlaylistsSongsRepository;
import ar.edu.unnoba.pdyc.mymusic.repository.SongRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class PlaylistServiceImp implements PlaylistService{

    @Autowired
    private Utils utils;

    @Autowired
    private SongRepository songRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private PlaylistRepository playlistRepository;

    @Autowired
    private PlaylistsSongsRepository playlistsSongsRepository;

    //implementacion metodo asincronico para obtener todas las playlists
    @Override
    @Async("taskExecutor")
    public CompletableFuture<List<Playlist>> getPlaylistsAsync() {
        return CompletableFuture.supplyAsync(()-> playlistRepository.findAll());
    }

    //implementacion metodo asincronico para obtener una playlist con sus canciones
    @Override
    @Async("taskExecutor")
    public CompletableFuture<List<Song>> getSongsByPlaylistIdAsync(long id) throws NotFoundException{
        return CompletableFuture.supplyAsync(() -> {
            if(!playlistRepository.existsById(id)){
                throw new NotFoundException("Recurso inexistente");
            }
            return playlistRepository.getSongsByPlaylistId(id);
        });
    }

    //implementacion metodo asincronico para crear una playlist
    @Override
    @Async("taskExecutor")
    public CompletableFuture<Playlist> createAsync(PlaylistDTO playlistDTO, String loggedEmail){
        return CompletableFuture.supplyAsync(() ->{
            Playlist playlist = new Playlist();
            playlist.setName(playlistDTO.getName());
            playlist.setUser(utils.getUserLogged(loggedEmail));
            playlistRepository.save(playlist);
            return playlist;
        });
    }

    //implementacion metodo asincronico para modificar el nombre de una playlist
    @Override
    @Async("taskExecutor")
    public CompletableFuture<Playlist> updateAsync(long id,PlaylistDTO playlistDTO,User userLogged)throws NotFoundException,UnauthorizedException{
        return CompletableFuture.supplyAsync(()->{
            if(!playlistRepository.existsById(id)){
                throw new NotFoundException("Recurso inexistente");
            }
            Playlist playlistBD = playlistRepository.findById(id);
            if(!isOwner(playlistBD,userLogged)){
                throw new UnauthorizedException();
            }
            playlistBD.setName(playlistDTO.getName());
            playlistRepository.save(playlistBD);
            return playlistBD;
        });
    }

    //implementacion metodo asincronico para obtener el nombre de una playlist
    @Override
    @Async("taskExecutor")
    public CompletableFuture<String> getNameByIdAsync(long id) {
        return CompletableFuture.supplyAsync(() -> {
            if(!playlistRepository.existsById(id)){
                throw new NotFoundException("Recurso inexistente");
            }
            return playlistRepository.getNameById(id);
        });
    }

    //implementacion metodo asincronico para agregar una cancion a una playlist
    @Override
    @Async("taskExecutor")
    public CompletableFuture<Song> addSongAsync(long idPlaylist,long idSong,User userLogged)throws NotFoundException,UnauthorizedException,DataIntegrityViolationException{
        return CompletableFuture.supplyAsync(()->{
            if(!playlistRepository.existsById(idPlaylist) || !songRepository.existsById(idSong)){
                throw new NotFoundException("Recurso inexistente");
            }
            Playlist playlistBD = playlistRepository.findById(idPlaylist);
            if(!isOwner(playlistBD,userLogged)){
                throw new UnauthorizedException();
            }
            Song song = songRepository.findById(idSong);
            PlaylistsSongs playlistsSongs = new PlaylistsSongs();
            playlistsSongs.setSong(song);
            playlistsSongs.setPlaylist(playlistBD);
            playlistsSongsRepository.save(playlistsSongs);
            return song;
        });
    }

    //implementacion metodo asincronico para borrar una cancion a una playlist
    @Override
    @Async("taskExecutor")
    public CompletableFuture<Song> deleteSongAsync(long idPlaylist,long idSong,User userLogged){
        return CompletableFuture.supplyAsync(() ->{
            if(!playlistRepository.existsById(idPlaylist) || !songRepository.existsById(idSong)){
                throw new NotFoundException("Recurso inexistente");
            }
            Playlist playlistBD = playlistRepository.findById(idPlaylist);
            Song song = songRepository.findById(idSong);
            if(!isOwner(playlistBD,userLogged)){
                throw new UnauthorizedException();
            }
            Long pkToDelete = this.getIdByPlaylistIdAndSongId(idPlaylist,idSong);
            if(pkToDelete!=null){
                playlistsSongsRepository.deleteById(pkToDelete);
            }
            return song;
        });
    }

    //implementacion metodo asincronico para borrar una playlist
    @Override
    @Async("taskExecutor")
    public CompletableFuture<Playlist> deletePlaylistAsync(long idPlaylist,User userLogged)throws NotFoundException,UnauthorizedException{
        return CompletableFuture.supplyAsync(()->{
            if(!playlistRepository.existsById(idPlaylist)){
                throw new NotFoundException("Recurso inexistente");
            }
            Playlist playlistBD = playlistRepository.findById(idPlaylist);
            if(!isOwner(playlistBD,userLogged)){
                throw new UnauthorizedException();
            }
            this.deletePlaylist(idPlaylist);
            return playlistBD;
        });
    }

    @Override
    @Async("taskExecutor")
    public void deletePlaylist(long id){
        List<Long> pkABorrar = playlistsSongsRepository.getIdByPlaylistId(id);  //consigo todos los pk de la tabla nn
        for (Long aLong : pkABorrar) {
            playlistsSongsRepository.deleteById(aLong); //borro de la tabla nn
        }
        playlistRepository.deleteById(id);  //borro de la tabla playlist
    }

    @Override
    public Long getIdByPlaylistIdAndSongId(long plId, long sId){
        return playlistsSongsRepository.getIdByPlaylistIdAndSongId(plId,sId);
    }

    @Override
    public User getOwner(Playlist playlist)throws NotFoundException{
        return userService.findById(playlist.getUser().getId());
    }

    @Override
    public boolean isOwner(Playlist playlist,User userLogged){
        return getOwner(playlist).equals(userLogged);
    }

}
