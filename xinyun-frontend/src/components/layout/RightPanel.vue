<template>
  <aside class="right-panel">
    <div class="panel-content">
      <div class="panel-section">
        <h3 class="section-title">推荐用户</h3>
        <div class="user-list">
          <div 
            class="user-item" 
            v-for="user in recommendUsers" 
            :key="user.id"
          >
            <img :src="user.avatar" class="user-avatar" :alt="user.nickname">
            <div class="user-info">
              <div class="user-name">{{ user.nickname }}</div>
              <div class="user-location">{{ user.city }}</div>
            </div>
            <el-button type="primary" size="small" plain>关注</el-button>
          </div>
        </div>
      </div>
    </div>
  </aside>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getRecommendUsers } from '@/api/user'

const recommendUsers = ref([])

onMounted(async () => {
  try {
    const res = await getRecommendUsers()
    recommendUsers.value = res.data
  } catch (error) {
    console.error('获取推荐用户失败:', error)
  }
})
</script>

<style scoped>
.right-panel {
  width: 280px;
  position: sticky;
  top: 80px;
  align-self: flex-start;
}

.panel-content {
  background: white;
  border-radius: 12px;
  padding: 20px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
}

.section-title {
  font-size: 16px;
  font-weight: 600;
  color: #333;
  margin-bottom: 16px;
}

.user-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.user-item {
  display: flex;
  align-items: center;
  gap: 12px;
}

.user-avatar {
  width: 48px;
  height: 48px;
  border-radius: 50%;
  object-fit: cover;
}

.user-info {
  flex: 1;
}

.user-name {
  font-size: 14px;
  font-weight: 500;
  color: #333;
}

.user-location {
  font-size: 12px;
  color: #999;
  margin-top: 2px;
}
</style>
