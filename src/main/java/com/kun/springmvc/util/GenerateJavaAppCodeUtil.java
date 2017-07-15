/**
 * Program  : GenerateJavaUtil.java
 * Author   : songkun
 * Create   : 2017年4月21日 下午2:51:11
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

import java.util.HashSet;
import java.util.Set;

import com.kun.springmvc.Context;
import com.kun.springmvc.bean.ColField;
import com.kun.springmvc.bean.TableClass;
import com.kun.springmvc.mybatis.Resources;

/**
 * 生成java文件
 *
 * @author songkun
 * @version 1.0.0
 * @date 2017年4月21日 下午2:51:11
 */
public class GenerateJavaAppCodeUtil {
	private GenerateJavaAppCodeUtil() {
	}
	private static final String JAVA_FILE_SUFFIX = ".java";
	private static long SERIAL_VERSION_UID = System.currentTimeMillis();

	// JAVA文件命名的模板
	private static final String MODEL_JAVA_FILE_NAME_TPL = Context.MODEL_CLASS_NAME_TPL + JAVA_FILE_SUFFIX;
	private static final String DATA_JAVA_FILE_NAME_TPL = Context.DATA_CLASS_NAME_TPL + JAVA_FILE_SUFFIX;
	private static final String ISERVICE_JAVA_FILE_NAME_TPL = Context.ISERVICE_CLASS_NAME_TPL + JAVA_FILE_SUFFIX;
	private static final String SERVICE_IMPL_JAVA_FILE_NAME_TPL = Context.SERVICE_IMPL_CLASS_NAME_TPL + JAVA_FILE_SUFFIX;
	private static final String CONTROLLER_JAVA_FILE_NAME_TPL = Context.CONTROLLER_CLASS_NAME_TPL + JAVA_FILE_SUFFIX;
	// 模板文件
	private static final String MODEL_TPL_FILE_NAME = "tpl/model.tpl";
	private static final String DATA_TPL_FILE_NAME = "tpl/data.tpl";
	private static final String ISERVICE_TPL_FILE_NAME = "tpl/iservice.tpl";
	private static final String SERVICEIMPL_TPL_FILE_NAME = "tpl/serviceimpl.tpl";
	private static final String CONTROLLER_TPL_FILE_NAME = "tpl/controller.tpl";
	private static String MODEL_TPL_CONTENT = null;
	private static String DATA_TPL_CONTENT = null;
	private static String ISERVICE_TPL_CONTENT = null;
	private static String SERVICEIMPL_TPL_CONTENT = null;
	private static String CONTROLLER_TPL_CONTENT = null;

	private static final String AUTHOR_PLACEHOLDER = "${author}";
	private static final String YEAR_PLACEHOLDER = "${year}";
	private static final String DATETIME_PLACEHOLDER = "${dateTime}";
	private static final String PACKAGE_PLACEHOLDER = "${package}";
	private static final String ROOT_PACKAGE_PLACEHOLDER = "${rootPackage}";
	private static final String COMMENT_PLACEHOLDER = "${comment}";
	private static final String IMPORT_PLACEHOLDER = "${import}";
	private static final String FIELDS_PLACEHOLDER = "${fields}";
	private static final String METHODS_PLACEHOLDER = "${methods}";
	private static final String MODELVARNAME_PLACEHOLDER = "${modelVarName}";
	private static final String SERIAL_VERSION_UID_PLACEHOLDER = "${serialVersionUID}";

