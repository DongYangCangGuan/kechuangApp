<template>
    <!--20200922-xues-->
    <div>
        <el-container>
            <el-col :span="24" class="toolbar" style="padding-bottom: 0px;">
                <el-form :inline="true" :model="Searchfilter">
                    <el-form-item>
                        <el-input v-model="Searchfilter.filetitle" placeholder="输入文件名" :clearable="true"></el-input>
                    </el-form-item>
                    <el-form-item>
                        <el-select v-model="Searchfilter.filetype" placeholder="选择类型" :clearable="true" @change="change1">
                            <el-option v-for="(item,index) in items" :key="index" :label="item.name" :value="item.id"></el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item>
                        <el-button type="primary" icon="el-icon-search" plain @click="Search()">查询</el-button>
                    </el-form-item>
                    <el-form-item>
                        <el-button type="primary" icon="el-icon-plus" plain @click="AddList(),dialogVisibleAdd = true">
                            添加
                        </el-button>
                    </el-form-item>
                    <el-form-item>
                        <el-button type="primary" icon="el-icon-search" plain @click="ImportExam()">导入</el-button>
                    </el-form-item>
                    <el-form-item>
                        <el-button type="primary" icon="el-icon-document" @click="ImportExamTemplate()">
                            模版下载
                        </el-button>
                    </el-form-item>
                </el-form>
            </el-col>

            <!--列表-->
            <el-table :data="files" border stripe highlight-current-row v-loading="listLoading"
                      @selection-change="selsChange" style="width: 100%;">
                <el-table-column type="selection" width="55"></el-table-column>
                <el-table-column type="index" width="50" label="序号"></el-table-column>
                <el-table-column prop="itemContent" label="问题内容"></el-table-column>
                <el-table-column prop="examType" label="专栏类型" width="100"></el-table-column>
                <el-table-column prop="answerA" label="答案A" width="250"></el-table-column>
                <el-table-column prop="answerB" label="答案B" width="250"></el-table-column>
                <el-table-column prop="answerC" label="答案C" width="250"></el-table-column>
                <el-table-column prop="answerD" label="答案D" width="250"></el-table-column>
                <el-table-column prop="rightanswer" label="正确答案" width="100"></el-table-column>
                <el-table-column label="操作" width="150">
                    <template slot-scope="scope">
                        <el-button scope.idtype="text" type="primary" plain size="small" @click="handleEdit(scope.$index, scope.row)">修改</el-button>
                        <el-button type="danger" size="small" @click="handleDel(scope.$index, scope.row)">删除</el-button>
                    </template>
                </el-table-column>
            </el-table>

            <!--工具条-->
            <el-footer class="footer">
                <el-button type="danger" @click="batchRemove" :disabled="this.sels.length===0">批量删除</el-button>
                <el-pagination background
                               @size-change="handleSizeChange"
                               @current-change="handleCurrentChange"
                               :current-page="listQuery.pageIndex"
                               :page-sizes="[10,20,30, 50]"
                               :page-size="listQuery.pageSize"
                               layout="total, sizes, prev, pager, next, jumper"
                               :total="listQuery.total" style="float:right;">
                </el-pagination>
            </el-footer>
        </el-container>


        <!--添加文件-->
        <el-dialog
                title="添加"
                :visible.sync="dialogVisibleAdd"
                width="80%" top="4vh">
      <span>
       <el-form ref="addfrom" :model="addfrom" label-width="80px">
        <el-form-item label="问题内容" prop="itemContent"
                      :rules="[{ required: true, message: '请输入问题内容', trigger: 'blur' }]">
          <el-input v-model="addfrom.itemContent" type="textarea" placeholder="请输入问题内容.."></el-input>
        </el-form-item>

        <el-form-item label="专栏类型" prop="type" :rules="[{ required: true, message: '请选择专栏类型', trigger: 'change' }]">
            <el-select v-model="addfrom.type" placeholder="请选择" @change="change">
                <el-option v-for="(item,index) in items" :key="index" :label="item.name" :value="item.id"></el-option>
            </el-select>
        </el-form-item>

        <el-form-item label="答案A" prop="answerA" :rules="[{ required: true, message: '请输入答案A', trigger: 'blur'}]">
          <el-input v-model="addfrom.answerA" :maxlength="50" placeholder="请输入答案A.."></el-input>
        </el-form-item>

        <el-form-item label="答案B" prop="answerB" :rules="[{ required: true, message: '请输入答案B', trigger: 'blur'}]">
          <el-input v-model="addfrom.answerB" :maxlength="50" placeholder="请输入答案B.."></el-input>
        </el-form-item>

        <el-form-item label="答案C" prop="answerC" :rules="[{ required: true, message: '请输入答案C', trigger: 'blur'}]">
          <el-input v-model="addfrom.answerC" :maxlength="50" placeholder="请输入答案C.."></el-input>
        </el-form-item>

        <el-form-item label="答案D" prop="answerD" :rules="[{ required: true, message: '请输入答案D', trigger: 'blur'}]">
          <el-input v-model="addfrom.answerD" :maxlength="50" placeholder="请输入答案D.."></el-input>
        </el-form-item>

        <el-form-item label="正确答案" prop="rightanswer" :rules="[{ required: true, message: '请输入正确答案', trigger: 'blur'}]">
          <el-input v-model="addfrom.rightanswer" :maxlength="50" placeholder="请输入正确答案.."></el-input>
        </el-form-item>
      </el-form>
       </span>
            <span slot="footer" class="dialog-footer">
     <el-button @click="dialogVisibleAdd = false">取 消</el-button>
     <el-button type="primary" @click="addSubmit">确 定</el-button>
    </span>
        </el-dialog>


        <!--修改文件:disabled="true"-->
        <el-dialog title="修改" :visible.sync="dialogVisibleEdit" width="80%" top="4vh">
      <span>
          <el-form ref="editfrom" :model="editfrom" label-width="80px">
        <el-form-item label="问题内容" prop="itemContent"
                      :rules="[{ required: true, message: '请输入问题内容', trigger: 'blur' }]">
          <el-input v-model="editfrom.itemContent" type="textarea" placeholder="请输入问题内容.."></el-input>
        </el-form-item>
        <el-form-item label="专栏类型" prop="type" :rules="[{ required: true, message: '请选择专栏类型', trigger: 'change' }]">
            <el-select v-model="editfrom.type" placeholder="请选择" @change="change">
                <el-option v-for="(item,index) in items" :key="index" :label="item.name" :value="item.id"></el-option>
            </el-select>
        </el-form-item>
        <el-form-item label="答案A" prop="answerA" :rules="[{ required: true, message: '请输入答案A', trigger: 'blur'}]">
          <el-input v-model="editfrom.answerA" :maxlength="50" placeholder="请输入答案A.."></el-input>
        </el-form-item>

        <el-form-item label="答案B" prop="answerB" :rules="[{ required: true, message: '请输入答案B', trigger: 'blur'}]">
          <el-input v-model="editfrom.answerB" :maxlength="50" placeholder="请输入答案B.."></el-input>
        </el-form-item>

        <el-form-item label="答案C" prop="answerC" :rules="[{ required: true, message: '请输入答案C', trigger: 'blur'}]">
          <el-input v-model="editfrom.answerC" :maxlength="50" placeholder="请输入答案C.."></el-input>
        </el-form-item>

        <el-form-item label="答案D" prop="answerD" :rules="[{ required: true, message: '请输入答案D', trigger: 'blur'}]">
          <el-input v-model="editfrom.answerD" :maxlength="50" placeholder="请输入答案D.."></el-input>
        </el-form-item>

        <el-form-item label="正确答案" prop="rightanswer" :rules="[{ required: true, message: '请输入正确答案', trigger: 'blur'}]">
          <el-input v-model="editfrom.rightanswer" :maxlength="50" placeholder="请输入正确答案.."></el-input>
        </el-form-item>
      </el-form>
       </span>
            <span slot="footer" class="dialog-footer">
     <el-button @click="dialogVisibleEdit = false">取 消</el-button>
     <el-button type="primary" @click="updateSubmit">确 定</el-button>
    </span>
        </el-dialog>
        <el-dialog title="导入" :visible.sync="dialogVisibleImport">
            <el-form :model="importForm">
                <el-form-item label="专栏类型">
                    <el-select v-model="importForm.type" placeholder="请选择" @change="change">
                        <el-option v-for="(item,index) in items" :key="index" :label="item.name" :value="item.id"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="文件">
                    <upload-excel-component :on-success="handleSuccess" :before-upload="beforeUpload" v-model="importForm.file"/>
                    <div style="margin-left: 16%;">
                        <span>{{importFile}}</span>
                        <i v-if="importFile" class="el-icon-check"></i>
                    </div>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click="dialogVisibleImport = false">取 消</el-button>
                <el-button type="primary" @click="sunmitImport()">确 定</el-button>
            </div>
        </el-dialog>
    </div>
