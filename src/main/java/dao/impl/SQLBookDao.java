package dao.impl;

import bean.Book;
import dao.BookDao;
import dao.exception.DAOException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SQLBookDao implements BookDao {
    private final List<Book> list = new ArrayList<>();
    private int i = 20;

    {
        list.add(new Book(1, "Сурков Д.А.", "Эфиродинамика", 1));
        list.add(new Book(2, "Сурков К.А.", "Сталин - агент госдепа, или почему Россия - сверхдержава", 2));
        list.add(new Book(3, "Фадеева Е.Е.", "Психбольница в руках пациентов", 3));
//        list.add(new Book(4, "Anonynimous", "The book you cannot get", 0));
    }

    @Override
    public void addBook(Book book) throws DAOException {
        book.setId(i++);
        list.add(book);
    }

    @Override
    public void editBook(Book book) throws DAOException {
        Book edited = list.stream().filter(b -> b.getId() == book.getId()).findFirst().get();
        edited.setTitle(book.getTitle());
        edited.setAuthor(book.getAuthor());
        edited.setCount(book.getCount());
    }

    @Override
    public void deleteBook(Book book) throws DAOException { //TODO: get orders by book and delete
        Book odd = list.stream().filter(b -> b.getId() == book.getId()).findFirst().get();
        list.remove(odd);
    }

    @Override
    public void decrementCount(Book book) throws DAOException {
        Book needed = list.stream().filter(b -> b.getId() == book.getId()).findFirst().get();
        needed.setCount(needed.getCount() - 1);
    }

    @Override
    public void incrementCount(Book book) throws DAOException {
        Book needed = list.stream().filter(b -> b.getId() == book.getId()).findFirst().get();
        needed.setCount(needed.getCount() + 1);
    }

    @Override
    public Book getBookById(int id) throws DAOException {
        return list.stream().filter(book -> book.getId() == id).findFirst().orElse(null);
    }

    @Override
    public List<Book> getBooks() throws DAOException {
        return list;
    }

    @Override
    public List<Book> searchBooks(String request) throws DAOException {
        return Stream.concat(list.stream().filter(book -> book.getTitle().contains(request)),
                list.stream().filter(book -> book.getAuthor().contains(request)))
                .distinct()
                .collect(Collectors.toList());
    }
}
