<template>
    <div class="staffDiv" v-loading="loading.form">
        <el-container>
            <el-header>
                <span class="header">修改人员信息</span>
            </el-header>

            <el-main>
                <div>
                    <div style="display: inline-block;float: left;width: 20%">
                        <el-form>
                            <el-form-item label="上传头像">
                                <el-upload
                                    ref="upload"
                                    class="avatar-uploader"
                                    :class="dynamicUploader"
                                    action="https://jsonplaceholder.typicode.com/posts/"
                                    :on-remove="OnRemove"
                                    list-type="picture-card"
                                    :limit=1
                                    :file-list="files"
                                    :http-request="myUpload"
                                    :before-upload="beforeAvatarUpload">
                                    <img v-if="ImageUrl?true:false" :src="ImageUrl" width="100%" height="100%">
                                    <i v-else class="el-icon-plus"></i>
                                </el-upload>
                            </el-form-item>
                        </el-form>
                    </div>

                    <div style="display: inline-block;width: 80%">
                        <el-form ref="editStaff" :model="editStaff" :rules="rules" label-width="100px">

                            <el-row class="panel-group" :gutter="24">

                                <el-col :span="8">
                                    <el-form-item label="姓名" prop="name">
                                        <el-input style="width: 100%" v-model="editStaff.name" :maxlength="8"
                                                  placeholder="请输入姓名.."></el-input>
                                    </el-form-item>
                                </el-col>

                                <el-col :span="8">
                                    <el-form-item label="员工工号" prop="empid">
                                        <el-input style="width: 100%" v-model="editStaff.empid" :maxlength="8"
                                                  placeholder="请输入员工工号.."></el-input>
                                    </el-form-item>
                                </el-col>

                                <el-col :span="8">
                                    <el-form-item label="角色" prop="role">
                                        <el-select v-model="editStaff.role" multiple placeholder="请选择"
                                                   style="width: 100%" collapse-tags>
                                            <el-option v-for="item in options" :key="item.id" :label="`${item.name}`"
                                                       :value="item.id"></el-option>
                                        </el-select>
                                    </el-form-item>
                                </el-col>

                            </el-row>

                            <el-row class="panel-group" :gutter="24">

                                <el-col :span="8">
                                    <el-form-item label="登录名" prop="loginname">
                                        <el-input style="width: 100%" v-model="editStaff.loginname" :maxlength="10"
                                                  placeholder="请输入登录名.."></el-input>
                                    </el-form-item>
                                </el-col>

                                <el-col :span="8">
                                    <el-form-item label="职务" prop="job">
                                        <el-input style="width: 100%" v-model="editStaff.job" :maxlength="10"
                                                  placeholder="请输入职务.."></el-input>
                                    </el-form-item>
                                </el-col>

                                <!--<el-col :span="8">
                                    <el-form-item label="性别" prop="sex">
                                        <el-select v-model="editStaff.sex" placeholder="请选择" style="width: 100%">
                                            <el-option v-for="item in options1" :key="item.id" :label="item.name"
                                                       :value="item.code">
                                            </el-option>
                                        </el-select>
                                    </el-form-item>
                                </el-col>-->

                            </el-row>

                            <el-row class="panel-group" :gutter="24">

                                <el-col :span="8">
                                    <el-form-item label="电话号码" prop="mobile">
                                        <el-input style="width: 100%" v-model="editStaff.mobile" :maxlength="11"
                                                  :minlength="11" placeholder="请输入手机号码.."></el-input>
                                    </el-form-item>
                                </el-col>

                                <el-col :span="8">
                                    <el-form-item label="邮箱" prop="email">
                                        <el-input style="width: 100%" v-model="editStaff.email" :maxlength="100"
                                                  placeholder="请输入邮箱.."></el-input>
                                    </el-form-item>
                                </el-col>

                                <el-col :span="8">
                                    <el-form-item label="生日" prop="birthday">
                                        <el-date-picker style="width: 100%" v-model="editStaff.birthday" type="date"
                                                        value-format="yyyy-MM-dd" placeholder="选择日期"></el-date-picker>
                                    </el-form-item>
                                </el-col>

                            </el-row>

                            <el-row class="panel-group" :gutter="24">

                                <el-col :span="8">
                                    <el-form-item label="所在部门" prop="departmentid">
                                        <el-button type="primary" icon="el-icon-plus" plain
                                                   @click="dialogVisibleDepart"></el-button>
                                    </el-form-item>
                                </el-col>

                                <!--<el-col :span="8">
                                    <el-form-item label="选择学历" prop="eduid">
                                        <el-button type="primary" icon="el-icon-plus" plain
                                                   @click="dialogVisibleEdu"></el-button>
                                    </el-form-item>
                                </el-col>-->

                                <el-col :span="8">
                                    <el-form-item label="是否停用">
                                        <el-switch v-model="isused" active-color="#13ce66" inactive-color="#ff4949"
                                                   @change="deleteInfo"></el-switch>
                                        {{ isused ? "使用" : "停用" }}
                                    </el-form-item>
                                </el-col>

                            </el-row>

                        </el-form>
                    </div>

                    <div>
                        <el-row>
                            <el-col>
                                <el-tabs type="border-card">
                                    <el-tab-pane label="部门经历">
                                        <el-table :data="editStaff.experienceList" border fit highlight-current-row
                                                  style="width: 100%">

                                            <el-table-column align="center" label="部门"
                                                             prop="departmentname"></el-table-column>

                                            <el-table-column align="center" label="时间"
                                                             prop="entrytime"></el-table-column>

                                            <el-table-column align="center" label="类型"
                                                             prop="userstatusname"></el-table-column>
                                        </el-table>
                                    </el-tab-pane>

                                    <el-tab-pane label="学历">
                                        <el-table :data="editStaff.educationList" border fit highlight-current-row
                                                  style="width: 100%">

                                            <el-table-column align="center" label="学历"
                                                             prop="educationname"></el-table-column>

                                            <el-table-column align="center" label="时间" prop="eduTime"></el-table-column>

                                            <el-table-column align="center" label="学位" prop="major"></el-table-column>
                                        </el-table>
                                    </el-tab-pane>
                                </el-tabs>
                            </el-col>
                        </el-row>
                    </div>

                    <el-dialog title="部门" :visible.sync="showDepart" :modal-append-to-body='false'>
                        <div>
                            <el-form label-width="80px" :model="DepartInfo">
                                <el-form-item label="选择部门">
                                    <treeselect
                                        style="width: 50%"
                                        @select="select"
                                        :load-options="loadOptions"
                                        :options="SelectTreeData"
                                        placeholder="上级机构"
                                        :searchable="false"
                                        :clearable="false"
                                        :normalizer="normalizer"
                                        v-model="treeselectvalue"
                                    />
                                </el-form-item>

                                <el-form-item label="时间">
                                    <el-date-picker style="width: 50%" v-model="DepartInfo.entrytime" type="date"
                                                    value-format="yyyy-MM-dd" placeholder="选择日期"></el-date-picker>
                                </el-form-item>

                                <el-form-item label="类型">
                                    <el-select placeholder="请选择" style="width: 50%" v-model="DepartInfo.userstatus"
                                               @change="inputType">
                                        <el-option v-for="item in options3" :key="item.id" :label="item.name"
                                                   :value="item.code">
                                        </el-option>
                                    </el-select>
                                </el-form-item>

                            </el-form>
                        </div>
                        <el-button @click="cancelDialogVisibleDepart">取 消</el-button>
                        <el-button type="primary" @click="departButton">确 定</el-button>
                    </el-dialog>

                   <!-- <el-dialog title="学历" :visible.sync="showEdu" :modal-append-to-body='false'>
                        <div>
                            <el-form label-width="80px" :model="EduInfo">
                                <el-form-item label="学历">
                                    <el-select style="width: 50%" placeholder="请选择" v-model="EduInfo.education"
                                               id="eduSelect" @change="inputEdu">
                                        <el-option v-for="item in options2" :key="item.id" :label="item.name"
                                                   :value="item.id"></el-option>
                                    </el-select>
                                </el-form-item>

                                <el-form-item label="时间">
                                    <el-date-picker
                                        style="width: 50%"
                                        v-model="EduInfo.eduTime"
                                        type="daterange"
                                        value-format="yyyy-MM-dd"
                                        start-placeholder="开始日期"
                                        end-placeholder="结束日期">
                                    </el-date-picker>
                                </el-form-item>

                                <el-form-item label="专业">
                                    <el-input style="width: 50%" :maxlength="100" placeholder="请输入专业.."
                                              v-model="EduInfo.major"></el-input>
                                </el-form-item>

                            </el-form>

                        </div>
                        <el-button @click="cancelDialogVisibleEdu">取 消</el-button>
                        <el-button type="primary" @click="eduButton">确 定</el-button>
                    </el-dialog>-->


                </div>
            </el-main>

            <el-footer>
                <el-button type="info" icon="el-icon-back" plain @click="editBackClick">返回</el-button>
                <el-button :loading="loading.btn" type="success" icon="el-icon-check" plain @click="editStaffInfoClick">
                    保存
                </el-button>
            </el-footer>

        </el-container>

    </div>
