<template>
    <div class="currentDiv">
        <el-header class="header">
            <el-row class="headerBtn">
                <span class="title">新增产品信息</span>
            </el-row>
        </el-header>

        <el-main>
            <div>
                <el-form :model="formData" :rules='rules' ref='form' status-icon label-width="120px">
                    <el-form-item label="产品类型"  prop="productType" class="selectype">
                        <el-cascader
                            class="userInput"
                            size='small'
                            placeholder="请选择产品类型"
                            v-model="formData.productType"
                            :options="productTypeList"
                            :props="defaultProps"
                            @change="getInputId"
                            :show-all-levels="false">
                        </el-cascader>
<!--                        <i class="el-icon-arrow-down"-->
<!--                            style="position: absolute;z-index: 999;left: 470px;top: 35%;color: #8c939d"-->
<!--                        ></i>-->
<!--                        <el-input class="userInput"-->
<!--                                  placeholder="请选择产品类型"-->
<!--                                  v-model="formData.productType"-->
<!--                                  @focus="projectOrgFun"-->
<!--                                  size='small'-->
<!--                                  aria-required="true"-->
<!--                                  style="position: relative"-->
<!--                        >-->
<!--                        </el-input>-->
<!--                            <el-tree  class="ORGTree"-->
<!--                                      v-show="ishowTree"-->
<!--                                      ref="tree"-->
<!--                                      :data="productTypeList"-->
<!--                                      key="id"-->
<!--                                      node-key="id"-->
<!--                                      highlight-current-->
<!--                                      @node-click="handleNodeClick"-->
<!--                                      :props="defaultProps">-->
<!--                            </el-tree>-->
                    </el-form-item>
                    <el-form-item label="产品名称" prop="productName">
                        <el-input name="productName" class="input" v-model="formData.productName" placeholder="请输入产品名称"
                                  clearable  size="small"></el-input>
<!--                        :maxLength="20"-->
                    </el-form-item>
                    <el-form-item label="产品简介" prop="introduction">
                        <el-input class="userInput"
                                  type="textarea"
                                  :rows="5"
                                  v-model="formData.introduction"  placeholder="请输入产品简介"
                                  size="small"></el-input>
<!--                        :maxLength="50"-->
                    </el-form-item>

<!--                    <el-form-item label="产品归属" prop="ascription">-->
<!--                        <el-input class="userInput" v-model="formData.ascription" :maxLength="10" placeholder="请输入产品归属"-->
<!--                                  size="small"></el-input>-->
<!--                    </el-form-item>-->
                    <el-form-item label="合作方" prop="departmentId">
                        <el-select v-model="formData.departmentId" placeholder="请选择合作方" class="userInput" size="small">
                            <el-option
                                v-for="(item,index) in departmentList"
                                :key="index"
                                :label="item.name"
                                :value="item.id"
                            ></el-option>
                        </el-select>
<!--                        <i class="el-icon-arrow-down"-->
<!--                           style="position: absolute;z-index: 999;left: 470px;top: 35%;color: #8c939d"-->
<!--                        ></i>-->
<!--                        <el-input class="userInput" v-model="formData.departmentName"-->
<!--                                  @focus="showTree"-->
<!--                                  :maxLength="10" placeholder="请选择合作方"-->
<!--                                  size="small"-->
<!--                                  style="position: relative;"-->
<!--                        ></el-input>-->
<!--                        <el-container style="overflow: auto">-->
<!--                            <el-tree-->
<!--                                class="ORGTree"-->
<!--                                v-show="ishowTree1"-->
<!--                                style="overflow: auto"-->
<!--                                :props="defaultProps1"-->
<!--                                @node-click="nodeClick"-->
<!--                                :expand-on-click-node="false"-->
<!--                                :highlight-current="true"-->
<!--                                :load="nodeOpen"-->
<!--                                :key="treeKey"-->
<!--                                lazy-->
<!--                            >-->
<!--                            </el-tree>-->
<!--                        </el-container>-->

                    </el-form-item>

                    <el-form-item label="准入条件" prop="access" >
                        <el-input class="userInput"
                                  type="textarea"
                                  :rows="5"
                                  v-model="formData.access"  placeholder="请输入准入条件"
                                  size="small"></el-input>
                        <!--                        :maxLength="10"-->
                    </el-form-item>

                    <el-form-item label="服务对象">
                        <div v-for="(item, index) in serviceObjectList" :key="index">
                            <el-form-item :prop="'formData.serviceObject'+ parseFloat(index+1) ">
                                <el-input class="userInput" v-model="item.value" clearable></el-input>
