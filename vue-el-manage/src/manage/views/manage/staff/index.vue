<template>
    <div class="container">
        <div :class="control.state.showInfoStyle" v-show="control.state.isShow">
            <el-row class="panel-group" :gutter="20">
                <el-col :xs="12" :sm="12" :lg="9" class="card-panel-col">
                    <div class="card-panel  animated pulse">
                        <depart-tree :showheader="false"></depart-tree>
                    </div>
                </el-col>
                <el-col :xs="12" :sm="12" :lg="15" class="card-panel-col">
                    <div class="card-panel animated pulse">
                        <staff-info :onAdd="AddBtn" :onModify="EditBtn" :onImport="ImportBtn"></staff-info>
                    </div>
                </el-col>
            </el-row>
        </div>

        <div :class="control.state.addInfoStyle " v-if="control.state.isAdd">
            <el-row class="  card-panel  animated pulse " :gutter="20">
                <add-staff :onBack="AddCancelBtn"></add-staff>
            </el-row>
        </div>

        <div :class="control.state.editInfoStyle" v-if="control.state.isEdit">
            <el-row class="  card-panel  animated pulse " :gutter="20">
                <edit-staff :onBack="EditCancelBtn"></edit-staff>
            </el-row>
        </div>

        <div :class="control.state.importInfoStyle" v-if="control.state.isImport">
            <el-row class="  card-panel  animated pulse " :gutter="20">
                <input-staff :onBack="ImportCancelBtn"></input-staff>
            </el-row>
        </div>
    </div>
</template>

<script>
import staffInfo from './components/StaffInfo'
import departTree from '@MAN/views/manage/department/components/DepartTree'
import addStaff from './components/AddStaff'
import editStaff from './components/EditStaff'
import inputStaff from './components/InputStaff'
import control from '@MAN/store/modules/control';

export default {
    name: "manage-staff",
    components: {
        staffInfo,
        departTree,
        addStaff,
        editStaff,
        inputStaff,
        control
    },
    data() {
        return {
            control: control,//控制滑动组件
        }
    },
    methods: {
        AddBtn: function (val) {
            this.$store.commit('SET_FORWARD_STYLE', 'add');
        },
        EditBtn: function (val) {
            this.$store.commit('SET_FORWARD_STYLE', 'edit');
        },
        ImportBtn: function (val) {
            this.$store.commit('SET_FORWARD_STYLE', 'import');
        },
        AddCancelBtn: function (val) {
            this.$store.commit('SET_BACK_STYLE');
        },
        EditCancelBtn: function (val) {
            this.$store.commit('SET_BACK_STYLE');
        },
        ImportCancelBtn: function (val) {
            this.$store.commit('SET_BACK_STYLE');
        },
    },
}
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
            font-size: 48px;
        }

        .card-panel-description {
            float: right;
            font-weight: bold;
            margin: 26px 26px 26px 0px;

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
