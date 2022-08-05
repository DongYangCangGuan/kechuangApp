package com.cloud.servicewechat.service;

import com.alibaba.fastjson.JSONObject;
import com.cloud.commonsmng.constants.Constants;
import com.cloud.commonsmng.entity.appletEntity.Collect;
import com.cloud.commonsmng.entity.appletEntity.Point;
import com.cloud.commonsmng.entity.appletEntity.PointEntity;
import com.cloud.commonsmng.factory.BaseService;
import com.cloud.servicewechat.common.ConstantUtil;
import com.cloud.servicewechat.common.PageUtil;
import com.cloud.servicewechat.common.PageVo;
import com.cloud.servicewechat.mapper.PointMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.misc.BASE64Encoder;

import java.text.SimpleDateFormat;
import java.util.*;

@Service("point")
public class PointService extends BaseService {

    @Autowired
    private PointMapper pointMapper;


    //实例化日志对象，引用jar包org.slf4j.Logger
    private static final Logger logger = LoggerFactory.getLogger(NotesService.class);

    private static final BASE64Encoder encoder = new BASE64Encoder();

    @Override
    public Map<String, Object> exec(String method, String param) {
        Map<String, Object> obj = new HashMap<>();
        switch (method) {
            case ConstantUtil.ADD_POINT:
                obj = addPoint(param);
                break;
            case ConstantUtil.GET_POINT_LIST:
                obj = getPointList(param);
                break;
            default:
                break;
        }
        return obj;
    }

    // 添加埋点信息
    public Map<String, Object> addPoint(String param) {
        Map<String, Object> result = new HashMap<>();
        PointEntity point = JSONObject.parseObject(param, PointEntity.class);
        String time=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(Long.parseLong(point.getDateTime())));
        point.setDateTime(time);
        if (super.getUserInfo() == null) {
            Constants.getErrMsg(result, false);
            return result;
        }
        String userId = super.getUserInfo().getId();
        point.setUserId(userId);
        int update = pointMapper.addPoint(point);
        Constants.getSuccMsg(result, update > 0);
        return result;
    }

    // 更新经纬度和地理位置
    public Map<String, Object> updateLocationAndAddress(String param) {
        Map<String, Object> result = new HashMap<>();
        Point point = JSONObject.parseObject(param, Point.class);
        if (super.getUserInfo() == null) {
            Constants.getErrMsg(result, false);
            return result;
        }
        String userId = super.getUserInfo().getId();
        point.setUserId(userId);
        int update = pointMapper.updateLocationAndAddress(point);
        Constants.getSuccMsg(result, update > 0);
        return result;
    }


    private Map<String, Object> getPointList(String param) {
        Map<String, Object> result = new HashMap<>();
        PageVo pageVo = JSONObject.parseObject(param, PageVo.class);//把前端传来的参数分装成分页类
        PageUtil pageUtil = pageVo.getPage();
        pageVo.setUserId(this.getUserInfo().getId());
        int totalNum = pointMapper.getPageTotal(pageVo);
        if (totalNum > 0) {
            List<Point> point = pointMapper.getPointList(pageVo);
            PageVo<Point> pageVo1 = new PageVo<>();
            pageVo1.setPage(new PageUtil(pageUtil.getPageIndex(), pageUtil.getPageSize(), totalNum));
            pageVo1.setDataList(point);
            Constants.getSuccMsg(result, pageVo1);
        }
        return result;
    }

    private Map<String,Object>getCollectDetail( String param){
        Map<String,Object> result  = new HashMap<>();
        JSONObject json=JSONObject.parseObject(param);
        String userId =super.getUserInfo().getId();
        String productId =json.getString("productId");
        Point p=pointMapper.getPointDetail(productId,userId);
        Constants.getSuccMsg(result,p);
        return result;
    }


}
