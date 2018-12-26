package com.aop;

import lombok.extern.slf4j.Slf4j;
import org.apache.ignite.Ignite;
import org.apache.ignite.transactions.Transaction;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class IgniteTransactionAspect {
    @Autowired
    private Ignite ignite;

    @Pointcut("@annotation(com.annotation.IgniteTransaction)")
    public void trPoint() {
    }


    @Around("trPoint()")
    public void aftReturn(ProceedingJoinPoint pjp) {

        try (Transaction tx = ignite.transactions().txStart()) {


            pjp.proceed();
            tx.commit();
        } catch (Throwable throwable) {
            log.error("", throwable);
        }
    }
}
