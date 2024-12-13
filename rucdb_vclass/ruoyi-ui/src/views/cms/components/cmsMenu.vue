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
                        <router-link :to="child.name"
                            :class="{ 'active-menu': currentSecondMenu && currentSecondMenu.name === child.name }"
                            class="second-menu-link">
                            {{ child.meta.title }}
                        </router-link>
                    </li>
                </div>
            </div>
        </el-col>
        <el-col :xs="24" :sm="12">
            <el-card style="background-color: rgba(255, 255, 255,1)" class="first-card">
                <el-card style="background-color: rgba(255,255,255,0.9)" class="left-item">
                    <el-row type="flex" align="middle" style="flex-wrap: wrap" :gutter="20" v-for="blog in blogList"
                        :key="blog.id" shadow="never" class="blog-content">
                        <div @click="getBlogInfo(blog.id)">
                            <el-col class="img" :xs="24" :sm="6">
                                <el-image v-if="blog.blogPicType == '0'" lazy :src="blog.blogPicLink">
                                    <div slot="error" class="image-slot">
                                        <el-image src="/errorImg.jpg" fit="cover" class="blogPic">></el-image>
                                    </div>
                                </el-image>
                                <el-image v-if="blog.blogPicType == '1'" lazy :src="blog.blogPic"></el-image>
                            </el-col>
                            <el-col :xs="24" :sm="18"
                                style="padding-left: 10px;padding-right: 10px;margin-bottom: 5px;margin-top: -5px;">
                                <div>
                                    <div style="font-size: 16px; margin-top: 5px; margin-bottom: 5px;">
                                        <svg-icon icon-class="Topping" v-show="blog.top == 1" />
                                        {{ blog.title }}
                                    </div>
                                    <div style="color: grey; font-size: 12px; margin-bottom: 5px;">
                                        {{ blog.updateTime ? blog.updateTime : blog.createTime }}
                                    </div>
                                    <div style="font-size: 14px; margin-bottom: 10px;">
                                        <span style="color: rgba(0, 0, 0, .4);"> {{ blog.blogDesc }}</span>
                                    </div>
                                </div>
                            </el-col>
                        </div>
                    </el-row>
                    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum"
                        :limit.sync="queryParams.pageSize" background layout="total, sizes, prev, pager, next, jumper"
                        @pagination="getBlogList" style="margin-bottom: 30px;float: right;margin-right: 10px;" />
                </el-card>
            </el-card>
        </el-col>
        <el-col :xs="24" :sm="0"></el-col>
        <el-col :sm="4" class="hidden-xs-only" style="opacity:0;">右侧占位</el-col>
    </el-row>
</template>

<script>
// import 'cherry-markdown/dist/cherry-markdown.min.css'
//使用prism.js代码高亮
import '@/views/cms/plugins/prism.js'
import '@/views/cms/plugins/prism.css'
import comment from "./comment/Ipcomment"
import { mapState } from 'vuex'
import CherryMarkdown from '@/components/CherryMarkdown'
import { cmsListByMenuId } from "@/api/cms/blog";
import { fetchColumns } from "@/api/cms/column";

export default {
    components: {
        comment,
        CherryMarkdown,
    },
    data() {
        return {
            total: 0,
            blog: {},
            blogList: [],
            commentForm: {
                content: ''
            },
            columns: [], //存储菜单数据
            currentFirstMenu: null, // 当前一级菜单
            currentSecondMenu: null, //当前二级菜单
            // 查询参数
            queryParams: {
                pageNum: 1,
                pageSize: 10,
                title: null,
                type: 1,
                content: null,
                top: null,
                views: null,
                status: null,
                createBy: null
            }
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
            this.getBlogList();
            // 设置侧边栏菜单
            this.setSidebarMenu();
        } catch (error) {
            console.error('Error fetching menu or blog data:', error);
        }
    },
    computed: {
        ...
        mapState([
            'userInfo',
            'administrator',
        ])
    }
    ,
    methods: {
        getBlogList() {
            cmsListByMenuId(this.$route.query.id).then(response => {
                this.blogList = this.picSrc(response.rows);
                this.total = response.total;
            });
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
        },
        //首图地址修改
        picSrc(blogList) {
            for (let i = 0; i < blogList.length; i++) {
                let blogInfo = blogList[i];
                if (blogInfo.blogPic.length > 0) {
                    blogList[i].blogPic = process.env.VUE_APP_BASE_API + blogInfo.blogPic
                } else {
                    blogList[i].blogPic = '/errorImg.jpg'
                }
            }
            ;
            return blogList
        }
        ,
        getBlogInfo(blogId) {
            let routeUrl = this.$router.resolve({
                path: '/cms/main/blog',
                query: {
                    id: blogId
                }
            });
            console.log(routeUrl)
            window.open(routeUrl.href, '_blank');
        }
        ,
    }
    ,

    setup() {
        const blogList = ref(null)
        return { blogList }
    }
}
</script>

<style scoped>
.total {
    display: flex;
    justify-content: space-between;
    align-items: center;
    font-size: larger;
    font-weight: bold;
}

.blog-content:hover {
    background-color: rgba(58, 142, 230, 0.3);
    cursor: pointer;
}

.blog-content {
    text-align: left;
    transition: .3s;
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
