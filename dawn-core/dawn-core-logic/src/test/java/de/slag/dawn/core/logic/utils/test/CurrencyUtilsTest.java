package de.slag.dawn.core.logic.utils.test;

import java.util.Currency;

import javax.money.CurrencyUnit;
import javax.money.Monetary;
import javax.money.MonetaryAmount;

import org.junit.Assert;
import org.junit.Test;

import de.slag.core.logic.utils.CurrencyUtils;

public class CurrencyUtilsTest {
	
	@Test
	public void test() {
		MonetaryAmount newAmount = CurrencyUtils.newAmount();
		Assert.assertNotNull(newAmount);
		
		MonetaryAmount usd = CurrencyUtils.newAmount(1, "USD");
		MonetaryAmount eur = CurrencyUtils.newAmount(1, "EUR");
		
		MonetaryAmount add = CurrencyUtils.add(eur, usd);
		
		MonetaryAmount add2 = CurrencyUtils.add(add, CurrencyUtils.newAmount(2, "USD"));
		
		CurrencyUtils.toAmount(add2.toString());
		
		
	
	}
	
	@Test
	public void testMonetaryAmount() {
		CurrencyUnit eur = Monetary.getCurrency("EUR");
		MonetaryAmount amount = Monetary.getDefaultAmountFactory().setCurrency(eur).setNumber(200).create();

		
	}
	
	@Test
	public void testCurrencyUnit() {
		CurrencyUnit eur = Monetary.getCurrency("EUR");
		Assert.assertNotNull(eur);		
	}
}
