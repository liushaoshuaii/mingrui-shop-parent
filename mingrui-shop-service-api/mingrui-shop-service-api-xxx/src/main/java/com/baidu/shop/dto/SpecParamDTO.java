package com.baidu.shop.dto;

import com.baidu.shop.group.MingruiOperation;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;

import javax.persistence.Id;
import javax.validation.constraints.NotNull;

/**
 * @ClassName SpecParamDTO
 * @Description: TODO
 * @Author lss
 * @Date 2021/1/4
 * @Version V1.0
 **/
@ApiOperation(value="规格参数数据传输Dto")
@Data
public class SpecParamDTO {
    @ApiModelProperty(value="主键",example = "1")
    @NotNull(message = "主键不能为空",groups = {MingruiOperation.Update.class})
    private Integer id;
    @ApiModelProperty(value="分类id",example = "1")
    @NotNull(message = "商品分类id不能为空",groups = {MingruiOperation.Update.class})
    private Integer cid;
    @ApiModelProperty(value="规格组id",example = "1")
    @NotNull(message = "品牌id不能为空",groups = {MingruiOperation.Update.class})
    private Integer groupId;
    @ApiModelProperty(value="规格参数名称")
    @NotNull(message = "规格参数名称不能为空",groups = {MingruiOperation.Add.class})
    private String name;
    @ApiModelProperty(value="是否是数字类型参数 1->true或0->false",example = "0")
    @NotNull(message = "是否是数值类型的参数 ",groups = {MingruiOperation.Add.class,MingruiOperation.Update.class})
    private Boolean numeric;
    @ApiModelProperty(value="数字类型参数的单位，非数字类型可以为空")
    private String unit;
    @ApiModelProperty(value = "是否是sku通用属性，1->true或0->false", example = "0")
    @NotNull(message = "是否用于搜索过滤不能为空",groups = {MingruiOperation.Add.class,MingruiOperation.Update.class})
    private Boolean generic;
    @ApiModelProperty(value = "是否用于搜索过滤，1->true或0->false", example = "0")
    @NotNull(message = "是否用于搜索过滤不能为空",groups = {MingruiOperation.Add.class,MingruiOperation.Update.class})
    private Boolean searching;
    @ApiModelProperty(value = "数值类型参数，如果需要搜索，则添加分段间隔值，如CPU频率间 隔：0.5-1.0")
    private String segments;
}
