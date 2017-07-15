/**
 * Program  : DbUtil.java
 * Author   : songkun
 * Create   : 2017年4月20日 上午8:24:52
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

package com.kun.springmvc.util;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import com.kun.springmvc.Config;
import com.kun.springmvc.bean.ColField;
import com.kun.springmvc.bean.TableClass;

/**
 * 数据库相关工具
 *
 * @author songkun
 * @version 1.0.0
 * @date 2017年4月20日 上午8:24:52
 */
public class DbUtil {

	private DbUtil() {
	}

	/**
	 * 打开数据库连接
	 * 
	 * @author songkun
	 * @create 2017年4月17日 下午7:18:08
	 * @param config
	 * @return
	 * @return Connection
	 */
	public static final Connection openConnection(Config config) {
		Connection conn = null;
		try {
			Class.forName(config.getDbDriver());// 动态加载mysql驱动
			System.out.println("SQL driver success...");
			Properties properties = new Properties();
			properties.setProperty("user", config.getDbUser());
			properties.setProperty("password", config.getDbPswd());
			// 设置可以获取remarks信息
			properties.setProperty("remarks", "true");
			properties.setProperty("useInformationSchema", "true");
			properties.put("remarksReporting", "true");
			properties.put("characterEncoding", "utf-8");
			// 一个Connection代表一个数据库连接
			return conn = DriverManager.getConnection(config.getDbUrl(), properties);
		} catch (Exception e) {
			if (conn != null) {
				closeConnection(conn);
			}
		}
		return null;
	}

	/**
	 * 关闭连接
	 * 
	 * @author songkun
	 * @create 2017年4月17日 下午7:17:57
	 * @param connection
	 * @return void
	 */
	public static final void closeConnection(Connection connection) {
		if (connection == null) {
			return;
		}
		try {
			connection.close();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	/**
	 * 关闭resultSet
	 * 
	 * @author songkun
	 * @create 2017年4月19日 下午7:44:39
	 * @param resultSet
	 * @return void
	 */
	public static final void closeResultSet(ResultSet resultSet) {
		if (resultSet == null) {
			return;
		}
		try {
			resultSet.close();
		} catch (Exception e) {
		}
	}

	/**
	 * 获取数据库列表
	 * 
	 * @author songkun
	 * @create 2017年4月17日 下午7:24:25
	 * @param statement
	 * @return List<Table2Class>
	 */
	public static final List<TableClass> listAllTables(DatabaseMetaData metaData, Config config) {
		if (metaData == null) {
			return null;
		}
		try {
			List<TableClass> table2Classes = new ArrayList<>();
			ResultSet resultSet = metaData.getTables(null, null, null, new String[]{"TABLE"});
			if (resultSet != null) {
				while (resultSet.next()) {
					String tableName = resultSet.getString("TABLE_NAME");
					if (config.isSkip(tableName)) {
						continue;
					}
					TableClass table2Class = new TableClass();
					table2Class.setComment(resultSet.getString("REMARKS"));
					table2Class.setTableName(tableName);
					TableClass cfg = (config == null) ? null : config.getTableConfig(table2Class.getTableName());
					if (cfg != null) {
						table2Class.setClassName(cfg.getClassName());
						table2Class.setTargetPackage(cfg.getTargetPackage());
						if (cfg.getComment() != null && cfg.getComment().length() > 0) {
							table2Class.setComment(cfg.getComment());
						}
					}
					if (table2Class.getClassName() == null) {
						table2Class.setClassName(CommonUtil.tableName2ClassName(table2Class.getTableName(), config));
					}
					if (table2Class.getTargetPackage() == null) {
						table2Class.setTargetPackage(CommonUtil.getTargetPackage(table2Class, config));
					}
					table2Classes.add(table2Class);
				}
			}
			resultSet.close();
			return table2Classes;
		} catch (Exception e) {
		}
		return null;
	}

	/**
	 * 获取列信息
	 * 
	 * @author songkun
	 * @create 2017年4月19日 下午7:45:15
	 * @param tableClass
	 * @param metaData
	 * @return List<Col>
	 */
	public static final void loadAllCols(TableClass tableClass, DatabaseMetaData metaData, Config config) {
		if (tableClass == null) {
			return;
		}
		try {
			Set<String> keys = new HashSet<>();
			ResultSet resultSet = metaData.getPrimaryKeys(null, null, tableClass.getTableName());
			if (resultSet != null) {
				while (resultSet.next()) {
					keys.add(resultSet.getString("COLUMN_NAME"));
				}
			}
			DbUtil.closeResultSet(resultSet);
			resultSet = metaData.getColumns(null, null, tableClass.getTableName(), null);
			if (resultSet != null) {
				while (resultSet.next()) {
					ColField colField = new ColField();
					colField.setColName(resultSet.getString("COLUMN_NAME"));
					colField.setSqlType(resultSet.getInt("DATA_TYPE"));
					colField.setSqlTypeName(JdbcTypeNameTranslator.getJdbcTypeName(colField.getSqlType()));
					colField.setRemark(resultSet.getString("REMARKS"));
					colField.setKeyAutoInc("YES".equalsIgnoreCase(resultSet.getString("IS_AUTOINCREMENT")));
					colField.setKey(keys.contains(colField.getColName()));
					colField.setFieldName(CommonUtil.colName2FieldName(colField.getColName(), config));
					colField.setJavaTypeName(CommonUtil.getJavaType(colField.getSqlType()));
					tableClass.addColField(colField);
				}
			}
			DbUtil.closeResultSet(resultSet);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 数据库字段是否是数字类型
	 * 
	 * @author songkun
	 * @create 2017年4月21日 上午11:03:47
	 * @param type
	 * @return boolean
	 */
	public static final boolean isNumber(int type) {
		return isInt(type) || isFloat(type);
	}

	/**
	 * 是否是整型数字
	 * 
	 * @author songkun
	 * @create 2017年5月16日 上午11:14:04
	 * @param type
	 * @return boolean
	 */
	public static final boolean isInt(int type) {
		switch (type) {
			case Types.BIT :
			case Types.TINYINT :
			case Types.SMALLINT :
			case Types.INTEGER :
			case Types.BIGINT :
				return true;
		}
		return false;
	}

	/**
	 * 数据库字段是否是数字类型
	 * 
	 * @author songkun
	 * @create 2017年4月21日 上午11:03:47
	 * @param type
	 * @return boolean
	 */
	public static final boolean isFloat(int type) {
		switch (type) {
			case Types.FLOAT :
			case Types.REAL :
			case Types.DOUBLE :
			case Types.NUMERIC :
			case Types.DECIMAL :
				return true;
		}
		return false;
	}

	/**
	 * 数据库字段是否是字符串类型
	 * 
	 * @author songkun
	 * @create 2017年4月21日 上午11:05:35
	 * @param type
	 * @return boolean
	 */
	public static final boolean isString(int type) {
		switch (type) {
			case Types.CHAR :
			case Types.VARCHAR :
			case Types.LONGVARCHAR :
				return true;
		}
		return false;
	}

	/**
	 * 数据库字段是否是时间类型
	 * 
	 * @author songkun
	 * @create 2017年4月21日 上午11:05:35
	 * @param type
	 * @return boolean
	 */
	public static final boolean isDate(int type) {
		switch (type) {
			case Types.DATE :
			case Types.TIME :
			case Types.TIMESTAMP :
				return true;
		}
		return false;
	}
}
