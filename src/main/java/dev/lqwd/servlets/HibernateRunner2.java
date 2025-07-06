package dev.lqwd.servlets;


import dev.lqwd.entity.Player;
import dev.lqwd.utils.HibernateUtil;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.sql.SQLException;
import java.util.List;

@Slf4j
public class HibernateRunner2 {

    public static void main(String[] args) {
        try (Session session = HibernateUtil.openSession()) {
            Transaction transaction = session.beginTransaction();
            log.trace("Transaction is created: {}", transaction);

            Player player1 = session.byId(Player.class).load(1L);
            List<Player> ls = session.createQuery("SELECT pl FROM Player pl", Player.class)
                    .setFirstResult(2)
                    .setMaxResults(4)
                    .list();
            System.out.println(player1);

            log.info("Transaction is in persistent state: {}, session {}", player1, session);

            session.getTransaction().commit();
            log.info("Transaction is in detached state: {}, session {}", player1, session);
        } catch (Exception e){
            log.error("Exception occurred", e);
            throw e;
        }


    }

}

