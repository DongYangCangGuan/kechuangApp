package com.cloud.servicemanage.service;

import com.alibaba.fastjson.JSONObject;
import com.cloud.commonsmng.constants.Constants;
import com.cloud.commonsmng.factory.BaseService;
import com.cloud.servicemanage.common.ConstantUtil;
import com.cloud.servicemanage.common.PageUtil;
import com.cloud.servicemanage.common.PageVo;
import com.cloud.commonsmng.entity.appletEntity.Dictionary;
import com.cloud.servicemanage.mapper.DictionaryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("Dictionary")
public class DictionaryService extends BaseService {

    @Autowired
    DictionaryMapper dictionary;

    @Override
    public Map<String, Object> exec(String method, String param) {
        Map<String, Object> obj = new HashMap<String, Object>();
        switch (method) {
            case ConstantUtil.GET_Dictionary_BASE_MSG:
                obj = getDictionaryInfo(param);
                break;
            case ConstantUtil.GETKIND_Dictionary_CODE_BASEMSG:
                obj = getKindnameInfo();
                break;
            case ConstantUtil.ADD_Dictionary_BASE_MSG:
                obj = addDictionary(param);
                break;
            case ConstantUtil.UPDATE_Dictionary_BASE_MSG:
                obj = updateDictionary(param);
                break;
            case ConstantUtil.DELETE_Dictionary_BASEMSG:
                obj = deleteDictionary(param);
                break;
            case ConstantUtil.ADD_classDictionary_BASE_MSG:
                obj = addclassDictionary(param);
                break;
            case ConstantUtil.SelectIdDictionary_BASE_MSG:
                obj = selectId(param);
                break;
            case ConstantUtil.SelectNameDictionary_BASE_MSG:
                obj = selectName(param);
                break;
            case ConstantUtil.SelectCodeDictionary_BASE_MSG:
                obj = selectCode(param);
                break;
            case ConstantUtil.SelectName1Dictionary_BASE_MSG:
                obj = selectName1(param);
                break;
            case ConstantUtil.SelectCode1Dictionary_BASE_MSG:
                obj = selectCode1(param);
                break;
            case ConstantUtil.SESECT_BY_KIND:
                obj = selectByKind(param);
                break;
            case ConstantUtil.GET_DICTIONARY_BY_ID: //getDictionaryById
                obj = getDictionaryById(param);
                break;
            case ConstantUtil.SELECT_BY_PARENT:
                obj = selectByParent(param);
            default:
                break;
        }

        return obj;
    }


    /*
     * 根据id查询字典信息
     * author: tjs
     * 2021-06-24 add
     */
    private Map<String, Object> getDictionaryById(String param) {
        Map<String, Object> result = new HashMap<>();
        String id = JSONObject.parseObject(param).getString("id");
        Dictionary dictionaryById = dictionary.getDictionaryById(id);
        Constants.getSuccMsg(result, dictionaryById);
        return result;
    }

