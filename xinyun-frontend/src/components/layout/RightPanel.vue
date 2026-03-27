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
/* 【布局优化】右侧边栏容器 */
.right-panel {
  width: 100%;
  padding: 20px 16px;
  box-sizing: border-box;
}

/* 【布局优化】广告横幅 */
.ad-banner {
  border-radius: 16px;
  overflow: hidden;
  margin-bottom: 24px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.06);
}

.ad-banner img {
  width: 100%;
  display: block;
  border-radius: 16px;
}

/* 【布局优化】模块卡片 - 统一间距 */
.panel-section {
  background: #ffffff;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.06);
  border-radius: 16px;
  padding: 20px;
  margin-bottom: 24px;
}

.panel-section:last-child {
  margin-bottom: 0;
}

.section-header {
  margin-bottom: 16px;
}

/* 【布局优化】模块标题 */
.section-title {
  font-size: 15px;
  font-weight: 700;
  color: #1a1a1a;
  padding-left: 4px;
}

.hot-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

/* 【布局优化】热点列表项 */
.hot-item {
  display: flex;
  align-items: center;
  gap: 10px;
  cursor: pointer;
  transition: all 0.3s;
  padding: 12px 8px;
  border-radius: 8px;
}

.hot-item:hover {
  background: rgba(123, 97, 255, 0.06);
}

.hot-item:hover .hot-text {
  color: #7b61ff;
}

/* 【布局优化】热点排名 */
.hot-rank {
  width: 20px;
  font-size: 14px;
  font-weight: 700;
  color: #888888;
  flex-shrink: 0;
}

.hot-rank.rank-1 {
  color: #7b61ff;
}

.hot-rank.rank-2 {
  color: #9b8aff;
}

.hot-rank.rank-3 {
  color: #b8b0ff;
}

/* 【布局优化】热点文字 - 文字截断 */
.hot-text {
  flex: 1;
  font-size: 14px;
  color: #333333;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  transition: color 0.3s;
}

/* 【布局优化】热点标签 */
.hot-tag {
  font-size: 14px;
  color: #7b61ff;
  flex-shrink: 0;
}

/* 【布局优化】查看更多 */
.see-more {
  font-size: 13px;
  color: #7b61ff;
  text-align: right;
  margin-top: 16px;
  cursor: pointer;
  transition: color 0.3s;
}

.see-more:hover {
  color: #5b41ef;
}

.bar-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

/* 【布局优化】推荐吧列表项 */
.bar-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 12px 8px;
  border-radius: 8px;
  transition: all 0.3s;
}

.bar-item:hover {
  background: rgba(123, 97, 255, 0.06);
}

.bar-item:hover .bar-name {
  color: #7b61ff;
}

.bar-info {
  flex: 1;
  min-width: 0;
}

/* 【布局优化】吧名称 - 文字截断 */
.bar-name {
  font-size: 14px;
  color: #333333;
  display: block;
  transition: color 0.3s;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

/* 【布局优化】吧描述 - 文字截断 */
.bar-desc {
  font-size: 12px;
  color: #888888;
  margin-top: 4px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

/* 【布局优化】查看按钮 */
.bar-item :deep(.el-button--primary.is-plain) {
  border-color: rgba(123, 97, 255, 0.3);
  color: #7b61ff;
  border-radius: 24px;
  transition: all 0.3s;
  flex-shrink: 0;
}

.bar-item :deep(.el-button--primary.is-plain:hover) {
  background: linear-gradient(135deg, #7b61ff, #9b8aff);
  border-color: #7b61ff;
  color: #fff;
}
</style>
