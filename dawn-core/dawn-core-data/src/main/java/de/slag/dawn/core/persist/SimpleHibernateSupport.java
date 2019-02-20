package de.slag.dawn.core.persist;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.boot.model.naming.PhysicalNamingStrategy;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;

import de.slag.dawn.core.ClassUtils;
import de.slag.root.base.DatabaseConfig;
import de.slag.root.base.SlagConfigSupport;

public class SimpleHibernateSupport {

	private static final Log LOG = LogFactory.getLog(SimpleHibernateSupport.class);

	private final static Map<DatabaseConfig, SessionFactory> SESSION_FACTORYS = new HashMap<>();

	private SimpleHibernateSupport() {

	}

	public static Session openSession() {
		return openSession(SlagConfigSupport.getDefault(DatabaseConfig.class));
	}

	public static Session openSession(final DatabaseConfig dbConfig) {
		return getSessionFactory(dbConfig).openSession();
	}

	public static void validateDatabase(final DatabaseConfig dbConfig) {
		try (final Session session = createSessionFactory(configuration(dbConfig, "validate")).openSession()) {
			// done
		}
	}
	
	public static void updateDatabase(final DatabaseConfig dbConfig) {
		try (final Session session = createSessionFactory(configuration(dbConfig, "update")).openSession()) {
			// done
		}
	}

	private static SessionFactory getSessionFactory(DatabaseConfig dbConfig) {
		if (!SESSION_FACTORYS.containsKey(dbConfig)) {
			final Configuration configuration = configuration(dbConfig);
			final SessionFactory sessionFactory = createSessionFactory(configuration);
			SESSION_FACTORYS.put(dbConfig, sessionFactory);
		}
		return SESSION_FACTORYS.get(dbConfig);
	}

	private static SessionFactory createSessionFactory(final Configuration configuration) {
		final Collection<Class> findAnnotatedClasses = findAnnotatedClasses();
		findAnnotatedClasses.forEach(c -> configuration.addAnnotatedClass(c));
		return configuration.buildSessionFactory(
				new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build());
	}

	private static Collection<Class> findAnnotatedClasses() {
		return ClassUtils.getAllSubclassesOf(PersistBean.class);
	}

	private static Configuration configuration(DatabaseConfig config) {
		return configuration(config, null);
	}

	private static Configuration configuration(DatabaseConfig config, String hbm2ddl) {
		final Configuration configuration = new Configuration();

		configuration.setProperty("hibernate.connection.driver_class", config.getDriverClass());
		configuration.setProperty("hibernate.connection.url", config.getUrl());
		configuration.setProperty("hibernate.connection.username", config.getUsername());
		configuration.setProperty("hibernate.connection.password", config.getPassword());
		configuration.setProperty("hibernate.dialect", config.getDialect());
		configuration.setProperty("hibernate.show_sql", "true");
		configuration.setProperty("hibernate.connection.pool_size", "10");

		if (StringUtils.isNotBlank(hbm2ddl)) {
			configuration.setProperty("hibernate.hbm2ddl.auto", hbm2ddl);
			wieso funktioniert das hier nicht?
		}

		configuration.setPhysicalNamingStrategy(getNamingStrategy());
		return configuration;
	}

	protected static PhysicalNamingStrategy getNamingStrategy() {
		return new PhysicalNamingStrategy() {

			@Override
			public Identifier toPhysicalTableName(Identifier name, JdbcEnvironment jdbcEnvironment) {
				return convert(name);
			}

			@Override
			public Identifier toPhysicalSequenceName(Identifier name, JdbcEnvironment jdbcEnvironment) {
				return convert(name);
			}

			@Override
			public Identifier toPhysicalSchemaName(Identifier name, JdbcEnvironment jdbcEnvironment) {
				return convert(name);
			}

			@Override
			public Identifier toPhysicalColumnName(Identifier name, JdbcEnvironment jdbcEnvironment) {
				return convert(name);
			}

			@Override
			public Identifier toPhysicalCatalogName(Identifier name, JdbcEnvironment jdbcEnvironment) {
				return convert(name);
			}

			private Identifier convert(Identifier identifier) {
				if (identifier == null || StringUtils.isBlank(identifier.getText())) {
					return identifier;
				}

				String regex = "([a-z])([A-Z])";
				String replacement = "$1_$2";
				String newName = identifier.getText().replaceAll(regex, replacement).toLowerCase();
				return Identifier.toIdentifier(newName);
			}
		};
	}
}
