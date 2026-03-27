<template>
  <div class="right-panel-outer">
    <div class="right-panel-inner">
      <div class="panel-scroll">
        <div class="module active-users-module">
          <div class="module-header">
            <span class="module-title">
              <el-icon><UserFilled /></el-icon>
              活跃用户
            </span>
            <span class="module-more">更多</span>
          </div>
          <div class="module-content">
            <div v-loading="activeUsersLoading" class="users-grid">
              <div
                v-for="user in activeUsers"
                :key="user.id"
                class="user-item"
                @click="goToUserProfile(user.id)"
              >
                <div class="user-avatar-wrapper">
                  <img :src="user.avatar" :alt="user.nickname" class="user-avatar">
                  <span v-if="user.isOnline" class="online-dot"></span>
                </div>
                <div class="user-info">
                  <span class="user-nickname">{{ user.nickname }}</span>
                  <el-button
                    :type="user.isFollowed ? 'default' : 'primary'"
                    size="small"
                    class="follow-btn"
                    :class="{ followed: user.isFollowed }"
                    @click.stop="handleFollowUser(user)"
                  >
                    {{ user.isFollowed ? '已关注' : '关注' }}
                  </el-button>
                </div>
              </div>
            </div>
          </div>
        </div>

        <div class="module hot-topics-module">
          <div class="module-header">
            <span class="module-title">
              <el-icon><TrendCharts /></el-icon>
              热点信息
            </span>
            <span class="module-more">更多</span>
          </div>
          <div class="module-content">
            <div v-loading="hotTopicsLoading" class="topics-list">
              <div
                v-for="(topic, index) in hotTopics"
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

        <div class="module active-bars-module">
          <div class="module-header">
            <span class="module-title">
              <el-icon><Collection /></el-icon>
              活跃的吧
            </span>
            <span class="module-more">更多</span>
          </div>
          <div class="module-content">
            <div v-loading="activeBarsLoading" class="bars-list">
              <div
                v-for="bar in activeBars"
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
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { UserFilled, TrendCharts, Collection, View, Top, Bottom, Minus } from '@element-plus/icons-vue'
import { getActiveUsers, followUser, unfollowUser, getHotTopics, getActiveBars, followBar, unfollowBar } from '@/api/rightPanel'

const activeUsersLoading = ref(false)
const hotTopicsLoading = ref(false)
const activeBarsLoading = ref(false)

const activeUsers = ref([])
const hotTopics = ref([])
const activeBars = ref([])

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
  padding: 12px;
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

.module {
  background: #ffffff;
  border-radius: 12px;
  padding: 14px;
  margin-bottom: 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.04);
}

.module:last-child {
  margin-bottom: 0;
}

.module-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12px;
  padding-bottom: 10px;
  border-bottom: 1px solid #f0f0f0;
}

.module-title {
  font-size: 14px;
  font-weight: 600;
  color: #333333;
  display: flex;
  align-items: center;
  gap: 6px;
}

.module-title .el-icon {
  font-size: 16px;
  color: #7b61ff;
}

.module-more {
  font-size: 12px;
  color: #999999;
  cursor: pointer;
  transition: color 0.3s;
}

.module-more:hover {
  color: #7b61ff;
}

.users-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 10px;
}

.user-item {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 8px;
  border-radius: 10px;
  cursor: pointer;
  transition: all 0.3s;
  background: #fafafa;
}

.user-item:hover {
  background: #f5f5f5;
  transform: translateY(-1px);
}

.user-avatar-wrapper {
  position: relative;
  flex-shrink: 0;
}

.user-avatar {
  width: 36px;
  height: 36px;
  border-radius: 10px;
  object-fit: cover;
}

.online-dot {
  position: absolute;
  bottom: 0;
  right: 0;
  width: 10px;
  height: 10px;
  background: #52c41a;
  border: 2px solid #ffffff;
  border-radius: 50%;
}

.user-info {
  flex: 1;
  min-width: 0;
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.user-nickname {
  font-size: 12px;
  font-weight: 500;
  color: #333333;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.follow-btn {
  font-size: 11px !important;
  padding: 2px 8px !important;
  height: 22px !important;
  border-radius: 10px !important;
  min-width: 48px;
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

.topics-list {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.topic-item {
  display: flex;
  align-items: flex-start;
  gap: 10px;
  padding: 8px 10px;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.3s;
}

.topic-item:hover {
  background: #f8f8f8;
}

.topic-rank {
  width: 20px;
  height: 20px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 12px;
  font-weight: 600;
  color: #999999;
  background: #f0f0f0;
  border-radius: 4px;
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
  line-height: 1.4;
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
  gap: 10px;
}

.bar-item {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 10px;
  border-radius: 10px;
  cursor: pointer;
  transition: all 0.3s;
  background: #fafafa;
}

.bar-item:hover {
  background: #f5f5f5;
  transform: translateY(-1px);
}

.bar-avatar {
  width: 40px;
  height: 40px;
  border-radius: 10px;
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
</style>
