/**
 * Program  : Config.java
 * Author   : songkun
 * Create   : 2017年1月1日 下午3:05:41
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

import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.kun.springmvc.bean.TableClass;
import com.kun.springmvc.mybatis.Resources;

/**
 * 配置信息
 *
 * @author songkun
 * @version 1.0.0
 * @date 2017年1月1日 下午3:05:41
 */
public class Config {

	private static final String NAME_SEPERATOR = "_";// 数据库中命名分隔符
	private static final String CONFIG_SEPERATOR = ",";// 配置文件中一个属性对应多个值时的分隔符

	private String author = "songkun";// 作者名
	private String dbDriver = "com.mysql.jdbc.Driver";// 数据库驱动类
	private String dbUrl;// 数据库url
	private String dbUser;// 数据库用户名
	private String dbPswd;// 数据库密码
	private String projectPath;// 工程的绝对路径
	private String srcRootDir = "src/main/java";// 代码根目录
	private String resourceRootDir = "src/main/resources";// 资源根目录(mybatis的xml文件放在此目录下的mybatis文件夹中)
	private boolean overWrite = false;// 是否覆盖已经生成的实体类
	private String rootPackage = "";// 代码存放的package
	private String[] includeTable = null;// 需要处理的数据库表
	private String[] excludeTable = null;// 不处理的数据库表,优先级低于includeTable
	private String[] tablePrefix = {"tb" + NAME_SEPERATOR};// 数据库表前缀
	private String tableNameSeparator = NAME_SEPERATOR;// 表名分隔符
	private String colNameSeparator = NAME_SEPERATOR;// 列名分隔符
	private Map<String, TableClass> table2Entity = new HashMap<>();// 特定数据库对应实体的命名
	private boolean generateWebXmlIfNotExist = true;// 是否生成web.xml
	private boolean addDependencyToPom = true;// 是否追加dependency到pom.xml
	private String webRoot = "src/main/webapp";// webapp中html/js/css/images...存放的根目录
	private String webXmlDir = "src/main/webapp/WEB-INF";// web.xml存放的路径

	public String getWebRootPath() {
		if (this.projectPath == null || webXmlDir == null) {
			return webRoot;
		}
		String webRootPath = this.projectPath;
		if (!webRootPath.endsWith("/")) {
			webRootPath = webRootPath + "/";
		}
		return webRootPath + webRoot;
	}
	public String getWebXmlPath() {
		if (this.projectPath == null || webXmlDir == null) {
			return webXmlDir;
		}
		String webXmlPath = this.projectPath;
		if (!webXmlPath.endsWith("/")) {
			webXmlPath = webXmlPath + "/";
		}
		return webXmlPath + webXmlDir;
	}
	public boolean isGenerateWebXmlIfNotExist() {
		return generateWebXmlIfNotExist;
	}
	public boolean isAddDependencyToPom() {
		return addDependencyToPom;
	}
	public String getProjectPath() {
		return projectPath;
	}
	public String getProjectName() {
		if (projectPath == null) {
			return "";
		}
		String tmp = projectPath;
		if (tmp.endsWith("/")) {
			tmp = tmp.substring(0, tmp.length() - 1);
		}
		if (tmp.endsWith("\\")) {
			tmp = tmp.substring(0, tmp.length() - 1);
		}
		int lastIndex = tmp.lastIndexOf("/");
		if (lastIndex < 0) {
			lastIndex = tmp.lastIndexOf('\\');
		}
		if (lastIndex < 0) {
			return tmp;
		}
		return tmp.substring(lastIndex + 1);
	}
	public String getAuthor() {
		return author;
	}
	public String getSrcRootPath() {
		if (this.projectPath == null) {
			return srcRootDir;
		}
		String srcRootPath = this.projectPath;
		if (!srcRootPath.endsWith("/")) {
			srcRootPath = srcRootPath + "/";
		}
		return srcRootPath + srcRootDir;
	}
	public String getResourceRootPath() {
		if (this.projectPath == null) {
			return resourceRootDir;
		}
		String resourceRootPath = this.projectPath;
		if (!resourceRootPath.endsWith("/")) {
			resourceRootPath = resourceRootPath + "/";
		}
		return resourceRootPath + resourceRootDir;
	}
	public String getDbDriver() {
		return dbDriver;
	}
	public String getTableNameSeparator() {
		return tableNameSeparator;
	}
	public String getColNameSeparator() {
		return colNameSeparator;
	}
	public String getRootPackage() {
		return rootPackage;
	}
	public String getDbUrl() {
		return dbUrl;
	}
	public String getDbUser() {
		return dbUser;
	}
	public String getDbPswd() {
		return dbPswd;
	}
	public boolean isOverWrite() {
		return overWrite;
	}
	public String[] getIncludeTable() {
		return includeTable;
	}
	public String[] getExcludeTable() {
		return excludeTable;
	}
	public String[] getTablePrefix() {
		return tablePrefix;
	}

