/*
 * www.yiji.com Inc.
 * Copyright (c) 2011 All Rights Reserved.
 *                    _ooOoo_
 *                   o8888888o
 *                   88" . "88
 *                   (| -_- |)
 *                   O\  =  /O
 *                ____/`---'\____
 *              .'  \\|     |//  `.
 *             /  \\|||  :  |||//  \
 *            /  _||||| -:- |||||-  \
 *            |   | \\\  -  /// |   |
 *            | \_|  ''\---/''  |   |
 *            \  .-\__  `-`  ___/-. /
 *          ___`. .'  /--.--\  `. . __
 *       ."" '<  `.___\_<|>_/___.'  >'"".
 *      | | :  `- \`.;`\ _ /`;.`/ - ` : | |
 *      \  \ `-.   \_ __\ /__ _/   .-` /  /
 *  ======`-.____`-.___\_____/___.-`____.-'======
 *                     `=---='
 *  ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
 *           佛祖保佑       永无BUG
 */

package com.bbd.adk.event.boot;
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
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
/**
 * 事件总线配置选项
 * @author zhanglinlin@d-bigdata.com
 * @see
 */
@ConfigurationProperties(prefix = "bbd.adk.event")
public class EventProperties implements BeanClassLoaderAware, InitializingBean {
	
	/** 是否启用此组件(默认:false) */
	private boolean enable = false;

    /** 外部线程池Spring Bean名称 */
    private String threadPoolBeanName ;

    /**
     * 异步事件开关，线程池是否生效以此开关为准。
     * 即便外部已经配置了线程池在关闭的情况下仍然会忽略(默认：true)
     * 如需要使用自定义线程池，只需存在相应的spring定义即可。
     */
    private boolean asyncEnable = true;

    /** 核心线程数(默认:2) */
    private int corePoolSize = 2;

    /** 最大线程数(默认:10) */
    private int maxPoolSize = 10;

    /** 限制线程保持时间(默认:300秒) */
    private int keepAliveSeconds = 300;

    /** 线程队列容量(默认:50) */
    private int queueCapacity = 50;

    private ClassLoader beanClassLoader;
	
	@Override
	public void afterPropertiesSet() throws Exception {
	}
	
	@Override
	public void setBeanClassLoader(ClassLoader beanClassLoader) {
		this.beanClassLoader = beanClassLoader;
	}
	
	public ClassLoader getBeanClassLoader() {
		return beanClassLoader;
	}
	
	public boolean isEnable() {
		return enable;
	}
	
	public void setEnable(boolean enable) {
		this.enable = enable;
	}

    public int getCorePoolSize() {
        return corePoolSize;
    }

    public void setCorePoolSize(int corePoolSize) {
        this.corePoolSize = corePoolSize;
    }

    public int getMaxPoolSize() {
        return maxPoolSize;
    }

    public void setMaxPoolSize(int maxPoolSize) {
        this.maxPoolSize = maxPoolSize;
    }

    public int getKeepAliveSeconds() {
        return keepAliveSeconds;
    }

    public void setKeepAliveSeconds(int keepAliveSeconds) {
        this.keepAliveSeconds = keepAliveSeconds;
    }

    public int getQueueCapacity() {
        return queueCapacity;
    }

    public void setQueueCapacity(int queueCapacity) {
        this.queueCapacity = queueCapacity;
    }

    public boolean isAsyncEnable() {
        return asyncEnable;
    }

    public void setAsyncEnable(boolean asyncEnable) {
        this.asyncEnable = asyncEnable;
    }

    public String getThreadPoolBeanName() {
        return threadPoolBeanName;
    }

    public void setThreadPoolBeanName(String threadPoolBeanName) {
        this.threadPoolBeanName = threadPoolBeanName;
    }
}