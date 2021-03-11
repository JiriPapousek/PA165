package cz.muni.fi.pa165.currency;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.Currency;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;


import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class CurrencyConvertorImplTest {

    private static final Currency CZK = Currency.getInstance("CZK");
    private static final Currency EUR = Currency.getInstance("EUR");

    @Mock
    private ExchangeRateTable exchangeRateTable;

    private CurrencyConvertor convertor;

    @Before
    public void init() {
        convertor = new CurrencyConvertorImpl(exchangeRateTable);
    }

    @Test
    public void testConvert() throws ExternalServiceFailureException {
        when(exchangeRateTable.getExchangeRate(CZK, EUR)).thenReturn(new BigDecimal("0.04"));
        assertThat(convertor.convert(CZK, EUR, new BigDecimal("100"))).isEqualTo(new BigDecimal("4.00"));
        assertThat(convertor.convert(CZK, EUR, new BigDecimal("1"))).isEqualTo(new BigDecimal("0.04"));
        assertThat(convertor.convert(CZK, EUR, new BigDecimal("0"))).isEqualTo(new BigDecimal("0.00"));
        assertThat(convertor.convert(CZK, EUR, new BigDecimal("250.3725"))).isEqualTo(new BigDecimal("10.01"));
        assertThat(convertor.convert(CZK, EUR, new BigDecimal("250.375"))).isEqualTo(new BigDecimal("10.02"));
        assertThat(convertor.convert(CZK, EUR, new BigDecimal("28.125"))).isEqualTo(new BigDecimal("1.12"));

    }

    @Test
    public void testConvertWithNullSourceCurrency() {
        assertThatThrownBy(()->{convertor.convert(null, EUR, new BigDecimal("100"));})
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void testConvertWithNullTargetCurrency() {
        assertThatThrownBy(()->{convertor.convert(EUR, null, new BigDecimal("100"));})
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void testConvertWithNullSourceAmount() throws ExternalServiceFailureException {
        assertThatThrownBy(()->{convertor.convert(CZK, EUR, null);})
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void testConvertWithUnknownCurrency() throws ExternalServiceFailureException {
        when(exchangeRateTable.getExchangeRate(CZK, EUR)).thenReturn(null);
        assertThatThrownBy(()->{convertor.convert(CZK, EUR, new BigDecimal("100"));})
                .isInstanceOf(UnknownExchangeRateException.class);
    }

    @Test
    public void testConvertWithExternalServiceFailure() throws ExternalServiceFailureException {
        when(exchangeRateTable.getExchangeRate(CZK, EUR)).thenThrow(ExternalServiceFailureException.class);
        assertThatThrownBy(()->{convertor.convert(CZK, EUR, new BigDecimal("100"));})
                .isInstanceOf(UnknownExchangeRateException.class);
    }

}
