package io.coupling.systems.simulation;

import static net.ttddyy.dsproxy.listener.logging.SLF4JLogLevel.TRACE;
import static org.springframework.util.ReflectionUtils.findMethod;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import javax.sql.DataSource;
import net.ttddyy.dsproxy.support.ProxyDataSourceBuilder;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.beans.factory.config.BeanPostProcessor;

public class DataSourceProxy implements BeanPostProcessor {

  @Override
  public Object postProcessAfterInitialization(final Object bean, final String beanName) {
    if (bean instanceof DataSource) {
      final ProxyFactory factory = new ProxyFactory(bean);
      factory.setProxyTargetClass(true);
      final DataSource dataSource = (DataSource) bean;
      final DataSource proxyDataSource = proxyDataSource(dataSource, beanName);
      factory.addAdvice(new ProxyDataSourceInterceptor(proxyDataSource));
      return factory.getProxy();
    }
    return bean;
  }

  @Override
  public Object postProcessBeforeInitialization(final Object bean, final String beanName) {
    return bean;
  }

  private DataSource proxyDataSource(final DataSource dataSource, final String beanName) {
    return ProxyDataSourceBuilder.create(dataSource)
        .name("proxy-" + beanName)
        .asJson()
        .logQueryBySlf4j(TRACE)
        .build();
  }

  private static class ProxyDataSourceInterceptor implements MethodInterceptor {

    private final DataSource proxyDataSource;

    ProxyDataSourceInterceptor(final DataSource proxyDataSource) {
      this.proxyDataSource = proxyDataSource;
    }

    @Override
    public Object invoke(final MethodInvocation invocation)
        throws InvocationTargetException, IllegalAccessException {
      final Class<? extends DataSource> proxyDataSourceClass = proxyDataSource.getClass();
      final String invocationMethodName = invocation.getMethod().getName();
      final Method proxyMethod = findMethod(proxyDataSourceClass, invocationMethodName);
      return proxyMethod.invoke(proxyDataSource, invocation.getArguments());
    }
  }
}
