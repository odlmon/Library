package controller;

import bean.Book;
import bean.Order;
import bean.User;
import bean.enums.OrderStatus;
import bean.enums.UserRole;
import service.ClientService;
import service.LibraryService;
import service.exception.ServiceException;
import service.factory.ServiceFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

public class Controller extends HttpServlet {
    private final ClientService clientService = ServiceFactory.getInstance().getClientService();
    private final LibraryService libraryService = ServiceFactory.getInstance().getLibraryService();

    private String dispatch(String url, String destination) {
        return url.substring(0, url.lastIndexOf("/")) + "/" + destination;
    }

    private void signIn(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        var user = new User();
        user.setLogin(request.getParameter("login"));
        user.setPassword(request.getParameter("password"));
        try {
            user = clientService.signIn(user);
            if (user != null) {
                session.setAttribute("user", user);
                response.sendRedirect(dispatch(request.getRequestURL().toString(), "catalog"));
            } else {
                request.setAttribute("errorMessage", "Неправильно введен логин или пароль");
                request.getRequestDispatcher("/WEB-INF/jsp/sign-in.jsp").forward(request, response);
            }
        } catch (ServiceException e) {
            request.setAttribute("errorMessage", "Поля не должны быть пустыми");
            request.getRequestDispatcher("/WEB-INF/jsp/sign-in.jsp").forward(request, response);
        }
    }

    private void signUp(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        var user = new User();
        user.setLogin(request.getParameter("login"));
        user.setPassword(request.getParameter("password"));
        user.setFirstName(request.getParameter("firstName"));
        user.setLastName(request.getParameter("lastName"));
        user.setRole(UserRole.USER);
        try {
            user = clientService.signUp(user);
            if (user != null) {
                session.setAttribute("user", user);
                response.sendRedirect(dispatch(request.getRequestURL().toString(), "catalog"));
            } else {
                request.setAttribute("errorMessage", "Логин занят");
                request.getRequestDispatcher("/WEB-INF/jsp/sign-up.jsp").forward(request, response);
            }
        } catch (ServiceException e) {
            request.setAttribute("errorMessage", "Поля не должны быть пустыми");
            request.getRequestDispatcher("/WEB-INF/jsp/sign-up.jsp").forward(request, response);
        }
    }

