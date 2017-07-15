/**
 * Program  : MessageOut.java
 * Author   : songkun
 *
 */

package ${rootPackage}.common.web.response;

import java.util.List;

/**
 * 文本消息输出类
 * 
 * @author songkun
 */
public class MessageOut<T> implements Out<T> {

	/**
	 * 是否成功
	 */
	private boolean success;
	/**
	 * 输出信息
	 */
	private String message;

	public MessageOut() {
	}

	public MessageOut(boolean success, String message) {
		this.success = success;
		this.message = message;
	}

	public boolean getSuccess() {
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

	// 登录成功/失败 提示信息
	public static final MessageOut<Object> LOGIN_OK_MESSAGE = new MessageOut<Object>(true, "登录成功");
	public static final MessageOut<Object> LOGIN_FAIL_MESSAGE = new MessageOut<Object>(false, "登录失败,账号 或 密码 有误");
	// 登录成功/失败 提示信息
	// public static final String LOGIN_OK = "登录成功";
	// public static final String LOGIN_FAIL = "登录失败,账号 或 密码 有误";

	// 退出成功/失败 提示信息
	public static final MessageOut<Object> LOGOUT_OK_MESSAGE = new MessageOut<Object>(true, "退出成功");
	public static final MessageOut<Object> LOGOUT_FAIL_MESSAGE = new MessageOut<Object>(false, "退出失败,请刷新再试");
	// 退出成功/失败 提示信息
	// public static final String LOGOUT_OK = "退出成功";
	// public static final String LOGOUT_FAIL = "退出失败,请刷新再试";

	// 名称已存在 提示信息
	// public static final String NAME_EXIST = "此名称已存在,请确认";
	// public static final String RECORD_UN_EXIST = "此记录已删除,请刷新再试";
	public static final MessageOut<Object> RECORD_EXIST_MESSAGE = new MessageOut<Object>(false, "此记录已存在,请确认");
	public static final MessageOut<Object> NAME_EXIST_MESSAGE = new MessageOut<Object>(false, "此名称已存在,请确认");
	public static final MessageOut<Object> NAME_OR_EMAIL_EXIST_MESSAGE = new MessageOut<Object>(false, "账号或邮箱已存在,请确认");
	// 记录已经删除 提示信息
	// public static final String RECORD_UN_EXIST = "此记录已删除,请刷新再试";
	public static final MessageOut<Object> RECORD_UN_EXIST_MESSAGE = new MessageOut<Object>(false, "此记录已删除,请刷新再试");
	// 无权修改他人信息
	public static final MessageOut<Object> NO_PERMIT_UPDATE_MESSAGE = new MessageOut<Object>(false, "无权修改他人信息");

	// 添加成功/失败 提示信息
	public static final MessageOut<Object> ADD_OK_MESSAGE = new MessageOut<Object>(true, "新增成功");
	public static final MessageOut<Object> ADD_FAIL_MESSAGE = new MessageOut<Object>(false, "新增失败,请刷新再试");
	// public static final String ADD_OK = "新增成功";
	// public static final String ADD_FAIL = "新增失败,请刷新再试";

	// 修改成功/失败 提示信息
	public static final MessageOut<Object> UPDATE_OK_MESSAGE = new MessageOut<Object>(true, "修改成功");
	public static final MessageOut<Object> UPDATE_FAIL_MESSAGE = new MessageOut<Object>(false, "修改失败,请刷新再试");
	// 提交审核成功/失败 提示信息
	public static final MessageOut<Object> COMMIT_OK_MESSAGE = new MessageOut<Object>(true, "提交审核成功");
	public static final MessageOut<Object> COMMIT_FAIL_MESSAGE = new MessageOut<Object>(false, "提交审核失败,请刷新再试");
	// 修改成功/失败 提示信息
	// public static final String UPDATE_OK = "修改成功";
	// public static final String UPDATE_FAIL = "修改失败,请刷新再试";

	// 删除成功/失败
	public static final MessageOut<Object> DELETE_OK_MESSAGE = new MessageOut<Object>(true, "删除成功");
	public static final MessageOut<Object> DELETE_FAIL_MESSAGE = new MessageOut<Object>(false, "删除失败,请刷新再试");
	// 启用成功/失败
	public static final MessageOut<Object> ENABLE_OK_MESSAGE = new MessageOut<Object>(true, "启用成功");
	public static final MessageOut<Object> ENABLE_FAIL_MESSAGE = new MessageOut<Object>(false, "启用失败,请刷新再试");
	// 禁用成功/失败
	public static final MessageOut<Object> DISBLE_OK_MESSAGE = new MessageOut<Object>(true, "禁用成功");
	public static final MessageOut<Object> DISABLE_FAIL_MESSAGE = new MessageOut<Object>(false, "禁用失败,请刷新再试");
	// 被使用
	public static final MessageOut<Object> IS_USED_CANNOT_DELETE_MESSAGE = new MessageOut<Object>(true, "此记录被使用，无法删除");

	// 删除成功/失败
	// public static final String DELETE_OK = "删除成功";
	// public static final String DELETE_FAIL = "删除失败,请刷新再试";

	@Override
	public List<T> getRows() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getTotalRows() {
		// TODO Auto-generated method stub
		return 0;
	}

}
