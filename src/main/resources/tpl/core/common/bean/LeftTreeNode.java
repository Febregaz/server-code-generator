/**
 * Program  : LeftTreeNode.java
 * Author   : songkun
 *
 */

package ${rootPackage}.common.bean;

import java.util.ArrayList;
import java.util.List;


/**
 * 权限的树形结构
 *
 * @author songkun
 * @version 1.0.0
 * @date 2016年10月8日 上午11:10:25
 */
public class LeftTreeNode {

	private String name;// 节点名称
	private String path;// 节点对应的路径
	private Integer leaf;// 1=叶子节点，0=非叶子节点
	private String css;// css样式
	private List<LeftTreeNode> children;// 子节点列表

	public String getCss() {
		return css;
	}
	public void setCss(String css) {
		this.css = css;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public Integer getLeaf() {
		return leaf;
	}
	public void setLeaf(Integer leaf) {
		this.leaf = leaf;
	}
	public List<LeftTreeNode> getChildren() {
		return children;
	}
	public void setChildren(List<LeftTreeNode> children) {
		this.children = children;
	}

	/**
	 * 添加子节点
	 * 
	 * @author songkun
	 * @param childNode
	 * @return void
	 * @date 2011-6-3 下午04:29:08
	 * @since 2.0.0
	 */
	public void addToChildNodes(LeftTreeNode childNode) {
		if (childNode == null) {
			return;
		}
		if (this.children == null) {
			this.children = new ArrayList<LeftTreeNode>();
		}
		this.children.add(childNode);
	}

	/**
	 * 构造树形权限节点
	 * 
	 * @author songkun
	 */
//	public static final LeftTreeNode getInstance(Permission permission) {
//		if (permission == null) {
//			return null;
//		}
//		LeftTreeNode node = new LeftTreeNode();
//		node.setName(permission.getName());
//		node.setPath(permission.getPath());
//		node.setLeaf(permission.getLeaf());
//		node.setCss(permission.getCss());
//		return node;
//	}
	
	public LeftTreeNode(){}
	public LeftTreeNode(String name, String path, Integer leaf, String css){
		this.name = name;
		this.path = path;
		this.leaf = leaf;
		this.css = css;
	}

}
