package genericdao.util;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import genericdao.annotations.DynamicNamedQueries;
import genericdao.annotations.DynamicNamedQuery;

public class UtilReflection {

    private UtilReflection() {

    }

    public static Map<String, List<String>> carregaDynamicQueries(final Class<?> clazz) {

        Map<String, List<String>> dynamicJPQLQueries = new LinkedHashMap<String, List<String>>();
        DynamicNamedQueries dynamicNamedQueries;
        if (clazz.isAnnotationPresent(DynamicNamedQueries.class)) {

            dynamicNamedQueries = clazz.getAnnotation(DynamicNamedQueries.class);

            DynamicNamedQuery[] value = dynamicNamedQueries.value();
            for (DynamicNamedQuery dynamicNamedQuery : value) {
                dynamicJPQLQueries.put(dynamicNamedQuery.name(), Arrays.asList(dynamicNamedQuery.query()));
            }

        }

        return dynamicJPQLQueries;
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
