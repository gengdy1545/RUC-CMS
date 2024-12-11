<template xmlns="http://www.w3.org/1999/html">
    <el-row :gutter="20">
        <el-col :sm="2" class="hidden-xs-only" style="opacity:0;">左侧占位</el-col>
        <div>
            <el-dialog :visible.sync="dialogVisible" title="公告内容" width="50%">
                <span>{{ noticeContent }}</span>
            </el-dialog>
        </div>
        <el-col :xs="24" :sm="15" style="width: 64vw">
            <div class="tabs-container">
                <el-tabs class="left-item left-tabs" @tab-click="handleTabClick" :sm="15" v-model="activeTab">
                    <el-tab-pane v-for="cmsType in typeList" :key="cmsType.typeId" :name="cmsType.typeId.toString()"
                        :label="cmsType.typeName" @click="selectType(cmsType)" :class="cmsType.typeId === typeId ?
                            'activeType' : ''">
                    </el-tab-pane>
                </el-tabs>
                <div style="margin-right: 15px" class="link-style" @click="selectMore()">
                    MORE+
                </div>
            </div>

            <div>
                <template>
                    <el-row :gutter="20" class="blog-gallery">
                        <el-col :xs="24" :sm="12" :md="8" :lg="6" v-for="blog in blogList" :key="blog.id"
                            class="blog-col">
                            <div @click="getBlogInfo(blog.id)" class="blog-content">
                                <el-card class="blog-card">
                                    <div class="blog-image-container">
                                        <el-image :src="blog.blogPic" class="blog-image"></el-image>
                                    </div>
                                    <div class="blog-content">
                                        <div class="card-title">
                                            {{ blog.title }}
                                            <svg-icon icon-class="Topping" style="z-index: 100"
                                                v-show="blog.top == 1" />
                                        </div>
                                        <div style="color: grey; font-size: 12px; margin-bottom: 5px;">
                                            {{ blog.updateTime ? blog.updateTime : blog.createTime }}
                                        </div>
                                    </div>
                                </el-card>
                            </div>
                        </el-col>
                    </el-row>
                </template>
            </div>

        </el-col>
        <el-col :xs="24" :sm="4">
            <el-card style="background-color: rgba(255,255,255,0.9)" class="right-item">
                <div slot="header" class="attributes" style="display: flex; justify-content: space-between;">
                    <b>最新通知</b>
                </div>
                <div class=" recommend-blog l-text" v-for="blog in recommendList" :key="blog.noticeId"
                    @click="showNotice(blog.noticeId)">
                    <el-row>
                        <div class="user-info">
                            <span class="blog-time">{{ blog.updateTime ? blog.updateTime : blog.createTime }}</span>
                        </div>
                    </el-row>
                    <el-row>
                        <a class="recommend-a">{{ blog.noticeTitle }}</a>
                    </el-row>
                </div>
            </el-card>
            <div class="contact_code">
                    <img src="@/assets/images/er.jpg">
                </div>
        </el-col>
        <el-col :sm="2" class="hidden-xs-only" style="opacity:0;">右侧占位</el-col>
    </el-row>

</template>

<script>
import 'element-ui/lib/theme-chalk/display.css';
import {
    Loading
} from 'element-ui';
import {
    cmsListBlog,
    getBlogDetail,
    cmsListByTypeId,
    cmsListByTagId,
    cmsListRecommend,
} from "@/api/cms/blog";
import {
    listNotice,
    getNotice
} from "@/api/system/notice";


