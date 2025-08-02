package dev.lqwd.utils;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.h2.tools.Server;

@Slf4j
@WebListener
public final class HibernateUtil implements ServletContextListener {

    private static SessionFactory sessionFactory;
    private final static String user = System.getenv("DB_USER");
    private final static String password = System.getenv("DB_PASSWORD");


    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {

            Server.createWebServer("-web", "-webAllowOthers", "-webPort", "8082").start();
            Server.createTcpServer("-tcp", "-tcpAllowOthers", "-tcpPort", "9092").start();

            Configuration configuration = new Configuration();

            configuration.setProperty("hibernate.connection.username", user);
            configuration.setProperty("hibernate.connection.password", password);

            configuration.configure("hibernate.cfg.xml");

            sessionFactory = configuration.buildSessionFactory();
            log.info("sessionFactory is build: {}}", sessionFactory);
        } catch (Exception e) {
            log.error("Error on initialisation BD, message: {}, stack.trace: {}", e.getMessage(), e.getStackTrace());
            throw new RuntimeException("Error on initialisation BD", e);
        }
    }

    public static Session openSession() throws RuntimeException {
        return sessionFactory.openSession();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }
}
