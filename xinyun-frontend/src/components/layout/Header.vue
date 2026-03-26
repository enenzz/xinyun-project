<template>
  <header class="header">
    <div class="header-content">
      <!-- 左侧Logo（保留你的Emoji风格） -->
      <div class="logo">
        <span class="logo-icon">💜</span>
        <span class="logo-text">心云</span>
      </div>

      <!-- 【新增】中间搜索框 -->
      <div class="search-box">
        <el-input
          v-model="searchKeyword"
          placeholder="搜索好友、动态、话题..."
          prefix-icon="Search"
          clearable
        />
      </div>

      <!-- 右侧操作区（优化） -->
      <div class="header-right">
        <!-- 【新增】发布动态按钮 -->
        <el-button type="primary" class="publish-btn">
          <el-icon><EditPen /></el-icon>
          发布动态
        </el-button>

        <!-- 【新增】用户头像下拉菜单（把退出登录放这里） -->
        <el-dropdown @command="handleCommand">
          <div class="user-avatar-wrapper">
            <el-avatar :size="36" src="https://i.pravatar.cc/100?img=0" />
          </div>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item command="profile">我的主页</el-dropdown-item>
              <el-dropdown-item command="setting">设置</el-dropdown-item>
              <el-dropdown-item command="logout" divided>退出登录</el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </div>
    </div>
  </header>
</template>

<script setup>
import { ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { EditPen, Search } from '@element-plus/icons-vue'
import { logout } from '@/api/user'

const searchKeyword = ref('')

// 处理下拉菜单命令
const handleCommand = async (command) => {
  if (command === 'logout') {
    // 保留你原有的完整退出登录逻辑
    try {
      await ElMessageBox.confirm('确定要退出登录吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
      
      await logout()
      localStorage.removeItem('token')
      localStorage.removeItem('userVO')
      ElMessage.success('退出登录成功')
      // TODO: 对接后端时跳转到登录页
      window.location.reload()
    } catch (error) {
      if (error !== 'cancel') {
        console.error('退出登录失败:', error)
      }
    }
  } else if (command === 'profile') {
    ElMessage.info('跳转到我的主页')
    // TODO: 后续加路由跳转
  } else if (command === 'setting') {
    ElMessage.info('跳转到设置')
  }
}
</script>

<style scoped>
/* 优化背景：加个渐变，更有质感 */
.header {
  background: linear-gradient(90deg, #6366f1 0%, #8b5cf6 100%);
  box-shadow: 0 2px 12px rgba(99, 102, 241, 0.2);
  position: sticky;
  top: 0;
  z-index: 100;
}

.header-content {
  max-width: 1280px; /* 稍微加宽一点，和下面布局对齐 */
  margin: 0 auto;
  padding: 0 20px;
  height: 64px; /* 稍微加高一点，更大气 */
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 20px; /* 新增：元素之间加间距 */
}

.logo {
  display: flex;
  align-items: center;
  gap: 8px;
  flex-shrink: 0; /* 防止Logo被压缩 */
}

.logo-icon {
  font-size: 26px;
}

.logo-text {
  font-size: 22px;
  font-weight: bold;
  color: white;
  user-select: none;
}

/* 新增：搜索框样式 */
.search-box {
  flex: 1; /* 占据中间剩余空间 */
  max-width: 450px; /* 限制最大宽度 */
  min-width: 200px;
}

.search-box :deep(.el-input__wrapper) {
  border-radius: 24px; /* 圆角搜索框 */
  box-shadow: none;
  background: rgba(255, 255, 255, 0.9);
  transition: background 0.2s;
}

.search-box :deep(.el-input__wrapper:hover) {
  background: rgba(255, 255, 255, 1);
}

.header-right {
  display: flex;
  align-items: center;
  gap: 16px;
  flex-shrink: 0; /* 防止右侧被压缩 */
}

/* 新增：发布按钮样式 */
.publish-btn {
  border-radius: 20px;
  font-weight: 500;
  background: rgba(255,255,255,0.2);
  border: 1px solid rgba(255,255,255,0.3);
  backdrop-filter: blur(4px);
}

.publish-btn:hover {
  background: rgba(255,255,255,0.3) !important;
  border-color: rgba(255,255,255,0.4) !important;
}

/* 新增：用户头像样式 */
.user-avatar-wrapper {
  cursor: pointer;
  transition: transform 0.2s;
  border-radius: 50%;
  overflow: hidden;
  border: 2px solid rgba(255,255,255,0.5);
}

.user-avatar-wrapper:hover {
  transform: scale(1.05);
}
</style>