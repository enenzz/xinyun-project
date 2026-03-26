<template>
  <aside class="sidebar">
    <div class="nav-item active">
      <el-icon class="nav-icon"><House /></el-icon>
      <span>首页</span>
    </div>

    <div class="divider"></div>

    <div class="section-title">大家都在逛的吧</div>

    <div class="bar-list">
      <div class="bar-item" v-for="bar in bars" :key="bar.id">
        <img :src="bar.avatar" class="bar-avatar" :alt="bar.name">
        <span class="bar-name">{{ bar.name }}</span>
      </div>
    </div>
  </aside>
</template>

<script setup>
import { ref } from 'vue'
import { House } from '@element-plus/icons-vue'

const bars = ref([
  { id: 1, name: '王者荣耀吧', avatar: 'https://i.pravatar.cc/40?img=11' },
  { id: 2, name: '孙笑川吧', avatar: 'https://i.pravatar.cc/40?img=12' },
  { id: 3, name: 'assu吧', avatar: 'https://i.pravatar.cc/40?img=13' },
  { id: 4, name: '三角洲行动吧', avatar: 'https://i.pravatar.cc/40?img=14' },
  { id: 5, name: '大学吧', avatar: 'https://i.pravatar.cc/40?img=15' },
  { id: 6, name: '888吧', avatar: 'https://i.pravatar.cc/40?img=16' },
  { id: 7, name: 'lol陪玩吧', avatar: 'https://i.pravatar.cc/40?img=17' },
  { id: 8, name: '英语四六级吧', avatar: 'https://i.pravatar.cc/40?img=18' },
  { id: 9, name: '土木工程吧', avatar: 'https://i.pravatar.cc/40?img=19' },
  { id: 10, name: '双一流吧', avatar: 'https://i.pravatar.cc/40?img=20' },
  { id: 11, name: '崩坏星穹铁道...吧', avatar: 'https://i.pravatar.cc/40?img=21' },
  { id: 12, name: 'bilibili吧', avatar: 'https://i.pravatar.cc/40?img=22' },
  { id: 13, name: '历史吧', avatar: 'https://i.pravatar.cc/40?img=23' }
])
</script>

<style scoped>
.sidebar {
  width: 100%;
  position: sticky;
  top: 80px;
  align-self: flex-start;
  height: calc(100vh - 80px);
  overflow-y: auto;
  padding-top: 10px;
  padding-bottom: 10px;
}

.sidebar::-webkit-scrollbar {
  width: 6px;
}

.sidebar::-webkit-scrollbar-track {
  background: transparent;
}

.sidebar::-webkit-scrollbar-thumb {
  background: #d1d5db;
  border-radius: 3px;
}

.sidebar::-webkit-scrollbar-thumb:hover {
  background: #9ca3af;
}

.nav-item {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 12px 16px;
  border-radius: 8px;
  cursor: pointer;
  color: #333;
  font-size: 14px;
  margin-bottom: 4px;
  transition: background 0.2s;
}

.nav-item:hover {
  background: #f5f6f7;
}

.nav-item.active {
  background: #e8f3f9;
  color: #2385bb;
  font-weight: 500;
}

.nav-icon {
  font-size: 18px;
}

.divider {
  height: 1px;
  background: #e5e6eb;
  margin: 16px 0;
}

.section-title {
  font-size: 12px;
  color: #999;
  padding: 0 16px;
  margin-bottom: 12px;
}

.bar-list {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.bar-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 12px 16px;
  border-radius: 8px;
  cursor: pointer;
  transition: background 0.2s;
}

.bar-item:hover {
  background: #f5f6f7;
}

.bar-avatar {
  width: 32px;
  height: 32px;
  border-radius: 8px;
  object-fit: cover;
}

.bar-name {
  font-size: 14px;
  color: #333;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
</style>
