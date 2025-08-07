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
    private final static String user = System.getenv("DB_USER");
    private final static String password = System.getenv("DB_PASSWORD");

    public static void openSessionFactory() {
        if (sessionFactory == null) {
            try {

                Server.createWebServer("-web", "-webAllowOthers", "-webPort", "8082").start();
                Server.createTcpServer("-tcp", "-tcpAllowOthers", "-tcpPort", "9092").start();

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

        parameters.put("hibernate.connection.username", user);
        parameters.put("hibernate.connection.password", password);

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
