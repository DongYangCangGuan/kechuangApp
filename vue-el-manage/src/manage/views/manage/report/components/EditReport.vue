<template>
    <div class="currentDiv">
        <el-header class="header">
            <el-row class="headerBtn">
                <span class="title">编辑产品信息</span>
            </el-row>
        </el-header>

        <el-main>
            <div>
                <el-form :model="productForm" :rules='rules' ref='productForm' status-icon label-width="150px">

                    <el-form-item label="产品图片" prop="img">
<!--                        <el-input class="userInput" v-model="productForm.id" :maxLength="10" placeholder="请输入产品编号"-->
<!--                                  size="small"></el-input>-->
                        <img width="100" height="100" :src="imgUrl">
                        <el-button type="primary" size="mini" plain @click="changePic">点击更换图片</el-button>
                    </el-form-item>

                    <el-form-item label="产品编号" prop="id">
                        <el-input class="userInput" v-model="productForm.id" :maxLength="10" placeholder="请输入产品编号"
                                  size="small" readonly></el-input>
                    </el-form-item>
                    <el-form-item label="产品名称" prop="productName">
                        <el-input class="userInput" v-model="productForm.productName"  placeholder="请输入产品名称"
                                  size="small"></el-input>
<!--                        :maxLength="20"-->
                    </el-form-item>

                    <el-form-item label="产品类型"  prop="typeName" class="selectype">
                        <el-input class="userInput"
                                  placeholder="请选择产品类型"
                                  v-model="productForm.typeName"
                                  size='small'
                                  aria-required="true"
                                  readonly
                        >
<!--                            @focus="projectOrgFun"-->
                        </el-input>
<!--                        <el-tree  class="ORGTree"-->
<!--                                  v-show="ishowTree"-->
<!--                                  ref="tree"-->
<!--                                  :data="productTypeList"-->
<!--                                  key="id"-->
<!--                                  node-key="id"-->
<!--                                  highlight-current-->
<!--                                  @node-click="handleNodeClick"-->
<!--                                  :props="defaultProps">-->
<!--                        </el-tree>-->
                    </el-form-item>

                    <el-form-item label="产品状态" prop="status">
                        <el-input class="userInput" v-model="productForm.status = 1?'有效':'无效'" :maxLength="10" readonly
                                  size="small"></el-input>
<!--                        <el-select v-model="productForm.status = 1?'有效':'无效'" class="comboSelect" placeholder="请选择" size="small">-->
<!--                            <el-option-->
<!--                                v-for="(item,index) in statusList"-->
<!--                                :key="index"-->
<!--                                :label="item.name"-->
<!--                                :value="item.code"-->
<!--                            ></el-option>-->
<!--                        </el-select>-->
                    </el-form-item>

                    <el-form-item label="产品简介" prop="introduction">
                        <el-input class="userInput"
                                  type="textarea"
                                  :rows="5"
                                  v-model="productForm.introduction"  placeholder="请输入产品简介"
                                  size="small"></el-input>
<!--                        :maxLength="50"-->
                    </el-form-item>

<!--                    <el-form-item label="产品归属" prop="ascription">-->
<!--                        <el-input class="userInput" v-model="productForm.ascription" :maxLength="10" placeholder="请输入产品归属"-->
<!--                                  size="small"></el-input>-->
<!--                    </el-form-item>-->

                    <el-form-item label="合作方" prop="departmentId">
                        <el-select v-model="productForm.departmentId" placeholder="请选择合作方" class="userInput" size="small">
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
<!--                        <el-input class="userInput"-->
<!--                                  v-model="productForm.departmentName"-->
<!--                                  @focus="showTree"-->
<!--                                  :maxLength="10"-->
<!--                                  placeholder="请选择合作方"-->
<!--                                  size="small"-->
<!--                                  style="position: relative;"-->
<!--                        ></el-input>-->
<!--                        <el-container style="overflow: auto">-->
<!--                            <el-tree-->
<!--                                class="ORGTree"-->
<!--                                v-show="ishowTree"-->
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

                    <el-form-item label="准入条件" prop="access">
                        <el-input class="userInput"
                                  type="textarea"
                                  :rows="5"
                                  v-model="productForm.access"  placeholder="请输入准入条件"
                                  size="small"></el-input>
                    </el-form-item>

                    <el-form-item label="服务对象">
                        <div v-for="(item, index) in serviceObjectList" :key="index">
                            <el-form-item :prop="'productForm.serviceObject'+ parseFloat(index+1) ">
                                <el-input class="userInput" v-model="item.value" clearable></el-input>
