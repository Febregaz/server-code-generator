/**
 * Program  : SessionTimeoutCheckFilter.java
 * Author   : songkun
 *
 */
package ${rootPackage}.common.web.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Session过期检查.
 * 
 */
public class SessionTimeoutCheckFilter implements Filter {

	//private static final Logger LOGGER = Logger.getLogger(SessionTimeoutCheckFilter.class);

	private FilterConfig filterConfig;
	private String[] vips;

	public FilterConfig getFilterConfig() {
		return filterConfig;
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		this.filterConfig = filterConfig;
		String tmp = filterConfig.getInitParameter("vips");
		if (tmp != null && tmp.length() > 0) {
			vips = tmp.split(",");
		}
	}

	public void setFilterConfig(FilterConfig filterconfig) {
		this.filterConfig = filterconfig;
	}

	public void destroy() {
		this.filterConfig = null;
	}

	/**
	 * 判断url是否需要做超时验证
	 * 
	 * @param url
	 */
	private boolean isVIP(String url) {
		if (vips == null || vips.length <= 0 || url == null || url.length() <= 0) {
			return false;
		}
		for (int i = 0; i < vips.length; i++) {
			if (url.indexOf(vips[i]) > -1) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 超时重定向
	 * 
	 * @param request
	 * @param response
	 */
	private boolean redirect(HttpServletRequest request, HttpServletResponse response) {
		try {
			String contextPath = request.getContextPath();
			contextPath = (contextPath == null || contextPath.trim().length() <= 0 || "/".equals(contextPath)) ? "" : contextPath;
			if (request.getHeader("x-requested-with") != null && request.getHeader("x-requested-with").equalsIgnoreCase("XMLHttpRequest")) {
				response.setContentType("application/json;charset=utf-8");
				response.setStatus(408);
				response.getWriter().write(contextPath + "/");
				return true;
			} else {
				response.sendRedirect(contextPath + "/");
				return true;
			}
		} catch (IOException e) {
			//LOGGER.error(e);
		}
		return false;
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
		try {
			HttpServletRequest request = (HttpServletRequest) req;
			String requestUri = request.getServletPath();
			HttpServletResponse response = (HttpServletResponse) res;
			HttpSession session = request.getSession();
			if (!this.isVIP(requestUri)) {
				if (session == null /*|| session.getAttribute(Context.USER_INFO) == null*/) {
					redirect(request, response);// 跳转
					return;// 如果出错了，直接返回
				}
			}
			String contextPath = request.getContextPath();
			contextPath = (contextPath == null || contextPath.trim().length() <= 0 || "/".equals(contextPath)) ? "" : contextPath;
//			if (requestUri.equals(contextPath) || requestUri.equals("/index.html") || requestUri.equals(contextPath + "/index.html")) {// 如果是登陆界面，判断是否登陆了，如果登陆了，则直接进入
//				if (session != null /*&& session.getAttribute(Context.USER_INFO) != null*/) {
//					User user = (User) session.getAttribute(Context.USER_INFO);
//					if (user.getType() == Context.USER_TYPE_OPERATER) {// 跳转
//						List<String> auths = AuthCache.getPermissions(user.getId());
//						if (auths != null && auths.size() > 0) {
//							if (auths.contains(Context.MAIN_PAGE_OPERATER)) {
//								((HttpServletResponse) response).sendRedirect(contextPath + Context.MAIN_PAGE_OPERATER);
//							} else {
//								((HttpServletResponse) response).sendRedirect(contextPath + "/" + auths.get(0));
//							}
//						} else {
//							((HttpServletResponse) response).sendRedirect(contextPath + "/");
//						}
//					} else {
//						response.sendRedirect(contextPath + Context.MAIN_PAGE_CUSTOMER);
//					}
//					return;// 如果出错了，直接返回
//				}
//			}
			chain.doFilter(req, res);
		} catch (Exception ex) {
			//LOGGER.error(ex);
		} finally {
		}
	}
}
