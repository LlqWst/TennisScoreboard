package dev.lqwd.servlets;

import dev.lqwd.entity.Player;
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
@WebServlet("/findAllPlayers")
public class PlayersServlet extends BasicServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        findAllPLayers(response);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    public static void findAllPLayers(HttpServletResponse response) throws IOException {

        try (Session session = HibernateUtil.openSession()) {
            Transaction transaction = session.beginTransaction();
            log.trace("Transaction is created: {}", transaction);

            //Player player1 = session.byId(Player.class).load(1L);
            List<Player> players = session.createQuery("SELECT p FROM Player p", Player.class)
                    .setFirstResult(0)
                    .setMaxResults(10)
                    .list();
            log.info("Transaction is in persistent state: {}, session {}", players, session);

            PrintWriter writer = response.getWriter();
            for (Player player : players) {
                Long id = player.getId();
                String name = player.getName();
                String message = id + " " + name;
                writer.write("<html><body>");
                writer.write("<h1>" + message + "</h1>");
                writer.write("</body></html>");
            }

            writer.write("<html><body>");
            writer.write("<html><body>");
            writer.write("<a href=\"index.jsp\">Menu</a>");
            writer.write("</body></html>");
            writer.close();

            session.getTransaction().commit();
            log.info("Transaction is in detached state: {}, session {}", players, session);
        } catch (RuntimeException e) {
            log.error("Exception occurred", e);
            throw new RuntimeException(e);
        }
    }
}