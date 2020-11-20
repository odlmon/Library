package dao.impl;

import bean.User;
import bean.enums.UserRole;
import dao.UserDao;
import dao.exception.DAOException;

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
