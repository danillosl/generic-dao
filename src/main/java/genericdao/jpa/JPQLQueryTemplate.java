package genericdao.jpa;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import com.google.common.base.Preconditions;

import genericdao.annotations.NamedQuery;

class JPQLQueryTemplate {

	private Map<String, List<String>> mapQueryPartsAndNamedParameters;

	private static final Pattern PARAMETER_PATTERN = Pattern.compile(":(\\w+)");

	JPQLQueryTemplate(NamedQuery namedQuery) {

		for (String queryPart : namedQuery.query()) {

			this.mapQueryPartsAndNamedParameters.put(queryPart, findParameters(queryPart));
		}
	}

	<T> TypedQuery<T> build(Map<String, Object> parameters, EntityManager entityManager, Class<T> clazz) {

		Preconditions.checkNotNull(parameters, "parameters must not be null.");
		Preconditions.checkNotNull(entityManager, "entityManager must not be null.");

		Map<String, Object> unmodifiableParameters = Collections.unmodifiableMap(parameters);

		String jpql = this.buildJPQL(unmodifiableParameters);

		TypedQuery<T> typedQuery = entityManager.createQuery(jpql, clazz);

		for (Entry<String, Object> parameter : parameters.entrySet()) {
			typedQuery.setParameter(parameter.getKey(), parameter.getValue());
		}

		return typedQuery;
	}

	private String buildJPQL(Map<String, Object> parameters) {
		StringBuilder builder = new StringBuilder();
		for (Entry<String, List<String>> queryPartAndParameters : mapQueryPartsAndNamedParameters.entrySet()) {
			if (parameters.keySet().containsAll(queryPartAndParameters.getValue())) {
				builder.append(queryPartAndParameters.getKey()).append(" ");
			}
		}
		return builder.toString();
	}

	private List<String> findParameters(String queryPart) {

		final List<String> parameters = new ArrayList<String>();

		Matcher matcher = PARAMETER_PATTERN.matcher(queryPart);

		while (matcher.find()) {
			parameters.add(matcher.group().substring(1));
		}
		return parameters;
	}

	public Map<String, List<String>> getMapQueryPartsAndNamedParameters() {
		return Collections.unmodifiableMap(mapQueryPartsAndNamedParameters);
	}

}