export default {
    name: 'cmsIndex',
    data() {
        return {
            totalcount: 100,
            queryInfo: {
                query: '',
                pagenum: 1,
                pagesize: 8
            },
            intro: '',
            blogList: [],
            typeList: [],
            tagList: [],
            fullTypeList: [],
            fullTagList: [],
            recommendList: [],
            columns: [],
            activeTab: '13',
            typeId: 13,
            tagId: -1,
            selected: false,
            moreType: true,
            moreTag: true,
            value: new Date(),
            timer: null,
            start: false,
            dialogVisible: false,
            noticeContent: '',
            screenWidth: document.documentElement.clientWidth, //实时屏幕宽度
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
            },
            // 总条数
            total: 0,
        }
    },
    computed: {
        pagSmall() {
            return this.screenWidth <= 768;
        },
        // 计算分页栏样式
        pagLayout() {
            if (this.screenWidth < 768) {
                return 'prev, pager, next'
            } else {
                return 'total, prev, pager, next, jumper'
            }
        },
    },
    created() {
        this.$nextTick(function () {
            // 仅在整个视图都被渲染之后才会运行的代码
            this.getTypeList();
            this.selectType({typeId: '13', typeName: '新闻公告'})
            this.getRecommendList()
            let idx = 0;
            this.screenWidth = document.documentElement.clientWidth
        })
    }
    ,
    methods: {
        handleTabClick(tab) {
            const cmsType = this.typeList.find(type => type.typeId.toString() === tab.name);
            if (cmsType) {
                this.selectType(cmsType);
            }
        }
        ,
        showNotice(id) {
            getNotice(id).then(response => {
                this.noticeContent = response.data.noticeContent;
                this.dialogVisible = true;
            });
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
        // 开始进入主页
        startRead() {
            this.$nextTick(() => {
                document.getElementById('index').scrollIntoView({
                    behavior: 'smooth',
                    block: 'start',
                    // inline: 'nearest'
                });
            })
        }
        ,
        compare(property) {
            return function (a, b) {
                let value1 = a[property].length;
                let value2 = b[property].length;
                return value2 - value1;
            }
        }
        ,
        // 获取推荐文章列表
        async getRecommendList() {
            this.queryParams.status = 0;
            listNotice(this.queryParams).then(response => {
                const {
                    data: res
                } = response;
                this.recommendList = response.rows;
                this.total = response.total;
            });
        }
        ,
        // 获取文章类型列表
        async getTypeList() {
            getBlogDetail(this.$route.query.id).then(response => {
                for (let i = 0; i < response.types.length; i++) {
                    let typeInfo = response.types[i];
                    if (typeInfo.typePic.length > 0) {
                        response.types[i].typePic = process.env.VUE_APP_BASE_API + typeInfo.typePic
                    }
                }
                ;
                const {
                    data: res
                } = response;
                this.fullTypeList = response.types
                this.typeList = response.types;
            });
        },
        // 跳转到文章详情页
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
        selectMore(isNews) {
            var tpath;
            if (isNews) {
                tpath = '/cms/main/menu?id=2079';
            } else if (this.typeId === 13) {
                tpath = '/cms/main/menu?id=2079';
            } else if (this.typeId === 14) {
                tpath = '/cms/main/blog?id=27';
            } else if (this.typeId === 11) {
                return;
            }
            let routeUrl = this.$router.resolve({
                path: tpath,
            });
            window.open(routeUrl.href, '_blank');
        },
        // 按分类筛选文章
        async selectType(cmsType) {
            let loadingInstance = Loading.service({
                target: ".left-item"
            });
            this.typeId = cmsType.typeId
            cmsListByTypeId(this.typeId).then(response => {
                this.blogList = this.picSrc(response.rows);
                this.total = response.total;
                this.selected = true
            }).finally(() => {
                loadingInstance.close();
            });
        },
        // 屏幕尺寸变化的监听函数
        screenAdapter() {
            this.screenWidth = document.documentElement.clientWidth;
        }

    }
    ,
}
</script>

<style scoped>
.left-item .pagination-container {
    background: rgb(255, 255, 255, 0)
}

@keyframes clipMe {

    0%,
    100% {
        clip: rect(0px, 806px, 6px, 0px);
    }

    25% {
        clip: rect(0px, 6px, 112px, 0px);
    }

    50% {
        clip: rect(112px, 812px, 112px, 0px);
    }

    75% {
        clip: rect(0px, 812px, 112px, 806px);
    }
}

@keyframes bounce {

    0%,
    20%,
    50%,
    80%,
    100% {
        transform: translate(-50%, 0);
    }

    40% {
        transform: translate(-50%, -30px);
    }

    60% {
        transform: translate(-50%, -15px);
    }
}


.el-pagination {
    padding-bottom: 20px;
}


.el-card /deep/ .el-card__body {
    padding: 5px;
}

.right-item {
    height: 540px;
    background: #fff;
    border: 1px solid #ebebeb;
    box-shadow: 1px 1px 2px #ebebeb;
    margin-bottom: 20px;
    overflow: hidden;
    position: relative;
}


.activeType {
    background-color: rgba(58, 142, 230, 0.3);
    cursor: pointer;
    color: gray;
}


.recommend-blog {
    overflow: hidden;
    text-overflow: ellipsis;
    display: -webkit-box;
    -webkit-line-clamp: 1;
    -webkit-box-orient: vertical;
    font-size: 14px;
    padding-left: 10px;
    padding-right: 10px;
    margin-bottom: 5px;
    margin-top: 5px;
    border-radius: 5px;
}

.recommend-a {
    padding-bottom: 5px;
    margin-left: 5px;
    max-width: 100%;
    overflow: hidden;
    white-space: normal;
    line-height: 25px;
    display: block;
    text-decoration: none;
    color: black;
    border-bottom: 1px solid rgba(34, 36, 38, .15);
}

.recommend-a:hover {
    color: #3a8ee6;
}

.blog-time {
    font-size: smaller;
    margin-left: 5px;
    color: grey;
}

.total {
    display: flex;
    justify-content: space-between;
    align-items: center;
    font-size: larger;
    font-weight: bold;
}

