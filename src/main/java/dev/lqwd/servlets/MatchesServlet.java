package dev.lqwd.servlets;

import dev.lqwd.entity.Match;
import dev.lqwd.utils.HibernateUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@Slf4j
@WebServlet("/findAllMatches")
public class MatchesServlet extends BasicServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        findAllMatches(response);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    public static void findAllMatches(HttpServletResponse response) {

        try (Session session = HibernateUtil.openSession()) {
            Transaction transaction = session.beginTransaction();
            log.trace("Transaction is created: {}", transaction);

            PrintWriter writer = response.getWriter();
            List<Match> matches = session.createQuery("SELECT m FROM Match m", Match.class)
                    .setFirstResult(0)
                    .setMaxResults(10)
                    .list();
            log.info("Transaction is in persistent state: {}, session {}", matches, session);
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
            writer.write("<html><body>");
            writer.write("<a href=\"index.jsp\">Menu</a>");
            writer.write("</body></html>");
            writer.close();

        } catch (Exception e) {
            throw new RuntimeException("Fignya kakaya-to");
        }
    }

}