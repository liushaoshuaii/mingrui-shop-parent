package com.baidu.shop.entity;

import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @ClassName BrandEntity
 * @Description: TODO
 * @Author lss
 * @Date 2020/12/25
 * @Version V1.0
 **/
@Data
@Table(name = "tb_brand")
public class BrandEntity {

    @Id
    private Integer id;

    private String name;

    private String image;

    private Character letter;
}
