package ws;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import modelo.dao.daoUsuario;
import modelo.entidad.Usuario;

@Path("Usuarios")
public class ServicioUsuario {

    @Context
    private UriInfo context;

    public ServicioUsuario() {
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
    public Response getListaUsuarios(@HeaderParam("Authorization") String token) {
        if (!validarToken(token)) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("Invalid or missing token").build();
        }

        daoUsuario du = new daoUsuario();
        return Response.ok(du.getAll()).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response insertarUsuario(@HeaderParam("Authorization") String token, Usuario usuario) {
        if (!validarToken(token)) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("Invalid or missing token").build();
        }

        daoUsuario du = new daoUsuario();
        int resultado = du.insertarUsuario(usuario);
        if (resultado > 0) {
            return Response.status(Response.Status.CREATED).entity("User created successfully").build();
        } else {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error creating user").build();
        }
    }

    @PUT
    @Path("{username}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response actualizarUsuario(@HeaderParam("Authorization") String token, @PathParam("username") String username, Usuario usuario) {
        if (!validarToken(token)) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("Invalid or missing token").build();
        }

        daoUsuario du = new daoUsuario();
        int resultado = du.actualizarUsuario(username, usuario);
        if (resultado > 0) {
            return Response.status(Response.Status.OK).entity("User updated successfully").build();
        } else {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error updating user").build();
        }
    }

    @DELETE
    @Path("{username}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response eliminarUsuario(@HeaderParam("Authorization") String token, @PathParam("username") String username) {
        if (!validarToken(token)) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("Invalid or missing token").build();
        }

        daoUsuario du = new daoUsuario();
        int resultado = du.eliminarUsuario(username);
        if (resultado > 0) {
            return Response.status(Response.Status.OK).entity("User deleted successfully").build();
        } else {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error deleting user").build();
        }
    }
}
