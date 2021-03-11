package cz.muni.fi.pa165.currency;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Currency;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is base implementation of {@link CurrencyConvertor}.
 *
 * @author petr.adamek@embedit.cz
 */
public class CurrencyConvertorImpl implements CurrencyConvertor {

    private final ExchangeRateTable exchangeRateTable;
    private final Logger logger = LoggerFactory.getLogger(CurrencyConvertorImpl.class);

    public CurrencyConvertorImpl(ExchangeRateTable exchangeRateTable) {
        this.exchangeRateTable = exchangeRateTable;
    }

    @Override
    public BigDecimal convert(Currency sourceCurrency, Currency targetCurrency, BigDecimal sourceAmount) {
        logger.trace("convert() method has been called, sourceCurrency={}, targetCurrency={}, sourceAmount={}",
                sourceCurrency, targetCurrency, sourceAmount);

        if (sourceCurrency == null) throw new IllegalArgumentException("sourceCurrency argument is empty.");
        if (targetCurrency == null) throw new IllegalArgumentException("targetCurrency argument is empty.");
        if (sourceAmount == null) throw new IllegalArgumentException("sourceAmount argument is empty.");

        BigDecimal rate;
        try {
            rate = exchangeRateTable.getExchangeRate(sourceCurrency, targetCurrency);
        } catch (ExternalServiceFailureException e) {
            logger.error("convert() failed due to ExternalServiceFailureException thrown by ExchangeRateTable");
            throw new UnknownExchangeRateException("Lookup for current exchange rate failed.", e);
        }

        if (rate == null) {
            logger.warn("convert() failed due to missing exchange rate from {} to {}", sourceCurrency, targetCurrency);
            throw new UnknownExchangeRateException("The exchange rate for specified pair of currencies is unknown.");
        }

        BigDecimal result = sourceAmount.multiply(rate);
        result = result.setScale(2, RoundingMode.HALF_UP);
        return result;
    }

}
