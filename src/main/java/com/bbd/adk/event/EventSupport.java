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
package com.bbd.adk.event;

/**
 * 事件支持注解
 *
 * @author zhanglinlin@d-bigdata.com
 * @version 1.0.0
 * @see
 */

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.StringUtils;

import java.util.Map;

public class EventSupport implements ApplicationContextAware, InitializingBean {

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void afterPropertiesSet() throws Exception {

        Map<String, Object> listeners = applicationContext.getBeansWithAnnotation(Listener.class);

        if (listeners != null && listeners.size() > 0) {

            listeners.entrySet().stream().forEach(entry -> {

                Listener listener = entry.getValue().getClass().getAnnotation(Listener.class);

                Notifier notifier = applicationContext.getBean(
                        StringUtils.isEmpty(listener.notifier()) ? "notifierBus" : listener.notifier(),
                        Notifier.class);

                notifier.register(entry.getValue());

            });
        }

    }
}
