package de.slag.dawn.core.persist.map.field;

import java.lang.reflect.Field;
import java.util.function.BiConsumer;

import de.slag.dawn.core.persist.PersistBean;
import de.slag.root.base.BaseException;
import de.slag.root.base.ModelBean;
import de.slag.root.base.ReflectionUtils;

public class ValidUntil {

	public static final BiConsumer<Object, Object> PERSIST_TO_MODEL = (m, p) -> {
	
		if (!(p instanceof PersistBean)) {
			return;
		}
		if (!(m instanceof ModelBean)) {
			return;
		}
		final PersistBean persistBean = (PersistBean) p;
		final ModelBean modelBean = (ModelBean) m;
		final Field pbValidUntil = ReflectionUtils.field(PersistBean.class, "validUntil");
		final Field mbValidUntil = ReflectionUtils.field(ModelBean.class, "validUntil");
	
		pbValidUntil.setAccessible(true);
		mbValidUntil.setAccessible(true);
		
		final Object value;
		try {
			value = pbValidUntil.get(persistBean);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			throw new BaseException(e);
		}
		try {
			mbValidUntil.set(modelBean, value);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			throw new BaseException(e);
		}
		
		pbValidUntil.setAccessible(false);
		mbValidUntil.setAccessible(false);
	};
	public static final BiConsumer<Object, Object> MODEL_TO_PERSIST = (p, m) -> {
	
		if (!(p instanceof PersistBean)) {
			return;
		}
		if (!(m instanceof ModelBean)) {
			return;
		}
		final PersistBean persistBean = (PersistBean) p;
		final ModelBean modelBean = (ModelBean) m;
		final Field pbValidUntil = ReflectionUtils.field(PersistBean.class, "validUntil");
		final Field mbValidUntil = ReflectionUtils.field(ModelBean.class, "validUntil");
	
		pbValidUntil.setAccessible(true);
		mbValidUntil.setAccessible(true);
		
		final Object value;
		try {
			value = mbValidUntil.get(modelBean);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			throw new BaseException(e);
		}
		try {
			pbValidUntil.set(persistBean, value);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			throw new BaseException(e);
		}
		
		pbValidUntil.setAccessible(false);
		mbValidUntil.setAccessible(false);
	};

}
