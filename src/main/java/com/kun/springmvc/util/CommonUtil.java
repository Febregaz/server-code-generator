/**
 * Program  : AppUtils.java
 * Author   : songkun
 * Create   : 2017年4月18日 下午6:38:10
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

import java.sql.Types;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.kun.springmvc.Config;
import com.kun.springmvc.Context;
import com.kun.springmvc.bean.TableClass;

/**
 * 工具类
 *
 * @author songkun
 * @version 1.0.0
 * @date 2017年4月18日 下午6:38:10
 */
public class CommonUtil {

	private CommonUtil() {
	}

	/**
	 * 表名到类名
	 * 
	 * @author songkun
	 * @create 2017年4月18日 下午5:39:25
	 * @param tableName
	 * @param config
	 * @return
	 * @return String
	 */
	public static final String tableName2ClassName(String tableName, Config config) {
		if (tableName == null || tableName.trim().length() <= 0) {
			return null;
		}
		tableName = tableName.trim();
		if (config.getTablePrefix() != null && config.getTablePrefix().length > 0) {
			for (String prefix : config.getTablePrefix()) {
				if (prefix != null && prefix.length() > 0 && tableName.startsWith(prefix)) {
					tableName = tableName.substring(prefix.length());
					break;
				}
			}
		}
		if (config.getTableNameSeparator() != null && config.getTableNameSeparator().length() > 0) {
			String[] splits = tableName.split(config.getTableNameSeparator());
			String className = "";
			for (int i = 0; i < splits.length; i++) {
				if (splits[i] == null || splits[i].length() <= 0) {
					continue;
				}
				className = className + firstCharToUpperCase(splits[i]);
			}
			return className;
		} else {
			return firstCharToUpperCase(tableName);
		}
	}

	/**
	 * 将首字母大写
	 * 
	 * @author songkun
	 * @create 2017年4月24日 下午2:24:43
	 * @param source
	 * @return String
	 */
	private static final String firstCharToUpperCase(String source) {
		if (source == null || (source = source.trim()).length() <= 0) {
			return "";
		}
		if (source.length() <= 1) {
			source = source.toUpperCase();
		} else {
			source = Character.toUpperCase(source.charAt(0)) + source.substring(1);
		}
		return source;
	}

	/**
	 * 将列名转换成属性名
	 * 
	 * @author songkun
	 * @create 2017年4月19日 下午6:58:43
	 * @param colName
	 * @param config
	 * @return String
	 */
	public static final String colName2FieldName(String colName, Config config) {
		if (colName == null || colName.length() <= 0) {
			return null;
		}
		colName = colName.trim();
		String fieldName = colName;
		if (config.getColNameSeparator() != null && config.getColNameSeparator().length() > 0) {
			String[] splits = colName.split(config.getColNameSeparator());
			fieldName = splits[0];
			for (int i = 1; i < splits.length; i++) {
				if (splits[i] == null || splits[i].length() <= 0) {
					continue;
				}
				if (splits[i].length() == 1) {
					fieldName = fieldName + Character.toUpperCase(splits[i].charAt(0));
				} else {
					fieldName = fieldName + Character.toUpperCase(splits[i].charAt(0)) + splits[i].substring(1);
				}
			}
		}
		return fieldName;
	}

	/**
	 * 构造包路径
	 * 
	 * @author songkun
	 * @create 2017年4月18日 下午6:39:47
	 * @param table2Class
	 * @param className
	 * @param config
	 * @return
	 * @return String
	 */
	public static final String getTargetPackage(TableClass table2Class, Config config) {
		if (table2Class == null || table2Class.getClassName() == null) {
			return null;
		}
		String targetPackage = null;
		if (table2Class != null && table2Class.getTargetPackage() != null) {
			targetPackage = table2Class.getTargetPackage();
		} else {
			if (config.getRootPackage() != null) {
				targetPackage = config.getRootPackage() + Context.DOT + Context.APP_PACKAGE_NAME + Context.DOT
						+ table2Class.getClassName().toLowerCase();
			} else {
				targetPackage = Context.APP_PACKAGE_NAME + Context.DOT + table2Class.getClassName().toLowerCase();
			}
		}
		return targetPackage;
	}

