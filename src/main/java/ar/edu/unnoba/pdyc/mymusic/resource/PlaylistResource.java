package ar.edu.unnoba.pdyc.mymusic.resource;

import ar.edu.unnoba.pdyc.mymusic.dto.*;
import ar.edu.unnoba.pdyc.mymusic.exception.NotFoundException;
import ar.edu.unnoba.pdyc.mymusic.exception.UnauthorizedException;
import ar.edu.unnoba.pdyc.mymusic.model.User;
import ar.edu.unnoba.pdyc.mymusic.service.PlaylistService;
import ar.edu.unnoba.pdyc.mymusic.service.Utils;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

import javax.ws.rs.*;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.lang.reflect.Type;
import java.util.List;


/*
GET http://localhost:8080/mymusic/app/playlists                   lista de playlists (sin canciones)
GET http://localhost:8080/mymusic/app/playlists/:id               info de una playlist (con canciones)
POST http://localhost:8080/mymusic/app/playlists                  nueva playlist
PUT http://localhost:8080/mymusic/app/playlists/:id               modificar nombre de la playlist = id
DELETE http://localhost:8080/mymusic/app/playlists/:id            borra la playlist = id
POST http://localhost:8080/mymusic/app/playlists/:id/songs        insertar una cancion en una playlist = id
DELETE http://localhost:8080/mymusic/app/playlists/:id/songs/:id            borra una cancion de un playlist
DELETE http://localhost:8080/mymusic/app/playlists/:id            borra una playlist
 */

@Path("/playlists")
public class PlaylistResource {

    @Autowired
    private PlaylistService playlistService;

    @Autowired
    private Utils utils;

    /*
    //metodo asincronico para obtener todas las playlists
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public void getPlaylist(@Suspended AsyncResponse response) {
        playlistService.getPlaylistsAsync().thenAccept((list) -> {
            ModelMapper modelMapper = new ModelMapper();
            Type listType = new TypeToken<List<PlaylistDTO>>(){}.getType();
            List<PlaylistDTO> listDto = modelMapper.map(list,listType);
            response.resume(Response.ok(listDto).build());
        });
    }*/

    //metodo asincronico para obtener todas las playlists del usuario logged
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public void getPlaylist(@Suspended AsyncResponse response) {
        User userLogged = utils.getUserLogged(utils.getEmailLogged());
        playlistService.getPlaylistsAsync(userLogged).thenAccept((list) -> {
            ModelMapper modelMapper = new ModelMapper();
            Type listType = new TypeToken<List<PlaylistDTO>>(){}.getType();
            List<PlaylistDTO> listDto = modelMapper.map(list,listType);
            response.resume(Response.ok(listDto).build());
        });
    }

    // crear una playlist
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public void createPlaylist(@Suspended AsyncResponse response,PlaylistDTO playlistDTO){
        playlistService.createAsync(playlistDTO,utils.getEmailLogged()).thenAccept((res) ->{
            response.resume(Response.status(Response.Status.CREATED).build());
        });
    }

    //info de una playlist (incluye todas sus canciones)
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public void getPlaylistById(@Suspended AsyncResponse response,@PathParam("id") long id){
        ModelMapper modelMapper = new ModelMapper();
        PlaylistWithSongsDTO playlistWithSongsDTO = new PlaylistWithSongsDTO(); //creo el DTO a retornar
        Type listSongDTOType = new TypeToken<List<SongDTO>>(){}.getType();
        playlistService.getNameByIdAsync(id).thenCombine(playlistService.getSongsByPlaylistIdAsync(id), (name,list)->{
            playlistWithSongsDTO.setPlaylistName(name);  // le seteo el nombre de la playlist
            List<SongDTO> listSongsDTO = modelMapper.map(list,listSongDTOType); // creo la lista de songsdto
            playlistWithSongsDTO.setSongs(listSongsDTO);
            response.resume(Response.ok(playlistWithSongsDTO).build()) ;
            return null;
        }).exceptionally(ex ->{
            response.resume(Response.status(Response.Status.NOT_FOUND).build());
            return null;
        });
    }

    // modificar el nombre de una playlist
    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public void updatePlaylist (@Suspended AsyncResponse response, @PathParam("id")long id, PlaylistUpdateDTO playlistUpdateDTO){
        User userLogged = utils.getUserLogged(utils.getEmailLogged());
        playlistService.updateAsync(id,playlistUpdateDTO,userLogged).handle((res,ex) ->{
            if(res != null){
                response.resume(Response.ok().build());
            }
            if(ex.getCause() instanceof NotFoundException){
                response.resume(Response.status(Response.Status.NOT_FOUND).build());
            }
            if(ex.getCause() instanceof UnauthorizedException){
                response.resume(Response.status(Response.Status.UNAUTHORIZED).build());
            }
            return null;
        });
    }

    // metodo asincrono para agregar una cancion a una playlist
    @POST
    @Path("/{id}/songs")
    @Consumes(MediaType.APPLICATION_JSON)
    public void addSongInPlaylist(@Suspended AsyncResponse response,@PathParam("id") long idPlaylist, PlaylistsSongsDTO playlistsSongsDTO){
        long idSong = Long.parseLong(playlistsSongsDTO.getId());
        User userLogged = utils.getUserLogged(utils.getEmailLogged());
        playlistService.addSongAsync(idPlaylist,idSong,userLogged).handle((res,ex) ->{
            if(res!=null){
                response.resume(Response.status(Response.Status.CREATED).build());
            }
            if(ex.getCause() instanceof NotFoundException){
                response.resume(Response.status(Response.Status.NOT_FOUND).build());
            }
            if(ex.getCause() instanceof UnauthorizedException){
                response.resume(Response.status(Response.Status.UNAUTHORIZED).build());
            }
            if(ex.getCause() instanceof DataIntegrityViolationException){
                response.resume(Response.status(Response.Status.BAD_REQUEST).build());
            }
            return null;
        });
    }

    // metodo asincrono para eliminar una cancion a una playlist
    @DELETE
    @Path("/{idPlaylist}/songs/{idSong}")
    public void deleteSongInPlaylist(@Suspended AsyncResponse response,@PathParam("idPlaylist") long idPlaylist,@PathParam("idSong") long idSong){
        User userLogged = utils.getUserLogged(utils.getEmailLogged());
        playlistService.deleteSongAsync(idPlaylist,idSong, userLogged).handle((res,ex) ->{
            if(res != null){
                response.resume(Response.ok().build());
            }
            if(ex.getCause() instanceof NotFoundException){
                response.resume(Response.status(Response.Status.NOT_FOUND).build());
            }
            if(ex.getCause() instanceof UnauthorizedException){
                response.resume(Response.status(Response.Status.UNAUTHORIZED).build());
            }
            return null;
        });
    }

    // metodo asincrono para eliminar una playlist
    @DELETE
    @Path("/{idPlaylist}")
    public void delete(@Suspended AsyncResponse response,@PathParam("idPlaylist") long idPlaylist){
        User userLogged = utils.getUserLogged(utils.getEmailLogged());
        playlistService.deletePlaylistAsync(idPlaylist,userLogged).handle((res,ex)->{
            if(res != null){
                response.resume(Response.ok().build());
            }
            if(ex.getCause() instanceof NotFoundException){
                response.resume(Response.status(Response.Status.NOT_FOUND).build());
            }
            if(ex.getCause() instanceof UnauthorizedException){
                response.resume(Response.status(Response.Status.UNAUTHORIZED).build());
            }
            return null;
            });
    }
}
