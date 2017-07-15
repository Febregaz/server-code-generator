/**
 * Program  : BaseController.java
 * Author   : songkun
 *
 */

package ${rootPackage}.common.web.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


/**
 * 控制层基类
 * 
 * @author songkun
 */
public abstract class BaseController<T> {

	/**
	 * 获取当前登录的用户信息
	 * 
	 * @return User
	 */
//	protected User getCurrentUser() {
//		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
//		if (request.getSession() == null) {
//			return null;
//		}
//		return (User) request.getSession().getAttribute(Context.USER_INFO);
//	}

	/**
	 * 获取服务器上dir的绝对路径
	 * 
	 * @param fileName
	 * @return String
	 */
	protected String getRealPath(String dir) {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		return request.getSession().getServletContext().getRealPath(dir);
	}

	/**
	 * 获取basePath
	 * 
	 * @return String
	 */
	protected String getBasePath() {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
		if (basePath.endsWith("/")) {
			return basePath.substring(0, basePath.length() - 1);
		}
		return basePath;
	}

	/**
	 * 获取Logger
	 * 
	 * @return Logger
	 */
	protected final Logger getLogger() {
		return LogManager.getLogger(this.getClass());
	}
}
