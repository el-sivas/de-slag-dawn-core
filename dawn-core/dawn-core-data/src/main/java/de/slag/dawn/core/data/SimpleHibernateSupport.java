package de.slag.dawn.core.data;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.boot.model.naming.PhysicalNamingStrategy;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;

import de.slag.dawn.core.ClassUtils;
import de.slag.root.base.DatabaseConfig;
import de.slag.root.base.SlagConfigSupport;

public class SimpleHibernateSupport {
	
	private final Map<DatabaseConfig,SessionFactory> sessionFactorys = new HashMap<>();
	
	private static SimpleHibernateSupport instance;
	
	private SimpleHibernateSupport() {		
		
	}
	
	public static SimpleHibernateSupport getInctance() {
		if(instance == null) {
			instance = new SimpleHibernateSupport();
		}
		return instance;
	}
	

	public static Session openSession() {		
		final Configuration configuration = configuration(SlagConfigSupport.getDefault(DatabaseConfig.class));
		findAnnotatedClasses().forEach(c -> configuration.addAnnotatedClass(c));
		return createSessionFactory(configuration).openSession();
	}

	private static SessionFactory createSessionFactory(final Configuration configuration) {
		return configuration.buildSessionFactory(
				new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build());
	}

	private static Collection<Class> findAnnotatedClasses() {
		return ClassUtils.getAllSubclassesOf(PersistBean.class);
	}

	private static Configuration configuration(DatabaseConfig config) {
		final Configuration configuration = new Configuration();

		configuration.setProperty("hibernate.connection.driver_class", config.getDriverClass());
		configuration.setProperty("hibernate.connection.url", config.getUrl());
		configuration.setProperty("hibernate.connection.username", config.getUsername());
		configuration.setProperty("hibernate.connection.password", config.getPassword());
		configuration.setProperty("hibernate.dialect", config.getDialect());
		configuration.setProperty("hibernate.show_sql", "true");
		configuration.setProperty("hibernate.connection.pool_size", "10");
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
