package ar.edu.unnoba.pdyc.mymusic.resource;

import ar.edu.unnoba.pdyc.mymusic.dto.PlaylistDTO;
import ar.edu.unnoba.pdyc.mymusic.dto.PlaylistWithSongsDTO;
import ar.edu.unnoba.pdyc.mymusic.dto.PlaylistsSongsDTO;
import ar.edu.unnoba.pdyc.mymusic.dto.SongDTO;
import ar.edu.unnoba.pdyc.mymusic.service.PlaylistService;
import ar.edu.unnoba.pdyc.mymusic.service.Utils;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import javax.ws.rs.*;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.lang.reflect.Type;
import java.util.List;

/*
*   TO DO
*   HACER EL SIGNUP ENDPOINT
*   EL PAGINADO
*
*
* */


/*
GET http://localhost:8080/mymusic/playlists                   lista de playlists (sin canciones)
GET http://localhost:8080/mymusic/playlists/:id               lista de playlists (con canciones)
POST http://localhost:8080/mymusic/playlists                  nueva playlist
PUT http://localhost:8080/mymusic/playlists/:id               actualizo la playlist = id
DELETE http://localhost:8080/mymusic/playlists/:id            borra la playlist = id
POST http://localhost:8080/mymusic/playlists/:id/songs        insertar una cancion en una playlist
DELETE http://localhost:8080/mymusic/playlists/:id/songs/:id            borra una cancion de un playlist
DELETE http://localhost:8080/mymusic/playlists/:id            borra una playlist
 */

//el .map(objeto a convertir el cual sirve de fuente de datos, el tipo al que quiero que lo convierta)

@Path("/playlists")
public class PlaylistResource {

    @Autowired
    private PlaylistService playlistService;

    @Autowired
    private Utils utils;

    /********************************************
     *              metodos sincronos
     *******************************************/

    // obtener todas las playlists (sin canciones)

    /*@GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPlaylist(){
        ModelMapper modelMapper = new ModelMapper();
        Type listType = new TypeToken<List<PlaylistDTO>>(){}.getType();
        List<PlaylistDTO> list = modelMapper.map(playlistService.getPlaylists(),listType);
        return Response.ok(list).build();
    }*/

    /*
    // crear una playlist
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createPlaylist(PlaylistDTO playlistDTO){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication(); //contexto de seguridad de spring
        String loggedEmail = (String) auth.getPrincipal();   //email del usuario loggeado
        playlistService.create(playlistDTO,loggedEmail);
        return Response.status(Response.Status.CREATED).build();
    }
    */

    /*
    //info de una playlist (incluye todas sus canciones)
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPlaylistById(@PathParam("id") long id){
        ModelMapper modelMapper = new ModelMapper();
        PlaylistWithSongsDTO playlistWithSongsDTO = new PlaylistWithSongsDTO(); //creo el DTO a retornar
        playlistWithSongsDTO.setPlaylistName(playlistService.getNameById(id));  // le seteo el nombre de la playlist
        Type listSongDTOType = new TypeToken<List<SongDTO>>(){}.getType(); //armo el tipo lista de songsDTO (sin el id)
        List<SongDTO> listSongsDTO = modelMapper.map(playlistService.getSongsByPlaylistId(id),listSongDTOType); // creo la lista de songsdto
        //el .map(objeto a convertir el cual sirve de fuente de datos, el tipo al que quiero que lo convierta)
        playlistWithSongsDTO.setSongs(listSongsDTO);
        return Response.ok(playlistWithSongsDTO).build();
    }
    */
    /*
    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updatePlaylist (@PathParam("id")long id,PlaylistDTO playlistDTO){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String loggedEmail = (String) auth.getPrincipal();
        try{
            playlistService.update(id,playlistDTO,loggedEmail);
            return Response.ok().build();
        } catch (Exception e){
            return Response.status(Response.Status.FORBIDDEN).build();
        }
    }*/

    /*
    @POST
    @Path("/{id}/songs")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addSongInPlaylist(@PathParam("id") long idPlaylist, PlaylistsSongsDTO playlistsSongsDTO){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String loggedEmail = (String) auth.getPrincipal();
        Long idSong = Long.valueOf(playlistsSongsDTO.getId());
        try {
            playlistService.addSong(idPlaylist,idSong,loggedEmail);
            return Response.status(Response.Status.CREATED).build();
        } catch (Exception e) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
    }
     */

