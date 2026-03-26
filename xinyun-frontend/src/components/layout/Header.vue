<template>
  <header class="header">
    <div class="header-content">
      <div class="logo">
        <span class="logo-text">心云</span>
      </div>

      <div class="search-box">
        <el-input
          v-model="searchKeyword"
          placeholder="搜索吧或帖子"
          prefix-icon="Search"
          clearable
        />
      </div>

      <div class="header-right">
        <el-button type="primary" class="publish-btn">
          + 发帖
        </el-button>
        <div class="header-icons">
          <el-tooltip content="消息" placement="bottom">
            <el-icon class="icon-btn"><Bell /></el-icon>
          </el-tooltip>
          <el-tooltip content="收藏" placement="bottom">
            <el-icon class="icon-btn"><Star /></el-icon>
          </el-tooltip>
          <el-tooltip content="历史" placement="bottom">
            <el-icon class="icon-btn"><Clock /></el-icon>
          </el-tooltip>
        </div>
        <el-dropdown @command="handleCommand">
          <div class="user-avatar-wrapper">
            <el-avatar :size="32" src="https://i.pravatar.cc/100?img=0" />
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
import { Search, Bell, Star, Clock } from '@element-plus/icons-vue'
import { logout } from '@/api/user'

const searchKeyword = ref('')

const handleCommand = async (command) => {
  if (command === 'logout') {
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
      window.location.reload()
    } catch (error) {
      if (error !== 'cancel') {
        console.error('退出登录失败:', error)
      }
    }
  } else if (command === 'profile') {
    ElMessage.info('跳转到我的主页')
  } else if (command === 'setting') {
    ElMessage.info('跳转到设置')
  }
}
</script>

<style scoped>
.header {
  background: #fff;
  border-bottom: 1px solid #e5e6eb;
  position: sticky;
  top: 0;
  z-index: 100;
  min-width: 1000px;
}

.header-content {
  max-width: 1400px;
  margin: 0 auto;
  padding: 0 20px;
  height: 60px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 24px;
}

.logo {
  display: flex;
  align-items: center;
  gap: 8px;
  flex-shrink: 0;
}

.logo-text {
  font-size: 20px;
  font-weight: 700;
  color: #2385bb;
  user-select: none;
}

.search-box {
  flex: 1;
  max-width: 520px;
  min-width: 200px;
}

.search-box :deep(.el-input__wrapper) {
  border-radius: 20px;
  box-shadow: none;
  background: #f5f6f7;
  border: 1px solid #e5e6eb;
}

.search-box :deep(.el-input__wrapper:hover) {
  background: #fff;
  border-color: #c9cdd4;
}

.search-box :deep(.el-input__wrapper.is-focus) {
  background: #fff;
  border-color: #2385bb;
  box-shadow: 0 0 0 2px rgba(35, 133, 187, 0.1);
}

.header-right {
  display: flex;
  align-items: center;
  gap: 16px;
  flex-shrink: 0;
}

.publish-btn {
  border-radius: 6px;
  font-weight: 500;
  background: #2385bb;
  border: none;
  padding: 8px 20px;
}

.publish-btn:hover {
  background: #1e75a6 !important;
}

.header-icons {
  display: flex;
  align-items: center;
  gap: 12px;
}

.icon-btn {
  font-size: 20px;
  color: #666;
  cursor: pointer;
  padding: 4px;
  transition: color 0.2s;
}

.icon-btn:hover {
  color: #2385bb;
}

.user-avatar-wrapper {
  cursor: pointer;
  border-radius: 50%;
  overflow: hidden;
}
</style>