    private void signOut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        session.removeAttribute("user");
        response.sendRedirect(request.getContextPath() + "/library/sign-in");
    }

    private void serveUserCatalogPage(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String searchRequest = request.getParameter("search");
        if (searchRequest == null) {
            try {
                request.setAttribute("books", libraryService.getBookList());
            } catch (ServiceException e) {
                request.setAttribute("errorMessage", "Ошибка получения списка книг");
            }
        } else {
            try {
                List<Book> bookList = libraryService.searchBooks(searchRequest);
                request.setAttribute("books", bookList);
                if (bookList.size() == 0) {
                    request.setAttribute("noElements", "Ничего не найдено");
                }
            } catch (ServiceException e) {
                request.setAttribute("errorMessage", "Ошибка при поиске книг");
            }
        }
        request.getRequestDispatcher("/WEB-INF/jsp/catalog.jsp").forward(request, response);
    }

    private void serveLibrarianCatalogPage(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        String bookId = request.getParameter("bookId");
        if (action != null) {
            switch (action) {
                case "delete" -> {
                    try {
                        if (bookId != null) {
                            Book book = libraryService.getBookById(bookId);
                            libraryService.deleteBook(book);
                        }
                        response.sendRedirect(dispatch(request.getRequestURL().toString(), "catalog"));
                    } catch (ServiceException e) {
                        request.setAttribute("errorMessage", "Произошла ошибка удаления");
                        request.getRequestDispatcher("/WEB-INF/jsp/catalog.jsp").forward(request, response);
                    }
                }
                case "edit" -> {
                    try {
                        request.setAttribute("book", libraryService.getBookById(bookId));
                        request.setAttribute("type", "edit");
                        request.getRequestDispatcher("/WEB-INF/jsp/add-edit.jsp").forward(request, response);
                    } catch (ServiceException e) {
                        request.setAttribute("errorMessage", "Ошибка при попытке получения книги");
                        request.getRequestDispatcher("/WEB-INF/jsp/catalog.jsp").forward(request, response);
                    }
                }
                case "add" -> {
                    request.setAttribute("type", "add");
                    request.getRequestDispatcher("/WEB-INF/jsp/add-edit.jsp").forward(request, response);
                }
            }
        } else {
            try {
                request.setAttribute("books", libraryService.getBookList());
            } catch (ServiceException e) {
                request.setAttribute("errorMessage", "Ошибка при получении списка книг");
            }
            request.getRequestDispatcher("/WEB-INF/jsp/catalog.jsp").forward(request, response);
        }
    }

    private void serveCatalogPage(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute("user");
        if (user.getRole() == UserRole.USER) {
            serveUserCatalogPage(request, response);
        } else {
            serveLibrarianCatalogPage(request, response);
        }
    }

    private List<Order> getUserOrders(String bookId, User user, String action) throws ServiceException {
        if (bookId != null) {
            Book book = libraryService.getBookById(bookId);
            var order = new Order();
            order.setUser(user);
            order.setBook(book);
            if (action == null) {
                order.setStatus(OrderStatus.PROCESSING);
                libraryService.addNewOrder(order);
            } else {
                libraryService.deleteOrder(order);
            }
        }
        return libraryService.getUserOrders(user);
    }

    private List<Order> getOrders(String bookId, String userLogin, String action) throws ServiceException {
        if (bookId != null) {
            User user = clientService.getUserByLogin(userLogin);
            Book book = libraryService.getBookById(bookId);
            var order = new Order();
            order.setUser(user);
            order.setBook(book);
            switch (action) {
                case "reading_room" -> {
                    order.setStatus(OrderStatus.READING_ROOM);
                    libraryService.updateOrder(order);
                }
                case "subscription" -> {
                    order.setStatus(OrderStatus.SUBSCRIPTION);
                    libraryService.updateOrder(order);
                }
                case "cancel" -> {
                    libraryService.deleteOrder(order);
                }
            }
        }
        return libraryService.getOrders();
    }

    private void serveOrdersPage(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            User user = (User) request.getSession().getAttribute("user");
            String action = request.getParameter("action");
            String userLogin = request.getParameter("userLogin");
            String bookId = request.getParameter("bookId");
            List<Order> orders;
            if (user.getRole() == UserRole.USER) {
                orders = getUserOrders(bookId, user, action);
            } else {
                orders = getOrders(bookId, userLogin, action);
            }
            if (orders.stream().noneMatch(order -> order.getStatus() == OrderStatus.PROCESSING)) {
                request.setAttribute("noProcessing", "У вас нет заказов в обработке");
            }
            if (orders.stream().noneMatch(order -> order.getStatus() != OrderStatus.PROCESSING)) {
                request.setAttribute("noActive", "У вас нет активных заказов");
            }
            request.setAttribute("orders", orders);
            request.getRequestDispatcher("/WEB-INF/jsp/orders.jsp").forward(request, response);
        } catch (ServiceException e) {
            request.setAttribute("errorMessage", "Ошибка при получении списка заказов");
        }
    }

    private void addBook(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String title = request.getParameter("title");
        String author = request.getParameter("author");
        String count = request.getParameter("count");
        if (title != null && author != null && count != null) {
            try {
                var book = new Book();
                book.setTitle(title);
                book.setAuthor(author);
                book.setCount(Integer.parseInt(count));
                if (!libraryService.addNewBook(book)) {
                    request.setAttribute("errorMessage",
                            "Книга не была добавлена из-за некорректно введенных данных");
                    request.setAttribute("type", "add");
                    request.getRequestDispatcher("/WEB-INF/jsp/add-edit.jsp").forward(request, response);
                } else {
                    response.sendRedirect(dispatch(request.getRequestURL().toString(), "catalog"));
                }
            } catch (ServiceException e) {
                request.setAttribute("errorMessage", "Ошибка при добавлении книги");
                request.setAttribute("type", "add");
                request.getRequestDispatcher("/WEB-INF/jsp/add-edit.jsp").forward(request, response);
            } catch (NumberFormatException e) {
                request.setAttribute("errorMessage", "Количество должно быть числом не меньше 0");
                request.setAttribute("type", "add");
                request.getRequestDispatcher("/WEB-INF/jsp/add-edit.jsp").forward(request, response);
            }
        }
    }

    private void editBook(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String id = request.getParameter("id");
        String title = request.getParameter("title");
        String author = request.getParameter("author");
        String count = request.getParameter("count");
        if (id != null && title != null && author != null && count != null) {
            try {
                var book = new Book();
                book.setId(Integer.parseInt(id));
                book.setTitle(title);
                book.setAuthor(author);
                book.setCount(Integer.parseInt(count));
                if (!libraryService.addEditedBook(book)) {
                    request.setAttribute("errorMessage",
                            "Книга не была изменена из-за некорректно введенных данных");
                    request.setAttribute("book", book);
                    request.setAttribute("type", "edit");
                    request.getRequestDispatcher("/WEB-INF/jsp/add-edit.jsp").forward(request, response);
                } else {
                    response.sendRedirect(dispatch(request.getRequestURL().toString(), "catalog"));
                }
            } catch (ServiceException e) {
                request.setAttribute("errorMessage", "Ошибка при добавлении книги");
                request.setAttribute("type", "edit");
                request.getRequestDispatcher("/WEB-INF/jsp/add-edit.jsp").forward(request, response);
            } catch (NumberFormatException e) {
                request.setAttribute("errorMessage", "Количество должно быть числом не меньше 0");
                request.setAttribute("type", "edit");
                request.getRequestDispatcher("/WEB-INF/jsp/add-edit.jsp").forward(request, response);
            }
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String[] arr = request.getRequestURI().split("/");
        String action = arr[arr.length - 1];
        switch (action) {
            case "sign-in" -> request.getRequestDispatcher("/WEB-INF/jsp/sign-in.jsp").forward(request, response);
            case "sign-up" -> request.getRequestDispatcher("/WEB-INF/jsp/sign-up.jsp").forward(request, response);
            case "sign-out" -> signOut(request, response);
            case "catalog" -> serveCatalogPage(request, response);
            case "orders" -> serveOrdersPage(request, response);
            case "addBook" -> addBook(request, response);
            case "editBook" -> editBook(request, response);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String[] arr = request.getRequestURI().split("/");
        String action = arr[arr.length - 1];
        switch (action) {
            case "sign-in" -> signIn(request, response);
            case "sign-up" -> signUp(request, response);
        }
    }
}