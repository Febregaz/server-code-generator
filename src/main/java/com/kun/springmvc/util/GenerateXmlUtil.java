/**
 * Program  : GenerateUtil.java
 * Author   : songkun
 * Create   : 2017年4月18日 下午6:47:47
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

import com.kun.springmvc.Context;
import com.kun.springmvc.bean.ColField;
import com.kun.springmvc.bean.TableClass;
import com.kun.springmvc.mybatis.Resources;

/**
 * 生成器工具类
 *
 * @author songkun
 * @version 1.0.0
 * @date 2017年4月18日 下午6:47:47
 */
public class GenerateXmlUtil {
	private GenerateXmlUtil() {
	}
	private static final int LINE_WIDTH = 100;// 行宽

	private static final String MAPPER_XML_TPL_FILE_NAME = "tpl/mapperxml.tpl";// 模板文件名
	private static String MAPPER_XML_TPL_CONTENT;// 模板文件内容

	private static final String MAPPER_XML_FILE_NAME_TPL = Context.MODEL_NAME_PLACEHOLDER + "Mapper.xml";

	// 模板文件中的占位符
	private static final String DATA_CLASS_FULL_NAME_PLACEHOLDER = "${dataClassFullName}";
	private static final String MODEL_CLASS_FULL_NAME_PLACEHOLDER = "${modelClassFullName}";
	private static final String TABLE_NAME_PLACEHOLDER = "${tableName}";
	private static final String USE_GENERATED_KEYS_PLACEHOLDER = "${useGeneratedKeys}";
	private static final String OBJECT_KEY_WHERE_PLACEHOLDER = "${objectKeyWhere}";
	private static final String KEY_WHERE_PLACEHOLDER = "${keyWhere}";
	private static final String ORDER_BY_KEY_DESC_PLACEHOLDER = "${orderByKeyDesc}";
	private static final String COLS_PLACEHOLDER = "${cols}";
	private static final String RESULT_MAP_PLACEHOLDER = "${ResultMap}";
	private static final String INSERT_SQL_FRAGMENT_PLACEHOLDER = "${insertSqlFragment}";
	private static final String UPDATE_SQL_FRAGMENT_PLACEHOLDER = "${updateSqlFragment}";
	private static final String COMMON_STRING_WHERE_PLACEHOLDER = "${commonStringWhere}";
	private static final String SEARCH_STRING_WHERE_PLACEHOLDER = "${searchStringWhere}";
	private static final String COMMON_EQUAL_WHERE_PLACEHOLDER = "${commonEqualWhere}";
	private static final String ORDER_BY_WHEN_PLACEHOLDER = "${orderByWhen}";

	private static final String MYBATIS_FOLDER_NAME = "mybatis";

