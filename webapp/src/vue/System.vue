<template>
    <div>
        <div class="ship-panel-left"><p></p></div>
        <div class="ship-panel-center">
            <el-tabs type="border-card">
                <el-tab-pane label="管理端地址配置">
                    <el-form :model="system" :rules="editFormOutside" ref="system" label-width="100px" class="demo-ruleForm">
                        <el-form-item label="默认网关" prop="gateway">
                            <el-input v-model="system.gateway" auto-complete="off" placeholder="请输入网关"></el-input>
                        </el-form-item>
                        <el-form-item label="IP地址" prop="ip">
                            <el-input v-model="system.ip" auto-complete="off" placeholder="请输入IP地址"></el-input>
                        </el-form-item>
                        <el-form-item label="掩码" prop="mask">
                            <el-input v-model="system.mask" auto-complete="off" placeholder="请输入掩码"></el-input>
                        </el-form-item>
                        <el-form-item>
                            <el-button type="primary" @click="save">提交</el-button>
                        </el-form-item>
                    </el-form>
                </el-tab-pane>
            </el-tabs>
        </div>
        <div class="ship-panel-right"><p></p></div>
    </div>
</template>
<script>
    export default {
        data () {
            return {
                system: {
                    gateway: '',
                    ip: '',
                    mask: ''
                },
                editFormOutside: {}
            }
        },
        methods: {
            save: function () {
                
            }
        }
    }
</script>