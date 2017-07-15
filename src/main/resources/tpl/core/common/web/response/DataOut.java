/**
 * Program  : DataOut.java
 * Author   : songkun
 *
 */

package ${rootPackage}.common.web.response;

import java.util.List;

import ${rootPackage}.common.bean.Pagination;

/**
 * 数据输出类
 * 
 * @author songkun
 */
public class DataOut<T> implements Out<T> {

	private Pagination pagination;
	private List<T> rows;

	public DataOut(List<T> rows, Pagination pagination) {
		this.rows = rows;
		this.pagination = pagination;
	}

	@Override
	public List<T> getRows() {
		return rows;
	}

	@Override
	public String getMessage() {
		return null;
	}

	@Override
	public boolean getSuccess() {
		return true;
	}

	@Override
	public int getTotalRows() {
		if (this.pagination == null) {
			return 0;
		}
		return this.pagination.getTotalRows();
	}

	// extends
	public void setRows(List<T> rows) {
		this.rows = rows;
	}

}
