package com.baidu.shop.dto;

import com.baidu.shop.base.BaseDTO;
import com.baidu.shop.group.MingruiOperation;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @ClassName SpecGroupDTO
 * @Description: TODO
 * @Author lss
 * @Date 2021/1/4
 * @Version V1.0
 **/
@ApiModel(value="规格组数据传说dto")
@Data
public class SpecGroupDTO extends BaseDTO {

    @ApiModelProperty(value="主键",example = "1")
    @NotNull(message = "主键不能为空",groups={MingruiOperation.Update.class})
    private Integer id;

    @ApiModelProperty(value="类型Id",example = "1")
    @NotNull(message = "类型id不能为空",groups = {MingruiOperation.Add.class})
    private Integer cid;

    @ApiModelProperty(value = "规格组名")
    @NotNull(message = "规格组名不能为空",groups={MingruiOperation.Add.class})
    private String name;
}
