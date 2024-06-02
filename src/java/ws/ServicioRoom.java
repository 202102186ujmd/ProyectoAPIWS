package ws;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.Base64;
import modelo.dao.daoHabitaciones;
import modelo.entidad.Habitaciones;

@Path("Habitaciones")
public class ServicioRoom {

    @Context
    private UriInfo context;

    public ServicioRoom() {
    }

    private boolean validarToken(String token) {
        if (token == null || token.isEmpty()) {
            return false;
        }

        try {
            String[] datos = token.split("\\.");
            Base64.Decoder decodificador = Base64.getUrlDecoder();
            String payload = new String(decodificador.decode(datos[1]));
            System.out.println("Payload: " + payload);

            Jwts.parser().setSigningKey("miClave").parseClaimsJws(token);
            return true;
        } catch (SignatureException ex) {
            return false;
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getListaHabitaciones(@HeaderParam("Authorization") String token) {
        if (!validarToken(token)) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("Invalid or missing token").build();
        }

        daoHabitaciones dc = new daoHabitaciones();
        return Response.ok(dc.getAll()).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response insertarHabitacion(@HeaderParam("Authorization") String token, Habitaciones habitacion) {
        if (!validarToken(token)) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("Invalid or missing token").build();
        }

        daoHabitaciones dc = new daoHabitaciones();
        int resultado = dc.insertarHabitacion(habitacion);
        if (resultado > 0) {
            return Response.status(Response.Status.CREATED).entity("Room created successfully").build();
        } else {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error creating room").build();
        }
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response actualizarHabitacion(@HeaderParam("Authorization") String token, Habitaciones habitacion) {
        if (!validarToken(token)) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("Invalid or missing token").build();
        }

        daoHabitaciones dc = new daoHabitaciones();
        int resultado = dc.actualizarHabitacion(habitacion);
        if (resultado > 0) {
            return Response.status(Response.Status.OK).entity("Room updated successfully").build();
        } else {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error updating room").build();
        }
    }

    @DELETE
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response eliminarHabitacion(@HeaderParam("Authorization") String token, @PathParam("id") int id) {
        if (!validarToken(token)) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("Invalid or missing token").build();
        }

        daoHabitaciones dc = new daoHabitaciones();
        int resultado = dc.eliminarHabitacion(id);
        if (resultado > 0) {
            return Response.status(Response.Status.OK).entity("Room deleted successfully").build();
        } else {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error deleting room").build();
        }
    }
}
