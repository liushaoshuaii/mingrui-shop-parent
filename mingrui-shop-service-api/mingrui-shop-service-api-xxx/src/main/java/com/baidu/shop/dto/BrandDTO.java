package com.baidu.shop.dto;

import com.baidu.shop.base.BaseDTO;
import com.baidu.shop.group.MingruiOperation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import sun.plugin2.message.Message;

import javax.validation.constraints.NotNull;

/**
 * @ClassName BrandDTO
 * @Description: TODO
 * @Author lss
 * @Date 2020/12/25
 * @Version V1.0
 **/
@Data
@ApiModel(value="品牌DTO")
public class BrandDTO extends BaseDTO {
    @ApiModelProperty(value="品牌主键",example = "1")
    @NotNull(message = "主键不能为空",groups = {MingruiOperation.Update.class})
    private Integer id;
    @ApiModelProperty(value = "品牌名字")
    @NotNull(message = "品牌名字不能为空",groups = {MingruiOperation.Add.class,MingruiOperation.Update.class})
    private String name;
    @ApiModelProperty(value="品牌图片")
    private String image;
    @ApiModelProperty(value="品牌首字母")
    private Character letter;
    @NotNull(message = "品牌分类不能为空",groups = {MingruiOperation.Add.class})
    @ApiModelProperty(value="品牌分类信息")
    private String categories;
}
