package de.slag.dawn.core.persist;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.function.Consumer;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.Before;
import org.junit.Test;

import de.slag.root.base.DatabaseConfig;
import de.slag.root.base.SlagConfigSupport;

public class SimpleHibernateReadWriteTest {

	@Before
	public void init() {
		SimpleHibernateSupport.updateDatabase(SlagConfigSupport.getDefault(DatabaseConfig.class));
	}

	@Test
	public void testSelect() {
		Collection<Credential> all = new ArrayList<>();
		Consumer<Session> consumer = session -> {

			final Collection<Long> ids = SimpleHibernateSupport.HqlSupport.selectIds(session, Credential.class);

			ids.forEach(id -> {
				final Credential load = session.load(Credential.class, id);
				all.add((Credential) Hibernate.unproxy(load));
			});

		};
		execute(consumer);
		out(all);
	}

	@Test
	public void testLoad() {
		final Consumer<Session> consumer = session -> {
			final Credential load = session.load(Credential.class, 17l);
			out(load);
		};
		execute(consumer);
	}

	private void out(final Object o) {
		System.out.println(o);
	}

	@Test
	public void testWrite() {
		final Consumer<Session> consumer = session -> {
			final Credential credential = new Credential();
			credential.setName("test-" + new Date().getTime());
			session.save(credential);
		};
		execute(consumer);
	}

	private void execute(Consumer<Session> consumer) {
		try (final Session session = SimpleHibernateSupport.openSession()) {
			final Transaction tx = session.beginTransaction();
			consumer.accept(session);
			tx.commit();
		}
	}
}
