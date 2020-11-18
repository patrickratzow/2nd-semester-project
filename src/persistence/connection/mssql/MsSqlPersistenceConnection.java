package persistence.connection.mssql;

import persistence.connection.PersistenceConnection;
import persistence.connection.PersistenceRepositoryFactory;
import util.JUnit;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;

/**
 * The type Db connection.
 */
public class MsSqlPersistenceConnection implements PersistenceConnection {
    private Connection connection = null;
    private static final MsSqlPersistenceConnection persistenceConnection = new MsSqlPersistenceConnection();
    private final MsSqlRepositoryFactory repositoryFactory = new MsSqlRepositoryFactory();

    private static final String driverClass = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    private static final String serverAddress = "hildur.ucn.dk";
    private static final int    serverPort = 1433;
    private static final String password = "Password1!";

    public MsSqlPersistenceConnection() {
        final boolean isJUnit = JUnit.isJUnitTest();
        final String name = isJUnit ? "dmaa0220_1083802" : "dmaa0220_1083750";

        String connectionString = String.format("jdbc:sqlserver://%s:%s;database=%s;user=%s;password=%s",
                serverAddress, serverPort, name, name, password);
        try {
            Class.forName(driverClass);
            connection = DriverManager.getConnection(connectionString);
        } catch (ClassNotFoundException e) {
            System.err.println("Could not load JDBC driver");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("Could not connect to database " + name + "@" + serverAddress + ":" + serverPort + " as user " + name + " using password ******");
            System.out.println("Connection string was: " + connectionString.substring(0, connectionString.length() - password.length()) + "....");
            e.printStackTrace();
        }

        //if (isJUnit) {
            setupDatabase();
        //}
    }

    private void setupDatabase() {
        try {
            startTransaction();

            Files.walk(Paths.get("./sql/"))
                /* Only allow SQL files */
                .filter(p -> p.getFileName().toString().endsWith(".sql"))
                /* Execute each script sequentially to ensure that everything gets created correctly */
                .forEach(p -> {
                    try {
                        final StringBuilder sql = new StringBuilder();
                        for (String line : Files.readAllLines(p)) {
                            sql.append(line).append("\n");
                        }
                        final PreparedStatement ps = prepareStatement(sql.toString());
                        System.out.println(p.getFileName());
                        ps.execute();
                    } catch (IOException | SQLException e) {
                        e.printStackTrace();
                    }
                });

            commitTransaction();
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    public static MsSqlPersistenceConnection getInstance() {
        return persistenceConnection;
    }

    /**
     * Start transaction.
     *
     * @throws SQLException the sql exception
     */
    public void startTransaction() throws SQLException {
        connection.setAutoCommit(false);
    }

    public Savepoint setSavepoint() throws SQLException {
        return connection.setSavepoint();
    }

    /**
     * Commit transaction.
     *
     * @throws SQLException the sql exception
     */
    public void commitTransaction() throws SQLException {
        connection.commit();
        connection.setAutoCommit(true);
    }

    /**
     * Rollback transaction.
     *
     * @throws SQLException the sql exception
     */
    public void rollbackTransaction() throws SQLException {
        connection.rollback();
        connection.setAutoCommit(true);
    }

    public void rollbackTransaction(Savepoint savepoint) throws SQLException {
        connection.rollback(savepoint);
        connection.setAutoCommit(true);
    }

    /**
     * Gets connection.
     *
     * @return the connection
     */
    public Connection getConnection() {
        return connection;
    }

    /**
     * Prepare statement prepared statement.
     *
     * @param sql the sql
     * @return the prepared statement
     * @throws SQLException the sql exception
     */
    public PreparedStatement prepareStatement(String sql) throws SQLException {
        return connection.prepareStatement(sql);
    }

    public CallableStatement prepareCall(String sql) throws SQLException {
        return connection.prepareCall(sql);
    }

    /**
     * Prepare statement prepared statement.
     *
     * @param sql               the sql
     * @param autoGeneratedKeys the auto generated keys
     * @return the prepared statement
     * @throws SQLException the sql exception
     */
    public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException {
        return connection.prepareStatement(sql, autoGeneratedKeys);
    }

    /**
     * Disconnect.
     */
    public void disconnect() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public PersistenceRepositoryFactory getRepositoryFactory() {
        return repositoryFactory;
    }
}