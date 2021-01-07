package com.baidu.shop.service;

import com.alibaba.fastjson.JSONObject;
import com.baidu.shop.base.Result;
import com.baidu.shop.dto.SkuDTO;
import com.baidu.shop.dto.SpuDTO;
import com.baidu.shop.dto.SpuDetailDTO;
import com.baidu.shop.entity.SpuEntity;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Api(tags = "商品接口")
public interface GoodsService {
    @ApiOperation(value="获取spu信息")
    @GetMapping(value="goods/getSpuInfo")
    Result<PageInfo<SpuEntity>>getSpuInfo(SpuDTO spuDTO);

    @ApiOperation(value="商新新增")
    @PostMapping(value="goods/save")
    Result<JSONObject>saveGoods(@RequestBody SpuDTO spuDTO);

    @ApiOperation(value="获取spu信息")
    @GetMapping(value="goods/getSpuDetailByIdSpu")
    Result<SpuDetailDTO>getSpuDetailByIdSpu(Integer spuId);

    @ApiOperation(value="获取sku信息")
    @GetMapping(value="goods/getSkuBySpuId")
    Result<List<SkuDTO>>getSkuBySpuId(Integer spuId);
}
