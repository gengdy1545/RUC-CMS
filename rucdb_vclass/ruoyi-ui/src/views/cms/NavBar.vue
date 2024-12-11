<template>
    <div class="header_wrap">
        <el-header>
            <div class="image-container">
                <a href="/cms/main/cmsIndex">
                    <img src="@/assets/logo/logo.jpg" class="logo" @click="menuAway"></img>
                </a>
            </div>
            <el-col class="menu-container">
                <!-- Top Section -->
                <template>
                    <div class="bg-purple-light">
                        <el-menu :default-active="activeIndex" router class="el-menu-demo" mode="horizontal"
                            style="border: none;">
                            <!-- 一级菜单 -->
                            <el-menu-item v-for="column in topColumns" :key="column.title" class="flex-menu-item">
                                <div v-on="$listeners" @mouseenter="mouseenter(column)"
                                    @mouseleave="mouseleave(column)">
                                    <a :href="column.name">{{ column.meta.title }}</a>
                                </div>
                                <!-- 二级菜单 -->
                                <div v-if="column.del && column.children" v-on="$listeners" class="sub-menu-container"
                                    @mouseenter="keepSubMenuVisible(column)" @mouseleave="keepSubMenuUnVisible(column)">
                                    <el-tree :data="column.children" node-key="title" :expand-on-click-node="true"
                                        :highlight-current="false" @node-click="handleSubColumnClick"
                                        class="sub-menu-tree">
                                        <div class="custom-tree-node" slot-scope="{  data }">
                                            <a :href="data.path">{{ data.meta.title }}</a>
                                        </div>
                                    </el-tree>
                                </div>
                            </el-menu-item>
                            <!--            <el-menu-item style="visibility: hidden">test</el-menu-item>-->
                        </el-menu>
                    </div>
                </template>
            </el-col>
            <div class="bg-purple-light el-menu-hidden" v-if="menuHiddenVisiable">
                <el-menu :default-active="activeIndex" router background-color="rgba(84,92,100,0.5)" text-color="#fff"
                    active-text-color="#ffd04b">
                    <el-menu-item index="/cms/main/cmsIndex" @click="menuAway"><i class="el-icon-s-home"
                            style="color: rgba(255, 255, 255);"></i>首1页
                    </el-menu-item>
                    <el-menu-item :index="item.path" v-for="item in menulist" :key="item.id" @click="menuAway">
                        <!--                图标-->
                        <i :class="item.icon" style="color: rgba(255, 255, 255);"></i>
                        <!--                文本-->
                        {{ item.authName }}
                    </el-menu-item>
                </el-menu>
            </div>

            <div class="menu-expend" @click="menuExpend">
                <i class="el-icon-menu" style="color: rgba(255, 255, 255);"></i>
            </div>
        </el-header>
    </div>
</template>

<script>
import axios from 'axios';
import { mapGetters } from 'vuex';
import 'element-ui/lib/theme-chalk/display.css';
import { cmsListBlog } from "@/api/cms/blog";
import { fetchColumns } from "@/api/cms/column";

export default {
    inheritAttrs: false,
    name: 'cmsNavBar',
    data() {
        return {
            activeIndex: 'this.$router.path',
            searchInput: true,
            menuHiddenVisiable: false,
            headerBottom: 0,
            queryInfo: {
                query: '',
                timer: null
            },
            searchList: [],
            searching: false,
            columns: [],
            activeTab: '',
            queryParams: {
                pageNum: 1,
                pageSize: 10,
                title: null,
                type: 1,
                content: null,
                top: null,
                views: null,
                status: null
            }
        };
    },
    computed: {
        ...mapGetters(['avatar', 'name']),

        topColumns() {
            return Array.isArray(this.columns)
                ? this.columns.filter(column => column.showPosition === 0)
                : [];
        },
        middleColumns() {
            return Array.isArray(this.columns)
                ? this.columns.filter(column => column.showPosition === 1)
                : [];
        },
        rightColumns() {
            return Array.isArray(this.columns)
                ? this.columns.filter(column => column.showPosition === 2)
                : [];
        },
    },
    watch: {
        'queryInfo.query': {
            handler(value) {
                if (this.timer) {
                    clearTimeout(this.timer);
                }
                this.timer = setTimeout(() => {
                    this.searchBlog();
                }, 300);
            }
        }
    },
    created() {
        fetchColumns()
            .then(response => {
                this.columns = response; // 将返回的数组赋值给 `columns`
            })
            .catch(error => {
                console.error('Failed to fetch columns:', error);
                this.columns = []; // 如果失败，设为空数组
            });
    },
    methods: {
        mouseenter(column) {
            this.$set(column, 'del', true)
        },
        mouseleave(column) {
            this.$set(column, 'del', false)
        },
        // 子菜单保持可见
        keepSubMenuVisible(column) {
            this.$set(column, 'del', true)
        },
        keepSubMenuUnVisible(column) {
            this.$set(column, 'del', false)
        },
        // 处理子菜单点击事件
        handleSubColumnClick(column) {
            this.$router.push(column.data.path);
        },


        menulistAdd() {
            this.menulist.unshift({ id: "", authName: "" });
        },
        ResponsiveLayout() {
            var w = window.innerWidth || document.body.clientWidth;
            var h = window.innerHeight || document.body.clientHeight;
            console.log(w, h);
        },
        menuExpend() {
            this.menuHiddenVisiable = !this.menuHiddenVisiable;
            if (this.menuHiddenVisiable === true) {
                this.headerBottom = (this.menulist.length + 1) * 56;
            } else {
                this.headerBottom = 0;
            }
        },
        menuAway() {
            this.menuHiddenVisiable = false;
            this.headerBottom = 0;
        },
        notSearching() {
            setTimeout(() => {
                this.searching = false;
            }, 100);
        },
        checkInput() {
            this.searching = this.queryInfo.query !== '';
        },


        async searchBlog() {
            if (this.queryInfo.query === '') {
                this.searching = false;
                return;
            }
            this.queryParams.title = this.queryInfo.query;
            cmsListBlog(this.queryParams).then(response => {
                let lsitSize = response.rows.length;
                if (lsitSize > 0) {
                    for (let i = 0; i < lsitSize; i++) {
                        let redTitle = this.brightenKeyword(response.rows[i].title, this.queryInfo.query);
                        response.rows[i].title = redTitle;
                    }
                }
                this.searchList = response.rows;
                if (this.searchList.length !== 0) {
                    this.searching = true;
                }
            });
        },
        brightenKeyword(val, keyword) {
            const Reg = new RegExp(keyword, 'i');
            let res = '';
            if (val) {
                res = val.replace(Reg, `<span style="color: red;">${keyword}</span>`);
                return res;
            }
        },
        getBlogInfo(blogId) {
            let routeUrl = this.$router.resolve({
                path: '/cms/main/blog',
                query: { id: blogId }
            });
            window.open(routeUrl.href, '_blank');
        },

    }
};
</script>

