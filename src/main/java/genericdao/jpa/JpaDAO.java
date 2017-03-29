package genericdao.jpa;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import genericdao.DAO;
import genericdao.annotations.NamedQuery;
import genericdao.util.UtilReflection;

public class JpaDAO<T> implements DAO<T> {

	private Class<T> genericParameterClass;

	@PersistenceContext
	private EntityManager entityManager;

	private Map<String, JPQLQueryTemplate> dynamicJPQLQueries;

	@SuppressWarnings("unchecked")
	protected JpaDAO() {
		this.genericParameterClass = (Class<T>) UtilReflection.getGenericParameterClass(this.getClass());
		List<NamedQuery> namedQueries = UtilReflection.getNamedQueries(this.getClass());
		this.dynamicJPQLQueries = this.processJPQLBuilders(namedQueries);
	}

	private Map<String, JPQLQueryTemplate> processJPQLBuilders(List<NamedQuery> NamedQueries) {
		Map<String, JPQLQueryTemplate> dynamicJPQLQueries = new LinkedHashMap<String, JPQLQueryTemplate>();
		for (NamedQuery namedQuery : NamedQueries) {
			dynamicJPQLQueries.put(namedQuery.name(), new JPQLQueryTemplate(namedQuery));
		}
		return dynamicJPQLQueries;
	}

	public List<T> findAll() {
		return entityManager.createQuery("from " + genericParameterClass.getName(), genericParameterClass).getResultList();
	}

	public List<T> findAll(Integer startPosition, Integer maxResult) {
		return entityManager.createQuery("from " + genericParameterClass.getName(), genericParameterClass)
				.setFirstResult((startPosition - 1) * maxResult).setMaxResults(maxResult).getResultList();
	}

	public void create(final T entity) {
		entityManager.persist(entity);
	}

	public T update(final T entity) {
		return entityManager.merge(entity);
	}

	public void delete(final T entity) {
		entityManager.remove(entityManager.contains(entity) ? entity : this.update(entity));
	}

	public <P extends Serializable> void deleteById(P entityId) {
		final T entity = findOne(entityId);
		delete(entity);
	}

	public <P extends Serializable> T findOne(final P id) {

		return entityManager.find(genericParameterClass, id);
	}

	public <T2> T2 getNamedQuerySingleResult(String namedQuery, Map<String, Object> parameters, Class<T2> returnClass) {
		TypedQuery<T2> typedQuery = entityManager.createNamedQuery(namedQuery, returnClass);
		for (Entry<String, Object> parameter : parameters.entrySet()) {
			typedQuery.setParameter(parameter.getKey(), parameter.getValue());
		}
		return typedQuery.getSingleResult();
	}

	public <T2> List<T2> getNamedQueryResultList(String namedQuery, Map<String, Object> parameters,
			Class<T2> returnClass) {

		TypedQuery<T2> typedQuery = entityManager.createNamedQuery(namedQuery, returnClass);
		for (Entry<String, Object> parameter : parameters.entrySet()) {
			typedQuery.setParameter(parameter.getKey(), parameter.getValue());
		}
		return typedQuery.getResultList();
	}

	public <T2> T2 getJPQLQuerySingleResult(String namedQuery, Map<String, Object> parameters, Class<T2> returnClass) {
		return (T2) dynamicJPQLQueries.get(namedQuery).build(parameters, entityManager, returnClass).getSingleResult();

	}

	public <T2> List<T2> getJPQLQueryResultList(String namedQuery, Map<String, Object> parameters,
			Class<T2> returnClass) {
		return dynamicJPQLQueries.get(namedQuery).build(parameters, entityManager, returnClass).getResultList();

	}

	protected EntityManager getEntityManager() {
		return entityManager;
	}

	protected void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

}