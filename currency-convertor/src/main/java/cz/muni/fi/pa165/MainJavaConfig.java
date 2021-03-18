package cz.muni.fi.pa165;

import cz.muni.fi.pa165.currency.CurrencyConvertor;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.math.BigDecimal;
import java.util.Currency;

public class MainJavaConfig {

    public static AnnotationConfigApplicationContext context =
            new AnnotationConfigApplicationContext(AppConfig.class);

    public static void main(String[] args) {
        CurrencyConvertor convertor = context.getBean(CurrencyConvertor.class);
        BigDecimal result = convertor.convert(Currency.getInstance("EUR"),
                Currency.getInstance("CZK"),
                new BigDecimal("10"));
        System.out.println(result);
    }
}