	/**
	 * 生成get方法
	 * 
	 * @author songkun
	 * @create 2017年4月21日 下午1:50:52
	 * @param fieldName
	 * @return String
	 */
	public static final String toGetMethod(String fieldName) {
		if (fieldName == null || fieldName.length() <= 0) {
			return null;
		}
		if (fieldName.length() == 1) {
			return "get" + Character.toUpperCase(fieldName.charAt(0));
		}
		return "get" + Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);
	}

	/**
	 * 生成set方法
	 * 
	 * @author songkun
	 * @create 2017年4月21日 下午1:51:37
	 * @param fieldName
	 * @return String
	 */
	public static final String toSetMethod(String fieldName) {
		if (fieldName == null || fieldName.length() <= 0) {
			return null;
		}
		if (fieldName.length() == 1) {
			return "set" + Character.toUpperCase(fieldName.charAt(0));
		}
		return "set" + Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);
	}

	/**
	 * 转换成驼峰式变量名,eg:firstSecondThird...
	 * 
	 * @author songkun
	 * @create 2017年4月23日 上午10:15:03
	 * @param source
	 * @param split
	 * @return String
	 */
	public static final String toVar(String source, String split) {
		if (source == null || source.length() <= 0) {
			return null;
		}
		String[] splits = {source};
		if (split != null) {
			splits = source.split(split);
		}
		if (splits == null || splits.length <= 0) {
			return null;
		}
		String result = "";
		int i = 0;
		char firstChar;
		for (String tmp : splits) {
			if (tmp == null || (tmp = tmp.trim()).length() <= 0) {
				continue;
			}
			if (i++ == 0) {
				firstChar = Character.toLowerCase(tmp.charAt(0));
			} else {
				firstChar = Character.toUpperCase(tmp.charAt(0));
			}
			result = result + firstChar + (tmp.length() > 1 ? tmp.substring(1) : "");
		}
		return result;
	}

	/**
	 * 获取当前年份
	 * 
	 * @author songkun
	 * @create 2017年4月21日 下午4:53:49
	 * @return String
	 */
	public static final String getYear() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy");
		return dateFormat.format(new Date());
	}

	/**
	 * 获取当前时间：yyyy-MM-dd HH:mm:ss
	 * 
	 * @author songkun
	 * @create 2017年4月21日 下午4:53:49
	 * @return String
	 */
	public static final String getDateTime() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return dateFormat.format(new Date());
	}

	/**
	 * 获取当前的时间：yyyyMMddHHmmss
	 * 
	 * @author songkun
	 * @create 2017年4月25日 下午5:18:46
	 * @return
	 * @return String
	 */
	public static final String getDateTimeOnlyNumber() {
		DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		return dateFormat.format(new Date());
	}

	/**
	 * 获取控制器类全名(包含package)
	 * 
	 * @author songkun
	 * @create 2017年4月23日 上午8:00:40
	 * @param modelClassName
	 * @param targetPackage
	 * @return String
	 */
	public static final String getControllerClassFullName(String modelClassName, String targetPackage) {
		StringBuilder fullName = new StringBuilder();
		fullName.append(getControllerPackage(targetPackage)).append(Context.DOT)
				.append(Context.CONTROLLER_CLASS_NAME_TPL.replace(Context.MODEL_NAME_PLACEHOLDER, modelClassName));
		return fullName.toString();
	}

	/**
	 * 获取控制器的package
	 * 
	 * @author songkun
	 * @create 2017年4月23日 上午8:00:18
	 * @param targetPackage
	 * @return String
	 */
	public static final String getControllerPackage(String targetPackage) {
		StringBuilder packageName = new StringBuilder();
		packageName.append(targetPackage).append(Context.DOT).append(Context.CONTROLLER_FOLDER_NAME);
		return packageName.toString();
	}

	/**
	 * 获取service接口类全名(包含package)
	 * 
	 * @author songkun
	 * @create 2017年4月23日 上午8:00:40
	 * @param modelClassName
	 * @param targetPackage
	 * @return String
	 */
	public static final String getServiceImplClassFullName(String modelClassName, String targetPackage) {
		StringBuilder fullName = new StringBuilder();
		fullName.append(getServiceImplPackage(targetPackage)).append(Context.DOT)
				.append(Context.SERVICE_IMPL_CLASS_NAME_TPL.replace(Context.MODEL_NAME_PLACEHOLDER, modelClassName));
		return fullName.toString();
	}

	/**
	 * 获取service接口的package
	 * 
	 * @author songkun
	 * @create 2017年4月23日 上午8:00:18
	 * @param targetPackage
	 * @return String
	 */
	public static final String getServiceImplPackage(String targetPackage) {
		StringBuilder packageName = new StringBuilder();
		packageName.append(targetPackage).append(Context.DOT).append(Context.SERVICE_FOLDER_NAME).append(Context.DOT)
				.append(Context.IMPL_FOLDER_NAME);
		return packageName.toString();
	}

	/**
	 * 获取service接口类全名(包含package)
	 * 
	 * @author songkun
	 * @create 2017年4月23日 上午8:00:40
	 * @param modelClassName
	 * @param targetPackage
	 * @return String
	 */
	public static final String getIServiceClassFullName(String modelClassName, String targetPackage) {
		StringBuilder fullName = new StringBuilder();
		fullName.append(getIServicePackage(targetPackage)).append(Context.DOT)
				.append(Context.ISERVICE_CLASS_NAME_TPL.replace(Context.MODEL_NAME_PLACEHOLDER, modelClassName));
		return fullName.toString();
	}

	/**
	 * 获取service接口的package
	 * 
	 * @author songkun
	 * @create 2017年4月23日 上午8:00:18
	 * @param targetPackage
	 * @return String
	 */
	public static final String getIServicePackage(String targetPackage) {
		StringBuilder packageName = new StringBuilder();
		packageName.append(targetPackage).append(Context.DOT).append(Context.SERVICE_FOLDER_NAME);
		return packageName.toString();
	}

	/**
	 * 获取data(dao)类全名(包含package)
	 * 
	 * @author songkun
	 * @create 2017年4月23日 上午8:00:40
	 * @param modelClassName
	 * @param targetPackage
	 * @return String
	 */
	public static final String getDataClassFullName(String modelClassName, String targetPackage) {
		StringBuilder fullName = new StringBuilder();
		fullName.append(getDataPackage(targetPackage)).append(Context.DOT)
				.append(Context.DATA_CLASS_NAME_TPL.replace(Context.MODEL_NAME_PLACEHOLDER, modelClassName));
		return fullName.toString();
	}

	/**
	 * 获取data(dao)的package
	 * 
	 * @author songkun
	 * @create 2017年4月23日 上午8:00:18
	 * @param targetPackage
	 * @return String
	 */
	public static final String getDataPackage(String targetPackage) {
		StringBuilder packageName = new StringBuilder();
		packageName.append(targetPackage).append(Context.DOT).append(Context.DATA_FOLDER_NAME);
		return packageName.toString();
	}

	/**
	 * 获取模型类全名(包含package)
	 * 
	 * @author songkun
	 * @create 2017年4月23日 上午8:00:40
	 * @param modelClassName
	 * @param targetPackage
	 * @return String
	 */
	public static final String getModelClassFullName(String modelClassName, String targetPackage) {
		StringBuilder fullName = new StringBuilder();
		fullName.append(getModelPackage(targetPackage)).append(Context.DOT)
				.append(Context.MODEL_CLASS_NAME_TPL.replace(Context.MODEL_NAME_PLACEHOLDER, modelClassName));
		return fullName.toString();
	}

	/**
	 * 获取模型的package
	 * 
	 * @author songkun
	 * @create 2017年4月23日 上午8:00:18
	 * @param targetPackage
	 * @return String
	 */
	public static final String getModelPackage(String targetPackage) {
		StringBuilder packageName = new StringBuilder();
		packageName.append(targetPackage).append(Context.DOT).append(Context.MODEL_FOLDER_NAME);
		return packageName.toString();
	}

	/**
	 * 生成import代码
	 * 
	 * @author songkun
	 * @create 2017年4月23日 上午10:34:40
	 * @param classFullName
	 * @return String
	 */
	public static final String getImportCode(String classFullName) {
		StringBuilder importBuilder = new StringBuilder();
		importBuilder.append("import ").append(classFullName).append(Context.SEMICOLON).append(Context.NEW_LINE);// import..xxxx;
		return importBuilder.toString();
	}
	private static final Map<Integer, String> JDBC_2_JAVA_MAPPER = new HashMap<>();

	/**
	 * 根据sql类型获取java类型
	 * 
	 * @author songkun
	 * @create 2017年4月24日 上午10:36:19
	 * @param sqlType
	 * @return String
	 */
	public static final String getJavaType(int sqlType) {
		return JDBC_2_JAVA_MAPPER.get(sqlType);
	}
	static {
		JDBC_2_JAVA_MAPPER.put(Types.ARRAY, "Object");
		JDBC_2_JAVA_MAPPER.put(Types.BIGINT, "Long");
		JDBC_2_JAVA_MAPPER.put(Types.BINARY, "byte[]");
		JDBC_2_JAVA_MAPPER.put(Types.BIT, "Boolean");
		JDBC_2_JAVA_MAPPER.put(Types.BLOB, "byte[]");
		JDBC_2_JAVA_MAPPER.put(Types.BOOLEAN, "Boolean");
		JDBC_2_JAVA_MAPPER.put(Types.CHAR, "String");
		JDBC_2_JAVA_MAPPER.put(Types.CLOB, "String");
		JDBC_2_JAVA_MAPPER.put(Types.DATALINK, "Object");
		JDBC_2_JAVA_MAPPER.put(Types.DATE, "java.util.Date");
		JDBC_2_JAVA_MAPPER.put(Types.DECIMAL, "java.math.BigDecimal");
		JDBC_2_JAVA_MAPPER.put(Types.DISTINCT, "Object");
		JDBC_2_JAVA_MAPPER.put(Types.DOUBLE, "Double");
		JDBC_2_JAVA_MAPPER.put(Types.FLOAT, "Double");
		JDBC_2_JAVA_MAPPER.put(Types.INTEGER, "Integer");
		JDBC_2_JAVA_MAPPER.put(Types.JAVA_OBJECT, "Object");
		JDBC_2_JAVA_MAPPER.put(Types.LONGNVARCHAR, "String");
		JDBC_2_JAVA_MAPPER.put(Types.LONGVARBINARY, "byte[]");
		JDBC_2_JAVA_MAPPER.put(Types.LONGVARCHAR, "String");
		JDBC_2_JAVA_MAPPER.put(Types.NCHAR, "String");
		JDBC_2_JAVA_MAPPER.put(Types.NCLOB, "String");
		JDBC_2_JAVA_MAPPER.put(Types.NVARCHAR, "String");
		JDBC_2_JAVA_MAPPER.put(Types.NULL, "Object");
		JDBC_2_JAVA_MAPPER.put(Types.NUMERIC, "java.math.BigDecimal");
		JDBC_2_JAVA_MAPPER.put(Types.OTHER, "Object");
		JDBC_2_JAVA_MAPPER.put(Types.REAL, "Float");
		JDBC_2_JAVA_MAPPER.put(Types.REF, "Object");
		JDBC_2_JAVA_MAPPER.put(Types.SMALLINT, "Short");
		JDBC_2_JAVA_MAPPER.put(Types.STRUCT, "Object");
		JDBC_2_JAVA_MAPPER.put(Types.TIME, "java.util.Date");
		JDBC_2_JAVA_MAPPER.put(Types.TIMESTAMP, "java.util.Date");
		JDBC_2_JAVA_MAPPER.put(Types.TINYINT, "Byte");
		JDBC_2_JAVA_MAPPER.put(Types.VARBINARY, "byte[]");
		JDBC_2_JAVA_MAPPER.put(Types.VARCHAR, "String");
	}
}
