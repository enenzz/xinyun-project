<template>
  <div class="home">
    <div class="bg-gradient"></div>
    
    <div class="bg-spots">
      <div class="spot spot-1"></div>
      <div class="spot spot-2"></div>
      <div class="spot spot-3"></div>
      <div class="spot spot-4"></div>
      <div class="spot spot-5"></div>
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
}

.bg-gradient {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: linear-gradient(135deg, #9381ff 0%, #f8b1d8 100%);
  z-index: -2;
}

.bg-spots {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  z-index: -1;
  pointer-events: none;
  overflow: hidden;
}

.spot {
  position: absolute;
  border-radius: 50%;
  background: radial-gradient(circle, rgba(255, 255, 255, 0.8) 0%, rgba(255, 255, 255, 0) 70%);
}

.spot-1 {
  width: 300px;
  height: 300px;
  top: 10%;
  left: 5%;
  opacity: 0.4;
}

.spot-2 {
  width: 200px;
  height: 200px;
  top: 60%;
  right: 10%;
  opacity: 0.3;
}

.spot-3 {
  width: 150px;
  height: 150px;
  bottom: 20%;
  left: 15%;
  opacity: 0.35;
}

.spot-4 {
  width: 250px;
  height: 250px;
  top: 30%;
  right: 25%;
  opacity: 0.25;
}

.spot-5 {
  width: 180px;
  height: 180px;
  bottom: 40%;
  right: 5%;
  opacity: 0.3;
}

/* 【修复】左侧边栏fixed定位 - 固定在视口左侧 */
.sidebar-fixed {
  position: fixed;
  top: 64px;
  left: calc((100vw - 700px) / 2 - 240px - 24px);
  width: 240px;
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
  background: rgba(147, 129, 255, 0.3);
  border-radius: 3px;
}

/* 【修复】右侧边栏fixed定位 - 固定在视口右侧 */
.right-panel-fixed {
  position: fixed;
  top: 64px;
  right: calc((100vw - 700px) / 2 - 300px - 24px);
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
  background: rgba(147, 129, 255, 0.3);
  border-radius: 3px;
}

/* 【修复】主容器 - 顶部留出导航栏空间 */
.home-wrapper {
  width: 100%;
  min-width: 700px;
  padding: 84px 50px 40px;
  position: relative;
  z-index: 1;
  box-sizing: border-box;
}

/* 【修复】中间内容区 - 固定宽度700px，精准居中 */
.main-area {
  width: 700px;
  max-width: 700px;
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
