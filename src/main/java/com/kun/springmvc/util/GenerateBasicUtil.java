/**
 * Program  : GenerateCore.java
 * Author   : songkun
 * Create   : 2017年4月25日 上午10:46:09
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

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.kun.springmvc.Config;
import com.kun.springmvc.Context;
import com.kun.springmvc.bean.TableClass;
import com.kun.springmvc.mybatis.Resources;

/**
 * 生成基础代码(Pagination/IMapper/IService/AbstractServiceImpl/BaseController/ServiceException/util工具类/web相关的一些代码)
 *
 * @author songkun
 * @version 1.0.0
 * @date 2017年4月25日 上午10:46:09
 */
public class GenerateBasicUtil {
	private GenerateBasicUtil() {
	}

	private static final String APP_FOLDER = "app";
	private static final String COMMON_FOLDER = "common";
	private static final String CORE_TPL_FOLDER = "tpl/core/";
	private static final String WEB_APP_TPL_FOLDER = "tpl/web/webapp";
	private static final String APPLICATION_CONTEXT_XML_NAME = "applicationContext.xml";
	private static final String DATA_SOURCE_PROPERTIES_NAME = "datasource.properties";
	private static final String POM_XML_NAME = "pom.xml";
	private static final String SPRING_MVC_XML_NAME = "springmvc.xml";
	private static final String LOG4J2_XML_NAME = "log4j2.xml";
	private static final String WEB_XML_NAME = "web.xml";
	private static final String ROOT_PACKAGE_PLACEHOLDER = "${rootPackage}";
	private static final String PROJECT_NAME_PLACEHOLDER = "${projectName}";
	private static final String PROJECT_PATH_PLACEHOLDER = "${projectPath}";
	private static final String DS_DRIVER_CLASSNAME_CLASSPATH_PLACEHOLDER = "${dsDriverClassName}";
	private static final String DS_URL_CLASSPATH_PLACEHOLDER = "${dsUrl}";
	private static final String DS_USERNAME_CLASSPATH_PLACEHOLDER = "${dsUserName}";
	private static final String DS_PASSWORD_CLASSPATH_PLACEHOLDER = "${dsPassword}";
	private static final String SERIAL_VERSION_UID_PLACEHOLDER = "${serialVersionUID}";
	private static final String INDEX_PAGE = "${indexPage}";

	private static final String MODELVARNAME_PLACEHOLDER = "${modelVarName}";
	private static final String ADD_NODES_CODE_PLACEHOLDER = "${addNodesCode}";
	private static final String MODELVARNAME_LOWERCASE_PLACEHOLDER = "${modelVarNameLowerCase}";
	private static final String ADD_NODE_CODE_TPL = "nodes.add(new LeftTreeNode(\"${modelVarName}\", \"../${modelVarNameLowerCase}/list.html\", 1, \"glyphicon glyphicon-question-sign\"));";

	private static final String PROJECT_ELEMENT_NAME = "project";
	private static final String MODELVERSION_ELEMENT_NAME = "modelVersion";
	private static final String GROUPID_ELEMENT_NAME = "groupId";
	private static final String ARTIFACTID_ELEMENT_NAME = "artifactId";
	private static final String PACKAGING_ELEMENT_NAME = "packaging";
	private static final String NAME_ELEMENT_NAME = "name";
	private static final String PROPERTIES_ELEMENT_NAME = "properties";
	private static final String DEPENDENCIES_ELEMENT_NAME = "dependencies";
	private static final String DEPENDENCY_ELEMENT_NAME = "dependency";
	private static final String BUILD_ELEMENT_NAME = "build";
	private static final String MYSQL_VERSION_ELEMENT_NAME = "mysql.version";
	private static final String MYBATIS_VERSION_ELEMENT_NAME = "mybatis.version";
	private static final String SPRING_VERSION_ELEMENT_NAME = "spring.version";
	private static final String CXF_VERSION_ELEMENT_NAME = "cxf.version";
	private static final String JACKSON_VERSION_ELEMENT_NAME = "jackson.version";

	/**
	 * 生成基础文件
	 * 
	 * @author songkun
	 * @create 2017年5月15日 下午3:45:32
	 * @param config
	 * @param tableClasses
	 * @return void
	 */
	public static final void generateBasic(Config config, List<TableClass> tableClasses) {
		copyCommonFolder(config);
		copyConfigFiles(config);
		if (config.isAddDependencyToPom()) {
			addDependencyToPom(config);
		}
		if (config.isGenerateWebXmlIfNotExist()) {
			copyWebXml(config);
		}
		copyAppFolder(config, tableClasses);
		copyWebAppBasic(config);
	}

