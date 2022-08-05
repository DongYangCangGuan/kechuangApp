<template>
    <div class="navbar">
        <hamburger :is-active="sidebar.opened" class="hamburger-container" @toggleClick="toggleSideBar"/>
        <breadcrumb class="breadcrumb-container"/>
        <div class="right-menu">
            <el-dropdown class="avatar-container right-menu-item hover-effect" trigger="click">
                <div class="avatar-wrapper">
                    <img v-if="imageUrl" :src="imageUrl" class="user-avatar">
                    <!--                    <svg-icon icon-class='choice' class="user-avatar"></svg-icon>-->
                    <i class="el-icon-caret-bottom"/>
                </div>
                <el-dropdown-menu slot="dropdown">
                    <router-link to="/">
                        <el-dropdown-item>
                            首页
                        </el-dropdown-item>
                    </router-link>
                    <el-dropdown-item>
                        <span style="display:block;" @click="logout">退出登录</span>
                    </el-dropdown-item>
                    <el-dropdown-item divided>
                        <span style="display:block;" @click="updatePassword">修改密码</span>
                    </el-dropdown-item>
                </el-dropdown-menu>
            </el-dropdown>
        </div>


        <span class="title right-menu">

            <!--导航栏消息提示-->
            <span style="margin-right: 20px;position: relative;" v-if="productPermission === true">
                <el-tooltip placement="bottom" effect="light" >
                    <div slot="content" v-if="unApprovedCount > 0" style="cursor: default;" @click="goVerify">
                        <span style="font-size: 14px;">您有{{unApprovedCount}}个用户未审核！</span>
                        <span style="color: deepskyblue;font-size: 12px;">&nbsp;详情请点击&nbsp;>></span>
                    </div>
                    <div slot="content" v-else>
                        暂无消息！
                    </div>
                  <i style="font-size: 25px;" class="el-icon-chat-line-square" ></i>
                </el-tooltip>
                <i  v-if="unApprovedCount > 0"
                    style="  display: inline-block;
                            border-radius: 50%;
                            width: 15px;
                            height: 15px;
                            background-color: red;
                            position: absolute;
                            top: -12px;
                            color: #FFFFFF;
                            font-size: 10px;
                            right: 0;">
                    <span style="position: absolute;
                            top: -17px;
                            right: 4px;">1</span>
                </i>
            </span>

            <span>欢迎:
<!--                {{ depart }} &nbsp;-->
                {{ name }}&nbsp;角色：</span>
            <el-select v-model="value1" size="mini" placeholder="请选择" @change="change">
                  <el-option
                      v-for="item in roleslist"
                      :key="item.id"
                      :label="item.name"
                      :value="item.id"
                  >
                  </el-option>
            </el-select>
        </span>

        <el-dialog
            title="提示"
            :visible.sync="updatePwdDialog"
            width="30%"
            append-to-body>
            <div>
                <el-form :rules="UpdatePwdrules" ref="formLabelPwd" :label-position="labelPosition" label-width="100px"
                         :model="formLabelPwd">
                    <el-form-item label="旧密码" prop="oldPwd">
                        <el-input type="password" v-model="formLabelPwd.oldPwd" placeholder="请输入旧密码"></el-input>
                    </el-form-item>
                    <el-form-item label="新密码" prop="newPwd">
                        <el-input type="password" v-model="formLabelPwd.newPwd" placeholder="请输入新密码"></el-input>
                    </el-form-item>
                    <el-form-item label="确认新密码" prop="confirmPwd">
                        <el-input type="password" v-model="formLabelPwd.confirmPwd" placeholder="请再次输入新密码"></el-input>
                    </el-form-item>
                </el-form>
            </div>
            <span slot="footer" class="dialog-footer">
                <el-button @click="cancleUpdatePwd">取 消</el-button>
                <el-button type="primary" @click="confirmUpdatePwd('formLabelPwd')">确 定</el-button>
            </span>
        </el-dialog>
    </div>
</template>

<script>
import {mapState,mapActions} from 'vuex'
import Breadcrumb from '@MAN/components/Breadcrumb'
import Hamburger from '@MAN/components/Hamburger'
import Search from '@MAN/components/HeaderSearch'
import PanelLabelGroup from "../../views/dashboard/system/components/PanelLabelGroup";

