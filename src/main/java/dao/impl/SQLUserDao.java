package dao.impl;

import bean.User;
import bean.enums.UserRole;
import dao.UserDao;
import dao.exception.DAOException;

import java.util.ArrayList;
import java.util.List;

public class SQLUserDao implements UserDao {
    private final List<User> users = new ArrayList<>();
    private int i = 2;

    {
        users.add(new User(1, "A", "B", "C", "D", UserRole.USER));
        users.add(new User(228, "Mike", "Govnovskiy", "L", "L", UserRole.LIBRARIAN));
    }

    @Override
    public User signIn(User user) throws DAOException {
        return users.stream()
                .filter(u -> u.getLogin().equals(user.getLogin()) && u.getPassword().equals(user.getPassword()))
                .findFirst().get();
    }

    @Override
    public User signUp(User user) throws DAOException {
        users.add(user);
        user.setId(i++);
        return user;
    }

    @Override
    public User getUserByLogin(String login) throws DAOException {
        return users.stream().filter(user -> user.getLogin().equals(login)).findFirst().get();
    }

    @Override
    public List<User> getUsers() throws DAOException {
        return users;
    }
}
