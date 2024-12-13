<template>
    <el-row :gutter="20">
        <el-col :sm="4" class="hidden-xs-only" style="opacity:0;">左侧占位</el-col>
        <el-col :sm="4" class="hidden-xs-only">
            <div class="sidebar" v-if="currentFirstMenu">
                <!-- 一级菜单标题 -->
                <div class="first-menu">
                    <a :href="currentFirstMenu.name">{{ currentFirstMenu.meta.title }}</a>
                </div>
                <div class="second-menu">
                    <li v-for="child in currentFirstMenu.children" :key="child.name">
                        <router-link 
                            :to="child.name" 
                            :class="{'active-menu': currentSecondMenu && currentSecondMenu.name === child.name}"
                            class="second-menu-link">
                            {{ child.meta.title }}
                        </router-link>
                    </li>
                </div>
            </div>
        </el-col>
        <el-col :xs="24" :sm="12">
            <el-card style="background-color: rgba(255, 255, 255,1)" class="first-card">
                <div slot="header" class="total blog-info">
                    <div class="user-info">
                        <i class="el-icon-user"></i>
                        <span class="header"> {{ blog.createBy }}</span>
                    </div>
                    <div class="blog-date">
                        <i class="el-icon-date"></i>
                        <span> {{ blog.updateTime ? blog.updateTime : blog.createTime }}</span>
                    </div>
                </div>
                <h2 class="blog-title header">{{ blog.title }}
                    <el-tag size="mini" v-for="tag in blog.types" :key="tag.typeId" type="info">{{ tag.typeName
                        }}</el-tag>
                </h2>
                <div v-if="blog.contentType === '1'" class="typo m-padded-lr-responsive m-padded-tb-large ql-editor"
                    v-html="blog.content"></div>
                <div v-if="blog.contentType === '3'" v-html="blog.content"></div>
                <CherryMarkdown ref="CherryMarkdown" v-if="blog.contentType === '2'" v-model='blog.contentMarkdown'
                    :defaultModel="'previewOnly'"></CherryMarkdown>
                <div class="tags">
                    <div class="tag-item" v-for="tag in blog.tags" :key="tag.tagId">
                        <div class="sjx-outer">
                            <div class="sjx-inner"></div>
                        </div>
                        <div class="tag">
                            {{ tag.tagName }}
                        </div>
                    </div>
                </div>
                <el-table v-if="blog.blogFilesNew && blog.blogFilesNew.length > 0" :data="blog.blogFilesNew" :border="true"
                    style="width: 99.99%;">
                    <el-table-column align="center" min-width="30%" prop="remark" label="附件">
                        <template slot-scope="scope">
                            <el-row>
                                <el-col :span="6">
                                    <div class="blogFilesInfoName">名称：</div>
                                </el-col>
                                <el-col :span="18">
                                    <el-input v-model="scope.row.fileOriginName" disabled />
                                </el-col>
                            </el-row>
                        </template>
                    </el-table-column>
                    <el-table-column align="center" min-width="50%" prop="remark" label="备注">
                        <template slot-scope="scope">
                            <el-input v-model="scope.row.remark" type="textarea" :rows="1" size="small" disabled />
                        </template>
                    </el-table-column>
                    <el-table-column align="center" min-width="20%" label="操作">
                        <template slot-scope="scope">
                            <el-button size="mini" plain @click="handleDownload(scope.row)">下载</el-button>
                            <el-button size="mini" plain @click="handlePreview(scope.row)">预览</el-button>
                        </template>
                    </el-table-column>
                </el-table>
            </el-card>
        </el-col>
        <el-col :xs="24" :sm="0"></el-col>
        <el-col :sm="4" class="hidden-xs-only" style="opacity:0;">右侧占位</el-col>
        <!-- 设置底部距离的 -->
        <el-backtop :bottom="60">
            <div style="{
            height: 50px;
            width: 50px;
            background-color: rgba(240,239,241,1);
            box-shadow: 0 0 6px rgba(0,0,0, .12);
            text-align: center;
            line-height: 40px;
            border-radius:2px;
            color: #1989fa;
          }">
                <svg-icon icon-class="top" />
            </div>
        </el-backtop>
    </el-row>
</template>

