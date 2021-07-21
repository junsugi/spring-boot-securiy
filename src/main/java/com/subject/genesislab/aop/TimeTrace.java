package com.subject.genesislab.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class TimeTrace {

    private static final Logger logger = LoggerFactory.getLogger(TimeTrace.class);

    @Around("execution(* com.subject.genesislab..*(..))")
    public Object Logging(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        logger.debug("START : {}", joinPoint.getSignature().getDeclaringTypeName());
        try{
            return  joinPoint.proceed();
        } finally {
            long end = System.currentTimeMillis();
            long time = end - start;
            logger.debug("END : {}, EXECUTE TIME : {}ms", joinPoint.getSignature().getDeclaringTypeName(), time);
        }
    }

}
