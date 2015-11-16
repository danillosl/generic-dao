package genericdao;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public interface DAO<T> {

    List<T> findAll();

    void create(T entity);

    T update(T entity);

    void delete(T entity);

    public <P extends Serializable> T findOne(final P id);

    public <P extends Serializable> void deleteById(P id);

    public <T2> T2 getNamedQuerySingleResult(String namedQuery, Map<String, Object> parameters, Class<T2> returnClass);

    public <T2> List<T2> getNamedQueryResultList(String namedQuery, Map<String, Object> parameters,
            Class<T2> returnClass);

    public <T2> T2 getJPQLQuerySingleResult(String namedQuery, Map<String, Object> parameters, Class<T2> returnClass);

    public <T2> List<T2> getJPQLQueryResultList(String namedQuery, Map<String, Object> parameters,
            Class<T2> returnClass);

}
