/**
 * Program  : IpFilter.java
 * Author   : songkun
 *
 */

package ${rootPackage}.common.web.filter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
 * ip过滤,不需要登陆，放在所以filter之前，作为第一个filter
 *
 * @author songkun
 */
public class IpFilter implements Filter {

	// private static final Logger LOGGER = Logger.getLogger(IpFilter.class);

	private Map<String, String[]> ipAuths = new HashMap<>();// IP限制,key为url,value为ip前缀列表（配置的ip用下划线隔开）

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		synchronized (ipAuths) {
			ipAuths.clear();
			String tmp = filterConfig.getInitParameter("ipAuths");// 格式：url1-ip11_ip12,url2-ip21_ip22
			if (tmp != null && tmp.length() > 0) {
				String[] tmps = tmp.split(",");// 不同的【url-ips】以逗号隔开
				if (tmps != null && tmps.length > 0) {
					for (String url2Ips : tmps) {// url与ips以-隔开
						String[] urlIps = url2Ips.split("-");
						if (urlIps != null && urlIps.length == 2) {
							ipAuths.put(urlIps[0], urlIps[1].split("_"));
						}
					}
				}
			}
		}
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		String requestUri = req.getServletPath();
		if (this.ipAuth(requestUri, getIpAddr(req))) {
			chain.doFilter(request, response);
		}
	}

	/**
	 * 对特定的url做ip限制判断
	 * 
	 * @author songkun
	 * @create 2016年10月12日 下午12:09:49
	 * @param requestUri
	 * @param ip
	 * @return boolean
	 */
	private boolean ipAuth(String requestUri, String ip) {
		if (ip == null || ip.length() <= 0) {
			return false;
		}
		ip = ip.trim();
		String[] ips = ipAuths.get(requestUri);
		if (ips == null || ips.length <= 0) {
			return true;
		}
		for (String tmp : ips) {
			if (ip.startsWith(tmp)) {// 如果是
				return true;
			}
		}
		return false;
	}

	/**
	 * 获取客户机的IP地址
	 * 
	 * @author songkun
	 * @param request
	 * @return String
	 */
	private String getIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
	}

}
