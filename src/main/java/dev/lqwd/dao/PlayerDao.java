package dev.lqwd.dao;

import dev.lqwd.entity.Player;
import dev.lqwd.exception.DataBaseException;
import dev.lqwd.utils.HibernateUtil;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;
import java.util.Optional;

@Slf4j
public class PlayerDao {

    private final static String READ_DB_ERROR = "Failed to read Player name '%s' from the database";
    private final static String READ_ID_DB_ERROR = "Failed to read Player id '%d' from the database";
    private final static String READ_ALL_DB_ERROR = "Failed to read Players from the database";
    private final static String SAVE_DB_ERROR = "Failed to save Player '%s' to the database";


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
            throw new DataBaseException(READ_ALL_DB_ERROR);
        }

    }

    public Optional<Player> findById(long id) {

        try (Session session = HibernateUtil.openSession()) {
            Transaction transaction = session.beginTransaction();
            log.trace("Transaction is created: {}", transaction);

            Player player = session.byId(Player.class).load(id);

            transaction.commit();
            return Optional.of(player);

        } catch (Exception e) {
            throw new DataBaseException(READ_ID_DB_ERROR.formatted(id));
        }
    }

    public Optional<Player> findByName(String name) {

        String hql = "SELECT p FROM Player AS p WHERE upper(name) = upper(:playerName) ";

        try (Session session = HibernateUtil.openSession()) {
            Transaction transaction = session.beginTransaction();

            log.trace("Transaction is created: {}", transaction);

            Player player = session.createQuery(hql, Player.class)
                    .setParameter("playerName", name)
                    .uniqueResult();

            transaction.commit();

            return Optional.ofNullable(player);
        } catch (Exception e) {
            throw new DataBaseException(READ_DB_ERROR.formatted(name));
        }

    }

    public Player save(Player player) {

        try (Session session = HibernateUtil.openSession()) {
            Transaction transaction = session.beginTransaction();
            log.trace("Transaction is created: {}", transaction);

            session.persist(player);
            transaction.commit();

            log.trace("Player saved: {}", player);
            return player;

        } catch (Exception e) {
            throw new DataBaseException(SAVE_DB_ERROR.formatted(player.getName()));
        }

    }


}