<template>
    <div class="container">
        <div :class="control.state.showInfoStyle" v-show="control.state.isShow">
            <question-manage ref="questionManage" :onAdd="addBtn" :onEdit="editBtn" :onImport="importBtn" :onDetail="detailBtn"></question-manage>
        </div>

        <div class="panel-group" :class="control.state.addInfoStyle" v-show="control.state.isAdd">
            <el-row class="  card-panel  animated pulse " :gutter="20">
                <add-question ref="addQuestion" :on-back="backBtn"></add-question>
            </el-row>
        </div>

        <div class="panel-group" :class="control.state.editInfoStyle" v-show="control.state.isEdit">
            <el-row class="  card-panel  animated pulse " :gutter="20">
                <edit-question ref="editQuestion" :on-back="backBtn"></edit-question>
            </el-row>
        </div>

        <div class="panel-group" :class="control.state.importInfoStyle" v-show="control.state.isImport">
            <el-row class="  card-panel  animated pulse " :gutter="20">
                <answer-manage ref="answerManage" :on-back="backBtn"></answer-manage>
            </el-row>
        </div>

        <div class="panel-group" :class="control.state.detailInfoStyle" v-show="control.state.isDetail">
            <el-row class="  card-panel  animated pulse " :gutter="20">
                <question-detail ref="questionDetail" :on-back="backBtn"></question-detail>
            </el-row>
        </div>



    </div>
</template>
<script>

import questionManage from "./components/questionManage";
import answerManage from "./components/answerManage";
import addQuestion from "./components/addQuestion";
import editQuestion from "./components/editQuestion";
import questionDetail from "./components/questionDetail";
import control from '@MAN/store/modules/control';

export default {
    name: "customizeQuestion",
    data() {
        return {
            control: control,//控制滑动组件
        }
    },

    components: {
        questionManage, addQuestion, editQuestion, answerManage, questionDetail,
    },
    watch: {},
    methods: {
        addBtn: function () {
            this.$store.commit('SET_FORWARD_STYLE', 'add');
        },

        editBtn: function (id) {
            this.$store.commit('SET_FORWARD_STYLE', 'edit');
            this.$refs.editQuestion.load(id);
        },

        backBtn: function (val) {
            if(val == "succ")
                this.$refs.questionManage.getFirstTableData();
            this.$store.commit('SET_BACK_STYLE');
        },

        importBtn: function () {
            this.$store.commit('SET_FORWARD_STYLE', 'import');
        },

        detailBtn: function (id) {
            this.$store.commit('SET_FORWARD_STYLE', 'detail');
            this.$refs.questionDetail.load(id);
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

