package cz.muni.fi.pa165;


import cz.muni.fi.pa165.currency.CurrencyConvertor;
import cz.muni.fi.pa165.currency.CurrencyConvertorImpl;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.math.BigDecimal;
import java.util.Currency;

public class MainXml {

    private static ClassPathXmlApplicationContext context =
            new ClassPathXmlApplicationContext("applicationContext.xml");

    public static void main(String[] args) {
        CurrencyConvertor convertor = context.getBean(CurrencyConvertorImpl.class);
        BigDecimal result = convertor.convert(Currency.getInstance("EUR"),
                Currency.getInstance("CZK"),
                new BigDecimal("10"));
        System.out.println(result);
    }
}
