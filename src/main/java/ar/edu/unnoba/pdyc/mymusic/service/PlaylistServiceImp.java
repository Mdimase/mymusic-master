package ar.edu.unnoba.pdyc.mymusic.service;

import ar.edu.unnoba.pdyc.mymusic.dto.PlaylistDTO;
import ar.edu.unnoba.pdyc.mymusic.model.Playlist;
import ar.edu.unnoba.pdyc.mymusic.model.PlaylistsSongs;
import ar.edu.unnoba.pdyc.mymusic.model.Song;
import ar.edu.unnoba.pdyc.mymusic.model.User;
import ar.edu.unnoba.pdyc.mymusic.repository.PlaylistRepository;
import ar.edu.unnoba.pdyc.mymusic.repository.PlaylistsSongsRepository;
import ar.edu.unnoba.pdyc.mymusic.repository.SongRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
    public CompletableFuture<List<Song>> getSongsByPlaylistIdAsync(long id) {
        return CompletableFuture.supplyAsync(() -> playlistRepository.getSongsByPlaylistId(id));
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
    public CompletableFuture<Playlist> updateAsync(long id,PlaylistDTO playlistDTO, String loggedEmail)throws RuntimeException{
        return CompletableFuture.supplyAsync(()->{
            Playlist playlistBD = null;
            if(playlistRepository.findById(id).isPresent()){
                playlistBD = playlistRepository.findById(id).get();
                if(getOwner(playlistBD).equals(utils.getUserLogged(loggedEmail))){
                    playlistBD.setName(playlistDTO.getName());
                    playlistRepository.save(playlistBD);
                }
                else {
                    throw new RuntimeException("no podes agregar una cancion a una playlist de la que no sos el dueño");
                }
            }
            return playlistBD;
        });
    }

    //implementacion metodo asincronico para obtener el nombre de una playlist
    @Override
    @Async("taskExecutor")
    public CompletableFuture<String> getNameByIdAsync(long id) {
        return CompletableFuture.supplyAsync(() -> playlistRepository.getNameById(id));
    }

    //implementacion metodo asincronico para agregar una cancion a una playlist
    @Override
    @Async("taskExecutor")
    public CompletableFuture<Song> addSongAsync(long idPlaylist,Long idSong,String loggedEmail) throws RuntimeException{
        return CompletableFuture.supplyAsync(()->{
            Song song = null;
            if(playlistRepository.findById(idPlaylist).isPresent() && songRepository.findById(idSong).isPresent()){
                Playlist playlistBD = playlistRepository.findById(idPlaylist).get();
                song = songRepository.findById(idSong).get();
                if(getOwner(playlistBD).equals(utils.getUserLogged(loggedEmail))){
                    PlaylistsSongs playlistsSongs = new PlaylistsSongs();
                    playlistsSongs.setSong(song);
                    playlistsSongs.setPlaylist(playlistBD);
                    playlistsSongsRepository.save(playlistsSongs);
                } else {
                    throw new RuntimeException("no podes agregar una cancion a una playlist de la que no sos el dueño");
                }
            }
            return song;
        });
    }

    //implementacion metodo asincronico para borrar una cancion a una playlist
    @Override
    @Async("taskExecutor")
    public CompletableFuture<Song> deleteSongAsync(long idPlaylist,Long idSong,String loggedEmail) throws RuntimeException {
        return CompletableFuture.supplyAsync(() ->{
            Song song = null;
            Long pkToDelete = this.getIdByPlaylistIdAndSongId(idPlaylist,idSong);
            if(playlistRepository.findById(idPlaylist).isPresent() && pkToDelete!=null && songRepository.findById(idSong).isPresent()){
                song = songRepository.findById(idSong).get();
                Playlist playlistBD = playlistRepository.findById(idPlaylist).get();
                if(getOwner(playlistBD).equals(utils.getUserLogged(loggedEmail))){
                    playlistsSongsRepository.deleteById(pkToDelete);
                }else {
                    throw new RuntimeException("no podes borrar una cancion a una playlist de la que no sos el dueño");
                }
            }
            return song;
        });
    }

    //implementacion metodo asincronico para borrar una playlist
    @Override
    @Async("taskExecutor")
    public CompletableFuture<Playlist> deletePlaylistAsync(long idPlaylist,String loggedEmail) throws RuntimeException{
        return CompletableFuture.supplyAsync(()->{
            Playlist playlistBD = null;
            if(playlistRepository.findById(idPlaylist).isPresent()){
                playlistBD = playlistRepository.findById(idPlaylist).get();
                if(getOwner(playlistBD).equals(utils.getUserLogged(loggedEmail))){
                    this.deletePlaylist(idPlaylist);
                }else {
                    throw new RuntimeException("no podes borrar una playlist de la que no sos el dueño");
                }
            }
            return playlistBD;
        });
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
    public Long getIdByPlaylistIdAndSongId(long plId, long sId){
        return playlistsSongsRepository.getIdByPlaylistIdAndSongId(plId,sId);
    }

    @Override
    public User getOwner(Playlist playlist){
        return userService.findById(playlist.getUser().getId());
    }

}
