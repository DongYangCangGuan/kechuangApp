<template>
    <div class="currentDiv">
        <el-header class="header">
            <el-row class="headerbtn">
                <span class="title">产品信息</span>
            </el-row>
        </el-header>

        <el-main class="content">
            <div>
                <div>
<!--                    <span class="labelStyle"></span>-->
                </div>
                <div class="memberInfo">
                    <el-row class="panel-group">
                        <el-col>
                            产品图片：<img width="100" height="100" :src="form.pic">
                        </el-col>
                    </el-row>

                    <el-row class="panel-group" :getter="24">
                        <el-col>
                            产品编号：{{ form.id }}
                        </el-col>
                    </el-row>
                    <el-row class="panel-group" :getter="24">
                        <el-col>
                            产品名称：{{form.productName}}
                        </el-col>
                    </el-row>
                    <el-row class="panel-group" :getter="24">
                        <el-col>
                            产品类型：{{form.typeName}}
                        </el-col>
                    </el-row>

                    <el-row class="panel-group" :getter="24">
                        <el-col>
                            产品状态：{{ form.status = 1? '有效' : '无效' }}
                        </el-col>
                    </el-row>

                    <el-row class="panel-group" :getter="24" v-if="form.access !== undefined && form.access !== ''">
                        准入条件：
                        <el-col>

<!--                            <span style="display: inline-block;width: 80%;vertical-align: top;">{{ form.access }}</span>-->
<!--                            <el-form-item label="准入条件" prop="access">-->
                            <el-input class="userInput"
                                      type="textarea"
                                      :rows="5"
                                      :autosize="{ minRows: 2, maxRows: 100}"
                                      v-model="form.access"
                                      readonly
                                      ><template slot="prepend"></template></el-input>
<!--                            </el-form-item>-->
                        </el-col>
                    </el-row>




                    <el-row class="panel-group">
                        <el-col>
                            产品描述：{{form.introduction}}
                        </el-col>
                    </el-row>

<!--                    <el-row class="panel-group">-->
<!--                        <el-col>-->
<!--                            产品归属：{{form.ascription}}-->
<!--                        </el-col>-->
<!--                    </el-row>-->

                    <el-row class="panel-group" :getter="24">
                        <el-col>
                            合作方：{{ form.departmentName }}
                        </el-col>
                    </el-row>

                    <el-row class="panel-group" :getter="24">
                        <el-col>
                            服务对象：{{ form.serviceObject1 }} <span v-if="form.serviceObject1 !== undefined && form.serviceObject1 !== ''">；</span>
                                    {{ form.serviceObject2 }} <span v-if="form.serviceObject2 !== undefined && form.serviceObject2 !== ''">；</span>
                                    {{ form.serviceObject3 }} <span v-if="form.serviceObject3 !== undefined && form.serviceObject3 !== ''">；</span>
                                    {{ form.serviceObject4 }} <span v-if="form.serviceObject4 !== undefined && form.serviceObject4 !== ''">；</span>
                                    {{ form.serviceObject5 }} <span v-if="form.serviceObject5 !== undefined && form.serviceObject5 !== ''">；</span>
                                    {{ form.serviceObject6 }} <span v-if="form.serviceObject6 !== undefined && form.serviceObject6 !== ''">；</span>
                        </el-col>
                    </el-row>


                    <el-row class="panel-group" :getter="24">
                        <el-col>
                            使用场景：{{ form.useScenes1 }} <span v-if="form.useScenes1 !== undefined && form.useScenes1 !== ''">；</span>
                                    {{ form.useScenes2 }} <span v-if="form.useScenes2 !== undefined && form.useScenes2 !== ''">；</span>
                                    {{ form.useScenes3 }} <span v-if="form.useScenes3 !== undefined && form.useScenes3 !== ''">；</span>
                                    {{ form.useScenes4 }} <span v-if="form.useScenes4 !== undefined && form.useScenes4 !== ''">；</span>
                                    {{ form.useScenes5 }} <span v-if="form.useScenes5 !== undefined && form.useScenes5 !== ''">；</span>
                                    {{ form.useScenes6 }} <span v-if="form.useScenes6 !== undefined && form.useScenes6xx !== ''">；</span>
                        </el-col>
                    </el-row>

                    <el-row class="panel-group" :getter="24">
                        <el-col>
                            产品亮点：{{ form.productFeatures }}
                        </el-col>
                    </el-row>
<!--v-if="type === '11' || type === '14' || type === '21'"-->
                    <el-row class="panel-group" :getter="24" >
                        <el-col>
                            贷款金额：{{ form.loanAmount }}
                        </el-col>
                    </el-row>
<!--v-if="type === '11' || type === '14' || type === '21' || type === '24' || type === '26'"-->
                    <el-row class="panel-group" :getter="24" >
                        <el-col>
                            期限：{{ form.timeLimit }}
                        </el-col>
                    </el-row>