<style scoped>
.header_wrap {
    width: 100%;
    height: 78px;
    background-color: #FFFFFF;
}

.el-header {
    max-width: 1300px;
    height: 100% !important;
    display: flex;
    justify-content: center;
    align-items: center;
    transition: .2s;
    margin: 0 auto;
    padding: 0;
}

.el-header:hover {
    opacity: 1 !important;
}

.bg-purple-light {
    float: right;
}

.el-menu {
    flex-shrink: 0;
    background-color: rgba(0, 0, 0, 0) !important;
}

.el-menu /deep/ .el-menu-item {
    color: black !important;
}

.el-menu-item:hover {
    color: #FFFFFF !important;
    background-color: #0e419c !important;
}

.sub-menu-container:hover {
    color: #FFFFFF !important;
    background-color: #0e419c !important;
}


.sub-menu-container {
    top: 100%;
    /* 紧贴一级菜单 */
    width: 100%;
    left: 0;
    position: absolute;
    background-color: #0e419c !important;
    z-index: 100;
    /*box-shadow: 0 2px 10px rgba(0, 0, 0, 0.2);*/
    /*  min-width: 100px; !* 设置最小宽度 *!
  max-width: 180px; !* 可选：设置最大宽度，防止过长 *!*/
    white-space: nowrap;
    /* 防止文字换行 */
    overflow: hidden;
    /* 隐藏超出的文字 */
    text-overflow: ellipsis;
    /* 超出文字用省略号表示 */
}

.sub-menu-container .el-tree {
    width: 100%;
    /* 确保二级菜单树宽度 100% */
}

.sub-menu-tree:hover {
    color: #fecc01 !important;
    background-color: transparent !important;
}

.custom-tree-node:hover {
    color: #fecc01 !important;
    background-color: transparent !important;
}

.sub-menu-tree {
    background-color: #0e419c;
    border: gray;
}

.sub-menu-tree:hover {
    border: none;
}

.custom-tree-node {
    display: flex;
    align-items: center;
    color: #fff;
    /* 文本颜色 */
    top: 10px !important;
    bottom: 10px !important;
}

.image-container {
    align-items: center;
    position: relative;
}

.logo {
    float: left;
    max-width: 100%;
    /* 确保图片宽度不超过其容器 */
    height: auto;
    /* 保持图片的原始比例 */
}

.menu-container {
    display: flex;
    justify-content: right;
    /* Center the menu horizontally */
    flex-grow: 1;
    /* Allow the menu to take the remaining space */
}

.el-menu-hidden {
    position: absolute;
    top: 60px;
    left: 0;
    border-top: 1px solid #ccc;
    border-right: none;
    width: 100%;
}

.el-menu-demo {
    display: flex;
    flex-wrap: nowrap;
    /* 禁止换行 */
    flex-shrink: 1;
    /* 允许收缩 */
    min-width: 0;
    /* 允许子元素缩小到小于其内容的宽度 */
    font-size: 28px;
}

.flex-menu-item {
    display: flex;
    align-items: center;
    position: relative;
    flex-shrink: 1;
    /* 允许子元素收缩 */
    min-width: 0;
    /* 允许子元素缩小到小于其内容的宽度 */
    font-size: 16px;
}

.menu-expend {
    display: none !important;
}

@media screen and (max-width: 768px) {
    .el-menu /deep/ .el-menu-item {
        background-color: rgba(0, 0, 0, 0.3) !important;
    }


    .menu-expend {
        display: block !important;
        float: right;
    }

    .menu-expend:hover {
        color: #ffd04b;
        cursor: pointer;
    }
}

.el-tree /deep/ .el-tree-node__content {
    height: 40px;
    border-top: solid 1px #114ebc;
}

.el-tree /deep/ .el-tree-node__content:hover {
    background-color: #0e419c!important;
}
</style>
