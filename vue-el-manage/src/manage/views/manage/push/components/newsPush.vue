<template>
    <div class="currentDiv">
        <el-header class="header">
            <el-row class="headerbtn">
                <span class="title">新闻详情</span>
            </el-row>
        </el-header>

        <el-main class="content">
            <div class="memberInfo">
                <div>
                    <el-form :model="formData" label-width="100px">
                        <el-form-item label="新闻图片" prop="newsPic">
                            <img :src="formData.newsPic" width="120" height="120">
                        </el-form-item>
                        <el-form-item label="新闻标题" prop="title">
                            <el-input v-model="formData.title" readonly class="userInput"></el-input>
                        </el-form-item>
                        <el-form-item label="新闻内容" prop="describe">
                            <el-input v-model="formData.describe"
                                      type="textarea"
                                      :rows="10"
                                      class="userInput"
                                      readonly
                            ></el-input>
                        </el-form-item>
                        <el-form-item label="外部链接" prop="newsLink">
                            <el-input v-model="formData.newsLink" readonly class="userInput"></el-input>
                        </el-form-item>
                        <el-form-item label="发布时间" prop="newsTime">
                            <el-input v-model="formData.newsTime" readonly class="userInput"></el-input>
                        </el-form-item>
                    </el-form>
                </div>

            </div>

            <el-button type="info"
                       icon="el-icon-back"
                       plain
                       @click="backBtn">返回
            </el-button>

        </el-main>
    </div>
</template>

<script>
import axios from 'axios';
import qs from 'qs';
export default {
    name: "news-detail",
    props: {
        //子页面调用父页面
        onBack: {
            type: Function,
            default: null
        }
    },
    data() {
        return {
            loading: {
                form: false,
                btn: false,
            },
            formData: {}
        }
    },

    methods: {

        init(row) {
            console.log('row===',row);
            this.formData = row;
        },

        //返回按钮
        backBtn: function () {
            if (this.onBack) {
                this.onBack(null);
            }
        }
    },
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
.header {
    background-color: #f1f5f9;
    text-align: left;
    margin-bottom: 10px;
}

.headerbtn {
    text-align: right;
    line-height: 5;
    height: 12px;
    float: left;
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
    //min-height: 220px;
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
.userInput {
    width: 70%;
}
</style>
