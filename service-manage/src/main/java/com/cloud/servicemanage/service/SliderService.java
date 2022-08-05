package com.cloud.servicemanage.service;

import com.alibaba.fastjson.JSONObject;
import com.cloud.commonsmng.constants.Constants;
import com.cloud.commonsmng.entity.appletEntity.Dictionary;
import com.cloud.commonsmng.entity.appletEntity.Slider;
import com.cloud.commonsmng.factory.BaseService;
import com.cloud.servicemanage.common.ConstantUtil;
import com.cloud.servicemanage.common.PageUtil;
import com.cloud.servicemanage.common.PageVo;
import com.cloud.servicemanage.mapper.MemberMapper;
import com.cloud.servicemanage.mapper.SliderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("Slider")
public class SliderService extends BaseService {

    @Autowired
    private SliderMapper sliderMapper;
    @Autowired
    private MemberMapper memberMapper;

    @Override
    public Map<String, Object> exec(String method, String param) {
        Map<String, Object> obj = new HashMap<>();
        switch (method) {
            case ConstantUtil.GET_SLIDER_PAGE_VO: //getSliderPageVo
                obj = getSliderPageVo(param);
                break;
            case ConstantUtil.GET_SLIDER_BY_ID: //getSliderById
                obj = getSliderById(param);
                break;
            case ConstantUtil.INSERT_SLIDER: //insertSlider
                obj = insertSlider(param);
                break;
            case ConstantUtil.UPDATE_SLIDER: //updateSlider
                obj = updateSlider(param);
                break;
            case ConstantUtil.DELETE_SLIDER: //deleteSlider
                obj = deleteSlider(param);
                break;
            case ConstantUtil.CHECK_SLIDER_BY_INDEX: //checkSliderByIndex
                obj = checkSliderByIndex(param);
                break;
            default:
                break;
        }
        return obj;
    }

    //判断优先级是否存在
    private Map<String, Object> checkSliderByIndex(String param) {
        Map<String, Object> result = new HashMap<>();
        Integer index = JSONObject.parseObject(param).getInteger("index");
        Integer checkSliderByIndex = sliderMapper.checkSliderByIndex(index);
        Constants.getSuccMsg(result, checkSliderByIndex > 0);
        return result;
    }

    //删除轮播图信息
    private Map<String, Object> deleteSlider(String param) {
        Map<String, Object> result = new HashMap<>();
        String id = JSONObject.parseObject(param).getString("id");
        Integer delete = sliderMapper.deleteSlider(id);
        Constants.getSuccMsg(result, delete > 0);
        return result;
    }

    //修改轮播图信息
    private Map<String, Object> updateSlider(String param) {
        Map<String, Object> result = new HashMap<>();
        Slider slider = JSONObject.parseObject(param, Slider.class);
        super.updateBaseInfo(slider, slider.getId());
        //获取图片服务器
        /*List<Dictionary> ds = memberMapper.getMemberTypelist("pictureaddress");
        String address = ds.get(0).getName();
        String pic = slider.getPic();
        slider.setPic(address + pic);*/
        Integer update = sliderMapper.updateSlider(slider);
        Constants.getSuccMsg(result, update > 0);
        return result;
    }

    //新增轮播图信息
    private Map<String, Object> insertSlider(String param) {
        Map<String, Object> result = new HashMap<>();
        Slider slider = JSONObject.parseObject(param, Slider.class);
        super.insertBaseInfo(slider);
        //获取图片服务器
        /*List<Dictionary> ds = memberMapper.getMemberTypelist("pictureaddress");
        String address = ds.get(0).getName();
        String pic = slider.getPic();
        slider.setPic(address + pic);*/
        Integer insert = sliderMapper.insertSlider(slider);
        Constants.getSuccMsg(result, insert > 0);
        return result;
    }

    //根据编号查询轮播图信息
    private Map<String, Object> getSliderById(String param) {
        Map<String, Object> result = new HashMap<>();
        String id = JSONObject.parseObject(param).getString("id");
        Slider sliderById = sliderMapper.getSliderById(id);
        Constants.getSuccMsg(result, sliderById);
        return result;
    }

    //分页获取轮播图信息
    private Map<String, Object> getSliderPageVo(String param) {
        Map<String, Object> result = new HashMap<>();
        PageVo pageVo = JSONObject.parseObject(param, PageVo.class);
        PageUtil page = pageVo.getPage();
        Integer pageTotal = sliderMapper.getPageTotal(pageVo);
        List<Slider> sliderList = sliderMapper.getSliderPageVo(pageVo);
        PageVo<Slider> sliderPageVo = new PageVo<>();
        sliderPageVo.setPage(new PageUtil(page.getPageIndex(), page.getPageSize(), pageTotal));
        sliderPageVo.setDataList(sliderList);
        Constants.getSuccMsg(result, sliderPageVo);
        return result;
    }
}
