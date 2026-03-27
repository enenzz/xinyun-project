<template>
  <div class="user-info-container" ref="containerRef" @wheel="handleWheel">
    <div class="arrow-btn arrow-up" @click="goPrev" :class="{ disabled: currentPage === 0 }">
      <el-icon><ArrowUp /></el-icon>
    </div>

    <div class="user-cards-wrapper">
      <div
        v-for="(user, index) in currentUsers"
        :key="user.id"
        class="user-info-card"
        :style="{ animationDelay: index * 0.1 + 's' }"
      >
        <div class="user-header">
          <img :src="user.avatar" class="user-avatar" :alt="user.nickname">
          <div class="user-basic">
            <h3 class="user-nickname">{{ user.nickname }}</h3>
            <div class="user-meta">
              <span class="user-age">{{ user.age }}岁</span>
              <span class="user-city">{{ user.city }}</span>
            </div>
          </div>
        </div>

        <div class="user-image-single">
          <img :src="user.image" class="user-image" alt="用户照片">
        </div>

        <div class="user-bio">
          {{ user.bio }}
        </div>

        <div class="user-tags">
          <span v-for="tag in user.tags" :key="tag" class="tag-item">
            {{ tag }}
          </span>
        </div>

        <div class="user-actions">
          <el-button type="primary" class="action-btn detail-btn">
            详情
          </el-button>
          <el-button type="default" class="action-btn message-btn" @click="handlePrivateChat(user)">
            <el-icon><ChatDotRound /></el-icon>
            私聊
          </el-button>
        </div>
      </div>
    </div>

    <div class="arrow-btn arrow-down" @click="goNext" :class="{ disabled: currentPage >= totalPages - 1 }">
      <el-icon><ArrowDown /></el-icon>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { ChatDotRound, ArrowUp, ArrowDown } from '@element-plus/icons-vue'

const emit = defineEmits(['private-chat'])

const containerRef = ref(null)
const currentPage = ref(0)
const pageSize = 2

const users = ref([
  {
    id: 1,
    avatar: 'https://i.pravatar.cc/120?img=68',
    nickname: '小明同学',
    age: 24,
    city: '北京',
    bio: '喜欢运动和美食，找志同道合的朋友一起分享生活',
    isFollowed: false,
    tags: ['运动', '美食', '旅行'],
    image: 'https://picsum.photos/300/200?random=1'
  },
  {
    id: 2,
    avatar: 'https://i.pravatar.cc/120?img=69',
    nickname: '小红',
    age: 22,
    city: '上海',
    bio: '热爱生活，喜欢看书和画画',
    isFollowed: true,
    tags: ['阅读', '绘画', '咖啡'],
    image: 'https://picsum.photos/300/200?random=4'
  },
  {
    id: 3,
    avatar: 'https://i.pravatar.cc/120?img=70',
    nickname: '小刚',
    age: 26,
    city: '深圳',
    bio: '程序员一枚，喜欢科技和游戏',
    isFollowed: false,
    tags: ['编程', '游戏', '科技'],
    image: 'https://picsum.photos/300/200?random=7'
  },
  {
    id: 4,
    avatar: 'https://i.pravatar.cc/120?img=71',
    nickname: '小美',
    age: 23,
    city: '杭州',
    bio: '喜欢旅行和美食，探索世界的美好',
    isFollowed: false,
    tags: ['旅行', '美食', '摄影'],
    image: 'https://picsum.photos/300/200?random=10'
  },
  {
    id: 5,
    avatar: 'https://i.pravatar.cc/120?img=72',
    nickname: '小强',
    age: 27,
    city: '广州',
    bio: '健身达人，分享运动心得',
    isFollowed: true,
    tags: ['健身', '跑步', '瑜伽'],
    image: 'https://picsum.photos/300/200?random=13'
  },
  {
    id: 6,
    avatar: 'https://i.pravatar.cc/120?img=73',
    nickname: '小丽',
    age: 21,
    city: '成都',
    bio: '吃货一枚，寻找成都美食',
    isFollowed: false,
    tags: ['美食', '火锅', '川菜'],
    image: 'https://picsum.photos/300/200?random=16'
  },
  {
    id: 7,
    avatar: 'https://i.pravatar.cc/120?img=74',
    nickname: '小华',
    age: 25,
    city: '南京',
    bio: '喜欢历史和文化，逛博物馆',
    isFollowed: false,
    tags: ['历史', '文化', '旅行'],
    image: 'https://picsum.photos/300/200?random=19'
  },
  {
    id: 8,
    avatar: 'https://i.pravatar.cc/120?img=75',
    nickname: '小伟',
    age: 28,
    city: '武汉',
    bio: '音乐爱好者，弹吉他',
    isFollowed: true,
    tags: ['音乐', '吉他', '摇滚'],
    image: 'https://picsum.photos/300/200?random=22'
  },
  {
    id: 9,
    avatar: 'https://i.pravatar.cc/120?img=76',
    nickname: '小芳',
    age: 22,
    city: '西安',
    bio: '汉服爱好者，喜欢传统文化',
    isFollowed: false,
    tags: ['汉服', '传统文化', '茶道'],
    image: 'https://picsum.photos/300/200?random=25'
  }
])

