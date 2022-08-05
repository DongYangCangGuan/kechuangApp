package com.cloud.servicewechat.service;

import com.cloud.commonsmng.constants.Constants;
import com.cloud.commonsmng.entity.appletEntity.Slider;
import com.cloud.commonsmng.factory.BaseService;
import com.cloud.servicewechat.common.ConstantUtil;
import com.cloud.servicewechat.mapper.SliderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("slider")
public class SliderService extends BaseService {

    @Autowired
    private SliderMapper sliderMapper;

    @Override
    public Map<String, Object> exec(String method, String param) {
        Map<String,Object> obj = new HashMap<>();
        switch (method){
            case ConstantUtil.GET_SLIDER: //getSlider;
                obj = getSlider(param);
                break;
            default:
                break;
        }
        return obj;
    }
    private Map<String, Object> getSlider(String param){
        Map<String,Object> result = new HashMap<>();
        //获取所有展示的轮播图
        List<Slider> sliderList = sliderMapper.getSlider();
        Constants.getSuccMsg(result,sliderList);
        return result;
    }


}
