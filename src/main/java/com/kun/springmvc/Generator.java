/**
 * Program  : Generator.java
 * Author   : songkun
 * Create   : 2016年12月30日 下午10:02:45
 *
 * Copyright 2016 songkun. All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of songkun.  
 * You shall not disclose such Confidential Information and shall 
 * use it only in accordance with the terms of the license agreement 
 * you entered into with songkun.
 *
 */

package com.kun.springmvc;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.util.List;

import com.kun.springmvc.bean.TableClass;
import com.kun.springmvc.util.DbUtil;
import com.kun.springmvc.util.GenerateBasicUtil;
import com.kun.springmvc.util.GenerateJavaAppCodeUtil;
import com.kun.springmvc.util.GenerateWebUtil;
import com.kun.springmvc.util.GenerateXmlUtil;

/**
 * 代码生成器，生成xml/pojo/mapper/service/controller
 *
 * @author songkun
 * @version 1.0.0
 * @date 2016年12月30日 下午10:02:45
 */
public class Generator {

	public static void main(String[] args) throws Exception {
		// if (args == null || args.length <= 0) {
		// System.out.println(" arguments is empty, please set the config's
		// path(config.xml).");
		// return;
		// }
		Config config = Config.getInstance("/Users/songkun/Documents/workspace/code-generator/src/main/resources/config.xml");
		// Config config =
		// Config.getInstance("D://workspace/code-generator/src/main/resources/config.xml");
		if (config == null) {
			System.out.println("loading config fail....");
			return;
		}
		Connection connection = DbUtil.openConnection(config);
		if (connection == null) {
			System.out.println("open connection fail...");
			return;
		}
		DatabaseMetaData metaData = connection.getMetaData();
		List<TableClass> table2Classes = DbUtil.listAllTables(metaData, config);
		GenerateBasicUtil.generateBasic(config, table2Classes);
		if (table2Classes != null && table2Classes.size() > 0) {
			processTables(table2Classes, config, metaData);
		}
		DbUtil.closeConnection(connection);
	}

	/**
	 * 处理所有表
	 * 
	 * @author songkun
	 * @create 2017年4月17日 下午7:39:32
	 * @param table2Classes
	 * @param config
	 * @return void
	 */
	private static final void processTables(List<TableClass> table2Classes, Config config, DatabaseMetaData metaData) {
		if (table2Classes == null || table2Classes.size() <= 0 || config == null) {
			return;
		}
		for (TableClass tableClass : table2Classes) {
			DbUtil.loadAllCols(tableClass, metaData, config);
			GenerateXmlUtil.generateXml(tableClass, config.getResourceRootPath(), config.isOverWrite());
			GenerateJavaAppCodeUtil.generateJavaFiles(tableClass, config.getAuthor(), config.getSrcRootPath(), config.getRootPackage());
			GenerateWebUtil.generateWebApp(config, tableClass);
		}
	}

}
