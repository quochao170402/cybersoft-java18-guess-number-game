package cybersoft.javabackend.java18.game.servlet;

import cybersoft.javabackend.java18.game.utils.JspUtils;
import cybersoft.javabackend.java18.game.utils.UrlUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = {
        UrlUtils.NOT_FOUND,
        UrlUtils.INTERNAL_ERROR
})
public class ErrorServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        switch (req.getServletPath()){
            case UrlUtils.NOT_FOUND -> resp.sendRedirect(JspUtils.NOT_FOUND);
            case UrlUtils.INTERNAL_ERROR -> resp.sendRedirect(JspUtils.INTERNAL_ERROR);
//            default -> resp.sendRedirect(JspUtils.NOT_FOUND);
        }
    }
}
