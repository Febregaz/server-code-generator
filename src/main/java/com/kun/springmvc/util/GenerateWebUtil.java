/**
 * Program  : GenerateWebUtil.java
 * Author   : songkun
 * Create   : 2017年5月4日 下午6:37:45
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

import java.io.File;
import java.sql.Types;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.kun.springmvc.Config;
import com.kun.springmvc.Context;
import com.kun.springmvc.bean.ColField;
import com.kun.springmvc.bean.TableClass;
import com.kun.springmvc.mybatis.Resources;

/**
 * 生成web文件夹的工具类
 *
 * @author songkun
 * @version 1.0.0
 * @date 2017年5月4日 下午6:37:45
 */
public class GenerateWebUtil {

	private GenerateWebUtil() {
	}
	private static final String WEB_TPL_FOLDER = "tpl/web/webtpl";
	private static final String PROJECT_NAME_PLACEHOLDER = "${projectName}";
	private static final String AUTHOR_PLACEHOLDER = "${author}";
	private static final String YEAR_PLACEHOLDER = "${year}";
	private static final String MODELVARNAME_PLACEHOLDER = "${modelVarName}";
	private static final String MODELVARNAME_LOWERCASE_PLACEHOLDER = "${modelVarNameLowerCase}";
	private static final String CUSTOMTOOLHTML_PLACEHOLDER = "${customToolHtml}";
	private static final String POSTDATA_PLACEHOLDER = "${postData}";
	private static final String COLUMNS_PLACEHOLDER = "${columns}";
	private static final String FIELD_NAME_TPL = "${fieldName}";
	private static final String JS_FIELDS_TPL = "${jsFields}";
	private static final String WEB_FIELDS_TPL = "${webFields}";
	private static final String HIDDEN_KEYS_TPL = "${hiddenKeys}";
	private static final String HIDDEN_TPL = "${hidden}";
	private static final String NAME_FIELD = "name";
	// 搜索
	private static final String SEARCH_HTML = "<div class=\"pull-left\" style=\"margin-left:5px;\"><input id=\"search_input_name\" type=\"text\" onchange=search() class=\"form-control\" style=\"width:150px; height:28px;float:left;padding:0px;\"><button onclick=search() class=\"btn btn-primary btn-sm\" type=\"button\"  style=\"padding:3px 5px 0px 5px;margin-left:10px;margin-top:2px;\"><i class=\"glyphicon glyphicon-search\"></i></button></div>";
	private static final String POSTDATA_TPL = "${fieldName}:$('#search_input_name').val()";

	// columns
	private static final String COLUMN_TPL = "{\n            field: \"${fieldName}\",\n            header: \"${fieldName}\",\n            hidden: ${hidden},\n            formatter: function(rowIndex, value, rowData) {\n               return value;\n            }\n        }";
	// columns operate buttons
	private static final String COLUMN_OPERATE_TPL = "{\n            field: \"\",\n            exculdeVisible: true,\n            header: \"\",\n            formatter: function(rowIndex, value, rowData) {\n                var editBtn = '<button title=\"修改\" onclick=edit(this,\"' + rowData['${fieldName}'] +\n                    '\") type=\"button\" class=\"btn btn-primary btn-sm\" style=\"padding:0px;padding-left:3px;padding-right:2px;margin-left:5px;\">' +\n                    '<i class=\"glyphicon glyphicon-edit \")></i></button>';\n                var deleteBtn = '<button title=\"删除\" onclick=del(this,\"' + rowData['${fieldName}'] +\n                    '\") type=\"button\" class=\"btn btn-primary btn-sm\" style=\"padding:0px;padding-left:3px;padding-right:2px;margin-left:5px;\">' +\n                    '<i class=\"glyphicon glyphicon-remove\"></i></button>';\n                return editBtn + deleteBtn;\n            }\n        }";

	private static final String JS_STRING_FIELD_TPL = "\n            ${fieldName}: {\n                validators: {\n                    notEmpty: {\n                        message: '不能为空'\n                    },\n                }\n            }";
	private static final String JS_INT_FIELD_TPL = "\n            ${fieldName}: {\n                validators: {\n                    notEmpty: {\n                        message: '请输入0-99,999,999,999的正整数'\n                    },\n                    regexp: {\n                        regexp: /^(0|([1-9][0-9]{0,10}))$/,\n                        message: '请输入1-99,999,999,999的正整数'\n                    }\n                }\n            }";
	private static final String JS_FLOAT_FIELD_TPL = "\n            ${fieldName}: {\n                validators: {\n                    notEmpty: {\n                        message: '不能为空'\n                    },\n                    regexp: {\n                        regexp: /^((0(\\.((0[1-9])|([1-9]\\d?))))|(100000000(\\.[0]{1,2})?)|(([1-9]\\d{0,7})(\\.[0-9]{1,2})?))$/,\n                        message: '请输入(0-100,000,000]的正数(保留两位小数点)'\n                    }\n                }\n            }";

