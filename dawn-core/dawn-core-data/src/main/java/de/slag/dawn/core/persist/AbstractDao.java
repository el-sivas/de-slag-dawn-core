package de.slag.dawn.core.persist;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.NativeQuery;

import de.slag.dawn.core.persist.map.field.ValidUntil;
import de.slag.root.base.BaseException;
import de.slag.root.base.ModelBean;

public abstract class AbstractDao<M extends ModelBean, P extends PersistBean> {

	private static final Log LOG = LogFactory.getLog(AbstractDao.class);

	protected abstract Class<? extends PersistBean> getPersistentClass();

	protected abstract Supplier<M> getSupplier();

	public void deleteBean(M bean) {
		final Date now = new Date();
		if (bean.getValidUntil().after(now)) {
			bean.setValidUntil(now);
		}
		final P persist = loadPersistById(bean.getId());
		mapData(bean, persist);
		saveOrUpdate(persist);
	}

	public void saveBean(M bean) {
		final Long id = bean.getId();
		final P persist;
		if (id != null) {
			persist = loadPersistById(id);
		} else {
			// neuer Datensatz
			persist = createNewPersistBean();
		}
		mapData(bean, persist);
		saveOrUpdate(persist);
	}

	private P createNewPersistBean() {
		try {
			return (P) getPersistentClass().newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			throw new BaseException(e);
		}
	}

	private void saveOrUpdate(P persist) {
		setLastUpdate(persist);
		execute(session -> session.saveOrUpdate(persist));
	}

	private void setLastUpdate(P proxied) {
		final P persist = (P) Hibernate.unproxy(proxied);
		if (!(persist instanceof PersistBean)) {
			return;
		}
		Field lastUpdateField;
		try {
			lastUpdateField = PersistBean.class.getDeclaredField("lastUpdate");
		} catch (NoSuchFieldException | SecurityException e) {
			throw new BaseException(e);
		}
		lastUpdateField.setAccessible(true);
		try {
			lastUpdateField.set(persist, new Date());
		} catch (IllegalArgumentException | IllegalAccessException e) {
			throw new BaseException(e);
		}
		lastUpdateField.setAccessible(false);

	}

	public M loadById(Long id) {
		final P persist = loadPersistById(id);
		final M bean = getSupplier().get();
		mapData(persist, bean);
		return bean;
	}

	protected P loadPersistById(Long id) {
		try (Session session = session()) {
			return (P) session().load(getPersistentClass(), id);
		}
	}

	protected void execute(Consumer<Session> consumer) {
		try (final Session session = session()) {
			final Transaction tx = session.beginTransaction();
			consumer.accept(session);
			tx.commit();
		}
	}

	private Session session() {
		return SimpleHibernateSupport.openSession();
	}

	protected void mapData(P from, M to) {
		DataMappingUtils.map(from, to);
		ValidUntil.PERSIST_TO_MODEL.accept(from, to);
	}

	protected void mapData(M from, P to) {
		DataMappingUtils.map(from, to);
		ValidUntil.MODEL_TO_PERSIST.accept(from, to);
	}

	protected Collection<P> select(String sql) {
		final Collection<P> results = new ArrayList<>();
		final Consumer<Session> consumer = session -> {
			final NativeQuery<? extends PersistBean> query = session.createNativeQuery(sql, getPersistentClass());
			final List<? extends PersistBean> resultList = query.getResultList();
			results.addAll(resultList.stream().map(e -> (P) e).collect(Collectors.toList()));
		};
		execute(consumer);
		return results;
	}

	protected M model(P persist) {
		final M model = getSupplier().get();
		mapData(persist, model);
		return model;
	}

	protected Collection<Long> findIdsBy(Session session, String where) {
		return SimpleHibernateSupport.HqlSupport.selectIds(session, getPersistentClass(), where);
	}
}
