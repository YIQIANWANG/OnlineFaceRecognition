package com.chenframework.common.service;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.chenframework.common.model.SortParams;
import com.chenframework.common.model.TreeModel;
import com.chenframework.common.model.view.TreeBData;
import com.chenframework.common.persistence.entity.BaseTreeEntity;
import com.chenframework.common.persistence.repository.BaseRepository;
import com.chenframework.common.utils.clazz.BeanUtil;
import com.chenframework.common.utils.clazz.CglibBean;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 树形结构的实体基础处理业务实现
 */
@Transactional(readOnly = true)
public abstract class BaseTreeService<E extends BaseTreeEntity<E>, D extends BaseRepository<E>> extends BaseService<E, D> {

    /**
     * 将数据构建成树形模型
     */
    public List<TreeModel<E>> buildTree() {
        return buildTree(findAll());
    }

    /**
     * 将数据构建成树形模型，数据列表中必须有顶级的节点（也就是parent=null）
     */
    public List<TreeModel<E>> buildTree(List<E> datas) {
        datas.sort(new BaseTreeEntity.DefaultTreeEntityComparator<>()); // 排序,保证parent为空的在最前面
        List<TreeModel<E>> roots = new ArrayList<>(); // 数据库中parent_id为空的对应的Tree模型集合
        for (int i = 0; i < datas.size(); ) {
            E e = datas.get(0);
            if (e.getParent() == null) {
                roots.add(new TreeModel<>(e));
                datas.remove(0);
            } else {
                break;
            }
        }

        for (TreeModel<E> root : roots) {
            traverse(root, datas);
        }
        return roots;
    }

    /**
     * 构造treegrid表格数据
     */
    public List<Object> findTreegridData() {
        List<E> list = findAll();
        List<Object> result = new ArrayList<>();

        List<Field> fields = BeanUtil.getDeclaredFields(getEntityClass());
        Map<String, Class<?>> propertyMap = new HashMap<>();
        fields.forEach(field -> {
            String name = field.getName();
            if (!name.equals(BaseTreeEntity.PARENT) && !name.equals(BaseTreeEntity.PARENTIDS)) {
                boolean isJsonIgnore = field.isAnnotationPresent(JsonIgnore.class);
                if (!isJsonIgnore) {
                    propertyMap.put(name, field.getType());
                }
            }
        });

        String parentId = "parentId";
        propertyMap.put(parentId, String.class);

        list.forEach(entity -> {
            CglibBean bean = new CglibBean(propertyMap);
            propertyMap.keySet().forEach(field -> {
                if (field.equals(parentId)) {
                    String parentIdValue = entity.findParentId();
                    bean.setValue(field, parentIdValue == null ? "" : parentIdValue);
                } else {
                    bean.setValue(field, BeanUtil.getFieldValue(entity, field));
                }
            });
            result.add(bean.getObject());
        });

        return result;
    }

    /**
     * 构造treeview树形结构数据
     */
    public List<TreeBData> findBTreeData() {
        List<TreeModel<E>> treeModel = buildTree();
        return new ArrayList<>(traverse(treeModel));
    }

    private List<TreeBData> traverse(List<TreeModel<E>> treeModel) {
        List<TreeBData> list = new ArrayList<>();

        treeModel.forEach(model -> {
            E bean = model.getBean();

            TreeBData node = new TreeBData();
            node.setText(bean.getName());
            node.setIcon(bean.getIcon());
            node.getTags().add(bean.getId());
            node.getState().setExpanded(true);
            List<TreeBData> childrenNodes = traverse(model.getChildren());
            if (childrenNodes.size() > 0) {
                node.getNodes().addAll(childrenNodes);
            } else {
                node.setNodes(null);
            }

            list.add(node);
        });
        return list;
    }

    /*
     * (non-Javadoc)
     * @see cn.leesoft.chenframework.common.service.BaseService#findAll()
     */
    @Override
    public List<E> findAll() {
        SortParams sortParams = new SortParams(BaseTreeEntity.SORT);
        return super.findAll(sortParams);
    }

    /*
     * (non-Javadoc)
     * @see cn.leesoft.chenframework.common.service.BaseService#save(cn.leesoft.chenframework.common.persistence.entity.IdEntity)
     */
    @Override
    @Transactional
    public E save(E entity) {
        return super.save(resetEntityParentIds(entity));
    }

    /*
     * (non-Javadoc)
     * @see cn.leesoft.chenframework.common.service.BaseService#update(cn.leesoft.chenframework.common.persistence.entity.IdEntity)
     */
    @Override
    @Transactional
    public E update(E entity) {
        return super.update(resetEntityParentIds(entity));
    }

    /*
     * 添加获取修改树形结构实体,重置其父节点数据
     */
    private E resetEntityParentIds(E entity) {
        if (entity.getParent() == null || entity.findParentId() == null) {
            entity.setParentIds("0,");
        } else {
            E parent = super.findById(entity.findParentId());
            entity.setParent(parent);
            String parentIds = parent.getParentIds() + entity.getParent().getId() + ",";
            entity.setParentIds(parentIds);
        }
        return entity;
    }

    private void traverse(TreeModel<E> tree, List<E> datas) {
        String id = tree.getBean().getId();
        for (int i = 0; i < datas.size(); i++) {
            E e = datas.get(i);
            if (e.getParent().getId().equals(id)) {
                TreeModel<E> newTree = new TreeModel<>(e);
                tree.getChildren().add(newTree);
                traverse(newTree, datas);
            }
        }
    }

}
