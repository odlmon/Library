package service.impl;

import bean.Book;
import bean.Order;
import bean.User;
import dao.BookDao;
import dao.OrderDao;
import dao.exception.DAOException;
import dao.factory.DAOFactory;
import service.LibraryService;
import service.exception.ServiceException;

import java.util.List;

public class LibraryServiceImpl implements LibraryService { //service should check input params
    private final BookDao bookDao = DAOFactory.getInstance().getBookDAO();
    private final OrderDao orderDao = DAOFactory.getInstance().getOrderDAO();

    @Override
    public boolean addNewBook(Book book) throws ServiceException {
        if (book.getTitle().equals("") || book.getAuthor().equals("") || book.getCount() < 0) {
            return false;
        } else {
            try {
                bookDao.addBook(book);
                return true;
            } catch (DAOException e) {
                throw new ServiceException(e);
            }
        }
    }

    @Override
    public boolean addEditedBook(Book book) throws ServiceException {
        if (book.getTitle().equals("") || book.getAuthor().equals("") || book.getCount() < 0 || book.getId() < 0) {
            return false;
        } else {
            try {
                bookDao.editBook(book);
                return true;
            } catch (DAOException e) {
                throw new ServiceException(e);
            }
        }
    }

    @Override
    public void deleteBook(Book book) throws ServiceException {
        try {
            bookDao.deleteBook(book);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public List<Book> getBookList() throws ServiceException {
        try {
            return bookDao.getBooks();
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public List<Book> searchBooks(String request) throws ServiceException {
        try {
            return bookDao.searchBooks(request);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public Book getBookById(String id) throws ServiceException {
        try {
            return bookDao.getBookById(Integer.parseInt(id));
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public void addNewOrder(Order order) throws ServiceException {
        try {
            orderDao.addOrder(order);
            bookDao.decrementCount(order.getBook());
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public void deleteOrder(Order order) throws ServiceException {
        try {
            orderDao.deleteOrder(order);
            bookDao.incrementCount(order.getBook());
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public List<Order> getOrders() throws ServiceException {
        try {
            return orderDao.getOrders();
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public List<Order> getUserOrders(User user) throws ServiceException {
        try {
            return orderDao.getUserOrders(user);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public void updateOrder(Order order) throws ServiceException {
        try {
            orderDao.updateOrder(order);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }
}
