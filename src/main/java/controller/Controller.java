package controller;

import bean.User;
import service.ClientService;
import service.exception.ServiceException;
import service.factory.ServiceFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class Controller extends HttpServlet {
    private final ClientService clientService = ServiceFactory.getInstance().getClientService();

    private String dispatch(String url, String destination) {
        return url.substring(0, url.lastIndexOf("/")) + "/" + destination;
    }

    private void signIn(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        var user = new User();
        user.setLogin(request.getParameter("login"));
        user.setPassword(request.getParameter("password"));
        try {
            if (clientService.signIn(user)) {
                session.setAttribute("user", user);
                response.sendRedirect(dispatch(request.getRequestURL().toString(), "library/catalog"));
            } else {
                session.setAttribute("errorMessage", "Неправильно введен логин или пароль");
                response.sendRedirect(dispatch(request.getRequestURL().toString(), "sign-in"));
            }
        } catch (ServiceException e) {
            session.setAttribute("errorMessage", "Поля не должны быть пустыми");
            response.sendRedirect(dispatch(request.getRequestURL().toString(), "sign-in"));
        }
    }

    private void signUp(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        var user = new User();
        user.setLogin(request.getParameter("login"));
        user.setPassword(request.getParameter("password"));
        user.setFirstName(request.getParameter("firstName"));
        user.setLastName(request.getParameter("lastName"));
        try {
            if (clientService.signUp(user)) {
                session.setAttribute("user", user);
                response.sendRedirect(dispatch(request.getRequestURL().toString(), "library/catalog"));
            } else {
                session.setAttribute("errorMessage", "Логин занят");
                response.sendRedirect(dispatch(request.getRequestURL().toString(), "sign-up"));
            }
        } catch (ServiceException e) {
            session.setAttribute("errorMessage", "Поля не должны быть пустыми");
            response.sendRedirect(dispatch(request.getRequestURL().toString(), "sign-up"));
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println(request.getRequestURI());
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        switch (action) {
            case "sign-in" -> {
                signIn(request, response);
            }
            case "sign-up" -> {
                signUp(request, response);
            }
        }
    }
}