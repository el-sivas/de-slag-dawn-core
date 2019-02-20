package de.slag.core.logic;

import de.slag.core.data.Dao;
import de.slag.root.base.ModelBean;

public abstract class AbstractBusinessService<M extends ModelBean> {
	
	protected abstract Dao<M> getDao();

	public void save(M bean) {
		getDao().save(bean);

	}

	public void delete(M bean) {
		getDao().delete(bean);

	}

	public M loadById(Long id) {
		return getDao().loadById(id);
	}

}
