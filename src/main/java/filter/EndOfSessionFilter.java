package filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class EndOfSessionFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        var request = (HttpServletRequest) servletRequest;
        var response = (HttpServletResponse) servletResponse;
        HttpSession session = request.getSession(false);
        if (session != null && !session.isNew()) {
            filterChain.doFilter(servletRequest, servletResponse);
        } else {
            String url = request.getRequestURL().toString();
            url = url.substring(0, url.lastIndexOf("/")) + "/sign-in";
            response.sendRedirect(url);
        }
    }

    @Override
    public void destroy() {

    }
}