<script>
// import 'cherry-markdown/dist/cherry-markdown.min.css'
//使用prism.js代码高亮
import '@/views/cms/plugins/prism.js'
import '@/views/cms/plugins/prism.css'
import comment from "./comment/Ipcomment"
import {
    getBlogDetail,
    addBlogViews,
} from "@/api/cms/blog";
import { mapState } from 'vuex'
import CherryMarkdown from '@/components/CherryMarkdown'
import { listFileInfoInBlog } from "@/api/cms/fileInfo";
import { getToken } from '@/utils/auth'
import { fetchColumns } from "@/api/cms/column";

export default {
    components: {
        comment,
        CherryMarkdown,
    },
    data() {
        return {
            blog: {},
            commentForm: {
                content: ''
            },
            columns: [], //存储菜单数据
            currentFirstMenu: null, // 当前一级菜单
            currentSecondMenu: null, //当前二级菜单
        }
    },
    watch: {
        '$route'(to, from) {
            this.$router.go(0);
        }
    },
    async created() {
        try {
            // 获取菜单数据
            this.columns = await fetchColumns();
            // 获取博客数据
            await this.getBlogInfomation();
            // 设置侧边栏菜单
            this.setSidebarMenu();
        } catch (error) {
            console.error('Error fetching menu or blog data:', error);
        }
    },
    computed: {
        ...mapState([
            'userInfo',
            'administrator',
        ]),
    },
    methods: {
        getMenuIdFromPath(path) {
            const match = path.match(/\/cms\/main\/menu[?&]id=(\d+)/);
            return match ? parseInt(match[1], 10) : null;
        },
        // 获取文章详情信息
        async getBlogInfomation() {
            // 增加阅读量
            addBlogViews(this.$route.query.id);
            try {
                const response = await getBlogDetail(this.$route.query.id);
                const { data: res } = response;
                this.blog = res;
                this.blog.blogFilesNew = [];
                if (res.blogFiles !== null) {
                    this.blog.blogFilesNew = JSON.parse(res.blogFiles);
                }
            } catch (error) {
                console.error('Failed to fetch blog details:', error);
            }
        },
        //设置侧边栏菜单
        setSidebarMenu() {
            if (!this.columns.length || !this.blog) return;
            for (const firstMenu of this.columns) {
                if (firstMenu.name == this.$route.fullPath) {
                    this.currentFirstMenu = firstMenu;
                }
                // 检查二级菜单
                if (firstMenu.children && firstMenu.children.length > 0) {
                    for (const childMenu of firstMenu.children) {
                        if (childMenu.name == this.$route.fullPath) {
                            this.currentFirstMenu = firstMenu;
                            this.currentSecondMenu = childMenu;
                            break;
                        }
                    }
                }
            }
            const blogMenuId = this.blog.menuIds[0];
            // 检查是否属于某一个 menu 菜单
            for (const firstMenu of this.columns) {
                if (this.getMenuIdFromPath(firstMenu.name) == blogMenuId) {
                    this.currentFirstMenu = firstMenu;
                    break;
                }
                // 检查二级菜单
                if (firstMenu.children && firstMenu.children.length > 0) {
                    for (const childMenu of firstMenu.children) {
                        if (this.getMenuIdFromPath(childMenu.name) == blogMenuId) {
                            this.currentFirstMenu = firstMenu;
                            this.currentSecondMenu = childMenu;
                            break;
                        }
                    }
                }
            }
        },
        // 文件下载处理
        handleDownload(row) {
            var onlyLoginCanDownload;
            listFileInfoInBlog({ filePath: row.fileId }).then(response => {

                onlyLoginCanDownload = response.rows[0].onlyLoginCanDownload;
            }).then(() => {
                if (!getToken() && onlyLoginCanDownload === 1) {
                    this.$message.error("请先登录！")
                } else {
                    var name = row.fileOriginName;
                    var url = row.filePath;
                    var suffix = url.substring(url.lastIndexOf("."), url.length);
                    const a = document.createElement('a')
                    a.setAttribute('download', name)
                    a.setAttribute('target', '_blank')
                    a.setAttribute('href', process.env.VUE_APP_BASE_API + url)
                    a.click()
                }
            })

        },
        // 文件预览处理
        async handlePreview(row) {
            var name = row.fileOriginName;
            var filePath = row.filePath;
            var url = process.env.VUE_APP_BASE_API + filePath;
            const response = await fetch(url);
            if (response.status === 200) {
                const blob = await response.blob()
                var objectURL = window.URL.createObjectURL(blob);
                var openUrl = window.location.origin + '/fileViewer/index.html' + '?name=' + name + '&url=' + objectURL
                window.open(openUrl);
            } else {
                this.$message.error("文件不存在！")
            }
        },
    },

}
</script>

