package service.impl;

import bean.User;
import dao.UserDao;
import dao.exception.DAOException;
import dao.factory.DAOFactory;
import service.ClientService;
import service.exception.ServiceException;

import java.util.List;

public class ClientServiceImpl implements ClientService {
    private final UserDao userDao = DAOFactory.getInstance().getUserDAO();

    private boolean isPasswordCorrect(User user) throws ServiceException {
        try {
            List<User> users = userDao.getUsers();
            User comparedUser = users.stream()
                    .filter(u -> u.getLogin().equals(user.getLogin()))
                    .findFirst().orElse(null);
            return (comparedUser == null) ? false : comparedUser.getPassword().equals(user.getPassword());
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    private boolean isLoginUnique(User user) throws ServiceException {
        try {
            List<User> users = userDao.getUsers();
            return users.stream().noneMatch(u -> u.getLogin().equals(user.getLogin()));
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public boolean signIn(User user) throws ServiceException {
        if (user.getLogin().equals("")) {
            throw new ServiceException("Login can't be empty");
        } else {
            try {
                if (isPasswordCorrect(user)) {
                    userDao.signIn(user);
                    return true;
                } else {
                    return false;
                }
            } catch (DAOException e) {
                throw new ServiceException(e);
            }
        }
    }

    @Override
    public void signOut(String login) throws ServiceException {

    }

    @Override
    public boolean signUp(User user)
            throws ServiceException {
        if (user.getLogin().equals("") || user.getPassword().equals("") || user.getFirstName().equals("") ||
                user.getLastName().equals("")) {
            throw new ServiceException("Fields can't be empty");
        } else {
            try {
                if (isLoginUnique(user)) {
                    userDao.signUp(user);
                    return true;
                } else {
                    return false;
                }
            } catch (DAOException e) {
                throw new ServiceException(e);
            }
        }
    }
}
