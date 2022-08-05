package com.cloud.servicemanage.service;

import com.alibaba.fastjson.JSONObject;
import com.cloud.commonsmng.constants.Constants;
import com.cloud.commonsmng.entity.BaseUserInfo;
import com.cloud.commonsmng.entity.appletEntity.*;
import com.cloud.commonsmng.factory.BaseService;
import com.cloud.servicemanage.common.ConstantUtil;
import com.cloud.servicemanage.common.PageUtil;
import com.cloud.servicemanage.common.PageVo;
import com.cloud.servicemanage.common.UUIDGenerator;
import com.cloud.servicemanage.mapper.MemberMapper;
import com.cloud.servicemanage.mapper.ProductMapper;
import com.cloud.servicemanage.mapper.UserInfoMapper;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("produce")
public class ProductService extends BaseService {

    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Autowired
    private MemberMapper memberMapper;

    @Override
    public Map<String, Object> exec(String method, String param) {
        Map<String, Object> obj = new HashMap<>();
        switch (method) {
            case ConstantUtil.INSERT_PRODUCT:
                obj = insertproduct(param);
                break;
            case ConstantUtil.GET_PRODUCTS:
                obj = getProductPageVo(param);
                break;
            case  ConstantUtil.GET_PRODCUTS_TYPES:
                obj = getProductType();
                break;
            case ConstantUtil.GET_COOPERATION_MODE_BY_PRODUCTID:
                obj = getCooperationByProductId(param);
                break;
            case ConstantUtil.DELETE_PRODUCT_BYID:
                obj = deleteProduct(param);
                break;
            case ConstantUtil.RELEASE_PRODUCT_BYID:
                obj = releaseProduct(param);
                break;
            case ConstantUtil.UPDATE_COOPERATION_MODE:
                obj = updateCooperationMode(param);
                break;
            case ConstantUtil.ADD_COOPERATION_MODE:
                obj = addCooperationMode(param);
                break;
            case ConstantUtil.UPDATE_PRODUCT:
                obj = updateProduct(param);
                break;
            case ConstantUtil.GET_PRODUCT_PICTURES:
                obj = getProductPictures();
                break;
            case ConstantUtil.GET_PRODUCT_BYID:
                obj = getProductById(param);
                break;
            case ConstantUtil.GET_PRODUCT_PERMISSION:
                obj = getPermission();
                break;
            default:
                break;
        }
        return obj;
    }

