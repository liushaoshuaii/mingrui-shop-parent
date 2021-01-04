package com.baidu.shop.service;

import com.baidu.shop.base.Result;
import com.baidu.shop.dto.SpecGroupDTO;
import com.baidu.shop.entity.SpecGroupEntity;
import com.google.gson.JsonObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "规格接口")
public interface SpecificationService {
    @ApiOperation(value="通过条件查询规格组")
    @GetMapping(value="specgroup/getSpecGroupInfo")
    Result<List<SpecGroupEntity>>getSpecGroupInfo(SpecGroupDTO specGroupDTO);

    @ApiOperation(value="新增规格组")
    @PostMapping(value="specgroup/save")
    Result<JsonObject>saveSpecGroup(@RequestBody SpecGroupDTO specGroupDTO);

    @ApiOperation(value="修改规格组0")
    @PutMapping(value="specgroup/save")
    Result<JsonObject>editSpecGroup(@RequestBody SpecGroupDTO specGroupDTO);

    @ApiOperation(value="删除规格组")
    @DeleteMapping(value="specgroup/delete")
    Result<JsonObject>deleteSpecGroup( Integer id);
}
