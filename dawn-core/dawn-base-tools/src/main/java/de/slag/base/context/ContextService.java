package de.slag.base.context;

public interface ContextService {
	
	default <T> T getBean(Class<T> c) {
		return SlagContext.getBean(c);
	}

}
