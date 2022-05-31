package org.example.adapters.repositories;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;

import org.example.adapters.repositories.models.AuthorModel;
import org.example.adapters.repositories.models.BookModel;
import org.example.adapters.repositories.models.translators.BookModelTranslator;
import org.example.application.dtos.BookDTO;
import org.example.application.ports.out.BooksRepositoryPort;

import io.quarkus.hibernate.orm.panache.PanacheRepository;

@ApplicationScoped
public class JPABooksRepository implements BooksRepositoryPort {

	private PanacheBooksRepository booksRepository;
	private PanacheAuthorsRepository authorsRepository;

	public JPABooksRepository(PanacheBooksRepository booksRepository, PanacheAuthorsRepository authorsRepository) {
		this.booksRepository = booksRepository;
		this.authorsRepository = authorsRepository;
	}

	@Transactional
	@Override
	public BookDTO create(BookDTO dto) {
		Set<AuthorModel> authors = new HashSet<AuthorModel>(authorsRepository.findByNameIn(dto.getAuthors()));
		BookModel model = BookModelTranslator.fromDTO(dto, authors);

		model = booksRepository.save(model);

		return BookModelTranslator.toDTO(model);
	}

	@Override
	public List<BookDTO> findAll() {

		return booksRepository.findAll().list().stream().map(book -> BookModelTranslator.toDTO(book))
				.collect(Collectors.toList());
	}

	@Override
	public Optional<BookDTO> findByIsbn(String isbn) {
		Optional<BookModel> optModel = booksRepository.findByIsbn(isbn);

		if (!optModel.isPresent()) {
			return Optional.<BookDTO>empty();
		}

		return Optional.of(BookModelTranslator.toDTO(optModel.get()));
	}
}

@ApplicationScoped
class PanacheBooksRepository implements PanacheRepository<BookModel> {
	public Optional<BookModel> findByIsbn(String isbn) {
		return find("isbn", isbn).singleResultOptional();
	}

	public BookModel save(BookModel model) {
		persist(model);
		flush();
		return model;
	}
}
