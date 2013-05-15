package org.easy.util.logging;

import java.util.Arrays;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TODO
 * Check usage of http://www.jcabi.com/jcabi-aspects
 * http://www.adam-bien.com/roller/abien/entry/server_independent_thread_tracking_utility
 */
public class LoggingInterceptor {
    @AroundInvoke
    public Object logCall(InvocationContext context) throws Exception{
        Logger logger = LoggerFactory.getLogger(context.getTarget().getClass());
        
        long time = System.currentTimeMillis();
        Object result = null;
        Exception error = null;
        try {
            result = context.proceed();
        } catch (Exception e) {
            error = e;
            throw e;
        } finally {
            if (logger.isDebugEnabled()) {
                time = System.currentTimeMillis() - time;
                logger.debug("#{}({}) --> {} in {}ms", 
                    context.getMethod().getName(), 
                    parseValue(context.getParameters()),
                    parseValue(result),
                    time);
            }
            if (error != null) {
                logger.error("{} failed {}", context.getMethod().getName(), error);
                error.printStackTrace();
            }
        }
        return result;
    }
    
    private String parseValue(Object in) {
        String result = "";
        if (in != null) {
            result = (in.getClass().isArray() ? Arrays.toString((Object[])in) : in.toString());
            if (result.length() > 60) {
                result = result.substring(0, 57) + "...";
            }
        }
        return result;
    }
}
