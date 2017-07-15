/**
 * Program  : Table.java
 * Author   : songkun
 * Create   : 2017年4月17日 下午5:27:49
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

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

/**
 * 表/类 相关的参数
 *
 * @author songkun
 * @version 1.0.0
 * @date 2017年4月17日 下午5:27:49
 */
public class TableClass {

	private String tableName;// 数据库表名
	private String className;// 对应的类名
	private String comment;// 注释
	private String targetPackage;// 包名
	private List<ColField> colFields = new ArrayList<>();// 列/字段 相关信息

	public List<ColField> getColFields() {
		return colFields;
	}
	public void setColFields(List<ColField> colFields) {
		this.colFields = colFields;
	}
	public void addColField(ColField colField) {
		if (colField == null) {
			return;
		}
		colFields.add(colField);
	}
	public String getComment() {
		if (this.comment == null) {
			return "";
		}
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}

	public String getTargetPackage() {
		return targetPackage;
	}
	public void setTargetPackage(String targetPackage) {
		this.targetPackage = targetPackage;
	}
	/**
	 * 获取表实例
	 * 
	 * @author songkun
	 * @create 2017年4月17日 下午5:35:10
	 * @param node
	 * @return Table
	 */
	public static final TableClass getInstance(Node node) {
		if (node == null) {
			return null;
		}
		NamedNodeMap attrs = node.getAttributes();
		Node tmp = attrs.getNamedItem("tableName");
		if (tmp == null || tmp.getTextContent() == null || tmp.getTextContent().trim().length() <= 0) {
			return null;
		}
		TableClass table = new TableClass();
		table.setTableName(tmp.getTextContent().trim());
		tmp = attrs.getNamedItem("className");
		if (tmp != null && tmp.getTextContent() != null && tmp.getTextContent().trim().length() > 0) {
			table.setClassName(tmp.getTextContent().trim());
		}
		tmp = attrs.getNamedItem("targetPackage");
		if (tmp != null && tmp.getTextContent() != null && tmp.getTextContent().trim().length() > 0) {
			table.setTargetPackage(tmp.getTextContent().trim());
		}
		tmp = attrs.getNamedItem("comment");
		if (tmp != null && tmp.getTextContent() != null && tmp.getTextContent().trim().length() > 0) {
			table.setComment(tmp.getTextContent().trim());
		}
		return table;
	}

}
