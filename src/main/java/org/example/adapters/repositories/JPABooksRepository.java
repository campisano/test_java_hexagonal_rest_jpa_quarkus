package org.example.adapters.repositories;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.inject.Singleton;
import javax.transaction.Transactional;

import org.example.adapters.repositories.models.AuthorModel;
import org.example.adapters.repositories.models.BookModel;
import org.example.adapters.repositories.models.translators.BookModelTranslator;
import org.example.application.dtos.BookDTO;
import org.example.application.ports.out.BooksRepositoryPort;

import io.quarkus.hibernate.orm.panache.PanacheRepository;

@Singleton
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
        var authors = new HashSet<AuthorModel>(authorsRepository.findByNameIn(dto.getAuthors()));
        var model = BookModelTranslator.fromDTO(dto, authors);

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
        var optModel = booksRepository.findByIsbn(isbn);

        if (!optModel.isPresent()) {
            return Optional.<BookDTO>empty();
        }

        return Optional.of(BookModelTranslator.toDTO(optModel.get()));
    }
}

@Singleton
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
