<template>
  <div class="right-panel-outer">
    <div class="right-panel-inner">
      <div class="panel-scroll">
        <div class="section active-users-section">
          <div class="section-header">
            <span class="section-title">活跃用户</span>
            <span class="section-more">更多</span>
          </div>
          <div class="section-content">
            <div v-loading="activeUsersLoading" class="users-horizontal">
              <div
                v-for="user in firstSixUsers"
                :key="user.id"
                class="user-horizontal-item"
                @click="goToUserProfile(user.id)"
              >
                <img :src="user.avatar" :alt="user.nickname" class="user-avatar-horizontal">
                <span class="user-nickname-horizontal">{{ user.nickname }}</span>
              </div>
            </div>
          </div>
        </div>

        <div class="divider"></div>

        <div class="section hot-topics-section">
          <div class="section-header">
            <span class="section-title">热点信息</span>
            <span class="section-more">更多</span>
          </div>
          <div class="section-content">
            <div v-loading="hotTopicsLoading" class="topics-list">
              <div
                v-for="(topic, index) in firstFiveTopics"
                :key="topic.id"
                class="topic-item"
                @click="goToPostDetail(topic.id)"
              >
                <span class="topic-rank" :class="getRankClass(index)">{{ index + 1 }}</span>
                <div class="topic-content">
                  <span class="topic-title">{{ topic.title }}</span>
                  <div class="topic-meta">
                    <span class="heat-value">
                      <el-icon><View /></el-icon>
                      {{ formatHeat(topic.heat) }}
                    </span>
                    <span class="trend-icon" :class="topic.trend">
                      <el-icon v-if="topic.trend === 'up'"><Top /></el-icon>
                      <el-icon v-else-if="topic.trend === 'down'"><Bottom /></el-icon>
                      <el-icon v-else><Minus /></el-icon>
                    </span>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>

        <div class="divider"></div>

        <div class="section active-bars-section">
          <div class="section-header">
            <span class="section-title">活跃的吧</span>
            <span class="section-more">更多</span>
          </div>
          <div class="section-content">
            <div v-loading="activeBarsLoading" class="bars-list">
              <div
                v-for="bar in firstFiveBars"
                :key="bar.id"
                class="bar-item"
                @click="goToBarDetail(bar.id)"
              >
                <img :src="bar.avatar" :alt="bar.name" class="bar-avatar">
                <div class="bar-info">
                  <span class="bar-name">{{ bar.name }}</span>
                  <span class="bar-members">{{ formatMemberCount(bar.memberCount) }} 成员</span>
                </div>
                <el-button
                  :type="bar.isFollowed ? 'default' : 'primary'"
                  size="small"
                  class="follow-btn"
                  :class="{ followed: bar.isFollowed }"
                  @click.stop="handleFollowBar(bar)"
                >
                  {{ bar.isFollowed ? '已关注' : '关注' }}
                </el-button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { View, Top, Bottom, Minus } from '@element-plus/icons-vue'
import { getActiveUsers, followUser, unfollowUser, getHotTopics, getActiveBars, followBar, unfollowBar } from '@/api/rightPanel'

const activeUsersLoading = ref(false)
const hotTopicsLoading = ref(false)
const activeBarsLoading = ref(false)

const activeUsers = ref([])
const hotTopics = ref([])
const activeBars = ref([])

const firstSixUsers = computed(() => {
  return activeUsers.value.slice(0, 6)
})

const firstFiveTopics = computed(() => {
  return hotTopics.value.slice(0, 5)
})

const firstFiveBars = computed(() => {
  return activeBars.value.slice(0, 5)
})

const loadActiveUsers = async () => {
  try {
    activeUsersLoading.value = true
    const res = await getActiveUsers()
    activeUsers.value = res.data || []
  } catch (error) {
    console.error('加载活跃用户失败:', error)
  } finally {
    activeUsersLoading.value = false
  }
}

const loadHotTopics = async () => {
  try {
    hotTopicsLoading.value = true
    const res = await getHotTopics()
    hotTopics.value = res.data || []
  } catch (error) {
    console.error('加载热点信息失败:', error)
  } finally {
    hotTopicsLoading.value = false
  }
}

const loadActiveBars = async () => {
  try {
    activeBarsLoading.value = true
    const res = await getActiveBars()
    activeBars.value = res.data || []
  } catch (error) {
    console.error('加载活跃的吧失败:', error)
  } finally {
    activeBarsLoading.value = false
  }
}

const handleFollowUser = async (user) => {
  try {
    if (user.isFollowed) {
      await unfollowUser(user.id)
      user.isFollowed = false
      ElMessage.success('已取消关注')
    } else {
      await followUser(user.id)
      user.isFollowed = true
      ElMessage.success('关注成功')
    }
  } catch (error) {
    ElMessage.error('操作失败，请重试')
    console.error('关注操作失败:', error)
  }
}

const handleFollowBar = async (bar) => {
  try {
    if (bar.isFollowed) {
      await unfollowBar(bar.id)
      bar.isFollowed = false
      ElMessage.success('已取消关注')
    } else {
      await followBar(bar.id)
      bar.isFollowed = true
      ElMessage.success('关注成功')
    }
  } catch (error) {
    ElMessage.error('操作失败，请重试')
    console.error('关注操作失败:', error)
  }
}

const goToUserProfile = (userId) => {
  console.log('跳转到用户主页:', userId)
}

