package genericdao.util;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;

import genericdao.annotations.NamedQueries;
import genericdao.annotations.NamedQuery;

public class UtilReflection {

	private UtilReflection() {

	}

	public static List<NamedQuery> loadDynamicQueries(final Class<?> clazz) {

		List<NamedQuery> namedQueriesList = new LinkedList<NamedQuery>();

		if (clazz.isAnnotationPresent(NamedQueries.class)) {

			NamedQueries namedQueries = clazz.getAnnotation(NamedQueries.class);

			for (NamedQuery namedQuery : namedQueries.value()) {
				namedQueriesList.add(namedQuery);
			}

		}
		return namedQueriesList;
	}

	public static Class<?> getGenericParameterClass(final Class<?> clazz, final Integer index) {

		Type[] superClassTypes = null;

		Type genericSuperclass = clazz.getGenericSuperclass();

		if (genericSuperclass instanceof ParameterizedType) {
			ParameterizedType parameterizedType = (ParameterizedType) genericSuperclass;

			superClassTypes = parameterizedType.getActualTypeArguments();
		}
		return (Class<?>) superClassTypes[index];
	}

}
