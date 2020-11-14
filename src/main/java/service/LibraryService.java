package service;

import bean.Book;
import service.exception.ServiceException;

public interface LibraryService {
    void addNewBook(Book book) throws ServiceException;
    void addEditedBook(Book book) throws ServiceException;
}
