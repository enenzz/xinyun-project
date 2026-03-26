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
  position: sticky;
  top: 80px;
  align-self: flex-start;
}

.ad-banner {
  border-radius: 12px;
  overflow: hidden;
  margin-bottom: 20px;
}

.ad-banner img {
  width: 100%;
  display: block;
}

.panel-section {
  background: #fff;
  border-radius: 12px;
  padding: 20px;
  margin-bottom: 20px;
}

.section-header {
  margin-bottom: 16px;
}

.section-title {
  font-size: 15px;
  font-weight: 600;
  color: #333;
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
}

.hot-rank {
  width: 20px;
  font-size: 14px;
  font-weight: 600;
  color: #999;
}

.hot-rank.rank-1 {
  color: #ff6b00;
}

.hot-rank.rank-2 {
  color: #ff8a00;
}

.hot-rank.rank-3 {
  color: #ffaa00;
}

.hot-text {
  flex: 1;
  font-size: 14px;
  color: #333;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.hot-text:hover {
  color: #2385bb;
}

.hot-tag {
  font-size: 14px;
  color: #ff6b00;
  flex-shrink: 0;
}

.see-more {
  font-size: 13px;
  color: #999;
  text-align: right;
  margin-top: 16px;
  cursor: pointer;
}

.see-more:hover {
  color: #2385bb;
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
}

.bar-info {
  flex: 1;
  min-width: 0;
}

.bar-name {
  font-size: 14px;
  color: #333;
  display: block;
}

.bar-desc {
  font-size: 12px;
  color: #999;
  margin-top: 4px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
</style>