	static {
		try {// 初始化模板文件
			MAPPER_XML_TPL_CONTENT = FileUtil.readFileToString(Resources.getResourceAsFile(MAPPER_XML_TPL_FILE_NAME));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 生成mybatis xml
	 * 
	 * @author songkun
	 * @create 2017年4月18日 下午6:48:42
	 * @param tableName
	 * @param className
	 * @param targetPackage
	 * @param cols
	 * @return void
	 */
	public static final void generateXml(TableClass tableClass, String resourceRootDir, boolean overWrite) {
		if (tableClass == null || tableClass.getColFields() == null || tableClass.getColFields().size() <= 0) {
			return;
		}
		String xmlRootDir = resourceRootDir;
		if (xmlRootDir != null && xmlRootDir.length() > 0) {
			xmlRootDir = xmlRootDir + (xmlRootDir.endsWith("/") ? "" : "/") + MYBATIS_FOLDER_NAME;
		} else {
			xmlRootDir = MYBATIS_FOLDER_NAME;
		}
		String content = MAPPER_XML_TPL_CONTENT
				.replace(DATA_CLASS_FULL_NAME_PLACEHOLDER, CommonUtil.getDataClassFullName(tableClass.getClassName(), tableClass.getTargetPackage()))
				.replace(MODEL_CLASS_FULL_NAME_PLACEHOLDER,
						CommonUtil.getModelClassFullName(tableClass.getClassName(), tableClass.getTargetPackage()))
				.replace(TABLE_NAME_PLACEHOLDER, tableClass.getTableName()).replace(USE_GENERATED_KEYS_PLACEHOLDER, buildUseGeneratedKeys(tableClass))
				.replace(OBJECT_KEY_WHERE_PLACEHOLDER, buildObjectKeyWhere(tableClass)).replace(KEY_WHERE_PLACEHOLDER, buildKeyWhere(tableClass))
				.replace(ORDER_BY_KEY_DESC_PLACEHOLDER, buildOrderByKeyDesc(tableClass)).replace(COLS_PLACEHOLDER, buildColumns(tableClass))
				.replace(RESULT_MAP_PLACEHOLDER, buildResultMap(tableClass)).replace(INSERT_SQL_FRAGMENT_PLACEHOLDER, buildInsert(tableClass))
				.replace(UPDATE_SQL_FRAGMENT_PLACEHOLDER, buildUpdate(tableClass))
				.replace(COMMON_STRING_WHERE_PLACEHOLDER, buildCommonStringWhere(tableClass))
				.replace(SEARCH_STRING_WHERE_PLACEHOLDER, buildSearchStringWhere(tableClass))
				.replace(COMMON_EQUAL_WHERE_PLACEHOLDER, buildCommonEqualWhere(tableClass))
				.replace(ORDER_BY_WHEN_PLACEHOLDER, buildOrderByWhen(tableClass));
		FileUtil.writeStringToFile(FileUtil.initXmlFile(MAPPER_XML_FILE_NAME_TPL.replace(Context.MODEL_NAME_PLACEHOLDER, tableClass.getClassName()),
				tableClass.getClassName().toLowerCase(), xmlRootDir, overWrite), content);
	}

	/**
	 * eg: order by id desc
	 * 
	 * @author songkun
	 * @create 2017年4月23日 下午1:39:16
	 * @param cols
	 * @return String
	 */
	private static final String buildOrderByKeyDesc(TableClass tableClass) {
		if (tableClass == null || tableClass.getColFields() == null || tableClass.getColFields().size() <= 0) {
			return "";
		}
		StringBuilder orderBuilder = new StringBuilder();
		for (ColField colField : tableClass.getColFields()) {
			if (colField.isKey()) {
				orderBuilder.append("order by ").append(colField.getColName()).append(" desc");
				break;
			}
		}
		return orderBuilder.toString();

	}

	/**
	 * id=#{key}
	 * 
	 * @author songkun
	 * @create 2017年4月23日 下午1:31:35
	 * @param cols
	 * @return String
	 */
	private static final String buildKeyWhere(TableClass tableClass) {
		if (tableClass == null || tableClass.getColFields() == null || tableClass.getColFields().size() <= 0) {
			return "";
		}
		StringBuilder keyBuilder = new StringBuilder();
		for (ColField colField : tableClass.getColFields()) {
			if (colField.isKey()) {
				keyBuilder.append(colField.getColName()).append("=#{key}");
				break;
			}
		}
		return keyBuilder.toString();

	}

	/**
	 * eg:id = #{object.id,jdbcType=BIGINT}
	 * 
	 * @author songkun
	 * @create 2017年4月23日 下午1:29:42
	 * @param cols
	 * @param config
	 * @return
	 * @return String
	 */
	private static final String buildObjectKeyWhere(TableClass tableClass) {
		if (tableClass == null || tableClass.getColFields() == null || tableClass.getColFields().size() <= 0) {
			return "";
		}
		StringBuilder keyBuilder = new StringBuilder();
		for (ColField colField : tableClass.getColFields()) {
			if (colField.isKey()) {
				keyBuilder.append(colField.getColName()).append("=#{object.").append(colField.getFieldName()).append(",jdbcType=")
						.append(colField.getSqlTypeName()).append("}");
				break;
			}
		}
		return keyBuilder.toString();
	}

	/**
	 * 在新增记录的时候是否返回key
	 * 
	 * @author songkun
	 * @create 2017年4月23日 下午1:21:50
	 * @param colFields
	 * @param config
	 * @return
	 * @return String
	 */
	private static final String buildUseGeneratedKeys(TableClass tableClass) {
		if (tableClass == null || tableClass.getColFields() == null || tableClass.getColFields().size() <= 0) {
			return "";
		}
		StringBuilder keyBuilder = new StringBuilder();
		for (ColField colField : tableClass.getColFields()) {
			if (colField.isKey() && colField.isKeyAutoInc()) {
				keyBuilder.append("useGeneratedKeys=\"true\" keyProperty=\"object.").append(colField.getFieldName()).append("\"");
				break;
			}
		}
		return keyBuilder.toString();
	}

	/**
	 * 构造order by 片段
	 * 
	 * @author songkun
	 * @create 2017年4月21日 下午2:48:16
	 * @param tableClass
	 * @return String
	 */
	private static final String buildOrderByWhen(TableClass tableClass) {
		if (tableClass == null || tableClass.getColFields() == null || tableClass.getColFields().size() <= 0) {
			return "";
		}
		StringBuilder orderByBuilder = new StringBuilder();
		for (ColField col : tableClass.getColFields()) {
			if (DbUtil.isNumber(col.getSqlType()) || DbUtil.isString(col.getSqlType()) || DbUtil.isDate(col.getSqlType())) {
				if (orderByBuilder.length() > 0) {
					orderByBuilder.append(Context.NEW_LINE).append(Context.TAB).append(Context.TAB).append(Context.TAB).append(Context.TAB)
							.append(Context.TAB);
				}
				orderByBuilder.append("<when test=\"item.field =='").append(col.getFieldName()).append("'\">").append(Context.NEW_LINE);
				orderByBuilder.append(Context.TAB).append(Context.TAB).append(Context.TAB).append(Context.TAB).append(Context.TAB).append(Context.TAB)
						.append(col.getColName()).append(" <if test=\"item.type == 1\">desc</if>").append(Context.NEW_LINE);
				orderByBuilder.append(Context.TAB).append(Context.TAB).append(Context.TAB).append(Context.TAB).append(Context.TAB).append("</when>");
			}
		}
		return orderByBuilder.toString();
	}

	/**
	 * 构造 Common_Fragment 片段 <br>
	 * 
	 * @author songkun
	 * @create 2017年4月21日 上午10:13:39
	 * @return String
	 */
	private static final String buildCommonEqualWhere(TableClass tableClass) {
		if (tableClass == null || tableClass.getColFields() == null || tableClass.getColFields().size() <= 0) {
			return "";
		}
		StringBuilder whereBuilder = new StringBuilder();
		for (ColField colField : tableClass.getColFields()) {
			if (DbUtil.isNumber(colField.getSqlType())) {
				if (whereBuilder.length() > 0) {
					whereBuilder.append(Context.NEW_LINE).append(Context.TAB).append(Context.TAB);
				}
				whereBuilder.append("<if test=\"object.").append(colField.getFieldName()).append("!=null\">").append(colField.getColName())
						.append("=#{object.").append(colField.getFieldName()).append("} and ").append("</if>");
			}
		}
		return whereBuilder.toString();
	}

	/**
	 * 构造 Search_Where_Fragment 片段 <br>
	 * 
	 * &lt;sql id="Search_Where_Fragment"&gt;<br>
	 * &nbsp;&nbsp;&lt;trim prefix="where" suffixOverrides="and"&gt;<br>
	 * &nbsp;&nbsp;&nbsp;&lt;include refid="Common_Fragment" /&gt;<br>
	 * &nbsp;&nbsp;&nbsp;&lt;if test="object.name != null and object.name !=
	 * ''"&gt;<br>
	 * &nbsp;&nbsp;&nbsp;&nbsp;&lt;bind name="objName"
	 * value="'%'+object.getName()+'%'" /&gt;<br>
	 * &nbsp;&nbsp;&nbsp;&nbsp;name like #{objName} and <br>
	 * &nbsp;&nbsp;&nbsp;&lt;/if&gt;<br>
	 * &nbsp;&nbsp;&nbsp;&lt;/trim&gt;<br>
	 * &lt;/sql&gt;
	 * 
	 * @author songkun
	 * @create 2017年4月21日 上午10:13:39
	 * @param tableClass
	 * @return String
	 */
	private static final String buildSearchStringWhere(TableClass tableClass) {
		if (tableClass == null || tableClass.getColFields() == null || tableClass.getColFields().size() <= 0) {
			return "";
		}
		StringBuilder searchBuilder = new StringBuilder();
		int index = 0;
		for (ColField colField : tableClass.getColFields()) {
			if (DbUtil.isString(colField.getSqlType())) {
				if (searchBuilder.length() > 0) {
					searchBuilder.append(Context.NEW_LINE).append(Context.TAB).append(Context.TAB).append(Context.TAB);
				}
				String paramName = "paramName" + index++;
				searchBuilder.append("<if test=\"object.").append(colField.getFieldName()).append("!=null and object.")
						.append(colField.getFieldName()).append("!=''\">").append(Context.NEW_LINE);
				searchBuilder.append(Context.TAB).append(Context.TAB).append(Context.TAB).append(Context.TAB).append("<bind name=\"")
						.append(paramName).append("\" value=\"'%'+object.").append(CommonUtil.toGetMethod(colField.getFieldName()))
						.append("()+'%'\"/>").append(Context.NEW_LINE);
				searchBuilder.append(Context.TAB).append(Context.TAB).append(Context.TAB).append(Context.TAB).append(colField.getColName())
						.append(" like #{").append(paramName).append("} and ").append(Context.NEW_LINE);
				searchBuilder.append(Context.TAB).append(Context.TAB).append(Context.TAB).append("</if>");
			}
		}
		return searchBuilder.toString();
	}

	/**
	 * 构造 Common_Where_Fragment 片段
	 * 
	 * @author songkun
	 * @create 2017年4月21日 上午10:13:39
	 * @param tableClass
	 * @return String
	 */
	private static final String buildCommonStringWhere(TableClass tableClass) {
		if (tableClass == null || tableClass.getColFields() == null || tableClass.getColFields().size() <= 0) {
			return "";
		}
		StringBuilder findBuilder = new StringBuilder();
		for (ColField colField : tableClass.getColFields()) {// 如果是字符串，自动生成(数字类型在Common_Fragment生成)，其他的类型(非[数字/字符串])，自己手动增加
			if (DbUtil.isString(colField.getSqlType())) {
				if (findBuilder.length() > 0) {
					findBuilder.append(Context.NEW_LINE).append(Context.TAB).append(Context.TAB).append(Context.TAB);
				}
				findBuilder.append("<if test=\"object.").append(colField.getFieldName()).append("!=null and object.").append(colField.getFieldName())
						.append("!=''\">").append(Context.NEW_LINE);
				findBuilder.append(Context.TAB).append(Context.TAB).append(Context.TAB).append(Context.TAB).append(colField.getColName())
						.append("=#{object.").append(colField.getFieldName()).append("} and ").append(Context.NEW_LINE);
				findBuilder.append(Context.TAB).append(Context.TAB).append(Context.TAB).append("</if>");
			}
		}
		return findBuilder.toString();
	}

	/**
	 * 构造update片段
	 * 
	 * @author songkun
	 * @create 2017年4月23日 下午1:52:27
	 * @param tableClass
	 * @return String
	 */
	private static final String buildUpdate(TableClass tableClass) {
		if (tableClass == null || tableClass.getColFields() == null || tableClass.getColFields().size() <= 0) {
			return "";
		}
		StringBuilder updateBuilder = new StringBuilder();
		updateBuilder.append("<set>").append(Context.NEW_LINE);
		for (ColField colField : tableClass.getColFields()) {
			if (colField.isKeyAutoInc()) {// 如果是自增的，则跳过
				continue;
			}
			// <if test="object.fieldName !=
			// null">col_name=#{object.fieldName,jdbcType=VARCHAR},</if>
			updateBuilder.append(Context.TAB).append(Context.TAB).append(Context.TAB).append("<if test=\"object.").append(colField.getFieldName())
					.append(" != null\">").append(colField.getColName()).append("=#{object.").append(colField.getFieldName()).append(",jdbcType=")
					.append(colField.getSqlTypeName()).append("},</if>").append(Context.NEW_LINE);
		}
		updateBuilder.append(Context.TAB).append(Context.TAB).append("</set>");
		return updateBuilder.toString();
	}

	/**
	 * 构造insert片段
	 * 
	 * @author songkun
	 * @create 2017年4月20日 下午5:27:54
	 * @param tableClass
	 * @return String
	 */
	private static final String buildInsert(TableClass tableClass) {
		if (tableClass == null || tableClass.getColFields() == null || tableClass.getColFields().size() <= 0) {
			return "";
		}
		StringBuilder insertBuilder = new StringBuilder();
		StringBuilder insertCols = new StringBuilder();
		StringBuilder insertVals = new StringBuilder();
		insertCols.append("<trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\">").append(Context.NEW_LINE);
		insertVals.append(Context.TAB).append(Context.TAB).append("<trim prefix=\"values (\" suffix=\")\" suffixOverrides=\",\">")
				.append(Context.NEW_LINE);
		for (ColField col : tableClass.getColFields()) {
			if (col.isKeyAutoInc()) {// 如果是自增的，则跳过
				continue;
			}
			// <if test="object.fieldName != null">col_name,</if>
			insertCols.append(Context.TAB).append(Context.TAB).append(Context.TAB).append("<if test=\"object.").append(col.getFieldName())
					.append(" != null\">").append(col.getColName()).append(",</if>").append(Context.NEW_LINE);
			// <if test="object.fieldName !=
			// null">#{object.fieldName,jdbcType=VARCHAR},</if>
			insertVals.append(Context.TAB).append(Context.TAB).append(Context.TAB).append("<if test=\"object.").append(col.getFieldName())
					.append(" != null\">").append("#{object.").append(col.getFieldName()).append(",jdbcType=").append(col.getSqlTypeName())
					.append("},</if>").append(Context.NEW_LINE);
		}
		insertCols.append(Context.TAB).append(Context.TAB).append("</trim>").append(Context.NEW_LINE);
		insertVals.append(Context.TAB).append(Context.TAB).append("</trim>");
		insertBuilder.append(insertCols).append(insertVals);
		return insertBuilder.toString();
	}

	/**
	 * 构造列片段
	 * 
	 * @author songkun
	 * @create 2017年4月20日 上午8:54:30
	 * @param tableClass
	 * @return String
	 */
	private static final String buildColumns(TableClass tableClass) {
		if (tableClass == null || tableClass.getColFields() == null || tableClass.getColFields().size() <= 0) {
			return "";
		}
		StringBuilder builder = new StringBuilder();
		int lineWidth = 0;
		for (ColField colField : tableClass.getColFields()) {
			if (lineWidth >= LINE_WIDTH) {
				builder.append(Context.NEW_LINE).append(Context.TAB).append(Context.TAB);
				lineWidth = 0;
			}
			builder.append(colField.getColName()).append(Context.COMMA);
			lineWidth = lineWidth + colField.getColName().length() + 1;
		}
		if (builder.length() > 0) {
			builder.setLength(builder.length() - 1);
		}
		return builder.toString();
	}

	/**
	 * 生成ResultMap
	 * 
	 * @author songkun
	 * @create 2017年4月20日 上午8:36:09
	 * @param tableClass
	 * @return String
	 */
	private static final String buildResultMap(TableClass tableClass) {
		if (tableClass == null || tableClass.getColFields() == null || tableClass.getColFields().size() <= 0) {
			return "";
		}
		StringBuilder builder = new StringBuilder();
		for (ColField colField : tableClass.getColFields()) {
			if (builder.length() > 0) {// 第一行不用tab,因为模板文件中有了
				builder.append(Context.NEW_LINE).append(Context.TAB).append(Context.TAB);
			}
			if (colField.isKey()) {
				builder.append("<id column=\"");
			} else {
				builder.append("<result column=\"");
			}
			builder.append(colField.getColName()).append("\" property=\"").append(colField.getFieldName());
			builder.append("\" jdbcType=\"").append(colField.getSqlTypeName()).append("\"/>");
		}
		return builder.toString();
	}

}
