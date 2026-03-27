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
.post-card {
  background: #FFFFFF;
  box-shadow: none;
  border: 1px solid #E5E7EB;
  border-radius: 16px;
  padding: 20px;
  margin-bottom: 24px;
  cursor: pointer;
  transition: all 0.3s;
}

.post-card:hover {
  box-shadow: 0 4px 20px rgba(22, 93, 255, 0.1);
  transform: translateY(-2px);
  border-color: #165DFF;
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

.bar-name {
  font-size: 15px;
  font-weight: 600;
  color: #1F2937;
  margin-bottom: 4px;
}

.bar-meta {
  font-size: 12px;
  color: #6B7280;
}

.bar-meta .dot {
  margin: 0 6px;
}

.follow-btn {
  border-radius: 24px;
  padding: 4px 12px;
  font-size: 12px;
  border-color: #165DFF;
  color: #165DFF;
  background: #F5F8FF;
  transition: all 0.3s;
}

.follow-btn:hover {
  background: #165DFF !important;
  border-color: #165DFF !important;
  color: #fff !important;
}

.post-title {
  font-size: 16px;
  font-weight: 600;
  color: #1F2937;
  line-height: 1.5;
  margin-bottom: 16px;
  transition: color 0.3s;
}

.post-title:hover {
  color: #165DFF;
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

.post-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-top: 16px;
  border-top: 1px solid #E5E7EB;
}

.footer-left {
  display: flex;
  gap: 24px;
}

.action-item {
  display: flex;
  align-items: center;
  gap: 6px;
  font-size: 13px;
  color: #6B7280;
  transition: all 0.3s;
}

.action-item .el-icon {
  font-size: 16px;
}

.action-item:hover {
  color: #165DFF;
}

.post-time {
  font-size: 12px;
  color: #6B7280;
}
</style>
