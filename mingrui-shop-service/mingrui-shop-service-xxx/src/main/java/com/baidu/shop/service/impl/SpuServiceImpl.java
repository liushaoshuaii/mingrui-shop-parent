package com.baidu.shop.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baidu.shop.base.BaseApiService;
import com.baidu.shop.base.Result;
import com.baidu.shop.dto.SkuDTO;
import com.baidu.shop.dto.SpuDTO;
import com.baidu.shop.dto.SpuDetailDTO;
import com.baidu.shop.entity.*;
import com.baidu.shop.mapper.*;
import com.baidu.shop.service.GoodsService;
import com.baidu.shop.status.HTTPStatus;
import com.baidu.shop.utils.BaiduBeanUtil;
import com.baidu.shop.utils.ObjectUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RestController;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @ClassName SpuServiceImpl
 * @Description: TODO
 * @Author lss
 * @Date 2021/1/5
 * @Version V1.0
 **/
@RestController
@Slf4j
public class SpuServiceImpl extends BaseApiService implements GoodsService {
    @Resource
    private SpuMapper spuMapper;
    @Resource
    private BrandMapper brandMapper;
    @Resource
    private CategoryMapper categoryMapper;

    @Resource
    private SpuDetailMapper spuDetailMapper;
    @Resource
    private SkuMapper skuMapper;
    @Resource
    private StockMapper stockMapper;

    @Override
    public Result<PageInfo<SpuEntity>> getSpuInfo(SpuDTO spuDTO) {
        if (ObjectUtil.isNotNull(spuDTO.getPage())&&ObjectUtil.isNotNull(spuDTO.getRows()))//如果当前页数也每页条数不为空的话
            PageHelper.startPage(spuDTO.getPage(),spuDTO.getRows());//获取当前页和每页条数
        Example example = new Example(SpuEntity.class);
        Example.Criteria criteria = example.createCriteria();
        if(ObjectUtil.isNotNull(spuDTO.getSaleable())&&spuDTO.getSaleable()<2){//判断上架状态不为空同时小于2,那就只能是1和0
            criteria.andEqualTo("saleable",spuDTO.getSaleable());
        }
        if(!StringUtils.isEmpty(spuDTO.getTitle()))//判断标题不为空 进行模糊匹配条件查询
            criteria.andLike("title","%"+spuDTO.getTitle()+"%");
        if(!StringUtils.isEmpty((spuDTO.getSort())) && !StringUtils.isEmpty(spuDTO.getOrder()))
            example.setOrderByClause(spuDTO.getOrderBy());
        List<SpuEntity> spuEntities = spuMapper.selectByExample(example);

        List<SpuDTO> spuDTOList = spuEntities.stream().map(spuEntity -> {
            SpuDTO spuDTO1 = BaiduBeanUtil.copyProperties(spuEntity, SpuDTO.class);
            /*分类名称 方法一
            CategoryEntity categoryEntity = categoryMapper.selectByPrimaryKey(spuEntity.getCid1());
            CategoryEntity categoryEntity1 = categoryMapper.selectByPrimaryKey(spuEntity.getCid2());
            CategoryEntity categoryEntity2 = categoryMapper.selectByPrimaryKey(spuEntity.getCid3());
            spuDTO1.setCategoryName(categoryEntity.getName()+"/"+categoryEntity1.getName()+"/"+categoryEntity2.getName());*/
        //分类名称 方法二
            List<CategoryEntity> categoryEntities = categoryMapper.selectByIdList(Arrays.asList(spuEntity.getCid1(), spuEntity.getCid2(), spuEntity.getCid3()));
            String categoryName = categoryEntities.stream().map(categoryEntity -> categoryEntity.getName()).collect(Collectors.joining("/"));
            spuDTO1.setCategoryName(categoryName);
            /*String categoryName = "";
            List<String>strings= new ArrayList<>();
            strings.set(0,"");
            categoryEntities.stream().forEach(categoryEntity -> {
                strings.set(0,strings.get(0)+categoryEntity.getName()+"/");
            });
            categoryName = strings.get(0).substring(0,strings.get(0).length());*/
            //品牌名称
            BrandEntity brandEntity = brandMapper.selectByPrimaryKey(spuEntity.getBrandId());
            spuDTO1.setBrandName(brandEntity.getName());
            return spuDTO1;
        }).collect(Collectors.toList());
        PageInfo<SpuEntity> spuEntityPageInfo = new PageInfo<>(spuEntities);
        return this.setResult(HTTPStatus.OK,spuEntityPageInfo.getTotal()+"",spuDTOList);
    }
    @Transactional
    @Override
    public Result<JSONObject> saveGoods(SpuDTO spuDTO) {
        final Date date  = new Date();
        //新增spu
        SpuEntity spuEntity = BaiduBeanUtil.copyProperties(spuDTO, SpuEntity.class);
        spuEntity.setSaleable(1);
        spuEntity.setValid(1);
        spuEntity.setCreateTime(date);
        spuEntity.setLastUpdateTime(date);
        spuMapper.insertSelective(spuEntity);
        //新增spuDetail
        SpuDetailDTO spuDetail = spuDTO.getSpuDetail();
        SpuDetailEntity spuDetailEntity = BaiduBeanUtil.copyProperties(spuDetail, SpuDetailEntity.class);
        spuDetailEntity.setSpuId(spuEntity.getId());
        spuDetailMapper.insertSelective(spuDetailEntity);

        //新增sku
        this.saveSkusAndStock(spuDTO,spuEntity.getId(),date);

        return this.setResultSuccess();
    }

