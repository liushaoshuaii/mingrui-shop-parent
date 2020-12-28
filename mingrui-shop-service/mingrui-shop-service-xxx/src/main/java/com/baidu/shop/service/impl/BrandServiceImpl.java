package com.baidu.shop.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baidu.shop.base.BaseApiService;
import com.baidu.shop.base.Result;
import com.baidu.shop.dto.BrandDTO;
import com.baidu.shop.entity.BrandEntity;
import com.baidu.shop.entity.CategoryBrandEntity;
import com.baidu.shop.mapper.BrandMapper;
import com.baidu.shop.mapper.CategoryBrandMapper;
import com.baidu.shop.service.BrandService;
import com.baidu.shop.utils.BaiduBeanUtil;
import com.baidu.shop.utils.ObjectUtil;
import com.baidu.shop.utils.PinyinUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RestController;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName BrandServiceImpl
 * @Description: TODO
 * @Author lss
 * @Date 2020/12/25
 * @Version V1.0
 **/
@RestController
public class BrandServiceImpl extends BaseApiService implements BrandService {

    @Autowired
    private BrandMapper brandMapper;

    @Autowired
    private CategoryBrandMapper categoryBrandMapper;


    @Override
    public Result<PageInfo<BrandEntity>> getBrandInfo(BrandDTO brandDTO) {
        PageHelper.startPage(brandDTO.getPage(),brandDTO.getRows());

        if(!StringUtils.isEmpty(brandDTO.getSort())) PageHelper.orderBy(brandDTO.getOrderBy());

        BrandEntity brandEntity = BaiduBeanUtil.copyProperties(brandDTO,BrandEntity.class);

        Example example = new Example(BrandEntity.class);
        if (ObjectUtil.isNotNull(brandEntity.getName())){
            example.createCriteria().andLike("name","%"+brandEntity.getName()+"%");
        }


        List<BrandEntity>brandEntities=brandMapper.selectByExample(example);

        PageInfo<BrandEntity>pageInfo = new PageInfo<>(brandEntities);

        return this.setResultSuccess(pageInfo);
    }
    @Transactional
    @Override
    public Result<JSONObject> saveBrand(BrandDTO brandDTO) {
        BrandEntity brandEntity = BaiduBeanUtil.copyProperties(brandDTO, BrandEntity.class);
        brandEntity.setLetter(PinyinUtil.getUpperCase(String.valueOf(brandEntity.getName().toCharArray()[0]),false).toCharArray()[0]);
        brandMapper.insertSelective(brandEntity);

        String categories = brandDTO.getCategories();
        if(StringUtils.isEmpty(brandDTO.getCategories())) return this.setResultError("");
        List<CategoryBrandEntity> categoryBrandEntities  = new ArrayList<>();

        if(categories.contains(",")){
            String[] categoryArr  = categories.split(",");

            /*for (String s : categoryArr){
                CategoryBrandEntity categoryBrandEntity = new CategoryBrandEntity();
                categoryBrandEntity.setBrandId(brandEntity.getId());
                categoryBrandEntity.setCategoryId(Integer.valueOf(s));
                categoryBrandEntities.add(categoryBrandEntity);
            }
                categoryBrandMapper.insertList(categoryBrandEntities);*/
            categoryBrandMapper.insertList(Arrays.asList(categoryArr).stream().map(categoryIdStr -> {
                CategoryBrandEntity categoryBrandEntity = new CategoryBrandEntity();
                categoryBrandEntity.setBrandId(brandEntity.getId());
                categoryBrandEntity.setCategoryId(Integer.valueOf(categoryIdStr));
                return categoryBrandEntity;
            }).collect(Collectors.toList()));

        }else{
            CategoryBrandEntity categoryBrandEntity = new CategoryBrandEntity();
            categoryBrandEntity.setBrandId(brandEntity.getId());
            categoryBrandEntity.setCategoryId(Integer.valueOf(categories));
            categoryBrandMapper.insert(categoryBrandEntity);
        }
        return this.setResultSuccess();
    }
}
