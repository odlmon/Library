package dao;

import bean.Order;
import bean.User;
import dao.exception.DAOException;

import java.util.List;

public interface OrderDao {
    void addOrder(Order order) throws DAOException;
    void deleteOrder(Order order) throws DAOException;
    void updateOrder(Order order) throws DAOException;
    List<Order> getOrders() throws DAOException;
    List<Order> getUserOrders(User user) throws DAOException;
}
