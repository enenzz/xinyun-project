<template>
  <div class="post-card">
    <div class="bar-info">
      <img :src="post.barAvatar" class="bar-avatar" :alt="post.barName">
      <div class="bar-detail">
        <div class="bar-name">{{ post.barName }}</div>
        <div class="bar-meta">
          <span>关注 {{ post.followers }}w</span>
          <span class="dot">·</span>
          <span>帖子 {{ post.posts }}w</span>
        </div>
      </div>
      <el-button type="primary" size="small" plain class="follow-btn">关注</el-button>
    </div>

    <div class="post-title" @click="handleClick">{{ post.title }}</div>

    <div class="post-media">
      <div v-if="post.video" class="video-preview">
        <img :src="post.thumbnail" class="video-thumb" alt="视频预览">
        <div class="video-overlay">
          <el-icon class="play-icon"><VideoPlay /></el-icon>
          <span class="video-duration">{{ post.duration }}</span>
        </div>
      </div>
      <div v-else-if="post.images?.length" class="post-images">
        <img 
          v-for="(img, index) in post.images" 
          :key="index"
          :src="img" 
          class="post-image"
          :class="{ 'single-image': post.images.length === 1 }"
          alt="帖子图片"
        >
      </div>
    </div>

    <div class="post-footer">
      <div class="footer-left">
        <div class="action-item">
          <el-icon><ChatDotRound /></el-icon>
          <span>{{ post.replyCount }}</span>
        </div>
        <div class="action-item">
          <el-icon><View /></el-icon>
          <span>{{ post.viewCount }}</span>
        </div>
        <div class="action-item">
          <el-icon><Share /></el-icon>
          <span>{{ post.shareCount }}</span>
        </div>
      </div>
      <div class="post-time">{{ post.createTime }}</div>
    </div>
  </div>
</template>

<script setup>
import { VideoPlay, ChatDotRound, View, Share } from '@element-plus/icons-vue'

const props = defineProps({
  post: {
    type: Object,
    required: true
  }
})

const emit = defineEmits(['click'])

const handleClick = () => {
  emit('click', props.post)
}
</script>

<style scoped>
/* 【视觉优化】帖子卡片 - 纯白背景+轻微柔化阴影 */
.post-card {
  background: #FAFAF5;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.06);
  border-radius: 16px;
  padding: 20px;
  margin-bottom: 24px;
  cursor: pointer;
  transition: all 0.3s;
}

.post-card:hover {
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
  transform: translateY(-2px);
}

.bar-info {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 16px;
}

/* 【视觉优化】吧头像 - 圆角12px */
.bar-avatar {
  width: 48px;
  height: 48px;
  border-radius: 12px;
  object-fit: cover;
}

.bar-detail {
  flex: 1;
}

/* 【视觉优化】吧名称 - 标题黑 */
.bar-name {
  font-size: 15px;
  font-weight: 600;
  color: #1a1a1a;
  margin-bottom: 4px;
}

/* 【视觉优化】吧元数据 - 辅助灰色 */
.bar-meta {
  font-size: 12px;
  color: #888888;
}

.bar-meta .dot {
  margin: 0 6px;
}

/* 【视觉优化】关注按钮 - 浅紫色描边 */
.follow-btn {
  border-radius: 24px;
  padding: 4px 12px;
  font-size: 12px;
  border-color: rgba(123, 97, 255, 0.3);
  color: #7b61ff;
  transition: all 0.3s;
}

.follow-btn:hover {
  background: linear-gradient(135deg, #7b61ff, #9b8aff) !important;
  border-color: #7b61ff !important;
  color: #fff !important;
}

/* 【视觉优化】帖子标题 - 标题黑 */
.post-title {
  font-size: 16px;
  font-weight: 600;
  color: #1a1a1a;
  line-height: 1.5;
  margin-bottom: 16px;
  transition: color 0.3s;
}

.post-title:hover {
  color: #7b61ff;
}

.post-media {
  margin-bottom: 16px;
}

/* 【视觉优化】视频预览 - 圆角12px */
.video-preview {
  position: relative;
  border-radius: 12px;
  overflow: hidden;
}

.video-thumb {
  width: 100%;
  height: 240px;
  object-fit: cover;
}

.video-overlay {
  position: absolute;
  bottom: 10px;
  right: 10px;
  display: flex;
  align-items: center;
  gap: 8px;
  background: rgba(0, 0, 0, 0.6);
  backdrop-filter: blur(8px);
  padding: 4px 10px;
  border-radius: 12px;
}

.play-icon {
  font-size: 18px;
  color: #fff;
}

.video-duration {
  font-size: 12px;
  color: #fff;
}

/* 【视觉优化】帖子图片 - 圆角12px */
.post-images {
  display: flex;
  gap: 8px;
  flex-wrap: wrap;
}

.post-image {
  width: 160px;
  height: 120px;
  object-fit: cover;
  border-radius: 12px;
}

.post-image.single-image {
  width: 100%;
  height: auto;
  max-height: 360px;
}

/* 【视觉优化】帖子底部 */
.post-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-top: 16px;
  border-top: 1px solid #f0f0f0;
}

.footer-left {
  display: flex;
  gap: 24px;
}

/* 【视觉优化】互动按钮 - 辅助灰色 */
.action-item {
  display: flex;
  align-items: center;
  gap: 6px;
  font-size: 13px;
  color: #888888;
  transition: all 0.3s;
}

.action-item .el-icon {
  font-size: 16px;
}

.action-item:hover {
  color: #7b61ff;
}

/* 【视觉优化】帖子时间 - 辅助灰色 */
.post-time {
  font-size: 12px;
  color: #888888;
}
</style>
