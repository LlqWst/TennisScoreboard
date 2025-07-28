import dev.lqwd.dto.MatchFilterRequestDto;
import dev.lqwd.entity.Match;
import dev.lqwd.entity.Player;
import dev.lqwd.exception.DataBaseException;
import dev.lqwd.utils.HibernateUtil;
import lombok.Cleanup;
import lombok.extern.slf4j.Slf4j;
import org.h2.tools.Server;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
public class MatchesDaoTest {

    private final static String SAVE_DB_ERROR = "Failed to save Player '%s' to the database";
    private static SessionFactory sessionFactory;
    private Session session;

    @BeforeAll
    static void setup() {
        try {
            Server.createWebServer("-web", "-webAllowOthers", "-webPort", "8082").start();
            Server.createTcpServer("-tcp", "-tcpAllowOthers", "-tcpPort", "9092").start();

            Configuration configuration = new Configuration();

            configuration.setProperty("hibernate.connection.username", "sa");
            configuration.setProperty("hibernate.connection.password", "");
            configuration.configure("hibernate.cfg.xml");
            sessionFactory = configuration.buildSessionFactory();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void save() throws SQLException {

        Match match = Match.builder()
                .player1(Player.builder()
                        .id(1L)
                        .build())
                .player2(Player.builder()
                        .id(2L)
                        .build())
                .winner(Player.builder()
                        .id(2L)
                        .build())
                .build();

        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();

            session.persist(match);
            assertNotNull(match.getId());
            transaction.commit();


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Test
    public void findAll() {

        String hql = """
                SELECT m FROM Match m
                JOIN FETCH m.player1
                JOIN FETCH m.player2
                JOIN FETCH m.winner
                """;

        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();

            List<Match> matches = session.createQuery(hql, Match.class)
                    .setFirstResult(0)
                    .setMaxResults(10)
                    .list();

            System.out.println(matches);
            transaction.commit();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Test
    public void countPlayedMatches(){

        String name = null;
        String hql = "SELECT count(m) FROM Match m";

        if(name != null) {

            hql = """
            SELECT count(m) FROM Match m
            WHERE upper(m.player1.name) = upper(:playerName)
            OR upper(m.player2.name) = upper(:playerName)
            """;

        }

        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            Query<Long> query = session.createQuery(hql, Long.class);

            if (name != null) {
                query.setParameter("playerName", name);
            }

            long count = query.getSingleResult();

            System.out.println(count);
            assertEquals(9, count);
            transaction.commit();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Test
    public void findAllByFilters() {

        int page = 2;
        int maxSize = 4;
        String name = null;

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

        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            log.trace("Transaction is created: {}", transaction);

            Query<Match> query = session.createQuery(hql, Match.class)
                    .setFirstResult((page -1) * maxSize)
                    .setMaxResults(maxSize);

            if (name != null){
                query.setParameter("playerName", name);
            }

            List<Match> matches = query.list();
            System.out.println(matches);

            transaction.commit();

        } catch (Exception e) {
            log.error("Failed to read all Matches w page: {}, name: {}", page, name);
            e.printStackTrace();
        }

    }

    @Test
    public void find(){

        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();

            Match match1 =  session.byId(Match.class).load(2L);
            System.out.println(match1);
            transaction.commit();


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
