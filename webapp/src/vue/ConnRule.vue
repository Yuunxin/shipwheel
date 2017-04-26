<template>
    <div>
        <div class="ship-top">
            <span class="ship-span">连通规则</span>
        </div>
        <div class="ship-line"></div>
        <div class="view">
            <div class="ship-btn">
                <el-button-group>
                    <el-button @click.native="initAdd"><i class="fa fa-plus" aria-hidden="true"></i></el-button>
                    <el-button @click.native="initMod"><i class="fa fa-pencil" aria-hidden="true"></i></el-button>
                    <el-button @click.native="del"><i class="fa fa-trash" aria-hidden="true"></i></el-button>
                    <el-button @click.native="loadIpAddr(nodeId)"><i class="fa fa-refresh" aria-hidden="true"></i>
                    </el-button>
                </el-button-group>
            </div>
            <div class="ship-table">
                <el-table :data="cannRuleList" border tooltip-effect="dark" @selection-change="handleSelectionChange">
                    <el-table-column type="selection" width="55"></el-table-column>
                    <el-table-column prop="id" label="ID" width="100" sortable></el-table-column>
                    <el-table-column prop="appType" label="应用类型"></el-table-column>
                    <el-table-column prop="direction" label="传输方向"></el-table-column>
                    <el-table-column prop="listenIp" label="监听地址"></el-table-column>
                    <el-table-column prop="listenPort" label="监听端口"></el-table-column>
                    <el-table-column prop="dstIp" label="目的地址"></el-table-column>
                    <el-table-column prop="dstPort" label="目的端口"></el-table-column>
                    <el-table-column prop="connIp" label="连接地址"></el-table-column>
                    <el-table-column prop="status" label="连接状态"></el-table-column>
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
        <el-dialog :title="editTitle" v-model="formVisible" :close-on-click-modal="false" size="small">
            <el-form label-width="100px" :model="connRule" :rules="editFormRules" ref="connRule">
                <el-form-item label="应用类型">
                    <el-select v-model="connRule.appType">
                        <el-option v-for="type in  appTypes" :value="type.value" :label="type.name"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="传输方向">
                    <el-select v-model="connRule.direction">
                        <el-option v-for="direction in  directions" :value="direction.value" :label="direction.name"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="监听地址">
                    <el-select v-model="connRule.listenIp.id">
                        <el-option v-for="ip in  listenIps" :value="ip.id" :label="ip.ip"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="监听端口" prop="listenPort">
                    <el-input type="number" v-model="connRule.listenPort" auto-complete="off" placeholder="请输入监听端口"></el-input>
                </el-form-item>
                <el-form-item label="目的地址" prop="dstIp">
                    <el-input v-model="connRule.dstIp" auto-complete="off" placeholder="请输入目的地址"></el-input>
                </el-form-item>
                <el-form-item label="目的端口" prop="dstPort">
                    <el-input v-model="connRule.dstPort" auto-complete="off" placeholder="请输入目的端口"></el-input>
                </el-form-item>
                <el-form-item label="连接地址">
                    <el-table :data="connIpList" border tooltip-effect="dark" @selection-change="coonIpChange" height="200">
                        <el-table-column type="selection" width="55"></el-table-column>
                        <el-table-column prop="id" label="ID" width="100" sortable></el-table-column>
                        <el-table-column prop="ip" label="IP"></el-table-column>
                    </el-table>
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
                    pageSize: 15,
                    tatal: 0
                },
                editTitle: '',
                formVisible: false,
                connRule: {
                    appType: 'TCP',
                    direction: 1,
                    listenIp: {id: 1},
                    listenPort: '',
                    dstIp: '',
                    connIp: [],
                    dstPort: '',
                    status: false
                },
                connIpList: [
                    {id: 1, ip: '163.0.0.3'},
                    {id: 2, ip: '163.0.0.4'},
                    {id: 3, ip: '163.0.0.5'},
                    {id: 4, ip: '163.0.0.6'},
                    {id: 5, ip: '163.0.0.6'},
                ],
                appTypes: [
                    {value: 'TCP', name: 'TCP'},
                    {value: 'UDP', name: 'UDP'},
                    {value: 'HTTP', name: 'HTTP'},
                    {value: 'HTTP_PROXY', name: 'HTTP PROXY'},
                    {value: 'FTP', name: 'FTP'}
                ],
                listenIps: [
                    {id: 1, ip: '163.0.0.1'},
                    {id: 2, ip: '163.0.0.2'}
                ],
                directions: [
                    {value: 1, name: '内端→外端'},
                    {value: 2, name: '外端→内端'}
                ],
                editFormRules: {
                    listenPort: [{validator: checkListenPort, trigger: 'blur'}],
                    dstIp: [{required: true, message: '请输入目的地址', trigger: 'blur'}],
                    dstPort: [{validator: checkDstPort, trigger: 'blur'}]
                }
            }
        },
        methods: {
            //获取checkbox
            handleSelectionChange: function (tab) {

            },
            //跳转页
            handleCurrentChange: function () {

            },
            //获取连接地址
            coonIpChange: function (val) {
                this.connRule.connIp = val;
            },
            initAdd: function () {
                let self = this;
                self.editTitle = '新增';
                self.formVisible = true;
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
                        console.log(self.connRule.connIp);
                        if (self.connRule.connIp.length < 1) {
                            util.dialog.notifyError(self, '至少选择一条连接地址');
                            return false;
                        }
                    }
                })
            }
        },
        mounted: function () {
            let self = this;
            self.$nextTick(function () {
                self.page.tatal = self.cannRuleList.length;
            })
        },
    }
</script>