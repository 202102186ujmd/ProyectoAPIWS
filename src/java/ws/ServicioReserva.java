package ws;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import modelo.dao.daoReservaciones;
import modelo.entidad.Reservaciones;

@Path("Reservas")
public class ServicioReserva {

    @Context
    private UriInfo context;

    public ServicioReserva() {
    }

    private boolean validarToken(String token) {
        if (token == null || token.isEmpty()) {
            return false;
        }

        try {
            Jwts.parser().setSigningKey("miClave").parseClaimsJws(token);
            return true;
        } catch (SignatureException ex) {
            return false;
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getListaReservas(@HeaderParam("Authorization") String token) {
        if (!validarToken(token)) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("Invalid or missing token").build();
        }

        daoReservaciones dc = new daoReservaciones();
        return Response.ok(dc.getAll()).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response insertarReserva(@HeaderParam("Authorization") String token, Reservaciones reserva) {
        if (!validarToken(token)) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("Invalid or missing token").build();
        }

        daoReservaciones dc = new daoReservaciones();
        int resultado = dc.insertarReservacion(reserva);
        if (resultado > 0) {
            return Response.status(Response.Status.CREATED).entity("Reservation created successfully").build();
        } else {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error creating reservation").build();
        }
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response actualizarReserva(@HeaderParam("Authorization") String token, Reservaciones reserva) {
        if (!validarToken(token)) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("Invalid or missing token").build();
        }
        
        System.out.println("Token válido: " + token);
        System.out.println("Actualizando reserva: " + reserva);

        daoReservaciones dc = new daoReservaciones();
        int resultado = dc.actualizarReservacion(reserva);
        if (resultado > 0) {
            System.out.println("Reserva actualizada en la base de datos: " + reserva);
            return Response.status(Response.Status.OK).entity("Reservation updated successfully").build();
        } else {
            System.out.println("Error al actualizar la reserva: " + reserva);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error updating reservation").build();
        }
    }

    @DELETE
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response eliminarReserva(@HeaderParam("Authorization") String token, @PathParam("id") int id) {
        if (!validarToken(token)) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("Invalid or missing token").build();
        }

        daoReservaciones dc = new daoReservaciones();
        int resultado = dc.eliminarReservacion(id);
        if (resultado > 0) {
            return Response.status(Response.Status.OK).entity("Reservation deleted successfully").build();
        } else {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error deleting reservation").build();
        }
    }
}
