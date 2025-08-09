package dev.lqwd.utils;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.h2.tools.Server;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.util.HashMap;
import java.util.Map;

@UtilityClass
@Slf4j
public final class HibernateUtil {

    private static SessionFactory sessionFactory;
    private final static String PASSWORD = System.getenv("PASS_DB");
    private final static String USER = System.getenv("USER_DB");

    public static void openSessionFactory() {
        if (sessionFactory == null) {
            try {

                Server.createWebServer("-web", "-webAllowOthers", "-webPort", "8083").start();
                Server.createTcpServer("-tcp", "-tcpAllowOthers", "-tcpPort", "9093").start();

                StandardServiceRegistry standardRegistry = new StandardServiceRegistryBuilder()
                        .configure("hibernate.cfg.xml")
                        .applySettings(getHibernateParameters())
                        .build();

                MetadataSources sources = new MetadataSources(standardRegistry);
                Metadata metadata = sources.getMetadataBuilder().build();

                sessionFactory = metadata.getSessionFactoryBuilder().build();

                log.info("sessionFactory is build: {}}", sessionFactory);
            } catch (Exception e) {

                log.error("Error on initialisation BD, message: {}, stack.trace: {}", e.getMessage(), e.getStackTrace());
                throw new RuntimeException("Error on initialisation BD", e);

            }
        }
    }

    private static Map<String, Object> getHibernateParameters() {
        Map<String, Object> parameters = new HashMap<>();

        parameters.put("hibernate.hikari.username", USER);
        parameters.put("hibernate.hikari.password", PASSWORD);

        return parameters;

    }

    public static Session openSession() throws RuntimeException {

        return sessionFactory.openSession();

    }

    public static void closeSessionFactory() {

        if (sessionFactory != null) {
            sessionFactory.close();
        }

    }

}
