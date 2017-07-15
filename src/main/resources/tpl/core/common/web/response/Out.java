/**
 * Program  : Out.java
 * Author   : songkun
 *
 */

package ${rootPackage}.common.web.response;

import java.util.List;

/**
 * 输出接口
 * 
 * @author songkun
 */
public interface Out<T> {
	/**
	 * 获取数据集
	 * 
	 */
	public List<T> getRows();

	/**
	 * 获取提示信息
	 * 
	 */
	public String getMessage();

	/**
	 * 是否成功
	 * 
	 */
	public boolean getSuccess();

	/**
	 * 获取记录总数
	 * 
	 */
	public int getTotalRows();

}
