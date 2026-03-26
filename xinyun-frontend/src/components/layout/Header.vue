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
        
        <div v-if="!isLoggedIn" class="login-btn-wrapper">
          <el-avatar :size="32" class="login-btn" @click="showLoginDialog = true">
            登录
          </el-avatar>
        </div>
        
        <el-dropdown v-else @command="handleCommand">
          <div class="user-avatar-wrapper">
            <el-avatar :size="32" :src="userAvatar" />
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

    <el-dialog
      v-model="showLoginDialog"
      width="480px"
      :close-on-click-modal="true"
      destroy-on-close
      class="login-dialog"
    >
      <template #header>
        <div class="dialog-header">
          <h2 class="dialog-title">登录心云</h2>
          <p class="dialog-subtitle">欢迎回来，继续你的精彩</p>
        </div>
      </template>
      
      <el-form :model="loginForm" :rules="loginRules" ref="loginFormRef" class="login-form">
        <el-form-item prop="username">
          <el-input 
            v-model="loginForm.username" 
            placeholder="请输入用户名"
            size="large"
            prefix-icon="User"
          />
        </el-form-item>
        <el-form-item prop="password">
          <el-input 
            v-model="loginForm.password" 
            type="password" 
            placeholder="请输入密码"
            size="large"
            prefix-icon="Lock"
            show-password
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" size="large" class="login-submit-btn" @click="handleLogin" :loading="loginLoading">
            登录
          </el-button>
        </el-form-item>
      </el-form>
      
      <template #footer>
        <div class="dialog-footer">
          <span class="footer-text">还没有账号？</span>
          <span class="footer-link" @click="goToRegister">立即注册</span>
        </div>
      </template>
    </el-dialog>
  </header>
</template>

<script setup>
import { ref, computed, onMounted, watch } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search, Bell, Star, Clock, User, Lock } from '@element-plus/icons-vue'
import { login, logout } from '@/api/user'
import md5 from 'blueimp-md5'

const router = useRouter()
const route = useRoute()
const searchKeyword = ref('')
const showLoginDialog = ref(false)
const loginLoading = ref(false)
const loginFormRef = ref(null)

const loginForm = ref({
  username: '',
  password: ''
})

const loginRules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 4, max: 16, message: '用户名长度为4-16位', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, max: 20, message: '密码长度为6-20位', trigger: 'blur' }
  ]
}

const isLoggedIn = computed(() => {
  return !!localStorage.getItem('token')
})

const userAvatar = computed(() => {
  const userVO = localStorage.getItem('userVO')
  if (userVO) {
    try {
      return JSON.parse(userVO).avatar || ''
    } catch {
      return ''
    }
  }
  return ''
})

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

const handleLogin = async () => {
  if (!loginFormRef.value) return
  
  try {
    await loginFormRef.value.validate()
  } catch {
    return
  }

  try {
    loginLoading.value = true
    const res = await login({
      username: loginForm.value.username,
      password: md5(loginForm.value.password)
    })

    if (res.code === 200) {
      localStorage.setItem('token', res.data.token)
      localStorage.setItem('userVO', JSON.stringify(res.data.userVO))
      ElMessage.success('登录成功')
      showLoginDialog.value = false
      window.location.reload()
    } else {
      ElMessage.error(res.message || '登录失败')
    }
  } catch (error) {
    console.error('登录失败:', error)
    ElMessage.error(error.response?.data?.message || '登录失败，请重试')
  } finally {
    loginLoading.value = false
  }
}

const goToRegister = () => {
  showLoginDialog.value = false
  router.push('/register')
}

// 检测路由参数 openLogin，自动弹出登录框
watch(
  () => route.query,
  (query) => {
    if (query.openLogin === 'true' && !isLoggedIn.value) {
      showLoginDialog.value = true
    }
  },
  { immediate: true }
)

onMounted(() => {
})
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
  max-width: 1600px;
  margin: 0 auto;
  padding: 0 8px 0 8px;
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

.login-btn-wrapper {
  cursor: pointer;
}

.login-btn {
  background: linear-gradient(135deg, #6366f1, #8b5cf6);
  color: #fff;
  font-size: 14px;
  cursor: pointer;
  transition: all 0.3s;
  box-shadow: 0 4px 12px rgba(99, 102, 241, 0.3);
}

.login-btn:hover {
  background: linear-gradient(135deg, #4f46e5, #7c3aed) !important;
  transform: translateY(-1px);
  box-shadow: 0 6px 20px rgba(99, 102, 241, 0.4);
}

.user-avatar-wrapper {
  cursor: pointer;
  border-radius: 50%;
  overflow: hidden;
}

.login-dialog :deep(.el-dialog) {
  border-radius: 20px;
  box-shadow: 0 25px 80px rgba(0, 0, 0, 0.15);
}

.login-dialog :deep(.el-dialog__header) {
  padding: 40px 40px 0;
  margin: 0;
  border-bottom: none;
}

.login-dialog :deep(.el-dialog__body) {
  padding: 32px 40px 24px;
}

.login-dialog :deep(.el-dialog__footer) {
  padding: 0 40px 40px;
  border-top: none;
}

.dialog-header {
  text-align: center;
}

.dialog-title {
  font-size: 28px;
  font-weight: 700;
  color: #111827;
  margin: 0 0 8px 0;
}

.dialog-subtitle {
  font-size: 14px;
  color: #6b7280;
  margin: 0;
}

.login-form {
  margin-top: 8px;
}

.login-form :deep(.el-form-item) {
  margin-bottom: 24px;
}

.login-form :deep(.el-input__wrapper) {
  border-radius: 12px;
  padding: 12px 16px;
  transition: all 0.3s;
}

.login-form :deep(.el-input__wrapper:hover) {
  box-shadow: 0 0 0 1px #d1d5db;
}

.login-form :deep(.el-input__wrapper.is-focus) {
  box-shadow: 0 0 0 2px rgba(99, 102, 241, 0.2);
}

.login-submit-btn {
  width: 100%;
  height: 48px;
  border-radius: 24px;
  font-size: 16px;
  font-weight: 600;
  background: linear-gradient(135deg, #6366f1, #8b5cf6);
  border: none;
  box-shadow: 0 4px 14px rgba(99, 102, 241, 0.4);
  transition: all 0.3s;
}

.login-submit-btn:hover {
  background: linear-gradient(135deg, #4f46e5, #7c3aed) !important;
  transform: translateY(-2px);
  box-shadow: 0 8px 20px rgba(99, 102, 241, 0.5);
}

.dialog-footer {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 6px;
}

.footer-text {
  font-size: 14px;
  color: #6b7280;
}

.footer-link {
  font-size: 14px;
  color: #6366f1;
  font-weight: 600;
  cursor: pointer;
  transition: color 0.2s;
}

.footer-link:hover {
  color: #4f46e5;
}
</style>
