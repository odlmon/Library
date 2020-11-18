package dao.impl;

import bean.Book;
import bean.Order;
import bean.User;
import bean.enums.OrderStatus;
import bean.enums.UserRole;
import dao.OrderDao;
import dao.exception.DAOException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SQLOrderDao implements OrderDao {
    private final List<Order> orders = new ArrayList<>();

    {
        var order = new Order();
        order.setUser(new User(1, "A", "B", "C", "D", UserRole.USER));
        order.setBook(new Book(5, "Leonardo di Caprio", "How to win Oscar in 100 ways", 1));
        order.setStatus(OrderStatus.SUBSCRIPTION);
        orders.add(order);
    }

    @Override
    public void addOrder(Order order) throws DAOException {
        orders.add(order);
    }

    @Override
    public void deleteOrder(Order order) throws DAOException {
        Order odd = orders.stream().filter(o -> o.getUser().equals(order.getUser()) && o.getBook().equals(order.getBook())).findFirst().get();
        orders.remove(odd);
    }

    @Override
    public void updateOrder(Order order) throws DAOException {
        orders.stream()
                .filter(o -> o.getUser().equals(order.getUser()) && o.getBook().equals(order.getBook()))
                .findFirst()
                .get()
                .setStatus(order.getStatus());
    }

    @Override
    public List<Order> getOrders() throws DAOException {
        return orders;
    }

    @Override
    public List<Order> getUserOrders(User user) throws DAOException {
        return orders.stream().filter(order -> order.getUser().equals(user)).collect(Collectors.toList());
    }
}