const totalPages = computed(() => Math.ceil(users.value.length / pageSize))

const currentUsers = computed(() => {
  const start = currentPage.value * pageSize
  return users.value.slice(start, start + pageSize)
})

const goPrev = () => {
  if (currentPage.value > 0) {
    currentPage.value--
  }
}

const goNext = () => {
  if (currentPage.value < totalPages.value - 1) {
    currentPage.value++
  }
}

const handleWheel = (e) => {
  if (e.deltaY > 0) {
    goNext()
  } else {
    goPrev()
  }
}

const handlePrivateChat = (user) => {
  emit('private-chat', user)
}
</script>

<style scoped>
.user-info-container {
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
  position: relative;
  box-sizing: border-box;
}

.arrow-btn {
  width: 100%;
  height: 32px;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  color: #165DFF;
  transition: all 0.3s;
  flex-shrink: 0;
}

.arrow-btn:hover:not(.disabled) {
  background: #E8F3FF;
  color: #165DFF;
}

.arrow-btn.disabled {
  color: #e0e0e0;
  cursor: not-allowed;
}

.arrow-btn .el-icon {
  font-size: 18px;
}

.user-cards-wrapper {
  flex: 1;
  overflow-y: auto;
  padding: 8px 0;
  display: flex;
  flex-direction: column;
  justify-content: space-around;
  align-items: center;
}

.user-cards-wrapper::-webkit-scrollbar {
  display: none;
}

.user-cards-wrapper {
  -ms-overflow-style: none;
  scrollbar-width: none;
}

.user-info-card {
  background: #ffffff;
  border-radius: 14px;
  padding: 14px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
  flex-shrink: 0;
  animation: fadeInUp 0.4s ease-out forwards;
  opacity: 0;
  transform: translateY(20px);
  display: flex;
  flex-direction: column;
  width: 100%;
  min-height: calc(48% - 8px);
  box-sizing: border-box;
}

@keyframes fadeInUp {
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.user-header {
  display: flex;
  gap: 12px;
  margin-bottom: 10px;
  flex-shrink: 0;
}

.user-avatar {
  width: 56px;
  height: 56px;
  border-radius: 14px;
  object-fit: cover;
  flex-shrink: 0;
}

.user-basic {
  flex: 1;
  min-width: 0;
}

.user-nickname {
  font-size: 15px;
  font-weight: 700;
  color: #1F2937;
  margin: 0 0 5px 0;
}

.user-meta {
  display: flex;
  gap: 10px;
  font-size: 12px;
  color: #6B7280;
}

.user-image-single {
  margin-bottom: 10px;
  flex-shrink: 0;
}

.user-image {
  width: 100%;
  height: 140px;
  border-radius: 10px;
  object-fit: cover;
  flex-shrink: 0;
}

.user-bio {
  font-size: 13px;
  color: #6B7280;
  line-height: 1.5;
  margin-bottom: 10px;
  display: -webkit-box;
  -webkit-line-clamp: 1;
  -webkit-box-orient: vertical;
  overflow: hidden;
  flex-shrink: 0;
}

.user-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 6px;
  margin-bottom: auto;
  flex-shrink: 0;
}

.tag-item {
  padding: 4px 10px;
  background: #E8F3FF;
  color: #165DFF;
  font-size: 11px;
  border-radius: 12px;
  cursor: pointer;
  transition: all 0.3s;
  flex-shrink: 0;
}

.tag-item:hover {
  transform: translateY(-1px);
  background: #D6E8FF;
}

.user-actions {
  display: flex;
  gap: 10px;
  margin-top: auto;
  flex-shrink: 0;
}

.action-btn {
  flex: 1;
  border-radius: 16px;
  font-weight: 600;
  border: none;
  transition: all 0.3s;
  font-size: 13px;
  padding: 8px 0;
}

.detail-btn {
  background: #165DFF;
  color: #ffffff;
}

.detail-btn:hover {
  background: #0E42D2;
  transform: translateY(-1px);
}

.message-btn {
  background: #F5F8FF;
  color: #165DFF;
  border: 1px solid #E8F3FF;
}

.message-btn:hover {
  background: #E8F3FF;
  border-color: #165DFF;
  color: #165DFF;
}
</style>
