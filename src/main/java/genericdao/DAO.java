package genericdao;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public interface DAO<T> {

	List<T> findAll();

	List<T> findAll(Integer startPosition, Integer maxResult);

	void create(T entity);

	T update(T entity);

	void delete(T entity);

	<P extends Serializable> T findOne(P id);

	<P extends Serializable> void deleteById(P id);

	<T2> T2 getNamedQuerySingleResult(String namedQuery, Map<String, Object> parameters, Class<T2> returnClass);

	<T2> List<T2> getNamedQueryResultList(String namedQuery, Map<String, Object> parameters, Class<T2> returnClass);

	<T2> T2 getJPQLQuerySingleResult(String namedQuery, Map<String, Object> parameters, Class<T2> returnClass);

	<T2> List<T2> getJPQLQueryResultList(String namedQuery, Map<String, Object> parameters, Class<T2> returnClass);
	
	

}
