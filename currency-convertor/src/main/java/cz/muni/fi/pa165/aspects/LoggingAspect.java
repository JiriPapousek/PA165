package cz.muni.fi.pa165.aspects;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.util.StopWatch;

import javax.inject.Named;
import java.util.Arrays;


@Aspect
@Named
public class LoggingAspect {

    @Around("execution(* *(..))")
    public Object logDuration(ProceedingJoinPoint pjp) throws Throwable {
        StopWatch stopWatch = new StopWatch();
        System.err.println("Calling method " + pjp.getSignature().getName() + " of " + pjp.getTarget().getClass()
                + " with parameters " + Arrays.asList(pjp.getArgs()));
        stopWatch.start();
        Object result = pjp.proceed();
        stopWatch.stop();
        System.err.println("Method call finished, took " + stopWatch.getTotalTimeMillis() + " ms.");
        return result;
    }
}
