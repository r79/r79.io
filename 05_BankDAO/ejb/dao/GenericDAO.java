package dao;

import java.util.List;

public interface GenericDAO<T> {
	T findById(Long id);

	List<T> findAll();

	T save(T entity);

	T delete(T entity);

	long count();
}
