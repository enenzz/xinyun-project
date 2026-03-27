<template>
  <div class="home">
    <!-- 墨水溅射背景效果 - 固定在页面最底层 -->
    <div class="bg-spots">
      <div class="spot spot-1"></div>
      <div class="spot spot-2"></div>
      <div class="spot spot-3"></div>
      <div class="spot spot-4"></div>
    </div>
    
    <!-- 顶部导航栏 - 通栏固定 -->
    <Header />
    
    <!-- 左侧边栏 - 25% 宽度，fixed 定位，内部独立滚动 -->
    <aside class="sidebar-fixed">
      <Sidebar />
    </aside>
    
    <!-- 中间主内容区 - 50% 宽度，页面唯一全局滚动主体 -->
    <main class="main-content">
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
    </main>

    <!-- 右侧边栏 - 25% 宽度，fixed 定位，内部独立滚动 -->
    <aside class="right-panel-fixed">
      <div class="right-panel-scroll">
        <RightPanel />
      </div>
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
/* ========== 基础容器 ========== */
.home {
  min-height: 100vh;
  position: relative;
  overflow-x: hidden;
  background: #ffffff;
}

/* ========== 墨水溅射背景效果 - 固定在页面最底层 ========== */
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

/* ========== 核心布局架构 - 1:2:1 比例 ========== */

/* 【左侧边栏】25% 视口宽度，fixed 定位，内部独立滚动 */
.sidebar-fixed {
  position: fixed;
  top: 64px;
  left: 0;
  width: 25vw;
  min-width: 240px;
  max-width: 480px;
  height: calc(100vh - 64px);
  overflow-y: auto;
  z-index: 99;
  background: #ffffff;
  box-shadow: 2px 0 12px rgba(0, 0, 0, 0.06);
  transition: width 0.2s ease-in-out, min-width 0.2s ease-in-out, max-width 0.2s ease-in-out;
  box-sizing: border-box;
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

/* 【右侧边栏】25% 视口宽度，fixed 定位，内部独立滚动 */
.right-panel-fixed {
  position: fixed;
  top: 64px;
  right: 0;
  width: 25vw;
  min-width: 280px;
  max-width: 480px;
  height: calc(100vh - 64px);
  overflow: hidden;
  z-index: 99;
  background: #ffffff;
  box-shadow: -2px 0 12px rgba(0, 0, 0, 0.06);
  box-sizing: border-box;
  transition: width 0.2s ease-in-out, min-width 0.2s ease-in-out, max-width 0.2s ease-in-out;
  pointer-events: auto;
}

/* 右侧边栏内层滚动容器 */
.right-panel-scroll {
  width: 100%;
  height: 100%;
  overflow-x: hidden;
  overflow-y: auto;
  box-sizing: border-box;
}

.right-panel-scroll::-webkit-scrollbar {
  width: 6px;
}

.right-panel-scroll::-webkit-scrollbar-track {
  background: transparent;
}

.right-panel-scroll::-webkit-scrollbar-thumb {
  background: rgba(0, 0, 0, 0.1);
  border-radius: 3px;
}

/* 【中间主内容区】50% 视口宽度，页面唯一全局滚动主体 */
.main-content {
  width: 50vw;
  min-width: 600px;
  max-width: 960px;
  margin: 64px auto 0;
  padding: 20px 24px 40px;
  box-sizing: border-box;
  position: relative;
  z-index: 10;
  transition: width 0.2s ease-in-out, min-width 0.2s ease-in-out, max-width 0.2s ease-in-out, margin 0.2s ease-in-out;
}

.post-list {
  min-height: 400px;
}

/* ========== 4 阶段响应式缩放逻辑 ========== */

/* 【第一阶段】超大屏状态（≥1920px）：三栏严格 25%/50%/25% 比例 */
@media (min-width: 1920px) {
  .sidebar-fixed {
    width: 480px;
    min-width: 480px;
    max-width: 480px;
  }
  
  .right-panel-fixed {
    width: 480px;
    min-width: 480px;
    max-width: 480px;
  }
  
  .main-content {
    width: 960px;
    min-width: 960px;
    max-width: 960px;
  }
}

/* 【第二阶段】大屏状态（1280px ≤ 宽度 < 1920px）：1:2:1 比例，无重叠 */
@media (min-width: 1280px) and (max-width: 1919px) {
  .sidebar-fixed {
    width: 25vw;
    min-width: 320px;
    max-width: 480px;
  }
  
  .right-panel-fixed {
    width: 25vw;
    min-width: 320px;
    max-width: 480px;
  }
  
  .main-content {
    width: 50vw;
    min-width: 640px;
    max-width: 960px;
  }
}

/* 【第三阶段】中屏状态（900px ≤ 宽度 < 1280px）：隐藏右侧边栏 */
@media (max-width: 1279px) {
  .right-panel-fixed {
    display: none;
  }
  
  .sidebar-fixed {
    width: 240px;
    min-width: 240px;
  }
  
  .main-content {
    width: calc(100% - 240px - 24px);
    margin-left: calc(240px + 24px);
    margin-right: 24px;
    min-width: auto;
    max-width: none;
  }
}

/* 【第四阶段】小屏/窄窗口状态（宽度 < 900px）：隐藏左侧边栏 */
@media (max-width: 899px) {
  .sidebar-fixed {
    display: none;
  }
  
  .main-content {
    width: 100%;
    max-width: calc(100% - 48px);
    margin: 64px 24px 0;
    min-width: auto;
  }
}
</style>
