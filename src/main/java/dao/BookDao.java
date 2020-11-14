package dao;

import bean.Book;
import dao.exception.DAOException;

public interface BookDao {
    void addBook(Book book) throws DAOException;
    void deleteBook(int id) throws DAOException;
}
