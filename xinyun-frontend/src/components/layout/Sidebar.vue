<template>
  <aside class="sidebar">
    <div class="sidebar-content">
      <div class="sidebar-section">
        <h3 class="section-title">热门话题</h3>
        <div class="topic-list">
          <div class="topic-item" v-for="topic in topics" :key="topic.id">
            <span class="topic-tag">#</span>
            <span class="topic-name">{{ topic.name }}</span>
          </div>
        </div>
      </div>
    </div>
  </aside>
</template>

<script setup>
import { ref } from 'vue'

const topics = ref([
  { id: 1, name: '周末去哪玩' },
  { id: 2, name: '美食分享' },
  { id: 3, name: '日常打卡' },
  { id: 4, name: '交友脱单' }
])
</script>

<style scoped>
.sidebar {
  width: 280px;
  position: sticky;
  top: 80px;
  align-self: flex-start;
}

.sidebar-content {
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

.topic-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.topic-item {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 8px 12px;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.3s;
}

.topic-item:hover {
  background: #f0f2ff;
}

.topic-tag {
  color: #6366f1;
  font-weight: 600;
}

.topic-name {
  color: #555;
}
</style>
