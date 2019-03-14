package io.github.tintinrevient.gis.config;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.concurrent.ConcurrentTaskExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.lang.reflect.Method;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Configuration
public class AsyncConfig implements AsyncConfigurer {

	private static final Logger LOGGER = LoggerFactory.getLogger(AsyncConfig.class);


	@Override
  public Executor getAsyncExecutor() {
    return new ConcurrentTaskExecutor(
      Executors.newFixedThreadPool(2));
  }

  @Override
  public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
    return new CustomAsyncExceptionHandler();
  }

  static class CustomAsyncExceptionHandler implements AsyncUncaughtExceptionHandler {

    @Override
    public void handleUncaughtException(
      Throwable throwable, Method method, Object... obj) {

					 LOGGER.warn("Exception message - " + throwable.getMessage());
					 LOGGER.warn("Method name - " + method.getName());
      for (Object param : obj) {
							 LOGGER.warn("Parameter value - " + param);
      }
    }
  }
}
