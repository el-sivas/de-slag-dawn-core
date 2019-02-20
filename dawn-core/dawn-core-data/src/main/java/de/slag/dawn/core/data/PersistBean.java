package de.slag.dawn.core.data;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.MappedSuperclass;

import de.slag.root.base.DateUtils;
import de.slag.root.base.SlagConstants;

@MappedSuperclass
public abstract class PersistBean implements ElseAttributesEntity {

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

	public Date getCreatedAt() {
		return createdAt;
	}

	@Override
	public String getElseAttributes() {
		return elseAttributes;
	}

	@Override
	public void setElseAttributes(String elseAttributes) {
		this.elseAttributes = elseAttributes;
	}

}