    /**
     * 新增产品
     * @param param
     * @return
     */
    private Map<String,Object> insertproduct(String param){
        Map<String, Object> result = new HashMap<>();
       // Map<String, Object> map = new HashMap<>();
        try {
            Product product = JSONObject.parseObject(param,Product.class);
            //获取操作者基本信息
            super.insertBaseInfo(product);
            List<CooperationMode> m = product.getCooperationModes();
            if(m != null && !m.equals("") && m.size()>0){
                for(int j = 0; j < m.size(); j++){
                    super.insertBaseInfo(m.get(j));
                }
                product.setCooperationModes(m);
            }

            List<ProductsExpand> peList = product.getProductsExpandList();
            if(peList != null && !peList.equals("") && peList.size()>0){
                for(int j = 0; j < peList.size(); j++){
                    super.insertBaseInfo(peList.get(j));
                }
                product.setProductsExpandList(peList);
            }
            Integer i = productMapper.insertProduct(product);
            Map<String, Object> resMap = new HashMap<>();
            resMap.put("Success",i>0);
            resMap.put("id",product.getId());
            Constants.getSuccMsg(result, resMap);
           /* if(i>0){
                map.put("code", 200);
                map.put("msg", "succ");
                Constants.getSuccMsg(result, map);
                return result;
            }else{
                map.put("code", 204);
                map.put("msg", "添加产品失败!");
                Constants.getSuccMsg(result, map);
            }*/
        }catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
            //map.put("msg", e.getMessage());
            Constants.getSuccMsg(result, "产品添加失败");
        }
        return result;
    }
    /**
     * 获取产品list
     *
     */
    private Map<String,Object> getProductPageVo(String param){
        Map<String, Object> result = new HashMap<>();
        try {
            PageVo pagevo = JSONObject.parseObject(param,PageVo.class);
            PageUtil pageUtil = pagevo.getPage();//获取前端的页面分页信息
            BaseUserInfo u = super.getUserInfo();
            String uRole = null;
            //获取用户角色
            User u1 = userInfoMapper.getuRolebyId(u.getId());

            if(u1==null||u1.equals("")||u1.getURole()== null ||u1.getURole().equals("")){
                Constants.getErrMsg(result, "用户角色获取失败");
                return result;
            }

            uRole = u1.getURole();

           /* if(u!=null && !u.equals("") && u.getURole()!=null && !u.getURole().equals("")){
                uRole = u.getURole().toString();
            }else{
                //获取用户角色
                User u1 = userInfoMapper.getuRolebyId(u.getId());

                if(u1==null||u1.equals("")||u1.getURole()== null ||u1.getURole().equals("")){
                    Constants.getErrMsg(result, "用户角色获取失败");
                    return result;
                }

                uRole = u1.getURole().toString();
                u.setURole(u1.getURole());

            }*/

            //判断是否为科创
            Map<String,Object> isKC = getPermission();
            if(null != isKC.get("data") && Boolean.parseBoolean(isKC.get("data").toString())){
                Object searchdata = pagevo.getSearchdata();
                Map<String, Object> searchMap = JSONObject.parseObject(JSONObject.toJSONString(searchdata), HashMap.class);
                searchMap.put("isKC","1");
                pagevo.setSearchdata(searchMap);
            }else{
                Object searchdata = pagevo.getSearchdata();
                Map<String, Object> searchMap = JSONObject.parseObject(JSONObject.toJSONString(searchdata), HashMap.class);
                searchMap.put("isKC","0");
                pagevo.setSearchdata(searchMap);
            }
            //uRole对应字典表中kind 为membertype的code字段
            //获取产品查看权限，是否限制，只能查看所属部门下的产品，字典表中type 为1，表示有发布权限，2表示限制部门查看本部门下的产品
            Integer t = productMapper.getProductPermission(uRole,"2","membertype");
            if(t>0){
                Object searchdata = pagevo.getSearchdata();
                Map<String, Object> searchMap = JSONObject.parseObject(JSONObject.toJSONString(searchdata), HashMap.class);
                //Map<String,String> departmentId = new HashMap<>();
                searchMap.put("departmentId",u.getDepartmentId());
                pagevo.setSearchdata(searchMap);
            }
            int totalNum = productMapper.getPageTotal(pagevo);
            List<Product> products = productMapper.getProductlistPageVo(pagevo);
            products.forEach((Product p) -> {
                 List<ProductsExpand> peList = productMapper.getProductExpandList(p.getId());
                p.setProductsExpandList(peList);
                if(StringUtils.isEmpty(p.getStatus()+"") || !StringUtils.equals("2",p.getStatus()+"")){
                    p.setModifierName("");
                }

            });
            PageVo<Product> productPageVo = new PageVo<>();
            productPageVo.setPage(new PageUtil(pageUtil.getPageIndex(), pageUtil.getPageSize(), totalNum));
            productPageVo.setDataList(products);
            Constants.getSuccMsg(result, productPageVo);
        }catch (Exception e) {
            e.printStackTrace();
            Constants.getErrMsg(result, Constants.RESULT_NO_DATA);
        }

        return result;

    }

    /**
     * 获取产品类型list
     */
    private Map<String,Object> getProductType(){
        Map<String,Object> result = new HashMap<>();
        try {
            List<Dictionary> prodcuttype1 = this.getdictionarys("producttype","0",null);
            //System.out.println("--------"+prodcuttype1.toString());
            Constants.getSuccMsg(result,prodcuttype1);
        } catch (Exception e) {
            e.printStackTrace();
            Constants.getErrMsg(result, Constants.RESULT_NO_DATA);
        }
        return result;
    }

    /**
     * 获取产品种类list
     * @param kind
     * @param parentId
     * @param dictionaries
     * @return
     */
    private List<Dictionary> getdictionarys(String kind, String parentId, List<Dictionary> dictionaries){

        if(parentId.equals("0")){
            dictionaries = productMapper.getProductTypes(kind,"0");
        }
        if(dictionaries != null && dictionaries.size()>0){
            for(int i = 0; i < dictionaries.size(); i++){
                //获取dictionary子集
                Dictionary dictionary = dictionaries.get(i);
                List<Dictionary> children = productMapper.getProductTypes(kind,dictionary.getCode());
                dictionary.setChildren(children);
                dictionaries.set(i,dictionary);
                this.getdictionarys(kind, "", children);
            }
            return dictionaries;
        }else{
            return null;
        }
    }

    /**
     *
     * 根据产品Id获取合作模式信息list
     * @param param
     * @return
     */
    private Map<String,Object> getCooperationByProductId(String param){
        Map<String,Object> result = new HashMap<>();
        try {
            CooperationMode m = JSONObject.parseObject(param,CooperationMode.class);
            if(m != null && m.getProductId() != null && !m.getProductId().equals("")){
                List<CooperationMode> modes = productMapper.getCooperationByProductId(m.getProductId());
                Constants.getSuccMsg(result,modes);
            }else{
               // Map<String,Object>map = new HashMap<>();
               // map.put("code","201");
               // map.put("msg","err,产品Id为空");
               // map.put("data","");
                Constants.getErrMsg(result,"err,产品Id为空");
            }

        } catch (Exception e) {
            e.printStackTrace();
            Constants.getErrMsg(result, Constants.RESULT_NO_DATA);
        }
            return result;
    }

    /**
     * 删除产品byID
     * @param param
     * @return
     */
    private Map<String,Object> deleteProduct(String param){
        Map<String,Object> result = new HashMap<>();
        try {
            Product p = JSONObject.parseObject(param,Product.class);
            p.setStatus(0);
            super.updateBaseInfo(p,p.getId());
            Integer i = productMapper.updateProductStatus(p);
            Constants.getSuccMsg(result,i > 0);
            //System.out.println("-------"+result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 发布产品byID
     * @param param
     * @return
     */
    private Map<String,Object> releaseProduct(String param){
        Map<String,Object> result = new HashMap<>();
        try {
            Product p = JSONObject.parseObject(param,Product.class);
            if(null == p.getStatus() || "".equals(p.getStatus())){
                p.setStatus(2);
            }
            super.updateBaseInfo(p,p.getId());
            Integer i = productMapper.updateProductStatus(p);
            //提交发布后发送消息
            if(null == p.getStatus() || "1".equals(p.getStatus())){
                Product product = productMapper.getProductById(p.getId());
                String msg = "来自"+product.getDepartmentName()+"的"+product.getTypeName()+"类型产品（"+product.getProductName()+"）正等待发布，请尽快前往处理。";
                Notes notes = new Notes();
                String noteId = UUIDGenerator.getUUID();
                notes.setTitle("产品发布通知");
                notes.setId(noteId);
                notes.setContent(msg);
                notes.setTaskType("user");
                notes.setIsFeedBack(0);
                notes.setStatus("1");
                notes.setCreatorId("admin");
                notes.setModifierId("admin");

                List<String> userList = productMapper.getUserByType();
                List<NotesDetail> notesDetailList = new ArrayList<>();
                userList.forEach((String id) -> {
                    NotesDetail n = new NotesDetail();
                    n.setNotesId(noteId);
                    n.setId(UUIDGenerator.getUUID());
                    n.setStatus("1");
                    n.setUserId(id);
                });
                notes.setNotesDetailList(notesDetailList);
                Integer i1 = memberMapper.insertNotes(notes);
            }
            Constants.getSuccMsg(result,i > 0);
            //System.out.println("-------"+result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 更新合作信息表
     * @param param
     * @return
     */
    private Map<String,Object> updateCooperationMode(String param){
        Map<String,Object> result = new HashMap<>();
        try {
            CooperationMode m = JSONObject.parseObject(param,CooperationMode.class);
            super.updateBaseInfo(m,m.getId());
            if(m != null&& !m.equals("") && m.getId() != null && !m.getId().equals("")){
                Integer i = productMapper.updateCooperationMode(m);
                Constants.getSuccMsg(result,i>0);
            }else{
                Constants.getErrMsg(result,"入参数据异常");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Constants.getErrMsg(result,Constants.RESULT_NO_DATA);
        }
        return result;
    }

    /**
     * 添加合作信息
     * @return
     */
    private Map<String,Object> addCooperationMode(String param){
        Map<String,Object> result = new HashMap<>();
        try {
            CooperationMode m = JSONObject.parseObject(param,CooperationMode.class);
            super.insertBaseInfo(m);
            if(m != null && !m.equals("")){
                Integer i = productMapper.addCooperationMode(m);
                Constants.getSuccMsg(result,i>0);
            }else{
                Constants.getErrMsg(result,"入参数据异常");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Constants.getErrMsg(result,Constants.RESULT_NO_DATA);
        }
        return  result;
    }

    /**
     * 更新产品信息
     * @param param
     * @return
     */
    private Map<String,Object> updateProduct(String param){
        Map<String, Object> result = new HashMap<>();
        try {
            Product p = JSONObject.parseObject(param,Product.class);

            List<ProductsExpand> peList = p.getProductsExpandList();
            if(peList != null && !peList.equals("") && peList.size()>0){
                for(int j = 0; j < peList.size(); j++){
                    super.insertBaseInfo(peList.get(j));
                }
                p.setProductsExpandList(peList);
            }

           productMapper.insertProductExpand(p);

            super.updateBaseInfo(p,p.getId());
            if(p != null && !p.equals("")){
                p.setStatus(1);
                Integer i = productMapper.updateProduct(p);
                Constants.getSuccMsg(result,i>0);
            }else{
                Constants.getErrMsg(result,"入参数据异常");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Constants.getErrMsg(result,Constants.RESULT_NO_DATA);
        }
        return  result;
    }

    /**
     * 获取产品图片list
     * @return
     */
    private Map<String, Object> getProductPictures(){
        Map<String, Object> result = new HashMap<>();
        try {
            List<ProductPicture> pictures = productMapper.getProductPictures(1);
            Constants.getSuccMsg(result,pictures);
        } catch (Exception e) {
            e.printStackTrace();
            Constants.getErrMsg(result,Constants.RESULT_NO_DATA);
        }
        return result;
    }

    /**
     * 根据产品Id获取产品
     * @param param
     * @return
     */
    private Map<String, Object> getProductById(String param){
        Map<String, Object> result = new HashMap<>();
        try {
            String  id = JSONObject.parseObject(param).getString("id");
            if(id == null || id.equals("")){
                Constants.getErrMsg(result,"未获取到产品Id");
            }else {
                Product p = productMapper.getProductById(id);
                List<CooperationMode> modes = productMapper.getCooperationByProductId(id);
                Map<String,Object>  list = productMapper.SelectActivityFile(id);
                p.setCooperationModes(modes);
                List<ProductsExpand> peList = productMapper.getProductExpandList(p.getId());
                p.setProductsExpandList(peList);
                 Constants.getSuccMsg(result, p);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Constants.getErrMsg(result,Constants.RESULT_NO_DATA);
        }
        return result;
    }

    /**
     * 获取人员角色,是否有发布产品的权限
     * @return
     */
    private Map<String,Object> getPermission(){
        Map<String,Object> result = new HashMap<>();
        BaseUserInfo u = super.getUserInfo();
        String uRole = null;
        //获取用户角色
        User u1 = userInfoMapper.getuRolebyId(u.getId());
        if(u1==null||u1.equals("")||u1.getURole()== null ||u1.getURole().equals("")){
            Constants.getErrMsg(result, "用户角色获取失败");
            return result;
        }
        uRole = u1.getURole();

        /*if(u!=null && !u.equals("") && u.getURole()!=null && !u.getURole().equals("")){
            uRole = u.getURole().toString();
        }else{
            //获取用户角色
            User u1 = userInfoMapper.getuRolebyId(u.getId());

            if(u1==null||u1.equals("")||u1.getURole()== null ||u1.getURole().equals("")){
                Constants.getErrMsg(result, "用户角色获取失败");
                return result;
            }

            uRole = u1.getURole().toString();
            u.setURole(u1.getURole());

        }*/
        //uRole对应字典表中kind 为membertype的code字段
        //获取产品查看权限，是否限制，只能查看所属部门下的产品，字典表中type 为1，表示有发布权限，2表示限制部门查看本部门下的产品
        Integer t = productMapper.getProductPermission(uRole,"1","membertype");
        Constants.getSuccMsg(result,t>0);
        return result;
    }


}
