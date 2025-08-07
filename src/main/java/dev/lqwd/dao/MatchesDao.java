package dev.lqwd.dao;

import dev.lqwd.dto.finished_match.FinishedMatchRequestDto;
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
    private final static String SPACE = " ";

    public Optional<List<Match>> findAll() {

        String hql = """
                SELECT m FROM Match m
                JOIN FETCH m.player1
                JOIN FETCH m.player2
                JOIN FETCH m.winner
                """;

        try (Session session = HibernateUtil.openSession()) {

            return Optional.of(session.createQuery(hql, Match.class)
                    .setFirstResult(0)
                    .setMaxResults(10)
                    .list()
            );

        } catch (Exception e) {
            log.error("Failed to read all Matches");
            throw new DataBaseException(READ_ALL_DB_ERROR);
        }

    }

    public long countPlayedMatches(String name) {

        StringBuilder hql = new StringBuilder("SELECT count(m) FROM Match m").append(SPACE);

        if (!name.isEmpty()) {

            hql.append(getFilterByName());

        }

        try (Session session = HibernateUtil.openSession()) {

            Query<Long> query = session.createQuery(hql.toString(), Long.class);

            if (!name.isEmpty()) {
                query.setParameter("playerName", name);
            }

            return query.getSingleResult();

        } catch (Exception e) {
            log.error("Failed to read all Matches for name: {}", name);
            throw new DataBaseException(COUNT_W_FILTERS_DB_ERROR.formatted(name));
        }

    }

    public Optional<List<Match>> findAllByFilters(FinishedMatchRequestDto finishedMatchRequestDto, int maxSize) {

        int page = finishedMatchRequestDto.getPage();
        String name = finishedMatchRequestDto.getName();

        StringBuilder hql = new StringBuilder("""
                             SELECT m FROM Match m
                             JOIN FETCH m.player1
                             JOIN FETCH m.player2
                             JOIN FETCH m.winner
                             """).append(SPACE);

        if (!name.isEmpty()) {

            hql.append(getFilterByName());

        }

        try (Session session = HibernateUtil.openSession()) {

            Query<Match> query = session.createQuery(hql.toString(), Match.class)
                    .setFirstResult((page - 1) * maxSize)
                    .setMaxResults(maxSize);

            if (!name.isEmpty()) {
                query.setParameter("playerName", name);
            }

            return Optional.of(query.list());

        } catch (Exception e) {
            log.error("Failed to read all Matches w page: {}, name: {}", page, name);
            throw new DataBaseException(READ_ALL_W_FILTERS_DB_ERROR.formatted(page, name));
        }

    }

    public Optional<Match> findById(long id) {

        try (Session session = HibernateUtil.openSession()) {

            return Optional.of(session.byId(Match.class).load(id));

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
            log.error("Failed to save transaction for matchScoreDto {}:", match);
            throw new DataBaseException(SAVE_DB_ERROR.formatted(match));
        }
    }

    private static String getFilterByName() {
        return """
                WHERE m.player1.name ILIKE CONCAT('%', :playerName, '%')
                OR m.player2.name ILIKE CONCAT('%', :playerName, '%')
                """;
    }

}
