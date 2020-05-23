<template>
    <div class="app-container">
        <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
                                                                                                                                            <el-form-item label="消息标题" prop="title">
                            <el-input
                                    v-model="queryParams.title"
                                    placeholder="请输入消息标题"
                                    clearable
                                    size="small"
                                    @keyup.enter.native="handleQuery"
                            />
                        </el-form-item>
                                                                                                                                                    <el-form-item label="消息类型" prop="type">
                            <el-select v-model="queryParams.type" placeholder="请选择消息类型" clearable size="small">
                                <el-option label="请选择字典生成" value="" />
                            </el-select>
                        </el-form-item>
                                                                                                                                                    <el-form-item label="内容" prop="content">
                            <el-input
                                    v-model="queryParams.content"
                                    placeholder="请输入内容"
                                    clearable
                                    size="small"
                                    @keyup.enter.native="handleQuery"
                            />
                        </el-form-item>
                                                                                                                                                                                                                                                                                                                                                                    <el-form-item label="归属机构" prop="deptId">
                            <el-input
                                    v-model="queryParams.deptId"
                                    placeholder="请输入归属机构"
                                    clearable
                                    size="small"
                                    @keyup.enter.native="handleQuery"
                            />
                        </el-form-item>
                                                                                                                                                    <el-form-item label="扩展字段" prop="extend">
                            <el-input
                                    v-model="queryParams.extend"
                                    placeholder="请输入扩展字段"
                                    clearable
                                    size="small"
                                    @keyup.enter.native="handleQuery"
                            />
                        </el-form-item>
                                                                                                                                                                                <el-form-item label="创建时间">
                            <el-date-picker v-model="dateRange"
                                            size="small"
                                            style="width: 240px"
                                            value-format="yyyy-MM-dd"
                                            type="daterange"
                                            range-separator="-"
                                            start-placeholder="开始日期"
                                            end-placeholder="结束日期"
                            ></el-date-picker>
                        </el-form-item>
                                                                                                                                                                            <el-form-item>
                <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
                <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
            </el-form-item>
        </el-form>

        <el-row :gutter="10" class="mb8">
            <el-col :span="1.5">
                <el-button
                        type="primary"
                        icon="el-icon-plus"
                        size="mini"
                        @click="handleAdd"
                        v-hasPerm="['infoContent_add']"
                >新增</el-button>
            </el-col>
            <el-col :span="1.5">
                <el-button
                        type="success"
                        icon="el-icon-edit"
                        size="mini"
                        :disabled="single"
                        @click="handleEdit"
                        v-hasPerm="['infoContent_edit']"
                >修改</el-button>
            </el-col>
            <el-col :span="1.5">
                <el-button
                        type="danger"
                        icon="el-icon-delete"
                        size="mini"
                        :disabled="multiple"
                        @click="handleDel"
                        v-hasPerm="['infoContent_del']"
                >删除</el-button>
            </el-col>
            <el-col :span="1.5">
                <el-button
                        type="warning"
                        icon="el-icon-download"
                        size="mini"
                        @click="handleExport"
                        v-hasPerm="['infoContent_export']"
                >导出</el-button>
            </el-col>
            <div class="top-right-btn">
                <el-tooltip class="item" effect="dark" content="刷新" placement="top">
                    <el-button size="mini" circle icon="el-icon-refresh" @click="handleQuery" />
                </el-tooltip>
                <el-tooltip class="item" effect="dark" :content="showSearch ? '隐藏搜索' : '显示搜索'" placement="top">
                    <el-button size="mini" circle icon="el-icon-search" @click="showSearch=!showSearch" />
                </el-tooltip>
            </div>
        </el-row>

        <el-table v-loading="loading" :data="infoContentList" @selection-change="handleSelectionChange">
            <el-table-column type="selection" width="55" align="center" />
                                                                                <el-table-column label="编号" align="center" prop="id" />
                                                                                                <el-table-column label="消息标题" align="center" prop="title" />
                                                                                                <el-table-column label="消息类型" align="center" prop="type" />
                                                                                                <el-table-column label="内容" align="center" prop="content" />
                                                                                                <el-table-column label="附件" align="center" prop="files" />
                                                                                                <el-table-column label="连接" align="center" prop="url" />
                                                                                                <el-table-column label="归属机构" align="center" prop="deptId" />
                                                                                                <el-table-column label="扩展字段" align="center" prop="extend" />
                                                                                                                                                            <el-table-column label="创建时间" align="center" prop="createTime" width="180">
                        <template slot-scope="scope">
                            <span>{{ parseTime(scope.row.createTime) }}</span>
                        </template>
                    </el-table-column>
                                                                                                                                                                                                                                                                                        <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
                <template slot-scope="scope">
                    <el-button
                            size="mini"
                            type="text"
                            icon="el-icon-edit"
                            @click="handleEdit(scope.row)"
                            v-hasPerm="['infoContent_edit']"
                    >修改</el-button>
                    <el-button
                            size="mini"
                            type="text"
                            icon="el-icon-delete"
                            @click="handleDel(scope.row)"
                            v-hasPerm="['infoContent_del']"
                    >删除</el-button>
                </template>
            </el-table-column>
        </el-table>

        <pagination
                v-show="total>0"
                :total="total"
                :page.sync="queryParams.current"
                :limit.sync="queryParams.size"
                @pagination="getList"
        />

        <!-- 添加或修改消息内容对话框 -->
        <el-dialog :title="title" :visible.sync="open">
            <el-form ref="form" :model="form" :rules="rules" label-width="80px">
                <el-row>
                                                                                                                                                                                                                                                        <el-col :span="24">
                                        <el-form-item label="消息标题" prop="title">
                                            <el-input v-model="form.title" placeholder="请输入消息标题" />
                                        </el-form-item>
                                    </el-col>
                                                                                                                                                                                                                                                                                                <el-col :span="24">
                                        <el-form-item label="消息类型">
                                            <el-select v-model="form.type" placeholder="请选择消息类型">
                                                <el-option label="请选择字典生成" value="" />
                                            </el-select>
                                        </el-form-item>
                                    </el-col>
                                                                                                                                                                                                                                                                                                <el-col :span="24">
                                        <el-form-item label="内容" prop="content">
                                            <el-input v-model="form.content" placeholder="请输入内容" />
                                        </el-form-item>
                                    </el-col>
                                                                                                                                                                                                                                                                                                <el-col :span="24">
                                        <el-form-item label="附件" prop="files">
                                            <el-input v-model="form.files" type="textarea" placeholder="请输入内容" />
                                        </el-form-item>
                                    </el-col>
                                                                                                                                                                                                                                                                                                <el-col :span="24">
                                        <el-form-item label="连接" prop="url">
                                            <el-input v-model="form.url" type="textarea" placeholder="请输入内容" />
                                        </el-form-item>
                                    </el-col>
                                                                                                                                                                                                                                                                                                <el-col :span="24">
                                        <el-form-item label="归属机构" prop="deptId">
                                            <el-input v-model="form.deptId" placeholder="请输入归属机构" />
                                        </el-form-item>
                                    </el-col>
                                                                                                                                                                                                                                                                                                <el-col :span="24">
                                        <el-form-item label="扩展字段" prop="extend">
                                            <el-input v-model="form.extend" placeholder="请输入扩展字段" />
                                        </el-form-item>
                                    </el-col>
                                                                                                                                                                                                                                                                                                                                                                                                </el-row>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button type="primary" @click="submitForm">确 定</el-button>
                <el-button @click="cancel">取 消</el-button>
            </div>
        </el-dialog>
    </div>