<!--                                <el-button size="mini"-->
<!--                                           @click="addServiceObject"-->
<!--                                           :style="serviceObjectList.length === 6 ? 'pointer-events: none;': ''"-->
<!--                                           icon="el-icon-circle-plus-outline" circle></el-button>-->
<!--                                <el-button size="mini"-->
<!--                                           @click="delServiceObject(index)"-->
<!--                                           :style="serviceObjectList.length === 1 ? 'pointer-events: none;': ''"-->
<!--                                           icon="el-icon-remove-outline" circle></el-button>-->
                            </el-form-item>
                        </div>
                    </el-form-item>

                    <el-form-item label="使用场景">
                        <div v-for="(item, index) in useScenesList" :key="index">
                            <el-form-item :prop="'formData.useScenes'+ parseFloat(index+1) ">
                                <el-input class="userInput" v-model="item.value" clearable></el-input>
                                <el-button size="mini"
                                           @click="addUseScenes"
                                           :style="useScenesList.length === 6 ? 'pointer-events: none;': ''"
                                           icon="el-icon-circle-plus-outline" circle></el-button>
                                <el-button size="mini"
                                           @click="delUseScenes(index)"
                                           :style="useScenesList.length === 1 ? 'pointer-events: none;': ''"
                                           icon="el-icon-remove-outline" circle></el-button>
                            </el-form-item>
                        </div>
                    </el-form-item>

                    <el-form-item label="产品亮点" prop="productFeatures">
                        <el-input class="userInput"
                                  type="textarea"
                                  :rows="5"
                                  v-model="formData.productFeatures"  placeholder="请输入产品特点"
                                  size="small"></el-input>
<!--                        :maxLength="10"-->
                    </el-form-item>
                    <el-form-item label="产品特点" prop="highLight">
                        <el-input class="userInput"
                                  type="textarea"
                                  :rows="5"
                                  v-model="formData.highLight"  placeholder="请输入产品特点"
                                  size="small"></el-input>
                        <!--                        :maxLength="10"-->
                    </el-form-item>
<!--v-if="inputId === '11' || inputId === '14' || inputId === '21'"-->
                    <el-form-item label="贷款金额" prop="loanAmount" >
                        <el-input class="userInput" v-model="formData.loanAmount"  placeholder="请输入贷款金额"
                                  size="small"></el-input>
<!--                        :maxLength="10"-->
                    </el-form-item>
<!-- v-if="inputId === '11' || inputId === '14' || inputId === '21' || inputId === '24' || inputId === '26'"-->
                    <el-form-item label="期限" prop="timeLimit">
                        <el-input class="userInput" v-model="formData.timeLimit"  placeholder="请输入期限"
                                  size="small"></el-input>
<!--                        :maxLength="10"-->
                    </el-form-item>
<!-- v-if="inputId === '11' || inputId === '14' || inputId === '21' || inputId === '24' || inputId === '26'"-->
                    <el-form-item label="利率" prop="interestRate" >
                        <el-input class="userInput" v-model="formData.interestRate"  placeholder="请输入利率"
                                  size="small"></el-input>
<!--                        :maxLength="10"-->
                    </el-form-item>

<!--                    <el-form ref="formContent" label-width="100px" v-for="(item,index) in ques" :key="index">-->
<!--                        <el-form-item :label="'标题'+parseFloat(index+1)">-->
<!--                            <el-input v-model="item.questionDescription"></el-input>-->
<!--                            <el-button @click="additem" icon="el-icon-circle-plus-outline" circle></el-button>-->
<!--                            <el-button @click="removeitem(index)" icon="el-icon-remove-outline" circle></el-button>-->
<!--                        </el-form-item>-->
<!--                        <el-form-item label="类型">-->
<!--                            <el-radio-group v-model="item.questionType">-->
<!--                                <el-radio label="0">单选</el-radio>-->
<!--                                <el-radio label="1">多选</el-radio>-->
<!--                            </el-radio-group>-->
<!--                        </el-form-item>-->
<!--                        <el-form-item v-for="(it, ind) in item.myQuestion" :key="ind" :label=" String.fromCharCode(64 + parseInt(ind+1))">-->
<!--                        <el-input v-model="it.content"></el-input>-->
<!--                        <el-button size="mini" @click="addoption(index)" icon="el-icon-circle-plus-outline" circle></el-button>-->
<!--                        <el-button size="mini" @click="removeoption(index,ind)" icon="el-icon-delete" circle></el-button>-->
<!--                    </el-form-item>-->
<!--                    </el-form>-->
                    <el-form-item label="标题">
                        <div v-for="(item, index) in productsExpandList" :key="index">
