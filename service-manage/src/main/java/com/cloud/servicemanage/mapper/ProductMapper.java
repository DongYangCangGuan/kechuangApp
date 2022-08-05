package com.cloud.servicemanage.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cloud.commonsmng.entity.appletEntity.*;
import com.cloud.servicemanage.common.PageVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


import java.util.List;
import java.util.Map;

/**
 * 报告类的Mapper
 */
@Mapper
public interface ProductMapper extends BaseMapper<Product> {

    //新增产品
    Integer insertProduct(@Param("product") Product p);

    //获取产品
    List<Product> getProductlistPageVo(@Param("pageVo") PageVo pageVo);

    //获取产品总条数
    Integer getPageTotal(@Param("pageVo") PageVo pageVo);

    //获取产品对应的合作模式list
    List<CooperationMode> getCooperationModes(@Param("productid")String productId);

    //根据Id获取产品
    Product getProductById (@Param("id")String id);

    //根据kind和parentId获取dictionary
    List<Dictionary> getProductTypes(@Param("kind")String kind, @Param("parentId")String parentId);

    //根据产品Id获取合作信息list
    List<CooperationMode> getCooperationByProductId(@Param("productId")String productId);

    //根据产品Id删除产品，更新status状态为0     发布产品  状态为2
    Integer updateProductStatus(@Param("p")Product p);

    //更新合作信息表
    Integer updateCooperationMode(@Param("mode")CooperationMode mode);

    //添加合作信息
    Integer addCooperationMode(@Param("mode")CooperationMode mode);

    //修改产品信息  byId
    Integer updateProduct(@Param("p")Product p);

    //新增产品模板
    Integer insertProductExpand(@Param("product")Product product);


    //获取产品图片list
    List<ProductPicture> getProductPictures(@Param("delFlag")Integer delFlag);

    //判断是否有权限发布产品
    Integer getProductPermission(@Param("code")String code,@Param("type")String type,@Param("kind")String kind);

    //获取用户
    List<String> getUserByType();

    Map<String,Object> SelectActivityFile(@Param("id") String id);


    //获取配置list
    List<ProductsExpand> getProductExpandList(@Param("id") String id);
}