export default {
    components: {
        PanelLabelGroup,
        Breadcrumb,
        Hamburger,
        Search
    },

    data() {
        return {
            UpdatePwdrules: {
                oldPwd: [{required: true, message: '请输入旧密码', trigger: 'change'}],
                newPwd: [{required: true, message: '请输入新密码', trigger: 'change'}],
                confirmPwd: [{required: true, message: '请输入新密码', trigger: 'change'}],
            },

            value1: {code: '001', name: '超级管理员'},
            imageUrl: '',
            query: "",
            updatePwdDialog: false,

            labelPosition: 'right',
            formLabelPwd: {
                oldPwd: '',
                newPwd: '',
                confirmPwd: ''
            },
            productPermission: ''
        }
    },

    created() {
        this.getProductPermission();
        this.$nextTick(async () => {
            console.log(this.$store.state.user.avatar);
            let filePath = this.$store.state.user.avatar;
            if (filePath != null && filePath != '') { //当图片路径不为空时获取图片路径
                let pic = {
                    param: filePath,
                    url: process.env.BASE_API + '/api/file/downloadfile'
                };
                let res = await this.$store.dispatch('http/fileDownload', pic);
                if (res != null && res != '') {
                    this.imageUrl = "data:image/png;base64," + res;
                } else {
                    this.imageUrl = pic.param;
                }
            }
        });

        },

    computed: {
        ...mapState({
            sidebar: state => state.app.sidebar,
            name: state => state.user.name,
            avatar: state => state.user.avatar,
            depart: state => state.user.depart,
            roles: state => state.user.roles,
            roleslist: state => state.user.roleslist,
            device: state => state.app.device,
            unApprovedCount: state => state.approveCount.unApprovedCount
        })
    },

    mounted() {
        console.log(this.roleslist, 'rolesList----------->');
        this.value1 = this.roleslist[0].name;
        this.getUnApprovedCount();

    },

    methods: {
        goVerify() {
            console.log(111111111111);
            this.$router.push('../userinfo/certification')
        },
        change(val) {
            this.$store.dispatch('user/changeRoles', val).then(
                //this.$store.dispatch('user/login', JSON.stringify( this.loginForm)).then(
                () => {
                    alert(val);
                }
            ).catch(() => {
                this.loading = false
            })

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

        getUnApprovedCount() {
            this.$store.dispatch('approveCount/getUnApprovedCount');
        },

        toggleSideBar() {
            this.$store.dispatch('app/toggleSideBar')
        },

        updatePassword() {
            this.updatePwdDialog = true
        },

        logout() {
            this.$nextTick(async () => {
                await this.$store.dispatch('user/logout');
                this.$router.push(`/login?redirect=${this.$route.fullPath}`);
            })
        },

        cancleUpdatePwd() {
            this.updatePwdDialog = false;
            this.formLabelPwd.confirmPwd = '';
            this.formLabelPwd.newPwd = '';
            this.formLabelPwd.oldPwd = '';
        },

        confirmUpdatePwd(formLabelPwd) {
            this.$refs[formLabelPwd].validate((valid) => {
                if (valid) {
                    if (this.formLabelPwd.newPwd != this.formLabelPwd.confirmPwd) {
                        this.$message({message: '您两次输入的新密码不一致！', type: 'warning'});
                        return false;
                    } else {
                        this.$nextTick(async () => {
                            let params = {
                                className: 'Staff',
                                method: 'updatePassword',
                                param: this.formLabelPwd
                            };
                            let respose = await this.$store.dispatch('http/post', params);
                            console.log(respose)
                            if (respose.code == 200) {
                                this.$message({showClose: true, message: '修改成功！', type: 'success'});
                                await this.$store.dispatch('user/logout')
                                this.$router.push(`/login?redirect=${this.$route.fullPath}`)
                            } else if (respose.code == 201) {
                                this.$message({showClose: true, message: respose.data, type: 'warning'});
                            } else {
                                this.$message({showClose: true, message: respose.data, type: 'error'});
                            }
                        });
                    }
                } else {
                    console.log('error submit!!');
                    return false;
                }
            });
        }
    }
}
</script>

<style lang="scss" scoped>
.userinfo {
    margin-top: 2px;
}

.navbar {
    height: 50px;
    overflow: hidden;
    position: relative;
    background: #fff;
    box-shadow: 0 1px 4px rgba(0, 21, 41, .08);

    .hamburger-container {
        line-height: 46px;
        height: 100%;
        float: left;
        cursor: pointer;
        transition: background .3s;
        -webkit-tap-highlight-color: transparent;

        &:hover {
            background: rgba(0, 0, 0, .025)
        }
    }

    .breadcrumb-container {
        float: left;
    }

    .errLog-container {
        display: inline-block;
        vertical-align: top;
    }

    .right-menu {
        float: right;
        height: 100%;
        line-height: 50px;

        &:focus {
            outline: none;
        }

        .right-menu-item {
            display: inline-block;
            padding: 0 8px;
            height: 100%;
            font-size: 18px;
            color: #5a5e66;
            vertical-align: text-bottom;

            &.hover-effect {
                cursor: pointer;
                transition: background .3s;

                &:hover {
                    background: rgba(0, 0, 0, .025)
                }
            }
        }

        .avatar-container {
            margin-right: 30px;

            .avatar-wrapper {
                margin-top: 5px;
                position: relative;

                .user-avatar {
                    cursor: pointer;
                    width: 40px;
                    height: 40px;
                    border-radius: 10px;
                }

                .el-icon-caret-bottom {
                    cursor: pointer;
                    position: absolute;
                    right: -20px;
                    top: 25px;
                    font-size: 12px;
                }
            }
        }
    }
}
/deep/.el-tooltip .el-icon-chat-line-square {
    cursor: default!important;
}
</style>
