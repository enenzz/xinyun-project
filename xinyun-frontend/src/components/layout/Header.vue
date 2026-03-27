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
          <el-avatar :size="32" class="login-btn" @click="handleOpenLogin">
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
  </header>

  <!-- 【修复】登录弹窗移到header外部，避免受父容器堆叠上下文影响 -->
  <el-dialog
    v-model="showLoginDialog"
    width="480px"
    :close-on-click-modal="true"
    destroy-on-close
    class="login-dialog"
    @open="handleDialogOpen"
    @closed="handleDialogClosed"
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
</template>

<script setup>
import { ref, computed, onMounted, watch, onUnmounted } from 'vue'
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

const handleOpenLogin = () => {
  showLoginDialog.value = true
}

const handleDialogOpen = () => {
  document.body.style.overflow = 'hidden'
}

const handleDialogClosed = () => {
  document.body.style.overflow = ''
}

watch(
  () => route.query,
  (query) => {
    if (query.openLogin === 'true' && !isLoggedIn.value) {
      showLoginDialog.value = true
      const newQuery = { ...route.query }
      delete newQuery.openLogin
      router.replace({ query: newQuery })
    }
  },
  { immediate: true }
)

onMounted(() => {
})

onUnmounted(() => {
  document.body.style.overflow = ''
})
</script>

<style scoped>
/* 【视觉优化】导航栏 - 纯白背景+轻微柔化阴影 */
.header {
  background: #ffffff;
  border-bottom: none;
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  z-index: 999;
  min-width: 1000px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.06);
}

.header-content {
  max-width: 1600px;
  margin: 0 auto;
  padding: 0 50px;
  height: 64px;
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 24px;
}

.logo {
  position: absolute;
  left: 116px;
  display: flex;
  align-items: center;
  gap: 8px;
  flex-shrink: 0;
}

.logo-text {
  font-size: 20px;
  font-weight: 700;
  color: #7b61ff;
  user-select: none;
}

.search-box {
  width: 520px;
  max-width: 520px;
}

.header-right {
  position: absolute;
  right: 116px;
  display: flex;
  align-items: center;
  gap: 16px;
  flex-shrink: 0;
}

/* 【视觉优化】搜索框 - 纯白背景+浅灰色描边 */
.search-box :deep(.el-input__wrapper) {
  border-radius: 24px;
  box-shadow: none;
  background: #ffffff;
  border: 1px solid #e0e0e0;
  transition: all 0.3s;
}

.search-box :deep(.el-input__wrapper:hover) {
  background: #ffffff;
  border-color: #c0c0c0;
}

.search-box :deep(.el-input__wrapper.is-focus) {
  background: #ffffff;
  border-color: #7b61ff;
  box-shadow: 0 0 0 3px rgba(123, 97, 255, 0.15);
}

.search-box :deep(.el-input__inner::placeholder) {
  color: #888888;
}

.header-right {
  display: flex;
  align-items: center;
  gap: 16px;
  flex-shrink: 0;
}

/* 【视觉优化】发帖按钮 - 低饱和紫色渐变 */
.publish-btn {
  border-radius: 24px;
  font-weight: 600;
  background: linear-gradient(135deg, #7b61ff, #9b8aff);
  border: none;
  padding: 8px 20px;
  box-shadow: 0 4px 16px rgba(123, 97, 255, 0.25);
  transition: all 0.3s;
}

.publish-btn:hover {
  background: linear-gradient(135deg, #6b51ef, #8b7aff) !important;
  transform: translateY(-2px);
  box-shadow: 0 8px 24px rgba(123, 97, 255, 0.35);
}

.header-icons {
  display: flex;
  align-items: center;
  gap: 12px;
}

/* 【视觉优化】图标按钮 - 深灰色 */
.icon-btn {
  font-size: 20px;
  color: #333333;
  cursor: pointer;
  padding: 4px;
  transition: all 0.3s;
}

.icon-btn:hover {
  color: #7b61ff;
  transform: scale(1.1);
}

.login-btn-wrapper {
  cursor: pointer;
}

/* 【视觉优化】登录按钮 - 低饱和紫色渐变 */
.login-btn {
  background: linear-gradient(135deg, #7b61ff, #9b8aff);
  color: #fff;
  font-size: 14px;
  cursor: pointer;
  transition: all 0.3s;
  box-shadow: 0 4px 16px rgba(123, 97, 255, 0.25);
}

.login-btn:hover {
  background: linear-gradient(135deg, #6b51ef, #8b7aff) !important;
  transform: translateY(-1px);
  box-shadow: 0 6px 20px rgba(123, 97, 255, 0.35);
}

.user-avatar-wrapper {
  cursor: pointer;
  border-radius: 50%;
  overflow: hidden;
  transition: all 0.3s;
}

.user-avatar-wrapper:hover {
  transform: scale(1.05);
}

/* 【修复】登录弹窗层级 - 最高级别99999 */
.login-dialog :deep(.el-dialog) {
  border-radius: 20px;
  box-shadow: 0 25px 80px rgba(123, 97, 255, 0.15);
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(12px);
  position: fixed !important;
  top: 50% !important;
  left: 50% !important;
  transform: translate(-50%, -50%) !important;
  margin: 0 !important;
  z-index: 99999 !important;
}

/* 【修复】登录弹窗遮罩层 - 全屏黑色半透明遮罩 */
.login-dialog :deep(.el-overlay) {
  z-index: 99998 !important;
  position: fixed !important;
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
  background: rgba(45, 38, 75, 0.5) !important;
  display: flex;
  justify-content: center;
  align-items: center;
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

/* 【视觉优化】弹窗标题 - 低饱和紫色 */
.dialog-title {
  font-size: 28px;
  font-weight: 700;
  color: #7b61ff;
  margin: 0 0 8px 0;
}

.dialog-subtitle {
  font-size: 14px;
  color: #868399;
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
  background: rgba(255, 255, 255, 0.9);
  border: 1px solid rgba(123, 97, 255, 0.15);
}

.login-form :deep(.el-input__wrapper:hover) {
  border-color: rgba(123, 97, 255, 0.3);
}

.login-form :deep(.el-input__wrapper.is-focus) {
  box-shadow: 0 0 0 3px rgba(123, 97, 255, 0.15);
  border-color: #7b61ff;
}

/* 【视觉优化】登录按钮 - 低饱和紫色渐变 */
.login-submit-btn {
  width: 100%;
  height: 48px;
  border-radius: 24px;
  font-size: 16px;
  font-weight: 600;
  background: linear-gradient(135deg, #7b61ff, #9b8aff);
  border: none;
  box-shadow: 0 4px 16px rgba(123, 97, 255, 0.25);
  transition: all 0.3s;
}

.login-submit-btn:hover {
  background: linear-gradient(135deg, #6b51ef, #8b7aff) !important;
  transform: translateY(-2px);
  box-shadow: 0 8px 24px rgba(123, 97, 255, 0.35);
}

.dialog-footer {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 6px;
}

.footer-text {
  font-size: 14px;
  color: #4a4658;
}

/* 【视觉优化】注册链接 - 低饱和紫色 */
.footer-link {
  font-size: 14px;
  color: #7b61ff;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s;
}

.footer-link:hover {
  color: #5b41ef;
}
</style>