</template>

<script>
    import UploadExcelComponent from '@DOC/components/UploadExcel/index.vue'
    export default {
        components: { UploadExcelComponent },
        data() {
            return {
                files: [],
                Searchfilter: {
                    filetitle: '',
                    filetype: ''
                },
                listLoading: false,
                dialogVisibleAdd: false,
                dialogVisibleEdit: false,
                dialogVisibleImport:false,
                items: [],
                sels: [],//列表选中列
                addfrom: {},
                editfrom: {},
                importForm:{},
                listQuery: {
                    pageIndex: 1,//当前页
                    pageSize: 10,//每页大小
                    pageCount: 0,//总页数
                    total: 0,// 总条目数
                    currentPage: 1,
                    currentItem: 1,
                    importance: undefined,
                    totalItems: 0,
                    title: undefined,
                    type: undefined,
                    sort: '+id',
                    orderKind: 'desc',
                    orderName: undefined,
                    searchData: {filetitle: undefined}
                },
                importTableData:[],
                importFile:"",
                importTemplate:[{
                    item_content:"国庆是哪一天？",
                    answerA:"三月八日",
                    answerB:"五月四日",
                    answerC:"六月一日",
                    answerD:"十月一日",
                    rightanswer:"D",
                }],
                filename:"exam_template",
                bookType:"xlsx",
                autoWidth: true
            }
        },
        methods: {
            sunmitImport(){
                if(!this.importForm.type){
                    this.$message.error('请选择专栏类型');
                    return;
                }
                if(!this.importTableData.length>0){
                    this.$message.error('未选择导入文件，或文件为空');
                    return;
                }
                this.$confirm('确定导入吗？', '提示', {}).then(() => {
                    let _this = this;
                    this.$nextTick(async () => {
                        let param = {
                            importTableData: this.importTableData,
                            type:this.importForm.type
                        }
                        const params = {className: 'Exam', method: 'importExam', param: param};

                        let response = await this.$store.dispatch('http/post', params);
                        this.dialogVisibleImport=false;
                        if(response==200){
                            this.$message({
                                message: '上传成功',
                                type: 'success'
                            });
                            this.getExamList();
                        }else {
                            this.$message.error('上传失败');
                        }
                    })
                });
            },
            beforeUpload(file) {
                return true
                // const isLt1M = file.size / 1024 / 1024 < 1
                // if (isLt1M) {
                //     return true
                // }
                // this.$message({
                //     message: 'Please do not upload files larger than 1m in size.',
                //     type: 'warning'
                // })
                // return false
            },
            handleSuccess({ name,results, header }) {
                this.importTableData = results;
                this.importFile=name;
            },
            ImportExam(){
                this.importFile="";
                this.dialogVisibleImport=true;
            },
            ImportExamTemplate(){
                import('@COM/vendor/Export2Excel').then(excel => {
                    const tHeader = ['item_content', 'answerA', 'answerB', 'answerC', 'answerD','rightanswer']
                    const filterVal = ['item_content', 'answerA', 'answerB', 'answerC', 'answerD','rightanswer']
                    const list = this.importTemplate
                    const data = this.formatJson(filterVal, list)
                    excel.export_json_to_excel({
                        header: tHeader,
                        data,
                        filename: this.filename,
                        autoWidth: this.autoWidth,
                        bookType: this.bookType
                    })
                })
            },
            formatJson(filterVal, jsonData) {
                return jsonData.map(v => filterVal.map(j => {
                    if (j === 'timestamp') {
                        return parseTime(v[j])
                    } else {
                        return v[j]
                    }
                }))
            },
            change: function () {
                this.getExamtype();
            },
            change1: function () {
                this.getExamList();
            },
            //获取考试类型
            getExamtype() {
                let _this = this;
                this.$nextTick(async () => {
                    const params = {className: 'Exam', method: 'getExamtype'};
                    let response = await this.$store.dispatch('http/post', params);
                    this.items = response;
                })
            },
            selsChange: function (sels) {
                this.sels = sels;
            },
            AddList() {
                for (var name in this.$data.addfrom) {
                    this.$data.addfrom[name] = ""
                }
            },

            sort(val) {
                this.order(val.order);
                val.prop == "filetitle"

                this.listQuery.orderName = val.prop;
                this.getExamList();
            },
            order(val) {
                if (val == "descending") {
                    this.listQuery.orderKind = 'desc'
                } else if (val == "ascending") {
                    this.listQuery.orderKind = 'asc'
                }
            },

            handleSizeChange(val) {
                this.listQuery.pageSize = val;
                this.getExamList();
            },
            handleCurrentChange(val) {
                this.listQuery.pageIndex = val;
                this.getExamList();
            },

            //搜索
            Search() {
                this.getExamList();
            },
            //新增
            addSubmit() {
                this.$refs.addfrom.validate((valid) => {
                    if (valid) {
                        this.$confirm('确定提交吗？', '提示', {}).then(() => {
                            this.addLoading = true;
                            this.$nextTick(async () => {
                                let params = {
                                    className: 'Exam',
                                    method: 'addExam',
                                    param: this.addfrom
                                }
                                let res = await this.$store.dispatch("http/post", params);
                                this.addLoading = false;
                                if (res === "200") {
                                    this.$message({message: '发布成功', type: 'success'});
                                } else {
                                    this.$message({message: '发布失败', type: 'failed'});
                                }
                                this.AddList();
                                this.dialogVisibleAdd = false;
                                this.getExamList();
                            })
                        });
                    }
                });
            },
            //修改
            updateSubmit() {
                this.$refs.editfrom.validate((valid) => {
                    if (valid) {
                        this.$confirm('确定提交吗？', '提示', {}).then(() => {
                            this.addLoading = true;
                            this.$nextTick(async () => {
                                let params = {
                                    className: 'Exam',
                                    method: 'updateExam',
                                    param: this.editfrom
                                }
                                let res = await this.$store.dispatch("http/post", params);
                                if (res === "200") {
                                    this.$message({message: '提交成功', type: 'success'});
                                } else {
                                    this.$message({message: '删除失败', type: 'failed'});
                                }
                                this.AddList();
                                this.dialogVisibleEdit = false;
                                this.getExamList();
                            })
                        }).then(() => {
                        });
                    }
                });
            },
            //获取用户列表
            getExamList() {
                let _this = this;
                this.$nextTick(async () => {
                    let param = {
                        searchData: {
                            searchName: _this.Searchfilter.filetitle,
                            type: _this.Searchfilter.filetype,
                        },
                        page: {
                            pageIndex: _this.listQuery.pageIndex,
                            pageSize: _this.listQuery.pageSize
                        }
                    }
                    // const params = {className:'VideoFile', method:'getVideofilebypage', param:param};
                    const params = {className: 'Exam', method: 'getExamList', param: param};

                    let response = await this.$store.dispatch('http/post', params);
                    this.files = response.dataList;
                    this.listQuery.total = response.page.totalNum;
                })
            },
            //update
            handleEdit(index, row) {
                this.dialogVisibleEdit = true;
                this.editfrom = Object.assign({}, row);
            },

            //批量删除
            batchRemove() {
                var questionNos = this.sels.map(item => item.questionNo).toString();
                this.$confirm('确认删除选中数据吗？', '提示', {
                    type: 'warning'
                }).then(() => {
                    this.$nextTick(async () => {
                        let param = {
                            questionNos: questionNos
                        }
                        let params = {
                            className: 'Exam',
                            method: 'batchDelExam',
                            param: param
                        }
                        let res = await this.$store.dispatch("http/post", params);
                        if (res === "200") {
                            this.listLoading = false;
                            this.$message({message: '删除成功', type: 'success'});
                        } else {
                            this.$message({message: '删除失败', type: 'failed'});
                        }
                        this.getExamList();
                    })
                }).catch(() => {

                });
            },
            //删除
            handleDel(index, row) {
                this.$confirm('确认删除该数据吗?', '提示', {
                    type: 'warning'
                }).then(() => {
                    this.listLoading = true;
                    this.$nextTick(async () => {
                        let param = {
                            questionNo: row.questionNo
                        }
                        let params = {
                            className: 'Exam',
                            method: 'delExam',
                            param: param
                        }
                        let res = await this.$store.dispatch("http/post", params);
                        if (res === "200") {
                            this.listLoading = false;
                            this.$message({message: '删除成功', type: 'success'});
                        } else {
                            this.$message({message: '删除失败', type: 'failed'});
                        }
                        this.getExamList();
                    })
                }).catch(() => {
                });
            },
        },
        mounted() {
            this.getExamList();
            this.getExamtype();
        }
    }
</script>

<style scoped>
    .drop[data-v-74bb1a4b] {
        margin: 0 70px !important;
    }
    .panel-group .card-panel[data-v-6e0e84be] {
        height: 54rem;
    }
    .header {
        background-color: rgb(240, 244, 245);
        text-align: left;
        margin-bottom: 10px;
    }

    .headerbtn {
        /*margin-top: 15px;*/
        text-align: right;
        line-height: 5;
    }

    .toolbar {
        margin-top: 1rem;
    }

    .footer {
        padding-top: 1rem;
    }
</style>
