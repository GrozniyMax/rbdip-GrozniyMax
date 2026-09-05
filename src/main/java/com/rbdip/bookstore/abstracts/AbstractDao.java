package com.rbdip.bookstore.abstracts;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Базовая реализация DAO поверх {@link JpaRepository}, работающая с доменной
 * моделью вместо персистентной сущности. Все методы транслируют домен в
 * сущность (при записи) и обратно (при чтении) через {@link Mapper}.
 *
 * @param <DOMAIN> доменная модель (record)
 * @param <ENTITY> JPA-сущность
 * @param <ID>     тип идентификатора
 */
public abstract class AbstractDao<DOMAIN, ENTITY, ID> {

    private final JpaRepository<ENTITY, ID> repository;

    private final Mapper<DOMAIN, ENTITY> mapper;

    protected AbstractDao(JpaRepository<ENTITY, ID> repository, Mapper<DOMAIN, ENTITY> mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public List<DOMAIN> findAll() {
        return repository.findAll().stream().map(mapper::unmap).toList();
    }

    public List<DOMAIN> findAllById(Iterable<ID> ids) {
        return repository.findAllById(ids).stream().map(mapper::unmap).toList();
    }

    public Optional<DOMAIN> findById(ID id) {
        return repository.findById(id).map(mapper::unmap);
    }

    public boolean existsById(ID id) {
        return repository.existsById(id);
    }

    public long count() {
        return repository.count();
    }

    public DOMAIN save(DOMAIN domain) {
        ENTITY saved = repository.save(mapper.map(domain));
        return mapper.unmap(saved);
    }

    public List<DOMAIN> saveAll(Iterable<DOMAIN> domains) {
        List<ENTITY> entities = new ArrayList<>();
        for (DOMAIN domain : domains) {
            entities.add(mapper.map(domain));
        }
        return repository.saveAll(entities).stream().map(mapper::unmap).toList();
    }

    public void deleteById(ID id) {
        repository.deleteById(id);
    }

    public void delete(DOMAIN domain) {
        repository.delete(mapper.map(domain));
    }

    public void deleteAll() {
        repository.deleteAll();
    }
}