# CodeQuest

CodeQuest is a Java 25, JSP, Servlet, JDBC, and MySQL learning game for Apache Tomcat 10.1. JSP pages use scriptlets and Java APIs; JSTL and JSP Expression Language are disabled.

## Requirements

- JDK 25
- Apache Maven 3.9+
- MySQL 8+
- Apache Tomcat 10.1

## Database setup

1. Run `db/schema.sql` in MySQL. It creates `codequest_db` and the four application tables.
2. Set database connection values in `src/main/webapp/WEB-INF/web.xml` (`dbUrl`, `dbUser`, and `dbPassword`) or override them with Tomcat context parameters.
3. Build the project, then generate an admin password hash from a system terminal with `java -cp target/classes com.codequest.util.PasswordHashTool`. The tool prompts without echoing the password. Insert an admin using the printed value:

   ```sql
   INSERT INTO admins (username, password_hash)
   VALUES ('admin', 'paste-the-generated-hash-here');
   ```

Admins and students both use PBKDF2 password hashes.

## Build and run

Import this folder into Eclipse as an **Existing Maven Project**, ensure the project uses JDK 25, and run `mvn clean package`. Add the project to a Tomcat 10.1 server in Eclipse, or deploy `target/codequest.war` to Tomcat's `webapps` directory. Open `http://localhost:8080/codequest/`.

The database settings are read from the web application's `ServletContext`. In Eclipse, update the three `context-param` values in `WEB-INF/web.xml` for your local MySQL account. Never commit production credentials.

## Routes

- `/register.jsp`, `/login.jsp`, `/admin-login.jsp`: account entry pages
- `/missions`, `/leaderboard`: public mission list and rankings
- `/dashboard`: signed-in student progress
- `/admin/dashboard`, `/admin/add-question`, `/admin/questions`: admin tools
- `/logout`, `/admin/logout`: invalidate the current session

Students get one attempt per mission, whether the answer is right or wrong. Correct submissions and XP changes are committed in one database transaction. Levels advance every 100 XP.