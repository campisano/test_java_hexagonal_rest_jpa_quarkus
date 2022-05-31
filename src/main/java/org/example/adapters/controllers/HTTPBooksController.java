package org.example.adapters.controllers;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.example.application.dtos.BookDTO;
import org.example.application.exceptions.AuthorInvalidException;
import org.example.application.exceptions.BookInvalidException;
import org.example.application.exceptions.IsbnAlreadyExistsException;
import org.example.application.exceptions.IsbnNotExistsException;
import org.example.application.ports.in.AddBookUseCasePort;
import org.example.application.ports.in.GetBookUseCasePort;
import org.example.application.ports.in.ListAllBooksUseCasePort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/v1/books")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class HTTPBooksController {
	private static final Logger LOGGER = LoggerFactory.getLogger(HTTPBooksController.class);

	private AddBookUseCasePort addBookUseCase;
	private GetBookUseCasePort getBookUseCase;
	private ListAllBooksUseCasePort listAllBooksUseCase;

	public HTTPBooksController(AddBookUseCasePort addBookUseCase, GetBookUseCasePort getBookUseCase,
			ListAllBooksUseCasePort listAllBooksUseCase) {
		this.addBookUseCase = addBookUseCase;
		this.getBookUseCase = getBookUseCase;
		this.listAllBooksUseCase = listAllBooksUseCase;
	}

	@POST
	public Response add(@Context Request request, @Context UriInfo uriInfo, Optional<AddBookRequest> body) {
		LOGGER.info("method={}, path={}, body={}", request.getMethod(), uriInfo.getRequestUri(), body);

		if (!body.isPresent()) {
			LOGGER.error("request without body");
			return Response.status(Response.Status.BAD_REQUEST).entity(null).build();
		}

		try {
			BookDTO book = addBookUseCase.execute(
					new BookDTO(body.get().isbn, body.get().title, body.get().authors, body.get().description));
			LOGGER.info("created, book={}", book);
			return Response.status(Response.Status.CREATED).entity(book).build();
		} catch (IsbnAlreadyExistsException | AuthorInvalidException | BookInvalidException exception) {
			LOGGER.error("exception, message={}", exception.getMessage());
			return Response.status(433).entity(null).build();
		}
	}

	@GET
	@Path("/{isbn}")
	public Response getByIsbn(@Context Request request, @Context UriInfo uriInfo, @PathParam("isbn") String isbn) {
		LOGGER.info("method={}, path={}, isbn={}", request.getMethod(), uriInfo.getRequestUri(), isbn);

		try {
			BookDTO book = getBookUseCase.execute(isbn);
			LOGGER.info("ok, isbn={}", isbn);
			return Response.status(Response.Status.OK).entity(book).build();
		} catch (IsbnNotExistsException exception) {
			LOGGER.error("exception, message={}", exception.getMessage());
			return Response.status(Response.Status.NOT_FOUND).entity(null).build();
		}
	}

	@GET
	public Response listAll(@Context Request request, @Context UriInfo uriInfo) {
		LOGGER.info("method={}, path={}", request.getMethod(), uriInfo.getRequestUri());

		List<BookDTO> books = listAllBooksUseCase.execute();
		LOGGER.info("ok");
		return Response.status(Response.Status.OK).entity(books).build();
	}
}

class AddBookRequest {
	public String isbn;
	public String title;
	public Set<String> authors;
	public String description;

	@Override
	public String toString() {
		return "AddBookRequest [isbn=" + isbn + ", title=" + title + ", authors=" + authors + ", description="
				+ description + "]";
	}
}