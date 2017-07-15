/**
 * Program  : LoginController.java
 * Author   : songkun
 *
 */
package ${rootPackage}.app.user.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import ${rootPackage}.common.web.controller.BaseController;
import ${rootPackage}.common.web.response.MessageOut;
import ${rootPackage}.common.web.response.Out;
import ${rootPackage}.app.user.model.Login;


/**
 * 登录控制器
 * 
 */
@Controller("loginController")
@RequestMapping(value = "/login", produces = "application/json;charset=UTF-8")
public class LoginController extends BaseController<Login> {

	/**
	 * 登录
	 * 
	 * @return Login
	 */
	@RequestMapping("/login.do")
	@ResponseBody
	public Login login(Login login) {// edit it
		// User dbUser = ....;// validate the user
		//if(dbUser == null){
		//	login.setPassword(null);
		//	login.setSuccess(false);
		//	login.setMessage("....");
		//	return login;
		//}
		//((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession(true)
		//.setAttribute(Context.USER_INFO, dbUser);
		Login response = new Login();
		response.setSuccess(true);
		response.setIndexPage("${indexPage}");
		//response.setIndexPage(Context.INDEX_PAGE);
		return response;
	}

	/**
	 * 退出
	 * 
	 * @return Out<Object>
	 */
	@RequestMapping("/logout.do")
	@ResponseBody
	public Out<Object> logout() {
		try {
			//((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession().removeAttribute(Context.USER_INFO);
		} catch (Exception e) {
			//this.getLogger().error(e);
		}
		return MessageOut.LOGOUT_OK_MESSAGE;
	}
}
