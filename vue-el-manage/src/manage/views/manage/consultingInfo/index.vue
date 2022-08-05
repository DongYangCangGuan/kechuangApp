<template>
    <div class="container">
        <div :class="questionControl.state.showInfoStyle" v-show="questionControl.state.isShow1">
            <consultingInfo-manage ref="consultingInfo" :on-add="addBtn" :on-edit="editBtn" :on-detail="detailBtn"></consultingInfo-manage>
        </div>
        <div class="panel-group" :class="questionControl.state.addInfoStyle" v-show="questionControl.state.isAdd1">
            <el-row class="  card-panel  animated pulse " :gutter="20">
                <add-consultingInfo ref="addConsultingInfo" :onBack="AddCancelBtn"></add-consultingInfo>
            </el-row>
        </div>
        <div class="panel-group" :class="questionControl.state.editInfoStyle" v-show="questionControl.state.isEdit1">
            <el-row class="  animated pulse " :gutter="20">
                <edit-consultingInfo ref="editConsultingInfo" :onBack="AddCancelBtn"></edit-consultingInfo>
            </el-row>
        </div>
<!--        <div class="panel-group" :class="questionControl.state.detailInfoStyle" v-show="questionControl.state.isDetail1">-->
<!--            <el-row class="  animated pulse " :gutter="20">-->
<!--                <questionnaire-details-manage ref="questionnaireDetailsManage" :onBack="AddCancelBtn"></questionnaire-details-manage>-->
<!--            </el-row>-->
<!--        </div>-->
    </div>
</template>
<script>

import consultingInfoManage from "./components/consultingInfoManage";
import addConsultingInfo from "./components/addConsultingInfo";
import editConsultingInfo from "./components/editConsultingInfo";
// import questionnaireDetailsManage from "./components/questionnaireDetailsManage";
import questionControl from "../../../store/modules/questionControl";



export default {
    name: "certification",
    data() {
        return {
            questionControl: questionControl,//控制滑动组件
        }
    },
    components: {
        consultingInfoManage,
        addConsultingInfo,
        editConsultingInfo,
        // questionnaireDetailsManage
    },
    watch: {},
    created() {
        console.log('isShow1',questionControl.state.isShow1)
    },
    methods: {
        addBtn: function (val) {
            this.$store.commit('SET_FORWARD_STYLE1', 'add');
            this.$refs.addConsultingInfo.getTypeList();
            this.$refs.addConsultingInfo.getNameList();
        },
        editBtn: function (row) {
            this.$store.commit('SET_FORWARD_STYLE1', 'edit');
            this.$refs.editConsultingInfo.init(row);
            this.$refs.editConsultingInfo.getTypeList();
            this.$refs.editConsultingInfo.getGPTypeList();
            this.$refs.editConsultingInfo.getAnotherTypeList();
            this.$refs.editConsultingInfo.getNameList();

        },
        detailBtn: function (row) {
            this.$store.commit('SET_FORWARD_STYLE1', 'detail');
            this.$refs.questionnaireDetailsManage.init(row);
        },
        AddCancelBtn: function (val) {
            if (val == 'succ')
                this.$refs.consultingInfo.getPageMessage();
            this.$store.commit('SET_BACK_STYLE1');
        }
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
        height: 100%;
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

