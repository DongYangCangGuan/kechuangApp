<template>
    <div class="login-container">
        <el-row :gutter="30" class="panel-group">
            <el-col :sm="12">
                <img class="box animated zoomIn img_login"
                     src="@MAN/assets/login_images/login.png"/>
            </el-col>
            <el-col :sm="12">
                <el-form ref="loginForm"
                         :model="loginForm"
                         :rules="loginRules"
                         class="login-form animated zoomIn"
                         auto-complete="on"
                         label-position="left">

                    <div class="title-container">
                        <h3 class="title">
                            系统登录
                        </h3>
                    </div>

                    <el-form-item prop="loginName">
        <span class="svg-container">
          <svg-icon icon-class="user"/>
        </span>
                        <el-input
                            ref="username"
                            v-model.trim="loginForm.loginname"
                            placeholder="账号"
                            name="loginName"
                            type="text"
                            auto-complete="on"
                        />
                    </el-form-item>

                    <el-form-item prop="password">
        <span class="svg-container">
          <svg-icon icon-class="password"/>
        </span>
                        <el-input
                            :key="passwordType"
                            ref="password"
                            v-model.trim="loginForm.password"
                            :type="passwordType"
                            placeholder="密码"
                            name="password"
                            auto-complete="on"
                            @keyup.enter.native="handleLogin"
                        />
                        <span class="show-pwd" @click="showPwd">
          <svg-icon :icon-class="passwordType === 'password' ? 'eye' : 'eye-open'"/>
        </span>
                    </el-form-item>

                    <el-button :loading="loading" type="primary" style="width:100%;margin-bottom:30px;"
                               @click.native.prevent="handleLogin">
                        登录
                    </el-button>

                </el-form>
            </el-col>
        </el-row>
    </div>
</template>

<script>
import {Encrypt} from '@COM/utils/encrypt'
import {showLoading, hideLoading} from "../../utils/loading";

export default {
    name: "index",
    data() {
        const validateUsername = (rule, value, callback) => {
            if (!value) {
                callback(new Error('Please enter the user name'))
            } else {
                callback()
            }
        }
        const validatePassword = (rule, value, callback) => {
            if (value.length < 1) {
                callback(new Error('The password can not be less than 1 digits'))
            } else {
                callback()
            }
        }
        return {
            loginForm: {
                loginname: '',
                password: ''
            },
            loginRules: {
                loginname: [{required: true, trigger: 'blur', validator: validateUsername}],
                password: [{required: true, trigger: 'blur', validator: validatePassword}]
            },
            passwordType: 'password',
            loading: false,
            showDialog: false,
            redirect: undefined
        }
    },
    watch: {
        $route: {
            handler: function (route) {
                this.redirect = route.query && route.query.redirect
            },
            immediate: true
        }
    },
    methods: {
        showPwd() {
            if (this.passwordType === 'password') {
                this.passwordType = ''
            } else {
                this.passwordType = 'password'
            }
            this.$nextTick(() => {
                this.$refs.password.focus()
            })
        },
        handleLogin() {
            this.$refs.loginForm.validate(valid => {
                if (valid) {
                    this.loading = true;
                    this.$store.dispatch('user/login', Encrypt(this.loginForm)).then(
                        //this.$store.dispatch('user/login', JSON.stringify( this.loginForm)).then(
                        () => {
                            console.info("你好：" + this.redirect);
                            this.$router.push({path: this.redirect || '/'})
                        }
                    ).catch(() => {
                        this.loading = false;
                    })
                } else {
                    this.$message({
                        showClose: true,
                        message: 'error submit'
                    });
                    this.loading = false;
                    return false
                }
            })
        },
    }
}
</script>

<style lang="scss">
/* 修复input 背景不协调 和光标变色 */
/* Detail see https://github.com/PanJiaChen/vue-element-admin/pull/927 */

$bg: #283443;
$light_gray: #fff;
$cursor: #fff;
$input_login: #666;

@supports (-webkit-mask: none) and (not (cater-color: $cursor)) {
    .login-container .el-input input {
        color: $cursor;
    }
}

/* reset element-ui css */
.login-container {
    .el-input {
        display: inline-block;
        height: 47px;
        width: 85%;

        input {
            background: transparent;
            border: 0px;
            -webkit-appearance: none;
            border-radius: 0px;
            padding: 12px 5px 12px 15px;
            color: $input_login;
            height: 47px;
            caret-color: $cursor;

            &:-webkit-autofill {
                box-shadow: 0 0 0px 1000px $bg inset !important;
                -webkit-text-fill-color: $cursor !important;
            }
        }
    }

    .el-form-item {
        border: 1px solid rgba(255, 255, 255, 0.1);
        background: rgba(0, 0, 0, 0.1);
        border-radius: 5px;
        color: #454545;
    }
}
</style>

<style lang="scss" scoped>
$bg: #2d3a4b;
$dark_gray: #889aa4;
$light_gray: #eee;
$login_title: #666;
$margin_top: 200px;

.login-container {
    min-height: 100%;
    width: 100%;
    background-color: $bg;
    overflow: hidden;

    .img_login {
        width: 80%;
        max-width: 100%;
        display: block;
        margin: $margin_top 10px 0 auto;
        @media only screen and (min-width: 768px) {
            width: 480px;
        }

        @media only screen and (max-width: 768px) {
            margin-top: 20px;
            margin-right: auto;
        }
    }

    .login-form {
        position: relative;
        width: 90%;
        max-width: 100%;
        margin: $margin_top auto 0 10px;
        background-color: #FFF;
        overflow: hidden;
        box-shadow: 4px 4px 40px rgba(0, 0, 0, 0.05);
        border-color: rgba(0, 0, 0, 0.05);
        border-radius: 5px;
        padding: 35px 20px 10px;

        @media only screen and (min-width: 768px) {
            width: 480px;
        }

        @media only screen and (max-width: 768px) {
            margin-top: 20px;
            margin-left: auto;
        }
    }

    .tips {
        font-size: 14px;
        color: #fff;
        margin-bottom: 10px;

        span {
            &:first-of-type {
                margin-right: 16px;
            }
        }
    }

    .svg-container {
        padding: 6px 5px 6px 15px;
        color: $dark_gray;
        vertical-align: middle;
        width: 30px;
        display: inline-block;
    }

    .title-container {
        position: relative;

        .title {
            font-size: 26px;
            color: $login_title;
            margin: 0px auto 40px auto;
            text-align: center;
            font-weight: bold;
        }

        .set-language {
            color: #fff;
            position: absolute;
            top: 3px;
            font-size: 18px;
            right: 0px;
            cursor: pointer;
        }
    }

    .show-pwd {
        position: absolute;
        right: 10px;
        top: 7px;
        font-size: 16px;
        color: $dark_gray;
        cursor: pointer;
        user-select: none;
    }

    .thirdparty-button {
        position: absolute;
        right: 0;
        bottom: 6px;
    }

    @media only screen and (max-width: 470px) {
        .thirdparty-button {
            display: none;
        }
    }
}
</style>
