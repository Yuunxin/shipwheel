<template>
    <div>
        <div class="ship-top">
            <span class="ship-span">IP地址管理</span>
        </div>
        <div class="ship-line"></div>
        <!--<el-tabs v-model="nodeId" type="card" @tab-click="changeHandle">
            <el-tab-pane v-for="menu in menus" :label="menu.name" :name="menu.id"></el-tab-pane>
        </el-tabs>-->

        <div class="view">
            <div class="ship-btn">
                <el-button-group>
                    <el-button @click.native="initAdd"><i class="fa fa-plus" aria-hidden="true"></i></el-button>
                    <el-button @click.native="initMod"><i class="fa fa-pencil" aria-hidden="true"></i></el-button>
                    <el-button @click.native="del"><i class="fa fa-trash" aria-hidden="true"></i></el-button>
                    <el-button @click.native="loadIpAddr(nodeId)"><i class="fa fa-refresh" aria-hidden="true"></i></el-button>
                </el-button-group>
                <div class="ship-tags">
                    <el-tabs v-model="nodeId" type="card" @tab-click="changeHandle">
                        <el-tab-pane v-for="menu in menus" :label="menu.name" :name="menu.id"></el-tab-pane>
                    </el-tabs>
                </div>
            </div>
            <div class="ship-table">
                <el-table :data="ipAddrList" border tooltip-effect="dark" @selection-change="handleSelectionChange">
                    <el-table-column type="selection" width="55"></el-table-column>
                    <el-table-column prop="id" label="ID" width="100" sortable></el-table-column>
                    <el-table-column prop="ip" label="IP"></el-table-column>
                    <el-table-column prop="mask" label="掩码"></el-table-column>
                    <el-table-column prop="nicName" label="网卡名称"></el-table-column>
                </el-table>
            </div>
            <div class="block ship-page">
                <el-pagination
                        @current-change="handleCurrentChange"
                        :current-page="page.curPage"
                        :page-size="page.pageSize"
                        :total="page.tatal">
                </el-pagination>
            </div>
        </div>


        <!--add and modify-->
        <el-dialog :title="editTitle" v-model="formVisible" :close-on-click-modal="false" size="tiny">
            <el-form label-width="100px" :model="ipAddr" :rules="editFormRules" ref="ipAddr">
                <el-form-item label="IP" prop="ip">
                    <el-input v-model="ipAddr.ip" auto-complete="off" placeholder="请输入IP地址"></el-input>
                </el-form-item>
                <el-form-item label="掩码" prop="mask">
                    <el-input v-model="ipAddr.mask" auto-complete="off" placeholder="请输入掩码"></el-input>
                </el-form-item>
                <el-form-item label="网卡选择" :style="{display: selectDisplay}">
                    <el-select v-model="ipAddr.nicId" placeholder="请选择网卡">
                        <el-option v-for="nic in  nics" :value="nic.id" :label="nic.name"></el-option>
                    </el-select>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click.native="formVisible = false">取消</el-button>
                <el-button type="primary" @click.native="edit">提交</el-button>
            </div>
        </el-dialog>
    </div>
</template>
<script>
    import util from '../js/util';
    import axios from 'axios';
    import lodash from 'lodash';
    export default {
        data () {
            let checkIp = (rule, value, callback) => {
                if (!value) {
                    callback(new Error('IP不能为空'));
                } else if (!util.PATTERN.IP.test(value)) {
                    callback(new Error('IP不合法'));
                } else {
                    callback();
                }
            };
            let checkMask = (rule, value, callback) => {
                if (!value) {
                    callback(new Error('掩码不能为空'));
                } else if (!util.PATTERN.MASK.test(value)) {
                    callback(new Error('掩码不合法'));
                } else {
                    callback();
                }
            };
            return {
                ipAddrList: [
                    {id: 1, ip: '163.0.0.1', mask: '255.255.255.0', nicName: 'eth0'}
                ],
                menus: [
                    {id: '1', name: '内端机'},
                    {id: '2', name: '外端机'}
                ],
                page: {
                    curPage: 1,
                    tatal: 0,
                    pageSize: 15
                },

                editTitle: '',
                formVisible: false,
                editFormRules: {
                    ip: [{validator: checkIp, trigger: 'blur'}],
                    mask: [{validator: checkMask, trigger: 'blur'}]
                },
                ipAddr: {
                    ip: '',
                    mask: '',
                    nicId: 1
                },
                nodeId: '1',
                nics: [
                    {id: 1, name: 'eth0'},
                    {id: 2, name: 'eth1'}
                ],
                selection: [],
                selectDisplay: 'none',
            }
        },
        methods: {
            //获取网卡IP
            loadIpAddr: function (nodeId) {

            },
            //选择内外端
            changeHandle: function (tab, event) {
                let self = this;
                self.nodeId = tab.name;
                self.loadIpAddr(self.nodeId);
            },
            //获取选择
            handleSelectionChange: function (val) {
                this.selection = val;
            },
            //跳转页
            handleCurrentChange: function (val) {

            },
            initAdd: function () {
                let self = this;
                self.formVisible = true;
                self.editTitle = '新增';
                self.ipAddr.id = undefined;
                self.selectDisplay = 'block';
                self.$refs.ipAddr.resetFields();
            },
            initMod: function () {
                let self = this;
                if (self.selection.length !== 1) {
                    //self.$notify.error({message: '请选择一条内容修改', offset: 100, duration: 2000});
                    util.dialog.notifyError(self, "请选择一条内容修改");
                    return false;
                }
                self.formVisible = true;
                self.editTitle = '修改';
                self.selectDisplay = 'none';
                self.ipAddr.id = 1;
            },
            edit: function () {
                let self = this;
                if (self.ipAddr.id === undefined) {
                    self.createIpAddr();
                } else {
                    self.modifyIpAddr();
                }
            },
            createIpAddr: function () {
                let self = this;
                self.$refs.ipAddr.validate((valid) => {
                    if (valid) {
                        console.log(self.ipAddr)
                    }
                })
            },
            modifyIpAddr: function () {
                let self = this;
                self.$refs.ipAddr.validate((valid) => {
                    if (valid) {

                    }
                })
            },
            del: function () {
                let self = this;
                if (self.selection.length === 0) {
                    util.dialog.notifyError(self, "至少选择一条内容删除");
                    return false;
                }
                self.$confirm('此操作将永久删除该文件, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    _.forEach(self.selection, (s) => {
                        console.log(s);
                        util.dialog.notifySuccess(self, '删除成功');
                    })
                }).catch(() => {
                    util.dialog.notifyInfo(self, '已取消删除');
                });

            }
        },
        mounted: function () {
            let self = this;
            self.$nextTick(function () {
                self.page.tatal = self.ipAddrList.length;
            })
        },
    }
</script>
<style>
    .el-select {
        display: block;
    }

    .ship-left {
        width: 20%;
        float: left;
    }

    .ship-right {
        width: 80%;
        float: right;
    }
</style>