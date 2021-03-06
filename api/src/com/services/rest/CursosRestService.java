package com.services.rest;

import java.net.URI;
import java.net.URISyntaxException;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.UriInfo;

import com.ejb.intf.CursosSession;
import com.model.dto.CursoDTO;
import com.model.ejb.entity.Curso;

@Named("CursoRest")
@RequestScoped
@Path("cursos")
public class CursosRestService {

	@EJB
	private CursosSession session;
	
	@Context
	UriInfo uriInfo;

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	public Response get() {		
		return Response.ok().entity(Mappers.converterLista(session.buscaTodos(), (c) -> Mappers.converteParaDTO(c))).build();
	}

	@GET
	@Path("/{id}")
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getPorId(@PathParam("id") Long id) {
		ResponseBuilder r = null;		
		Curso c = session.buscaPorId(id);
		if (c != null) {
			r = Response.ok().entity(Mappers.converteParaDTO(c));			
		} else {
			r = Response.status(Response.Status.NOT_FOUND).entity("Curso nao encontrado");
		}
		return r.build();
	}
	
	@GET
	@Path("/{id}/disciplinas")
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getDisciplinas(@PathParam("id") Long id) {
		ResponseBuilder r = null;		
		Curso c = session.buscaPorId(id);
		if (c != null) {
			r = Response.ok().entity(Mappers.converterLista(session.buscaDisciplinas(c), d -> Mappers.converteParaDTO(d)));			
		} else {
			r = Response.status(Response.Status.NOT_FOUND).entity("Curso nao encontrado");
		}
		return r.build();
	}

	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/{id}")
	public Response altera(@PathParam("id") Long id, CursoDTO dto) {
		Curso p = Mappers.converteParaEntidade(dto);
		p.setId(id);
		session.altera(p);
		return Response.ok().build();
	}
	
	@DELETE
	@Path("/{id}")
	public Response remove(@PathParam("id") Long id) {
		Curso c = session.buscaPorId(id);
		if (c != null) { 
			session.remove(c);
		}
		return Response.ok().build();
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response insere(CursoDTO dto) {
		Curso c = Mappers.converteParaEntidade(dto);
		session.insere(c);
		if (c != null && c.getId() != null) {
			try {
				URI uri = new URI(uriInfo.getAbsolutePath().toString() + "/" + c.getId());
				return Response.created(uri).build();
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}		
		}
		
		return Response.noContent().build();
	}
}
