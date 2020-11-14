package dao;

import bean.User;
import dao.exception.DAOException;

import java.util.List;

public interface UserDao {
    void signIn(User user) throws DAOException;
    void signUp(User user) throws DAOException;
    List<User> getUsers() throws DAOException;
}
