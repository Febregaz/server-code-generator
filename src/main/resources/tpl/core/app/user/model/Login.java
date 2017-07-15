/**
 * Program  : Login.java
 * Author   : songkun
 *
 */
package ${rootPackage}.app.user.model;

import java.io.Serializable;

/**
 * 登陆请求、响应实体
 */
public class Login implements Serializable {
	private static final long serialVersionUID = ${serialVersionUID}L;
	private String name;// 名称
	private String password;// 密码
	private boolean success = false;// 是否成功
	private String message;// 反馈信息
	private String indexPage;// 登陆成功的页面
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getIndexPage() {
		return indexPage;
	}
	public void setIndexPage(String indexPage) {
		this.indexPage = indexPage;
	}
}