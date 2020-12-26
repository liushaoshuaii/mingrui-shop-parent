package com.baidu.shop.base;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName BaseBTO
 * @Description: TODO
 * @Author lss
 * @Date 2020/12/25
 * @Version V1.0
 **/
@Data
@ApiModel(value = "用于传输数据.需要其他bto继承此类")
public class BaseDTO {
    @ApiModelProperty(value="当前页",example = "1")
    private Integer page;
    @ApiModelProperty(value="每页显示条数",example = "5")
    private Integer rows;
    @ApiModelProperty(value="排序字段")
    private String sort;
    @ApiModelProperty(value="是否升序")
    private String order;
}
