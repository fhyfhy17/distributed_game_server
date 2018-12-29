package com.aop;

import com.annotation.IgniteTransaction;
import com.annotation.P;
import com.config.CacheManager;
import com.entry.BaseEntry;
import com.exception.exceptionNeedSendToClient.ExceptionNeedSendToClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.transactions.Transaction;
import org.apache.ignite.transactions.TransactionConcurrency;
import org.apache.ignite.transactions.TransactionIsolation;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

@Aspect
@Component
@Slf4j
public class IgniteTransactionAspect {
    @Autowired
    private Ignite ignite;

    public final static ThreadLocal<BaseEntry[]> THREAD_LOCAL = new InheritableThreadLocal<>();

    @Pointcut("@annotation(com.annotation.IgniteTransaction)")
    public void trPoint() {
    }


    @Around("trPoint()")
    public Object around(ProceedingJoinPoint pjp) throws ExceptionNeedSendToClient {
        Object result = null;
        Signature signature = pjp.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method targetMethod = methodSignature.getMethod();

        IgniteTransaction[] annotations = targetMethod.getAnnotationsByType(IgniteTransaction.class);

        IgniteCache<Long, BaseEntry>[] caches = new IgniteCache[annotations[0].cacheEnum().length];
        //取键 要放在事务里
        BaseEntry[] baseEntries = new BaseEntry[annotations[0].cacheEnum().length];
        for (int i = 0; i < annotations[0].cacheEnum().length; i++) {
            caches[i] = CacheManager.getCache(annotations[0].cacheEnum()[i]);
            for (int k = 0; k < targetMethod.getParameters().length; k++) {
                Parameter parameter = targetMethod.getParameters()[k];
                P[] annotationsByType = parameter.getAnnotationsByType(P.class);
                if (annotationsByType != null && annotationsByType[0].p().name().equals(annotations[0].cacheEnum()[i].name())) {
                    baseEntries[i] = caches[i].get((Long) pjp.getArgs()[k]);
                }
            }


        }
        THREAD_LOCAL.set(baseEntries);
        //悲观锁，可重复读--意思就是，读写都锁
        try (Transaction tx = ignite.transactions().txStart(
                TransactionConcurrency.PESSIMISTIC, TransactionIsolation.REPEATABLE_READ)) {

            //执行方法
            result = pjp.proceed();
            for (int j = 0; j < caches.length; j++) {
                caches[j].put(baseEntries[j].getId(), baseEntries[j]);
            }
            tx.commit();
        } catch (ExceptionNeedSendToClient e) {
            throw e;
        } catch (Throwable throwable) {
            log.error("", throwable);
        } finally {
            THREAD_LOCAL.remove();
        }
        return result;
    }
}
