package de.slag.core.service;

import de.slag.root.base.ModelBean;

public interface BusinessService<M extends ModelBean> {

	void save(M bean);

	void delete(M bean);

	M loadById(Long bean);

}