.titleIndex {
    display: flex;
    align-items: center;
}

.el-icon-back {
    font-weight: bolder;
    color: #3a8ee6;
    margin-right: 10px;
}

.el-icon-back:hover {
    cursor: pointer;
}

.blog-content:hover {
    background-color: rgba(58, 142, 230, 0.3);
    cursor: pointer;
}

/*
.blog-content {
  text-align: left;
  transition: .3s;
}*/

.el-image {
    border-radius: 5px;
    box-sizing: border-box;
    flex-shrink: 0;
}


.user-info {
    display: flex;
    justify-content: space-around;
    align-items: center;
    margin-right: 15px;
    margin-top: 5px;
    float: left;
}

.header {
    text-decoration: none;
    color: #3a8ee6;
    font-weight: bold;
}

.blog-date {
    float: right;
    margin-right: 15px;
}

@media (max-width: 992px) {
    .blog-gallery {
        justify-content: center;
        /* 在小屏幕上居中对齐 */
        gap: 10px;
        /* 减小间距 */
    }

    .blog-card {
        width: 100%;
        /* 卡片全宽 */
        height: auto;
        /* 自动调整高度 */
    }
}

@media (max-width: 768px) {
    .blog-gallery {
        gap: 8px;
        /* 进一步减小间距 */
    }

    .blog-card {
        font-size: 14px;
        /* 缩小字体 */
    }

    .card-title {
        font-size: 14px;
        /* 缩小标题字体 */
    }
}

@media (max-width: 480px) {
    .blog-gallery {
        gap: 5px;
        /* 更小间距 */
    }

    .blog-card {
        font-size: 12px;
        /* 更小字体 */
    }
}

@media screen and (max-width: 768px) {
    .blog-date {
        display: none;
    }


    .border {
        display: none;
    }

    .tit {
        font-size: 2rem;
        width: 100%;
        line-height: 50px;
        letter-spacing: 2px;
        height: auto;
    }

    .intro {
        font-size: 1rem;
        line-height: 30px;
    }

    .el-pagination {
        width: 100%;
    }
}

.box-card {
    width: 100%;
    margin-bottom: 1px;
}

.blog-gallery {
    display: flex;
    flex-wrap: wrap;
}

.blog-col {
    display: flex;
    flex-direction: column;
    /* 确保内容垂直对齐 */
}

.blog-card {
    display: flex;
    flex-direction: column;
    height: auto;
    /* 设置统一的高度 */
    width: 100%;
    /*  margin: 0 0;*/
    overflow: hidden;
}

.blog-image-container {
    position: relative;
    width: 100%;
    padding-top: 56.25%;
    /* 16:9 横纵比 */
    overflow: hidden;
}

.blog-image {
    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    object-fit: cover;
    /* 保持图片覆盖整个容器 */
    padding: 5px;
}

.blog-content {
    padding: 0.2rem;
    flex-grow: 1;
    /*  height: calc(100% - 56.25%); !* 计算内容区域的高度 *!*/

}

.card-title {
    max-width: 100%;
    font-size: 14px;
    font-weight: bold;
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
}

.blog-desc {
    font-size: 12px;
    color: #666;
    margin-top: 0.5rem;
    display: -webkit-box;
    -webkit-line-clamp: 2;
    -webkit-box-orient: vertical;
    overflow: hidden;
    min-height: 2rem;
}

.link-style {
    font-size: 12px !important;
    color: GREY;
    /* 蓝色，类似于链接的颜色 */
    text-decoration: none;
    /* 默认无下划线 */
    cursor: pointer;
    /* 鼠标指针变为手指形状 */
}

.link-style:hover {
    text-decoration: underline;
    /* 鼠标悬停时添加下划线 */
}


.tabs-container {
    background: #fff;
    border: 1px solid #ebebeb;
    box-shadow: 1px 1px 2px #ebebeb;
    border-radius: 5px;
    width: 100%;
    /* 或者指定一个具体的宽度 */
    display: flex;
    /* 使用 Flexbox 布局 */
    align-items: center;
    /* 垂直居中对齐 */
    margin-bottom: 8px;
    font-size: 16px !important;
}

.left-tabs {
    padding-left: 1%;
    display: flex;
    flex-wrap: nowrap;
    /* 确保不换行 */
    background-color: white;
    width: 100%;
    /* 或者指定一个具体的宽度 */
    flex-grow: 1;
    /* 让 .left-item 占据剩余空间 */
    height: 38px;
}

/deep/ .el-tabs__header {
    margin: 0;
}

.contact_code {
    text-align: center;
    background: #fff;
    border-radius: 4px;
    border: 1px solid #ebebeb;
    box-shadow: 1px 1px 2px #ebebeb;
}

.contact_code img {
    width: 100%;
    height: auto;
    padding: 14px;
}
</style>
