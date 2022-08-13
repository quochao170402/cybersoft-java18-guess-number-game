package cybersoft.javabackend.java18.game.filter;

import cybersoft.javabackend.java18.game.utils.UrlUtils;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(urlPatterns = {UrlUtils.ALL})
public class AuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        if (isLoginUser(req) || isAuthUrl(req)) {
            chain.doFilter(request, response);
        } else {
            resp.sendRedirect(req.getContextPath() + UrlUtils.LOGIN);
        }

        /*
         process before the request get in servlet
         chain.doFilter(request,response)
         process response from servlet
        */
    }

    private boolean isLoginUser(HttpServletRequest req) {
        return req.getSession().getAttribute("currentUser") != null;
    }

    private boolean isAuthUrl(HttpServletRequest request) {
        String path = request.getServletPath();
        return path.startsWith(UrlUtils.REGISTER)
                || path.startsWith(UrlUtils.LOGIN);
    }
}