</template>

<script>
import Treeselect from '@riophae/vue-treeselect'
import '@riophae/vue-treeselect/dist/vue-treeselect.css'

export default {
    name: "EditStaff",
    components: {Treeselect},
    props: {
        //子页面调用父页面
        onBack: {
            type: Function,
            default: null
        }
    },

    data() {
        var checkID = async (rule, value, callback) => {
            if (!(/^[1-9]\d*$/.test(value))) {
                return callback(new Error('请输入员工编号'))
            } else {
                // 提交后台验证员工编号是否存在
                const params = {
                    className: 'Staff',
                    method: 'isStaffIdExisted',
                    param: {
                        empid: this.editStaff.empid,
                        id: this.editStaff.id
                    }
                };
                const res = await this.$store.dispatch('http/post', params);
                if (res === '203') {
                    return callback(new Error('员工编号已存在！'))
                } else {
                    return callback()
                }
            }
        };
        var checkLoginName = async (rule, value, callback) => {
            if (!value) {
                return callback(new Error('请输入登录名'))
            } else {
                // 提交后台验证员工登录名是否存在
                const params = {
                    className: 'Staff',
                    method: 'isStaffLoginNameExisted',
                    param: {
                        loginname: this.editStaff.loginname,
                        id: this.editStaff.id
                    }
                };
                const res = await this.$store.dispatch('http/post', params);
                if (res === '203') {
                    return callback(new Error('登录名重复！'))
                } else {
                    return callback()
                }
            }
        };
        var checkPhone = async (rule, value, callback) => {
            if (!(/^1[34578]\d{9}$/.test(value))) {
                callback(new Error('请输入正确的手机号码!'));
            }
        };
        var email = async (rule, value, callback) => {
            if (!(/\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*/.test(value))) {
                callback(new Error('请输入正确的邮箱格式!'));
            }
        };
        return {
            treeselectvalue: null,
            isused: true,
            showDepart: false,
            showEdu: false,
            SelectTreeData: [],
            fileList: [],
            ImageUrl: '',
            options: [],
            editStaff: {
                id: '',
                name: '',
                birthday: '',
                departmentid: '',
                sex: '',
                job: '',
                empid: '',
                loginname: '',
                mobile: '',
                email: '',
                pic: '',
                role: [],
                roles: [],
                experienceList: [],
                educationList: [],
                eduid: '',
            },
            DepartInfo: {
                departmentname: '',
                departmentid: '',
                entrytime: '',
                userstatus: '',
                userstatusname: '',
            },
            EduInfo: {
                eduTime: '',
                major: '',
                education: '',
                educationname: '',
            },
            rules: {
                name: [
                    {required: true, message: '请输入姓名', trigger: 'blur'}
                ],
                // birthday: [
                //   {required: true, message: '请输入生日', trigger: 'blur'}
                // ],
                loginname: [
                    {required: true, validator: checkLoginName, trigger: 'blur'}
                ],
                empid: [
                    {required: true, validator: checkID, trigger: 'blur'}
                ],
                // mobile: [
                //   {required: true, validator: checkPhone, trigger: 'blur'}
                // ],
                // email: [
                //   {required: true, validator: email, trigger: 'blur'}
                // ],
                departmentid: [
                    {required: true, message: '请选择部门', trigger: 'blur'}
                ],
                // sex: [
                //   {required: true, message: '请选择性别', trigger: 'blur'}
                // ],
                // job: [
                //   {required: true, message: '请输入职务', trigger: 'blur'}
                // ],
                role: [
                    {required: true, message: '请选择至少一个角色', trigger: 'blur'}
                ],
            },
            options1: [],
            options2: [],
            options3: [],
            value: '',
            value1: '',
            loading: {
                form: false,
                btn: false
            },
            normalizer(node) {  // 自定义下拉树节点模板
                return {
                    id: node.id,
                    label: node.name,
                    children: JSON.stringify(node.children) === '[]' ? '' : node.children
                }
            },
            files: [],
            dynamicUploader: ''
        }
    },
    methods: {
        // // 打印测试按钮
        // test:function(){
        //   console.log(this.editStaff);
        // },

        // 动态加载子树
        async loadOptions({action, parentNode, callback}) {
            if (action === 'LOAD_CHILDREN_OPTIONS') {
                const params = {
                    className: 'Dept',
                    method: 'getSubDeptsByDeptId',
                    param: {
                        id: parentNode.id,
                        isused: '1'
                    }
                };
                let children = await this.$store.dispatch('http/post', params);
                parentNode.children = JSON.stringify(children) === '[]' ? '' : children;  // 当选中节点展开时没有子节点，设置其子节点为''
                // 回调
                callback()
            }
        },
        inputType(data) {
            let obj = {};
            obj = this.options3.find((item) => {
                return item.id === data
            });
            this.DepartInfo.userstatusname = obj.name;
            this.DepartInfo.userstatus = obj.id;
        },

        inputEdu(data) {
            let obj = {};
            obj = this.options2.find((item) => {
                return item.id === data
            });
            this.EduInfo.educationname = obj.name;
            this.EduInfo.education = obj.id;
        },

        cancelDialogVisibleEdu() {
            this.EduInfo = {};
            this.showEdu = false;
        },
        cancelDialogVisibleDepart() {
            this.DepartInfo = {};
            this.treeselectvalue = null;
            this.showDepart = false;
        },

        eduButton() {
            let edumassage = {};
            edumassage.educationname = this.EduInfo.educationname;
            edumassage.education = this.EduInfo.education;
            edumassage.major = this.EduInfo.major;
            edumassage.starttime = this.EduInfo.eduTime[0];
            edumassage.endtime = this.EduInfo.eduTime[1];
            edumassage.eduTime = this.EduInfo.eduTime[0] + '至' + this.EduInfo.eduTime[1];
            this.editStaff.educationList.push(edumassage);
            this.editStaff.eduid = this.EduInfo.eduid;
            this.showEdu = false;
            this.EduInfo = {};
        },

        select(data) {
            this.DepartInfo.departmentid = data.id;
            this.DepartInfo.departmentname = data.name;
        },

        departButton() {
            let edumassage = {};
            edumassage.departmentname = this.DepartInfo.departmentname;
            edumassage.departmentid = this.DepartInfo.departmentid;
            edumassage.entrytime = this.DepartInfo.entrytime;
            edumassage.userstatus = this.DepartInfo.userstatus;
            edumassage.userstatusname = this.DepartInfo.userstatusname;

            this.editStaff.experienceList.push(edumassage);
            this.editStaff.departmentid = this.DepartInfo.departmentid;
            this.showDepart = false;
            this.DepartInfo = {};
            this.treeselectvalue = null;
        },

        dialogVisibleEdu() {
            this.showEdu = true;
        },

        dialogVisibleDepart() {
            this.showDepart = true;
        },

        //停用
        deleteInfo(val) {
            let change = '';
            if (val) {
                change = "1"
            } else {
                change = "0"
            }
            this.$nextTick(async () => {
                let id = {id: this.editStaff.id, switchChange: change}
                let params = {className: 'Staff', method: 'deleteStaffInfo', param: id};
                let response = await this.$store.dispatch('http/post', params);
                console.log(response)
                if (response == 1) {
                    this.$message({showClose: true, message: '停用人员成功！', type: 'success'});
                } else {
                    this.$message({
                        showClose: true,
                        message: '停用人员失败！',
                        type: 'error'
                    });
                }
            })
        },
        //覆盖默认的上传行为
        myUpload(content) {
            this.$nextTick(async () => {
                this.loading.form = true;
                this.dynamicUploader = 'exist-uploader';
                let fd = new FormData();
                fd.append('file', content.file);
                let params = {param: fd, url: process.env.BASE_API + '/api/file/imgFileUpload'};
                let response = await this.$store.dispatch('http/fileUpload', params);
                if (response.code === 200) {
                    this.editStaff.pic = response.data;
                    this.$message({
                        message: '上传成功',
                        type: 'success'
                    });
                } else {
                    this.files = [];
                    this.dynamicUploader = '';
                    this.$message({
                        message: response.msg,
                        type: 'error'
                    });
                }
                this.loading.form = false;
            })
        },

        //删除上传头像
        OnRemove() {
            this.editStaff.pic = "";
            this.dynamicUploader = '';
        },

        //取消按钮
        editBackClick: function () {
            if (this.onBack) {
                this.onBack(null)
            }
        },

        //确定按钮
        editStaffInfoClick: function () {
            this.$refs.editStaff.validate(valid => {
                if (valid) {
                    this.loading.btn = true;
                    this.$nextTick(async () => {
                        let params = {className: 'Staff', method: 'updateStaff', param: this.editStaff};
                        let repose = await this.$store.dispatch('http/post', params);
                        if (repose === '200') {
                            this.$message({showClose: true, message: '保存成功！', type: 'success'});
                            this.loading.btn = false;
                            setTimeout(() => {
                                if (this.onBack) {
                                    this.onBack('succ')
                                }
                            }, 500);
                        } else {
                            this.$message({showClose: true, message: '保存失败！', type: 'error'});
                            this.loading.btn = false;
                        }
                    })
                } else {
                    return false;
                }
            })
        },

        //上传文件之前的钩子，参数为上传的文件，若返回 false 或者返回 Promise 且被 reject，则停止上传。
        beforeAvatarUpload(file) {
            const extension = file.name.split('.')[file.name.split('.').length - 1] === 'jpg'
            const extension2 = file.name.split('.')[file.name.split('.').length - 1] === 'png'
            const extension3 = file.name.split('.')[file.name.split('.').length - 1] === 'gif'
            const isLt2M = file.size / 1024 / 1024 < 2;
            if (!extension && !extension2 && !extension3) {// && !extension4 && !extension5) {
                this.$message({
                    message: '上传模板只能是 jpg、png、gif格式!',
                    type: 'error',
                    duration: 2500
                });
                this.$refs.upload.clearFiles();
                return false;
            }
            if (!isLt2M) {
                this.$message({
                    message: '上传模板大小不能超过 2MB!',
                    type: 'error',
                    duration: 2500
                });
                this.$refs.upload.clearFiles();
                return false;
            }
            return (extension || extension2 || extension3) && isLt2M;
        },
    },

    created() {
        this.loading.form = true;
        this.editStaff = this.$store.state.db.get('editStaffForm');
      //  console.log("this.editStaff",this.editStaff)
        if (this.editStaff.isused == 1) {
            this.isused = true;
        } else {
            this.isused = false;
        }
       // console.log("aaaaaaa")
        this.$nextTick(async () => {
        //    console.log("qqqqqqqq")
            // 获取角色下拉菜单
            let selectRole = {className: 'Staff', method: 'selectRole', param: ''};
            this.options = await this.$store.dispatch('http/post', selectRole);
         //   console.log(" this.options", this.options)
            //显示头像
            /*let pic = {param: this.editStaff.pic, url: process.env.BASE_API + '/api/file/downloadfile'};
            let res = await this.$store.dispatch('http/fileDownload', pic);
            let path = "";
            if (res != "" && res != null) {
                path = "data:image/png;base64," + res;
            } else {
                path = "";
            }
            if (path != "") {
                this.ImageUrl = path;
            }*/
   // console.log("wwwwwww")
            // 获取学历下拉菜单
           // let eduParam = {kind: 'education'};
           // let selectEdu = {className: 'Staff', method: 'selectEducationOrType', param: eduParam};
           // this.options2 = await this.$store.dispatch('http/post', selectEdu);
           // console.log("this.options2",this.options2)
            // 获取类型下拉菜单
            let typeParam = {kind: 'staffType'};
            let selectType = {className: 'Staff', method: 'selectEducationOrType', param: typeParam};
            this.options3 = await this.$store.dispatch('http/post', selectType);
        //    console.log("this.options3",this.options3)
            // 获取性别下拉菜单
           // let sexParam = {kind: 'sex'};
            //let selectSex = {className: 'Staff', method: 'selectEducationOrType', param: sexParam};
            //this.options1 = await this.$store.dispatch('http/post', selectSex);
           // console.log("this.options1",this.options1)
            //获取机构下拉菜单
            let params = {
                className: 'Dept',
                method: 'getDeptInfoByDeptId',
                param: {id: this.$store.state.user.entity.departmentId}
            };
            const treeRoot = await this.$store.dispatch('http/post', params);
            this.SelectTreeData = [treeRoot];
            console.log("this.SelectTreeData",this.SelectTreeData)
            //查询人员部门经历
            const id = {id: this.editStaff.id};
            let dpt = {className: 'Staff', method: 'selectUserDepart', param: id};
            let DPT = await this.$store.dispatch('http/post', dpt);
            this.editStaff.experienceList = DPT;
            console.log("this.editStaff.experienceList",this.editStaff.experienceList)
            //查询人员学历
            let edu = {className: 'Staff', method: 'selectUserEdu', param: id};
            let EDU = await this.$store.dispatch('http/post', edu);
            this.editStaff.educationList = EDU;
            console.log("this.editStaff.educationList",this.editStaff.educationList)
            this.editStaff.educationList.forEach((item) => {
                if (item.name) {
                    item.educationname = item.name;
                }
            });
            console.log('EDU', EDU);
            for (let index in EDU) {
                this.editStaff.educationList[index].eduTime = JSON.parse(JSON.stringify(EDU[index])).starttime + '至' + JSON.parse(JSON.stringify(EDU[index])).endtime
            }
            //查询人员RoleId
            let rid = {className: 'Staff', method: 'selectUserRoleId', param: id};
            let RID = await this.$store.dispatch('http/post', rid);
            this.editStaff.role = RID;
            console.log("this.editStaff.role",this.editStaff.role)
            //查询人员RoleName
            let rname = {className: 'Staff', method: 'selectUserRoleName', param: id};
            let RNAME = await this.$store.dispatch('http/post', rname);
            this.editStaff.roles = RNAME;
            console.log("this.editStaff.roles",this.editStaff.roles)
            this.loading.form = false;
        });
    },
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
.header {
    font: 20px Extra large;
    color: #303133;
    line-height: 1.7;
    padding: 0;
}

.staffDiv {
    margin: 10px;
    padding: 4px;
    background: #fff;
    border-radius: 6px;
    box-shadow: 0 2px 4px rgba(0, 0, 0, .12), 0 0 6px rgba(0, 0, 0, .04)
}

.exist-uploader /deep/ .el-upload--picture-card {
    display: none;
}
</style>
