package cybersoft.javabackend.java18.game.filter;

import cybersoft.javabackend.java18.game.utils.UrlUtils;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(urlPatterns = {UrlUtils.ALL})
public class ErrorFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        System.out.println(req.getServletPath() + " " + resp.getStatus());
        if (resp.getStatus() == 500) {
            resp.sendRedirect(req.getContextPath() + UrlUtils.INTERNAL_ERROR);
        } else if (resp.getStatus() == 404) {
            resp.sendRedirect(req.getContextPath() + UrlUtils.NOT_FOUND);
        } else {
            chain.doFilter(request, response);
        }
    }
}
