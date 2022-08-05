<template>
    <div>
        <el-container>
            <el-header class="header">
                <el-row class="headerBtn">
                    名字/编号:
                    <el-input v-model="DictionaryName" :maxlength="100" style="width:200px" clearable
                              placeholder="名字/编号" size="small"></el-input>
                    类别:
                    <el-select v-model="value" placeholder="请选择种类" clearable
                               size="small">
                        <el-option
                            v-for="item in datalist"
                            :key="item.code"
                            :label="item.name"
                            :value="item.code">
                        </el-option>
                    </el-select>
                    <el-button size='small' icon="el-icon-search" type="primary" @click="getDictionary">搜索</el-button>
                    <!--<el-button type="primary" icon="el-icon-plus" plain @click="dialogVisibleAdd = true" size="small">新增</el-button>-->
                    <el-button type="primary" icon="el-icon-plus" plain @click="addBtn" size="small">新增</el-button>
                </el-row>
            </el-header>
            <el-main class="table">
                <el-table
                    :data="tableData">
                    <el-table-column label="序号" type="index" align="center" width="80"></el-table-column>
                    <el-table-column prop="code" label="编号" sortable="custom" align="center"
                                     min-width="200"></el-table-column>
                    <el-table-column prop="pic" label="图片" sortable="custom" align="center" width="120">
                        <template slot-scope="scope">
                            <img :src="scope.row.pic" alt="" width="50" height="50">
                        </template>
                    </el-table-column>
                    <el-table-column prop="name" label="名称" sortable="custom" align="center"
                                     min-width="150"></el-table-column>
                    <el-table-column prop="kind" label="种类" sortable="custom" align="center"
                                     min-width="120"></el-table-column>
                    <el-table-column prop="modifyname" label="创建者" sortable="custom" align="center"
                                     min-width="120"></el-table-column>
                    <el-table-column prop="modifytime" label="时间" sortable="custom" align="center"
                                     width="200"></el-table-column>
                    <el-table-column label="操作" align="center" fixed="right" width="340">
                        <template slot-scope="scope">
                            <el-button size="small" type="info" plain @click="edit(scope.row)">编辑</el-button>
                            <!-- <el-button size="small" plain @click="dialogVisible =true;aaa(scope.row)">修改</el-button>-->
                            <el-button size="small" type="danger" plain @click="delect(scope.row)">删除</el-button>
                        </template>
                    </el-table-column>
                </el-table>
            </el-main>
            <el-footer class="footer">
                <el-pagination
                    @size-change="handleSizeChange"
                    @current-change="handleCurrentChange"
                    :current-page=currentPage
                    :page-sizes="[10, 20, 30, 40]"
                    layout="total, sizes, prev, pager, next, jumper"
                    :total=num>
                </el-pagination>
            </el-footer>
        </el-container>
        <!--        <el-dialog-->
        <!--            title="修改"-->
        <!--            :visible.sync="dialogVisible"-->
        <!--            width="60%"-->
        <!--        >-->
        <!--      <span>-->
        <!--       <el-form ref="form" :model="form" label-width="80px">-->
        <!--        <el-form-item label="编号"-->
        <!--                      prop="code"-->
        <!--                      :rules="[-->
        <!--          { required: true, message: '请输入编号', trigger: 'blur' }]">-->
        <!--          <el-input v-model="form.code" :maxlength="50" placeholder="请输入编号.."></el-input>-->
        <!--        </el-form-item>-->
        <!--        <el-form-item label="名称"-->
        <!--                      prop="name"-->
        <!--                      :rules="[-->
        <!--          { required: true, message: '请输入名称', trigger: 'blur' }]">-->
        <!--          <el-input v-model="form.name" :maxlength="50" placeholder="请输入名称.."></el-input>-->
        <!--        </el-form-item>-->
        <!--         <el-row>-->
        <!--           <el-col :span="6">-->
        <!--         <el-form-item label="类别">-->
        <!--         <el-select v-model="form.kind" placeholder="请选择种类">-->
        <!--            <el-option-->
        <!--                v-for="item in datalist"-->
        <!--                :key="item.code"-->
        <!--                :label="item.name"-->
        <!--                :value="item.code"-->
        <!--                v-if="item.code ? true:false"-->
        <!--            >-->
        <!--            </el-option>-->
        <!--          </el-select>-->
        <!--           </el-form-item>-->
        <!--           </el-col>-->
        <!--           <el-col :span="8" :offset="10">-->
        <!--             <el-form-item label="是否启用">-->
        <!--              <el-switch-->
        <!--                  v-model="form.isused"-->
        <!--                  active-color="#13ce66"-->
        <!--                  inactive-color="#ff4949"-->
        <!--                  active-text="启用"-->
        <!--                  inactive-text="停用"-->
        <!--                  active-value="1"-->
        <!--                  inactive-value="0"-->
        <!--                  @change="Enabling">-->
        <!--              </el-switch>-->
        <!--           </el-form-item>-->
        <!--           </el-col>-->
        <!--         </el-row>-->

        <!--         <el-form-item label="上传图片">-->
        <!--           <span>请重新上传图片</span>-->
        <!--          <el-upload-->
        <!--              ref="upload"-->
        <!--              class="avatar-uploader"-->
        <!--              action="https://jsonplaceholder.typicode.com/posts/"-->
        <!--              :on-remove="OnRemove"-->
        <!--              list-type="picture-card"-->
        <!--              :limit=1-->
        <!--              :http-request="myUpload1"-->
        <!--              :before-upload="beforeAvatarUpload">-->
        <!--           <img :src="form.pic" alt="" style="width: 100%;height: 100%">-->
        <!--                <i lass="el-icon-plus"></i>-->
        <!--              </el-upload>-->
        <!--         </el-form-item>-->
        <!--      </el-form>-->
        <!--       </span>-->
        <!--            <span slot="footer" class="dialog-footer">-->
        <!--     <el-button @click="dialogVisible = false">取 消</el-button>-->
        <!--     <el-button type="primary" @click="update">确 定</el-button>-->
        <!--    </span>-->
        <!--        </el-dialog>-->
        <!--        <el-dialog-->
        <!--            title="添加"-->
        <!--            :visible.sync="dialogVisibleAdd"-->
        <!--            width="60%"-->
        <!--        >-->

        <!--            <el-tabs v-model="activeName2" type="card" @tab-click="handleClick">-->
        <!--                <el-tab-pane label="添加选项" name="first">-->
        <!--                    <el-form ref="addfrom" :model="addfrom" label-width="80px">-->

        <!--                        <el-form-item label="编号"-->
        <!--                                      prop="code"-->
        <!--                                      :rules="[{ required: true, message: '请输入编号', trigger: 'blur' },-->
        <!--                      {validator: this.selectCode1, trigger: 'blur'}-->
        <!--                      ]">-->
        <!--                            <el-input v-model="addfrom.code" :maxlength="50" placeholder="请输入编号.."></el-input>-->
        <!--                            <el-col :span="1">-->
        <!--                <span v-show="isCheckCode1" class="svg-container">-->
        <!--                  <i class="el-icon-loading"></i>-->
        <!--                </span>-->
        <!--                            </el-col>-->
        <!--                        </el-form-item>-->
        <!--                        <el-form-item label="名称"-->
        <!--                                      prop="name"-->
        <!--                                      :rules="[-->
        <!--          { required: true, message: '请输入名称', trigger: 'blur' },-->
        <!--          {validator: this.selectName1, trigger: 'blur'}-->
        <!--          ]">-->
        <!--                            <el-input v-model="addfrom.name" :maxlength="50" placeholder="请输入名称.."></el-input>-->
        <!--                            <el-col :span="1">-->
        <!--                <span v-show="isCheckName1" class="svg-container">-->
        <!--                  <i class="el-icon-loading"></i>-->
        <!--                </span>-->
        <!--                            </el-col>-->
        <!--                        </el-form-item>-->
        <!--                        <el-form-item label="类别">-->
        <!--                            <el-select v-model="addfrom.kind" placeholder="请选择种类">-->
        <!--                                <el-option-->
        <!--                                    v-for="item in datalist"-->
        <!--                                    :key="item.code"-->
        <!--                                    :label="item.name"-->
        <!--                                    :value="item.code"-->
        <!--                                    v-if="item.code ? true:false"-->
        <!--                                >-->
        <!--                                </el-option>-->
        <!--                            </el-select>-->
        <!--                        </el-form-item>-->

        <!--                        <el-form-item label="上传图片">-->
        <!--                            <el-upload-->
        <!--                                ref="upload"-->
        <!--                                class="avatar-uploader"-->
        <!--                                action="https://jsonplaceholder.typicode.com/posts/"-->
        <!--                                :on-remove="OnRemove"-->
        <!--                                list-type="picture-card"-->
        <!--                                :limit=1-->
        <!--                                :http-request="myUpload"-->
        <!--                                :before-upload="beforeAvatarUpload">-->
        <!--                                <i class="el-icon-plus avatar-uploader-icon"></i>-->
        <!--                            </el-upload>-->
        <!--                        </el-form-item>-->
        <!--                    </el-form>-->

        <!--                    <div class="aaa">-->
        <!--                        <el-button @click="dialogVisibleAdd = false,cancledialog('addfrom')">取 消</el-button>-->
        <!--                        <el-button type="primary" @click="DictionaryAdd">确 定</el-button>-->
        <!--                    </div>-->

        <!--                </el-tab-pane>-->

        <!--                <el-tab-pane label="添加种类" name="second">-->
        <!--                    <el-form ref="addclass" :model="addclass" label-width="80px">-->

        <!--                        <el-form-item label="编号"-->
        <!--                                      prop="code"-->
        <!--                                      :rules="[{ required: true, message: '请输入编号', trigger: 'blur' },-->
        <!--                      {validator: this.selectCode, trigger: 'blur'}-->
        <!--                      ]">-->
        <!--                            <el-input v-model="addclass.code" :maxlength="50" placeholder="请输入编号.."></el-input>-->
        <!--                            <el-col :span="1">-->
        <!--                  <span v-show="isCheckCode" class="svg-container">-->
        <!--                    <i class="el-icon-loading"></i>-->
        <!--                  </span>-->
        <!--                            </el-col>-->
        <!--                        </el-form-item>-->
        <!--                        <el-form-item label="类名称"-->
        <!--                                      prop="name"-->
        <!--                                      :rules="[-->
        <!--          { required: true, message: '请输入种类名称', trigger: 'blur' },-->
        <!--          {validator: this.selectName, trigger: 'blur'}-->
        <!--          ]">-->
        <!--                            <el-input v-model="addclass.name" :maxlength="50" placeholder="请输入名称.."></el-input>-->
        <!--                            <el-col :span="1">-->
        <!--                <span v-show="isCheckName" class="svg-container">-->
        <!--                  <i class="el-icon-loading"></i>-->
        <!--                </span>-->
        <!--                            </el-col>-->
        <!--                        </el-form-item>-->

        <!--                    </el-form>-->

        <!--                    <div class="aaa">-->
        <!--                        <el-button @click="dialogVisibleAdd = false,cancledialog('addclass')">取 消</el-button>-->
        <!--                        <el-button type="primary" @click="classAdd">确 定</el-button>-->
        <!--                    </div>-->
        <!--                </el-tab-pane>-->
        <!--            </el-tabs>-->

        <!--        </el-dialog>-->

    </div>
