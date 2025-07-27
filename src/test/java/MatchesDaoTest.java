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
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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
            throw new DataBaseException(SAVE_DB_ERROR.formatted(match));
        }

    }

    @Test
    public void find(){

        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();

            Match match1 =  session.byId(Match.class).load(6L);
            System.out.println(match1);
            transaction.commit();


        } catch (Exception e) {
            throw new DataBaseException(SAVE_DB_ERROR.formatted("match1"));
        }

    }

}
