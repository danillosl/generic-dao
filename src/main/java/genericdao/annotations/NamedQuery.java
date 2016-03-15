package genericdao.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import genericdao.util.QueryType;

@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface NamedQuery {

	String name();

	QueryType type() default QueryType.DYNAMIC;

	String[] query();

}