<!--v-if="type === '11' || type === '14' || type === '21' || type === '24' || type === '26'"-->
                    <el-row class="panel-group" :getter="24"  >
                        <el-col>
                            利率：{{ form.interestRate }}
                        </el-col>
                    </el-row>

                    <el-row class="panel-group" :getter="24" v-if="type === '11'">
                        <el-col>
                            融资额度：{{ form.financingAmount }}
                        </el-col>
                    </el-row>

                    <el-row class="panel-group" :getter="24" v-if="type === '11'">
                        <el-col>
                            还款节奏：{{ form.repaymentRhythm }}
                        </el-col>
                    </el-row>

                    <el-row class="panel-group" :getter="24">
                        <el-col>
                            案例描述：{{ form.caseDescription }}
                            <el-button size="mini" type="primary" v-if="form.casePath && form.casePath !== ''" icon="el-icon-download" @click="downloadFile">下载附件</el-button>
                        </el-col>
                    </el-row>

                    <el-row class="panel-group" :getter="24">
                        <el-col>
                            产品标签：
                            <span v-for="(item,index) in labelList" :key="index">{{item+';'}}</span>
                        </el-col>
                    </el-row>

                    <el-row class="panel-group" :getter="24">
                        <el-col>
                            产品拓展标签：
                            <span v-for="(item,index) in form.productsExpandList" :key="index">{{item.name+';'}}{{item.content+";"}}</span>
                        </el-col>
                    </el-row>

                    <el-row class="panel-group">
                        <el-col>
                            客户经理：{{form.linkMan}}
                        </el-col>
                    </el-row>
                    <el-row class="panel-group">
                        <el-col>
                            联系方式：{{form.linkPhone}}
                        </el-col>
                    </el-row>
                    <el-row class="panel-group">
                        <el-col>
                            邮箱：{{form.linkEmail}}
                        </el-col>
                    </el-row>



                </div>

                <el-button type="info"
                           icon="el-icon-back"
                           plain
                           @click="backBtn">返回
                </el-button>
            </div>


        </el-main>
    </div>
</template>

