package ws;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.Base64;
import modelo.dao.daoReservaciones;

@Path("Reservas")
public class ServicioReserva {

    @Context
    private UriInfo context;

    public ServicioReserva() {
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getListaReservas(@HeaderParam("Authorization") String token) {
        if (token == null || token.isEmpty()) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("Token is missing").build();
        }

        String[] datos = token.split("\\.");
        Base64.Decoder decodificador = Base64.getUrlDecoder();
        try {
            Jwts.parser().setSigningKey("miClave").parseClaimsJws(token);
            System.out.println("Autorizado");
            System.out.println("Payload: " + new String(decodificador.decode(datos[1])));
            daoReservaciones dc = new daoReservaciones();
            return Response.ok(dc.getAll()).build();
        } catch (SignatureException ex) {
            System.out.println("No Autorizado: " + ex.getMessage());
            return Response.status(Response.Status.UNAUTHORIZED).entity("Invalid token").build();
        } catch (Exception ex) {
            System.out.println("Error: " + ex.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("An error occurred").build();
        }
    }
}
