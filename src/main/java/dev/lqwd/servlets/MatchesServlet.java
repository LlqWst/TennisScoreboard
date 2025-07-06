package dev.lqwd.servlets;

import dev.lqwd.dao.MatchesDao;
import dev.lqwd.entity.Match;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@Slf4j
@WebServlet("/findAllMatches")
public class MatchesServlet extends BasicServlet {

    MatchesDao matchesDao = new MatchesDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Match> matches = matchesDao.findAll();
        PrintWriter writer = response.getWriter();

        printMatch(matches, writer); // перенести в тест

        Match match = matchesDao.findById(1L);
        printMatch(List.of(match), writer); // перенести в тест

        writer.write("<html><body>");
        writer.write("<a href=\"index.jsp\">Menu</a>");
        writer.write("</body></html>");
        writer.close();

    }

    private static void printMatch(List<Match> matches, PrintWriter writer) {
        for (Match match : matches) {

            long id = match.getId();
            String p1 = match.getPlayer1().getName();
            String p2 = match.getPlayer2().getName();
            String w = match.getWinner().getName();

            String message = id + " " + p1 + " " + p2 + " " + w;
            writer.write("<html><body>");
            writer.write("<h1>" + message + "</h1>");
            writer.write("</body></html>");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }


}