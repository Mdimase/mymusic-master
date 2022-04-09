package ar.edu.unnoba.pdyc.mymusic.service;

import ar.edu.unnoba.pdyc.mymusic.dto.PlaylistDTO;
import ar.edu.unnoba.pdyc.mymusic.model.Playlist;
import ar.edu.unnoba.pdyc.mymusic.model.PlaylistsSongs;
import ar.edu.unnoba.pdyc.mymusic.model.Song;
import ar.edu.unnoba.pdyc.mymusic.model.User;
import ar.edu.unnoba.pdyc.mymusic.repository.PlaylistRepository;
import ar.edu.unnoba.pdyc.mymusic.repository.PlaylistsSongsRepository;
import ar.edu.unnoba.pdyc.mymusic.repository.SongRepository;
import ar.edu.unnoba.pdyc.mymusic.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.util.Arrays;
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

    @Override
    public List<Playlist> getPlaylists() {
        return playlistRepository.findAll();
    }

    @Override
    public List<Song> getSongsByPlaylistId(long id) {
        return playlistRepository.getSongsByPlaylistId(id);
    }

    //implementacion metodo asincronico para obtener todas las playlists
    @Override
    @Async("taskExecutor")
    public CompletableFuture<List<Playlist>> getPlaylistsAsync() {
        return CompletableFuture.completedFuture(getPlaylists());
    }

    //implementacion metodo asincronico para obtener uan playlist con sus canciones
    @Override
    @Async("taskExecutor")
    public CompletableFuture<List<Song>> getSongsByPlaylistIdAsync(long id) {
        return CompletableFuture.completedFuture(getSongsByPlaylistId(id));
    }

    //implementacion metodo asincronico para crear una playlist
    @Override
    @Async("taskExecutor")
    public CompletableFuture<Playlist> createAsync(PlaylistDTO playlistDTO, String loggedEmail) {
        return CompletableFuture.completedFuture(create(playlistDTO,loggedEmail));
    }

    //implementacion metodo asincronico para modificar el nombre de una playlist
    @Override
    @Async("taskExecutor")
    public CompletableFuture<Playlist> updateAsync(long id,PlaylistDTO playlistDTO, String loggedEmail) throws Exception {
        return CompletableFuture.completedFuture(update(id,playlistDTO,loggedEmail));
    }

    //implementacion metodo asincronico para obtener el nombre de una playlist
    @Override
    @Async("taskExecutor")
    public CompletableFuture<String> getNameByIdAsync(long id) {
        return CompletableFuture.completedFuture(getNameById(id));
    }

    @Override
    public Playlist create(PlaylistDTO playlistDTO, String loggedEmail){
        User userLogged = userService.findByEmail(loggedEmail);
        Playlist playlist = new Playlist();
        playlist.setName(playlistDTO.getName());
        playlist.setUser(userLogged);
        playlistRepository.save(playlist);
        return playlist;
    }

    @Override
    public Playlist update(long id, PlaylistDTO playlistDTO, String loggedEmail) throws Exception {
        if(playlistRepository.findById(id).isPresent()){
            Playlist playlistBD = playlistRepository.findById(id).get();
            if(getOwner(playlistBD).equals(utils.getUserLogged(loggedEmail))){
                playlistBD.setName(playlistDTO.getName());
                playlistRepository.save(playlistBD);
            } else{
                throw new Exception("no podes modificar una playlist de la que no sos el dueño");
            }
            return playlistBD;
        }
        else{
            return null;
        }
    }

    //implementacion metodo asincronico para agregar una cancion a una playlist
    @Override
    @Async("taskExecutor")
    public CompletableFuture<Song> addSongAsync(long idPlaylist,Long idSong,String loggedEmail) throws Exception {
        return CompletableFuture.completedFuture(addSong(idPlaylist,idSong,loggedEmail));
    }

    @Override
    public Song addSong(long idPlaylist, Long idSong, String loggedEmail) throws Exception {
        if(playlistRepository.findById(idPlaylist).isPresent() && songRepository.findById(idSong).isPresent()){
            Playlist playlistBD = playlistRepository.findById(idPlaylist).get();
            Song song = songRepository.findById(idSong).get();
            if(getOwner(playlistBD).equals(utils.getUserLogged(loggedEmail))){
                PlaylistsSongs playlistsSongs = new PlaylistsSongs();
                playlistsSongs.setSong(song);
                playlistsSongs.setPlaylist(playlistBD);
                playlistsSongsRepository.save(playlistsSongs);
                return song;
            } else {
                throw new Exception("no podes agregar una cancion a una playlist de la que no sos el dueño");
            }
        }
        else{
            return null;
        }
    }

    //implementacion metodo asincronico para borrar una cancion a una playlist
    @Override
    @Async("taskExecutor")
    public CompletableFuture<Song> deleteSongAsync(long idPlaylist,Long idSong,String loggedEmail) throws Exception {
        return CompletableFuture.completedFuture(deleteSong(idPlaylist,idSong,loggedEmail));
    }

    @Override
    public Song deleteSong(long idPlaylist, long idSong, String loggedEmail) throws Exception{
        if(playlistRepository.findById(idPlaylist).isPresent() && songRepository.findById(idSong).isPresent()){
            Song song = songRepository.findById(idSong).get();
            Playlist playlistBD = playlistRepository.findById(idPlaylist).get();
            if(getOwner(playlistBD).equals(utils.getUserLogged(loggedEmail))){
                Long PkToDelete = this.getIdByPlaylistIdAndSongId(idPlaylist,idSong);
                playlistsSongsRepository.deleteById(PkToDelete);
            } else {
                throw new Exception("no podes borrar una cancion a una playlist de la que no sos el dueño");
            }
            return song;
        }
        else{
            return null;
        }
    }

    //implementacion metodo asincronico para borrar una playlist
    @Override
    @Async("taskExecutor")
    public CompletableFuture<Playlist> deletePlaylistAsync(long idPlaylist,String loggedEmail) throws Exception {
        return CompletableFuture.completedFuture(delete(idPlaylist,loggedEmail));
    }

    @Override
    public Playlist delete(long idPlaylist, String loggedEmail) throws Exception{
        if(playlistRepository.findById(idPlaylist).isPresent()){
            Playlist playlistBD = playlistRepository.findById(idPlaylist).get();
            if(getOwner(playlistBD).equals(utils.getUserLogged(loggedEmail))){
                this.deletePlaylist(idPlaylist);
            } else {
                throw new Exception("no podes borrar una playlist de la que no sos el dueño");
            }
            return playlistBD;
        } else{
            return null;
        }
    }



    @Override
    public String getNameById(long id) {
        return playlistRepository.getNameById(id);
    }

    @Override
    public long getOwner(long id){
        return playlistRepository.getOwner(id);
    }

    @Override
    public void deletePlaylist(long id){
        List<Long> pkABorrar = playlistsSongsRepository.getIdByPlaylistId(id);  //consigo todos los pk de la tabla nn
        for (Long aLong : pkABorrar) {
            playlistsSongsRepository.deleteById(aLong); //borro de la tabla nn
        }
        playlistRepository.deleteById(id);  //borro de la tabla playlist
    }

    @Override
    public long getIdByPlaylistIdAndSongId(long plId, long sId){
        return playlistsSongsRepository.getIdByPlaylistIdAndSongId(plId,sId);
    }

    public User getOwner(Playlist playlist){
        return userService.findById(playlist.getUser().getId());
    }

}