<!--                            <el-form-item >-->
                                <el-input class="userInput" v-model="item.name" placeholder="请输入标题" clearable></el-input>
                                <el-input class="userInput"  v-model="item.content" placeholder="请输入内容" clearable></el-input>
                                <el-button size="mini"
                                           @click="addUseScenes1"
                                           :style="productsExpandList.length === 6 ? 'pointer-events: none;': ''"
                                           icon="el-icon-circle-plus-outline" circle></el-button>
                                <el-button size="mini"
                                           @click="delUseScenes1(index)"
                                           :style="productsExpandList.length === 1 ? 'pointer-events: none;': ''"
                                           icon="el-icon-remove-outline" circle></el-button>
<!--                            </el-form-item>-->
                        </div>
                    </el-form-item>

                    <el-form-item label="融资额度" prop="financingAmount" v-if="inputId === '11'">
                        <el-input class="userInput" v-model="formData.financingAmount"  placeholder="请输入融资额度"
                                  size="small"></el-input>
<!--                        :maxLength="10"-->
                    </el-form-item>

                    <el-form-item label="还款节奏" prop="repaymentRhythm" v-if="inputId === '11'">
                        <el-input class="userInput" v-model="formData.repaymentRhythm"  placeholder="请输入还款节奏"
                                  size="small"></el-input>
<!--                        :maxLength="10"-->
                    </el-form-item>

                    <el-form-item label="产品图片" prop="img">
                        <el-button type="primary" icon="el-icon-plus" size="small" plain @click='pickImg'>选择图片</el-button>
                    </el-form-item>

                    <div style="margin-left: 120px;margin-bottom: 20px;" v-if="showImg">
                        <img style="width: 100px;height: 100px;" :src="choosenImg">
                    </div>

                    <el-form-item label="案例描述" prop="caseDescription" >
                        <el-input class="userInput" type="textarea" v-model="formData.caseDescription"  placeholder="请输入案例描述"
                                  size="small"></el-input>
                    </el-form-item>
                    <el-form-item label="附件名称" prop="example">
                        <el-input class="userInput"
                                  :rows="5"
                                  v-model="formData.example"  placeholder="请输入附件名称"
                                  size="small"></el-input>
<!--                        :maxLength="50"-->

                        <el-upload
                            class="upload-demo"
                            action="https://jsonplaceholder.typicode.com/posts/"
                            :on-preview="handlePreview"
                            :on-remove="handleRemove"
                            :before-remove="beforeRemove"
                            multiple
                            :limit="1"
                            :http-request="myUpload"
                            :on-exceed="handleExceed"
                            :file-list="fileList">
                            <el-button size="small" icon="el-icon-plus" type="primary" style="margin-top:10px;">点击上传</el-button>
                            <div slot="tip" class="el-upload__tip">只能上传pdf/word文件</div>
                        </el-upload>

                    </el-form-item>
                    <el-form-item label="产品标签" prop="label">
                        <el-checkbox-group v-model="labelList" @change="handleCheckedChange">
                            <el-checkbox v-for="(item,index) in productLabelList" :label="item.code" :key="index">{{item.name}}</el-checkbox>
                        </el-checkbox-group>
                    </el-form-item>

                    <el-form-item label="客户经理" prop="linkMan">
                        <el-input class="userInput" v-model="formData.linkMan" placeholder="请输入客户经理名称"
                                  size="small"></el-input>
                    </el-form-item>
                    <el-form-item label="联系方式" prop="linkPhone">
                        <el-input class="userInput" v-model="formData.linkPhone" placeholder="请输入联系方式"
                                  size="small"></el-input>
                    </el-form-item>
                    <el-form-item label="邮箱" prop="linkEmail">
                        <el-input class="userInput" v-model="formData.linkEmail" placeholder="请输入邮箱"
                                  size="small"></el-input>
                    </el-form-item>


                </el-form>
            </div>
        </el-main>

        <el-dialog
            :close-on-click-modal="false"
            :append-to-body="true"
            :visible.sync="dialog"
            :show-close="true"
            width="40%"

        >

            <div class="dialog-main">
                <div>
                    <div style="text-align: center;font-weight: 700;font-size: 16px;">请选择图片</div>
                    <div class="imgList">
                        <div v-for="(item,index) in imgList" :key="index" @click="choosePic(item)" style="margin: 10px;">
                            <img :src="item.pic" width="100" height="100">
                        </div>
                    </div>
                </div>
                <div slot="footer" class="dialog-footer" style="text-align: right;margin-top: 20px;">
                </div>
            </div>
        </el-dialog>

        <el-footer style="margin-top: 30px;">
            <el-button type="info" icon="el-icon-back" plain @click="backBtn">返回</el-button>