    /**
     * 获取字典信息
     *
     * @return
     */
    private Map<String, Object> getDictionaryInfo(String param) {
        Map<String, Object> result = new HashMap<String, Object>();
        //把前端传过来的数据转化为object类型
        PageVo pageVo = JSONObject.parseObject(param, PageVo.class);
        //获取前端的页面分页信息
        PageUtil pageUtil = pageVo.getPage();
        try {
            //调用rolemapper对应的总页数查询方法，吧前端获取到的数据当参数传入，得到返回结果totalNum总页数
            int totalNum = dictionary.getPageTotal(pageVo);
            if (totalNum > 0) {
                List<Dictionary> InfoList = dictionary.getDictionaryInfo(pageVo);
                //返回参数
                //然后在使用pagevo类里面的datalist数组存放返回来的数组
                //在把roleBeanBaseMsgList 放到pagevo类(该类存放了page类的引用和datalist)里面的list中
                PageVo<Dictionary> pageVo1 = new PageVo<>();
                pageVo1.setPage(new PageUtil(pageUtil.getPageIndex(), pageUtil.getPageSize(), totalNum));
                pageVo1.setDataList(InfoList);
                Constants.getSuccMsg(result, pageVo1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    //获取下拉菜单选项
    private Map<String, Object> getKindnameInfo() {
        Map<String, Object> result = new HashMap<String, Object>();
        // 执行sql查询（mybatis）
        List<Dictionary> kindnameInfoList = dictionary.getKindnameInfo();
        // 拼装返回前端的数据
        result.put("code", 200);
        result.put("msg", "succ");
        result.put("data", kindnameInfoList);
        return result;
    }

    //增加
    private Map<String, Object> addDictionary(String param) {
        Map<String, Object> result = new HashMap<>();
        Dictionary dictionary1 = JSONObject.parseObject(param, Dictionary.class);
        super.insertBaseInfo(dictionary1);
        int i = dictionary.addDictionary(dictionary1);
        if (i > 0) {
            Constants.getSuccMsg(result, Constants.RESULTCODE_SUCC);
        } else {
            Constants.getSuccMsg(result, Constants.RESULTCODE_REPEAT);
        }
        return result;
    }

    //增加大类
    private Map<String, Object> addclassDictionary(String param) {
        Map<String, Object> result = new HashMap<>();
        Dictionary dictionary1 = JSONObject.parseObject(param, Dictionary.class);
        super.insertBaseInfo(dictionary1);
        int i = dictionary.addclassDictionary(dictionary1);
        if (i > 0) {
            Constants.getSuccMsg(result, Constants.RESULTCODE_SUCC);
        } else {
            Constants.getSuccMsg(result, Constants.RESULTCODE_REPEAT);
        }
        return result;
    }

    private Map<String, Object> updateDictionary(String param) {
        Map<String, Object> result = new HashMap<>();
        Dictionary bean = JSONObject.parseObject(param, Dictionary.class);
        super.updateBaseInfo(bean, bean.getId());
        int i = dictionary.updateDictionary(bean);

        if (i > 0) {
            Constants.getSuccMsg(result, Constants.RESULTCODE_SUCC);
        } else {
            Constants.getErrMsg(result, Constants.RESULT_NO_DATA);
        }

        return result;
    }

    private Map<String, Object> deleteDictionary(String param) {
        Map<String, Object> result = new HashMap<>();
        JSONObject jsonObject = JSONObject.parseObject(param);
        String id = jsonObject.getString("id");
        int i = dictionary.deleteDictionary(id);
        if (i > 0) {
            Constants.getSuccMsg(result, Constants.RESULTCODE_SUCC);
        } else {
            Constants.getSuccMsg(result, Constants.RESULTCODE_REPEAT);
        }


        return result;
    }

    //查询大类名字重复
    private Map<String, Object> selectName(String param) {
        Map<String, Object> result = new HashMap<>();
        JSONObject jsonObject = JSONObject.parseObject(param);
        //String id=jsonObject.getString("id");
        String name = jsonObject.getString("name");
        try {
            int i = dictionary.selectName(name);
            if (i > 0) {

                Constants.getSuccMsg(result, Constants.RESULTCODE_SUCC);
            } else {
                Constants.getSuccMsg(result, Constants.RESULTCODE_REPEAT);
            }

        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    //查询大类code编码重复
    private Map<String, Object> selectCode(String param) {
        Map<String, Object> result = new HashMap<>();
        JSONObject jsonObject = JSONObject.parseObject(param);
        String code = jsonObject.getString("code");

        try {
            int i = dictionary.selectCode(code);
            if (i > 0) {

                Constants.getSuccMsg(result, Constants.RESULTCODE_SUCC);
            } else {
                Constants.getSuccMsg(result, Constants.RESULTCODE_REPEAT);
            }

        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    //查询名字重复
    private Map<String, Object> selectName1(String param) {
        Map<String, Object> result = new HashMap<>();
        JSONObject jsonObject = JSONObject.parseObject(param);
        //String id=jsonObject.getString("id");
        String name = jsonObject.getString("name");
        try {
            int i = dictionary.selectName1(name);
            if (i > 0) {

                Constants.getSuccMsg(result, Constants.RESULTCODE_SUCC);
            } else {
                Constants.getSuccMsg(result, Constants.RESULTCODE_REPEAT);
            }

        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    //查询code编码重复
    private Map<String, Object> selectCode1(String param) {
        Map<String, Object> result = new HashMap<>();
        JSONObject jsonObject = JSONObject.parseObject(param);
        String code = jsonObject.getString("code");

        try {
            int i = dictionary.selectCode1(code);
            if (i > 0) {

                Constants.getSuccMsg(result, Constants.RESULTCODE_SUCC);
            } else {
                Constants.getSuccMsg(result, Constants.RESULTCODE_REPEAT);
            }

        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    //查询ID重复
    private Map<String, Object> selectId(String param) {
        Map<String, Object> result = new HashMap<>();
        JSONObject jsonObject = JSONObject.parseObject(param);
        String id = jsonObject.getString("id");
        if (id == null || id.isEmpty()) {
            id = "-99999";
        }
        try {
            int i = dictionary.selectId(id);
            if (i > 0) {

                Constants.getSuccMsg(result, Constants.RESULTCODE_SUCC);
            } else {
                Constants.getSuccMsg(result, Constants.RESULTCODE_REPEAT);
            }

        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", e.getMessage());
            e.printStackTrace();
        }
        return result;
    }


    //根据kind查询
    private Map<String, Object> selectByKind(String param) {
        Map<String, Object> result = new HashMap<>();
        JSONObject jsonObject = JSONObject.parseObject(param);
        String kind = jsonObject.getString("kind");

        try {
            List<Dictionary> dictionaryList = dictionary.selectByKind(kind);
            if (dictionaryList != null) {
                result.put("code", 200);
                result.put("msg", "succ");
                result.put("data", dictionaryList);
            }
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    //根据Parent查询
    private Map<String, Object> selectByParent(String param) {
        Map<String, Object> result = new HashMap<>();
        JSONObject jsonObject = JSONObject.parseObject(param);
        String parentId = jsonObject.getString("parentId");
        String kind = jsonObject.getString("kind");

        try {
            List<Dictionary> dictionaryList = dictionary.selectByParent(parentId,kind);
            if (dictionaryList!= null) {
                result.put("code", 200);
                result.put("msg", "succ");
                result.put("data", dictionaryList);
            }
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", e.getMessage());
        }
        return result;
    }

}