const goToPostDetail = (postId) => {
  console.log('跳转到帖子详情:', postId)
}

const goToBarDetail = (barId) => {
  console.log('跳转到吧详情:', barId)
}

const getRankClass = (index) => {
  if (index === 0) return 'rank-first'
  if (index === 1) return 'rank-second'
  if (index === 2) return 'rank-third'
  return ''
}

const formatHeat = (heat) => {
  if (heat >= 10000) {
    return (heat / 10000).toFixed(1) + '万'
  }
  return heat.toString()
}

const formatMemberCount = (count) => {
  if (count >= 10000) {
    return (count / 10000).toFixed(1) + '万'
  }
  return count.toString()
}

onMounted(() => {
  loadActiveUsers()
  loadHotTopics()
  loadActiveBars()
})
</script>

<style scoped>
.right-panel-outer {
  width: 100%;
  height: 100%;
  box-sizing: border-box;
}

.right-panel-inner {
  width: 100%;
  height: 100%;
  background: #FAFAF5;
  border-radius: 16px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
  box-sizing: border-box;
  overflow: hidden;
}

.panel-scroll {
  width: 100%;
  height: 100%;
  overflow-y: auto;
  padding: 12px 8px;
  box-sizing: border-box;
}

.panel-scroll::-webkit-scrollbar {
  width: 4px;
}

.panel-scroll::-webkit-scrollbar-track {
  background: transparent;
}

.panel-scroll::-webkit-scrollbar-thumb {
  background: rgba(0, 0, 0, 0.1);
  border-radius: 2px;
}

.section {
  padding: 0 4px;
  margin-bottom: 12px;
}

.section:last-child {
  margin-bottom: 0;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
}

.section-title {
  font-size: 15px;
  font-weight: 600;
  color: #333333;
}

.section-more {
  font-size: 12px;
  color: #999999;
  cursor: pointer;
  transition: color 0.3s;
}

.section-more:hover {
  color: #7b61ff;
}

.divider {
  height: 1px;
  background: linear-gradient(to right, transparent, #e0e0e0, transparent);
  margin: 0 4px 12px 4px;
}

.users-horizontal {
  display: flex;
  justify-content: space-between;
  gap: 10px;
  overflow-x: auto;
  padding: 4px 0;
}

.users-horizontal::-webkit-scrollbar {
  display: none;
}

.user-horizontal-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 8px;
  cursor: pointer;
  transition: all 0.3s;
  flex-shrink: 0;
  min-width: 0;
}

.user-horizontal-item:hover {
  transform: translateY(-2px);
}

.user-avatar-horizontal {
  width: 48px;
  height: 48px;
  border-radius: 50%;
  object-fit: cover;
  border: 2px solid #ffffff;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.user-nickname-horizontal {
  font-size: 12px;
  color: #333333;
  text-align: center;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  max-width: 56px;
}

.topics-list {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.topic-item {
  display: flex;
  align-items: flex-start;
  gap: 10px;
  padding: 6px 4px;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.3s;
}

.topic-item:hover {
  background: rgba(123, 97, 255, 0.04);
}

.topic-rank {
  width: 22px;
  height: 22px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 12px;
  font-weight: 700;
  color: #999999;
  background: #f0f0f0;
  border-radius: 6px;
  flex-shrink: 0;
}

.topic-rank.rank-first {
  background: linear-gradient(135deg, #ff6b6b, #ff8e53);
  color: #ffffff;
}

.topic-rank.rank-second {
  background: linear-gradient(135deg, #ffa940, #ffc53d);
  color: #ffffff;
}

.topic-rank.rank-third {
  background: linear-gradient(135deg, #ffc53d, #ffec3d);
  color: #ffffff;
}

.topic-content {
  flex: 1;
  min-width: 0;
}

.topic-title {
  font-size: 13px;
  color: #333333;
  line-height: 1.5;
  display: -webkit-box;
  -webkit-line-clamp: 1;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.topic-meta {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-top: 4px;
}

.heat-value {
  font-size: 11px;
  color: #999999;
  display: flex;
  align-items: center;
  gap: 3px;
}

.heat-value .el-icon {
  font-size: 12px;
}

.trend-icon {
  display: flex;
  align-items: center;
}

.trend-icon.up {
  color: #ff4d4f;
}

.trend-icon.down {
  color: #52c41a;
}

.trend-icon.stable {
  color: #999999;
}

.bars-list {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.bar-item {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 6px 4px;
  border-radius: 10px;
  cursor: pointer;
  transition: all 0.3s;
}

.bar-item:hover {
  background: rgba(123, 97, 255, 0.04);
}

.bar-avatar {
  width: 42px;
  height: 42px;
  border-radius: 12px;
  object-fit: cover;
  flex-shrink: 0;
}

.bar-info {
  flex: 1;
  min-width: 0;
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.bar-name {
  font-size: 13px;
  font-weight: 500;
  color: #333333;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.bar-members {
  font-size: 11px;
  color: #999999;
}

.follow-btn {
  font-size: 12px !important;
  padding: 4px 12px !important;
  height: 28px !important;
  border-radius: 14px !important;
  min-width: 56px;
  font-weight: 500;
}

.follow-btn.followed {
  background: #f5f5f5;
  border-color: #e0e0e0;
  color: #999999;
}

.follow-btn:not(.followed) {
  background: linear-gradient(135deg, #98E4D0, #87CEEB);
  border: none;
  color: #ffffff;
}
</style>
