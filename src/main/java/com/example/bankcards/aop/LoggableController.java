package com.example.bankcards.aop;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.util.ContentCachingRequestWrapper;

import java.io.IOException;
import java.util.stream.Collectors;

@Aspect
@Component
@Slf4j
public class LoggableController {

    @Around("@within(com.example.bankcards.aop.LogController)")
    public Object logging(ProceedingJoinPoint joinPoint) throws Throwable {
        ContentCachingRequestWrapper wrapper = getCurrentRequest();
        String requestBody = getRequestBody(wrapper);
        log.info("Request url: - {}, param: - {}, body: - {}", wrapper.getRequestURL(), parameterRequest(wrapper), requestBody);
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String methodName = signature.getMethod().getDeclaringClass().getSimpleName() + " - " + signature.getName();
        StopWatch time = new StopWatch();
        time.start();
        Object result = joinPoint.proceed();
        time.stop();
        String stopWatchOutput = String.format("method %s executed in %f seconds", methodName,
                time.getTotalTimeSeconds());
        log.info("Execution metrics controller: {}", stopWatchOutput);
        log.info("After controller: - {}", result == null ? "void" : result);
        return result;
    }

    private ContentCachingRequestWrapper getCurrentRequest() {
        HttpServletRequest request =
                ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                        .getRequest();
        return new ContentCachingRequestWrapper(request);
    }

    private String getRequestBody(ContentCachingRequestWrapper wrapper) throws IOException {
        String requestBody = new String(wrapper.getContentAsByteArray(), wrapper.getCharacterEncoding());
        return requestBody.isEmpty() ? "empty request body" : requestBody;
    }

    private String parameterRequest(ContentCachingRequestWrapper wrapper) {
        return wrapper.getParameterMap().isEmpty() ? "empty param" : wrapper.getParameterMap().entrySet().stream()
                .map(p -> String.format("%s=%s", p.getKey(), String.join(",", p.getValue())))
                .collect(Collectors.joining("&"));

    }
}
