/**
 * Program  : Order.java
 * Author   : songkun
 *
 */

package ${rootPackage}.common.bean;

/**
 * 排序对象
 *
 * @version 1.0.0
 */
public class OrderBy {

	public static final int TYPE_ASC = 0; // 排序-顺序
	public static final int TYPE_DESC = 1;// 排序-倒序

	private String field;// 字段名
	private Integer type = TYPE_DESC;// 排序类型

	public String getField() {
		return field;
	}
	public void setField(String field) {
		this.field = field;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}

}