<!--            <el-button :loading="loading.btn" type="success" icon="el-icon-check" plain-->
<!--                       @click.native.prevent="submitReport('reportParam')">保存-->
                <el-button :loading="loading.btn" type="success" icon="el-icon-check" plainR
                           @click="submitForm()">保存
            </el-button>
            <el-button v-if="productPermission == false"
                type="primary" icon="el-icon-upload2" plain @click="applyForm()">提交发布申请</el-button>
        </el-footer>
    </div>
</template>

<script>
import mixin from '../mixin/mixin.js';


let PDFJS = require("pdfjs-dist/legacy/build/pdf.js");
export default {
    mixins: [mixin],
    name: "add-report",
    props: {
        //子页面调用父页面
        onBack: {
            type: Function,
            default: null
        }
    },
    data() {
        // 报告新增时校验报告编号是否已经存在
        // let checkCode = async (rule, value, callback) => {
        //     if (!value) {
        //         return callback(new Error('请输入报告编码'))
        //     } else {
        //         // 提交后台验证角色编码是否存在
        //         const params = {
        //             className: 'Report',
        //             method: 'checkReportCode',
        //             param: {
        //                 id: this.reportParam.form.id,
        //                 code: this.reportParam.form.code
        //             }
        //         };
        //         const res = await this.$store.dispatch('http/post', params);
        //         if (res === '203') {
        //             return callback(new Error('报告编号已存在！'))
        //         } else {
        //             return callback()
        //         }
        //     }
        // };

        //联系电话的验证
        // var phoneVerify = async (rule, value, callback) => {
        //     if (!(/^1[34578]\d{9}$/.test(value))) {
        //         callback(new Error('请输入正确的手机号码!'));
        //     }
        // };
        var phoneVerify = (rule, value, callback) => {
            if (value === '') {
                callback(new Error('请输入联系人电话'));
            } else {
                let regPone = null;
                let mobile = /^1(3|4|5|6|7|8|9)\d{9}$/; //最新16手机正则
                let tel = /^(0[0-9]{2,3}\-)([2-9][0-9]{4,7})+(\-[0-9]{1,4})?$/; //座机
                if (value.charAt(0) == 0) {    // charAt查找第一个字符方法，用来判断输入的是座机还是手机号
                    regPone = tel;
                } else {
                    regPone = mobile;
                }
                if (!regPone.test(value)) {
                    return callback(
                        new Error("请填写联系人电话(座机格式'区号-座机号码')")
                    );
                }
                callback();
            }
        };

        //邮箱的验证
        var emailVerify = async (rule, value, callback) => {
            if (!(/\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*/.test(value))) {
                callback(new Error('请输入正确的邮箱格式!'));
            }
        };

        return {
            loading: {
                form: false,
                btn: false
            },

            showQyfz: false,            //展示区域发展下拉框
            showHyfx: false,            //展示行业分析下拉框
            showZtbg: false,            //展示专题报告下拉框
            showHgjj: false,             //展示宏观经济下拉框
            selectChange: [],                //用户选择的所有报告属性
            selectChangeData: [],
            // pic: '',                             //获取的图片路径
            showPicUrl: '',                          //用于展示的封面路径
            reportParam: {},
            ques: [
                {
                    questionDescription: '',
                    questionType: '',
                    releaseFlag: '',
                    sequen: '',
                    myQuestion: [
                        {answer: '', content:''},
                        {answer: '', content:''}
                    ]
                }
            ],
            formData: {
                // serviceObject1: '',
                // useScenes1: '',
                productType:'',
                departmentId:'',
                img: '',
                label: '',
                productFeatures: '',
                highLight:'',
                access:'',
                loanAmount: '',
                timeLimit: '',
                interestRate: '',
                example:'',
                financingAmount: '',
                repaymentRhythm: '',
                caseDescription: '',
            },
            showImg: false,
            imgList: [],
            dialog: false,
            productTypeList: [],
            // productStatusList: [{
            //     value: '0',
            //     label: '无效'
            // }, {
            //     value: '1',
            //     label: '有效'
            // }],
            rules: {
                productType: [{required: true,message:'请选择产品类型'}],
                productName: [{required: true,message:'请输入产品名称',trigger: 'blur'}],
                introduction: [{required: true,message:'请输入产品简介',trigger: 'blur'}],
                departmentId: [{required: true,message:'请选择合作方'}],
                img: [{required: true,message:'请选择产品图片'}],
                linkMan: [{required: true,message:'请输入客户经理名称',trigger: 'blur'}],
                linkPhone: [
                    {required: true, validator: phoneVerify, trigger: 'blur'}
                ],
                linkEmail: [
                    {required: true, validator: emailVerify, trigger: 'blur'}
                ],
            },
            choosenImg: '',
            ishowTree: false,
            ishowTree1: false,
            defaultProps: {
                // children: 'tree',
                label: 'name',
                value: 'code'
            },
            cascaderList: [],
            // expandedKeys: [-1],
            productType: [],
            inputId: '',
            serviceObjectList: [{
                value: ''
            }],
            useScenesList: [{
                value: ''
            }],
            productsExpandList:[{
                name:'',
                content:'',
            }],
            defaultProps1: {
                label: 'name',
                isLeaf: 'isLeaf'
            },
            treeKey: '',
            productPermission: '',
            departmentList: '',
            fileList: [],//上传的pdf文件
            productLabelList: [],
            labelList: [],
            casePath: {
                name: '',
                url: ''
            }
        }
    },

    components: {},
    methods: {

        init() {
            this.formData = {
                productType:'',
                departmentId:'',
                img: '',
                label: '',
                productFeatures: '',
                highLight:'',
                loanAmount: '',
                timeLimit: '',
                interestRate: '',
                example:'',
                financingAmount: '',
                repaymentRhythm: '',
                caseDescription: '',
                productsExpandList:'',
            }
        },
        additem() {
            let obj = {
                questionDescription: '',
                questionType: '',
                releaseFlag: '',
                sequen: '',
                myQuestion: [
                    {answer: '', content:''},
                    {answer: '', content:''}
                ]
            }
            this.ques.push(obj)
        },

        removeitem(index) {
            this.ques.splice(index, 1)
        },
        addoption(index) {
            let obj = {order: '', content:''}
            this.ques[index].myQuestion.push(obj)
        },
        removeoption(index, ind) {
            this.ques[index].myQuestion.splice(ind, 1)
        },
        //返回按钮
        backBtn() {
            if (this.onBack) {

                this.serviceObjectList = [{
                    value: ''
                }];
                this.useScenesList = [{
                    value: ''
                }];
                this.productsExpandList=[{
                    name:'',
                    content:'',
                }]
                this.cascaderList = [];
                this.inputId = '';
                this.choosenImg = '';
                this.showImg = false;
                this.labelList = [];
                this.fileList = [];
                this.onBack(null);
                this.$refs.form.resetFields();//清除form表格的内容和验证
            }
        },
        //当多选框选择发生变化时触发
        // change(e) {
        //     let array = new Array();
        //     array = e;
        //
        //     array.forEach((item) => {
        //         if (this.selectChange == null || !this.selectChange.find(m => m.key === item)) {
        //             let res = this.economy.find(m => m.key === item);
        //             if (res != null) {
        //                 this.selectChange.push(res);
        //             }
        //
        //             let res2 = this.special.find(m => m.key === item);
        //             if (res2 != null) {
        //                 this.selectChange.push(res2);
        //             }
        //         }
        //     });
        //     this.getShowPicUrl();
        // },

        // getShowPicUrl() {
        //     console.log(this.selectChange, 'selectChange------------>');
        //     if (this.selectChange != null && this.selectChange.length > 0) {
        //         this.pic = this.selectChange[0].pic;
        //         this.getPic(this.selectChange[0]).then(res => {
        //             this.showPicUrl = res;
        //         });
        //     } else {
        //
        //         this.showPicUrl = '';
        //         console.log('showPicUrl---------------->', this.showPicUrl)
        //     }
        // },

        //将路径转成base64位的图片
        // async getPic(item) {
        //     let params = {
        //         param: item.pic,
        //         url: process.env.BASE_API + '/api/file/downloadfile'
        //     };
        //     let res = await this.$store.dispatch('http/fileDownload', params);
        //     let path = (res != null && res != '') ? "data:image/png;base64," + res : "";
        //     return path;
        // },
        getInputId() {
            console.log('this.formData.productType=',this.formData.productType);
            let arr = this.formData.productType;
            this.inputId = arr[arr.length-1];
            console.log('this.inputid=',this.inputId);
        },

        handleCheckedChange() {
            console.log('this.labelList==',this.labelList);
        },

        //上传相关方法
        handleRemove(file, fileList) {
            console.log(file, fileList);
        },
        handlePreview(file) {
            console.log('file===',file);
        },
        // handleExceed(files, fileList) {
        //     this.$message.warning(`当前限制选择 3 个文件，本次选择了 ${files.length} 个文件，共选择了 ${files.length + fileList.length} 个文件`);
        // },
        beforeRemove(file, fileList) {
            return this.$confirm(`确定移除该文件？`);
        },
        myUpload(content) {
            let fd = new FormData();
            fd.append('file', content.file);
            // this.formData.filename=content.file.name;
            // this.formData.fileformat=content.file.type;
            console.log("fffddd",fd)
            this.casePath.name = content.file.name;
            this.$nextTick(async () =>{
                let params = {
                    param:fd,
                    url: process.env.BASE_API +'/api/file/productupload'
                };
                let response = await this.$store.dispatch('http/fileUpload2', params);
                this.casePath.url = response.data;
                if(response){
                    console.log("serviceFilePath:",response);
                    this.formData.casePath = this.casePath.url;
                    this.$message({message: '上传成功', type: 'success'});
                }else{
                    this.$message({message: '上传失败', type: 'failed'});
                }
            })
        },

        //获取产品标签多选列表
        getProductLabel() {
            this.$nextTick(async () => {
                let params = {
                    className:'Member',
                    method: 'getdictionarylist',
                    param:{
                        kind:'productlabel'  //产品标签
                    }
                };
                let res = await this.$store.dispatch('http/post', params);
                console.log('res222',res);
                this.productLabelList = res;
            });
        },


        //获取该用户是否有发布、编辑产品的权限
        getProductPermission() {
            this.$nextTick(async () => {
                let params = {
                    className:'produce',
                    method:'getProductPermission',
                    param:{}
                };
                let res = await this.$store.dispatch('http/post', params);
                this.productPermission = res;
                console.log('this.productPermission==',this.productPermission)
            });
        },

        getDepartmentList() {
            this.$nextTick(async () => {
                let params = {
                    className: 'Member',
                    method: 'getGYList',
                    param: {

                    }
                };
                let res = await this.$store.dispatch('http/post', params);
                this.departmentList = res;
                console.log('this.departmentList=',this.departmentList)
            });
        },

        // 机构树节点选中
        nodeClick(data) {
            console.log('data=',data);
            this.formData.departmentId = data.id;
            this.formData.departmentName = data.name;
            this.ishowTree1 = false;
        },
        // 机构树数据加载
        nodeOpen(node, resolve) {
            console.log('node=',node);
            setTimeout(async () => {  // 懒加载
                if(node.level > 0) {
                    const params = {
                        //className: 'Dept',
                       // method: 'getSubDeptsByDeptId',
                        className: 'Member',
                        method: 'getGYList',
                        param: {
                            id: node.id,
                            isused:'1' //默认都是传1
                        }
                    };
                    return resolve(
                        await this.$store.dispatch('http/post', params)
                    )
                } else {
                    const params = {
                        //className: 'Dept',
                        //method: 'getSubDeptsByDeptId',
                        className: 'Member',
                        method: 'getGYList',
                        param: {
                            id: '0',
                            isused:'1' //默认都是传1
                        }
                    };
                    return resolve(
                        await this.$store.dispatch('http/post', params)
                    )
                }

            }, 500);
        },

        showTree() {
            this.ishowTree1 = true;

        },


        projectOrgFun() {
            this.ishowTree = true
        },

        closeTree(){
            this.ishowTree = false
        },

        handleNodeClick(e){
            this.formData.productType = e.name
            this.inputId = e.code.substring(0,2);
            console.log('this.inputId',this.inputId);
            this.ishowTree = false
        },

        getproductTypeList() {
            this.$nextTick(async () => {
                let params = {
                    className:'produce',
                    method: 'getProductTypes'
                };
                let res = await this.$store.dispatch('http/post', params);

                for(let i = 0;i< res.length;i++) {
                    if( res[i].children && res[i].children.length > 0) {
                        let arr = res[i].children;
                        for(let j=0;j<arr.length;j++) {
                            if(arr[j].children.length > 0) {
                                let rootArr = arr[j].children
                                for (let k=0;k<rootArr.length;k++) {
                                    if(rootArr[k].children.length > 0) {
                                        rootArr[k] = rootArr[k]
                                    } else {
                                        this.deleteParam(rootArr[k])
                                    }
                                }
                            } else {
                                this.deleteParam(arr[j])

                            }
                        }
                    } else {
                        this.deleteParam(res[i])
                    }
                }

                this.productTypeList = res;
                console.log('this.productTypeList=',this.productTypeList);
            });
        },
        deleteParam(obj) {
            for(let e in obj) {
                if(e == 'children') {
                    delete obj[e]
                }
            }
        },

        addServiceObject(e) {
            this.serviceObjectList.push({
                value: ''
            })
        },
        delServiceObject(index) {
            this.serviceObjectList.splice(index, 1)
        },

        addUseScenes() {
            this.useScenesList.push({
                value: ''
            })
        },
        addUseScenes1() {
            this.productsExpandList.push({
                name:'',
                content:'',
            })
        },
        delUseScenes(index) {
            this.useScenesList.splice(index,1)
        },
        delUseScenes1(index) {
            this.productsExpandList.splice(index,1)
        },


        //提示只能上传一个pdf文件
        handleExceed(files, fileList) {
            this.$message.warning(`当前限制选择 1 个文件，本次选择了 ${files.length} 个文件，共选择了 ${files.length + fileList.length} 个文件`);
        },

        //选择主题时触发
        changeThemeValue(selectValue) {
            this.showQyfz = selectValue.includes('qyfz');
            this.showHyfx = selectValue.includes('hyfx');
            this.showZtbg = selectValue.includes('ztbg');
            this.showHgjj = selectValue.includes('hgjj');
        },

        getImgList() {
            this.$nextTick(async () => {
                let params = {
                    className:'produce',
                    method: 'getProductPictures',
                    param: {}
                };
                let res = await this.$store.dispatch('http/post', params);
                console.log('res111=',res);
                this.imgList = res;
            });
        },

        pickImg() {
            this.dialog = true;
        },

        choosePic(item) {
            console.log(item);
            this.choosenImg = item.pic;
            this.formData.img = item.id;
            this.dialog = false;
            this.showImg = true;
        },

        submitForm() {
            this.$refs.form.validate(valid => {
                console.log('valid===',valid);
                if (valid) {
                    // this.inputId = this.formData.productType.pop();
                    this.formData.productType = this.inputId;
                    this.formData.label = this.labelList;

                    this.serviceObjectList.forEach((e, i) => {
                        this.formData['serviceObject'+parseFloat(i+1)] = e.value;
                    });
                    this.useScenesList.forEach((e, i) => {
                        this.formData['useScenes'+parseFloat(i+1)] = e.value;
                    });
                    // console.log('111',this.productsExpandList)
                    // this.productsExpandList.forEach((e, i) => {
                    //     this.formData['productsExpand'+parseFloat(i+1)] = e;
                    // });
                    this.formData.productsExpandList = this.productsExpandList
                    console.log('this.formData=',this.formData);

                    this.$nextTick(async () => {
                        let params = {
                            className:'produce',
                            method: 'insertproduct',
                            param: this.formData
                        };
                        let res = await this.$store.dispatch('http/post', params);
                        console.log('res333=',res);
                        if (res.Success) {
                            this.$message({showClose: true, message: '保存成功！', type: 'success'});
                            this.loading.btn = false;
                            setTimeout(() => {
                                if (this.onBack) {
                                    this.serviceObjectList = [{
                                        value: ''
                                    }];
                                    this.useScenesList = [{
                                        value: ''
                                    }];
                                    this.cascaderList = [];
                                    this.inputId = '';
                                    this.choosenImg = '';
                                    this.showImg = false;
                                    this.labelList = [];
                                    this.fileList = [];
                                    this.onBack('succ');
                                    this.$refs.form.resetFields();//清除form表格的内容和验证
                                }
                            }, 500);
                        } else {
                            this.$message({showClose: true, message: '保存失败！', type: 'error'});
                            this.loading.btn = false;
                        }
                    });
                }
            })

        },

        applyForm() {
            this.$refs.form.validate(valid => {

                if (valid) {
                    // this.inputId = this.cascaderList.pop();
                    this.formData.productType = this.inputId;
                    this.formData.label = this.labelList;

                    this.serviceObjectList.forEach((e, i) => {
                        this.formData['serviceObject'+parseFloat(i+1)] = e.value;
                    });
                    this.useScenesList.forEach((e, i) => {
                        this.formData['useScenes'+parseFloat(i+1)] = e.value;
                    });

                    console.log('this.formData=',this.formData);

                    this.$nextTick(async () => {
                        let params = {
                            className:'produce',
                            method: 'insertproduct',
                            param: this.formData
                        };
                        let res = await this.$store.dispatch('http/post', params);
                        console.log('res333=',res);
                        if (res.Success) {
                            let id = res.id;
                            this.$nextTick(async () => {
                                let params = {
                                    className:'produce',
                                    method:'releaseProductById',
                                    param:{
                                        id: id, //产品id
                                        status: '1'
                                    }
                                };
                                let res2 = await this.$store.dispatch('http/post', params);
                                console.log('res2',res2);
                                if (res2) {
                                    this.$message({showClose: true, message: '发布申请提交成功！', type: 'success'});
                                    // this.loading.btn = false;
                                    setTimeout(() => {
                                        if (this.onBack) {

                                            this.serviceObjectList = [{
                                                value: ''
                                            }];
                                            this.useScenesList = [{
                                                value: ''
                                            }];
                                            this.cascaderList = [];
                                            this.inputId = '';
                                            this.choosenImg = '';
                                            this.showImg = false;
                                            this.labelList = [];
                                            this.fileList = [];
                                            this.onBack('succ');
                                            this.$refs.form.resetFields();//清除form表格的内容和验证
                                        }
                                    }, 500);
                                } else {
                                    this.$message({showClose: true, message: '发布申请提交失败！', type: 'error'});
                                    // this.loading.btn = false;
                                }
                            })

                        }
                    });
                }
            })

        },


        // //当绑定值变化时触发的事件
        // handleCheckedCitiesChange(value) {
        //     let checkedCount = value.length;
        //     this.isIndeterminate = checkedCount > 0 && checkedCount < this.cities.length;
        // }
    },

}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
.header {
    background-color: #f1f5f9;
    text-align: left;
    margin-bottom: 10px;
}