<!--                                <el-button size="mini"-->
<!--                                           @click="addServiceObject"-->
<!--                                           :style="serviceObjectList.length === 6 ? 'pointer-events: none;': ''"-->
<!--                                           icon="el-icon-circle-plus-outline" circle></el-button>-->
<!--                                <el-button size="mini"-->
<!--                                           @click="delServiceObject(index)"-->
<!--                                           :style="serviceObjectList.length === 1 ? 'pointer-events: none;': ''"-->
<!--                                           icon="el-icon-delete" circle></el-button>-->
                            </el-form-item>
                        </div>
                    </el-form-item>

                    <el-form-item label="使用场景">
                        <div v-for="(item, index) in useScenesList" :key="index">
                            <el-form-item :prop="'productForm.useScenes'+(index+1)">
                                <el-input class="userInput" v-model="item.value" clearable></el-input>
                                <el-button size="mini"
                                           @click="addUseScenes"
                                           :style="useScenesList.length === 6 ? 'pointer-events: none;': ''"
                                           icon="el-icon-circle-plus-outline" circle></el-button>
                                <el-button size="mini"
                                           @click="delUseScenes(index)"
                                           :style="useScenesList.length === 1 ? 'pointer-events: none;': ''"
                                           icon="el-icon-delete" circle></el-button>
                            </el-form-item>
                        </div>
                    </el-form-item>

                    <el-form-item label="产品亮点" prop="productFeatures">
                        <el-input class="userInput"
                                  type="textarea"
                                  :rows="5"
                                  v-model="productForm.productFeatures"  placeholder="请输入产品特点"
                                  size="small"></el-input>
<!--                        :maxLength="10"-->
                    </el-form-item>
                    <el-form-item label="产品特点" prop="highLight">
                        <el-input class="userInput"
                                  type="textarea"
                                  :rows="5"
                                  v-model="productForm.highLight"  placeholder="请输入产品特点"
                                  size="small"></el-input>
                        <!--                        :maxLength="10"-->
                    </el-form-item>
<!--prop="loanAmount" v-if="type === '11'|| type === '14' || type === '21'"-->
                    <el-form-item label="贷款金额" >
                        <el-input class="userInput" v-model="productForm.loanAmount"  placeholder="请输入贷款金额"
                                  size="small"></el-input>
<!--                        :maxLength="10"-->
                    </el-form-item>

<!--                    <el-form-item label="产品金额" prop="loanAmount" v-if="type === ''">-->
<!--                        <el-input class="userInput" v-model="productForm." :maxLength="10" placeholder="请输入产品金额"-->
<!--                                  size="small"></el-input>-->
<!--                    </el-form-item>-->
<!--v-if="type === '11' || type === '14' || type === '21' || type === '24' || type === '26'"-->
                    <el-form-item label="期限" prop="timeLimit" >
                        <el-input class="userInput" v-model="productForm.timeLimit"  placeholder="请输入期限"
                                  size="small"></el-input>
<!--                        :maxLength="10"-->
                    </el-form-item>
<!--v-if="type === '11' || type === '14' || type === '21' || type === '24' || type === '26'"-->
                    <el-form-item label="利率" prop="interestRate"  >
                        <el-input class="userInput" v-model="productForm.interestRate"  placeholder="请输入利率"
                                  size="small"></el-input>
