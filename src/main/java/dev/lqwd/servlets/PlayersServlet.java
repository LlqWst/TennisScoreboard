package dev.lqwd.servlets;

import dev.lqwd.dao.PlayerDao;
import dev.lqwd.entity.Player;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.PrintWriter;

import java.util.List;

@Slf4j
@WebServlet("/findAllPlayers")
public class PlayersServlet extends BasicServlet {

    PlayerDao playerDao = new PlayerDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        List<Player> players = playerDao.findAll();

        PrintWriter writer = response.getWriter();

        Player player = playerDao.findById(2L);
        Player player2 = playerDao.findByName("Ivan");

        printPLayer(players, writer); // перенести в тест
        printPLayer(List.of(player), writer); // перенести в тест
        printPLayer(List.of(player2), writer); // перенести в тест

        writer.write("<html><body>");
        writer.write("<a href=\"index.jsp\">Menu</a>");
        writer.write("</body></html>");
        writer.close();

    }

    private static void printPLayer(List<Player> players, PrintWriter writer) {
        for (Player player : players) {
            Long id = player.getId();
            String name = player.getName();
            String message = id + " " + name;
            writer.write("<html><body>");
            writer.write("<h1>" + message + "</h1>");
            writer.write("</body></html>");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String name = request.getParameter("name");

        PrintWriter writer = response.getWriter();
        Player player = playerDao.save(name);

        printPLayer(List.of(player), writer);
        writer.write("<html><body>");
        writer.write("<a href=\"index.jsp\">Menu</a>");
        writer.write("</body></html>");
        writer.close();
    }

}