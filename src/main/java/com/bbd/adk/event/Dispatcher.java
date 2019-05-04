package com.bbd.adk.event;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.concurrent.Executor;

/**
 * 通知事件总线实现
 * @author zhanglinlin@d-bigdata.com
 * @version 1.0.0
 * @see
 *
 */
public class Dispatcher {
	
	private static final Logger logger = LoggerFactory.getLogger(Dispatcher.class);
	
	private Executor executor;
	
	public Dispatcher(Executor executor) {
		
		this.executor = executor;
	}
	
	public void execute(DispatcherTask task) {
		
		if (task.isAsync()) {
			executor.execute(task);
		} else {
			task.run();
		}
		
	}
	
	public static class DispatcherTask implements Runnable {
		
		private SubscriberWrapper wrapper;
		
		private Object[] events;
		
		public DispatcherTask(SubscriberWrapper wrapper, Object[] events) {
			
			this.wrapper = wrapper;
			this.events = events;
		}
		
		public boolean isAsync() {
			
			return this.wrapper.isAsync();
		}
		
		public void run() {
			if (isAsync()) {
				try {
					wrapper.invocation(events);
				} catch (Exception e) {
					logger.error("事件{}处理时发生异常", Arrays.toString(events), e);
				}
			} else {
				wrapper.invocation(events);
			}
			
		}
	}
}
