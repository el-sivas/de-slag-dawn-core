package de.slag.central.data;

import java.util.Date;
import java.util.function.Function;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.MappedSuperclass;

import de.slag.dawn.base.features.SlagConstants;
import de.slag.dawn.base.utils.DateUtils;

@MappedSuperclass
public abstract class PersistBean {

	private static final String ELSE_ATTRIBUTE_SEPARATOR = ";";

	private static final String ELSE_ATTRIBUTE_KV_SEPARATOR = "=";

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id;

	@Column
	private Date validUntil = DateUtils.toDate(SlagConstants.END_OF_DEKAMILLENIAL);

	@Column
	private Date createdAt = new Date();

	@Column
	private Date lastUpdate;

	@Column
	@Lob
	private String elseAttributes;

	public Long getId() {
		return id;
	}

	protected void setElseLong(String key, Long value) {
		setElseAttribute(key, String.valueOf(value));
	}

	protected void setElseString(String key, String value) {
		setElseAttribute(key, value);
	}

	protected String getElseString(String key) {
		return getElseAttribute(key, (s) -> s);
	}

	protected Long getElseLong(String key) {
		return getElseAttribute(key, (s) -> Long.valueOf(s));
	}

	protected <T> T getElseAttribute(String key, Function<String, T> function) {
		String elseAttribute = getElseAttribute(key);
		if (elseAttribute == null) {
			return null;
		}
		return function.apply(elseAttribute);
	}

	private void setElseAttribute(String key, String value) {
		synchronized (this) {
			elseAttributes = PersistBeanUtils.setElseAttribute(elseAttributes, key, value, ELSE_ATTRIBUTE_SEPARATOR,
					ELSE_ATTRIBUTE_KV_SEPARATOR);
		}
	}

	private String getElseAttribute(String key) {
		return PersistBeanUtils.getElseAttribute(key, elseAttributes, ELSE_ATTRIBUTE_SEPARATOR,
				ELSE_ATTRIBUTE_KV_SEPARATOR);
	}

	public Date getCreatedAt() {
		return createdAt;
	}

}
