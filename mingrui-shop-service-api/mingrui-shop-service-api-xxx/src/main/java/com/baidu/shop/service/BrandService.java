package com.baidu.shop.service;

import com.alibaba.fastjson.JSONObject;
import com.baidu.shop.base.Result;
import com.baidu.shop.dto.BrandDTO;
import com.baidu.shop.entity.BrandEntity;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Api(tags="品牌接口 ")
public interface BrandService {
    @GetMapping(value="brand/list")
    @ApiOperation(value = "查询品牌列表")
    Result<PageInfo<BrandEntity>>getBrandInfo(BrandDTO brandDTO);

    @PostMapping(value="brand/save")
    @ApiOperation(value="新增品牌")
    Result<JSONObject>saveBrand(@RequestBody BrandDTO brandDTO);
}
