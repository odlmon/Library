package controller;

import bean.Book;
import bean.Order;
import bean.User;
import bean.enums.OrderStatus;
import bean.enums.UserRole;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
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
import java.util.Locale;
import java.util.ResourceBundle;

public class Controller extends HttpServlet {
    private final ClientService clientService = ServiceFactory.getInstance().getClientService();
    private final LibraryService libraryService = ServiceFactory.getInstance().getLibraryService();
    private final Logger logger = (Logger) LogManager.getLogger();
    private ResourceBundle bundle = ResourceBundle.getBundle("text");

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
                request.setAttribute("errorMessage", bundle.getString("incorrect_login_password"));
                request.getRequestDispatcher("/WEB-INF/jsp/sign-in.jsp").forward(request, response);
            }
        } catch (ServiceException e) {
            logger.error(e.getMessage());
            request.setAttribute("errorMessage", bundle.getString("empty_fields"));
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
                request.setAttribute("errorMessage", bundle.getString("login_is_busy"));
                request.getRequestDispatcher("/WEB-INF/jsp/sign-up.jsp").forward(request, response);
            }
        } catch (ServiceException e) {
            logger.error(e.getMessage());
            request.setAttribute("errorMessage", bundle.getString("empty_fields"));
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
                logger.error(e.getMessage());
                request.setAttribute("errorMessage", bundle.getString("error_book_list"));
            }
        } else {
            try {
                List<Book> bookList = libraryService.searchBooks(searchRequest);
                request.setAttribute("books", bookList);
                if (bookList.size() == 0) {
                    request.setAttribute("noElements", bundle.getString("nothing_found"));
                }
            } catch (ServiceException e) {
                logger.error(e.getMessage());
                request.setAttribute("errorMessage", bundle.getString("error_searching"));
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
                        logger.error(e.getMessage());
                        request.setAttribute("errorMessage", bundle.getString("error_deleting"));
                        request.getRequestDispatcher("/WEB-INF/jsp/catalog.jsp").forward(request, response);
                    }
                }
                case "edit" -> {
                    try {
                        request.setAttribute("book", libraryService.getBookById(bookId));
                        request.setAttribute("type", "edit");
                        request.getRequestDispatcher("/WEB-INF/jsp/add-edit.jsp").forward(request, response);
                    } catch (ServiceException e) {
                        logger.error(e.getMessage());
                        request.setAttribute("errorMessage", bundle.getString("error_getting"));
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
                logger.error(e.getMessage());
                request.setAttribute("errorMessage", bundle.getString("error_book_list"));
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

    private List<Order> getUserOrders(String bookId, User user, String action, boolean langIsSet) throws ServiceException {
        if (bookId != null && !langIsSet) {
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

    private List<Order> getOrders(String bookId, String userLogin, String action, boolean langIsSet) throws ServiceException {
        if (bookId != null && !langIsSet) {
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
            boolean langIsSet = request.getParameter("lang") != null;
            List<Order> orders;
            if (user.getRole() == UserRole.USER) {
                orders = getUserOrders(bookId, user, action, langIsSet);
            } else {
                orders = getOrders(bookId, userLogin, action, langIsSet);
            }
            if (orders.stream().noneMatch(order -> order.getStatus() == OrderStatus.PROCESSING)) {
                request.setAttribute("noProcessing", bundle.getString("no_processing"));
            }
            if (orders.stream().noneMatch(order -> order.getStatus() != OrderStatus.PROCESSING)) {
                request.setAttribute("noActive", bundle.getString("no_active"));
            }
            request.setAttribute("orders", orders);
            request.getRequestDispatcher("/WEB-INF/jsp/orders.jsp").forward(request, response);
        } catch (ServiceException e) {
            logger.error(e.getMessage());
            request.setAttribute("errorMessage", bundle.getString("error_order_list"));
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
                    request.setAttribute("errorMessage", bundle.getString("error_add_book_data"));
                    request.setAttribute("type", "add");
                    request.getRequestDispatcher("/WEB-INF/jsp/add-edit.jsp").forward(request, response);
                } else {
                    response.sendRedirect(dispatch(request.getRequestURL().toString(), "catalog"));
                }
            } catch (ServiceException e) {
                logger.error(e.getMessage());
                request.setAttribute("errorMessage", bundle.getString("error_add_book"));
                request.setAttribute("type", "add");
                request.getRequestDispatcher("/WEB-INF/jsp/add-edit.jsp").forward(request, response);
            } catch (NumberFormatException e) {
                logger.error(e.getMessage());
                request.setAttribute("errorMessage", bundle.getString("error_count"));
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
                    request.setAttribute("errorMessage", bundle.getString("error_edit_book_data"));
                    request.setAttribute("book", book);
                    request.setAttribute("type", "edit");
                    request.getRequestDispatcher("/WEB-INF/jsp/add-edit.jsp").forward(request, response);
                } else {
                    response.sendRedirect(dispatch(request.getRequestURL().toString(), "catalog"));
                }
            } catch (ServiceException e) {
                logger.error(e.getMessage());
                request.setAttribute("errorMessage", bundle.getString("error_edit_book"));
                request.setAttribute("type", "edit");
                request.getRequestDispatcher("/WEB-INF/jsp/add-edit.jsp").forward(request, response);
            } catch (NumberFormatException e) {
                logger.error(e.getMessage());
                request.setAttribute("errorMessage", bundle.getString("error_count"));
                request.setAttribute("type", "edit");
                request.getRequestDispatcher("/WEB-INF/jsp/add-edit.jsp").forward(request, response);
            }
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String lang = request.getParameter("lang");
        if (lang != null) {
            request.getSession().setAttribute("lang", lang);
            bundle = ResourceBundle.getBundle("text", new Locale(lang));
            String query = request.getQueryString();
            request.setAttribute("queryWithLang", query.substring(0, query.lastIndexOf("=")) + "=");
        }
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
            default -> request.getRequestDispatcher("/WEB-INF/jsp/404.jsp").forward(request, response);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String[] arr = request.getRequestURI().split("/");
        String action = arr[arr.length - 1];
        switch (action) {
            case "sign-in" -> signIn(request, response);
            case "sign-up" -> signUp(request, response);
            default -> request.getRequestDispatcher("/WEB-INF/jsp/404.jsp").forward(request, response);
        }
    }
}