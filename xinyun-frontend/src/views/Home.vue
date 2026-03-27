<template>
  <div class="home">
    <!-- 顶部导航栏 - 通栏固定 -->
    <Header />
    
    <!-- 四模块布局容器 -->
    <div class="layout-container">
      <!-- 模块1：左侧边栏 - 导航与发现 -->
      <aside class="sidebar-fixed">
        <Sidebar />
      </aside>
      
      <!-- 模块2：中间左 - 用户信息卡片（fixed定位，独立滚动） -->
      <div class="user-info-area">
        <div class="user-info-scroll">
          <UserInfoCard />
        </div>
      </div>
      
      <!-- 右侧内容容器 - 包含中间右和右侧栏，占65% -->
      <div class="right-content-wrapper">
        <!-- 模块3：中间右 - 帖子流（全局滚动） -->
        <div class="post-area">
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
        
        <!-- 模块4：右侧边栏 - 推荐与互动 -->
        <aside class="right-panel">
          <RightPanel />
        </aside>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import Header from '@/components/layout/Header.vue'
import Sidebar from '@/components/layout/Sidebar.vue'
import RightPanel from '@/components/layout/RightPanel.vue'
import PostCard from '@/components/PostCard.vue'
import UserInfoCard from '@/components/UserInfoCard.vue'
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
  height: 100vh;
  position: relative;
  overflow: hidden;
  background: #F7F9FC;
}

/* ========== 四模块布局容器 ========== */
.layout-container {
  display: flex;
  padding-top: 64px;
  position: relative;
  z-index: 1;
  height: 100%;
}

/* 【模块1】左侧边栏 - 15% 视口宽度 */
.sidebar-fixed {
  width: 15vw;
  min-width: 200px;
  max-width: 280px;
  height: calc(100vh - 64px);
  overflow-y: auto;
  z-index: 99;
  background: #E6F9E6;
  border-right: 1px solid #E8ECF4;
  box-sizing: border-box;
  flex-shrink: 0;
  transition: width 0.2s ease-in-out, min-width 0.2s ease-in-out, max-width 0.2s ease-in-out;
}

.sidebar-fixed::-webkit-scrollbar {
  width: 6px;
}

.sidebar-fixed::-webkit-scrollbar-track {
  background: transparent;
}

.sidebar-fixed::-webkit-scrollbar-thumb {
  background: rgba(0, 0, 0, 0.08);
  border-radius: 3px;
}

/* 【模块2】中间左 - 用户信息卡片区域 */
.user-info-area {
  width: 20vw;
  min-width: 240px;
  max-width: 320px;
  height: calc(100vh - 64px);
  z-index: 98;
  background: #FAFAF5;
  flex-shrink: 0;
}

.user-info-scroll {
  width: 100%;
  height: 100%;
  padding: 8px 12px;
  overflow-y: auto;
  box-sizing: border-box;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.user-info-scroll > * {
  max-width: 100%;
  box-sizing: border-box;
}

/* 隐藏滚动条 */
.user-info-scroll {
  -ms-overflow-style: none;
  scrollbar-width: none;
}

.user-info-scroll::-webkit-scrollbar {
  display: none;
}

/* 【右侧内容容器】包含中间右和右侧栏，占65% */
.right-content-wrapper {
  flex: 1;
  display: flex;
  background: linear-gradient(to right, #F0FBEF, #FFFFFF);
  height: calc(100vh - 64px);
  overflow: hidden;
}

/* 【模块3】中间右 - 帖子流区域（独立滚动） */
.post-area {
  width: 60%;
  min-width: 400px;
  padding: 24px 16px 40px;
  box-sizing: border-box;
  flex-shrink: 0;
  height: 100%;
  overflow-y: auto;
}

.post-area::-webkit-scrollbar {
  width: 6px;
}

.post-area::-webkit-scrollbar-track {
  background: transparent;
}

.post-area::-webkit-scrollbar-thumb {
  background: rgba(0, 0, 0, 0.08);
  border-radius: 3px;
}

.post-list {
  min-height: 400px;
}

/* 【模块4】右侧边栏（固定不动） */
.right-panel {
  width: 40%;
  min-width: 280px;
  height: 100%;
  overflow: hidden;
  box-sizing: border-box;
  padding: 16px;
  flex-shrink: 0;
}

/* ========== 响应式逻辑 ========== */

/* 中屏状态（1000px ≤ 宽度 < 1400px）：隐藏右侧边栏 */
@media (max-width: 1399px) {
  .right-panel {
    display: none;
  }
  
  .post-area {
    width: 100%;
    min-width: 400px;
    max-width: none;
  }
}

/* 小屏状态（800px ≤ 宽度 < 1000px）：隐藏右侧边栏 */
@media (max-width: 999px) {
  .right-panel {
    display: none;
  }
  
  .sidebar-fixed {
    width: 20vw;
    min-width: 180px;
    max-width: 260px;
  }
  
  .user-info-area {
    width: 25vw;
    min-width: 220px;
    max-width: 300px;
  }
  
  .post-area {
    width: 100%;
    min-width: 340px;
  }
}

/* 超小屏/窄窗口状态（宽度 < 800px）：隐藏左侧边栏和用户信息，只保留帖子流 */
@media (max-width: 799px) {
  .sidebar-fixed {
    display: none;
  }
  
  .user-info-area {
    display: none;
  }
  
  .post-area {
    width: 100%;
    min-width: auto;
    max-width: none;
    padding: 24px 20px 40px;
  }
  
  .layout-container {
    flex-direction: column;
  }
}
</style>
