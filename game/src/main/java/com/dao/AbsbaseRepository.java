package com.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;


public abstract class AbsbaseRepository<T, ID> {


    private CrudRepository<T, ID> baseRepository;

    public AbsbaseRepository(CrudRepository<T, ID> crudRepository) {
        this.baseRepository = crudRepository;
    }

    public <S extends T> S save(S entity) {
        return baseRepository.save(entity);
    }


    public <S extends T> Iterable<S> saveAll(Iterable<S> entities) {
        return baseRepository.saveAll(entities);
    }


    public Optional<T> findById(ID aLong) {
        return baseRepository.findById(aLong);
    }

    public boolean existsById(ID aLong) {
        return baseRepository.existsById(aLong);
    }


    public Iterable<T> findAll() {
        return baseRepository.findAll();
    }


    public Iterable<T> findAllById(Iterable<ID> longs) {
        return baseRepository.findAllById(longs);
    }


    public long count() {
        return baseRepository.count();
    }


    public void deleteById(ID aLong) {
        baseRepository.deleteById(aLong);
    }


    public void delete(T entity) {
        baseRepository.delete(entity);
    }


    public void deleteAll(Iterable<? extends T> entities) {
        baseRepository.deleteAll(entities);
    }


    public void deleteAll() {
        baseRepository.deleteAll();
    }
}
