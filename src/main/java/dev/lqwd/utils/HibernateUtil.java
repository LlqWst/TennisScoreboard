package dev.lqwd.utils;

import dev.lqwd.entity.Player;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

@Slf4j
@WebListener
public final class HibernateUtil implements ServletContextListener {

    private static SessionFactory sessionFactory;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {

            Configuration configuration = new Configuration();
            configuration.configure();

            sessionFactory = configuration.buildSessionFactory();
            log.info("sessionFactory is build: {}}", sessionFactory);
        } catch (Exception e) {
            throw new RuntimeException("Error on initialisation BD", e);
        }
    }

    public static Session openSession() throws RuntimeException{
        return sessionFactory.openSession();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        if(sessionFactory != null){
            sessionFactory.close();
        }
    }
}
