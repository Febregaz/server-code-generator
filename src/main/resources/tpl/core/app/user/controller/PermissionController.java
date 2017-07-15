/**
 * Program  : PermissionController.java
 * Author   : songkun
 *
 */

package ${rootPackage}.app.user.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import ${rootPackage}.common.bean.LeftTreeNode;
import ${rootPackage}.common.web.controller.BaseController;

/**
 * 权限控制器
 * 
 * @author songkun
 * @version 1.0.0
 * @date 2016年9月27日 下午7:29:51
 */
@Controller("permissionController")
@RequestMapping(value = "/permission", produces = "application/json;charset=UTF-8")
public class PermissionController extends BaseController<Object> {

	// @Resource(name = "permissionService")
	// private IPermissionService permissionService;

	/**
	 * 获取绑定到用户的权限树
	 * 
	 * @author songkun
	 */
	@RequestMapping("/listLeft.do")
	@ResponseBody
	public List<LeftTreeNode> listByUserForLeftTree() {
		try {
			// User user = this.getCurrentUser();
			// if (user == null) {
			// return null;
			// }
			// if (user.getType() == Context.USER_TYPE_OPERATER) {
			// return toTrees(PermissionCache.listPermissions());
			// }
			// return toTrees(this.permissionService.listByUser(user.getId()));

			// 你应该将此代码改成从数据库中load
			List<LeftTreeNode> nodes = new ArrayList<>();
			${addNodesCode}
			return nodes;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}


//	/**
//	 * 组装页面左边的树形结构(注意：支持最深2层，如果支持多层需要编写递归处理父节点)
//	 * 
//	 * @author songkun
//	 * @create 2016年10月8日 下午1:51:51
//	 * @param permissions
//	 * @return List<LeftTreeNode>
//	 */
//	private List<LeftTreeNode> toTrees(List<Permission> permissions) {
//		if (permissions == null || permissions.size() <= 0) {
//			return null;
//		}
//		Map<Long, LeftTreeNode> tmpCache = new HashMap<>();// 缓存一下，便于获取
//		List<LeftTreeNode> result = new ArrayList<>();// 只存放第一级节点，二级节点放在node的children中
//		for (Permission permission : permissions) {
//			if (tmpCache.containsKey(permission.getId())) {// 如果已经存在了，则跳过
//				continue;// 这里不考虑，系统启动后，手动修改数据库，导致数据不一致的情况，假设数据库权限表是死的
//			}
//			if (permission.getParentId() != null && permission.getParentId() > 0) {// 如果父节点不为空，则存在父节点，直接放到父节点的children列表里
//				LeftTreeNode parentNode = tmpCache.get(permission.getParentId());
//				if (parentNode == null) {// 只处理两层父子节点，系统暂时没有大于两层的页面导览树形结构，如果处理多层，可以用递归调用
//					Permission pp = PermissionCache.getByKey(permission.getParentId());
//					if (pp != null) {// 如果父节点存在，则处理，如果父节点不存在，则，数据有问题，跳过
//						LeftTreeNode pNode = LeftTreeNode.getInstance(pp);
//						result.add(pNode);
//						tmpCache.put(pp.getId(), pNode);
//						// 将自己放入父节点的children列表中
//						pNode.addToChildNodes(LeftTreeNode.getInstance(permission));
//					}
//				} else {// 直接放入父节点的children中
//					parentNode.addToChildNodes(LeftTreeNode.getInstance(permission));
//				}
//			} else {// 如果是第一级节点，则放入列表中，同时缓存。另：二级节点不需要缓存，因为，系统最深就是二级，不会被重复使用
//				LeftTreeNode node = LeftTreeNode.getInstance(permission);
//				tmpCache.put(permission.getId(), node);
//				result.add(node);
//			}
//		}
//		return result;
//	}
}