	/**
	 * 拷贝webapp中的基础文件
	 * 
	 * @author songkun
	 * @create 2017年5月12日 下午4:28:56
	 * @param config
	 * @return void
	 */
	private static final void copyWebAppBasic(Config config) {
		try {
			File source = Resources.getResourceAsFile(WEB_APP_TPL_FOLDER);
			File dest = new File(config.getWebRootPath());
			FileUtil.doCopyFileOrFolder(source, dest, null, true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 处理登陆相关代码
	 * 
	 * @author songkun
	 * @create 2017年5月4日 下午5:01:20
	 * @param config
	 * @return void
	 */
	private static final void copyAppFolder(Config config, List<TableClass> tableClasses) {
		try {
			File dest = new File(FileUtil.toFile(config.getRootPackage(), config.getSrcRootPath()), APP_FOLDER);
			File source = new File(Resources.getResourceAsFile(CORE_TPL_FOLDER), APP_FOLDER);
			Map<String, String> placeHolders = new HashMap<>();
			placeHolders.put(ROOT_PACKAGE_PLACEHOLDER, config.getRootPackage());
			placeHolders.put(INDEX_PAGE, tableClasses.get(0).getClassName().toLowerCase() + "/list.html");
			placeHolders.put(SERIAL_VERSION_UID_PLACEHOLDER, (System.currentTimeMillis() / 10) + "");
			placeHolders.put(ADD_NODES_CODE_PLACEHOLDER, generateLeftTree(tableClasses));
			FileUtil.doCopyFileOrFolder(source, dest, placeHolders, true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 生成左侧树形结构的代码（生成的代码在项目里应该是从数据库中load，有权限相关的逻辑处理，而不是这样写死）
	 * 
	 * @author songkun
	 * @create 2017年5月15日 下午4:30:51
	 * @param tableClasses
	 * @return String
	 */
	private static final String generateLeftTree(List<TableClass> tableClasses) {
		if (tableClasses == null || tableClasses.size() <= 0) {
			return "";
		}
		StringBuilder builder = new StringBuilder();
		for (TableClass tableClass : tableClasses) {
			if (builder.length() > 0) {
				builder.append("			");
			}
			builder.append(ADD_NODE_CODE_TPL.replace(MODELVARNAME_PLACEHOLDER, CommonUtil.toVar(tableClass.getClassName(), null))
					.replace(MODELVARNAME_LOWERCASE_PLACEHOLDER, tableClass.getClassName().toLowerCase())).append(Context.NEW_LINE);
		}
		builder.setLength(builder.length() - 1);
		return builder.toString();
	}

	/**
	 * 拷贝web.xml到工程中
	 * 
	 * @author songkun
	 * @create 2017年4月26日 下午4:08:35
	 * @param config
	 * @return void
	 */
	private static final void copyWebXml(Config config) {
		try {
			if (config.getWebXmlPath() == null) {
				return;
			}
			File file = new File(config.getWebXmlPath(), WEB_XML_NAME);
			if (file.exists()) {
				return;
			}
			String content = FileUtil.readFileToString(Resources.getResourceAsFile(CORE_TPL_FOLDER + WEB_XML_NAME));
			if (content == null) {
				return;
			}
			content = content.replace(ROOT_PACKAGE_PLACEHOLDER, config.getRootPackage());
			FileUtil.writeStringToFile(file, content);
		} catch (Exception e) {
		}
	}

	/**
	 * 复制common目录下的java文件
	 * 
	 * @author songkun
	 * @create 2017年4月25日 下午2:01:17
	 * @param config
	 * @return void
	 */
	private static final void copyCommonFolder(Config config) {
		try {
			File dest = new File(FileUtil.toFile(config.getRootPackage(), config.getSrcRootPath()), COMMON_FOLDER);
			File source = new File(Resources.getResourceAsFile(CORE_TPL_FOLDER), COMMON_FOLDER);
			Map<String, String> placeHolders = new HashMap<>();
			placeHolders.put(ROOT_PACKAGE_PLACEHOLDER, config.getRootPackage());
			FileUtil.doCopyFileOrFolder(source, dest, placeHolders, true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 复制配置文件
	 * 
	 * @author songkun
	 * @create 2017年4月25日 下午2:30:44
	 * @param config
	 * @return void
	 */
	private static final void copyConfigFiles(Config config) {
		try {
			File sourceFolder = Resources.getResourceAsFile(CORE_TPL_FOLDER);
			File destFolder = new File(config.getResourceRootPath());
			Map<String, String> placeHolders = new HashMap<>();
			placeHolders.put(ROOT_PACKAGE_PLACEHOLDER, config.getRootPackage());
			// springmvc.xml
			FileUtil.doCopyFile(new File(sourceFolder, SPRING_MVC_XML_NAME), new File(destFolder, SPRING_MVC_XML_NAME), placeHolders);
			// applicationContext.xml
			FileUtil.doCopyFile(new File(sourceFolder, APPLICATION_CONTEXT_XML_NAME), new File(destFolder, APPLICATION_CONTEXT_XML_NAME),
					placeHolders);
			// log4j2.xml
			placeHolders.put(PROJECT_PATH_PLACEHOLDER, config.getProjectPath());
			placeHolders.put(PROJECT_NAME_PLACEHOLDER, config.getProjectName());
			FileUtil.doCopyFile(new File(sourceFolder, LOG4J2_XML_NAME), new File(destFolder, LOG4J2_XML_NAME), placeHolders);
			placeHolders.clear();
			placeHolders.put(DS_DRIVER_CLASSNAME_CLASSPATH_PLACEHOLDER, config.getDbDriver());
			placeHolders.put(DS_URL_CLASSPATH_PLACEHOLDER, config.getDbUrl());
			placeHolders.put(DS_USERNAME_CLASSPATH_PLACEHOLDER, config.getDbUser());
			placeHolders.put(DS_PASSWORD_CLASSPATH_PLACEHOLDER, config.getDbPswd());
			// datasource.properties
			FileUtil.doCopyFile(new File(sourceFolder, DATA_SOURCE_PROPERTIES_NAME), new File(destFolder, DATA_SOURCE_PROPERTIES_NAME), placeHolders);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 将dependency节点相关信息追加到目标工程的pom文件中
	 * 
	 * @author songkun
	 * @create 2017年4月26日 下午3:49:47
	 * @param config
	 * @return void
	 */
	private static final void addDependencyToPom(Config config) {
		try {
			File source = new File(Resources.getResourceAsFile(CORE_TPL_FOLDER), POM_XML_NAME);// pom.xml
			File dest = new File(config.getProjectPath(), POM_XML_NAME);
			Map<String, String> placeHolders = new HashMap<>();
			placeHolders.put(ROOT_PACKAGE_PLACEHOLDER, config.getRootPackage());
			placeHolders.put(PROJECT_NAME_PLACEHOLDER, config.getProjectName());
			if (dest.exists()) {// 如果存在，则判断是否需要追加appendency到原pom.xml
				doPomXml(source, dest, placeHolders);
			} else {// 如果不存在，则直接写过去
				FileUtil.doCopyFile(source, dest, placeHolders);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 处理pom.xml文件
	 * 
	 * @author songkun
	 * @create 2017年4月26日 下午3:49:18
	 * @param source
	 * @param dest
	 * @param placeHolders
	 * @return void
	 */
	private static final void doPomXml(File source, File dest, Map<String, String> placeHolders) {
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setNamespaceAware(true); // never forget this!
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document destDocument = builder.parse(dest);
			NodeList nodeList = destDocument.getElementsByTagName(PROJECT_ELEMENT_NAME);
			if (dest.length() <= 0 || nodeList == null || nodeList.getLength() <= 0) {
				dest.renameTo(new File(source.getParentFile(), source.getName() + "_" + CommonUtil.getDateTimeOnlyNumber()));// 重命名
				FileUtil.doCopyFile(source, dest, placeHolders);
			} else {
				String content = FileUtil.readFileToString(source);
				if (placeHolders != null) {
					for (Entry<String, String> entry : placeHolders.entrySet()) {
						content = content.replace(entry.getKey(), entry.getValue());
					}
				}
				Document sourceDocument = builder.parse(new ByteArrayInputStream(content.getBytes()));
				Node destProjectNode = nodeList.item(0);
				NodeList sourceNodeList = sourceDocument.getElementsByTagName(PROJECT_ELEMENT_NAME);
				if (sourceNodeList == null || sourceNodeList.getLength() <= 0) {
					return;
				}
				Node sourceProjectNode = sourceNodeList.item(0);
				doXmlNode(sourceProjectNode, destProjectNode, destDocument, MODELVERSION_ELEMENT_NAME);
				doXmlNode(sourceProjectNode, destProjectNode, destDocument, GROUPID_ELEMENT_NAME);
				doXmlNode(sourceProjectNode, destProjectNode, destDocument, ARTIFACTID_ELEMENT_NAME);
				doXmlNode(sourceProjectNode, destProjectNode, destDocument, PACKAGING_ELEMENT_NAME);
				doXmlNode(sourceProjectNode, destProjectNode, destDocument, NAME_ELEMENT_NAME);
				doPropertiesNode(sourceProjectNode, destProjectNode, destDocument);
				doDependency(sourceProjectNode, destProjectNode, destDocument);
				doBuild(sourceProjectNode, destProjectNode, destDocument);
				savePomXml(destDocument, dest);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 保存pom.xml
	 * 
	 * @author songkun
	 * @create 2017年4月26日 下午5:27:58
	 * @param document
	 * @param file
	 * @return void
	 */
	private static final void savePomXml(Document document, File file) {
		OutputStream outputStream = null;
		try {
			if (file.exists()) {
				file.renameTo(new File(file.getParentFile(), file.getName() + "_" + CommonUtil.getDateTimeOnlyNumber()));
			}
			TransformerFactory tFactory = TransformerFactory.newInstance();
			Transformer transformer = tFactory.newTransformer();
			DOMSource source = new DOMSource(document);
			outputStream = new FileOutputStream(file);
			StreamResult result = new StreamResult(outputStream);
			transformer.transform(source, result);
		} catch (Exception e) {
		} finally {
			try {
				outputStream.close();
			} catch (Exception e2) {
			}
		}
	}

	/**
	 * 处理build节点
	 * 
	 * @author songkun
	 * @create 2017年4月27日 上午11:01:41
	 * @param sourceProjectNode
	 * @param destProjectNode
	 * @param destDocument
	 * @return void
	 */
	private static final void doBuild(Node sourceProjectNode, Node destProjectNode, Document destDocument) {
		Node sourceBuild = getOneChildNode(sourceProjectNode, BUILD_ELEMENT_NAME);
		if (sourceBuild == null || !sourceBuild.hasChildNodes()) {
			return;
		}
		Node destBuild = getOneChildNode(destProjectNode, BUILD_ELEMENT_NAME);
		if (destBuild == null) {
			destProjectNode.appendChild(destDocument.importNode(sourceBuild, true));
		}
	}

	/**
	 * 处理Dependency节点
	 * 
	 * @author songkun
	 * @create 2017年4月26日 下午3:24:31
	 * @param sourceProjectNode
	 * @param destProjectNode
	 * @return void
	 */
	private static final void doDependency(Node sourceProjectNode, Node destProjectNode, Document destDocument) {
		Node sourceDependencies = getOneChildNode(sourceProjectNode, DEPENDENCIES_ELEMENT_NAME);
		if (sourceDependencies == null || !sourceDependencies.hasChildNodes()) {
			return;
		}
		Node destDependencies = getOneChildNode(destProjectNode, DEPENDENCIES_ELEMENT_NAME);
		NodeList destDepList = destDependencies == null ? null : destDependencies.getChildNodes();
		if (destDepList == null || destDepList.getLength() <= 0) {
			destProjectNode.appendChild(destDocument.importNode(sourceDependencies, true));
		} else {
			NodeList sourceDepList = sourceDependencies.getChildNodes();
			List<Node> resultNodes = new ArrayList<>();
			if (sourceDepList != null) {// 判断模板pom文件中的Dependency是否已经存在于目标pom中，如果不存在，则追加过去
				for (int i = 0; i < sourceDepList.getLength(); i++) {
					Node sourceTmp = sourceDepList.item(i);
					if (shouldCopyDependency(sourceTmp, destDepList)) {// 如果不存在于目标pom中
						resultNodes.add(sourceTmp);
					}
				}
			}
			for (Node node : resultNodes) {// 追加到目标文件pom中
				destDependencies.appendChild(destDocument.importNode(node, true));
			}
		}
	}

	/**
	 * 是否需要拷贝
	 * 
	 * @author songkun
	 * @create 2017年4月26日 下午3:22:09
	 * @param dependencyNode
	 * @param destDepList
	 * @return boolean
	 */
	private static final boolean shouldCopyDependency(Node dependencyNode, NodeList destDepList) {
		if (dependencyNode == null || !DEPENDENCY_ELEMENT_NAME.equalsIgnoreCase(dependencyNode.getNodeName()) || !dependencyNode.hasChildNodes()) {
			return false;// 如果dependency节点为空，则跳过
		}
		if (destDepList == null || destDepList.getLength() <= 0) {// 如果目标节点是空的，则，直接拷贝节点到目标节点的父节点内
			return true;
		}
		Node groupIdNode = getOneChildNode(dependencyNode, GROUPID_ELEMENT_NAME);
		if (groupIdNode == null) {// 没有groupId节点，跳过
			return false;
		}
		Node artifactIdNode = getOneChildNode(dependencyNode, ARTIFACTID_ELEMENT_NAME);
		if (artifactIdNode == null) {// 没有artifactId节点，跳过
			return false;
		}
		String groupId = groupIdNode.getTextContent();
		String artifactId = artifactIdNode.getTextContent();
		if (groupId == null || artifactId == null) {
			return false;// 如果dependency节点的groupId/artifactId有问题，则跳过
		}
		for (int i = 0; i < destDepList.getLength(); i++) {
			Node tmp = destDepList.item(i);
			if (!DEPENDENCY_ELEMENT_NAME.equalsIgnoreCase(tmp.getNodeName())) {
				continue;
			}
			groupIdNode = getOneChildNode(tmp, GROUPID_ELEMENT_NAME);
			if (groupIdNode == null) {// 没有groupId节点，跳过
				return false;
			}
			artifactIdNode = getOneChildNode(tmp, ARTIFACTID_ELEMENT_NAME);
			if (artifactIdNode == null) {// 没有artifactId节点，跳过
				return false;
			}
			if (groupId.equals(groupIdNode.getTextContent()) && artifactId.equals(artifactIdNode.getTextContent())) {
				return false;// 如果存在相同的，则不用处理
			}
		}
		return true;// 需要处理
	}

	/**
	 * 处理properties节点
	 * 
	 * @author songkun
	 * @create 2017年4月25日 下午7:22:30
	 * @param sourceProjectNode
	 * @param destProjectNode
	 *            project节点
	 * @return void
	 */
	private static final void doPropertiesNode(Node sourceProjectNode, Node destProjectNode, Document destDocument) {
		Node destPropertiesNode = getOneChildNode(destProjectNode, PROPERTIES_ELEMENT_NAME);
		if (destPropertiesNode == null) {
			destProjectNode.appendChild(destDocument.importNode(getOneChildNode(sourceProjectNode, PROPERTIES_ELEMENT_NAME), true));
		} else {
			doXmlNode(sourceProjectNode, destPropertiesNode, destDocument, MYSQL_VERSION_ELEMENT_NAME);
			doXmlNode(sourceProjectNode, destPropertiesNode, destDocument, MYBATIS_VERSION_ELEMENT_NAME);
			doXmlNode(sourceProjectNode, destPropertiesNode, destDocument, SPRING_VERSION_ELEMENT_NAME);
			doXmlNode(sourceProjectNode, destPropertiesNode, destDocument, CXF_VERSION_ELEMENT_NAME);
			doXmlNode(sourceProjectNode, destPropertiesNode, destDocument, JACKSON_VERSION_ELEMENT_NAME);
		}
	}

	/**
	 * 处理基础的几个节点
	 * 
	 * @author songkun
	 * @create 2017年4月25日 下午6:48:52
	 * @param sourceParentNode
	 * @param destParentNode
	 * @param tagName
	 * @return void
	 */
	private static final void doXmlNode(Node sourceParentNode, Node destParentNode, Document destDocument, String tagName) {
		Node destNode = getOneChildNode(destParentNode, tagName);
		if (destNode == null) {
			Node sourceNode = getOneChildNode(sourceParentNode, tagName);
			if (sourceNode != null) {
				destParentNode.appendChild(destDocument.importNode(sourceNode, true));
			}
		}
	}

	/**
	 * 根据元素名获取某节点下的子节点
	 * 
	 * @author songkun
	 * @create 2017年4月25日 下午7:20:19
	 * @param node
	 * @param tagName
	 * @return Node
	 */
	private static final Node getOneChildNode(Node node, String tagName) {
		if (node == null) {
			return null;
		}
		NodeList children = node.getChildNodes();
		if (children == null || children.getLength() <= 0) {
			return null;
		}
		for (int i = 0; i < children.getLength(); i++) {
			Node tmp = children.item(i);
			if (tmp != null && tmp.getNodeType() == Node.ELEMENT_NODE && tagName.equalsIgnoreCase(tmp.getNodeName())) {
				return tmp;
			}
		}
		return null;
	}
}