    @Override
    public Result<SpuDetailDTO> getSpuDetailByIdSpu(Integer spuId) {
        SpuDetailEntity spuDetailEntity = spuDetailMapper.selectByPrimaryKey(spuId);
        return setResultSuccess(spuDetailEntity);
    }

    @Override
    public Result<List<SkuDTO>> getSkuBySpuId(Integer spuId) {
        List<SkuDTO> list = skuMapper.selectSkuAndStockSpuId(spuId);
        return setResultSuccess(list);
    }
    @Transactional
    @Override
    public Result<JSONObject> editGoods(SpuDTO spuDTO) {
        final Date date = new Date();
        //修改spu属性
        SpuEntity spuEntity = BaiduBeanUtil.copyProperties(spuDTO, SpuEntity.class);
        spuEntity.setLastUpdateTime(date);
        spuMapper.updateByPrimaryKeySelective(spuEntity);

        //修改spuDetail
        spuDetailMapper.updateByPrimaryKeySelective(BaiduBeanUtil.copyProperties(spuDTO.getSpuDetail(),SpuDetailEntity.class));
        //修改sku 需要先通过spuId删除sku 然后新增数据\
//        Example example = new Example(SkuEntity.class);
//        example.createCriteria().andEqualTo("spuId",spuDTO.getId());
//        List<SkuEntity> skuEntities = skuMapper.selectByExample(example);
//        List<Long> skuIdArr = skuEntities.stream().map(sku -> sku.getId()).collect(Collectors.toList());
//        skuMapper.deleteByIdList(skuIdArr);
//        stockMapper.deleteByIdList(skuIdArr);
        this.deleteSkusAndStock(spuEntity.getId());
        //新增sku和stock数据
        this.saveSkusAndStock(spuDTO,spuEntity.getId(),date);

        return setResultSuccess();
    }
    @Transactional
    @Override
    public Result<JSONObject> deleteGoods(Integer spuId) {
        //通过spuId删除spu
        spuMapper.deleteByPrimaryKey(spuId);
        //删除spuDetail
        spuDetailMapper.deleteByPrimaryKey(spuId);
        //删除sku和stock
//        Example example = new Example(SkuEntity.class);
//        example.createCriteria().andEqualTo("spuId",spuId);
//        List<SkuEntity> skuEntities = skuMapper.selectByExample(example);
//        //得到skuId集合
//        List<Long> skuIdArr = skuEntities.stream().map(sku -> sku.getId()).collect(Collectors.toList());
//        skuMapper.deleteByIdList(skuIdArr);//通过spuId集合删除sku信息
//        stockMapper.deleteByIdList(skuIdArr);//通过spuId集合删除stock信息
        this.deleteSkusAndStock(spuId);
        return setResultSuccess();
    }

    @Override
    public Result<JSONObject> downOrUp(SpuDTO spuDTO) {
        SpuEntity spuEntity = BaiduBeanUtil.copyProperties(spuDTO, SpuEntity.class);
        if(ObjectUtil.isNotNull(spuEntity.getSaleable()) && spuEntity.getSaleable()<2){
            if(spuEntity.getSaleable()==1){
                spuEntity.setSaleable(0);
                spuMapper.updateByPrimaryKeySelective(spuEntity);
                return this.setResultSuccess("下架成功");
            }
            if(spuEntity.getSaleable()==0){
                spuEntity.setSaleable(1);
                spuMapper.updateByPrimaryKeySelective(spuEntity);
                return this.setResultSuccess("上架成功");
            }
        }
        return setResultError("操作失败");
    }

    //新增和修改代码中的 新增sku和stock数据代码重复 提出封装
    private  void saveSkusAndStock(SpuDTO spuDTO,Integer spuId,Date date){
        List<SkuDTO> skus = spuDTO.getSkus();
        skus.stream().forEach(skuDTO -> {
            SkuEntity skuEntity = BaiduBeanUtil.copyProperties(skuDTO, SkuEntity.class);
            skuEntity.setSpuId(spuId);
            skuEntity.setCreateTime(date);
            skuEntity.setLastUpdateTime(date);
            skuMapper.insertSelective(skuEntity);

            //新增stock

            StockEntity stockEntity = new StockEntity();
            stockEntity.setSkuId(skuEntity.getId());
            stockEntity.setStock(skuDTO.getStock());
            stockMapper.insertSelective(stockEntity);
        });
    }
    //删除skus和stock代码重复  提出来
    private void deleteSkusAndStock(Integer spuId){
        Example example = new Example(SkuEntity.class);
        example.createCriteria().andEqualTo("spuId",spuId);
        List<SkuEntity> skuEntities = skuMapper.selectByExample(example);
        //得到skuId集合
        List<Long> skuIdArr = skuEntities.stream().map(sku -> sku.getId()).collect(Collectors.toList());
        skuMapper.deleteByIdList(skuIdArr);//通过spuId集合删除sku信息
        stockMapper.deleteByIdList(skuIdArr);//通过spuId集合删除stock信息
    }
}
