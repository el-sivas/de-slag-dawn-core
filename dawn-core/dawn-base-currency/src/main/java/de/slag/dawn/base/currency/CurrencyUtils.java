package de.slag.dawn.base.currency;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.money.CurrencyUnit;
import javax.money.Monetary;
import javax.money.MonetaryAmount;
import javax.money.convert.CurrencyConversion;
import javax.money.convert.MonetaryConversions;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class CurrencyUtils {

	private static final Log LOG = LogFactory.getLog(CurrencyUtils.class);

	private static final String EUR = "EUR";

	private static final double ZERO = Double.valueOf(0);
	
	private static final Map<CurrencyUnit, CurrencyConversion> CONVERSIONS = new HashMap<>();

	private static final Map<CurrencyUnit, LocalDateTime> LAST_UPDATE = new HashMap<>();

	public static MonetaryAmount newAmount() {
		return newAmount(ZERO, defaultCurrency());
	}

	public static MonetaryAmount newAmount(String currency) {
		return newAmount(ZERO, currency(currency));
	}

	public static MonetaryAmount newAmount(Number number, CurrencyUnit currency) {
		return Monetary.getDefaultAmountFactory().setCurrency(currency).setNumber(number).create();
	}

	public static CurrencyUnit defaultCurrency() {
		final String property = System.getProperty("DefaultCurrency");
		if (property == null) {
			LOG.debug("No default currency found. Use: " + EUR);
			return currency(EUR);
		}
		return currency(property);
	}

	public static CurrencyUnit currency(String currency) {
		Objects.requireNonNull(currency, "currency not set");
		return Monetary.getCurrency(currency);
	}

	public static MonetaryAmount newAmount(int number, String currency) {
		return newAmount(Integer.valueOf(number), currency(currency));
	}

	public static MonetaryAmount add(MonetaryAmount a, MonetaryAmount b) {
		return a.add(inCurrency(a.getCurrency(), b));
	}

	public static MonetaryAmount subtract(MonetaryAmount a, MonetaryAmount b) {
		return a.subtract(inCurrency(a.getCurrency(), b));
	}

	private static MonetaryAmount inCurrency(CurrencyUnit currency, MonetaryAmount b) {
		if (currency.equals(b.getCurrency())) {
			return b;
		}
		return b.with(getConversion(currency));
	}

	public static CurrencyConversion getConversion(CurrencyUnit currency) {
		if (!CONVERSIONS.containsKey(currency) || isOutdated(currency)) {

			LOG.debug(MonetaryConversions.getDefaultConversionProviderChain());

			// warten vor der Abfrage. Dauert manchmal etwas.
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
			CONVERSIONS.put(currency, MonetaryConversions.getConversion(currency));
			LAST_UPDATE.put(currency, LocalDateTime.now());
		}
		return CONVERSIONS.get(currency);
	}

	private static boolean isOutdated(CurrencyUnit currency) {
		final LocalDateTime lastUpdated = LAST_UPDATE.get(currency);
		if (lastUpdated == null) {
			return true;
		}

		if (lastUpdated.isBefore(LocalDateTime.now().minusDays(1))) {
			return true;
		}
		return false;
	}

	public static MonetaryAmount toAmount(String amountAsString) {
		if (StringUtils.isEmpty(amountAsString)) {
			return null;
		}
		final String[] split = amountAsString.split(" ");
		final CurrencyUnit currency = currency(split[0]);
		final Double valueOf = Double.valueOf(split[1]);
		return newAmount(valueOf, currency);
	}

}
