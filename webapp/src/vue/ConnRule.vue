<template>
    <div>
        <div class="ship-top">
            <span class="ship-span">连通规则</span>
        </div>
        <div class="ship-line"></div>
        <div class="view">
            <div class="ship-coon-btn">
                <el-button-group>
                    <el-button @click.native="initAdd"><i class="fa fa-plus" aria-hidden="true"></i></el-button>
                    <el-button @click.native="initMod"><i class="fa fa-pencil" aria-hidden="true"></i></el-button>
                    <el-button @click.native="del"><i class="fa fa-trash" aria-hidden="true"></i></el-button>
                    <el-button @click.native="loadIpAddr()"><i class="fa fa-refresh" aria-hidden="true"></i>
                    </el-button>
                </el-button-group>
            </div>
            <div class="ship-table">
                <el-table :data="cannRuleList" border tooltip-effect="dark" @selection-change="handleSelectionChange">
                    <el-table-column type="selection" width="55"></el-table-column>
                    <el-table-column prop="id" label="ID" width="100" sortable></el-table-column>
                    <el-table-column prop="conn_type" label="应用类型"></el-table-column>
                    <el-table-column prop="direction" label="传输方向"></el-table-column>
                    <el-table-column prop="listen_ip" label="监听地址"></el-table-column>
                    <el-table-column prop="listen_port" label="监听端口"></el-table-column>
                    <el-table-column prop="dst_ip" label="目的地址"></el-table-column>
                    <el-table-column prop="dst_port" label="目的端口"></el-table-column>
                    <el-table-column prop="conn_ip" label="连接地址"></el-table-column>
                    <el-table-column prop="status" label="连接状态"></el-table-column>
                </el-table>
            </div>
            <div class="block ship-page">
                <el-pagination
                        @current-change="handleCurrentChange"
                        :current-page="page.curPage"
                        :page-size="page.limit"
                        :total="page.total">
                </el-pagination>
            </div>
        </div>
        <!--add and modify-->
        <el-dialog :title="editTitle" v-model="formVisible" :close-on-click-modal="false" size="small">
            <el-form label-width="100px" :model="connRule" :rules="editFormRules" ref="connRule">
                <el-form-item label="应用类型">
                    <el-select v-model="connRule.rule_type">
                        <el-option v-for="type in  appTypes" :value="type.value" :label="type.name" :key="type.value"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="传输方向">
                    <el-select v-model="connRule.direct" @change="changeDirect">
                        <el-option v-for="direction in  directions" :value="direction.value" :label="direction.name" :key="direction.value"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="监听地址">
                    <el-select v-model="connRule.listen_addr.id">
                        <el-option v-for="ip in  listenAddrs" :value="ip.id" :label="ip.ip" :key="ip.id"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="监听端口" prop="listen_port">
                    <el-input type="number" v-model="connRule.listen_port" auto-complete="off" placeholder="请输入监听端口"></el-input>
                </el-form-item>
                <el-form-item label="目的地址" prop="dst_addr">
                    <el-input v-model="connRule.dst_addr" auto-complete="off" placeholder="请输入目的地址"></el-input>
                </el-form-item>
                <el-form-item label="目的端口" prop="dst_port">
                    <el-input v-model="connRule.dst_port" auto-complete="off" placeholder="请输入目的端口"></el-input>
                </el-form-item>
                <el-form-item label="连接地址">
                    <el-select v-model="connRule.send_addr.id">
                        <el-option v-for="ip in  seedAddrs" :value="ip.id" :label="ip.ip" :key="ip.id"></el-option>
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
    export default {
        data () {
            let checkListenPort = (rule, value, callback) => {
                if (!value)
                    callback(new Error('请输入监听端口'));
                else if (!util.PATTERN.PORT.test(value))
                    callback(new Error('端口必须再1-65535之间'));
                else
                    callback();
            };
            let checkDstPort = (rule, value, callback) => {
                if (!value)
                    callback(new Error('请输入监听端口'));
                else if (!util.PATTERN.PORT.test(value))
                    callback(new Error('端口必须再1-65535之间'));
                else
                    callback();
            };
            return {
                cannRuleList: [],
                page: {
                    curPage: 1,
                    limit: 15,
                    total: 0
                },
                editTitle: '',
                formVisible: false,
                connRule: {
                    rule_type: 'TCP',
                    direct: 1,
                    listen_addr: {id: 0},
                    listen_port: '',
                    dst_addr: '',
                    send_addr: {id: 0},
                    dst_port: '',
                    status: false
                },
                seedAddrs: [],
                appTypes: [
                    {value: 'TCP', name: 'TCP'},
                    {value: 'HTTP', name: 'HTTP'},
                ],
                listenAddrs: [],
                directions: [
                    {value: 1, name: '内端→外端'},
                    {value: 2, name: '外端→内端'}
                ],
                editFormRules: {
                    listen_port: [{validator: checkListenPort, trigger: 'blur'}],
                    dst_addr: [{required: true, message: '请输入目的地址', trigger: 'blur'}],
                    dst_port: [{validator: checkDstPort, trigger: 'blur'}]
                },
                listenId: 1,
                sendId: 2
            }
        },
        methods: {
            loadConnRules: function (page) {
                let self = this;
                axios.post('/ship/node/connRuleList', {page: page, limit: self.page.limit}).then((res) => {
                    self.page.curPage = res.data.curPage;
                    self.page.total = res.data.total;
                    self.cannRuleList = res.data.data;
                }).catch((err) => {
                    util.dialog.notifyError(self, '数据加载失败');
                })
            },
            //获取checkbox
            handleSelectionChange: function (val) {
                this.selection = val;
            },
            //跳转页
            handleCurrentChange: function () {

            },
            changeDirect: function () {
                let self = this;
                if (self.connRule.direct === 1) {
                    self.listenId = 1;
                    self.sendId = 2;
                } else {
                    self.listenId = 2;
                    self.sendId = 1;
                }
                axios.get('/ship/node/ipAddrList?nodeId=' + self.listenId).then((res) => {
                    self.listenAddrs = res.data;
                    if (self.listenAddrs.length > 0) {
                        self.connRule.listen_addr = self.listenAddrs[0];
                    }
                });
                axios.get('/ship/node/ipAddrList?nodeId=' + self.sendId).then((res) => {
                    self.seedAddrs = res.data;
                    if (self.seedAddrs.length > 0) {
                        self.connRule.send_addr = self.seedAddrs[0];
                    }
                });
            },
            initAdd: function () {
                let self = this;
                self.editTitle = '新增';
                self.formVisible = true;
                self.connRule.id = undefined;
                axios.get('/ship/node/ipAddrList?nodeId=1').then((res) => {
                    self.listenAddrs = res.data;
                    if (self.listenAddrs.length > 0) {
                        self.connRule.listen_addr = self.listenAddrs[0];
                    }
                });
                axios.get('/ship/node/ipAddrList?nodeId=2').then((res) => {
                    self.seedAddrs = res.data;
                    if (self.seedAddrs.length > 0) {
                        self.connRule.send_addr = self.seedAddrs[0];
                    }
                });
            },
            edit: function () {
                let self = this;
                if (self.connRule.id === undefined) {
                    self.createConnRule();
                }
            },
            createConnRule: function () {
                let self = this;
                self.$refs.connRule.validate((valid) => {
                    if (valid) {
                        axios.post('/ship/node/connRule', self.connRule).then((res) => {
                            util.dialog.notifySuccess(self, '添加成功');
                            self.formVisible = false;
                            self.loadConnRules(self.page.curPage);
                        }).catch((err) => {
                            util.dialog.notifyError(self, '添加失败');
                        })
                    }
                })
            }
        },
        mounted: function () {
            let self = this;
            self.$nextTick(function () {
                self.loadConnRules(1);
            })
        },
    }
</script>

<style>
    .ship-coon-btn {
        margin-bottom: 10px;
        float: right;
    }
</style>