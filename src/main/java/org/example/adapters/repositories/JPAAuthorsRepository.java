package org.example.adapters.repositories;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.inject.Singleton;
import javax.transaction.Transactional;

import org.example.adapters.repositories.models.AuthorModel;
import org.example.adapters.repositories.models.translators.AuthorModelTranslator;
import org.example.application.dtos.AuthorDTO;
import org.example.application.ports.out.AuthorsRepositoryPort;

import io.quarkus.hibernate.orm.panache.PanacheRepository;

@Singleton
public class JPAAuthorsRepository implements AuthorsRepositoryPort {

    private PanacheAuthorsRepository authorsRepository;

    public JPAAuthorsRepository(PanacheAuthorsRepository authorsRepository) {
        this.authorsRepository = authorsRepository;
    }

    @Transactional
    @Override
    public AuthorDTO create(AuthorDTO dto) {
        var model = AuthorModelTranslator.fromDTO(dto);

        model = authorsRepository.save(model);

        return AuthorModelTranslator.toDTO(model);
    }

    @Override
    public Optional<AuthorDTO> findByName(String name) {
        var optModel = authorsRepository.findByName(name);

        if (!optModel.isPresent()) {
            return Optional.<AuthorDTO>empty();
        }

        return Optional.of(AuthorModelTranslator.toDTO(optModel.get()));
    }

    @Override
    public Set<AuthorDTO> findByNameIn(Set<String> authorNames) {
        return AuthorModelTranslator.toDTO(new HashSet<AuthorModel>(authorsRepository.findByNameIn(authorNames)));
    }
}

@Singleton
class PanacheAuthorsRepository implements PanacheRepository<AuthorModel> {
    public Optional<AuthorModel> findByName(String name) {
        return find("name", name).singleResultOptional();
    }

    public AuthorModel save(AuthorModel model) {
        persist(model);
        flush();

        return model;
    }

    public List<AuthorModel> findByNameIn(Set<String> authors) {
        return find("name IN ?1", authors).list();
    }
}
