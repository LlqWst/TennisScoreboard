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

        String hql = "SELECT count(m) FROM Match m" + SPACE;

        if (!name.isEmpty()) {

            hql += """
                    WHERE upper(m.player1.name) = upper(:playerName)
                    OR upper(m.player2.name) = upper(:playerName)
                    """;

        }

        try (Session session = HibernateUtil.openSession()) {

            Query<Long> query = session.createQuery(hql, Long.class);

            if (!name.isEmpty()) {
                query.setParameter("playerName", name);
            }

            return query.getSingleResult();

        } catch (Exception e) {
            log.error("Failed to read all Matches for name: {}", name);
            throw new DataBaseException(COUNT_W_FILTERS_DB_ERROR.formatted(name));
        }

    }

    public Optional<List<Match>> findAllByFilters(FinishedMatchRequestDto finishedMatchRequestDto) {

        int page = finishedMatchRequestDto.getPage();
        int maxSize = finishedMatchRequestDto.getMaxSize();
        String name = finishedMatchRequestDto.getName();

        String hql = """
                             SELECT m FROM Match m
                             JOIN FETCH m.player1
                             JOIN FETCH m.player2
                             JOIN FETCH m.winner
                             """
                     + SPACE;

        if (!name.isEmpty()) {
            hql += """
                    WHERE upper(m.player1.name) = upper(:playerName)
                       OR upper(m.player2.name) = upper(:playerName)
                    """;
        }

        try (Session session = HibernateUtil.openSession()) {

            Query<Match> query = session.createQuery(hql, Match.class)
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

}
