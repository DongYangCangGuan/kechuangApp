package com.cloud.servicemanage.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cloud.commonsmng.entity.appletEntity.Dictionary;
import com.cloud.commonsmng.entity.appletEntity.Member;
import com.cloud.servicemanage.common.PageVo;
import com.cloud.servicemanage.entity.HotFileInfo;
import com.cloud.servicemanage.mapper.HotFileInfoMapper;
import com.cloud.servicemanage.mapper.MemberMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class FileUploadService {

//    @Autowired
//    HotFileInfoMapper mapper;

    @Autowired
    private MemberMapper memberMapper;


//    public Integer InsertHotFile(List<HotFileInfo> fileInfos) {
//        return mapper.InsertHotFile(fileInfos);
//    }

    /**
     * 会员导出
     */
    public Map<String,Object> exportExcel(String membertype,String startTime,String endTime,String enterpriseName){
        Map<String,Object> result = new HashMap<>();

       // PageVo pageVo = JSONObject.parseObject(param,PageVo.class);
        PageVo pageVo = new PageVo();
        Map<String,String> searchdata = new HashMap<>();
        searchdata.put("memberType",membertype);
        searchdata.put("startTime",startTime);
        searchdata.put("endTime",endTime);
        searchdata.put("enterpriseName",enterpriseName);
        pageVo.setSearchdata(searchdata);
        //Map<String,String> searchdata = (Map<String, String>) pageVo.getSearchdata();
        List<Member> memberList = new ArrayList<>();
        String[] rowName = null;
        String title = null;
        List<Object[]> datalist = new ArrayList<>();
        if( membertype != null && !membertype.equals("")){
           // String membertype = searchdata.get("memberType");
            //Gp
            if(membertype.equals("1")){
                rowName = new String[] {"编号","机构编号","机构名称","机构类型","主要合伙人","联系电话","邮箱","密码",
                        "机构简称","科创投资基金","总部所在地点","上海是否有办公室","关注行业","关注阶段","投后项目经理"};
                memberList = memberMapper.exportMemberInfo(pageVo);
                title = "GP会员信息";
                //关注行业  attentionIndustry
                List<Dictionary> attentionIndustryMapList =  memberMapper.getDictionarysMap("attentionIndustry");
                Map<String, String> attentionIndustryMap = new HashMap<>();
                for (Dictionary kv : attentionIndustryMapList) {
                    String key = kv.getCode();
                    String value = kv.getName();
                    attentionIndustryMap.put(key, value);
                }
                //关注阶段  attentionStage
                List<Dictionary> attentionStageMapList = memberMapper.getDictionarysMap("attentionStage");
                Map<String, String> attentionStageMap = new HashMap<>();
                for (Dictionary kv : attentionStageMapList) {
                    String key = kv.getCode();
                    String value = kv.getName();

                    attentionStageMap.put(key, value);
                }
                for(int i = 0;i < memberList.size(); i++){
                    //关注行业
                    String attentionIndustry = memberList.get(i).getAttentionIndustryId();
                    if(attentionIndustry != null && !attentionIndustry.equals("")) {
                        JSONArray jsonArray = JSONArray.parseArray(attentionIndustry);
                        String[] attentionIndustrys = jsonArray.toArray(new String[]{});
                        String[] attentionIndustrysName = new String[attentionIndustrys.length];
                        for (int j = 0; j < attentionIndustrys.length; j++) {
                            if (attentionIndustryMap.get(attentionIndustrys[j]) != null && !attentionIndustryMap.get(attentionIndustrys[j]).equals("")) {
                                attentionIndustrysName[j] = attentionIndustryMap.get(attentionIndustrys[j]);
                            }
                        }
                        String attentionIndustryName = Arrays.toString(attentionIndustrysName);
                        String attentionIndustryName1 = attentionIndustryName.substring(attentionIndustryName.indexOf("[") + 1, attentionIndustryName.lastIndexOf("]"));
                        memberList.get(i).setAttentionIndustry(attentionIndustryName1);
                    }
                    //关注阶段
                    String attentionStage = memberList.get(i).getAttentionStageId();
                    if(attentionStage != null && !attentionStage.equals("")) {
                        JSONArray jsonArray = JSONArray.parseArray(attentionStage);
                        String[] attentionStages = jsonArray.toArray(new String[]{});
                        String[] attentionStagesname = new String[attentionStages.length];
                        for (int j = 0; j < attentionStages.length; j++) {
                            if (attentionStageMap.get(attentionStages[j]) != null && !attentionStageMap.get(attentionStages[j]).equals("")) {
                                attentionStagesname[j] = attentionStageMap.get(attentionStages[j]);
                            }
                        }
                        String attentionStagename = Arrays.toString(attentionStagesname);
                        String attentionStagename1 = attentionStagename.substring(attentionStagename.indexOf("[") + 1, attentionStagename.lastIndexOf("]"));
                        memberList.get(i).setAttentionStage(attentionStagename1);
                    }
                    datalist.add(memberList.get(i).getGPInformation());
                }
            }
            //科创
            if(membertype.equals("2")){
                memberList = memberMapper.getMemberInfoPageVo(pageVo);

                rowName = new String[] {"编号","机构编号","机构名称","机构类型","合伙人","联系电话","地址","邮箱","密码"};

                title = "科创会员信息";
                for(int i = 0;i < memberList.size(); i++){
                    datalist.add(memberList.get(i).getkechuangInformation());
                }
            }
            //创业公司
            if(membertype.equals("5")){
                rowName = new String[] {"编号","机构编号","项目法定名称","机构类型","主要合伙人","联系电话","邮箱","密码",
                        "项目简称","投资基金","科创行业分类","科创直投分类","犀牛标签","项目注册地",
                        "项目经营地","投后项目经理","首次投资阶段","是否领投","首次投资时间"};
                memberList = memberMapper.exportMemberInfo(pageVo);
                title = "创业公司会员信息";
                //犀牛标签
                List<Dictionary> labelsMaplist = memberMapper.getDictionarysMap("rhinocerosLabel");
                Map<String, String> labelsMap = new HashMap<>();
                for (Dictionary kv : labelsMaplist) {
                    String key = kv.getCode();
                    String value = kv.getName();
                    labelsMap.put(key, value);
                }
                for(int i = 0;i < memberList.size(); i++){
                    //犀牛标签
                    String label = memberList.get(i).getRhinocerosLabelId();
                    if(label != null && !label.equals("")) {
                        JSONArray jsonArray = JSONArray.parseArray(label);
                        String[] labels = jsonArray.toArray(new String[]{});
                        String[] labelsName = new String[labels.length];
                        for (int j = 0; j < labels.length; j++) {
                            if (labelsMap.get(labels[j]) != null && !labelsMap.get(labels[j]).equals("")) {
                                labelsName[j] = labelsMap.get(labels[j]);
                            }
                        }
                        String labelname = Arrays.toString(labelsName);
                        String labelname1 = labelname.substring(labelname.indexOf("[") + 1, labelname.lastIndexOf("]"));
                        memberList.get(i).setRhinocerosLabel(labelname1);
                    }
                    if(memberList.get(i).getInvestmentFundName()!=null && !memberList.get(i).getInvestmentFundName().equals("")){
                        memberList.get(i).setInvestmentFund(memberList.get(i).getInvestmentFundName());
                    }
                    datalist.add(memberList.get(i).getcygsInformation());
                }
            }
            //lp
            if(membertype.equals("3")){
                memberList = memberMapper.exportMemberInfo(pageVo);

                rowName = new String[] {"编号","机构编号","LP全称","LP简称","机构类型","联系人","职位","联系电话","邮箱","密码","参与科创基金"};

                title = "LP会员信息";
                //lp 参与科创基金
                List<Dictionary> participationFundMapList = memberMapper.getDictionarysMap("participationFund");
                Map<String, String> participationFundMap = new HashMap<>();
                for (Dictionary kv : participationFundMapList) {
                    String key = kv.getCode();
                    String value = kv.getName();

                    participationFundMap.put(key, value);
                }
                for(int i = 0;i < memberList.size(); i++){
                    //参与科创基金
                    String participationFundId = memberList.get(i).getParticipationFundId();
                    if(participationFundId != null && !participationFundId.equals("")) {
                        JSONArray jsonArray = JSONArray.parseArray(participationFundId);
                        String[] participationFundIds = jsonArray.toArray(new String[]{});
                        String[] participationFund = new String[participationFundIds.length];
                        for (int j = 0; j < participationFundIds.length; j++) {
                            if (participationFundMap.get(participationFundIds[j]) != null && !participationFundMap.get(participationFundIds[j]).equals("")) {
                                participationFund[j] = participationFundMap.get(participationFundIds[j]);
                            }
                        }
                        String participationFundname = Arrays.toString(participationFund);
                        String participationFundname1 = participationFundname.substring(participationFundname.indexOf("[") + 1, participationFundname.lastIndexOf("]"));
                        memberList.get(i).setParticipationFund(participationFundname1);
                    }
                    datalist.add(memberList.get(i).getlpInformation());
                }
            }
            //供应商
            if(membertype.equals("4")){
                memberList = memberMapper.getMemberInfoPageVo(pageVo);

                rowName = new String[] {"编号","机构编号","机构名称","机构类型","合伙人","联系电话","地址","邮箱","密码"};

                title = "供应商会员信息";
                for(int i = 0;i < memberList.size(); i++){
                    datalist.add(memberList.get(i).getkechuangInformation());
                }
            }
        }else{
             memberList = memberMapper.getMemberInfoPageVo(pageVo);

            rowName = new String[] {"编号","机构编号","机构名称","机构类型","合伙人","联系电话","地址","邮箱"};

             title = "会员基本信息";
            for(int i = 0;i < memberList.size(); i++){
                datalist.add(memberList.get(i).getBasicInformation());
            }

        }
        result.put("data",datalist);
        result.put("rowName",rowName);
        result.put("title",title);
        return result;

    }

    /**
     * 获取静态文件地址
     */
    public String getAddress(String kind){
        List<Dictionary> ds = memberMapper.getMemberTypelist(kind);
        String address = ds.get(0).getName();
        return address;
    }

}
