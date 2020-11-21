package dao.impl;

import bean.User;
import bean.enums.UserRole;
import dao.UserDao;
import dao.exception.DAOException;
import dao.pool.ConnectionPool;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of UserDao for SQL
 */
public class SQLUserDao implements UserDao {
    private final List<User> users = new ArrayList<>();
    private int i = 2;

    {
        users.add(new User(1, "A", "B", "C", "D", UserRole.USER));
        users.add(new User(228, "Mike", "Govnovskiy", "L", "L", UserRole.LIBRARIAN));
    }

    /**
     * Finds user with appropriate login-password pair
     * @param user existing user
     * @return user if exists, else null
     * @throws DAOException default
     */
    @Override
    public User signIn(User user) throws DAOException {
        return users.stream()
                .filter(u -> u.getLogin().equals(user.getLogin()) && u.getPassword().equals(user.getPassword()))
                .findFirst().get();
    }

    /**
     * Adds new user
     * @param user new user
     * @return user
     * @throws DAOException default
     */
    @Override
    public User signUp(User user) throws DAOException {
        ConnectionPool pool = null;
        Connection connection = null;
        try {
            pool = ConnectionPool.getConnectionPool();
            connection = pool.getConnection();

            var sql = "INSERT INTO users (first_name, last_name, login, password, role) VALUES (?, ?, ?, ?, ?)";
            var statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, user.getFirstName());
            statement.setString(2, user.getLastName());
            statement.setString(3, user.getLogin());
            statement.setString(4, user.getPassword());
            statement.setInt(5, user.getRole().ordinal());
            statement.executeUpdate();

            var generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                user.setId(generatedKeys.getInt(1));
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        } finally {
            if (pool != null)
                pool.returnConnection(connection);
        }

        users.add(user);
        user.setId(i++);
        return user;
    }

    /**
     * Finds user by login
     * @param login user login
     * @return user by login
     * @throws DAOException default
     */
    @Override
    public User getUserByLogin(String login) throws DAOException {
        return users.stream().filter(user -> user.getLogin().equals(login)).findFirst().get();
    }

    /**
     * Gets list of all users
     * @return list of all users
     * @throws DAOException default
     */
    @Override
    public List<User> getUsers() throws DAOException {
        return users;
    }
}
