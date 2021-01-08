package com.baidu.shop.service.impl;

import com.baidu.shop.base.BaseApiService;
import com.baidu.shop.base.Result;
import com.baidu.shop.entity.CategoryBrandEntity;
import com.baidu.shop.entity.CategoryEntity;
import com.baidu.shop.mapper.CategoryBrandMapper;
import com.baidu.shop.mapper.CategoryMapper;
import com.baidu.shop.service.CategoryService;
import com.baidu.shop.utils.ObjectUtil;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * @ClassName CategoryServiceImpl
 * @Description: TODO
 * @Author lss
 * @Date 2020/12/22
 * @Version V1.0
 **/
@RestController
public class CategoryServiceImpl extends BaseApiService implements CategoryService {
    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private CategoryBrandMapper categoryBrandMapper;
    @Override
    public Result<List<CategoryEntity>> getByBrand(Integer brandId) {
        List<CategoryEntity> categoryByBrandId = categoryMapper.getCategoryByBrandId(brandId);
        return this.setResultSuccess(categoryByBrandId);
    }

    @Transactional
    @Override
    public Result<JsonObject> updateCategory(CategoryEntity entity) {
        categoryMapper.updateByPrimaryKeySelective(entity);
        return this.setResultSuccess();
    }
    @Transactional
    @Override
    public Result<JsonObject> saveCategory(CategoryEntity categoryEntity) {
        CategoryEntity saveCategoryEntity = new CategoryEntity();
        saveCategoryEntity.setId(categoryEntity.getParentId());
        saveCategoryEntity.setIsParent(1);
        categoryMapper.updateByPrimaryKeySelective(saveCategoryEntity);
        categoryMapper.insertSelective(categoryEntity);
        return this.setResultSuccess();
    }

    @Override
    public Result<List<CategoryEntity>> getCategoryByPid(Integer pid) {
        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setParentId(pid);
        List<CategoryEntity>list = categoryMapper.select(categoryEntity);
        return this.setResultSuccess(list);
    }
    @Transactional
    @Override
    public Result<JsonObject> delCategory(Integer id) {
        //检验id是否合法
        if(ObjectUtil.isNull(id) || id<=0) return this.setResultError("id不合法");
        CategoryEntity categoryEntity = categoryMapper.selectByPrimaryKey(id);
        //判断id是否存在
        if(ObjectUtil.isNull(categoryEntity))return this.setResultError("数据不存在");
        //判断当前节点是否为父节点(isParent为1)
        if(categoryEntity.getIsParent()==1)return this.setResultError("当前节点为父节点");

        Example example1 = new Example(CategoryBrandEntity.class);
        example1.createCriteria().andEqualTo("categoryId",id);
        List<CategoryBrandEntity> categoryBrandEntities = categoryBrandMapper.selectByExample(example1);
        if(categoryBrandEntities.size() != 0){
            return this.setResultError("该属性已被品牌绑定不能删除");
        }


        //通过当前节点的父节点id查询当前节点的父节点下是否还有其他子节点;
        Example example = new Example(CategoryEntity.class);
        //前边格式固定 拼接sql语句   where 1 = 1; select form 表明 where 1=1 and parentId=?
        example.createCriteria().andEqualTo("parentId", categoryEntity.getParentId());
        //将拼接后的sql返回到list集合内
        List<CategoryEntity> categoryEntities = categoryMapper.selectByExample(example);



        //如果size<=1,如果当前节点被删除的话,当前节点父节点下就没有其他节点,将父节点的状态改为0
        if(categoryEntities.size()<=1){
            CategoryEntity updateCategoryEntity = new CategoryEntity();
            updateCategoryEntity.setIsParent(0);
            updateCategoryEntity.setId(categoryEntity.getParentId());

            categoryMapper.updateByPrimaryKeySelective(updateCategoryEntity);

        }
        //通过id删除节点
        categoryMapper.deleteByPrimaryKey(id);
        return this.setResultSuccess();
    }


}
