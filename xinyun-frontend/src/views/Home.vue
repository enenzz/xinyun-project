<template>
  <div class="home">
    <Header />
    <div class="home-content">
      <Sidebar />
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
      <RightPanel />
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

.home-content {
  max-width: 1400px;
  margin: 0 auto;
  padding: 0 24px;
  display: flex;
  gap: 24px;
}

.main-area {
  flex: 1;
  min-width: 0;
  padding-top: 8px;
}

.post-list {
  min-height: 400px;
}
</style>
