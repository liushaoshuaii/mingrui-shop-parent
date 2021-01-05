package com.baidu.shop.service.impl;

import com.baidu.shop.base.BaseApiService;
import com.baidu.shop.base.Result;
import com.baidu.shop.dto.SpuDTO;
import com.baidu.shop.entity.SpuEntity;
import com.baidu.shop.mapper.SpuMapper;
import com.baidu.shop.service.GoodsService;
import com.baidu.shop.utils.ObjectUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RestController;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @ClassName SpuServiceImpl
 * @Description: TODO
 * @Author lss
 * @Date 2021/1/5
 * @Version V1.0
 **/
@RestController
public class SpuServiceImpl extends BaseApiService implements GoodsService {
    @Autowired
    private SpuMapper spuMapper;

    @Override
    public Result<PageInfo<SpuEntity>> getSpuInfo(SpuDTO spuDTO) {
        if (ObjectUtil.isNotNull(spuDTO.getPage())&&ObjectUtil.isNotNull(spuDTO.getRows()))//如果当前页数也每页条数不为空的话
            PageHelper.startPage(spuDTO.getPage(),spuDTO.getRows());//获取当前页和每页条数
        Example example = new Example(SpuEntity.class);
        Example.Criteria criteria = example.createCriteria();
        if(ObjectUtil.isNotNull(spuDTO.getSaleable())&&spuDTO.getSaleable()<2){//判断上架状态不为空同时小于2,那就只能是1和0
            criteria.andEqualTo("saleable",spuDTO.getSaleable());
        }
        if(!StringUtils.isEmpty(spuDTO.getTitle()))//判断标题不为空 进行模糊匹配条件查询
            criteria.andLike("title","%"+spuDTO.getTitle()+"%");
        if(!StringUtils.isEmpty((spuDTO.getSort())))
            example.setOrderByClause(spuDTO.getOrderBy());
        List<SpuEntity> spuEntities = spuMapper.selectByExample(example);
        PageInfo<SpuEntity> spuEntityPageInfo = new PageInfo<>(spuEntities);
        return this.setResultSuccess(spuEntityPageInfo);
    }
}
