package com.cloud.servicemanage.service;

import com.alibaba.fastjson.JSONObject;
import com.cloud.commonsmng.constants.Constants;
import com.cloud.commonsmng.factory.BaseService;
import com.cloud.servicemanage.common.ConstantUtil;
import com.cloud.servicemanage.common.UUIDGenerator;
import com.cloud.servicemanage.entity.Department;
import com.cloud.commonsmng.entity.BaseUserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service("Dept")
public class DepartmentService extends BaseService {

    @Autowired
    public com.cloud.servicemanage.mapper.DepartmentMapper departmentMapper;
    @Autowired
    public com.cloud.servicemanage.mapper.StaffMapper staffMapper;

    public Map<String, Object> exec(String method, String param) {
        Map<String, Object> obj = new HashMap<String, Object>();
        switch (method) {
            case ConstantUtil.GET_DEPT_INFO_LIST: //getDeptInfoList
                obj = getDeptInfoList(param);
                break;
            case ConstantUtil.GET_DEPT_INFO_BY_DEPTID:
                obj = getDeptInfoByDeptId(param);
                break;
            case ConstantUtil.GET_SUB_DEPTS_BY_DEPTID:
                obj = getSubDeptsByDeptId(param);
                break;
            case ConstantUtil.IS_DEPT_CODE_EXISTED:
                obj = isDeptCodeExisted(param);
                break;
            case ConstantUtil.ADD_DEPT_INFO:
                obj = addDeptInfo(param);
                break;
            case ConstantUtil.EDIT_DEPT_INFO:
                obj = editDeptInfo(param);
                break;
            case ConstantUtil.GET_DEPT_TREE_BY_PARENTID:
                obj = getDeptTreeByParentId(param);
                break;
            default:
                break;
        }

        return obj;
    }

    /**
     * 获取全部机构信息
     * author tjs 2021-07-06
     *
     * @param param
     * @return
     */
    private Map<String, Object> getDeptInfoList(String param) {
        Map<String, Object> result = new HashMap<>();
        List<Department> deptInfoList = departmentMapper.getDeptInfoList();
        // 拼装返回结果
        Constants.getSuccMsg(result, deptInfoList);
        return result;
    }

    /**
     * 根据机构Id获取该机构信息
     *
     * @param param
     * @return
     */
    private Map<String, Object> getDeptInfoByDeptId(String param) {
        Map<String, Object> result = new HashMap<String, Object>();

        // 解析前端json获取sql查询条件
        JSONObject jsonObject = JSONObject.parseObject(param);
        String deptId = jsonObject.getString("id");

        // 执行sql查询（mybatis）
        Department dept = departmentMapper.getDeptInfoByDeptId(deptId);
        // 拼装返回结果
        Constants.getSuccMsg(result, dept);

        return result;
    }

    /**
     * 根据机构Id获取其下级机构列表
     *
     * @param param
     * @return
     */
    private Map<String, Object> getSubDeptsByDeptId(String param) {
        Map<String, Object> result = new HashMap<String, Object>();

        // 解析前端json获取sql查询条件
        JSONObject jsonObject = JSONObject.parseObject(param);
        String parentid = jsonObject.getString("id");
        String isused = jsonObject.getString("isused");

        // 调用mybatis查询数据库
        List<Department> subDepts = departmentMapper.getSubDeptsByDeptId(parentid, isused);
        // 拼装返回前端的数据
        Constants.getSuccMsg(result, subDepts);

        return result;
    }

    /**
     * 判断前端传入的部门机构Code是否重复
     *
     * @param param
     * @return
     */
    private Map<String, Object> isDeptCodeExisted(String param) {
        Map<String, Object> result = new HashMap<String, Object>();

        // 解析前端json获取sql查询条件
        JSONObject jsonObject = JSONObject.parseObject(param);
        String id = jsonObject.getString("id");
        String code = jsonObject.getString("code");

        // 判断用户输入的机构编号是否已经存在（已经停用的机构也算）
        boolean isExisted = departmentMapper.isDeptCodeExisted(id, code);
        if (isExisted) {
            Constants.getSuccMsg(result, Constants.RESULTCODE_REPEAT);
        } else {
            Constants.getSuccMsg(result, Constants.RESULT_NO_DATA);
        }

        return result;
    }

