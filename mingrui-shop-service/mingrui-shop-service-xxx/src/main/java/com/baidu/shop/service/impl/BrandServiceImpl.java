package com.baidu.shop.service.impl;

import com.baidu.shop.base.BaseApiService;
import com.baidu.shop.base.Result;
import com.baidu.shop.dto.BrandDTO;
import com.baidu.shop.entity.BrandEntity;
import com.baidu.shop.mapper.BrandMapper;
import com.baidu.shop.service.BrandService;
import com.baidu.shop.utils.BaiduBeanUtil;
import com.baidu.shop.utils.ObjectUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.StringUtil;

import java.util.List;

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

    @Override
    public Result<PageInfo<BrandEntity>> getBrandInfo(BrandDTO brandDTO) {
        PageHelper.startPage(brandDTO.getPage(),brandDTO.getRows());

        BrandEntity brandEntity = BaiduBeanUtil.copyProperties(brandDTO,BrandEntity.class);

        Example example = new Example(BrandEntity.class);
        if (ObjectUtil.isNotNull(brandEntity.getName())){
            example.createCriteria().andLike("name","%"+brandEntity.getName()+"%");
        }


        List<BrandEntity>brandEntities=brandMapper.selectByExample(example);

        PageInfo<BrandEntity>pageInfo = new PageInfo<>(brandEntities);

        return this.setResultSuccess(pageInfo);
    }
}
