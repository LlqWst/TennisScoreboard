package dev.lqwd.dao;

import dev.lqwd.dto.MatchFilterRequestDto;
import dev.lqwd.entity.Match;
import dev.lqwd.exception.DataBaseException;
import dev.lqwd.utils.HibernateUtil;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;
import java.util.Optional;

@Slf4j
public class MatchesDao {

    private final static String READ_ID_DB_ERROR = "Failed to read Match id '%d' from the database";
    private final static String READ_ALL_DB_ERROR = "Failed to read Matches from the database";
    private final static String SAVE_DB_ERROR = "Failed to save Match '%s' to the database";
    private final static String READ_ALL_W_FILTERS_DB_ERROR = "Failed to read Matches from the database w filters: %s, %s";
    private final static String COUNT_W_FILTERS_DB_ERROR = "Failed to count Matches from the database w name: %s";

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
            log.error("Failed to read all Matches");
            throw new DataBaseException(READ_ALL_DB_ERROR);
        }

    }

    public long countPlayedMatches(String name){

        String hql = "SELECT count(m) FROM Match m";

        if(name != null) {

            hql = """
            SELECT count(m) FROM Match m
            WHERE upper(m.player1.name) = upper(:playerName)
            OR upper(m.player2.name) = upper(:playerName)
            """;

        }

        try (Session session = HibernateUtil.openSession()) {
            Transaction transaction = session.beginTransaction();

            log.trace("Transaction is created: {}", transaction);

            Query<Long> query = session.createQuery(hql, Long.class);

            if (name != null) {
                query.setParameter("playerName", name);
            }

            long count = query.getSingleResult();

            transaction.commit();
            return count;

        } catch (Exception e) {
            log.error("Failed to read all Matches w name: {}", name);
            throw new DataBaseException(COUNT_W_FILTERS_DB_ERROR.formatted(name));
        }

    }

    public List<Match> findAllByFilters(MatchFilterRequestDto matchFilterRequestDto) {

        int page = matchFilterRequestDto.getPage();
        int maxSize = matchFilterRequestDto.getMaxSize();
        String name = matchFilterRequestDto.getName();

        String hql = """
                SELECT m FROM Match m
                JOIN FETCH m.player1
                JOIN FETCH m.player2
                JOIN FETCH m.winner
                """;

        if (name != null){

            hql = """
                SELECT m FROM Match m
                JOIN FETCH m.player1
                JOIN FETCH m.player2
                JOIN FETCH m.winner
                WHERE upper(m.player1.name) = upper(:playerName)
                   OR upper(m.player2.name) = upper(:playerName)
                """;

        }

        try (Session session = HibernateUtil.openSession()) {
            Transaction transaction = session.beginTransaction();
            log.trace("Transaction is created: {}", transaction);

            Query<Match> query = session.createQuery(hql, Match.class)
                    .setFirstResult((page -1) * maxSize)
                    .setMaxResults(maxSize);

            if (name != null){
                query.setParameter("playerName", name);
            }

            List<Match> matches = query.list();

            transaction.commit();
            return matches;

        } catch (Exception e) {
            log.error("Failed to read all Matches w page: {}, name: {}", page, name);
            throw new DataBaseException(READ_ALL_W_FILTERS_DB_ERROR.formatted(page, name));
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
            log.error("Failed to read transaction for id {}:", id);
            throw new DataBaseException(READ_ID_DB_ERROR.formatted(id));
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
            log.error("Failed to save transaction for match {}:", match);
            throw new DataBaseException(SAVE_DB_ERROR.formatted(match));
        }
    }

}
