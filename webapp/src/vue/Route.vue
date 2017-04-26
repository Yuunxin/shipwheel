<template>
    <div>
        <div class="ship-top">
            <span class="ship-span">路由管理</span>
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
                    <el-button @click.native="loadRoute(nodeId)"><i class="fa fa-refresh" aria-hidden="true"></i></el-button>
                </el-button-group>
                <div class="ship-tags">
                    <el-tabs v-model="nodeId" type="card" @tab-click="changeHandle">
                        <el-tab-pane v-for="menu in menus" :label="menu.name" :name="menu.id"></el-tab-pane>
                    </el-tabs>
                </div>
            </div>
            <div class="ship-table">
                <el-table :data="routeList" border tooltip-effect="dark" @selection-change="handleSelectionChange">
                    <el-table-column type="selection" width="55"></el-table-column>
                    <el-table-column prop="id" label="ID" width="100" sortable></el-table-column>
                    <el-table-column prop="subnet" label="目的子网"></el-table-column>
                    <el-table-column prop="mask" label="子网掩码"></el-table-column>
                    <el-table-column prop="gateway" label="网关"></el-table-column>
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
            <el-form label-width="100px" :model="route" :rules="editFormRules" ref="route">
                <div :style="{display: showMod}">
                    <el-form-item label="目的子网" prop="subnet">
                        <el-input v-model="route.subnet" auto-complete="off" placeholder="请输入目的子网"></el-input>
                    </el-form-item>
                    <el-form-item label="子网掩码" prop="mask">
                        <el-input v-model="route.mask" auto-complete="off" placeholder="请输入子网掩码"></el-input>
                    </el-form-item>
                    <el-form-item label="网卡选择">
                        <el-select v-model="route.nicId" placeholder="请选择网卡">
                            <el-option v-for="nic in  nics" :value="nic.id" :label="nic.name"></el-option>
                        </el-select>
                    </el-form-item>
                </div>
                <el-form-item label="网关" prop="gateway">
                    <el-input v-model="route.gateway" auto-complete="off" placeholder="请输入网关"></el-input>
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
    export default {
        data () {
            let checkSubnet = (rule, value, callback) => {
                if (value === '')
                    callback(new Error("请输入目的子网"));
                else if (!util.PATTERN.IP.test(value))
                    callback(new Error("目的子网不合法"));
                else
                    callback();
            };
            let checkMask = (rule, value, callback) => {
                if (value === '')
                    callback(new Error("请输入子网掩码"));
                else if (!util.PATTERN.MASK.test(value))
                    callback(new Error("子网掩码不合法"));
                else
                    callback();
            };
            let checkGateway = (rule, value, callback) => {
                if (value === '')
                    callback(new Error("请输入网关"));
                else if (!util.PATTERN.IP.test(value))
                    callback(new Error("网关不合法"));
                else
                    callback();

            };
            return {
                routeList: [],
                menus: [
                    {id: '1', name: '内端机'},
                    {id: '2', name: '外端机'}
                ],
                page: {
                    curPage: 1,
                    tatal: 0,
                    pageSize: 15
                },
                nodeId: '1',
                editTitle: '',
                route: {
                    subnet: '',
                    mask: '',
                    gateway: '',
                    nicId: 1
                },
                formVisible: false,
                nics:[
                    {id: 1, name: 'eth0', nodeId: 1},
                    {id: 2, name: 'eth1', nodeId: 1}
                ],
                editFormRules: {
                    subnet: [{validator: checkSubnet, trigger: 'blur'}],
                    mask: [{validator: checkMask, trigger: 'blur'}],
                    gateway: [{validator: checkGateway, trigger: 'blur'}]
                },
                selection: [],//checkbox select
                showMod: 'none'
            }
        },
        methods: {
            loadRoute: function (nodeId) {

            },
            //选择内外端
            changeHandle: function (tab, event) {
                let self = this;
                self.nodeId = tab.name;
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
                self.route.id = undefined;
                self.showMod = 'block';
                self.$refs.route.resetFields();
            },
            initMod: function () {
                let self = this;
                if (self.selection.length !== 1) {
                    //self.$notify.error({message: '', offset: 100, duration: 2000});
                    util.dialog.notifyError(self, '请选择一条内容修改');
                    return false;
                }
                self.formVisible = true;
                self.editTitle = '修改';
                self.showMod = 'none';
            },
            edit: function () {
                let self = this;
                if (self.route.id === undefined) {
                    self.createRoute();
                } else {
                    self.modifyRoute();
                }
            },
            createRoute: function () {
                let self = this;
                self.$refs.route.validate((valid) => {
                    if (valid) {
                        console.log(self.ipAddr)
                    }
                })
            },
            modifyRoute: function () {
                let self = this;
                self.$refs.route.validate((valid) => {
                    if (valid) {

                    }
                })
            },
            del: function () {
                let self = this;
                if (self.selection.length === 0) {
                    util.dialog.notifyError(self, '至少选择一条内容删除');
                    return false;
                }
                self.$confirm('此操作将永久删除该文件, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    _.forEach(self.selection, function (s) {
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
                self.page.tatal = self.routeList.length;
            })
        },
    }
</script>