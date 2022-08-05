<template>
    <div class="container">
        <div :class="control.state.showInfoStyle" v-show="control.state.isShow">
            <member-manage ref="memberManage" :on-add="addBtn" :on-edit="editBtn"
                           :on-detail="detailBtn"></member-manage>
        </div>

        <div class="panel-group" :class="control.state.addInfoStyle" v-show="control.state.isAdd">
            <el-row class="  animated pulse " :gutter="20">
                <add-member ref="addMember" :onBack="AddCancelBtn"></add-member>
            </el-row>
        </div>

        <div class="panel-group" :class="control.state.editInfoStyle" v-show="control.state.isEdit">
            <el-row class="  animated pulse " :gutter="20">
                <edit-memeber ref="editMember" :onBack="AddCancelBtn"></edit-memeber>
            </el-row>
        </div>

        <div class="panel-group" :class="control.state.detailInfoStyle" v-show="control.state.isDetail">
            <el-row class="  animated pulse " :gutter="20">
                <member-detail ref="memberDetailManage" :onBack="AddCancelBtn"></member-detail>
            </el-row>
        </div>
    </div>
</template>
<script>

import MemberManage from "./components/MemberManage";
import AddMember from "./components/AddMember";
import EditMemeber from "./components/EditMember";
import MemberDetail from "./components/MemberDetail";
import control from '@MAN/store/modules/control';

export default {
    name: "member",
    data() {
        return {
            control: control,//控制滑动组件
        }

    },
    components: {
        MemberManage, AddMember, EditMemeber, MemberDetail
    },
    watch: {},
    created() {

    },
    methods: {
        addBtn: function (val) {
            this.$store.commit('SET_FORWARD_STYLE', 'add');
            this.$refs.addMember.getMemberTypeList();
        },

        editBtn: function (id,code) {
            this.$store.commit('SET_FORWARD_STYLE', 'edit');
            this.$refs.editMember.init(code);
            this.$refs.editMember.getPageMessage(id);
            this.$refs.editMember.getCustomerManage();
        },

        detailBtn: function (id, code) {
            this.$store.commit('SET_FORWARD_STYLE', 'detail');
            this.$refs.memberDetailManage.init(id, code);
        },

        AddCancelBtn: function (val) {
            if (val == 'succ')
                this.$refs.memberManage.getPageMessage();
            this.$store.commit('SET_BACK_STYLE');
        },
    }

};
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
.container {
    padding: 8px;
}

.box1 {
    position: relative;
    border-radius: 3px;
    background: #ffffff;
    border-top: 3px solid #3c763d;
    margin-bottom: 20px;
    width: 100%;

}

.dashboard-editor-container {
    padding: 32px;
    background-color: rgb(240, 242, 245);

    .chart-wrapper {
        background: #fff;
        padding: 16px 16px 0;
        margin-bottom: 32px;
    }
}

.panel-group {
    margin-left: 5px;
    margin-top: 1px;
    margin-right: 8px;

    .card-panel-col {
        margin-bottom: 32px;
    }


    .card-panel {
        height: 40rem;
        cursor: default;
        font-size: 12px;
        position: relative;
        overflow: hidden;
        color: #666;
        background: #fff;
        box-shadow: 4px 4px 40px rgba(0, 0, 0, .05);
        border-color: rgba(0, 0, 0, .05);

        border-radius: 5px;

        &.selected {
            box-shadow: 1px 1px 5px #888888;
        }

        &:hover {
            box-shadow: 2px 2px 10px #888888;

            .card-panel-icon-wrapper {
                color: #fff;
            }

            .icon-people {
                background: #40c9c6;
            }

            .icon-message {
                background: #36a3f7;
            }

            .icon-money {
                background: #f4516c;
            }

            .icon-shoppingCard {
                background: #34bfa3
            }
        }

        .icon-people {
            color: #40c9c6;
        }

        .icon-message {
            color: #36a3f7;
        }

        .icon-money {
            color: #f4516c;
        }

        .icon-shoppingCard {
            color: #34bfa3
        }

        .card-panel-icon-wrapper {
            float: left;
            margin: 14px 0 0 14px;
            padding: 16px;
            transition: all 0.38s ease-out;
            border-radius: 6px;
        }

        .card-panel-icon {
            float: left;
            font-size: 25px;
        }

        .card-panel-description {
            float: right;
            font-weight: bold;
            margin: 26px;
            margin-left: 0px;

            .card-panel-text {
                line-height: 18px;
                color: rgba(0, 0, 0, 0.45);
                font-size: 16px;
                margin-bottom: 12px;
            }

            .card-panel-num {
                font-size: 20px;
            }
        }
    }
}
</style>

