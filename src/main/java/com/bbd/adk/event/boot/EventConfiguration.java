//
//                       _oo0oo_
//                      o8888888o
//                      88" . "88
//                      (| -_- |)
//                      0\  =  /0
//                    ___/`---'\___
//                  .' \\|     |// '.
//                 / \\|||  :  |||// \
//                / _||||| -:- |||||- \
//               |   | \\\  -  /// |   |
//               | \_|  ''\---/''  |_/ |
//               \  .-\__  '-'  ___/-. /
//             ___'. .'  /--.--\  `. .'___
//          ."" '<  `.___\_<|>_/___.' >' "".
//         | | :  `- \`.;`\ _ /`;.`/ - ` : | |
//         \  \ `_.   \_ __\ /__ _/   .-` /  /
//     =====`-.____`.___ \_____/___.-`___.-'=====
//                       `=---='
//
//
//     ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
//
//               佛祖保佑         永无BUG
//
//
//
package com.bbd.adk.event.boot;

import com.bbd.adk.event.EventSupport;
import com.bbd.adk.event.NotifierBus;
import org.springframework.beans.BeansException;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.StringUtils;

/**
 * @author zhanglinlin@d-bigdata.com
 * @see
 */
@Configuration
@EnableConfigurationProperties({EventProperties.class})
public class EventConfiguration implements ApplicationContextAware, InitializingBean {

    private ApplicationContext applicationContext;

    @Autowired
    private EventProperties eventProperties;

    private ThreadPoolTaskExecutor monitoredThreadPool;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void afterPropertiesSet()  {
        //没有配置独立的线程池，又打开了异步处理开关
        if (eventProperties.isAsyncEnable()) {
            try {
                String beanName = eventProperties.getThreadPoolBeanName();
                monitoredThreadPool = StringUtils.isEmpty(beanName)
                        ? applicationContext.getBean(ThreadPoolTaskExecutor.class)
                        : applicationContext.getBean(beanName, ThreadPoolTaskExecutor.class);
            } catch (NoSuchBeanDefinitionException e) {
                //如果不存在线程池定义，那么就自行创建，并委托给容器管理
                DefaultListableBeanFactory factory = (DefaultListableBeanFactory) applicationContext
                        .getAutowireCapableBeanFactory();

                RootBeanDefinition db = new RootBeanDefinition(ThreadPoolTaskExecutor.class);
                db.setScope(BeanDefinition.SCOPE_SINGLETON);

                MutablePropertyValues threadPoolPropertyValues = new MutablePropertyValues();
                db.setPropertyValues(threadPoolPropertyValues);

                threadPoolPropertyValues.add("corePoolSize", eventProperties.getCorePoolSize());
                threadPoolPropertyValues.add("keepAliveSeconds",
                        eventProperties.getKeepAliveSeconds());
                threadPoolPropertyValues.add("maxPoolSize", eventProperties.getMaxPoolSize());
                threadPoolPropertyValues.add("queueCapacity", eventProperties.getQueueCapacity());

                threadPoolPropertyValues.add("threadNamePrefix", "ADK-EXEC");
                threadPoolPropertyValues.add("enableGaugeMetric", true);
                threadPoolPropertyValues.add("enableTimerMetric", true);

                factory.registerBeanDefinition("org.springframework.scheduling.concurrent.threadpool", db);
                this.monitoredThreadPool = factory.getBean("org.springframework.scheduling.concurrent.threadpool",
                        ThreadPoolTaskExecutor.class);
            }
        }
    }

    @Bean
    public NotifierBus notifierBus() {
        return new NotifierBus(monitoredThreadPool);
    }

    @Bean
    public EventSupport eventSupport() {
        return new EventSupport();
    }
}
