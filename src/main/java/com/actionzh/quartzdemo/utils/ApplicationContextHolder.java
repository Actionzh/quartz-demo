package com.actionzh.quartzdemo.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class ApplicationContextHolder implements ApplicationContextAware {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationContextHolder.class);

    private static ApplicationContext context;

    private static long contextMaxWaitTime = 60000L;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        synchronized (ApplicationContextHolder.class) {
            ApplicationContextHolder.context = applicationContext;
            ApplicationContextHolder.class.notifyAll();
        }
    }

    public static ApplicationContext getApplicationContext() {
        try {
            waitForContextReady();
        } catch (InterruptedException ex) {
            LOGGER.error("Interrupted!", ex);
            Thread.currentThread().interrupt();
        }
        return context;
    }

    private static void waitForContextReady() throws InterruptedException {
        long maxWait = contextMaxWaitTime;
        long deadline = System.currentTimeMillis() + maxWait;
        synchronized (ApplicationContextHolder.class) {
            long time = System.currentTimeMillis();
            while (time < deadline && context == null) {
                ApplicationContextHolder.class.wait(deadline - time);
                time = System.currentTimeMillis();
            }

            if (context == null) {
                throw new IllegalStateException("No spring context available after " + maxWait + " millis");
            }
        }
    }

    public static long getContextMaxWaitTime() {
        return contextMaxWaitTime;
    }

    public static void setContextMaxWaitTime(long contextMaxWaitTime) {
        ApplicationContextHolder.contextMaxWaitTime = contextMaxWaitTime;
    }

}
