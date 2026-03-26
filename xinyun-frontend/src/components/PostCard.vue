<template>
  <div class="post-card">
    <div class="post-header">
      <img :src="post.avatar" class="user-avatar" :alt="post.username">
      <div class="user-info">
        <div class="user-name">{{ post.username }}</div>
        <div class="post-time" v-if="post.location">{{ post.location }} · {{ post.createTime }}</div>
        <div class="post-time" v-else>{{ post.createTime }}</div>
      </div>
    </div>

    <div class="post-content">{{ post.content }}</div>

    <div v-if="post.images?.length" class="post-images">
      <img 
        v-for="(img, index) in post.images" 
        :key="index"
        :src="img" 
        class="post-image"
        :class="{ 'single-image': post.images.length === 1 }"
        alt="动态图片"
      >
    </div>

    <div class="post-footer">
      <div class="action-item" @click="toggleLike">
        <span class="action-icon" :class="{ liked: post.isLiked }">
          {{ post.isLiked ? '❤️' : '🤍' }}
        </span>
        <span class="action-count">{{ post.likeCount }}</span>
      </div>
      <div class="action-item">
        <span class="action-icon">💬</span>
        <span class="action-count">{{ post.commentCount }}</span>
      </div>
      <div class="action-item">
        <span class="action-icon">🔗</span>
      </div>
    </div>
  </div>
</template>

<script setup>
import { likePost, unlikePost } from '@/api/post'
import { ElMessage } from 'element-plus'

const props = defineProps({
  post: {
    type: Object,
    required: true
  }
})

const emit = defineEmits(['like'])

const toggleLike = async () => {
  try {
    if (props.post.isLiked) {
      await unlikePost(props.post.id)
      props.post.isLiked = false
      props.post.likeCount--
    } else {
      await likePost(props.post.id)
      props.post.isLiked = true
      props.post.likeCount++
    }
    emit('like', props.post)
  } catch (error) {
    ElMessage.error('操作失败，请重试')
  }
}
</script>

<style scoped>
.post-card {
  background: white;
  border-radius: 12px;
  padding: 20px;
  margin-bottom: 20px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
  transition: all 0.3s;
}

.post-card:hover {
  box-shadow: 0 4px 16px rgba(99, 102, 241, 0.15);
  transform: translateY(-2px);
}

.post-header {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 16px;
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
  font-size: 15px;
  font-weight: 600;
  color: #333;
}

.post-time {
  font-size: 12px;
  color: #999;
  margin-top: 2px;
}

.post-content {
  font-size: 15px;
  color: #333;
  line-height: 1.6;
  margin-bottom: 16px;
}

.post-images {
  display: grid;
  gap: 8px;
  margin-bottom: 16px;
}

.post-images.single {
  grid-template-columns: 1fr;
}

.post-images:not(.single) {
  grid-template-columns: repeat(3, 1fr);
}

.post-image {
  width: 100%;
  aspect-ratio: 1;
  object-fit: cover;
  border-radius: 8px;
  cursor: pointer;
  transition: transform 0.3s;
}

.post-image:hover {
  transform: scale(1.02);
}

.post-image.single-image {
  aspect-ratio: auto;
  max-height: 400px;
  width: auto;
  max-width: 100%;
}

.post-footer {
  display: flex;
  align-items: center;
  gap: 32px;
  padding-top: 12px;
  border-top: 1px solid #f0f0f0;
}

.action-item {
  display: flex;
  align-items: center;
  gap: 6px;
  cursor: pointer;
  transition: color 0.3s;
}

.action-item:hover {
  color: #6366f1;
}

.action-icon {
  font-size: 20px;
  transition: transform 0.2s;
}

.action-icon:hover {
  transform: scale(1.2);
}

.action-icon.liked {
  animation: heartBeat 0.3s;
}

@keyframes heartBeat {
  0%, 100% { transform: scale(1); }
  50% { transform: scale(1.3); }
}

.action-count {
  font-size: 14px;
  color: #666;
}
</style>
