package cz.muni.fi.pa165.currency;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Currency;


/**
 * This is base implementation of {@link CurrencyConvertor}.
 *
 * @author petr.adamek@embedit.cz
 */
public class CurrencyConvertorImpl implements CurrencyConvertor {

    private final ExchangeRateTable exchangeRateTable;
    //private final Logger logger = LoggerFactory.getLogger(CurrencyConvertorImpl.class);

    public CurrencyConvertorImpl(ExchangeRateTable exchangeRateTable) {
        this.exchangeRateTable = exchangeRateTable;
    }

    @Override
    public BigDecimal convert(Currency sourceCurrency, Currency targetCurrency, BigDecimal sourceAmount) {
        if (sourceCurrency == null) throw new IllegalArgumentException("sourceCurrency argument is empty.");
        if (targetCurrency == null) throw new IllegalArgumentException("targetCurrency argument is empty.");
        if (sourceAmount == null) throw new IllegalArgumentException("sourceAmount argument is empty.");

        BigDecimal rate;
        try {
            rate = exchangeRateTable.getExchangeRate(sourceCurrency, targetCurrency);
        } catch (ExternalServiceFailureException e) {
            throw new UnknownExchangeRateException("Lookup for current exchange rate failed.", e);
        }

        if (rate == null) throw new UnknownExchangeRateException(
                "The exchange rate for specified pair of currencies is unknown.");

        BigDecimal result = sourceAmount.multiply(rate);
        result = result.setScale(2, RoundingMode.HALF_UP);
        return result;
    }

}
