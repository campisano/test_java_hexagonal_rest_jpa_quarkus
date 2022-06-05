package org.example.application.usecases;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.example.application.dtos.BookDTO;
import org.example.application.ports.MockedBooksRepositoryPort;
import org.example.application.ports.in.ListAllBooksUseCasePort;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TestListAllBooksUseCase {

    MockedBooksRepositoryPort booksRepository;
    ListAllBooksUseCasePort listllBooksUseCase;

    @BeforeEach
    void beforeEach() {
        booksRepository = new MockedBooksRepositoryPort();
        listllBooksUseCase = new ListAllBooksUseCase(booksRepository);
    }

    @Test
    void when_none_then_returns_empty_list() throws Exception {
        booksRepository.findAll_out = new ArrayList<BookDTO>();

        List<BookDTO> books = listllBooksUseCase.execute();

        Assertions.assertNotNull(books);
        Assertions.assertEquals(0, books.size());
    }

    @Test
    void when_some_then_returns_all() throws Exception {
        BookDTO book1 = new BookDTO("isbn1", "title1", new HashSet<>(Arrays.asList("author1")), "description1");
        BookDTO book2 = new BookDTO("isbn2", "title2", new HashSet<>(Arrays.asList("author2")), "description2");
        booksRepository.findAll_out = Arrays.asList(book1, book2);

        List<BookDTO> books = listllBooksUseCase.execute();

        Assertions.assertNotNull(books);
        Assertions.assertEquals(2, books.size());
        Assertions.assertEquals(books.get(0).toString(), book1.toString());
        Assertions.assertEquals(books.get(1).toString(), book2.toString());
    }
}