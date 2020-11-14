package dao.impl;

import bean.User;
import dao.UserDao;
import dao.exception.DAOException;

import java.util.ArrayList;
import java.util.List;

public class SQLUserDao implements UserDao {
    @Override
    public void signIn(User user) throws DAOException {

    }

    @Override
    public void signUp(User user) throws DAOException {

    }

    @Override
    public List<User> getUsers() throws DAOException {
        List<User> users = new ArrayList<>();
        users.add(new User(1, "A", "B", "C", "D", "User"));
        return users;
    }
}
