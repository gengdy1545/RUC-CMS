<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="分类名称" prop="typeName">
        <el-input v-model="queryParams.typeName" placeholder="请输入分类名称" clearable size="small"
          @keyup.enter.native="handleQuery" />
      </el-form-item>

      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd"
          v-hasPermi="['cms:type:add']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" plain icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdate"
          v-hasPermi="['cms:type:edit']">修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete"
          v-hasPermi="['cms:type:remove']">删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport"
          v-hasPermi="['cms:type:export']">导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="typeList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <!-- <el-table-column label="分类ID" align="center" prop="typeId" /> -->
      <el-table-column label="分类图像" align="center" prop="typePic" width="100">
        <template slot-scope="scope">
          <el-image style="width: 28px;height: 28px; border-radius: 50%; margin-right: 10px" :src="scope.row.typePicLink" lazy :preview-src-list="[scope.row.typePicLink]" v-if="scope.row.typePicType == '0'">
            <div slot="error" class="image-slot">
              <i class="el-icon-collection"></i>
            </div>
          </el-image>
          <el-image style="width: 28px;height: 28px; border-radius: 50%; margin-right: 10px" :src="scope.row.typePic" lazy :preview-src-list="[scope.row.typePic]" v-if="scope.row.typePicType == '1'">
            <div slot="error" class="image-slot">
              <i class="el-icon-collection"></i>
            </div>
          </el-image>

        </template>
      </el-table-column>
      <el-table-column label="分类名称" align="center" prop="typeName" />
      <el-table-column label="文章数量" align="center" prop="blogNum" />
      <el-table-column label="创建者" align="center" prop="createBy" />
      <el-table-column label="创建时间" align="center" prop="createTime" width="100">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)"
            v-hasPermi="['cms:type:edit']">修改</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)"
            v-hasPermi="['cms:type:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize"
      @pagination="getList" />

    <!-- 添加或修改分类管理对话框 -->
    <el-dialog :title="title" :visible.sync="open" :before-close="cancel" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="分类名称" prop="typeName">
          <el-input v-model="form.typeName" placeholder="请输入分类名称" />
        </el-form-item>
        <el-form-item label="标签">
          <el-checkbox-group v-model="form.tagIds">
            <el-checkbox v-for="item in tagOptions" :label="item.tagId" :key="item.tagId" :value="item.tagId">
              {{item.tagName}}
            </el-checkbox>
          </el-checkbox-group>
        </el-form-item>
        <el-form-item label="所属菜单">
          <el-checkbox v-model="menuExpand" @change="handleCheckedTreeExpand($event, 'menu')">展开/折叠</el-checkbox>
          <el-checkbox v-model="menuNodeAll" @change="handleCheckedTreeNodeAll($event, 'menu')">全选/全不选</el-checkbox>
          <el-checkbox v-model="form.menuCheckStrictly" @change="handleCheckedTreeConnect($event, 'menu')">父子联动</el-checkbox>
          <el-tree
            class="tree-border"
            :data="menuOptions"
            :show-checkbox="false"
            :highlight-current="true"
            show-checkbox
            ref="menu"
            node-key="id"
            :check-strictly="!form.menuCheckStrictly"
            empty-text="加载中，请稍候"
            :props="defaultProps"
          ></el-tree>
        </el-form-item>
        <el-form-item label="发布日期范围" >
          <el-date-picker type="daterange" v-model="date" format="yyyy-MM-dd"
                          value-format="yyyy-MM-dd" :style="{width: '100%'}" start-placeholder="开始日期" end-placeholder="结束日期"
                          range-separator="至" clearable></el-date-picker>
        </el-form-item>
        <el-form-item label="分类图像">
          <el-radio-group v-model="form.typePicType">
            <el-radio-button label="0">地址</el-radio-button>
            <el-radio-button label="1">上传</el-radio-button>
          </el-radio-group>
          <div v-show="form.typePicType == '0'" class="tabBlock">
            <el-input v-model="form.typePicLink" placeholder="请输入图片地址 https://" style="margin-bottom: 10px;" />
            <el-image :src="form.typePicLink" :preview-src-list="[form.typePicLink]" fit="cover" class="typePic" >
              <div slot="error" class="image-slot">
                <i class="el-icon-collection"></i>
              </div>
            </el-image>
          </div>
          <div v-show="form.typePicType == '1'" class="tabBlock">
            <imageUpload v-model="form.typePic" :limit="1" />
          </div>
        </el-form-item>

      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import {
    listType,
    getType,
    delType,
    addType,
    updateType,
    cancelType
  } from "@/api/cms/type";
  import { treeselect as menuTreeselect ,typeMenuTreeselect} from "@/api/system/menu";
  export default {
    name: "Type",
    data() {
      return {
        // 遮罩层
        loading: true,
        // 选中数组
        ids: [],
        names: [],
        // 非单个禁用
        single: true,
        // 非多个禁用
        multiple: true,
        // 显示搜索条件
        showSearch: true,
        // 总条数
        total: 0,
        // 分类管理表格数据
        typeList: [],
        tagOptions: [],
        menuOptions: [],
        menuNodeAll: false,
        menuExpand: false,
        date:null,
        menuId: null,
        menuIds: [],
        // 弹出层标题
        title: "",
        // 是否显示弹出层
        open: false,
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          typeName: null,
          typePic: null,
          createBy: null
        },
        // 表单参数
        form: {},
        defaultProps: {
          children: "children",
          label: "label"
        },
        // 表单校验
        rules: {
          typeName: [{
            required: true,
            message: "分类名称不能为空",
            trigger: "blur"
          }],
        }
      };
    },
    created() {
      this.getList();
    },
    methods: {
      /** 查询分类管理列表 */
      getList() {
        this.loading = true;
        listType(this.queryParams).then(response => {
          for (let i = 0; i < response.rows.length; i++) {
            let typeInfo = response.rows[i];
            if (typeInfo.typePic.length > 0) {
              response.rows[i].typePic = process.env.VUE_APP_BASE_API + typeInfo.typePic
            }
          };
          this.typeList = response.rows;
          this.total = response.total;
          this.loading = false;
        });
      },
      // 取消按钮
      cancel() {
            cancelType(this.form).then(response => {
              this.open = false;
              this.reset();
            });
      },
      // 表单重置
      reset() {
        this.menuNodeAll = false,
        this.menuExpand = false,
        this.form = {
          typeId: null,
          createBy: null,
          createTime: null,
          updateBy: null,
          updateTime: null,
          typeName: null,
          tagIds: [],
          date:null,
          menuId:null,
          typePicType: '0',
          typePic: null,
          typePicLink: null,
          startTime: null,
          endTime: null,
        };
        this.resetForm("form");
      },
      handleCheckedTreeExpand(value, type) {
        if (type == 'menu') {
          let treeList = this.menuOptions;
          for (let i = 0; i < treeList.length; i++) {
            this.$refs.menu.store.nodesMap[treeList[i].id].expanded = value;
          }
        } else if (type == 'dept') {
          let treeList = this.deptOptions;
          for (let i = 0; i < treeList.length; i++) {
            this.$refs.dept.store.nodesMap[treeList[i].id].expanded = value;
          }
        }
      },
      handleCheckedTreeConnect(value, type) {
        if (type == 'menu') {
          this.form.menuCheckStrictly = value ? true: false;
        } else if (type == 'dept') {
          this.form.deptCheckStrictly = value ? true: false;
        }
      },
      getTypeMenuTreeselect(typeId) {
        return typeMenuTreeselect(typeId).then(response => {
          this.menuOptions = response.menus;
          return response;
        });
      },
      // 树权限（全选/全不选）
      handleCheckedTreeNodeAll(value, type) {
        if (type == 'menu') {
          this.$refs.menu.setCheckedNodes(value ? this.menuOptions: []);
        } else if (type == 'dept') {
          this.$refs.dept.setCheckedNodes(value ? this.deptOptions: []);
        }
      },
      handleNodeClick(node) {
        this.form.menuId = node.id;
      },
      /** 搜索按钮操作 */
      handleQuery() {
        this.queryParams.pageNum = 1;
        this.getList();
      },
      /** 重置按钮操作 */
      resetQuery() {
        this.resetForm("queryForm");
        this.handleQuery();
      },
      // 多选框选中数据
      handleSelectionChange(selection) {
        this.ids = selection.map(item => item.typeId)
        this.names = selection.map(item => item.typeName)
        this.single = selection.length !== 1
        this.multiple = !selection.length
      },
      /** 查询菜单树结构 */
      getMenuTreeselect() {
        menuTreeselect().then(response => {
          this.menuOptions = response.data;
        });
      },
      /** 新增按钮操作 */
      handleAdd() {
        getType(0).then(response => {
          this.tagOptions = response.tags;
          this.reset();
          this.getMenuTreeselect();
          this.open = true;
          this.title = "添加分类管理";
        });
      },
      /** 修改按钮操作 */
      handleUpdate(row) {
        this.reset();
        const typeId = row.typeId || this.ids
        const typeMenu = this.getTypeMenuTreeselect(typeId);
        getType(typeId).then(response => {
          this.tagOptions = response.tags;
          this.form = response.data;
          this.open = true;
          this.$nextTick(() => {
            typeMenu.then(res => {
              let checkedKeys = res.checkedKeys
              checkedKeys.forEach((v) => {
                this.$nextTick(()=>{
                  this.$refs.menu.setChecked(v, true ,false);
                })
              })
            });
          });
          this.title = "修改分类管理";
        });
      },
      // 所有菜单节点数据
      getMenuAllCheckedKeys() {
        // 目前被选中的菜单节点
        let checkedKeys = this.$refs.menu.getCheckedKeys();
        // 半选中的菜单节点
        let halfCheckedKeys = this.$refs.menu.getHalfCheckedKeys();
        checkedKeys.unshift.apply(checkedKeys, halfCheckedKeys);
        return checkedKeys;
      },
      /** 提交按钮 */
      submitForm() {
        this.$refs["form"].validate(valid => {
          if (valid) {
            if (this.form.typeId != null) {
              this.form.menuIds = this.getMenuAllCheckedKeys();
              updateType(this.form).then(response => {
                this.$modal.msgSuccess("修改成功");
                this.open = false;
                this.getList();
              });
            } else {
              this.form.menuIds = this.getMenuAllCheckedKeys();
              addType(this.form).then(response => {
                this.$modal.msgSuccess("新增成功");
                this.open = false;
                this.getList();
              });
            }
          }
        });
      },
      /** 删除按钮操作 */
      handleDelete(row) {
        const typeIds = row.typeId || this.ids;
        let name = row.typeName || this.names;
        this.$modal.confirm('是否确认删除"' + name + '"？').then(function() {
          return delType(typeIds);
        }).then(() => {
          this.getList();
          this.$modal.msgSuccess("删除成功");
        }).catch(() => {});
      },
      /** 导出按钮操作 */
      handleExport() {
        this.download('cms/type/export', {
          ...this.queryParams
        }, `type_${new Date().getTime()}.xlsx`)
      }
    }
  };
</script>

<style scoped lang="scss">
  .tabBlock {
    height: 180px;
    margin-top: 20px;
  }
  .typePic {
    width: 28px;
    height: 28px;
    border-radius: 50%;
  }
</style>
