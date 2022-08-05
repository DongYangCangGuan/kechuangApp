package com.cloud.servicemanage.service;

import com.alibaba.fastjson.JSONObject;
import com.cloud.commonsmng.constants.Constants;
import com.cloud.commonsmng.entity.appletEntity.ConsultingInfo;
import com.cloud.commonsmng.factory.BaseService;
import com.cloud.servicemanage.common.ConstantUtil;
import com.cloud.servicemanage.common.PageUtil;
import com.cloud.servicemanage.common.PageVo;
import com.cloud.servicemanage.mapper.ConsultingMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 咨询业务层
 */
@Service("Consulting")
public class ConsultingService extends BaseService {
    @Autowired
    private ConsultingMapper consultingMapper;


    //实例化日志对象，引用jar包org.slf4j.Logger
    private static final Logger logger = LoggerFactory.getLogger(ConsultingService.class);

    @Override
    public Map<String, Object> exec(String method, String param) {
        Map<String, Object> obj = new HashMap<>();
        switch (method) {
            case ConstantUtil.GET_CONSULTING_List: //getMemberInfoPageVo
                obj = getConsultingInfoPageVo(param);
                break;
            default:
                break;
        }
        return obj;
    }

    private Map<String, Object> getConsultingInfoPageVo(String param){
        Map<String, Object> result = new HashMap<>();
        PageVo pageVo = JSONObject.parseObject(param, PageVo.class);
        PageUtil page = pageVo.getPage();


        try {
            int consultingPageTotal = consultingMapper.getConsultingPageTotal(pageVo);
            List<ConsultingInfo> consultingInfoList = consultingMapper.getConsultingPageVo(pageVo);
            PageVo<ConsultingInfo> consultingPageVo = new PageVo<>();
            consultingPageVo.setPage(new PageUtil(page.getPageIndex(), page.getPageSize(), consultingPageTotal));
            consultingPageVo.setDataList(consultingInfoList);
            Constants.getSuccMsg(result, consultingPageVo);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return result;
    }
}
