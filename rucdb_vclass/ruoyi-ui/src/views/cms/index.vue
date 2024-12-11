<template>

  <el-container>
    <top></top>
    <cmsNavBar></cmsNavBar>

    <banner v-if="isHomePage"></banner>
    <keep-alive>
      <cmsMain ></cmsMain>
    </keep-alive>
    <el-footer>
      <cmsFooter></cmsFooter>
    </el-footer>
  </el-container>
</template>

<script>
  import cmsMain from './main.vue'
  import cmsNavBar from './NavBar.vue'
  import cmsFooter from './Footer.vue'
  import top from './top.vue'
  // 引入背景图
  import backgroundImages from './backgroundImages.js'
  import banner from "@/views/cms/banner.vue";

  export default {
    data() {
      return {
      };
    },
    methods: {
      handleSelect(key, keyPath) {
        console.log(key, keyPath);
      }
    },
    components: {
      top,
      cmsNavBar,
      banner,
      cmsMain,
      cmsFooter,
    },
    computed: {
      isHomePage() {
        return this.$route.path.includes('/cmsIndex');
      }
    }
  }
</script>

<style scoped>
  .el-footer {
    background: linear-gradient(90deg, #25282c, #333842); /* 渐变背景 */
    padding: 0;
  }

  .background {
    background-color: #FFFFFF;
    background-repeat: no-repeat;
    background-size: cover;
    margin: 0px;
    padding: 0px;
    top: 0;
    width: 100%;
    height: 120vh;
    position: absolute;
    z-index:-1;
  }
  @media screen and (max-width: 1400px) {
    .background {
      height: 60vh;
    }
    .el-footer {
      background: linear-gradient(90deg, #25282c, #333842); /* 渐变背景 */
      height: 20vw !important;
    }
  }
  @media screen and (max-width: 768px) {
    .title {
      width: 100%;
      background-position-x: center;
      background-position-y: 0;
    }
    .el-footer {
      background: linear-gradient(90deg, #25282c, #333842); /* 渐变背景 */
      height: 25vw !important;
    }
  }
</style>
