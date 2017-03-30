# Generic-Dao

### DEPRECATED!!!

Guys, this is an old Framework now, back in the day, the Criteria API was horrible (still is!) and developers preferred to venture in very obscure ways with if statements to build their dynamic queries with JPA, this framework helped to keep the cyclomatic complexity down without the use of Criteria API or opinionate too much in the code. 
Today frameworks like Spring Data and queryDSL provide everything you need from Query Methods (queries based on the name of the method) to typesafe queries, so prefer to use those frameworks over this one.

# Description

A very simple Framework that helps you build your dynamic queries, this is for those who hate the Criteria API and prefer to use if statements to build their queries, making the cyclomatic complexity go crazy.


Old way:

```java
List<User> query(String p1, String p2, String p3, String p4) {
		String sql = "Select u from User u where 1=1";

		if (p1 != null) {
			sql += " AND u.p1 = :p1";
		}
		if (p2 != null) {
			sql += " AND u.p2 = :p2";
		}
		if (p3 != null) {
			sql += " AND u.p3 = :p3";
		}
		if (p4 != null) {
			sql += " AND u.p4 = :p4";
		}

		TypedQuery<User> query = entityManager.createQuery(sql, User.class);

		if (p1 != null) {
			query.setParameter("p1", p1);
		}
		if (p2 != null) {
			query.setParameter("p2", p2);
		}
		if (p3 != null) {
			query.setParameter("p3", p3);
		}
		if (p4 != null) {
			query.setParameter("p4", p4);
		}

		return query.getResultList();

	}
```
Now just annotate your DAO with @NamedQueries, and put all your queries splitted in the query parts like the follow:

```java
@NamedQueries({
	
	@NamedQuery(
	name = "User.Find", 
	query = { "Select u from User u where 1=1",
			"AND u.p1 = :p1", "AND u.p2 = :p2",
			"AND u.p3 = :p3", "AND u.p4 = :p4" }),
	})

public class UserDAO extends JpaDAO<User> {}

```
And then use it like so:
```java
private DAO<User> userDAO = new UserDAO();

List<User> query(String p1, String p2, String p3, String p4) {

		Map<String, Object> mapParameters = new HashMap<String, Object>();
		mapParameters.put("p1", p1);
		mapParameters.put("p2", p2);
		mapParameters.put("p3", p3);
		mapParameters.put("p4", p4);

		return userDAO.getJPQLQueryResultList("User.Find", mapParameters, User.class);

	}
```

No more crazy cyclomatic complexity, no criteria API either;
