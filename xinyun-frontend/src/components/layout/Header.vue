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
      title="登录"
      width="400px"
      :close-on-click-modal="true"
      destroy-on-close
    >
      <el-form :model="loginForm" label-width="80px">
        <el-form-item label="用户名">
          <el-input v-model="loginForm.username" placeholder="请输入用户名" />
        </el-form-item>
        <el-form-item label="密码">
          <el-input v-model="loginForm.password" type="password" placeholder="请输入密码" show-password />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="goToRegister">去注册</el-button>
          <el-button type="primary" @click="handleLogin" :loading="loginLoading">登录</el-button>
        </span>
      </template>
    </el-dialog>

    <el-dialog
      v-model="showRegisterDialog"
      title="注册"
      width="400px"
      :close-on-click-modal="true"
      destroy-on-close
    >
      <el-form :model="registerForm" label-width="80px">
        <el-form-item label="用户名">
          <el-input v-model="registerForm.username" placeholder="请输入用户名" />
        </el-form-item>
        <el-form-item label="密码">
          <el-input v-model="registerForm.password" type="password" placeholder="请输入密码" show-password />
        </el-form-item>
        <el-form-item label="确认密码">
          <el-input v-model="registerForm.confirmPassword" type="password" placeholder="请再次输入密码" show-password />
        </el-form-item>
        <el-form-item label="昵称">
          <el-input v-model="registerForm.nickname" placeholder="请输入昵称（可选）" />
        </el-form-item>
        <el-form-item label="头像">
          <el-upload
            class="avatar-uploader"
            :show-file-list="false"
            :before-upload="beforeAvatarUpload"
            :http-request="handleAvatarUpload"
            accept="image/*"
          >
            <el-avatar v-if="registerForm.avatarUrl" :size="80" :src="registerForm.avatarUrl" />
            <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
          </el-upload>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="goToLogin">去登录</el-button>
          <el-button type="primary" @click="handleRegister" :loading="registerLoading">注册</el-button>
        </span>
      </template>
    </el-dialog>
  </header>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search, Bell, Star, Clock, Plus } from '@element-plus/icons-vue'
import { login, register, logout, uploadImage } from '@/api/user'
import md5 from 'blueimp-md5'

const searchKeyword = ref('')
const showLoginDialog = ref(false)
const showRegisterDialog = ref(false)
const loginLoading = ref(false)
const registerLoading = ref(false)

const loginForm = ref({
  username: '',
  password: ''
})

const registerForm = ref({
  username: '',
  password: '',
  confirmPassword: '',
  nickname: '',
  avatarUrl: ''
})

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
  if (!loginForm.value.username || !loginForm.value.password) {
    ElMessage.warning('请输入用户名和密码')
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
  showRegisterDialog.value = true
}

const goToLogin = () => {
  showRegisterDialog.value = false
  showLoginDialog.value = true
}

const beforeAvatarUpload = (file) => {
  const isImage = file.type.startsWith('image/')
  const isLt2M = file.size / 1024 / 1024 < 2

  if (!isImage) {
    ElMessage.error('只能上传图片文件!')
    return false
  }
  if (!isLt2M) {
    ElMessage.error('图片大小不能超过 2MB!')
    return false
  }
  return true
}

const handleAvatarUpload = async (options) => {
  const formData = new FormData()
  formData.append('file', options.file)
  formData.append('type', 'avatar')

  try {
    const res = await uploadImage(formData)
    if (res.code === 200) {
      registerForm.value.avatarUrl = res.data.url
      ElMessage.success('头像上传成功')
    } else {
      ElMessage.error(res.message || '头像上传失败')
    }
  } catch (error) {
    console.error('头像上传失败:', error)
    ElMessage.error(error.response?.data?.message || '头像上传失败，请重试')
  }
}

const handleRegister = async () => {
  if (!registerForm.value.username || !registerForm.value.password) {
    ElMessage.warning('请输入用户名和密码')
    return
  }

  if (registerForm.value.password !== registerForm.value.confirmPassword) {
    ElMessage.warning('两次输入的密码不一致')
    return
  }

  try {
    registerLoading.value = true
    const data = {
      username: registerForm.value.username,
      password: md5(registerForm.value.password)
    }
    
    if (registerForm.value.nickname) {
      data.nickname = registerForm.value.nickname
    }
    if (registerForm.value.avatarUrl) {
      data.avatarUrl = registerForm.value.avatarUrl
    }

    const res = await register(data)

    if (res.code === 200) {
      ElMessage.success('注册成功')
      showRegisterDialog.value = false
      
      registerForm.value = {
        username: '',
        password: '',
        confirmPassword: '',
        nickname: '',
        avatarUrl: ''
      }
      
      showLoginDialog.value = true
    } else {
      ElMessage.error(res.message || '注册失败')
    }
  } catch (error) {
    console.error('注册失败:', error)
    ElMessage.error(error.response?.data?.message || '注册失败，请重试')
  } finally {
    registerLoading.value = false
  }
}

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
  background: #2385bb;
  color: #fff;
  font-size: 14px;
  cursor: pointer;
  transition: background 0.2s;
}

.login-btn:hover {
  background: #1e75a6 !important;
}

.user-avatar-wrapper {
  cursor: pointer;
  border-radius: 50%;
  overflow: hidden;
}

.avatar-uploader {
  display: inline-block;
}

.avatar-uploader :deep(.el-avatar) {
  cursor: pointer;
}

.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 80px;
  height: 80px;
  text-align: center;
  line-height: 80px;
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  transition: border-color 0.2s;
}

.avatar-uploader-icon:hover {
  border-color: #2385bb;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
}
</style>