<script>
import axios from 'axios';
import qs from 'qs';
import store from '@MAN/store'
export default {
    name: "report-detail",
    props: {
        //子页面调用父页面
        onBack: {
            type: Function,
            default: null
        }
    },
    data() {
        return {
            firstTableData: [],
            paginationData: {
                currentPage: 1,
                pageSize: 10,
                total: 0
            },
            searchData: {
                // memberId: '',
                // userName: '',
            },
            form: {},
            loading: {
                form: false,
                btn: false,
            },
            productId: '',
            type: '',
            imgList: [],
            imgUrl: '',
            productLabelList: [],
            labelList: [],
            fileList: []
        }
    },

    methods: {
        init(id,type) {
            // this.searchData = {
            //     // memberId: '',
            //     // userName: '',
            // };
            // this.searchData.memberId = id;
            this.productId = id;
            this.type = type.substring(0,2);
            console.log("this.type=",this.type);
            this.getFirstTableData();
            this.getImgList();
            this.getProductLabel();
            this.getPageMessage();
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

        //获取会员下属用户分页信息
        getFirstTableData() {
            this.$nextTick(async () => {
                //根据编号查询会员的相关信息
                let params = {
                    className: 'Member',
                    method: 'selectUserByMemberIdPageVo',
                    param: {
                        page: {
                            pageIndex: this.paginationData.currentPage,
                            pageSize: this.paginationData.pageSize,
                        },
                        searchdata: this.searchData,
                    },
                };
                let res = await this.$store.dispatch('http/post', params);
                console.log('res=',res);
                this.firstTableData = res.dataList;
                this.paginationData.total = res.page.totalNum;
            });
        },

        //加载页面信息
        getPageMessage() {
            this.$nextTick(async () => {
                //根据编号查询产品的相关信息
                let params = {
                    className: 'produce',
                    method: 'getProductById',
                    param: {
                        id: this.productId
                    },
                };
                this.form = await this.$store.dispatch('http/post', params);
                let arr = JSON.parse(this.form.label);
                let newArr = [];
                for(let i=0;i<arr.length;i++) {
                    let list = this.productLabelList;
                    for(let j=0;j<list.length;j++) {
                        if(arr[i] == list[j].code) {
                            newArr.push(list[j].name);
                        }
                    }
                    this.labelList = newArr;
                }
                console.log('this.form=',this.form);
                // this.fileList = JSON.parse(this.form.casePath);
                // this.form.memberId = this.searchData.memberId;
            });
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
                //     if(e.id === this.form.img) {
                //         this.imgUrl = e.pic
                //     }
                // });
            });
        },

        //下载附件
        // downloadFile(){
        //     this.$nextTick(async () => {
        //         let params = {
        //             className:'Activity',
        //             method: 'ActivityLoad',
        //             param: {'ID': 'e3391d75f96f427c8c3dace359a4cf33'}
        //         };
        //         let res = await this.$store.dispatch('http/post', params);
        //         console.log('res111=', res);
        //         console.log("转换后123",this.str2UTF8(res));
        //         const blob = new Blob(this.str2UTF8(res));//处理文档流
        //
        //         let elink = document.createElement('a');
        //         elink.style.display = 'none';
        //         elink.href = window.URL.createObjectURL(blob);
        //         elink.setAttribute("download","xxx.pdf");
        //         document.body.appendChild(elink);
        //         elink.click();
        //         URL.revokeObjectURL(elink.href); // 释放URL 对象
        //         document.body.removeChild(elink);
        //     });
        // },
        //
        // str2UTF8(str){
        //     var bytes = new Array();
        //     var len,c;
        //     len = str.length;
        //     for(var i = 0; i < len; i++){
        //         c = str.charCodeAt(i);
        //         if(c >= 0x010000 && c <= 0x10FFFF){
        //             bytes.push(((c >> 18) & 0x07) | 0xF0);
        //             bytes.push(((c >> 12) & 0x3F) | 0x80);
        //             bytes.push(((c >> 6) & 0x3F) | 0x80);
        //             bytes.push((c & 0x3F) | 0x80);
        //         }else if(c >= 0x000800 && c <= 0x00FFFF){
        //             bytes.push(((c >> 12) & 0x0F) | 0xE0);
        //             bytes.push(((c >> 6) & 0x3F) | 0x80);
        //             bytes.push((c & 0x3F) | 0x80);
        //         }else if(c >= 0x000080 && c <= 0x0007FF){
        //             bytes.push(((c >> 6) & 0x1F) | 0xC0);
        //             bytes.push((c & 0x3F) | 0x80);
        //         }else{
        //             bytes.push(c & 0xFF);
        //         }
        //     }
        //     return bytes;
        // },

        //下载附件
        downloadFile(){
            console.info("1111111111",this.form)
            window.location.href = this.form.casePath;
            // let thisData = qs.stringify({
            //   filePath:this.form.pic
            // });
            // let path='/data/uploadedFile/23c6e74b-6fb5-4a77-9e00-8a919ff745ba.pdf'
            // axios({
            //     headers: {
            //       'Content-Type': 'application/x-msdownload;charset=utf-8',
            //       'token':store.state.user.token
            //     },
            //     method: "POST",
            //     //url: process.env.BASE_API + '/manage/ReportDownLoad',
            //     url:'seal/manage/ReportDownLoad',
            //     data:path,
            //     responseType: 'blob',
            // })
            // .then(res => {
            //     console.log('res111',res);
            //     const blob = new Blob([res.data]);//处理文档流
            //     let elink = document.createElement('a');
            //     elink.style.display = 'none';
            //     elink.href = window.URL.createObjectURL(blob);
            //     elink.setAttribute("download",res.headers.filename);
            //     document.body.appendChild(elink);
            //     elink.click();
            //     URL.revokeObjectURL(elink.href); // 释放URL 对象
            //     document.body.removeChild(elink);
            // });
        },

        //返回按钮
        backBtn: function () {
            if (this.onBack) {
                this.onBack(null);
            }
        },

        //分页功能
        //每页总条数发送改变
        handleSizeChange(val) {
            this.paginationData.pageSize = val;
            this.getFirstTableData();
        },

        //页数发生改变
        handleCurrentChange(val) {
            this.paginationData.currentPage = val;
            this.getFirstTableData();
        },
    },
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
.header {
    background-color: #f1f5f9;
    text-align: left;
    margin-bottom: 10px;
}

.textarea.ant-input{
    resize: none;
    border: none;
}
.userInput {
    width: 1300px;
    min-height: 20px;
    padding: 5px;
    width: 100%;
    display: block;
    flex: 1;
    margin: 10px 5px;
    font-size: 15px;
    //color: #0c0c0c;
    outline: none;
    border: none;
    color: #606266;
    overflow-y: auto;
    appearance: none;
    text-align: inherit;
    font-family: -apple-system-font, "Helvetica Neue", sans-serif, "Microsoft YaHei"
}

.table {
    .el-table {
        width: 100%;
        text-align: center;
        border: 1px solid #bbc8c1;
    }
}

.memberInfo {
    padding-left: 20px;
    min-height: 220px;
    margin-bottom: 50px;
}

.panel-group {
    font-size: 15px;
    margin-bottom: 18px;
}

.labelStyle {
    display: inline-block;
    color: #101010;
    font-size: 15px;
    font-weight: 700;
    margin-bottom: 10px;
}

.currentDiv {
    background: #fff;
    margin: 10px;
    border-radius: 6px;
    box-shadow: 0 2px 4px rgba(0, 0, 0, .12), 0 0 6px rgba(0, 0, 0, .04)
}

.content {
    //height: 550px;
    margin-bottom: 20px;
}
</style>
