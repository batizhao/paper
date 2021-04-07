/*
 *
 * 此类来自 https://gitee.com/geek_qi/cloud-platform/blob/master/ace-common/src/main/java/com/github/wxiaoqi/security/common/util/TreeUtil.java
 * @ Apache-2.0
 */

package io.github.batizhao.util;

import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ace
 * @author lengleng
 * @date 2020-02-09
 */
@UtilityClass
public class TreeUtil {

	/**
	 * 两层循环实现建树
	 * @param treeNodes 传入的树节点列表
	 * @return
	 */
	public <T extends TreeNode> List<T> build(List<T> treeNodes, Object root) {

		List<T> trees = new ArrayList<>();

		for (T treeNode : treeNodes) {

			if (root.equals(treeNode.getPid())) {
				trees.add(treeNode);
			}

			for (T it : treeNodes) {
				if (it.getPid().equals(treeNode.getId())) {
					if (treeNode.getChildren() == null) {
						treeNode.setChildren(new ArrayList<>());
					}
					treeNode.add(it);
					treeNode.setIsLeaf(false);
				}
			}
		}
		return trees;
	}

	/**
	 * 使用递归方法建树
	 * @param treeNodes
	 * @return
	 */
	public <T extends TreeNode> List<T> buildByRecursive(List<T> treeNodes, Object root) {
		List<T> trees = new ArrayList<T>();
		for (T treeNode : treeNodes) {
			if (root.equals(treeNode.getPid())) {
				trees.add(findChildren(treeNode, treeNodes));
			}
		}
		return trees;
	}

	/**
	 * 递归查找子节点
	 * @param treeNodes
	 * @return
	 */
	public <T extends TreeNode> T findChildren(T treeNode, List<T> treeNodes) {
		for (T it : treeNodes) {
			if (treeNode.getId() == it.getPid()) {
				if (treeNode.getChildren() == null) {
					treeNode.setChildren(new ArrayList<>());
				}
				treeNode.add(findChildren(it, treeNodes));
			}
		}
		return treeNode;
	}

	/**
	 * 创建树形节点
	 * @param menus
	 * @param root
	 * @return
	 */
//	public List<MenuVO> buildTree(List<MenuVO> menus, int root) {
//		List<MenuVO> trees = new ArrayList<>();
//		MenuVO node;
//		for (MenuVO menu : menus) {
//			node = new MenuVO();
//			node.setId(menu.getId());
//			node.setPid(menu.getPid());
//			node.setName(menu.getName());
//			node.setPath(menu.getPath());
//			node.setPermission(menu.getPermission());
//			node.setIcon(menu.getIcon());
//			node.setType(menu.getType());
//			node.setSort(menu.getSort());
//			trees.add(node);
//		}
//		return TreeUtil.build(trees, root);
//	}

}
