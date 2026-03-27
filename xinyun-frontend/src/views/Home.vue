<template>
  <div class="home">
    <div class="bg-spots">
      <div class="spot spot-1"></div>
      <div class="spot spot-2"></div>
      <div class="spot spot-3"></div>
      <div class="spot spot-4"></div>
    </div>
    
    <Header />
    
    <!-- 【修复】左侧边栏fixed定位 -->
    <aside class="sidebar-fixed">
      <Sidebar />
    </aside>
    
    <div class="home-wrapper">
      <div class="main-area">
        <div v-loading="loading" class="post-list">
          <el-skeleton v-if="loading" :rows="5" animated />
          <PostCard 
            v-else 
            v-for="post in postList" 
            :key="post.id" 
            :post="post"
            @click="handlePostClick"
          />
          <el-empty v-if="!loading && postList.length === 0" description="暂无动态" />
        </div>
      </div>
    </div>
    
    <!-- 【修复】右侧边栏fixed定位 -->
    <aside class="right-panel-fixed">
      <RightPanel />
    </aside>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import Header from '@/components/layout/Header.vue'
import Sidebar from '@/components/layout/Sidebar.vue'
import RightPanel from '@/components/layout/RightPanel.vue'
import PostCard from '@/components/PostCard.vue'
import { getPostList } from '@/api/post'

const loading = ref(true)
const postList = ref([])

const loadPosts = async () => {
  try {
    loading.value = true
    const res = await getPostList({ page: 1, pageSize: 10 })
    postList.value = res.data.list || []
  } catch (error) {
    ElMessage.error('加载动态失败，请重试')
    console.error('加载动态失败:', error)
  } finally {
    loading.value = false
  }
}

const handlePostClick = (post) => {
  console.log('点击帖子:', post)
}

onMounted(() => {
  loadPosts()
})
</script>

<style scoped>
.home {
  min-height: 100vh;
  position: relative;
  overflow-x: hidden;
  background: #ffffff;
}

/* 【视觉优化】纯白背景 - 彻底删除粉紫渐变背景 */

/* 【视觉优化】边角颜料泼洒点缀效果 - 低饱和柔和色调，铺满整个窗口不随滚动 */
.bg-spots {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  z-index: 0;
  pointer-events: none;
  overflow: hidden;
}

.spot {
  position: absolute;
  border-radius: 50%;
  filter: blur(60px);
}

/* 左上角 - 淡粉 */
.spot-1 {
  width: 400px;
  height: 400px;
  top: -100px;
  left: -100px;
  background: rgba(255, 182, 193, 0.2);
}

/* 右下角 - 淡紫 */
.spot-2 {
  width: 500px;
  height: 500px;
  bottom: -150px;
  right: -150px;
  background: rgba(147, 112, 219, 0.15);
}

/* 左下角 - 浅黄 */
.spot-3 {
  width: 350px;
  height: 350px;
  bottom: -80px;
  left: 10%;
  background: rgba(255, 248, 220, 0.18);
}

/* 右上角 - 浅蓝 */
.spot-4 {
  width: 300px;
  height: 300px;
  top: 10%;
  right: -50px;
  background: rgba(173, 216, 230, 0.15);
}

/* 【布局调整】左侧边栏fixed定位 - 往左移100px */
.sidebar-fixed {
  position: fixed;
  top: 64px;
  left: calc((100vw - 600px) / 2 - 240px - 48px - 100px);
  width: 280px;
  height: calc(100vh - 64px);
  overflow-y: auto;
  z-index: 99;
}

.sidebar-fixed::-webkit-scrollbar {
  width: 6px;
}

.sidebar-fixed::-webkit-scrollbar-track {
  background: transparent;
}

.sidebar-fixed::-webkit-scrollbar-thumb {
  background: rgba(0, 0, 0, 0.1);
  border-radius: 3px;
}

/* 【布局调整】右侧边栏fixed定位 - 加大间距 */
.right-panel-fixed {
  position: fixed;
  top: 64px;
  right: calc((100vw - 650px) / 2 - 300px - 48px);
  width: 300px;
  height: calc(100vh - 64px);
  overflow-y: auto;
  z-index: 99;
}

.right-panel-fixed::-webkit-scrollbar {
  width: 6px;
}

.right-panel-fixed::-webkit-scrollbar-track {
  background: transparent;
}

.right-panel-fixed::-webkit-scrollbar-thumb {
  background: rgba(0, 0, 0, 0.1);
  border-radius: 3px;
}

/* 【布局调整】主容器 - 固定600px宽度 */
.home-wrapper {
  width: 1000px;
  margin: 0 auto;
  padding: 84px 0 40px;
  position: relative;
  z-index: 1;
  box-sizing: border-box;
}

/* 【布局调整】中间内容区 - 固定宽度600px */
.main-area {
  width: 650px;
  margin: 0 auto;
}

.post-list {
  min-height: 400px;
}

/* 【响应式逻辑保留】浏览器缩放时先隐藏右边栏 */
@media (max-width: 1300px) {
  .right-panel-fixed {
    display: none;
  }
}

/* 【响应式逻辑保留】再隐藏左边栏，最窄时保留中间卡片 */
@media (max-width: 1000px) {
  .sidebar-fixed {
    display: none;
  }
}

/* 最小屏幕宽度下，中间内容区自适应 */
@media (max-width: 750px) {
  .main-area {
    width: 100%;
    max-width: 100%;
  }
}
</style>
