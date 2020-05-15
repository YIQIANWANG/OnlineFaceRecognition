package com.chenframework.common.model.view;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * bootstrap-treeview数据节点模型
 */
@Getter
@Setter
@NoArgsConstructor
public class TreeBData {

    private String text;
    private String icon; // 列表树节点上的图标，通常是节点左边的图标。
    private String selectedIcon;// 当某个节点被选择后显示的图标，通常是节点左边的图标。
    private String href;// 结合全局enableLinks选项为列表树节点指定URL。
    private Boolean selectable = true;// 指定列表树的节点是否可选择。设置为false将使节点展开，并且不能被选择。
    private State state = new State();
    private String color = "#428BCA";
    private String backColor;
    private List<String> tags = new ArrayList<>();// 通过结合全局showTags选项来在列表树节点的右边添加额外的信息。

    private List<TreeBData> nodes = new ArrayList<>();

    public TreeBData addTas(String tag) {
        this.tags.add(tag);
        return this;
    }

    public TreeBData nullNodes() {
        this.nodes = null;
        return this;
    }

    public static class State {
        private boolean checked = false;
        private boolean disabled = false;
        private boolean expanded = false;
        private boolean selected = false;

        public boolean isChecked() {
            return checked;
        }

        public boolean isExpanded() {
            return expanded;
        }

        public boolean isSelected() {
            return selected;
        }

        public void setChecked(boolean checked) {
            this.checked = checked;
        }

        public boolean isDisabled() {
            return disabled;
        }

        public void setDisabled(boolean disabled) {
            this.disabled = disabled;
        }

        public void setExpanded(boolean expanded) {
            this.expanded = expanded;
        }

        public void setSelected(boolean selected) {
            this.selected = selected;
        }

    }

}
