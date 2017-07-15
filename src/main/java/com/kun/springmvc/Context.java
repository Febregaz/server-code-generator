/**
 * Program  : Context.java
 * Author   : songkun
 * Create   : 2017年4月21日 下午2:51:54
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

package com.kun.springmvc;

/**
 * 全局变量存放的地方
 *
 * @author songkun
 * @version 1.0.0
 * @date 2017年4月21日 下午2:51:54
 */
public class Context {

	public static final String APP_PACKAGE_NAME = "app";// 所有的业务代码都放在app包下
	public static final String MODEL_FOLDER_NAME = "model";// 模型的文件夹名(包名)
	public static final String CONTROLLER_FOLDER_NAME = "controller";// 控制层的文件夹名(包名)
	public static final String SERVICE_FOLDER_NAME = "service";// 业务层的文件夹名(包名)
	public static final String IMPL_FOLDER_NAME = "impl";// 业务层具体实现的文件夹名(包名)
	public static final String DATA_FOLDER_NAME = "data";// 数据层的文件夹名(包名)
	public static final String DOT = ".";
	public static final String NEW_LINE = System.lineSeparator();
	public static final String TAB = "\t";
	public static final String COMMA = ",";
	public static final String SEMICOLON = ";";

	public static final String MODEL_NAME_PLACEHOLDER = "${modelName}";
	// Java类名模板
	public static final String MODEL_CLASS_NAME_TPL = MODEL_NAME_PLACEHOLDER;
	public static final String DATA_CLASS_NAME_TPL = MODEL_NAME_PLACEHOLDER + "Mapper";
	public static final String ISERVICE_CLASS_NAME_TPL = "I" + MODEL_NAME_PLACEHOLDER + "Service";
	public static final String SERVICE_IMPL_CLASS_NAME_TPL = MODEL_NAME_PLACEHOLDER + "ServiceImpl";
	public static final String CONTROLLER_CLASS_NAME_TPL = MODEL_NAME_PLACEHOLDER + "Controller";

}
