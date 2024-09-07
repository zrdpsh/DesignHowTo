package InterfaceVsRealization;

import java.sql.*;

public class DatabaseStorage implements Storage {

    private static final String URL_DB = "jdbc:mysql://localhost:3306/newdatabase";
    private static final String USER_DB = "root";
    private static final String PASSWORD_DB = "password";

    private static final String CURRENT_DATABASE_NAME = "currentTable";

    private static final String MAKE_NEW_TABLE = "CREATE TABLE IF NOT EXISTS " + CURRENT_DATABASE_NAME + " ( " +
            "id INT AUTO_INCREMENT PRIMARY KEY, " +
            "data VARCHAR(255) NOT NULL)";
    private static final String INSERT_SQL = "INSERT INTO " + CURRENT_DATABASE_NAME + "(data) VALUES (?)";
    private static final String SELECT_SQL = "SELECT data FROM " + CURRENT_DATABASE_NAME + " WHERE id = ?";

    public DatabaseStorage() {
        try (Connection connection = connectToDatabase()) {
            connection.createStatement().execute(MAKE_NEW_TABLE);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void save(String data) {
        try(Connection connection = connectToDatabase();
            PreparedStatement statement =
                    connection.prepareStatement(INSERT_SQL)) {

            statement.setString(1, data);
            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String retrieve(int id) {
        try(Connection connection = connectToDatabase();
            PreparedStatement statement =
                    connection.prepareStatement(SELECT_SQL)) {

            statement.setInt(1, id);
            return getResultFromQuery(statement);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    private Connection connectToDatabase() throws SQLException {
        return DriverManager.getConnection(URL_DB, USER_DB, PASSWORD_DB);
    }

    private String getResultFromQuery(PreparedStatement statement) throws SQLException {
        try( ResultSet rs = statement.executeQuery()) {
            if (rs.next()) {
                return rs.getString("data");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}



