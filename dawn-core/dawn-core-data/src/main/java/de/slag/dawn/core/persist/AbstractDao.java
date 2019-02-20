package de.slag.dawn.core.persist;

import java.util.Date;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.hibernate.Session;

import de.slag.root.base.ModelBean;

public abstract class AbstractDao<M extends ModelBean, P extends PersistBean> {

	protected abstract Class<P> getPersistentClass();

	protected abstract Supplier<M> getSupplier();

	public void delete(M bean) {
		final Date now = new Date();
		if(bean.getValidUntil().after(now)) {
			bean.setValidUntil(now);
		}
		final P persist = loadPersistById(bean.getId());
		mapData(bean, persist);
		saveOrUpdate(persist);		
	}

	public void save(M bean) {
		final P persist = loadPersistById(bean.getId());
		mapData(bean, persist);
		saveOrUpdate(persist);
	}

	private void saveOrUpdate(P persist) {
		execute(session -> session.saveOrUpdate(persist));
	}

	public M loadById(Long id) {
		final P persist = loadPersistById(id);
		final M bean = getSupplier().get();
		mapData(persist, bean);
		return bean;
	}
	
	private P loadPersistById(Long id) {
		try (Session sessin = session()) {
			return (P) session().load(getPersistentClass(), id);
		}
	}

	private void execute(Consumer<Session> consumer) {
		try (final Session session = session()) {
			consumer.accept(session);
		}
	}

	private Session session() {
		return SimpleHibernateSupport.openSession();
	}

	private void mapData(Object from, Object to) {
		DataMappingUtils.map(from, to);
	}
}
