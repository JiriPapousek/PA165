package cz.muni.fi.pa165.aspects;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StopWatch;

import javax.inject.Named;
import java.util.Arrays;


@Aspect
@Named
public class LoggingAspect {

    @Around("execution(public * *(..))")
    public Object logDuration(ProceedingJoinPoint pjp) throws Throwable {
        Logger logger = LoggerFactory.getLogger(pjp.getSignature().getDeclaringType());

        StopWatch stopWatch = new StopWatch();
        logger.info("Calling method {} with parameters {}.",
                pjp.getSignature().getName(),
                Arrays.asList(pjp.getArgs()));

        stopWatch.start();
        Object result = pjp.proceed();
        stopWatch.stop();

        logger.info("Method call finished, took {} ms.", stopWatch.getTotalTimeMillis());
        return result;
    }
}
