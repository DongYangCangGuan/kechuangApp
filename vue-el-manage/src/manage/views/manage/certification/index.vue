<template>
    <div class="container">
        <div>
            <div :class="control.state.showInfoStyle" v-show="control.state.isShow">
                <certification-manage ref="certificationManage" :on-edit="editBtn"></certification-manage>
            </div>
            <div :class="control.state.editInfoStyle" v-show="control.state.isEdit">
                <el-row class="  animated pulse " :gutter="20">
                    <edit-certification ref="editCertification" :onBack="AddCancelBtn"></edit-certification>
                </el-row>
            </div>

        </div>
    </div>
</template>
<script>

import certificationManage from "./components/certificationManage";
import editCertification from "./components/editCertification";

import control from "../../../store/modules/control";

export default {
    name: "certification",
    data() {
        return {
            control: control,//控制滑动组件
        }
    },
    components: {
        certificationManage,
        editCertification
    },
    watch: {},
    created() {

    },
    methods: {
        addBtn: function (val) {
            this.$store.commit('SET_FORWARD_STYLE', 'add');
        },
        editBtn: function (val) {
            this.$store.commit('SET_FORWARD_STYLE', 'edit');
            this.$refs.editCertification.init(val);
        },
        AddCancelBtn: function (val) {
            if (val == 'succ')
                this.$refs.certificationManage.getFirstTableData();
                this.$store.commit('SET_BACK_STYLE');
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

