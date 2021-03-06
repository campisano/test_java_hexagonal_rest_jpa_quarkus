package org.example;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.example.application.ports.out.AuthorsRepositoryPort;
import org.example.application.ports.out.BooksRepositoryPort;
import org.example.application.usecases.AddAuthorUseCase;
import org.example.application.usecases.AddBookUseCase;
import org.example.application.usecases.GetBookUseCase;
import org.example.application.usecases.ListAllBooksUseCase;

@Singleton
class InjectionProvider {

    private AddBookUseCase addBookUseCase;
    private GetBookUseCase getBookUseCase;
    private ListAllBooksUseCase listAllBooksUseCase;
    private AddAuthorUseCase addAuthorUseCase;

    @Inject
    public InjectionProvider(AuthorsRepositoryPort authorsRepository, BooksRepositoryPort booksRepository) {
        addBookUseCase = new AddBookUseCase(booksRepository, authorsRepository);
        getBookUseCase = new GetBookUseCase(booksRepository);
        listAllBooksUseCase = new ListAllBooksUseCase(booksRepository);
        addAuthorUseCase = new AddAuthorUseCase(authorsRepository);
    }

    @Produces
    public AddBookUseCase getAddBookUseCase() {
        return addBookUseCase;
    }

    @Produces
    public GetBookUseCase getGetBookUseCase() {
        return getBookUseCase;
    }

    @Produces
    public ListAllBooksUseCase getListAllBooksUseCase() {
        return listAllBooksUseCase;
    }

    @Produces
    public AddAuthorUseCase getAddAuthorUseCase() {
        return addAuthorUseCase;
    }
}