    /*
    * @DELETE
    @Path("/{idPlaylist}/songs/{idSong}")
    public Response deleteSongInPlaylist(@PathParam("idPlaylist") long idPlaylist,@PathParam("idSong") long idSong){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String loggedEmail = (String) auth.getPrincipal();
        try{
            playlistService.deleteSong(idPlaylist,idSong,loggedEmail);
            return Response.ok().build();
        } catch (Exception e){
            return Response.status(Response.Status.FORBIDDEN).build();
        }
    }
    *
    * @DELETE
    @Path("/{idPlaylist}")
    public Response delete(@PathParam("idPlaylist") long idPlaylist){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String loggedEmail = (String) auth.getPrincipal();
        try{
            playlistService.delete(idPlaylist,loggedEmail);
            return Response.ok().build();
        } catch (Exception e){
            return Response.status(Response.Status.FORBIDDEN).build();
        }
    }
    *
    *
    *
    *
    * */


    /********************************************
     *              metodos asincronos
     *******************************************/

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
        Type listSongDTOType = new TypeToken<List<SongDTO>>(){}.getType(); //armo el tipo lista de songsDTO (sin el id)
        playlistService.getNameByIdAsync(id).thenAccept((res) ->{
            if(res != null){
                playlistWithSongsDTO.setPlaylistName(res);  // le seteo el nombre de la playlist
                playlistService.getSongsByPlaylistIdAsync(id).thenAccept((list) -> {
                    List<SongDTO> listSongsDTO = modelMapper.map(list,listSongDTOType); // creo la lista de songsdto
                    playlistWithSongsDTO.setSongs(listSongsDTO);
                    response.resume(Response.ok(playlistWithSongsDTO).build()) ;
                });
            }
            else{
                response.resume(Response.status(Response.Status.NOT_FOUND).build());
            }
        });
    }

    // modificar el nombre de una playlist
    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public void updatePlaylist (@Suspended AsyncResponse response,@PathParam("id")long id,PlaylistDTO playlistDTO){
        try{
            playlistService.updateAsync(id,playlistDTO,utils.getEmailLogged()).thenAccept((res)->{
                if(res!=null){
                    response.resume(Response.ok().build());
                }
                else{
                    response.resume(Response.status(Response.Status.NOT_FOUND).build());
                }
            });
        } catch (Exception e){
            response.resume(Response.status(Response.Status.FORBIDDEN).build());
        }
    }


    // metodo asincrono para agregar una cancion a una playlist
    @POST
    @Path("/{id}/songs")
    @Consumes(MediaType.APPLICATION_JSON)
    public void addSongInPlaylist(@Suspended AsyncResponse response,@PathParam("id") long idPlaylist, PlaylistsSongsDTO playlistsSongsDTO){
        Long idSong = Long.valueOf(playlistsSongsDTO.getId());
        try {
            playlistService.addSongAsync(idPlaylist,idSong,utils.getEmailLogged()).thenAccept((res) ->{
                if(res!=null){
                    response.resume(Response.status(Response.Status.CREATED).build());
                }
                else{
                    response.resume(Response.status(Response.Status.NOT_FOUND).build());
                }
            });
        } catch (Exception e) {
            response.resume(Response.status(Response.Status.FORBIDDEN).build());
        }
    }

    @DELETE
    @Path("/{idPlaylist}/songs/{idSong}")
    public void deleteSongInPlaylist(@Suspended AsyncResponse response,@PathParam("idPlaylist") long idPlaylist,@PathParam("idSong") long idSong){
        try{
            playlistService.deleteSongAsync(idPlaylist,idSong, utils.getEmailLogged()).thenAccept((res) ->{
                if(res != null){
                    response.resume(Response.ok().build());
                }
                else{
                    response.resume(Response.status(Response.Status.NOT_FOUND).build());
                }
            });
        }catch(Exception e){
            response.resume(Response.status(Response.Status.FORBIDDEN).build());
        }
    }

    @DELETE
    @Path("/{idPlaylist}")
    public void delete(@Suspended AsyncResponse response,@PathParam("idPlaylist") long idPlaylist){
        try{
            playlistService.deletePlaylistAsync(idPlaylist, utils.getEmailLogged()).thenAccept((res)->{
               if(res != null){
                   response.resume(Response.ok().build());
               }
               else{
                   response.resume(Response.status(Response.Status.NOT_FOUND).build());
               }
            });
        } catch (Exception e){
            response.resume(Response.status(Response.Status.FORBIDDEN).build());
        }
    }

}
