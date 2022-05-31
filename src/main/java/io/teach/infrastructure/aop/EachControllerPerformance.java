package io.teach.infrastructure.aop;

import io.teach.infrastructure.http.body.StandardRequest;
import io.teach.infrastructure.http.body.TrackingDto;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Aspect
@Component
public class EachControllerPerformance {

    @Around("execution(* io.teach.business.*.controller.*Controller.* (..)) and args(standardRequest)")
    public Object controllerStopWatch(final ProceedingJoinPoint joinPoint, final StandardRequest standardRequest) throws Throwable {
        final TrackingDto tracking = standardRequest.tracking();
        final Object target = joinPoint.getTarget();
        String className = target.getClass().getSimpleName();
        String methodName = joinPoint.getSignature().getName();
        String watchName = String.format("[%s][%s] ", className, methodName);
        StopWatch stopWatch = new StopWatch(watchName);
        stopWatch.start();

        final Object proceed = joinPoint.proceed();

        stopWatch.stop();
        System.out.printf("\n%s ----- %.4fs\n\n", stopWatch.getId(), stopWatch.getTotalTimeSeconds());

        return proceed;
    }
}
