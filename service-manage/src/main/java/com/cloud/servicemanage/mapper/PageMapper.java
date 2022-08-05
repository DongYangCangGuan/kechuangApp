package com.cloud.servicemanage.mapper;

import com.cloud.servicemanage.entity.RequestParamPageVo;
import com.cloud.servicemanage.entity.TmpPage;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author fyy
 */
@Mapper
public interface PageMapper {
    //查询总条数
    public int getPageTotal(RequestParamPageVo tmpPageVo);
    //查询列表
    public List<TmpPage> qryPages(RequestParamPageVo tmpPageVo);
    //更新页面信息
    void updatePage(TmpPage tmpPage);
    //页面启用禁用
    void updateStatus(TmpPage tmpPage);
    //新增页面信息
    void addPage(TmpPage tmpPage);
}