	private void setIncludeTable(String includeTable) {
		if (includeTable == null || includeTable.length() <= 0 || "*".equals(includeTable)) {
			return;
		}
		String[] splits = includeTable.split(CONFIG_SEPERATOR);
		this.includeTable = splits;
	}
	private void setExcludeTable(String excludeTable) {
		if (excludeTable == null || excludeTable.length() <= 0) {
			return;
		}
		this.excludeTable = excludeTable.split(CONFIG_SEPERATOR);
	}
	private void setTablePrefix(String prefix) {
		if (prefix == null || prefix.length() <= 0) {
			return;
		}
		this.tablePrefix = prefix.split(CONFIG_SEPERATOR);
	}
	private void addTable2Entity(String table, TableClass entity) {
		if (table == null || table.length() <= 0 || entity == null) {
			return;
		}
		this.table2Entity.put(table, entity);
	}
	/**
	 * 是否需要跳过处理
	 * 
	 * @author songkun
	 * @create 2017年4月24日 上午9:42:21
	 * @param table
	 * @return boolean true=跳过，false=处理
	 */
	public boolean isSkip(String table) {
		if (table == null) {
			return true;
		}
		if (excludeTable != null && excludeTable.length > 0) {
			for (String tmp : excludeTable) {
				if (table.equalsIgnoreCase(tmp)) {
					return true;// 不需要处理，跳过
				}
			}
		}
		if (includeTable != null && includeTable.length > 0) {
			for (String tmp : includeTable) {
				if (table.equalsIgnoreCase(tmp)) {
					return false;// 如果匹配，则需要处理
				}
			}
			return true;// 如果配置了includeTable，但是，没匹配上，则不处理
		}
		return false;// 需要处理
	}

	/**
	 * 获取表相关配置信息
	 * 
	 * @author songkun
	 * @create 2017年4月18日 上午9:52:48
	 * @param tableName
	 * @return Table2Class
	 */
	public TableClass getTableConfig(String tableName) {
		return this.table2Entity.get(tableName);
	}

	/**
	 * 随便获取一个表信息（主要是登陆成功后，需要进入某个页面，而这个页面，如果在config.xml配置，有点麻烦了，所以，先搞一个占位，如果需要修改，可以在生成的代码上修改）
	 * 
	 * @author songkun
	 * @create 2017年5月4日 下午5:22:22
	 * @return TableClass
	 */
	// public TableClass getOneTable() {
	// if (this.table2Entity == null) {
	// return null;
	// }
	// return this.table2Entity.values().iterator().next();
	// }

	/**
	 * 解析配置文件，获取实例对象
	 * 
	 * @author songkun
	 * @create 2017年4月17日 下午5:20:34
	 * @param path
	 * @throws Exception
	 * @return Config
	 */
	public static final Config getInstance(String path) throws Exception {
		// TODO Auto-generated method stub
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true); // never forget this!
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document doc = builder.parse(Resources.getResourceAsFile(path));
		NodeList nodeList = doc.getElementsByTagName("config");
		if (nodeList == null) {
			System.out.println("no <config> element...");
			return null;
		}
		Node configElement = nodeList.item(0);
		NodeList children = configElement.getChildNodes();
		if (children == null || children.getLength() <= 0) {
			System.out.println("format error ...");
			return null;
		}
		Config config = new Config();
		for (int i = 0; i < children.getLength(); i++) {
			parseNode(children.item(i), config);
		}
		if (!validate(config)) {
			System.out.println("parse config fail(dbUrl/dbUser/dbPswd is empty)....");
			return null;
		}
		System.out.println("parse config success....");
		return config;
	}

	/**
	 * 验证数据是否合法
	 * 
	 * @author songkun
	 * @create 2017年4月17日 下午5:25:28
	 * @param config
	 * @return
	 * @return boolean
	 */
	private static final boolean validate(Config config) {
		if (config == null) {
			return false;
		}
		if (config.getDbDriver() == null || config.getDbUrl() == null || config.getDbUser() == null || config.getDbPswd() == null) {
			return false;
		}
		if (config.getDbDriver().length() <= 0 || config.getDbUrl().length() <= 0 || config.getDbUser().length() <= 0
				|| config.getDbPswd().length() <= 0) {
			return false;
		}
		return true;
	}

	/**
	 * 解析节点
	 * 
	 * @author songkun
	 * @create 2017年4月17日 下午5:17:24
	 * @param node
	 * @param config
	 * @return void
	 */
	private static final void parseNode(Node node, Config config) {
		if (node == null || config == null) {
			return;
		}
		switch (node.getNodeName()) {
			case "author" :
				config.author = node.getTextContent();
				break;
			case "dbDriver" :
				config.dbDriver = node.getTextContent();
				break;
			case "dbUrl" :
				config.dbUrl = node.getTextContent();
				break;
			case "dbUser" :
				config.dbUser = node.getTextContent();
				break;
			case "dbPswd" :
				config.dbPswd = node.getTextContent();
				break;
			case "projectPath" :
				config.projectPath = node.getTextContent();
				break;
			// case "srcRootDir" :
			// config.srcRootDir = node.getTextContent();
			// break;
			// case "resourceRootDir" :
			// config.resourceRootDir = node.getTextContent();
			// break;
			// case "webRoot" :
			// config.webRoot = node.getTextContent();
			// break;
			// case "overWrite" :
			// config.overWrite = "true".equals(node.getTextContent());
			// break;
			case "rootPackage" :
				config.rootPackage = node.getTextContent();
				break;
			case "includeTable" :
				config.setIncludeTable(node.getTextContent());
				break;
			case "excludeTable" :
				config.setExcludeTable(node.getTextContent());
				break;
			case "tablePrefix" :
				config.setTablePrefix(node.getTextContent());
				break;
			case "tableNameSeparator" :
				config.tableNameSeparator = node.getTextContent();
				break;
			case "colNameSeparator" :
				config.colNameSeparator = node.getTextContent();
				break;
			// case "generateWebXmlIfNotExist" :
			// config.generateWebXmlIfNotExist = "true".equals(node.getTextContent());
			// break;
			// case "addDependencyToPom" :
			// config.addDependencyToPom = "true".equals(node.getTextContent());
			// break;
			// case "webXmlDir" :
			// config.webXmlDir = node.getTextContent();
			// break;
			case "table" :
				TableClass table2Class = TableClass.getInstance(node);
				if (table2Class != null) {
					config.addTable2Entity(table2Class.getTableName(), table2Class);
				}
				break;
			default :
				break;
		}
	}

}
