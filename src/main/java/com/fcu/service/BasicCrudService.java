package com.fcu.service;

/**
 * Define basic CRUD methods including insertion, deletion, update, and query.
 * @param <E> Entity type
 * @param <I> ID type
 */
public interface BasicCrudService<E, I> {
    public abstract E findOneById(I id);
    public abstract boolean insertOne(E entity);
    public abstract boolean updateOne(E entity);
    public abstract boolean deleteOne(E entity);
}