<style scoped>
.el-card {
    width: 100%;
}

.el-popper /deep/ {
    box-shadow: 0 2px 4px 0 rgb(34 36 38 / 12%),
}

.first-card {
    border-radius: 10px 10px 10px 10px;
    position: relative;
    padding-bottom: 10px;
    /*text-align: center;*/
    font: 300 1em/1.8 PingFang SC, Lantinghei SC, Microsoft Yahei, Hiragino Sans GB, Microsoft Sans Serif, WenQuanYi Micro Hei, sans-serif;

}

hr.style-one {
    width: 100%;
    background-image: linear-gradient(to right, rgba(64, 158, 255, 0), rgba(64, 158, 255, 0.75), rgba(64, 158, 255, 0));
}

.appreciate {
    text-align: center;
}

.tags {
    display: flex;
    align-items: center;
    margin-left: 50px;
    margin-top: 20px;
}

.tag-item {
    display: flex;
    justify-content: space-around;
    align-items: center;
    margin-left: 10px;
    margin-bottom: 20px;
}

.tag {
    padding-left: 10px;
    padding-right: 10px;
    border-radius: 5px;
    background-color: #ecf5ff;
    border: 1px solid #409eff;
    color: #409eff;
    display: flex;
}

.sjx-outer {
    width: 0;
    height: 0;
    border-top: 7px solid transparent;
    border-bottom: 7px solid transparent;
    border-right: 7px solid #409eff;
    position: relative;
}

.sjx-inner {
    border-top: 7px solid transparent;
    border-bottom: 7px solid transparent;
    border-right: 7px solid #ecf5ff;
    top: -7px;
    left: 1px;
    position: absolute;
}

.author {
    text-align: left;
    background-color: #fcfff5;
    box-shadow: 0 0 0 1px #a3c293 inset;
    color: #2c662d;
    width: 100%;
    position: absolute;
    left: 0;
    margin: 20px 0;
    padding: 20px 0;
    font-size: small;
    font-family: PingFang SC, Lantinghei SC, Microsoft Yahei, Hiragino Sans GB, Microsoft Sans Serif, WenQuanYi Micro Hei, sans-serif;
}

.comments {
    margin-top: 150px;
    box-shadow: 0 1px 2px 0 rgb(34 36 38 / 15%);
    border: 1px solid rgba(34, 36, 38, .15);
    border-top: 2px solid #409EFF;
    text-align: left;
}

.blog-title {
    text-align: center;
}

.blog-info {
    display: flex;
    align-items: center;
    color: rgba(0, 0, 0, .4);
    font-size: 13px;
}

.blog-date {
    margin-right: 5px;
    float: right;
}

.blog-views {
    margin-right: 5px;
    float: right;
}

.user-info {
    justify-content: space-around;
    align-items: center;
    margin-right: 15px;
    float: left;
}

.header {
    text-decoration: none;
    color: #3a8ee6;
    font-weight: bold;
}

@media screen and (max-width: 768px) {
    .tags {
        margin-left: 0;
        margin-top: 20px;
    }

    hr {
        display: none;
    }

    .comment-content {
        font-size: 12px !important;
    }
}

@media only screen and (max-width: 480px) {
    h2 {
        font-weight: normal;
    }

    code,
    pre {
        font-size: 13px !important;
    }
}

.blogFilesInfoName {
    text-align: center;
    padding-top: 5px;
}

.p {
    max-width: 100%;
}

.sidebar {
    width: 80%;
    border: 1px solid #ddd;
    font-family: '微软雅黑', sans-serif;
    background-color: #fff;
}

.first-menu {
    background-color: #0A2D6B;
    padding: 15px 20px;
}

.first-menu a {
    color: #fff;
    font-size: 16px;
    text-decoration: none;
}

.second-menu {
    list-style: none;
    margin: 0;
    padding: 0;
    background-color: #fff;
    border-top: 1px solid #ddd;
}

.second-menu li {
    border-bottom: 1px solid #eee;
}

.second-menu-link {
    display: block;
    padding: 10px 20px;
    text-decoration: none;
    color: #333;
    font-size: 14px;
    transition: background-color 0.3s;
}

.second-menu-link:hover {
    background-color: #f5f5f5;
}

.active-menu {
    color: #003F87;
    font-weight: bold;
}
</style>