</template>

<script>
    import { listInfoContent, getInfoContent, delInfoContent, addInfoContent, editInfoContent, exportInfoContent } from "@/api/msg/infoContent";

    export default {
        name: "InfoContent",
        data() {
            return {
                // 遮罩层
                loading: true,
                // 选中数组
                ids: [],
                // 非单个禁用
                single: true,
                // 非多个禁用
                multiple: true,
                // 总条数
                total: 0,
                // 消息内容表格数据
                    infoContentList: [],
                // 弹出层标题
                title: "",
                // 是否显示弹出层
                open: false,
                // 日期范围
                dateRange: [],
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        // 查询参数
                queryParams: {
                    current: 1,
                    size: 10,
                                                                            title: undefined,
                                                                type: undefined,
                                                                content: undefined,
                                                                files: undefined,
                                                                url: undefined,
                                                                deptId: undefined,
                                                                extend: undefined,
                                                                                            createTime: undefined,
                                                                                                                                                    },
            // 显示搜索条件
            showSearch: true,
                    // 表单参数
                    form: {},
            // 表单校验
            rules: {
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    }
        };
        },
        created() {
            this.getList();
                                                                                                                                                                                                                                                                                                                                                                                                                            },
        methods: {
            /** 查询消息内容列表 */
            getList() {
                this.loading = true;
                listInfoContent(this.addDateRange(this.queryParams, this.dateRange)).then(response => {
                    this.infoContentList = response.data;
                    this.total = response.total;
                    this.loading = false;
                });
            },
                                                                                                                                                                                                                                                                                                                                                                                                                                // 取消按钮
            cancel() {
                this.open = false;
                this.reset();
            },
            // 表单重置
            reset() {
                this.form = {
                                                            id: undefined,
                                                                                title: undefined,
                                                                                type: undefined,
                                                                                content: undefined,
                                                                                files: undefined,
                                                                                url: undefined,
                                                                                deptId: undefined,
                                                                                extend: undefined,
                                                                                createBy: undefined,
                                                                                createTime: undefined,
                                                                                updateBy: undefined,
                                                                                updateTime: undefined,
                                                                                remarks: undefined,
                                                                                delFlag: undefined
                                                };
                this.resetForm("form");
            },
            /** 搜索按钮操作 */
            handleQuery() {
                this.queryParams.current = 1;
                this.getList();
            },
            /** 重置按钮操作 */
            resetQuery() {
                this.resetForm("queryForm");
            },
            // 多选框选中数据
            handleSelectionChange(selection) {
                this.ids = selection.map(item => item.id)
                this.single = selection.length!=1
                this.multiple = !selection.length
            },
            /** 新增按钮操作 */
            handleAdd() {
                this.reset();
                this.open = true;
                this.title = "添加消息内容";
            },
            /** 修改按钮操作 */
            handleEdit(row) {
                this.reset();
                const id = row.id || this.ids
                getInfoContent(id).then(response => {
                    this.form = response.data;
                    this.open = true;
                    this.title = "修改消息内容";
                });
            },
            /** 提交按钮 */
            submitForm: function() {
                this.$refs["form"].validate(valid => {
                    if (valid) {
                        if (this.form.id != undefined) {
                            editInfoContent(this.form).then(response => {
                                if (response.code === 0) {
                                    this.msgSuccess("修改成功");
                                    this.open = false;
                                    this.getList();
                                } else {
                                    this.msgError(response.msg);
                                }
                            });
                        } else {
                            addInfoContent(this.form).then(response => {
                                if (response.code === 0) {
                                    this.msgSuccess("新增成功");
                                    this.open = false;
                                    this.getList();
                                } else {
                                    this.msgError(response.msg);
                                }
                            });
                        }
                    }
                });
            },
            /** 删除按钮操作 */
            handleDel(row) {
                const ids = row.id || this.ids;
                this.$confirm('是否确认删除消息内容编号为"' + ids + '"的数据项?', "警告", {
                    confirmButtonText: "确定",
                    cancelButtonText: "取消",
                    type: "warning"
                }).then(function() {
                    return delInfoContent(ids);
                }).then(() => {
                    this.getList();
                    this.msgSuccess("删除成功");
                }).catch(function() {});
            },
            /** 导出按钮操作 */
            handleExport() {
                const queryParams = this.queryParams;
                this.$confirm('是否确认导出所有消息内容数据项?', "警告", {
                    confirmButtonText: "确定",
                    cancelButtonText: "取消",
                    type: "warning"
                }).then(function() {
                    return exportInfoContent(queryParams);
                }).then(response => {
                    this.download(response.data);
                }).catch(function() {});
            }
        }
    };
</script>
