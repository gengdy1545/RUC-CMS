<template>
    <div class="footer_wrap">
      <div class="footer">
        <div class="f_left">
          <div class="contact">
            <img src="@/assets/logo/logo.png" class="f_logo"  width="100">
            <div  class="link-style" @click="getBlogInfo(47)">联系我们</div>
            <p>中国人民大学数据库虚拟教研室&nbsp;&nbsp;&nbsp;&nbsp;所在地址：中国人民大学理工配楼</p>
            <p>
              版权所有：中国人民大学数据库课程虚拟教研室 电话：029-8888888&nbsp;&nbsp;&nbsp;&nbsp;邮箱：8888888@163.com
            </p>
          </div>
        </div>
        <div class="f_right">
          <p>
            <a href="" class="footer-link">中国人民大学数据库虚拟教研室</a>
          </p>
        </div>
      </div>
    </div>
  </template>
  
  
  <script>
  export default {
    name: 'cmsFooter',
    methods: {
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
      },
    }
  }
  </script>
  
  <style scoped>
    /* 整体背景样式 */
  .footer_wrap {
    width: 100%;
    background: linear-gradient(90deg, #25282c, #333842); /* 渐变背景 */
    color: #b6b6b6;
    font-family: 'Arial', sans-serif;
  }
  
  /* 页脚主容器 */
  .footer {
    display: flex;
    justify-content: space-between;
    align-items: center;
    max-width: 1300px;
    height: 154px;
    margin: 0 auto;
    font-size: 16px;
  }
  
  /* 左侧内容 */
  .f_left {
    display: flex;
    align-items: flex-start;
  }
  
  .f_logo {
    float: left;
    margin-right: 20px;
    width: 100px;
    height: auto;
    border-right: 1px solid #3a4151;
    padding-right: 20px;
  }
    .link-style {
      font-size: 12px !important;
      color: GREY; /* 蓝色，类似于链接的颜色 */
      text-decoration: none; /* 默认无下划线 */
      cursor: pointer; /* 鼠标指针变为手指形状 */
    }
  
    .link-style:hover {
      text-decoration: underline; /* 鼠标悬停时添加下划线 */
    }
  .contact {
    line-height: 1.8;
    color: #e1e1e1;
  }
  .contact p {
    margin: 5px 0;
    white-space: nowrap;
  }
  
  .contact a {
    color: #ffda6d; /* 黄色链接 */
    text-decoration: none;
    transition: color 0.3s ease;
  }
  
  .contact a:hover {
    color: #ffd700; /* 更亮的黄色 */
  }
  
  /* 右侧内容 */
  .f_right {
    text-align: right;
  }
  
  .footer-link {
    text-decoration: underline;
  }
  
  
  
  /* 响应式设计 */
  @media (max-width: 768px) {
    .footer {
      flex-direction: column;
      text-align: center;
    }
  
    .f_left {
      margin-bottom: 20px;
      flex-direction: column;
      align-items: center;
    }
  
  
    .contact {
      text-align: center;
    }
  
    .f_right {
      text-align: center;
    }
  }
  </style>