	static {
		try {// 初始化模板文件
			MODEL_TPL_CONTENT = FileUtil.readFileToString(Resources.getResourceAsFile(MODEL_TPL_FILE_NAME));
			DATA_TPL_CONTENT = FileUtil.readFileToString(Resources.getResourceAsFile(DATA_TPL_FILE_NAME));
			ISERVICE_TPL_CONTENT = FileUtil.readFileToString(Resources.getResourceAsFile(ISERVICE_TPL_FILE_NAME));
			SERVICEIMPL_TPL_CONTENT = FileUtil.readFileToString(Resources.getResourceAsFile(SERVICEIMPL_TPL_FILE_NAME));
			CONTROLLER_TPL_CONTENT = FileUtil.readFileToString(Resources.getResourceAsFile(CONTROLLER_TPL_FILE_NAME));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static final void generateJavaFiles(TableClass tableClass, String author, String srcRootDir, String rootPackage) {
		generateModelFile(tableClass, author, srcRootDir, rootPackage);
		generateDataFile(tableClass, author, srcRootDir, rootPackage);
		generateIServiceFile(tableClass, author, srcRootDir, rootPackage);
		generateServiceImplFile(tableClass, author, srcRootDir, rootPackage);
		generateControllerFile(tableClass, author, srcRootDir, rootPackage);
	}

	/**
	 * 生成controller代码
	 * 
	 * @author songkun
	 * @create 2017年4月23日 上午10:46:32
	 * @param comment
	 * @param modelClassName
	 * @param targetPackage
	 * @param config
	 * @return void
	 */
	private static final void generateControllerFile(TableClass tableClass, String author, String srcRootDir, String rootPackage) {
		if (CONTROLLER_TPL_CONTENT == null) {
			return;
		}
		// import: 接口、模型、mapper
		StringBuilder importBuilder = new StringBuilder();
		importBuilder.append(CommonUtil.getImportCode(CommonUtil.getModelClassFullName(tableClass.getClassName(), tableClass.getTargetPackage())));
		importBuilder.append(CommonUtil.getImportCode(CommonUtil.getIServiceClassFullName(tableClass.getClassName(), tableClass.getTargetPackage())));
		String controllerPackage = CommonUtil.getControllerPackage(tableClass.getTargetPackage());
		String content = CONTROLLER_TPL_CONTENT.replace(Context.MODEL_NAME_PLACEHOLDER, tableClass.getClassName()).replace(AUTHOR_PLACEHOLDER, author)
				.replace(YEAR_PLACEHOLDER, CommonUtil.getYear()).replace(DATETIME_PLACEHOLDER, CommonUtil.getDateTime())
				.replace(PACKAGE_PLACEHOLDER, controllerPackage).replace(COMMENT_PLACEHOLDER, tableClass.getComment() + "(controller)")
				.replace(IMPORT_PLACEHOLDER, importBuilder.toString())
				.replace(MODELVARNAME_PLACEHOLDER, CommonUtil.toVar(tableClass.getClassName(), null)).replace(ROOT_PACKAGE_PLACEHOLDER, rootPackage);
		FileUtil.writeStringToFile(
				FileUtil.initJavaFile(CONTROLLER_JAVA_FILE_NAME_TPL.replace(Context.MODEL_NAME_PLACEHOLDER, tableClass.getClassName()),
						controllerPackage, srcRootDir, false),
				content);
	}

	/**
	 * 生成业务实现类
	 * 
	 * @author songkun
	 * @create 2017年4月23日 上午10:18:34
	 * @param comment
	 * @param modelClassName
	 * @param targetPackage
	 * @param config
	 * @return void
	 */
	private static final void generateServiceImplFile(TableClass tableClass, String author, String srcRootDir, String rootPackage) {
		if (SERVICEIMPL_TPL_CONTENT == null) {
			return;
		}
		// import: 接口、模型、mapper
		StringBuilder importBuilder = new StringBuilder();
		importBuilder.append(CommonUtil.getImportCode(CommonUtil.getModelClassFullName(tableClass.getClassName(), tableClass.getTargetPackage())));// import..model
		importBuilder.append(CommonUtil.getImportCode(CommonUtil.getDataClassFullName(tableClass.getClassName(), tableClass.getTargetPackage())));// import..data(dao)
		importBuilder.append(CommonUtil.getImportCode(CommonUtil.getIServiceClassFullName(tableClass.getClassName(), tableClass.getTargetPackage())));// import..service..interface
		String serviceImplPackage = CommonUtil.getServiceImplPackage(tableClass.getTargetPackage());
		String content = SERVICEIMPL_TPL_CONTENT.replace(Context.MODEL_NAME_PLACEHOLDER, tableClass.getClassName())
				.replace(AUTHOR_PLACEHOLDER, author).replace(YEAR_PLACEHOLDER, CommonUtil.getYear())
				.replace(DATETIME_PLACEHOLDER, CommonUtil.getDateTime()).replace(PACKAGE_PLACEHOLDER, serviceImplPackage)
				.replace(COMMENT_PLACEHOLDER, tableClass.getComment() + "(业务实现类)").replace(IMPORT_PLACEHOLDER, importBuilder.toString())
				.replace(MODELVARNAME_PLACEHOLDER, CommonUtil.toVar(tableClass.getClassName(), null)).replace(ROOT_PACKAGE_PLACEHOLDER, rootPackage);
		FileUtil.writeStringToFile(
				FileUtil.initJavaFile(SERVICE_IMPL_JAVA_FILE_NAME_TPL.replace(Context.MODEL_NAME_PLACEHOLDER, tableClass.getClassName()),
						serviceImplPackage, srcRootDir, false),
				content);
	}

	/**
	 * 生成iservice接口文件
	 * 
	 * @author songkun
	 * @create 2017年4月22日 下午11:04:30
	 * @param tableClass
	 * @param author
	 * @param srcRootDir
	 * @return void
	 */
	private static final void generateIServiceFile(TableClass tableClass, String author, String srcRootDir, String rootPackage) {
		if (ISERVICE_TPL_CONTENT == null) {
			return;
		}
		String iservicePackage = CommonUtil.getIServicePackage(tableClass.getTargetPackage());
		String content = ISERVICE_TPL_CONTENT.replace(Context.MODEL_NAME_PLACEHOLDER, tableClass.getClassName()).replace(AUTHOR_PLACEHOLDER, author)
				.replace(YEAR_PLACEHOLDER, CommonUtil.getYear()).replace(DATETIME_PLACEHOLDER, CommonUtil.getDateTime())
				.replace(PACKAGE_PLACEHOLDER, iservicePackage).replace(COMMENT_PLACEHOLDER, tableClass.getComment() + "(业务接口)")
				.replace(ROOT_PACKAGE_PLACEHOLDER, rootPackage).replace(IMPORT_PLACEHOLDER,
						CommonUtil.getImportCode(CommonUtil.getModelClassFullName(tableClass.getClassName(), tableClass.getTargetPackage())));
		FileUtil.writeStringToFile(
				FileUtil.initJavaFile(ISERVICE_JAVA_FILE_NAME_TPL.replace(Context.MODEL_NAME_PLACEHOLDER, tableClass.getClassName()), iservicePackage,
						srcRootDir, false),
				content);
	}

	/**
	 * 生成dao代码
	 * 
	 * @author songkun
	 * @create 2017年4月22日 下午10:52:35
	 * @param comment
	 * @param modelClassName
	 * @param targetPackage
	 * @param config
	 * @return void
	 */
	private static final void generateDataFile(TableClass tableClass, String author, String srcRootDir, String rootPackage) {
		if (DATA_TPL_CONTENT == null) {
			return;
		}
		String dataPackage = CommonUtil.getDataPackage(tableClass.getTargetPackage());
		String content = DATA_TPL_CONTENT.replace(Context.MODEL_NAME_PLACEHOLDER, tableClass.getClassName()).replace(AUTHOR_PLACEHOLDER, author)
				.replace(YEAR_PLACEHOLDER, CommonUtil.getYear()).replace(DATETIME_PLACEHOLDER, CommonUtil.getDateTime())
				.replace(PACKAGE_PLACEHOLDER, dataPackage).replace(COMMENT_PLACEHOLDER, tableClass.getComment() + "(dao)")
				.replace(ROOT_PACKAGE_PLACEHOLDER, rootPackage).replace(IMPORT_PLACEHOLDER,
						CommonUtil.getImportCode(CommonUtil.getModelClassFullName(tableClass.getClassName(), tableClass.getTargetPackage())));
		FileUtil.writeStringToFile(FileUtil.initJavaFile(DATA_JAVA_FILE_NAME_TPL.replace(Context.MODEL_NAME_PLACEHOLDER, tableClass.getClassName()),
				dataPackage, srcRootDir, false), content);
	}

	/**
	 * 生成model代码
	 * 
	 * @author songkun
	 * @create 2017年4月21日 下午4:36:11
	 * @param modelClassName
	 * @param targetPackage
	 * @param cols
	 * @return void
	 */
	private static final void generateModelFile(TableClass tableClass, String author, String srcRootDir, String rootPackage) {
		if (MODEL_TPL_CONTENT == null || tableClass == null || tableClass.getColFields() == null || tableClass.getColFields().size() <= 0) {
			return;
		}
		StringBuilder importBuilder = new StringBuilder();
		StringBuilder fieldsBuilder = new StringBuilder();
		StringBuilder methodsBuilder = new StringBuilder();
		Set<String> importCache = new HashSet<>();
		for (ColField col : tableClass.getColFields()) {
			if (!importCache.contains(col.getJavaTypeName())) {// 防止多次引入同样的类
				String type = col.getJavaTypeName();
				if (type.indexOf('.') > 0) {// 如果存在【.】,则说明，不是普通的java.lang.类型
					importBuilder.append(CommonUtil.getImportCode(type));
					type = type.substring(type.lastIndexOf('.'));
				}
				importCache.add(col.getJavaTypeName());
			}
			fieldsBuilder.append(generateField(col, fieldsBuilder.length() > 0));
			methodsBuilder.append(generateGetterSetter(col, methodsBuilder.length() > 0));
		}
		SERIAL_VERSION_UID = SERIAL_VERSION_UID + 1;
		String modelPackage = CommonUtil.getModelPackage(tableClass.getTargetPackage());
		String content = MODEL_TPL_CONTENT.replace(Context.MODEL_NAME_PLACEHOLDER, tableClass.getClassName()).replace(AUTHOR_PLACEHOLDER, author)
				.replace(YEAR_PLACEHOLDER, CommonUtil.getYear()).replace(DATETIME_PLACEHOLDER, CommonUtil.getDateTime())
				.replace(PACKAGE_PLACEHOLDER, modelPackage).replace(COMMENT_PLACEHOLDER, tableClass.getComment() + "(model)")
				.replace(SERIAL_VERSION_UID_PLACEHOLDER, SERIAL_VERSION_UID + "").replace(IMPORT_PLACEHOLDER, importBuilder.toString())
				.replace(FIELDS_PLACEHOLDER, fieldsBuilder.toString()).replace(METHODS_PLACEHOLDER, methodsBuilder.toString())
				.replace(ROOT_PACKAGE_PLACEHOLDER, rootPackage);
		FileUtil.writeStringToFile(FileUtil.initJavaFile(MODEL_JAVA_FILE_NAME_TPL.replace(Context.MODEL_NAME_PLACEHOLDER, tableClass.getClassName()),
				modelPackage, srcRootDir, false), content);
	}

	/**
	 * 生成getter/setter
	 * 
	 * @author songkun
	 * @create 2017年4月21日 下午6:21:59
	 * @param colField
	 * @return String
	 */
	private static final String generateGetterSetter(ColField colField, boolean appendTabFirst) {
		StringBuilder getterSetterBuilder = new StringBuilder();
		if (appendTabFirst) {
			getterSetterBuilder.append(Context.TAB);
		}
		String typeName = colField.getJavaTypeName();
		if (colField.getJavaTypeName().lastIndexOf('.') >= 0) {
			typeName = colField.getJavaTypeName().substring(colField.getJavaTypeName().lastIndexOf('.') + 1);
		}
		getterSetterBuilder.append("public ").append(typeName).append(" ").append(CommonUtil.toGetMethod(colField.getFieldName())).append("() {")
				.append(Context.NEW_LINE);
		getterSetterBuilder.append(Context.TAB).append(Context.TAB).append("return this.").append(colField.getFieldName()).append(Context.SEMICOLON)
				.append(Context.NEW_LINE);
		getterSetterBuilder.append(Context.TAB).append("}").append(Context.NEW_LINE);
		getterSetterBuilder.append(Context.TAB).append("public void ").append(CommonUtil.toSetMethod(colField.getFieldName())).append("(")
				.append(typeName).append(" ").append(colField.getFieldName()).append(") {").append(Context.NEW_LINE);
		getterSetterBuilder.append(Context.TAB).append(Context.TAB).append("this.").append(colField.getFieldName()).append(" = ")
				.append(colField.getFieldName()).append(Context.SEMICOLON).append(Context.NEW_LINE);
		getterSetterBuilder.append(Context.TAB).append("}").append(Context.NEW_LINE);
		return getterSetterBuilder.toString();
	}

	/**
	 * 生成属性定义语句,eg:private String fieldName;
	 * 
	 * @author songkun
	 * @create 2017年4月21日 下午6:09:59
	 * @param colField
	 * @return String
	 */
	private static final String generateField(ColField colField, boolean appendTabFirst) {
		StringBuilder fieldsBuilder = new StringBuilder();
		if (appendTabFirst) {// 如果是第一行，则不用tab
			fieldsBuilder.append(Context.TAB);
		}
		if (colField.getRemark() != null && colField.getRemark().length() > 0) {
			fieldsBuilder.append("/* ").append(colField.getRemark().replace("/*", "").replace("*/", "")).append(" */").append(Context.NEW_LINE)
					.append(Context.TAB);
		}
		String typeName = colField.getJavaTypeName();
		if (colField.getJavaTypeName().lastIndexOf('.') >= 0) {
			typeName = colField.getJavaTypeName().substring(colField.getJavaTypeName().lastIndexOf('.') + 1);
		}
		fieldsBuilder.append("private ").append(typeName).append(" ").append(colField.getFieldName()).append(Context.SEMICOLON);
		fieldsBuilder.append(Context.NEW_LINE);
		return fieldsBuilder.toString();
	}

}
