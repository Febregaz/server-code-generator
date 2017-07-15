/**
 * Program  : InitListener.java
 * Author   : songkun
 *
 */

package ${rootPackage}.common.web.listener;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

//import ${rootPackage}.Context;

/**
 * 初始化监听器，在spring容器启动后初始化一些参数信息，同时，可以获取spring容易的上下文
 *
 * @author songkun
 */
@Component
public class InitListener implements ApplicationListener<ContextRefreshedEvent> {

	private static boolean inited = false;// 是否已经初始化完毕
	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		// TODO Auto-generated method stub
		if (inited) {
			return;
		}
		synchronized (InitListener.class) {
			if (inited) {
				return;
			}
			// Context.init(event.getApplicationContext(), false);// 初始化web应用上下文
			inited = true;
		}
	}

}
