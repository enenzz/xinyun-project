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
  background: #fff;
  border-radius: 12px;
  padding: 20px;
  margin-bottom: 20px;
  cursor: pointer;
  transition: box-shadow 0.2s;
}

.post-card:hover {
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
}

.bar-info {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 16px;
}

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
  color: #333;
  margin-bottom: 4px;
}

.bar-meta {
  font-size: 12px;
  color: #999;
}

.bar-meta .dot {
  margin: 0 6px;
}

.follow-btn {
  border-radius: 16px;
  padding: 4px 12px;
  font-size: 12px;
  border-color: #2385bb;
  color: #2385bb;
}

.follow-btn:hover {
  background: #2385bb !important;
  border-color: #2385bb !important;
  color: #fff !important;
}

.post-title {
  font-size: 16px;
  font-weight: 500;
  color: #222;
  line-height: 1.5;
  margin-bottom: 16px;
}

.post-title:hover {
  color: #2385bb;
}

.post-media {
  margin-bottom: 16px;
}

.video-preview {
  position: relative;
  border-radius: 8px;
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

.post-images {
  display: flex;
  gap: 8px;
  flex-wrap: wrap;
}

.post-image {
  width: 160px;
  height: 120px;
  object-fit: cover;
  border-radius: 8px;
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
  border-top: 1px solid #f0f0f0;
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
  color: #999;
}

.action-item .el-icon {
  font-size: 16px;
}

.post-time {
  font-size: 12px;
  color: #ccc;
}
</style>
