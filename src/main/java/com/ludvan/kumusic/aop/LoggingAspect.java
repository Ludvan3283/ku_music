package com.ludvan.kumusic.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Aspect
@Component
public class LoggingAspect {
    private static final Logger log = LoggerFactory.getLogger(LoggingAspect.class);

    // 定义切入点 - 拦截UserController下的所有public方法
    @Pointcut("execution(public * com.ludvan.kumusic.controller.UserController.*(..))")
    public void userControllerMethods() {}

    // 环绕通知 - 记录方法执行时间、参数和返回值
    @Around("userControllerMethods()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        Object[] args = joinPoint.getArgs();

        // 记录方法开始
        log.info("===> {}.{}() 开始执行, 参数: {}", className, methodName, args);

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        try {
            // 执行目标方法
            Object result = joinPoint.proceed();

            stopWatch.stop();

            // 记录方法结束
            log.info("<=== {}.{}() 执行完成, 耗时: {}ms, 返回值: {}",
                    className, methodName, stopWatch.getTotalTimeMillis(), result);

            return result;
        } catch (Exception e) {
            stopWatch.stop();

            // 记录异常
            log.error("<!!! {}.{}() 执行异常, 耗时: {}ms, 异常: {}",
                    className, methodName, stopWatch.getTotalTimeMillis(), e.getMessage(), e);

            throw e;
        }
    }

    // 前置通知 - 专门记录敏感操作(如登录)
    @Before("execution(* com.ludvan.kumusic.controller.UserController.login(..)) && args(phone,..)")
    public void beforeLogin(String phone) {
        log.info("用户登录尝试, 手机号: {}", phone);
    }

    // 后置通知 - 专门记录验证码发送
    @AfterReturning(
            pointcut = "execution(* com.ludvan.kumusic.controller.UserController.sendCode(..)) && args(phone)",
            returning = "result"
    )
    public void afterSendCode(String phone, String result) {
        log.info("验证码发送结果 - 手机号: {}, 结果: {}", phone, result);
    }
}