<template>
  <div class="home">
    <Header />
    <div class="home-wrapper">
      <div class="home-content">
        <Sidebar class="sidebar-wrapper" />
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
        <RightPanel class="right-panel-wrapper" />
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
  background: #f5f6f7;
}

.home-wrapper {
  width: 100%;
  min-width: 1000px;
  padding: 0 8px 0 8px;
}

.home-content {
  display: flex;
  justify-content: space-between;
  max-width: 1600px;
  margin: 0 auto;
  padding-top: 20px;
}

.sidebar-wrapper {
  width: 240px;
  flex-shrink: 0;
}

.main-area {
  width: 800px;
  flex-shrink: 0;
  margin: 0 32px;
}

.right-panel-wrapper {
  width: 300px;
  flex-shrink: 0;
}

.post-list {
  min-height: 400px;
}

@media (max-width: 1200px) {
  .right-panel-wrapper {
    display: none;
  }
  .main-area {
    margin-right: 0;
  }
}

@media (max-width: 900px) {
  .sidebar-wrapper {
    display: none;
  }
  .main-area {
    margin-left: 0;
  }
}
</style>
