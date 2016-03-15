package genericdao.jpa;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import com.google.common.base.Preconditions;

class JPQLBuilder {

	private Map<String, List<String>> mapQueryPartsAndNamedParameters;

	private static final Pattern PARAMETER_PATTERN = Pattern.compile(":(\\w+)");

	JPQLBuilder(List<String> queryParts) {
		this.mapQueryPartsAndNamedParameters = JPQLBuilder.loadQueryPartsAndNamedParameters(queryParts);
	}

	JPQLBuilder(String[] queryParts) {
		this.mapQueryPartsAndNamedParameters = JPQLBuilder.loadQueryPartsAndNamedParameters(Arrays.asList(queryParts));
	}

	private static Map<String, List<String>> loadQueryPartsAndNamedParameters(List<String> queryParts) {
		Map<String, List<String>> pMapQueryPartsAndNamedParameters = new LinkedHashMap<String, List<String>>();
		for (String queryPart : queryParts) {

			pMapQueryPartsAndNamedParameters.put(queryPart, JPQLBuilder.findParameters(queryPart));
		}
		return pMapQueryPartsAndNamedParameters;
	}

	<T> TypedQuery<T> build(Map<String, Object> parameters, EntityManager entityManager, Class<T> clazz) {

		Preconditions.checkNotNull(parameters, "parameters must not be null.");
		Preconditions.checkNotNull(entityManager, "entityManager must not be null.");
		Map<String, Object> unmodifiableParameters = Collections.unmodifiableMap(parameters);

		String jpql = buildDynamicQueryString(unmodifiableParameters);

		TypedQuery<T> typedQuery = entityManager.createQuery(jpql, clazz);

		for (Entry<String, Object> parameter : parameters.entrySet()) {
			typedQuery.setParameter(parameter.getKey(), parameter.getValue());
		}

		return typedQuery;
	}

	private String buildDynamicQueryString(Map<String, Object> parameters) {
		StringBuilder builder = new StringBuilder();
		for (Entry<String, List<String>> queryPartAndParameters : mapQueryPartsAndNamedParameters.entrySet()) {
			if (parameters.keySet().containsAll(queryPartAndParameters.getValue())) {
				builder.append(queryPartAndParameters.getKey()).append(" ");
			}
		}
		return builder.toString();
	}

	private static List<String> findParameters(String queryPart) {

		final List<String> parameters = new ArrayList<String>();

		Matcher matcher = PARAMETER_PATTERN.matcher(queryPart);

		while (matcher.find()) {
			parameters.add(matcher.group().substring(1));
		}
		return parameters;
	}

}
