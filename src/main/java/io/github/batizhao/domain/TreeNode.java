/*
 *
 * 此类来自 https://gitee.com/geek_qi/cloud-platform/blob/master/ace-common/src/main/java/com/github/wxiaoqi/security/common/vo/TreeNode.java
 * @ Apache-2.0
 */

package io.github.batizhao.domain;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.Min;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ace
 * @author lengleng
 * @date 2017年11月9日23:33:45
 */
public class TreeNode {

    @ApiModelProperty(value = "ID", example = "100")
    protected Integer id;

    @ApiModelProperty(value = "父ID", example = "100")
    @Min(0)
    protected Integer pid;

    protected transient List<TreeNode> children = new ArrayList<>();

    protected transient boolean isLeaf = true;

    public void add(TreeNode node) {
        children.add(node);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public List<TreeNode> getChildren() {
        return children;
    }

    public void setChildren(List<TreeNode> children) {
        this.children = children;
    }

    public boolean getIsLeaf() {
        return isLeaf;
    }

    public void setIsLeaf(boolean leaf) {
        isLeaf = leaf;
    }
}
