package de.slag.dawn.core.persist;

import org.hibernate.Session;
import org.junit.Assert;
import org.junit.Test;

import de.slag.root.base.DatabaseConfig;
import de.slag.root.base.SlagConfigSupport;

public class SimpleHibernateSupportTest {

	@Test
	public void test() {
		try (Session session = SimpleHibernateSupport.openSession()) {
			Assert.assertNotNull(session);
		}
	}
	
	@Test
	public void testDbValidate() {
		SimpleHibernateSupport.validateDatabase(SlagConfigSupport.getDefault(DatabaseConfig.class));
	}
	
	@Test
	public void testDbUpdate() {
		SimpleHibernateSupport.updateDatabase(SlagConfigSupport.getDefault(DatabaseConfig.class));
	}
}
