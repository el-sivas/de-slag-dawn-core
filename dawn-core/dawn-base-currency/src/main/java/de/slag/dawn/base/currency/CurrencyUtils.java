package de.slag.dawn.base.currency;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.money.CurrencyUnit;
import javax.money.Monetary;
import javax.money.MonetaryAmount;
import javax.money.convert.CurrencyConversion;
import javax.money.convert.MonetaryConversions;

public class CurrencyUtils {

	private static Map<CurrencyUnit, CurrencyConversion> conversions = new HashMap<>();

	private static double ZERO = Double.valueOf(0);

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
		return currency("EUR");
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
		if (!conversions.containsKey(currency)) {

			List<String> defaultConversionProviderChain = MonetaryConversions.getDefaultConversionProviderChain();

			// warten vor der Abfrage. Dauert manchmal etwas.
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
			conversions.put(currency, MonetaryConversions.getConversion(currency));
		}
		return conversions.get(currency);
	}

}
