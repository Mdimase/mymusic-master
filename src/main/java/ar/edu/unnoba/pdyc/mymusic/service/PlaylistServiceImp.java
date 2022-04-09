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
    private SongRepository songRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PlaylistRepository playlistRepository;

    @Autowired
    private PlaylistsSongsRepository playlistsSongsRepository;

    @Override
    public List<Playlist> getPlaylists() {
        return playlistRepository.findAll();
    }

    //implementacion metodo asincronico para obtener todas las playlists
    @Override
    @Async("taskExecutor")
    public CompletableFuture<List<Playlist>> getPlaylistsAsync() {
        return CompletableFuture.completedFuture(playlistRepository.findAll());
    }

    @Override
    public void create(PlaylistDTO playlistDTO, String loggedEmail){
        User userLogged = userRepository.findByEmail(loggedEmail);
        Playlist playlist = new Playlist();
        playlist.setName(playlistDTO.getName());
        playlist.setUser(userLogged);
        playlistRepository.save(playlist);
    }

    @Override
    public void update(long id, PlaylistDTO playlistDTO, String loggedEmail) throws Exception {
        User userLogged = userRepository.findByEmail(loggedEmail);
        Playlist playlistBD = playlistRepository.findById(id).get();
        User ownerPlaylist = userRepository.findById(playlistBD.getUser().getId()).get();
        if(ownerPlaylist.equals(userLogged)){
            playlistBD.setName(playlistDTO.getName());
            playlistRepository.save(playlistBD);
        } else{
            throw new Exception("no podes modificar una playlist de la que no sos el dueño");
        }
    }

    @Override
    public void addSong(long idPlaylist, Long idSong, String loggedEmail) throws Exception {
        User userLogged = userRepository.findByEmail(loggedEmail);
        Playlist playlistBD = playlistRepository.findById(idPlaylist).get();
        User ownerPlaylist = userRepository.findById(playlistBD.getUser().getId()).get();
        if(ownerPlaylist.equals(userLogged)){
            Song song = songRepository.findById(idSong).get();
            PlaylistsSongs playlistsSongs = new PlaylistsSongs();
            playlistsSongs.setSong(song);
            playlistsSongs.setPlaylist(playlistBD);
            playlistsSongsRepository.save(playlistsSongs);
        } else {
            throw new Exception("no podes agregar una cancion a una playlist de la que no sos el dueño");
        }
    }

    @Override
    public void deleteSong(long idPlaylist, long idSong, String loggedEmail) throws Exception {
        User userLogged = userRepository.findByEmail(loggedEmail);
        Playlist playlistBD = playlistRepository.findById(idPlaylist).get();
        User ownerPlaylist = userRepository.findById(playlistBD.getUser().getId()).get();
        if(ownerPlaylist.equals(userLogged)){
            Long PkToDelete = this.getIdByPlaylistIdAndSongId(idPlaylist,idSong);
            playlistsSongsRepository.deleteById(PkToDelete);
        } else {
            throw new Exception("no podes borrar una cancion a una playlist de la que no sos el dueño");
        }
    }

    @Override
    public void delete(long idPlaylist, String loggedEmail) throws Exception {
        User userLogged = userRepository.findByEmail(loggedEmail);
        Playlist playlistBD = playlistRepository.findById(idPlaylist).get();
        User ownerPlaylist = userRepository.findById(playlistBD.getUser().getId()).get();
        if(ownerPlaylist.equals(userLogged)){
            this.deletePlaylist(idPlaylist);
        } else {
            throw new Exception("no podes borrar una playlist de la que no sos el dueño");
        }
    }

    @Override
    public List<Song> getSongsByPlaylistId(long id) {
        return playlistRepository.getSongsByPlaylistId(id);
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

}
