package dev.lqwd;

import dev.lqwd.exception.DataBaseException;
import dev.lqwd.utils.HibernateUtil;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Slf4j
@jakarta.servlet.annotation.WebListener
public class WebListener implements ServletContextListener {

    private static final String PATH_TO_INIT_DATA = "/sql_scripts/2_init_data.sql";
    
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        HibernateUtil.openSessionFactory();
        loadInitData();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        HibernateUtil.closeSessionFactory();
    }

    private static void loadInitData(){
        try (Session session = HibernateUtil.openSession();
             InputStream is = HibernateUtil.class.getResourceAsStream(PATH_TO_INIT_DATA)) {

            Transaction transaction = session.beginTransaction();

            if (is == null){
                log.error("Initial load error: InputStream is null");
                throw new DataBaseException("DB error");
            }

            String sql = new String(is.readAllBytes(), StandardCharsets.UTF_8);

            session.createNativeMutationQuery(sql).executeUpdate();

            transaction.commit();
        } catch (Exception e) {
            log.error("Initial load error: {}, stack.trace: {}", e.getMessage(), e.getStackTrace());
            throw new DataBaseException("DB error");
        }
    }

}
