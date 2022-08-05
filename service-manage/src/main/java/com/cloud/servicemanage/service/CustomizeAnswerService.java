package com.cloud.servicemanage.service;

import com.alibaba.fastjson.JSONObject;
import com.cloud.commonsmng.constants.Constants;
import com.cloud.commonsmng.factory.BaseService;
import com.cloud.commonsmng.entity.appletEntity.CustomizeAnswer;
import com.cloud.servicemanage.common.ConstantUtil;
import com.cloud.servicemanage.common.PageUtil;
import com.cloud.servicemanage.common.PageVo;
import com.cloud.servicemanage.mapper.CustomizeAnswerMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("customizeAnswer")
public class CustomizeAnswerService extends BaseService {

    @Autowired
    private CustomizeAnswerMapper customizeAnswerMapper;

    private static final Logger logger = LoggerFactory.getLogger(CustomizeAnswerService.class);

    @Override
    public Map<String, Object> exec(String method, String param) {
        Map<String, Object> obj = new HashMap<>();
        switch (method) {
            case ConstantUtil.GET_CUSTOMIZE_ANSWER_LIST:
                obj = getCustomizeAnswerList(param);
                break;
            case ConstantUtil.GET_REPLIED_CUSTOMIZE_ANSWER_LIST:
                obj = getRepliedCustomizeAnswerList(param);
                break;
            case ConstantUtil.GET_UN_REPLIED_CUSTOMIZE_ANSWER_LIST:
                obj = getUnRepliedCustomizeAnswerList(param);
                break;
            case ConstantUtil.GET_CUSTOMIZE_ANSWER_DETAIL_BY_ID:
                obj = getCustomizeAnswerDetailById(param);
                break;
            case ConstantUtil.REPLY_CUSTOMIZE_ANSWER:
                obj = replyCustomizeAnswer(param);
                break;
            default:
                break;
        }
        return obj;
    }

    // 回复
    private Map<String, Object> replyCustomizeAnswer(String param) {
        Map<String, Object> result = new HashMap<>();
        //转化json格式数据为Notes对象
        CustomizeAnswer customizeAnswer = JSONObject.parseObject(param, CustomizeAnswer.class);
        customizeAnswer.setReplyerId(super.getUserInfo().getId());
        int updateNotes = customizeAnswerMapper.replyCustomizeAnswer(customizeAnswer);
        Constants.getSuccMsg(result, updateNotes > 0);
        return result;
    }

    // 查看回答细节
    private Map<String, Object> getCustomizeAnswerDetailById(String param) {
        Map<String, Object> result = new HashMap<>();

        //获取前端传递的id
        String id = JSONObject.parseObject(param).getString("id");

        //根据id查询
        CustomizeAnswer customizeAnswer = customizeAnswerMapper.getCustomizeAnswerDetailById(id);

        Constants.getSuccMsg(result, customizeAnswer);
        return result;
    }


    // 查找已回复
    private Map<String, Object> getRepliedCustomizeAnswerList(String param) {
        Map<String, Object> result = new HashMap<>();

        //转化json格式数据为PageVo对象
        PageVo pageVo = JSONObject.parseObject(param, PageVo.class);
        PageUtil pageUtil = pageVo.getPage();//获取前端页面信息
        try {
            int pageTotal = customizeAnswerMapper.getRepliedPageTotal(pageVo);//获取页面数据总条数
            if (pageTotal > 0) {
                List<CustomizeAnswer> customizeAnswerList = customizeAnswerMapper.getRepliedCustomizeAnswerPageVo(pageVo);//获取页面信息

                //创建PageVo对象
                PageVo<CustomizeAnswer> customizeAnswerPageVo = new PageVo<>();
                //将前端传递的页面数据，和查询到的数据总条数赋值
                customizeAnswerPageVo.setPage(new PageUtil(pageUtil.getPageIndex(), pageUtil.getPageSize(), pageTotal));
                customizeAnswerPageVo.setDataList(customizeAnswerList);

                Constants.getSuccMsg(result, customizeAnswerPageVo);
            } else {
                Constants.getSuccMsg(result, "没有数据");
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return result;
    }

    // 查找未回复
    private Map<String, Object> getUnRepliedCustomizeAnswerList(String param) {
        Map<String, Object> result = new HashMap<>();

        //转化json格式数据为PageVo对象
        PageVo pageVo = JSONObject.parseObject(param, PageVo.class);
        PageUtil pageUtil = pageVo.getPage();//获取前端页面信息
        try {
            int pageTotal = customizeAnswerMapper.getUnRepliedPageTotal(pageVo);//获取页面数据总条数
            if (pageTotal > 0) {
                List<CustomizeAnswer> customizeAnswerList = customizeAnswerMapper.getUnRepliedCustomizeAnswerPageVo(pageVo);//获取页面信息

                //创建PageVo对象
                PageVo<CustomizeAnswer> customizeAnswerPageVo = new PageVo<>();
                //将前端传递的页面数据，和查询到的数据总条数赋值
                customizeAnswerPageVo.setPage(new PageUtil(pageUtil.getPageIndex(), pageUtil.getPageSize(), pageTotal));
                customizeAnswerPageVo.setDataList(customizeAnswerList);

                Constants.getSuccMsg(result, customizeAnswerPageVo);
            } else {
                Constants.getSuccMsg(result, "没有数据");
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return result;
    }

    // 查找全部
    private Map<String, Object> getCustomizeAnswerList(String param) {
        Map<String, Object> result = new HashMap<>();

        //转化json格式数据为PageVo对象
        PageVo pageVo = JSONObject.parseObject(param, PageVo.class);
        PageUtil pageUtil = pageVo.getPage();//获取前端页面信息
        try {
            int pageTotal = customizeAnswerMapper.getPageTotal(pageVo);//获取页面数据总条数
            if (pageTotal > 0) {
                List<CustomizeAnswer> customizeAnswerList = customizeAnswerMapper.getCustomizeAnswerPageVo(pageVo);//获取页面信息

                //创建PageVo对象
                PageVo<CustomizeAnswer> customizeAnswerPageVo = new PageVo<>();
                //将前端传递的页面数据，和查询到的数据总条数赋值
                customizeAnswerPageVo.setPage(new PageUtil(pageUtil.getPageIndex(), pageUtil.getPageSize(), pageTotal));
                customizeAnswerPageVo.setDataList(customizeAnswerList);

                Constants.getSuccMsg(result, customizeAnswerPageVo);
            } else {
                Constants.getSuccMsg(result, "没有数据");
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return result;
    }
}
