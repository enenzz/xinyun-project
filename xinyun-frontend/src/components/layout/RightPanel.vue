<template>
  <aside class="right-panel">
    <div class="ad-banner">
      <img src="https://picsum.photos/300/200?random=100" alt="广告">
    </div>

    <div class="panel-section">
      <div class="section-header">
        <h3 class="section-title">爆料##热点</h3>
      </div>
      <div class="hot-list">
        <div class="hot-item" v-for="(item, index) in hotTopics" :key="item.id">
          <span class="hot-rank" :class="'rank-' + (index + 1)">{{ index + 1 }}</span>
          <span class="hot-text">{{ item.title }}</span>
          <el-icon class="hot-tag" v-if="item.tag === 'hot'"><TrendCharts /></el-icon>
          <el-icon class="hot-tag" v-if="item.tag === 'new'"><Clock /></el-icon>
        </div>
      </div>
      <div class="see-more">查看更多 ></div>
    </div>

    <div class="panel-section">
      <div class="section-header">
        <h3 class="section-title">最有料吧</h3>
      </div>
      <div class="bar-list">
        <div class="bar-item" v-for="bar in recommendBars" :key="bar.id">
          <div class="bar-info">
            <span class="bar-name">{{ bar.name }}</span>
            <span class="bar-desc">{{ bar.desc }}</span>
          </div>
          <el-button type="primary" size="small" plain>查看</el-button>
        </div>
      </div>
    </div>
  </aside>
</template>

<script setup>
import { ref } from 'vue'
import { TrendCharts, Clock } from '@element-plus/icons-vue'

const hotTopics = ref([
  { id: 1, title: '保姆逝世巨婴把馆长推到公开', tag: 'hot' },
  { id: 2, title: '电竞赛事AI+GPU大作战', tag: 'hot' },
  { id: 3, title: '新规解除禁酒派 始料集体抗议', tag: 'new' },
  { id: 4, title: '高圆圆搜救队支援陕西洛南', tag: '' },
  { id: 5, title: '明星本色沦 强盗透查责任吗', tag: '' }
])

const recommendBars = ref([
  { id: 1, name: '杨超越吧', desc: '田曦薇逆袭一战成名 杨超越哭迎', members: '209w', posts: '618w' },
  { id: 2, name: '孙吧吧', desc: '00后改造9次，张新成救', members: '279w', posts: '1081w' },
  { id: 3, name: '今天吃什么', desc: '', members: '', posts: '' },
  { id: 4, name: '和田地吧', desc: '', members: '', posts: '' },
  { id: 5, name: '5岁吧', desc: '', members: '', posts: '' },
  { id: 6, name: '成名吧', desc: '', members: '', posts: '' }
])
</script>

<style scoped>
.right-panel {
  width: 100%;
}

.ad-banner {
  border-radius: 16px;
  overflow: hidden;
  margin-bottom: 20px;
  box-shadow: 0 4px 20px rgba(147, 129, 255, 0.15);
}

.ad-banner img {
  width: 100%;
  display: block;
  border-radius: 16px;
}

.panel-section {
  background: rgba(255, 255, 255, 0.75);
  backdrop-filter: blur(12px);
  border-radius: 16px;
  padding: 20px;
  margin-bottom: 20px;
  box-shadow: 0 4px 20px rgba(147, 129, 255, 0.15);
}

.section-header {
  margin-bottom: 16px;
}

.section-title {
  font-size: 15px;
  font-weight: 700;
  color: #6366f1;
}

.hot-list {
  display: flex;
  flex-direction: column;
  gap: 14px;
}

.hot-item {
  display: flex;
  align-items: center;
  gap: 10px;
  cursor: pointer;
  transition: all 0.3s;
  padding: 4px 0;
}

.hot-item:hover .hot-text {
  color: #6366f1;
}

.hot-rank {
  width: 20px;
  font-size: 14px;
  font-weight: 700;
  color: #9ca3af;
}

.hot-rank.rank-1 {
  color: #6366f1;
}

.hot-rank.rank-2 {
  color: #8b5cf6;
}

.hot-rank.rank-3 {
  color: #a78bfa;
}

.hot-text {
  flex: 1;
  font-size: 14px;
  color: #374151;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  transition: color 0.3s;
}

.hot-tag {
  font-size: 14px;
  color: #6366f1;
  flex-shrink: 0;
}

.see-more {
  font-size: 13px;
  color: #9381ff;
  text-align: right;
  margin-top: 16px;
  cursor: pointer;
  transition: color 0.3s;
}

.see-more:hover {
  color: #6366f1;
}

.bar-list {
  display: flex;
  flex-direction: column;
  gap: 14px;
}

.bar-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 8px 0;
  transition: all 0.3s;
}

.bar-item:hover .bar-name {
  color: #6366f1;
}

.bar-info {
  flex: 1;
  min-width: 0;
}

.bar-name {
  font-size: 14px;
  color: #374151;
  display: block;
  transition: color 0.3s;
}

.bar-desc {
  font-size: 12px;
  color: #9ca3af;
  margin-top: 4px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.bar-item :deep(.el-button--primary.is-plain) {
  border-color: #9381ff;
  color: #9381ff;
  border-radius: 12px;
  transition: all 0.3s;
}

.bar-item :deep(.el-button--primary.is-plain:hover) {
  background: linear-gradient(135deg, #6366f1, #8b5cf6);
  border-color: #6366f1;
  color: #fff;
}
</style>