<!--                        :maxLength="10"-->
                    </el-form-item>

                    <el-form-item label="产品拓展标签">
                        <div v-for="(item, index) in productForm.productsExpandList" :key="index">
                            <el-input class="userInput" v-model="item.name" placeholder="请输入标题" clearable></el-input>
                            <el-input class="userInput"  v-model="item.content" placeholder="请输入内容" clearable></el-input>
                                <el-button size="mini"
                                           @click="addUseScenes1"
                                           :style="productsExpandList.length === 6 ? 'pointer-events: none;': ''"
                                           icon="el-icon-circle-plus-outline" circle></el-button>
                                <el-button size="mini"
                                           @click="delUseScenes1(index)"
                                           :style="productsExpandList.length === 1 ? 'pointer-events: none;': ''"
                                           icon="el-icon-delete" circle></el-button>
                        </div>
                    </el-form-item>

                    <el-form-item label="融资额度" prop="financingAmount" v-if="type === '11'">
                        <el-input class="userInput" v-model="productForm.financingAmount"  placeholder="请输入融资额度"
                                  size="small"></el-input>
<!--                        :maxLength="10"-->
                    </el-form-item>

                    <el-form-item label="还款节奏" prop="repaymentRhythm" v-if="type === '11'">
                        <el-input class="userInput" v-model="productForm.repaymentRhythm"  placeholder="请输入还款节奏"
                                  size="small"></el-input>
<!--                        :maxLength="10"-->
                    </el-form-item>
                    <el-form-item label="案例描述" prop="caseDescription">
                        <el-input class="userInput" type="textarea" v-model="productForm.caseDescription"  placeholder="请输入案例描述"
                                  size="small"></el-input>
                        <!--                        :maxLength="10"-->
                    </el-form-item>

                    <el-form-item label="附件名称" prop="caseDescription">
                        <el-input class="userInput"
                                  :rows="5"
                                  v-model="productForm.example"  placeholder="请输入附件名称"
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
                        <el-checkbox-group v-model="productForm.label" @change="handleCheckedChange">
                            <el-checkbox v-for="(item,index) in productLabelList" :label="item.code" :key="index">{{item.name}}</el-checkbox>
                        </el-checkbox-group>
                    </el-form-item>

                    <el-form-item label="客户经理" prop="linkMan">
                        <el-input class="userInput" v-model="productForm.linkMan" placeholder="请输入客户经理名称"
                                  size="small"></el-input>
                    </el-form-item>
                    <el-form-item label="联系方式" prop="linkPhone">
                        <el-input class="userInput" v-model="productForm.linkPhone" placeholder="请输入联系方式"
                                  size="small"></el-input>
                    </el-form-item>
                    <el-form-item label="邮箱" prop="linkEmail">
                        <el-input class="userInput" v-model="productForm.linkEmail" placeholder="请输入邮箱"
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
                <div slot="footer" class="dialog-footer" style="text-align: right;margin-top: 20px;"></div>
            </div>

        </el-dialog>

        <el-footer>
            <el-button type="info" icon="el-icon-back" plain @click="backBtn">返回</el-button>
            <el-button :loading="loading.btn" type="success" icon="el-icon-check" plain
                       @click="submitReport()">保存
            </el-button>
            <el-button v-if="productPermission == false"
                       type="primary" icon="el-icon-upload2" plain @click="applyForm()">提交发布申请</el-button>
        </el-footer>
    </div>
</template>

<script>
import mixin from '../mixin/mixin.js'
import bus from '@COM/utils/bus';

