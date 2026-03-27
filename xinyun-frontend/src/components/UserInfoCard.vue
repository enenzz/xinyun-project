<template>
  <div class="user-info-container" ref="containerRef" @wheel="handleWheel">
    <!-- 上箭头 -->
    <div class="arrow-btn arrow-up" @click="goPrev" :class="{ disabled: currentPage === 0 }">
      <el-icon><ArrowUp /></el-icon>
    </div>

    <!-- 用户卡片列表 -->
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

        <!-- 用户图片区域 -->
        <div class="user-images">
          <img v-for="(img, idx) in user.images" :key="idx" :src="img" class="user-image" alt="用户照片">
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
          <el-button type="default" class="action-btn message-btn">
            <el-icon><ChatDotRound /></el-icon>
            私聊
          </el-button>
        </div>
      </div>
    </div>

    <!-- 下箭头 -->
    <div class="arrow-btn arrow-down" @click="goNext" :class="{ disabled: currentPage >= totalPages - 1 }">
      <el-icon><ArrowDown /></el-icon>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { ChatDotRound, ArrowUp, ArrowDown } from '@element-plus/icons-vue'

const containerRef = ref(null)
const currentPage = ref(0)
const pageSize = 2

// 多个用户数据
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
    images: [
      'https://picsum.photos/200/150?random=1',
      'https://picsum.photos/200/150?random=2',
      'https://picsum.photos/200/150?random=3'
    ]
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
    images: [
      'https://picsum.photos/200/150?random=4',
      'https://picsum.photos/200/150?random=5',
      'https://picsum.photos/200/150?random=6'
    ]
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
    images: [
      'https://picsum.photos/200/150?random=7',
      'https://picsum.photos/200/150?random=8',
      'https://picsum.photos/200/150?random=9'
    ]
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
    images: [
      'https://picsum.photos/200/150?random=10',
      'https://picsum.photos/200/150?random=11',
      'https://picsum.photos/200/150?random=12'
    ]
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
    images: [
      'https://picsum.photos/200/150?random=13',
      'https://picsum.photos/200/150?random=14',
      'https://picsum.photos/200/150?random=15'
    ]
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
    images: [
      'https://picsum.photos/200/150?random=16',
      'https://picsum.photos/200/150?random=17',
      'https://picsum.photos/200/150?random=18'
    ]
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
    images: [
      'https://picsum.photos/200/150?random=19',
      'https://picsum.photos/200/150?random=20',
      'https://picsum.photos/200/150?random=21'
    ]
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
    images: [
      'https://picsum.photos/200/150?random=22',
      'https://picsum.photos/200/150?random=23',
      'https://picsum.photos/200/150?random=24'
    ]
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
    images: [
      'https://picsum.photos/200/150?random=25',
      'https://picsum.photos/200/150?random=26',
      'https://picsum.photos/200/150?random=27'
    ]
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
  color: #7b61ff;
  transition: all 0.3s;
  flex-shrink: 0;
}

.arrow-btn:hover:not(.disabled) {
  background: rgba(123, 97, 255, 0.08);
  color: #7b61ff;
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
  padding: 18px;
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
  margin-bottom: 12px;
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
  color: #333333;
  margin: 0 0 5px 0;
}

.user-meta {
  display: flex;
  gap: 10px;
  font-size: 12px;
  color: #999999;
}

.user-images {
  display: flex;
  gap: 8px;
  margin-bottom: 12px;
  flex-shrink: 0;
}

.user-image {
  width: calc(33.333% - 5px);
  height: 80px;
  border-radius: 10px;
  object-fit: cover;
  flex-shrink: 0;
}

.user-bio {
  font-size: 13px;
  color: #666666;
  line-height: 1.5;
  margin-bottom: 12px;
  display: -webkit-box;
  -webkit-line-clamp: 2;
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
  background: linear-gradient(135deg, #98E4D0, #87CEEB);
  color: #ffffff;
  font-size: 11px;
  border-radius: 12px;
  cursor: pointer;
  transition: all 0.3s;
  flex-shrink: 0;
}

.tag-item:hover {
  transform: translateY(-1px);
  box-shadow: 0 2px 6px rgba(152, 228, 208, 0.4);
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
  background: linear-gradient(135deg, #98E4D0, #87CEEB);
  color: #ffffff;
}

.detail-btn:hover {
  background: linear-gradient(135deg, #7dd4c0, #72c4e0);
  transform: translateY(-1px);
}

.message-btn {
  background: #ffffff;
  color: #666666;
  border: 1px solid #e0e0e0;
}

.message-btn:hover {
  background: #f8f8f8;
  border-color: #98E4D0;
  color: #98E4D0;
}
</style>