    /**
     * 新增机构信息
     *
     * @param param
     * @return
     */
    private Map<String, Object> addDeptInfo(String param) {
        Map<String, Object> result = new HashMap<String, Object>();

        JSONObject jsonObject = JSONObject.parseObject(param);
        Department dept = JSONObject.parseObject(param, Department.class);
        dept.setId(UUIDGenerator.getUUID());    // 使用UUID作为主键
        dept.setCreatorid(super.getUserInfo().getId());
        dept.setModifierid(super.getUserInfo().getId());

        int rows = departmentMapper.addDeptInfo(dept);
        if (rows == 1) {
            Constants.getSuccMsg(result, Constants.RESULTCODE_SUCC);
        } else {
            Constants.getErrMsg(result, Constants.RESULT_NO_DATA);
        }

        return result;
    }

    /**
     * 修改机构信息：
     * <p>
     * 1、上级机构发生变动
     * 2、机构状态发生变动
     *
     * @param param
     * @return
     */
    private Map<String, Object> editDeptInfo(String param) {
        Map<String, Object> result = new HashMap<String, Object>();
        JSONObject jsonObject = JSONObject.parseObject(param);
        String id = jsonObject.getString("id");
        String parentId = jsonObject.getString("parentid");
        boolean isused = jsonObject.getBoolean("isused");
        String treeabout = jsonObject.getString("treeabout");
        BaseUserInfo userInfo = this.getUserInfo();
        String modifierid = userInfo.getId();
        // 获取更新前的机构信息
        Department deptOld = departmentMapper.getDeptInfoByDeptId(id);
        // 1、上级机构发生变动
        if (!deptOld.getParentid().equals(parentId)) {
            // 1）判断当前机构不能变动成为自己的子机构
            Department newParentDept = departmentMapper.getDeptInfoByDeptId(parentId);
            if (newParentDept.getTreeabout().contains(treeabout)) {
                Constants.getSuccMsg(result, Constants.RESULTCODE_REPEAT);
                return result;
            }
        }
        // 2、机构状态发生变动
        if (deptOld.isIsused() ^ isused) {
            // 1）判断机构下是否存在已启用的子机构
            List<Department> depts = departmentMapper.getSubDeptsByDeptId(id, "1");
            if (depts.size() > 0) {
                Constants.getSuccMsg(result, Constants.HAS_DEPART);
                return result;
            }
            // 2）判断机构下是否存在已启用的用户
            int count = staffMapper.getStaffSum(id, "1", true);
            if (count > 0) {
                Constants.getSuccMsg(result, Constants.HAS_PERSON);
                return result;
            }
        }

        // 更新机构信息
        Department deptNew = JSONObject.parseObject(param, Department.class);
        deptNew.setModifierid(super.getUserInfo().getId());
        int rows = departmentMapper.updateDeptInfo(deptNew);
        if (rows == 1) {
            Constants.getSuccMsg(result, Constants.RESULTCODE_SUCC);
        } else {
            Constants.getSuccMsg(result, Constants.RESULTCODE_ERR);
        }

        return result;
    }

    /**
     * 获取下拉框类型的机构树
     *
     * @param param
     * @return
     */
    private Map<String, Object> getDeptTreeByParentId(String param) {
        Map<String, Object> result = new HashMap<String, Object>();
        JSONObject jsonObject = JSONObject.parseObject(param);
        String id = jsonObject.getString("id");

        List<Department> depts = departmentMapper.getDeptTreeByParentId(id);
        if (depts.size() > 0) {
            Constants.getSuccMsg(result, depts);
        } else {
            Constants.getSuccMsg(result, Constants.RESULT_NO_DATA);
        }
        return result;
    }
}