</template>

<script>
//import $http from '@H'

export default {
    name: "KeyWordDialog",
    props: {
        onAdd: {
            type: Function,
            default: null
        },

        onEdit: {
            type: Function,
            default: null
        }
    },
    data() {
        return {
            currentPage: 1,  //当前页
            pageSize1: 10,
            num: 0,
            firstTotal: 0,
            DictionaryName: '',
            tableData: [],
            datalist: [],
            value: "",
            // isCheckId: false,
            // isCheckCode: false,
            // isCheckName: false,
            //
            // isCheckId1: false,
            // isCheckCode1: false,
            // isCheckName1: false,
            //
            // activeName2: 'first',
            // currentPage4: "",
            // dialogVisible: false,
            // dialogVisibleAdd: false,
            // imageUrl: '',
            // piclist: [],
            // listQuery: {
            //     importance: undefined,
            //     num: 0,
            //     totalItems: 0,
            //     title: undefined,
            //     type: undefined,
            //     sort: '+id',
            //     orderKind: undefined,
            //     orderName: undefined,
            //     searchData: {
            //         name: undefined
            //     }
            // },
            // addfrom: {
            //     // id: '',
            //     name: '',
            //     code: '',
            //     kind: '',
            //     pic: '',
            //     picUrl: '',
            //     creatorid: ''
            // },
            // addclass: {
            //     // id: '',
            //     name: '',
            //     code: '',
            //     pic: '',
            //     creatorid: ''
            // },
            // form: {
            //     id: '',
            //     name: '',
            //     code: '',
            //     kind: '',
            //     pic: '',
            //     picUrl: '',
            //     isused: '1',
            // },
            // filelist: [],
        }
    },
    created() {
        this.getDictionary();
        this.getSelect();
    },

    methods: {
        //添加
        addBtn() {
            if (this.onAdd) {
                this.onAdd(null);
            }
        },

        //编辑
        edit(row) {
            if (this.onEdit) {
                this.onEdit(row.id);
            }
        },

        //获取类名称的列表
        getSelect() {
            this.$nextTick(async () => {
                let params = {
                    className: 'Dictionary',
                    method: 'getKindnameInfo',
                    param: {}
                };
                this.datalist = await this.$store.dispatch('http/post', params);
            });
        },

        //获取字典分页列表信息
        getDictionary() {
            // console.log("登录"+this.$store.state.user.entity.id)
            this.$nextTick(async () => {
                let getDictionaryInfo = {
                    className: 'Dictionary',
                    method: 'getDictionaryInfo',
                    param: {
                        searchdata: {
                            "name": this.DictionaryName,
                            "kind": this.value
                        },
                        page: {
                            pageIndex: this.currentPage,
                            pageSize: this.pageSize1,
                        }
                    }
                };
                //debugger
                let res = await this.$store.dispatch('http/post', getDictionaryInfo);
                this.tableData = res.dataList;
                this.num = res.page.totalNum;
                this.getPic(this.tableData);
            });

        },

        //将路径转成base64位的图片
        getPic(tableData) {
            let base = this;
            tableData.forEach(function (item) {
                //console.log("数据"+item);
                //console.log("数据框中的数据"+item);
                base.$nextTick(async () => {
                    let pic = {
                        param: item.pic,
                        url: process.env.BASE_API + '/api/file/downloadfile'
                    };
                    let res = await base.$store.dispatch('http/fileDownload', pic);
                    let path = "";
                    if (res != "" && res != null) {
                        path = "data:image/png;base64," + res;
                    } else {
                        path = "";
                    }
                    if (path != "") {
                        item.pic = path;
                    }
                });
            });
        },

        //删除功能
        delect(row) {
            this.$confirm('此操作将永久删除该字典, 是否继续?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                this.$nextTick(async () => {
                    let params = {
                        className: 'Dictionary',
                        method: 'deleteDictionary',
                        param: {
                            id: row.id
                        }
                    };
                    let res = await this.$store.dispatch('http/post', params);
                    if (res == 200) {
                        this.getDictionary();
                        this.$message({message: '删除成功', type: 'success'});
                    } else {
                        this.$message({showClose: true, message: '删除失败！', type: 'error'});
                    }
                });
            }).catch(() => {
                this.$message({
                    type: 'info',
                    message: '已取消删除'
                });
            });
        },

        //分页方法
        handleSizeChange(val) {
            console.log(`A每页 ${val} 条`);
            console.log(`A当前页: ${this.currentPage}`);
            this.currentPage = 1;
            this.pageSize1 = val;
            this.getDictionary();
        },

        handleCurrentChange(val) {
            console.log(`B每页 ${this.pageSize1} 条`);
            console.log(`B当前页: ${val}`);
            //val - 1;
            //debugger
            this.currentPage = val;
            this.getDictionary();
        },

        //停用
        // Enabling(val) {
        //     console.log('change', this.form.isused)
        //     let change = '';
        //     if (val) {
        //         change = "1"
        //     } else {
        //         change = "0"
        //     }
        // },
        // OnRemove(file) {
        //     this.addfrom.pic = "";
        // },
        //
        // //图片上传覆盖默认的上传行为
        // myUpload(content) {
        //     this.$nextTick(async () => {
        //         let fd = new FormData();
        //         fd.append('file', content.file);
        //         let params = {param: fd, url: '/api/file/fileupload'};
        //         let response = await this.$store.dispatch('http/fileUpload', params);
        //         if (response) {
        //             this.addfrom.pic = response
        //             this.$message({
        //                 message: '上传成功'
        //             });
        //         } else {
        //             this.$message({
        //                 message: '上传失败'
        //             });
        //         }
        //     })
        // },
        // myUpload1(content) {
        //     this.$nextTick(async () => {
        //         let fd = new FormData();
        //         fd.append('file', content.file);
        //         let params = {param: fd, url: '/api/file/fileupload'};
        //         let response = await this.$store.dispatch('http/fileUpload', params);
        //         if (response) {
        //             this.form.pic = response
        //             this.$message({
        //                 message: '上传成功'
        //             });
        //         } else {
        //             this.$message({
        //                 message: '上传失败'
        //             });
        //         }
        //     })
        // },
        // beforeAvatarUpload(file) {
        //     const extension = file.name.split('.')[file.name.split('.').length - 1] === 'jpg'
        //     const extension2 = file.name.split('.')[file.name.split('.').length - 1] === 'png'
        //     const extension3 = file.name.split('.')[file.name.split('.').length - 1] === 'gif'
        //     const isLt2M = file.size / 1024 / 1024 < 10
        //     if (!extension && !extension2 && !extension3) {// && !extension4 && !extension5) {
        //         this.$message({
        //             message: '上传模板只能是 jpg、png、gif、pdf、word格式!',
        //             type: 'error',
        //             duration: 2500
        //         });
        //         this.$refs.upload.clearFiles();
        //     }
        //     if (!isLt2M) {
        //         this.$message({
        //             message: '上传模板大小不能超过 10MB!',
        //             type: 'error',
        //             duration: 2500
        //         });
        //         this.$refs.upload.clearFiles();
        //     }
        //     return extension || extension2 || extension3 || extension4 || extension5 && isLt2M
        // },
        // //增加功能
        // DictionaryAdd() {
        //     //alert("ssss");
        //     this.$nextTick(async () => {
        //         let addDictionary = {
        //             className: 'Dictionary',
        //             method: 'addDictionary',
        //             param: {
        //                 "id": this.addfrom.id,
        //                 "code": this.addfrom.code,
        //                 "name": this.addfrom.name,
        //                 "kind": this.addfrom.kind,
        //                 "pic": this.addfrom.pic,
        //                 "creatorid": this.$store.state.user.entity.id,
        //             }
        //         };
        //         let repose = await this.$store.dispatch('http/post', addDictionary);
        //         console.log("返回值" + repose);
        //         if (repose == 200) {
        //             this.$message({
        //                 showClose: true,
        //                 message: '增加成功！'
        //             });
        //
        //         } else {
        //             this.$message({
        //                 showClose: true,
        //                 message: '操作失败！'
        //             });
        //         }
        //         this.getDictionary();
        //         this.dialogVisibleAdd = false;
        //     });
        //     this.getDictionary();
        // },
        // //增加大类
        // classAdd() {
        //     //alert("class");
        //     this.$nextTick(async () => {
        //         let addclassDictionary = {
        //             className: 'Dictionary',
        //             method: 'addclassDictionary',
        //             param: {
        //                 // "id":this.addclass.id,
        //                 "code": this.addclass.code,
        //                 "name": this.addclass.name,
        //                 "creatorid": this.$store.state.user.entity.id,
        //             }
        //         };
        //         let repose = await this.$store.dispatch('http/post', addclassDictionary);
        //         console.log("返回值" + repose);
        //         if (repose == 200) {
        //             this.$message({
        //                 showClose: true,
        //                 message: '增加成功！'
        //             });
        //             this.getselect();
        //         } else {
        //             this.$message({
        //                 showClose: true,
        //                 message: '操作失败！'
        //             });
        //         }
        //         this.getselect();
        //         this.dialogVisibleAdd = false;
        //     });
        //     this.getselect();
        // },
        // //编辑功能
        // aaa(row) {
        //
        //     this.form = {
        //         id: row.id,
        //         code: row.code,
        //         name: row.name,
        //         pic: row.pic,
        //         kind: row.kindCode,
        //         isused: row.isused
        //     };
        //     console.log(this.form)
        // },
        // update() {
        //     //alert("修改")
        //     this.$nextTick(async () => {
        //         let updateDictionary = {
        //             className: 'Dictionary',
        //             method: 'updateDictionary',
        //             param: {
        //                 "id": this.form.id,
        //                 "code": this.form.code,
        //                 "name": this.form.name,
        //                 "kind": this.form.kind,
        //                 "pic": this.form.pic,
        //                 "creatorid": this.$store.state.user.entity.id,
        //                 "modifierid": this.$store.state.user.entity.id,
        //                 "isused": this.form.isused,
        //             }
        //         };
        //         console.log(updateDictionary)
        //         let repose = await this.$store.dispatch('http/post', updateDictionary);
        //         console.log("返回值" + repose);
        //         if (repose == 200) {
        //             //debugger
        //             this.$message({
        //                 showClose: true,
        //                 message: '修改成功！'
        //             });
        //             this.getDictionary();
        //             this.dialogVisible = false;
        //
        //         } else {
        //             this.$message({
        //                 showClose: true,
        //                 message: '修改失败！'
        //             });
        //         }
        //         this.dialogVisible = false;
        //
        //     });
        //
        // },

        // handleClick() {
        // },

        // selectName(rule, value, callback) {
        //     //name存在吗
        //     if (value === '') {
        //         callback(new Error('请输入名称'));
        //     } else {
        //         if (value !== '') {
        //             //this.isCheckName = true;
        //             this.$nextTick(async () => {
        //                 let selectName = {
        //                     className: 'Dictionary',
        //                     method: 'selectName',
        //                     param: {
        //                         "name": this.addclass.name,
        //                     }
        //                 };
        //                 let response = await this.$store.dispatch('http/post', selectName);
        //                 //console.log("返回值"+repose);
        //                 if (response == 200) {
        //                     callback(new Error('名称存在'));
        //                 } else {
        //                     callback();
        //                 }
        //                 that.isCheckName = false;
        //             });
        //         }
        //     }
        // },
        // selectCode(rule, value, callback) {
        //     //code存在吗
        //     if (value === '') {
        //         callback(new Error('请输入编号'));
        //     } else {
        //         if (value !== '') {
        //             // this.isCheckCode = true;
        //             this.$nextTick(async () => {
        //                 let selectCode = {
        //                     className: 'Dictionary',
        //                     method: 'selectCode',
        //                     param: {
        //                         "code": this.addclass.code,
        //                     }
        //                 };
        //                 let response = await this.$store.dispatch('http/post', selectCode);
        //                 //console.log("返回值"+repose);
        //                 if (response == 200) {
        //                     callback(new Error('编码存在'));
        //                 } else {
        //                     callback();
        //                 }
        //                 that.isCheckCode = false;
        //             });
        //         }
        //     }
        // },
        //
        // selectName1(rule, value, callback) {
        //     //name存在吗
        //     if (value === '') {
        //         callback(new Error('请输入名称'));
        //     } else {
        //         if (value !== '') {
        //             //this.isCheckName1 = true;
        //             this.$nextTick(async () => {
        //                 let selectName1 = {
        //                     className: 'Dictionary',
        //                     method: 'selectName1',
        //                     param: {
        //                         "name": this.addfrom.name,
        //                     }
        //                 };
        //                 let response = await this.$store.dispatch('http/post', selectName1);
        //                 //console.log("返回值"+repose);
        //                 if (response == 200) {
        //                     callback(new Error('名称存在'));
        //                 } else {
        //                     callback();
        //                 }
        //                 that.isCheckName1 = false;
        //             });
        //         }
        //     }
        // },
        // selectCode1(rule, value, callback) {
        //     //code存在吗
        //     if (value === '') {
        //         callback(new Error('请输入编号'));
        //     } else {
        //         if (value !== '') {
        //             //this.isCheckCode1 = true;
        //             this.$nextTick(async () => {
        //                 let selectCode1 = {
        //                     className: 'Dictionary',
        //                     method: 'selectCode1',
        //                     param: {
        //                         "code": this.addfrom.code,
        //                     }
        //                 };
        //                 let response = await this.$store.dispatch('http/post', selectCode1);
        //                 //console.log("返回值"+repose);
        //                 if (response == 200) {
        //                     callback(new Error('编码存在'));
        //                 } else {
        //                     callback();
        //                 }
        //                 that.isCheckCode1 = false;
        //             });
        //         }
        //     }
        // },
    }

}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
.header {
    background-color: rgb(240, 244, 245);
    text-align: left;
}

.headerBtn {
    margin-top: 13px;
}

.btnCss {
    margin-left: 10px;
}

.footer {
    background-color: rgb(240, 244, 245);
    text-align: left;
    padding-top: 15px;
}

/*.aaa {*/
/*    float: right;*/
/*}*/

.table {
    width: 100%;
    padding: 1px 22px 15px 22px;

    .el-table {
        width: 100%;
        text-align: center;
        border: 1px solid #bbc8c1
    }
}

</style>
