package cybersoft.javabackend.java18.game.servlet;

import cybersoft.javabackend.java18.game.model.GameSession;
import cybersoft.javabackend.java18.game.model.Player;
import cybersoft.javabackend.java18.game.service.GameService;
import cybersoft.javabackend.java18.game.utils.JspUtils;
import cybersoft.javabackend.java18.game.utils.UrlUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "gameServlet", urlPatterns = {UrlUtils.GAME, UrlUtils.NEW_GAME,UrlUtils.RANK})
public class GameServlet extends HttpServlet {

    private GameService service;

    @Override
    public void init() throws ServletException {
        super.init();
        service = GameService.getService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        switch (request.getServletPath()) {
            case UrlUtils.GAME -> {
                gameProcess(request,response);
            }
            case UrlUtils.RANK -> {
                ranking(request,response);
            }
            case UrlUtils.NEW_GAME -> {
                playNewGame(request,response);
            }
            default -> response.sendRedirect(JspUtils.NOT_FOUND);
        }
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processGuess(req,resp);
    }

    private void gameProcess(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Player currentPlayer = (Player) request.getSession().getAttribute("currentUser");
        GameSession currentGame = service.getCurrentGame(currentPlayer.getUsername());
        request.setAttribute("game", currentGame);
        request.setAttribute("guesses",currentGame.getGuesses());
        request.getRequestDispatcher(JspUtils.GAME).forward(request, response);
    }

    private void processGuess(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // get current player from request and player's active game in store
        Player currentPlayer = (Player) req.getSession().getAttribute("currentUser");
        GameSession currentGame = service.getCurrentGame(currentPlayer.getUsername());
        String numberInput = req.getParameter("number");
        int number = Integer.parseInt(numberInput);

        // guess number with current game and player's input number
        service.guessNumber(currentGame, number);

        // put guess to request
        req.setAttribute("guesses", currentGame.getGuesses());
        req.getRequestDispatcher(JspUtils.GAME).forward(req, resp);
//        resp.sendRedirect(req.getContextPath() + UrlUtils.GAME);
    }

    private void ranking(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<GameSession> gameSessions = GameService.getService().ranking();
        request.setAttribute("games", gameSessions);
        request.getRequestDispatcher(JspUtils.RANK).forward(request, response);
    }

    private void playNewGame(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // get current game
        Player currentPlayer = (Player) request.getSession().getAttribute("currentUser");
        GameSession currentGame = service.getCurrentGame(currentPlayer.getUsername());

        // deactivate current game
        currentGame.setActive(false);

        // create new game
        GameSession newGame = service.createGame(currentPlayer.getUsername());

        // set to request
        request.setAttribute("game", newGame);
        request.getRequestDispatcher(JspUtils.GAME).forward(request, response);
    }

}