	private static final String WEB_FIELD_TPL = "							<div class=\"form-group\">\n								<label class=\"col-xs-2 control-label\" style=\"text-align: right;\">${fieldName}<font style=\"color: red;\">*</font></label>\n								<div class=\"col-xs-4\">\n									<input type=\"text\" class=\"form-control\" name=\"${fieldName}\" />\n								</div>\n							</div>";

	private static final String HIDDEN_KEY_TPL = "							<input type=\"hidden\" name=\"${fieldName}\" />";

	private static final String JS_FIELD_SETTERS_TPL = "${jsFieldSetters}";
	private static final String JS_FIELD_SETTER_TPL = "            $('input[name=\"${fieldName}\"]').val(result.${fieldName});";

	/**
	 * 生成js/html
	 * 
	 * @author songkun
	 * @create 2017年5月12日 下午6:50:30
	 * @param config
	 * @param tableClass
	 * @return void
	 */
	public static final void generateWebApp(Config config, TableClass tableClass) {
		try {
			File source = Resources.getResourceAsFile(WEB_TPL_FOLDER);
			String destFolder = config.getWebRootPath();
			if (!(destFolder.endsWith("/") || destFolder.endsWith("\\"))) {
				destFolder = destFolder + "/";
			}
			File dest = new File(destFolder + tableClass.getClassName().toLowerCase());
			Map<String, String> placeHolders = new HashMap<>();
			placeHolders.put(PROJECT_NAME_PLACEHOLDER, config.getProjectName());
			placeHolders.put(AUTHOR_PLACEHOLDER, config.getAuthor());
			placeHolders.put(YEAR_PLACEHOLDER, CommonUtil.getYear());
			placeHolders.put(MODELVARNAME_PLACEHOLDER, CommonUtil.toVar(tableClass.getClassName(), null));
			placeHolders.put(MODELVARNAME_LOWERCASE_PLACEHOLDER, tableClass.getClassName().toLowerCase());
			generateCustomToolHtml(tableClass, placeHolders);
			generateColumns(tableClass, placeHolders);
			generateJsFields(tableClass, placeHolders);
			generateWebFields(tableClass, placeHolders);
			generateHiddenKeys(tableClass, placeHolders);
			generateJsFieldSetters(tableClass, placeHolders);
			FileUtil.doCopyFileOrFolder(source, dest, placeHolders, true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 生成修改页面的赋值语句（对各属性赋值）
	 * 
	 * @author songkun
	 * @create 2017年5月16日 下午3:19:41
	 * @param tableClass
	 * @param placeHolders
	 * @return void
	 */
	private static final void generateJsFieldSetters(TableClass tableClass, Map<String, String> placeHolders) {
		placeHolders.put(JS_FIELD_SETTERS_TPL, "");
		List<ColField> colFields = tableClass.getColFields();
		if (colFields == null || colFields.size() <= 0) {
			return;
		}
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < colFields.size(); i++) {
			ColField colField = colFields.get(i);
			builder.append(Context.NEW_LINE).append(JS_FIELD_SETTER_TPL.replace(FIELD_NAME_TPL, colField.getFieldName()));
		}
		placeHolders.put(JS_FIELD_SETTERS_TPL, builder.toString());
	}

	/**
	 * 构造隐藏的“键”列表（修改页面）
	 * 
	 * @author songkun
	 * @create 2017年5月16日 下午1:50:38
	 * @param tableClass
	 * @param placeHolders
	 * @return void
	 */
	private static final void generateHiddenKeys(TableClass tableClass, Map<String, String> placeHolders) {
		placeHolders.put(HIDDEN_KEYS_TPL, "");
		List<ColField> colFields = tableClass.getColFields();
		if (colFields == null || colFields.size() <= 0) {
			return;
		}
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < colFields.size(); i++) {
			ColField colField = colFields.get(i);
			if (colField.isKey()) {
				if (builder.length() <= 0) {
					builder.append(HIDDEN_KEY_TPL.trim().replace(FIELD_NAME_TPL, colField.getFieldName())).append(Context.NEW_LINE);
				} else {
					builder.append(HIDDEN_KEY_TPL.replace(FIELD_NAME_TPL, colField.getFieldName())).append(Context.NEW_LINE);
				}
			}
		}
		if (builder.length() > 0) {
			builder.setLength(builder.length() - 1);
		}
		placeHolders.put(HIDDEN_KEYS_TPL, builder.toString());
	}

	/**
	 * 生成web属性
	 * 
	 * @author songkun
	 * @create 2017年5月16日 下午1:41:59
	 * @param tableClass
	 * @param placeHolders
	 * @return void
	 */
	private static final void generateWebFields(TableClass tableClass, Map<String, String> placeHolders) {
		placeHolders.put(WEB_FIELDS_TPL, "");
		List<ColField> colFields = tableClass.getColFields();
		if (colFields == null || colFields.size() <= 0) {
			return;
		}
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < colFields.size(); i++) {
			ColField colField = colFields.get(i);
			if (colField.isKey()) {
				continue;
			}
			if (DbUtil.isNumber(colField.getSqlType()) || DbUtil.isString(colField.getSqlType())) {
				if (builder.length() <= 0) {
					builder.append(WEB_FIELD_TPL.trim().replace(FIELD_NAME_TPL, colField.getFieldName())).append(Context.NEW_LINE);
				} else {
					builder.append(WEB_FIELD_TPL.replace(FIELD_NAME_TPL, colField.getFieldName())).append(Context.NEW_LINE);
				}
			}
		}
		if (builder.length() > 0) {
			builder.setLength(builder.length() - 1);
		}
		placeHolders.put(WEB_FIELDS_TPL, builder.toString());
	}
	/**
	 * 生成js的属性validate列表
	 * 
	 * @author songkun
	 * @create 2017年5月16日 上午11:24:58
	 * @param tableClass
	 * @param placeHolders
	 * @return void
	 */
	private static final void generateJsFields(TableClass tableClass, Map<String, String> placeHolders) {
		placeHolders.put(JS_FIELDS_TPL, "");
		List<ColField> colFields = tableClass.getColFields();
		if (colFields == null || colFields.size() <= 0) {
			return;
		}
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < colFields.size(); i++) {
			ColField colField = colFields.get(i);
			if (colField.isKey()) {
				continue;
			}
			if (DbUtil.isNumber(colField.getSqlType()) || DbUtil.isString(colField.getSqlType())) {
				String tpl = JS_STRING_FIELD_TPL;
				if (DbUtil.isInt(colField.getSqlType())) {// 整型
					tpl = JS_INT_FIELD_TPL;
				} else if (DbUtil.isFloat(colField.getSqlType())) {// 浮点型
					tpl = JS_FLOAT_FIELD_TPL;
				}
				builder.append(tpl.replace(FIELD_NAME_TPL, colField.getFieldName())).append(Context.COMMA);
			}
		}
		if (builder.length() > 0) {
			builder.setLength(builder.length() - 1);
		}
		placeHolders.put(JS_FIELDS_TPL, builder.toString());
	}

	/**
	 * 生成列
	 * 
	 * @author songkun
	 * @create 2017年5月12日 下午6:50:00
	 * @param tableClass
	 * @param placeHolders
	 * @return void
	 */
	private static final void generateColumns(TableClass tableClass, Map<String, String> placeHolders) {
		placeHolders.put(COLUMNS_PLACEHOLDER, "");
		List<ColField> colFields = tableClass.getColFields();
		if (colFields == null || colFields.size() <= 0) {
			return;
		}
		StringBuilder builder = new StringBuilder();
		ColField keyField = null;
		for (int i = 0; i < colFields.size(); i++) {
			ColField colField = colFields.get(i);
			if (colField.isKey()) {
				keyField = colField;
			}
			if (i < 5) {
				builder.append(COLUMN_TPL.replace(FIELD_NAME_TPL, colField.getFieldName()).replace(HIDDEN_TPL, "false")).append(Context.COMMA);
			} else {
				builder.append(COLUMN_TPL.replace(FIELD_NAME_TPL, colField.getFieldName()).replace(HIDDEN_TPL, "true")).append(Context.COMMA);
			}
		}
		if (keyField != null) {
			builder.append(COLUMN_OPERATE_TPL.replace(FIELD_NAME_TPL, keyField.getFieldName()));
		} else {
			builder.setLength(builder.length() - 1);
		}
		placeHolders.put(COLUMNS_PLACEHOLDER, builder.toString());
	}

	/**
	 * 生成查询控件
	 * 
	 * @author songkun
	 * @create 2017年5月12日 下午5:25:29
	 * @param tableClass
	 * @param placeHolders
	 * @return void
	 */
	private static final void generateCustomToolHtml(TableClass tableClass, Map<String, String> placeHolders) {
		placeHolders.put(CUSTOMTOOLHTML_PLACEHOLDER, "");
		placeHolders.put(POSTDATA_PLACEHOLDER, "");
		List<ColField> colFields = tableClass.getColFields();
		if (colFields == null || colFields.size() <= 0) {
			return;
		}
		String fieldName = null;
		for (ColField colField : tableClass.getColFields()) {
			if (NAME_FIELD.equalsIgnoreCase(colField.getFieldName().toLowerCase())) {
				fieldName = colField.getFieldName();
				break;
			}
		}
		if (fieldName == null) {
			for (ColField colField : tableClass.getColFields()) {
				if (colField.getFieldName().toLowerCase().indexOf(NAME_FIELD) >= 0) {
					fieldName = colField.getFieldName();
					break;
				}
			}
		}
		if (fieldName == null) {
			for (ColField colField : tableClass.getColFields()) {
				if (colField.getSqlType() == Types.CHAR) {
					fieldName = colField.getFieldName();
					break;
				}
			}
		}
		if (fieldName != null) {
			placeHolders.put(CUSTOMTOOLHTML_PLACEHOLDER, SEARCH_HTML);
			placeHolders.put(POSTDATA_PLACEHOLDER, POSTDATA_TPL.replace(FIELD_NAME_TPL, fieldName));
		}
	}
}
