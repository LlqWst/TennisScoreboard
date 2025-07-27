package dev.lqwd.dao;

import dev.lqwd.entity.Match;
import dev.lqwd.exception.DataBaseException;
import dev.lqwd.utils.HibernateUtil;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

@Slf4j
public class MatchesDao {

    private final static String SAVE_DB_ERROR = "Failed to save Player '%s' to the database";

    public List<Match> findAll() {

        String hql = """
                SELECT m FROM Match m
                JOIN FETCH m.player1
                JOIN FETCH m.player2
                JOIN FETCH m.winner
                """;

        try (Session session = HibernateUtil.openSession()) {
            Transaction transaction = session.beginTransaction();
            log.trace("Transaction is created: {}", transaction);

            List<Match> matches = session.createQuery(hql, Match.class)
                    .setFirstResult(0)
                    .setMaxResults(10)
                    .list();

            transaction.commit();
            return matches;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public Match findById(long id) {
        try (Session session = HibernateUtil.openSession()) {
            Transaction transaction = session.beginTransaction();
            log.trace("Transaction is created: {}", transaction);

            Match match = session.byId(Match.class).load(id);
            transaction.commit();

            return match;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public Match save(Match match) {

        try (Session session = HibernateUtil.openSession()) {
            Transaction transaction = session.beginTransaction();
            log.trace("Transaction is created: {}", transaction);

            session.persist(match);
            transaction.commit();

            return match;

        } catch (Exception e) {
            throw new DataBaseException(SAVE_DB_ERROR.formatted(match));
        }
    }

}
