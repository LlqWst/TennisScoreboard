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

    private static final String PATH_TO_INIT_DATA = "/sql_scripts/init_data.sql";
    private static final String INIT_SCRIPT_ERROR_MESSAGE = "DB Error: file with init data wasn't found";
    private static final String DB_ERROR_MESSAGE = "DB Error";
    
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
             InputStream inputStream = HibernateUtil.class.getResourceAsStream(PATH_TO_INIT_DATA)) {

            Transaction transaction = session.beginTransaction();

            if (inputStream == null){
                log.error("Initial load error: InputStream is null");
                throw new DataBaseException(INIT_SCRIPT_ERROR_MESSAGE);
            }

            String sql = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);

            session.createNativeMutationQuery(sql).executeUpdate();

            transaction.commit();
        } catch (Exception e) {
            log.error("Initial load error: {}, stack.trace: {}", e.getMessage(), e.getStackTrace());
            throw new DataBaseException(DB_ERROR_MESSAGE);
        }
    }

}
