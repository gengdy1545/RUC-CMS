<template>
    <div class="top_wrap">
        <!--top index-->
        <div class="top">
            <el-col :span="20" class="top-content">
                <span class="welcome">欢迎您访问中国人民大学数据库课程虚拟教研室网站！</span>
                <el-col :span="2" class="login-container">
                    <div v-if="islogin" class="bg-purple">
                        <el-dropdown class="avatar-container right-menu-item hover-effect" trigger="click">
                            <div class="avatar-wrapper">
                                <el-avatar class="user-avatar" :src="avatar" @error="errorHandler">
                                    <i class="el-icon-s-custom" />
                                </el-avatar>
                                <p class="avatar-Name">{{ name }}</p>
                            </div>
                            <el-dropdown-menu slot="dropdown">
                                <router-link target="_blank" to="/index">
                                    <el-dropdown-item>个人中心</el-dropdown-item>
                                </router-link>
                                <el-dropdown-item divided @click.native="logout">
                                    <span>退出登录</span>
                                </el-dropdown-item>
                            </el-dropdown-menu>
                        </el-dropdown>
                    </div>
                    <div v-else class="bg-purple">
                        <div class="avatar-wrapper">
                            <p class="avatar-Name" @click="tologin">登录</p>
                        </div>
                    </div>
                </el-col>
                <div class="clear"></div>
            </el-col>
        </div>
    </div>

</template>

<style scoped lang="scss">
.top_wrap {
    width: 100%;
    height: 48px;
    line-height: 48px;
    background: #0a2d6b;
    color: #ecf5f9;
}

* {
    margin: 0;
    padding: 0;
}

.top {
    max-width: 1300px;
    margin: 0 auto;
    display: flex;
    justify-content: space-between;
    align-items: center;
}

.top-content {
    display: flex;
    justify-content: flex-start;
    align-items: center;
    width: 100%;
}

.welcome {
    flex: 1;
}

.login-container {
    display: flex;
    justify-content: flex-end;
    align-items: center;
}

.bg-purple {
    display: flex;
    align-items: center;
}

.user-avatar {
    cursor: pointer;
    border: dashed;
}

.avatar-container {
    margin-left: 10px;
}

.avatar-wrapper {
    display: flex;
    justify-content: space-between;
    align-items: center;
}

.avatar-Name {
    cursor: pointer;
    font-size: 16px;
    display: inline-block;
    margin: 0 15px;
    color: #ecf5f9;
}

@media screen and (max-width: 768px) {
    .welcome {
        width: 100%;
    }
}
</style>
<script>
import { mapGetters } from "vuex";

import { getToken } from '@/utils/auth';

export default {
    data() {
        return {
            islogin: false,
        }
    },
    created() {
        this.login();
    },
    computed: {
        ...mapGetters(['avatar', 'name']),
    },

    methods: {
        tologin() {
            this.$router.push({ path: "/cmsLogin" });
        },
        login() {
            let token = getToken();
            this.islogin = token != null && token !== '';
        },
        async logout() {
            this.$confirm('确定注销并退出系统登录吗？', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                this.$store.dispatch('LogOut').then(() => {
                    location.href = '/cms/main/cmsIndex';
                });
            }).catch(() => {
            });
        },
        errorHandler() {
            return true;
        }
    }
}
</script>
