package org.example.adapters.controllers;

import java.util.Optional;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.Request;

import org.example.application.dtos.AuthorDTO;
import org.example.application.exceptions.AuthorAlreadyExistsException;
import org.example.application.exceptions.AuthorInvalidException;
import org.example.application.ports.in.AddAuthorUseCasePort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/v1/authors")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class HTTPAuthorsController {
	private static final Logger LOGGER = LoggerFactory.getLogger(HTTPAuthorsController.class);

	private AddAuthorUseCasePort addAuthorUseCase;

	public HTTPAuthorsController(AddAuthorUseCasePort addAuthorUseCase) {
		this.addAuthorUseCase = addAuthorUseCase;
	}

	@POST
	public Response add(@Context Request request, @Context UriInfo uriInfo, Optional<AddAuthorRequest> body) {
		LOGGER.info("method={}, path={}, body={}", request.getMethod(), uriInfo.getRequestUri(), body);

		if (!body.isPresent()) {
			LOGGER.error("request without body");
			return Response.status(Response.Status.BAD_REQUEST).entity(null).build();
		}

		try {
			AuthorDTO author = addAuthorUseCase.execute(new AuthorDTO(body.get().name));
			LOGGER.info("created, author={}", author);
			return Response.status(Response.Status.CREATED).entity(author).build();
		} catch (AuthorInvalidException | AuthorAlreadyExistsException exception) {
			LOGGER.error("exception, message={}", exception.getMessage());
			return Response.status(422).entity(null).build();
		}
	}
}

class AddAuthorRequest {
	public String name;

	@Override
	public String toString() {
		return "AddAuthorRequest [name=" + name + "]";
	}
}