<template>
  <div class="banner-wrapper">
    <el-carousel
      :interval="8000"
      arrow="never"
      trigger="click"
      class="carousel"
      height="48vh"
      :loop="true"
    >
      <el-carousel-item v-for="(imagePath, index) in images" :key="index">
        <img :src="imagePath" :alt="'banner' + (index + 1)">
      </el-carousel-item>
    </el-carousel>
  </div>
</template>

<script>
export default {
  name: 'Banner',
  data() {
    return {
      images: [
        require('@/assets/banner/banner1.jpg'),
        require('@/assets/banner/banner2.jpg')
      ]
    };
  }
};
</script>

<style scoped>
.banner-wrapper {
  display: block;
  height: 48vh; /* 设置轮播图区域的高度 */
  max-width: 100%;
  padding: 0 10%;  /* 给轮播图区域加上左右空白 */
  justify-content: center; /* 水平居中 */
  align-items: center; /* 垂直居中 */
  overflow: auto; /* 隐藏溢出的内容 */
}

.carousel {
  position: relative;
  width: 100%;
  height: auto !important; /* 确保轮播容器占满父容器的高度 */
  object-fit: none;
}

.carousel img {
  width: 100%;
  height: 100% !important;
  object-fit: cover; /* 保持图片比例，裁剪多余部分 */
  display: block;
}

.el-carousel-item {
  background-size: cover;
  background-position: center;
  height: auto !important;
}

/* 使指示器为小圆点 */
.el-carousel__indicator {
  bottom: 20px; /* 设置指示器的位置 */
}


</style>
