package de.slag.central.data;

import java.math.MathContext;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.money.Monetary;
import javax.money.MonetaryAmount;

import de.slag.dawn.base.utils.KeyValueUtils;

public interface ElsePropertiesEntity {

	MathContext DEFAULT_MC = new MathContext(7, RoundingMode.HALF_UP);

	String DEFAULT_SDF = "yyyy.MM.dd HH:mm:ss";

	String BLANK = " ";

	// String
	default String getElseProperty(String key) {
		Map<String, String> split = KeyValueUtils.split(getElseProperties());
		return split.getOrDefault(key, null);
	}

	default void setElseProperty(String key, String value) {
		Map<String, String> split = KeyValueUtils.split(getElseProperties());
		split.put(key, value);
		setElseProperties(KeyValueUtils.join(split));
	}

	// Long
	default Long getElseLong(String key) {
		return Long.valueOf(getElseProperty(key));
	}

	default void setElseLong(String key, Long value) {
		if (value == null) {
			setNull(key);
			return;
		}
		setElseProperty(key, value.toString());
	}

	// Double
	default Double getElseDouble(String key) {
		return Double.valueOf(getElseProperty(key));
	}

	default void setElseLong(String key, Double value) {
		if (value == null) {
			setNull(key);
			return;
		}
		setElseProperty(key, value.toString());
	}

	// MonetaryAmount
	default MonetaryAmount getElseAmount(String key) {
		final String elseProperty = getElseProperty(key);
		if (elseProperty == null) {
			return null;
		}
		final String[] split = elseProperty.split(BLANK);
		final Double value = Double.valueOf(split[0]);
		final String currencyCode = split[1];
		return Monetary.getDefaultAmountFactory().setNumber(value).setCurrency(currencyCode).create();
	}

	default void setElseAmount(String key, MonetaryAmount amount) {
		if (amount == null) {
			setNull(key);
			return;
		}
		String currencyCode = amount.getCurrency().getCurrencyCode();
		String value = Double.valueOf(amount.getNumber().round(DEFAULT_MC).doubleValueExact()).toString();
		setElseProperty(key, value + BLANK + currencyCode);
	}

	// Date
	default Date getElseDate(String key) {
		final String elseProperty = getElseProperty(key);
		if (elseProperty == null) {
			return null;
		}
		try {
			return new SimpleDateFormat(DEFAULT_SDF).parse(elseProperty);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}

	default void setElseDate(String key, Date value) {
		if (value == null) {
			setNull(key);
			return;
		}
		setElseProperty(key, new SimpleDateFormat(DEFAULT_SDF).format(value));
	}

	default void setNull(String key) {
		setElseProperty(key, null);
	}

	void setElseProperties(String join);

	String getElseProperties();

}
