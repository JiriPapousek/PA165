package cz.muni.fi.pa165;

import cz.muni.fi.pa165.currency.CurrencyConvertor;
import cz.muni.fi.pa165.currency.CurrencyConvertorImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.math.BigDecimal;
import java.util.Currency;

public class MainAnnotations {

    private static AnnotationConfigApplicationContext context
            = new AnnotationConfigApplicationContext("cz.muni.fi.pa165.currency");

    public static void main(String[] args) {
        CurrencyConvertor convertor = context.getBean(CurrencyConvertorImpl.class);
        BigDecimal result = convertor.convert(Currency.getInstance("EUR"),
                Currency.getInstance("CZK"),
                new BigDecimal("10"));
        System.out.println(result);
    }
}
