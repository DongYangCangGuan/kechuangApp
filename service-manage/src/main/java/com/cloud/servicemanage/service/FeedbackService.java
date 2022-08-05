package com.cloud.servicemanage.service;

import com.alibaba.fastjson.JSONObject;
import com.cloud.commonsmng.constants.Constants;
import com.cloud.commonsmng.entity.appletEntity.Feedback;
import com.cloud.commonsmng.factory.BaseService;
import com.cloud.servicemanage.common.ConstantUtil;
import com.cloud.servicemanage.common.PageUtil;
import com.cloud.servicemanage.common.PageVo;
import com.cloud.servicemanage.mapper.FeedbackMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service("feedback")
public class FeedbackService  extends BaseService {

    @Autowired
    private FeedbackMapper feedbackMapper;

    private static final Logger logger = LoggerFactory.getLogger(FeedbackService.class);


    @Override
    public Map<String, Object> exec(String method, String param) {
        Map<String, Object> obj = new HashMap<>();
        switch (method) {
            case ConstantUtil.GET_FEEDBACK_PAGE_VO: //
                obj = getFeedbackPageVo(param);
                break;
            case ConstantUtil.GET_FEEDBACK_DETAIL_BY_ID:
                obj = getFeedbackDetailById(param);
                break;
            case ConstantUtil.DELETE_FEEDBACK:
                obj = deleteFeedback(param);
                break;
            default:
                break;
        }
        return obj;
    }

    //获取列表
    private Map<String, Object> getFeedbackPageVo(String param) {
        Map<String, Object> result = new HashMap<>();
        PageVo pageVo = JSONObject.parseObject(param, PageVo.class);
        PageUtil pageUtil = pageVo.getPage();//获取前端的页面分页信息
        try {
            int totalNum = feedbackMapper.getPageTotal(pageVo);
            List<Feedback>  feedbackList = feedbackMapper.getFeedbackListPageVo(pageVo);
            PageVo<Feedback> feedbackPageVo = new PageVo<>();
            feedbackPageVo.setPage(new PageUtil(pageUtil.getPageIndex(), pageUtil.getPageSize(), totalNum));
            feedbackPageVo.setDataList(feedbackList);
            Constants.getSuccMsg(result, feedbackPageVo);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return result;
    }


    // 查看详细
    private Map<String, Object> getFeedbackDetailById(String param) {
        Map<String, Object> result = new HashMap<>();

        //获取前端传递的id
        String id = JSONObject.parseObject(param).getString("id");

        //根据id查询
        Feedback feedback = feedbackMapper.getFeedbackDetailById(id);

        Constants.getSuccMsg(result, feedback);
        return result;
    }

    // 删除
    private Map<String, Object> deleteFeedback(String param) {
        Map<String, Object> result = new HashMap<>();
        JSONObject jsonObject = JSONObject.parseObject(param);

        String id = jsonObject.getString("id");
        Feedback feedback = new Feedback();
        feedback.setId(id);

        int delResult = feedbackMapper.deleteFeedback(feedback);

        Constants.getSuccMsg(result, delResult > 0);
        return result;
    }

    }