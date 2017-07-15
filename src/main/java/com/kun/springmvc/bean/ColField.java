/**
 * Program  : Col.java
 * Author   : songkun
 * Create   : 2017年4月17日 下午7:47:17
 *
 * Copyright 2017 songkun. All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of songkun.  
 * You shall not disclose such Confidential Information and shall 
 * use it only in accordance with the terms of the license agreement 
 * you entered into with songkun.
 *
 */

package com.kun.springmvc.bean;

/**
 * 数据库表的列信息
 *
 * @author songkun
 * @version 1.0.0
 * @date 2017年4月17日 下午7:47:17
 */
public class ColField {

	private String colName;// 列名
	private String fieldName;// 字段名
	private int sqlType;// 列的类型，java.sql.Types
	private String sqlTypeName;// 列的类型名
	private String javaTypeName;// java字段类型
	private boolean isKey = false;// 是否是主键
	private boolean isKeyAutoInc = false;// 是否是自增
	private String remark;// 备注

	public String getColName() {
		return colName;
	}
	public void setColName(String colName) {
		this.colName = colName;
	}
	public String getFieldName() {
		return fieldName;
	}
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	public int getSqlType() {
		return sqlType;
	}
	public void setSqlType(int sqlType) {
		this.sqlType = sqlType;
	}
	public String getSqlTypeName() {
		return sqlTypeName;
	}
	public void setSqlTypeName(String sqlTypeName) {
		this.sqlTypeName = sqlTypeName;
	}
	public String getJavaTypeName() {
		return javaTypeName;
	}
	public void setJavaTypeName(String javaTypeName) {
		this.javaTypeName = javaTypeName;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public boolean isKey() {
		return isKey;
	}
	public void setKey(boolean isKey) {
		this.isKey = isKey;
	}
	public boolean isKeyAutoInc() {
		return isKeyAutoInc;
	}
	public void setKeyAutoInc(boolean isKeyAutoInc) {
		this.isKeyAutoInc = isKeyAutoInc;
	}

}
