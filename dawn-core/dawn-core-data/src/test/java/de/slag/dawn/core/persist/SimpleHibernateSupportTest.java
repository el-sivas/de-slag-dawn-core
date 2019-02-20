package de.slag.dawn.core.persist;

import org.junit.Assert;
import org.junit.Test;

public class SimpleHibernateSupportTest {
	
	@Test
	public void test() {
		Assert.assertNotNull(SimpleHibernateSupport.openSession());
	}

}