export default {
    mixins: [mixin],
    name: "edit-report",
    props: {
        //子页面调用父页面
        onBack: {
            type: Function,
            default: null
        }
    },
    data() {
        // 报告新增时校验报告编号是否已经存在
        let checkCode = async (rule, value, callback) => {
            if (!value) {
                return callback(new Error('请输入报告编码'))
            } else {
                // 提交后台验证角色编码是否存在
                const params = {
                    className: 'Report',
                    method: 'checkReportCode',
                    param: {
                        id: this.reportParam.id,
                        code: this.reportParam.code
                    }
                };
                const res = await this.$store.dispatch('http/post', params);
                if (res === '203') {
                    return callback(new Error('报告编号已存在！'))
                } else {
                    return callback()
                }
            }
        };

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
            fileList: [],
            loading: {
                form: false,
                btn: false
            },
            pic: '',                             //获取的图片路径
            showPicUrl: '',                           //用于展示的封面图片路径
            selectChange: [],                        //用户选择的所有报告属性
            productTypeList: [],
            ishowTree: false,
            defaultProps: {
                // children: 'tree',
                label: 'name'
            },
            productId: '',
            type: '',
            productForm: {},
            statusList: [
                {
                    name: '有效',
                    code: '1'
                },
                {
                    name: '无效',
                    code: '0'
                }
            ],
            imgList: [],
            imgUrl: '',
            dialog: false,
            serviceObjectList: [],
            useScenesList: [],
            productsExpandList:[{
                name:'',
                content:'',
            }],
            defaultProps1: {
                label: 'name',
                children: 'zones'
            },
            treeKey: '',
            productPermission: '',
            departmentList: '',
            productLabelList: [],
            labelList: [],
            rules: {
                productName: [{required: true,message:'请输入产品名称',trigger: 'blur'}],
                introduction: [{required: true,message:'请输入产品简介',trigger: 'blur'}],
                departmentId: [{required: true,message:'请选择合作方'}],
                linkMan: [{required: true,message:'请输入客户经理名称',trigger: 'blur'}],
                linkPhone: [
                    {required: true, validator: phoneVerify, trigger: 'blur'}
                ],
                linkEmail: [
                    {required: true, validator: emailVerify, trigger: 'blur'}
                ],
            },
        }
    },
    mounted() {
    },
    created() {
        // bus.$on('dept-node-select', this.handleNodeClick)
    },
    methods: {
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
        //提示只能上传一个pdf文件
        handleExceed(files, fileList) {
            this.$message.warning(`当前限制选择 1 个文件，本次选择了 ${files.length} 个文件，共选择了 ${files.length + fileList.length} 个文件`);
        },
        myUpload(content) {
            console.log(content)
            console.log('content===',content);
            let fd = new FormData();
            fd.append('file', content.file);
            console.log('fd===',fd);
            this.productForm.filename=content.file.name;
            this.productForm.fileformat=content.file.type;
            this.$nextTick(async () =>{
                let params = {
                    param:fd,
                    // url:'/api/file/productupload'
                    url: process.env.BASE_API +'/api/file/productupload'
                };
                let response = await this.$store.dispatch('http/fileUpload2', params);
                console.log('response==',response)
                if(response){
                    console.log("serviceFilePath:",response);
                    this.productForm.casePath = response.data;
                    this.$message({message: '上传成功', type: 'success'});
                }else{
                    this.$message({message: '上传失败', type: 'failed'});
                }
            })
        },

        handleCheckedChange() {
            console.log('this.labelList==',this.labelList);
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
                this.productLabelList = res;
                console.log('this.productLabelList==',this.productLabelList)
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
            });
        },

        // 机构树节点选中
        nodeClick(data) {
            console.log('data=',data);
            this.productForm.departmentName = data.name;
            this.productForm.departmentId = data.id;
            this.ishowTree = false;
        },
        // 机构树数据加载
        nodeOpen(node, resolve) {
            console.log('node=',node)
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
                      //  method: 'getSubDeptsByDeptId',
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
            this.ishowTree = true;
        },

        backBtn: function () {
            if (this.onBack) {
                this.onBack(null);
            }
        },

        init(id, type) {
            this.productId = id;
            this.type = type.substring(0,2);
            // console.log('this.productId =',this.productId);
            // console.log('this.type =',this.type);
            this.getproductTypeList();
            this.getImgList();
            this.serviceObjectList = [];
            this.useScenesList = [];
        },

        getMessage(id) {
            this.$nextTick(async () => {
                let params = {
                    className:'produce',
                    method: 'getProductById',
                    param: {
                        id: id
                    }
                };
                let res = await this.$store.dispatch('http/post', params);
                for(let i in res) {
                    if(i.substring(0,13) === 'serviceObject') {
                        this.serviceObjectList.push({
                            value: res[i]
                        })
                    }
                    if(i.substring(0,9) === 'useScenes') {
                        this.useScenesList.push({
                            value: res[i]
                        })
                    }
                }

                this.productForm = res;
                console.log('this.productForm=',this.productForm);
                this.imgUrl = this.productForm.pic;

                if(this.productForm.label !== undefined) {
                    this.productForm.label = JSON.parse(this.productForm.label);
                } else {
                    this.productForm.label = []
                }

                if(this.productForm.serviceObject1 === undefined) {
                    this.serviceObjectList = [{
                        value: ''
                    }]
                }
                if(this.productForm.useScenes1 === undefined) {
                    this.useScenesList = [{
                        value: ''
                    }]
                }

                if(this.productForm.productsExpandList === undefined) {
                    this.productsExpandList = [{
                        name: '',
                        content:''
                    }]
                }

                let url = res.casePath;
                let index = url.lastIndexOf("\/");
                this.fileList = [];
                this.fileList.push({
                    name: url.substring(index+1, url.length),
                    url: res.casePath
                });

            });
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
        delUseScenes1(index) {
            this.productsExpandList.splice(index,1)
        },
        delUseScenes(index) {
            this.useScenesList.splice(index,1)
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
                // this.imgList.forEach((e, i) => {
                //     if(e.id === this.productForm.img) {
                //         this.imgUrl = e.pic
                //     }
                // });
            });
        },

        changePic() {
            this.dialog = true;
        },

        choosePic(item) {
            console.log(item);
            this.imgUrl = item.pic;
            this.productForm.img = item.id;
            this.dialog = false;
        },

        //加载页面信息
        // getMessage(id) {
        //     this.loading.form = true;
        //     this.$set(this.reportParam, 'id', id)
        //     this.$nextTick(async () => {
        //         let params = {
        //             className: 'Report',
        //             method: 'getReportInfoById',
        //             param: {id: id}
        //         }
        //         let res = await this.$store.dispatch('http/post', params);
        //         this.$set(this.reportParam, 'title', res.title);
        //         this.$set(this.reportParam, 'code', res.code);
        //         this.$set(this.reportParam, 'description', res.description);
        //         this.$set(this.reportParam, 'classifyValue', res.kindIdList);
        //         this.$set(this.reportParam, 'themeValue', res.themeList);
        //         this.$set(this.reportParam, 'industryValue', res.industryList);
        //         this.$set(this.reportParam, 'areaValue', res.areaList);
        //         this.$set(this.reportParam, 'economyValue', res.economyList);
        //         this.$set(this.reportParam, 'specialValue', res.specialList);
        //         this.$set(this.reportParam, 'labelValue', res.reportKind.label);
        //         this.$set(this.reportParam, 'url', res.url);
        //         this.fileList = [];
        //         this.fileList.push({name: res.title, url: res.url});
        //         this.getPic(res).then((result) => {
        //             this.showPicUrl = result;
        //         });
        //         this.loading.form = false;
        //     });
        // },

        //将路径转成base64位的图片
        async getPic(item) {
            let params = {
                param: item.pic,
                url: process.env.BASE_API + '/api/file/downloadfile'
            };
            let res = await this.$store.dispatch('http/fileDownload', params);
            let path = "";
            if (res != "" && res != null) {
                path = "data:image/png;base64," + res;
                item.pic = path;
            } else {
                item.pic = "";
            }
            return item.pic;
        },

        //当多选框选择发生变化时触发
        // change(e) {
        //     // console.log(e,'this')
        //     this.selectChange = [];
        //     //this.economy.find(m => m.key === e.value)
        //     if (this.reportParam.economyValue != null && this.reportParam.economyValue.length > 0) {
        //         for (let item of this.reportParam.economyValue) {
        //             this.selectChange.push(this.economy.find(m => m.key === item));
        //         }
        //     }
        //     if (this.reportParam.specialValue != null && this.reportParam.specialValue.length > 0) {
        //         for (let item of this.reportParam.specialValue) {
        //             this.selectChange.push(this.special.find(m => m.key === item));
        //         }
        //     }
        //     console.log('selectChange', this.selectChange)
        //     console.log('selectChange[0]', this.selectChange[0])
        //
        //     // this.getPic(this.selectChange[0]).then(res => {
        //     //     this.showPicUrl = res;
        //     // });
        //     // this.pic = this.selectChange[0].pic;
        //     //console.log('showPicUrl', this.showPicUrl)
        // },



        checkShow(item) {
            if(this.reportParam.themeValue!=null) {
                return this.reportParam.themeValue.includes(item);
            }else{
                return false;
            }
        },


        projectOrgFun() {
            this.ishowTree = true
        },

        closeTree(){
            this.ishowTree = false
        },

        handleNodeClick(e){
            // let res = this.$refs.tree.getCurrentKey()
            this.reportParam.productType = e.name
            this.inputId = e.code;
            this.ishowTree = false
        },


        getproductTypeList() {
            this.$nextTick(async () => {
                let params = {
                    className:'produce',
                    method: 'getProductTypes'
                };
                let res = await this.$store.dispatch('http/post', params);

                this.productTypeList = res;
                console.log('this.productTypeList=',this.productTypeList);
            });
        },

        submitReport() {

            if(this.productForm.status === '有效') {
                this.productForm.status = 1
            } else {
                this.productForm.status = 0
            }
            // console.log('this.serviceObjectList=1',this.serviceObjectList)
            this.serviceObjectList.forEach((e, i) => {
                this.productForm['serviceObject'+parseFloat(i+1)] = e.value;
            });
            this.useScenesList.forEach((e, i) => {
                this.productForm['useScenes'+parseFloat(i+1)] = e.value;
            });
            console.log("this.productsExpandList",this.productForm.productsExpandList)
            this.productForm.productsExpandList = this.productForm.productsExpandList

            console.log("this.productForm=",this.productForm);
            this.$refs.productForm.validate(valid => {
                if (valid) {
                    this.$nextTick(async () => {
                        let params = {
                            className:'produce',
                            method: 'updateproduct',
                            param: this.productForm
                        };
                        let res = await this.$store.dispatch('http/post', params);
                        console.log('res333=',res);
                        if (res) {
                            this.$message({showClose: true, message: '保存成功！', type: 'success'});
                            this.loading.btn = false;
                            setTimeout(() => {
                                if (this.onBack) {
                                    this.onBack('succ');
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
            if(this.productForm.status === '有效') {
                this.productForm.status = 1
            } else {
                this.productForm.status = 0
            }
            // console.log('this.serviceObjectList=1',this.serviceObjectList)
            this.serviceObjectList.forEach((e, i) => {
                this.productForm['serviceObject'+parseFloat(i+1)] = e.value;
            });
            this.useScenesList.forEach((e, i) => {
                this.productForm['useScenes'+parseFloat(i+1)] = e.value;
            });

            this.$refs.productForm.validate(valid => {
                if (valid) {
                    this.$nextTick(async () => {
                        let params = {
                            className:'produce',
                            method: 'updateproduct',
                            param: this.productForm
                        };
                        let res = await this.$store.dispatch('http/post', params);
                        if (res) {

                            this.$nextTick(async () => {
                                let params = {
                                    className:'produce',
                                    method:'releaseProductById',
                                    param:{
                                        id: this.productForm.id, //产品id
                                        status: '1'
                                    }
                                };
                                let res2 = await this.$store.dispatch('http/post', params);
                                if (res2) {
                                    this.$message({showClose: true, message: '发布申请提交成功！', type: 'success'});
                                    setTimeout(() => {
                                        if (this.onBack) {
                                            this.onBack('succ');
                                        }
                                    }, 500);
                                } else {
                                    this.$message({showClose: true, message: '保存失败！', type: 'error'});
                                }
                            })
                        }
                    });
                }
            })

        },

        //提交修改
        // submitReport(formName) {
        //     this.$refs[formName].validate(valid => {
        //         if (valid) {
        //             this.loading.btn = true;
        //             let obj = {
        //                 id: this.reportParam.id,
        //                 title: this.reportParam.title,
        //                 description: this.reportParam.description,
        //                 // pic: this.pic,
        //                 url: this.reportParam.url,
        //                 reportKind: {
        //                     theme: this.reportParam.themeValue != undefined && this.reportParam.themeValue.length ? this.reportParam.themeValue.join(',') : null,
        //                     industry: this.reportParam.industryValue != undefined && this.reportParam.industryValue.length && this.reportParam.themeValue.includes('hyfx') ? this.reportParam.industryValue.join(',').concat(',') : null,
        //                     kindId: this.reportParam.classifyValue != undefined && this.reportParam.classifyValue.length ? this.reportParam.classifyValue.join(',') : null,
        //                     label: this.reportParam.labelValue,
        //                     area: this.reportParam.areaValue != undefined && this.reportParam.areaValue.length && this.reportParam.themeValue.includes('qyfz')? this.reportParam.areaValue.join(',').concat(',') : null,
        //                     economy: this.reportParam.economyValue != undefined && this.reportParam.economyValue.length && this.reportParam.themeValue.includes('hgjj')? this.reportParam.economyValue.join(',').concat(',') : null,
        //                     special: this.reportParam.specialValue != undefined && this.reportParam.specialValue.length && this.reportParam.themeValue.includes('ztbg') ? this.reportParam.specialValue.join(',').concat(',') : null
        //                 }
        //             }
        //             if(obj.reportKind.theme == null && obj.reportKind.kindId == null){
        //                 this.$message('报告分类和报告专题至少选择一项');
        //             }else if(obj.reportKind.kindId.indexOf('hotHeadline') > -1 && obj.reportKind.theme == null){
        //                 this.$message('当报告分类为热点类型时至少选择一项报告专题');
        //             }else{
        //                 this.$nextTick(async () => {
        //                     let params = {className: 'Report', method: 'updateReport', param: obj};
        //                     let res = await this.$store.dispatch('http/post', params);
        //                     this.$refs[formName].clearValidate();
        //                     if (res) {
        //                         this.$message({showClose: true, message: 'success', type: 'success'});
        //                         setTimeout(() => {
        //                             if (this.onBack) {
        //                                 this.onBack('succ');
        //                             }
        //                         }, 500);
        //                     } else {
        //                         this.$message({showClose: true, message: 'error', type: 'error'});
        //                     }
        //                     // this.getFirstTableData();
        //                     this.loading.btn = false;
        //                 });
        //             }
        //         }
        //     });
        // },


    }
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

.userInput {
    width: 500px;
}

.comboSelect {
    width: 500px;
}

.imgList {
    width: 100%;
    margin: 20px 0;
    display: flex;
    flex-direction: row;
    //justify-content: space-around;
    flex-wrap: wrap;
}

.ORGTree{
    position:absolute;
    z-index: 999;
    //min-height: 50px;
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
