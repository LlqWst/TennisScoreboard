package dev.lqwd.dao;

import dev.lqwd.entity.Player;
import dev.lqwd.utils.HibernateUtil;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

@Slf4j
public class PlayerDao {

    public List<Player> findAll() {

        String hql = "SELECT p FROM Player p";

        try (Session session = HibernateUtil.openSession()) {
            Transaction transaction = session.beginTransaction();
            log.trace("Transaction is created: {}", transaction);

             List<Player> players = session.createQuery(hql, Player.class)
                    .setFirstResult(0)
                    .setMaxResults(10)
                    .list();

             transaction.commit();
             return players;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public Player findById(long id) {

        try (Session session = HibernateUtil.openSession()) {
            Transaction transaction = session.beginTransaction();
            log.trace("Transaction is created: {}", transaction);

            Player player = session.byId(Player.class).load(id);

            transaction.commit();
            return player;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Player findByName(String name) {

        String hql = "SELECT p FROM Player AS p WHERE name = :playerName";

        try (Session session = HibernateUtil.openSession()) {
            Transaction transaction = session.beginTransaction();
            log.trace("Transaction is created: {}", transaction);

            Player player = session.createQuery(hql, Player.class)
                    .setParameter("playerName", name)
                    .uniqueResult();

            transaction.commit();
            return player;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public Player save(String name) {
        try (Session session = HibernateUtil.openSession()) {
            Transaction transaction = session.beginTransaction();
            log.trace("Transaction is created: {}", transaction);

            Player player = Player.builder()
                    .name(name)
                    .build();

            session.persist(player);
            transaction.commit();

            return player;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }


}