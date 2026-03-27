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
/* 【布局优化】侧边栏容器 - 统一内边距 */
.sidebar {
  width: 100%;
  background: transparent;
  padding: 20px 24px;
  box-sizing: border-box;
}

/* 中屏状态（900px ≤ 宽度 < 1200px）：左右内边距一致 */
@media (max-width: 1199px) {
  .sidebar {
    padding: 20px 20px;
  }
}

/* 小屏/窄窗口状态（宽度 < 900px）：左右内边距一致 */
@media (max-width: 899px) {
  .sidebar {
    padding: 20px 16px;
  }
}

/* 【布局优化】首页按钮 - 饱满不拥挤 */
.nav-item {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 12px 16px;
  border-radius: 12px;
  cursor: pointer;
  color: #333333;
  font-size: 14px;
  margin-bottom: 24px;
  transition: all 0.3s;
}

.nav-item:hover {
  background: rgba(123, 97, 255, 0.08);
}

.nav-item.active {
  background: rgba(123, 97, 255, 0.08);
  color: #7b61ff;
  font-weight: 600;
}

.nav-icon {
  font-size: 18px;
}

/* 【布局优化】分割线 */
.divider {
  height: 1px;
  background: #e0e0e0;
  margin: 16px 0;
}

/* 【布局优化】分类标题 - 统一左对齐 */
.section-title {
  font-size: 12px;
  color: #888888;
  padding-left: 0;
  margin-bottom: 16px;
  font-weight: 600;
}

.bar-list {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

/* 【布局优化】列表项 - 统一间距对齐 */
.bar-item {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 10px 16px;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.3s;
}

.bar-item:hover {
  background: rgba(123, 97, 255, 0.08);
  transform: translateX(4px);
}

.bar-avatar {
  width: 32px;
  height: 32px;
  border-radius: 12px;
  object-fit: cover;
  flex-shrink: 0;
}

/* 【布局优化】吧名称 - 文字截断规范 */
.bar-name {
  font-size: 14px;
  color: #333333;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}
</style>