.headerBtn {
    text-align: right;
    line-height: 5;
    height: 12px;
    float: left;
    font-size: 13px;
}

.labelStyle {
    display: inline-block;
    color: #101010;
    font-size: 15px;
    font-weight: 700;
    margin-bottom: 20px;
}

.input {
    width: 500px;
}
.userInput {
    width: 500px;
}
/deep/.selectstatus .el-select>.el-input {
    width: 500px;
}

.currentDiv {
    background: #fff;
    margin: 10px;
    border-radius: 6px;
    box-shadow: 0 2px 4px rgba(0, 0, 0, .12), 0 0 6px rgba(0, 0, 0, .04)
}

.report_cover {
    width: 100px;
    height: 135px;
}

.dialog-footer {
    padding-right: 50px;
    margin-top: 45px;
}
.imgList {
    width: 100%;
    margin: 20px 0;
    display: flex;
    flex-direction: row;
    flex-wrap: wrap;
}
.ORGTree{
    position:absolute;
    z-index: 999;
    min-height: 50px;
    overflow: auto;
    width: 500px;
    //left: 50%;
    //transform: translateX(-50%);
    border: 1px solid #dcdfe6;
    border-radius:5px;
}

/*.avatar-uploader el-upload {*/
/*    border: 1px dashed #d9d9d9;*/
/*    border-radius: 6px;*/
/*    cursor: pointer;*/
/*    position: relative;*/
/*    overflow: hidden;*/
/*}*/

/*.avatar-uploader el-upload:hover {*/
/*    border-color: #409EFF;*/
/*}*/

/*.avatar-uploader-icon {*/
/*    font-size: 28px;*/
/*    color: #8c939d;*/
/*    width: 178px;*/
/*    height: 178px;*/
/*    line-height: 178px;*/
/*    text-align: center;*/
/*}*/

/*.avatar {*/
/*    width: 150px;*/
/*    height: 180px;*/
/*    display: block;*/
/*}*/

</style>
