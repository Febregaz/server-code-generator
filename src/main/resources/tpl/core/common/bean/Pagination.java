/**
 * Program  : Pagination.java
 * Author   : songkun
 *
 */
package ${rootPackage}.common.bean;

/**
 * 分页对象，维护显示页面分页信息
 * 
 * @version 1.0.0
 */
public class Pagination {

	// 每页大小缺省值
	public static final int PAGE_SIZE_DEFAULT = 10;
	private int pageSize = PAGE_SIZE_DEFAULT;

	private int pageNumber = 1;
	private int totalRows = 0;
	private OrderBy[] orderBies;// 排序列表

	// 适配页面js
	// page_num : s.pageNum,
	// rows_per_page : s.rowsPerPage,

	public Pagination() {
	}

	public OrderBy[] getOrderBies() {
		return orderBies;
	}

	public void setOrderBies(OrderBy[] orderBies) {
		this.orderBies = orderBies;
	}

	/**
	 * 取每页大小
	 * 
	 * @since
	 * @return
	 */
	public int getPageSize() {
		return pageSize;
	}

	/**
	 * 设置每页大小
	 * 
	 * @since
	 * @param pageSize
	 */
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	/**
	 * 设置每页大小
	 * 
	 * @since
	 * @param pageSize
	 */
	public void setPageSize(String pageSize) {
		if (pageSize != null && pageSize.length() > 0) {
			this.setPageSize(Integer.parseInt(pageSize));
		} else {
			this.setPageSize(PAGE_SIZE_DEFAULT);
		}
	}

	/**
	 * 设置记录的总条数
	 * 
	 * @since
	 * @param totalRows
	 */
	public void setTotalRows(int totalRows) {
		this.totalRows = totalRows;
	}

	/**
	 * 取记录总条数
	 * 
	 * @since
	 * @return
	 */
	public int getTotalRows() {
		return totalRows;
	}

	/**
	 * 获取起始页数
	 * 
	 * @return int
	 */
	public int getPageNumber() {
		return pageNumber;
	}

	/**
	 * 设置起始页数
	 * 
	 * @return void
	 * @param start
	 */
	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}

	/**
	 * 获取起始位置
	 * 
	 * @return
	 */
	public int getStart() {
		if (this.pageNumber < 1 || this.pageSize < 1) {
			return 0;
		}
		return this.pageSize * (this.pageNumber - 1);
	}

	/**
	 * 适配页面js的分页参数
	 * 
	 * @param pageNumber
	 */
	public void setPage_num(int pageNumber) {
		this.pageNumber = pageNumber;
	}

	/**
	 * 适配页面js的分页参数
	 * 
	 * @param pageSize
	 */
	public void setRows_per_page(int pageSize) {
		this.pageSize = pageSize;
	}
}
