package cybersoft.javabackend.java18.gamedoanso.filter;

import cybersoft.javabackend.java18.gamedoanso.utils.UrlUtils;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(urlPatterns = {UrlUtils.ALL})
public class InternalErrorFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
//        resp.getStatus()
        if (resp.getStatus() == 500) {
            resp.sendRedirect(req.getContextPath() + UrlUtils.INTERNAL_ERROR);
        } else if (resp.getStatus() == 404) {
            resp.sendRedirect(req.getContextPath() + UrlUtils.NOT_FOUND);
        } else {
            chain.doFilter(request, response);
        }
    }
}
