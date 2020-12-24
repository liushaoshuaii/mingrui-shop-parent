package com.baidu.shop.service;

import com.baidu.shop.base.Result;
import com.baidu.shop.entity.CategoryEntity;
import com.google.gson.JsonObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Api(tags = "商品分类接口")
public interface CategoryService {
    @ApiOperation(value="通过查询商品分类")
    @GetMapping(value = "category/list")
    Result<List<CategoryEntity>> getCategoryByPid(Integer pid);

    @ApiOperation(value="通过id删除分类")
    @DeleteMapping(value="category/del")
    Result<JsonObject>delCategory(Integer id);

    @ApiOperation(value="修改分类")
    @PutMapping(value="category/update")
    Result<JsonObject>updateCategory(@RequestBody  CategoryEntity entity);
}
