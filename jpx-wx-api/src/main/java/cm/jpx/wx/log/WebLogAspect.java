package cm.jpx.wx.log;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

/**
 * 打印请求api的http信息
 */
@Slf4j
@Aspect
@Component
public class WebLogAspect {

    /**
     * 定义切入点，切入点为cm.jpx.wx.web下的所有函数
     */
    @Pointcut("execution(public * cm.jpx.wx.web..*.*(..))")
    public void webLog(){}

    /**
     * 前置通知：在连接点之前执行
     * 记录request内容
     */
    @Before("webLog()")
    public void deBefore(JoinPoint joinPoint) throws Throwable {
        /**
         * JoinPoint可以获得通知的签名信息，如目标方法名、目标方法参数信息等
         * RequestContextHolder获取请求信息，session信息
         */
        //接收到请求，记录请求内容
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        //记录下请求内容
        log.info(String.format("url: %s", request.getRequestURL().toString()));
        log.info(String.format("server: %s", request.getServerName()));
        log.info(String.format("http_method: %s", request.getMethod()));
        log.info(String.format("ip: %s", request.getRemoteAddr()));
        //getDeclaringTypeName: aop代理类的名字
        //getName: 代理的方法的名字
        log.info(String.format("class_method: %s.%s", joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName()));
        //目标方法的参数信息
        log.info(String.format("args: %s", Arrays.toString(joinPoint.getArgs())));
    }

    /**
     * @param ret 代理方法的返回值
     * @throws Throwable
     */
    @AfterReturning(returning = "ret", pointcut = "webLog()")
    public void doAfterReturning(Object ret) throws Throwable {
        //处理完请求，返回内容
        log.info(String.format("response: %s", ret));
    }

}
