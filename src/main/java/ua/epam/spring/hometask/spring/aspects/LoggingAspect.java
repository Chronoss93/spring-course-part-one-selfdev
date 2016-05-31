//package ua.epam.spring.hometask.spring.aspects;
//
//import lombok.extern.slf4j.Slf4j;
//import org.aspectj.lang.JoinPoint;
//import org.aspectj.lang.annotation.AfterReturning;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Before;
//import org.aspectj.lang.annotation.Pointcut;
//import org.springframework.stereotype.Component;
//
///**
// * Created by Igor on 25.05.2016.
// */
//@Aspect
//@Component
//@Slf4j
//public class LoggingAspect {
//
//    @Pointcut("execution(* *.logEvent(..))")
//    private void allLogEventMethods() {
//
//    }
//
//    @Before("allLogEventMethods()")
//    public void logBefore(JoinPoint joinPoint) {
//        log.info("allLogEventMethodsPointcut \n Before:" + joinPoint.getTarget().getClass().getSimpleName() + " " + joinPoint.getSignature());
//
//    }
//
//    @AfterReturning(pointcut = "")
//}
