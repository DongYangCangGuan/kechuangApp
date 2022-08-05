<template>
    <div>
        <!-- 主页面 -->
            <div class="container">
                <div class="top">
                    <div class="left">关于我们</div>
                </div>

                <div class="table">
                    <el-input type="textarea" :rows="5" placeholder="请输入内容" v-model="textarea"></el-input>
                    <el-row style="margin-top: 20px;">
                        <el-button type="primary" plain @click="insertAboutus" >保存</el-button>
                    </el-row>
                </div>
            </div>
    </div>
</template>

<script>
export default {
    name: "aboutus-Manage",
    data() {
        return {
            textarea: ''
        };
    },

    // 钩子函数
    created() {
        this.getAboutus(); // 页面创建时获取关于我们的数据
    },

    // 方法
    methods: {

        // 页面创建时获取关于我们的数据
        getAboutus() {
            this.$nextTick(async () => {
                let param = {
                }
                let params = {
                    className: 'Aboutus',
                    method: 'getAboutus',
                    param: param,
                };
                let res = await this.$store.dispatch('http/post', params);
                this.textarea  = res.information;
                console.log("res", res)

            });
        },

        insertAboutus() {
            this.$nextTick(async () => {
                let params = {
                    className: 'Aboutus',
                    method: 'insertAboutus',
                    param: {information:this.textarea}
                }
                let res = await this.$store.dispatch('http/post', params);

                console.log("保存：",this.textarea)
                this.textarea  = res.information;
                this.getAboutus()
            });
        },
    }
};
</script>


<style lang="scss" scoped>
$dark_gray: #889aa4;

.right {
    float: right;
    margin-top: -7px;
}

.left {
    float: left;
    border-left: 4px solid #6ba7bd;
    padding-left: 1%;
}

.page {
    float: right
}

.table {
    width: 100%;
    padding: 1px 22px 15px 22px;
}

.top {
    width: 100%;
    height: 20px;
    padding: 0px 22px;
    margin: 15px 0px;
}

.container {
    border: 1px solid #bbc8c1;
    height: 100%;
    width: 100%
}

.selectBox {
    margin: 0px 22px 15px 22px
}

.assignUser {
    margin: 15px 22px;
}

.svg-container {
    color: $dark_gray;
    vertical-align: middle;
    width: 30px;
    margin-left: -49px;
    display: inline-block;
}
</style>

