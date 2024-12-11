<template>
    <el-main>
      <router-view :key="key" />
    </el-main>
</template>

<script>
  export default {
    name: 'cmsMain',
    computed: {
      key() {
        console.log(this.$route.path)
        return this.$route.path
      }
    }
  }
</script>

<style scoped>
  .el-main {
    min-height: 650px;
  }
</style>
