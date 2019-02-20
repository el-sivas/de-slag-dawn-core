package de.slag.core.data;

import de.slag.root.base.ModelBean;

public interface Dao<M extends ModelBean> {
	
	void save(M bean);
	
	void delete(M bean);
	
	M loadById(Long id);
	
	

}
