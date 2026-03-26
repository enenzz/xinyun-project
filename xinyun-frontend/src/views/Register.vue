<template>
  <div class="register-container">
    <div class="left-section">
      <div class="left-content">
        <h1 class="brand-title">心云</h1>
        <p class="brand-subtitle">让相遇更简单</p>
      </div>
    </div>
    
    <div class="right-section">
      <div class="form-card">
        <div class="form-header">
          <h2 class="form-title">欢迎加入心云</h2>
          <span class="form-link" @click="goToLogin">已有账号？去登录</span>
        </div>
        
        <el-form 
          :model="registerForm" 
          :rules="registerRules" 
          ref="registerFormRef" 
          class="register-form"
        >
          <el-form-item prop="username">
            <el-input 
              v-model="registerForm.username" 
              placeholder="请设置用户名"
              size="large"
              prefix-icon="User"
            />
          </el-form-item>
          
          <el-form-item prop="phone">
            <el-input 
              v-model="registerForm.phone" 
              placeholder="请输入手机号"
              size="large"
              prefix-icon="Phone"
            />
          </el-form-item>
          
          <el-form-item prop="password">
            <el-input 
              v-model="registerForm.password" 
              type="password" 
              placeholder="请设置密码（6-20位）"
              size="large"
              prefix-icon="Lock"
              show-password
            />
          </el-form-item>
          
          <el-form-item prop="confirmPassword">
            <el-input 
              v-model="registerForm.confirmPassword" 
              type="password" 
              placeholder="请再次输入密码"
              size="large"
              prefix-icon="Lock"
              show-password
            />
          </el-form-item>
          
          <el-form-item prop="captcha">
            <div class="captcha-wrapper">
              <el-input 
                v-model="registerForm.captcha" 
                placeholder="请输入验证码"
                size="large"
                prefix-icon="Key"
                class="captcha-input"
              />
              <el-button 
                type="primary" 
                size="large"
                class="captcha-btn"
                :disabled="countdown > 0"
                @click="handleGetCaptcha"
              >
                {{ countdown > 0 ? `${countdown}s` : '获取验证码' }}
              </el-button>
            </div>
          </el-form-item>
          
          <el-form-item prop="avatar">
            <div class="avatar-upload-label">头像上传（可选）</div>
            <el-upload
              class="avatar-uploader"
              :show-file-list="false"
              :before-upload="beforeAvatarUpload"
              :http-request="handleAvatarUpload"
              accept="image/*"
            >
              <el-avatar v-if="registerForm.avatarUrl" :size="100" :src="registerForm.avatarUrl" />
              <div v-else class="avatar-placeholder">
                <el-icon class="avatar-icon"><Plus /></el-icon>
                <span class="avatar-text">点击上传</span>
              </div>
            </el-upload>
          </el-form-item>
          
          <el-form-item prop="agreement">
            <el-checkbox v-model="registerForm.agreement">
              我已阅读并同意
              <span class="agreement-link">《用户服务协议》</span>
              和
              <span class="agreement-link">《隐私政策》</span>
            </el-checkbox>
          </el-form-item>
          
          <el-form-item>
            <el-button 
              type="primary" 
              size="large" 
              class="register-submit-btn"
              @click="handleRegister"
              :loading="registerLoading"
            >
              立即注册
            </el-button>
          </el-form-item>
        </el-form>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { User, Lock, Phone, Key, Plus } from '@element-plus/icons-vue'
import { register, uploadImage } from '@/api/user'
import md5 from 'blueimp-md5'

const router = useRouter()
const registerFormRef = ref(null)
const registerLoading = ref(false)
const countdown = ref(0)
let countdownTimer = null

const validateConfirmPassword = (rule, value, callback) => {
  if (value === '') {
    callback(new Error('请再次输入密码'))
  } else if (value !== registerForm.password) {
    callback(new Error('两次输入的密码不一致'))
  } else {
    callback()
  }
}

const validateAgreement = (rule, value, callback) => {
  if (!value) {
    callback(new Error('请先同意用户协议'))
  } else {
    callback()
  }
}

const validatePhone = (rule, value, callback) => {
  const phoneReg = /^1[3-9]\d{9}$/
  if (!value) {
    callback(new Error('请输入手机号'))
  } else if (!phoneReg.test(value)) {
    callback(new Error('请输入正确的手机号'))
  } else {
    callback()
  }
}

const registerForm = reactive({
  username: '',
  phone: '',
  password: '',
  confirmPassword: '',
  captcha: '',
  avatarUrl: '',
  agreement: false
})

const registerRules = {
  username: [
    { required: true, message: '请设置用户名', trigger: 'blur' },
    { min: 4, max: 16, message: '用户名长度为4-16位', trigger: 'blur' }
  ],
  phone: [
    { required: true, validator: validatePhone, trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请设置密码', trigger: 'blur' },
    { min: 6, max: 20, message: '密码长度为6-20位', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, validator: validateConfirmPassword, trigger: 'blur' }
  ],
  captcha: [
    { required: true, message: '请输入验证码', trigger: 'blur' }
  ],
  agreement: [
    { required: true, validator: validateAgreement, trigger: 'change' }
  ]
}

const handleGetCaptcha = () => {
  if (!registerForm.phone) {
    ElMessage.warning('请先输入手机号')
    return
  }
  countdown.value = 60
  countdownTimer = setInterval(() => {
    countdown.value--
    if (countdown.value <= 0) {
      clearInterval(countdownTimer)
    }
  }, 1000)
  ElMessage.success('验证码已发送')
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
      registerForm.avatarUrl = res.data.url
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
  if (!registerFormRef.value) return
  
  try {
    await registerFormRef.value.validate()
  } catch {
    return
  }

  try {
    registerLoading.value = true
    const data = {
      username: registerForm.username,
      password: md5(registerForm.password)
    }
    
    if (registerForm.avatarUrl) {
      data.avatarUrl = registerForm.avatarUrl
    }

    const res = await register(data)

    if (res.code === 200) {
      ElMessage.success('注册成功，请登录')
      setTimeout(() => {
        router.push('/')
      }, 1000)
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

const goToLogin = () => {
  router.push('/')
}
</script>

<style scoped>
.register-container {
  width: 100vw;
  height: 100vh;
  display: flex;
  overflow: hidden;
}

.left-section {
  width: 60%;
  height: 100%;
  position: relative;
  background-image: url('@/assets/images/register-bg.jpg');
  background-size: cover;
  background-position: center;
}

.left-section::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: linear-gradient(135deg, rgba(99, 102, 241, 0.8), rgba(139, 92, 246, 0.8));
}

.left-content {
  position: relative;
  z-index: 1;
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
}

.brand-title {
  font-size: 48px;
  font-weight: 700;
  color: #fff;
  margin: 0 0 12px 0;
  letter-spacing: 4px;
}

.brand-subtitle {
  font-size: 20px;
  color: rgba(255, 255, 255, 0.9);
  margin: 0;
  letter-spacing: 2px;
}

.right-section {
  width: 40%;
  height: 100%;
  background: #fff;
  display: flex;
  justify-content: center;
  align-items: center;
  overflow-y: auto;
  padding: 40px 0;
}

.form-card {
  width: 420px;
  background: #fff;
  border-radius: 16px;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.1);
  padding: 40px 36px;
}

.form-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 32px;
}

.form-title {
  font-size: 28px;
  font-weight: 700;
  color: #111827;
  margin: 0;
}

.form-link {
  font-size: 14px;
  color: #6366f1;
  cursor: pointer;
  font-weight: 500;
  transition: color 0.2s;
}

.form-link:hover {
  color: #4f46e5;
}

.register-form :deep(.el-form-item) {
  margin-bottom: 20px;
}

.register-form :deep(.el-input__wrapper) {
  border-radius: 12px;
  padding: 12px 16px;
  transition: all 0.3s;
}

.register-form :deep(.el-input__wrapper:hover) {
  box-shadow: 0 0 0 1px #d1d5db;
}

.register-form :deep(.el-input__wrapper.is-focus) {
  box-shadow: 0 0 0 2px rgba(99, 102, 241, 0.2);
}

.captcha-wrapper {
  display: flex;
  gap: 12px;
}

.captcha-input {
  flex: 1;
}

.captcha-btn {
  width: 140px;
  flex-shrink: 0;
  border-radius: 12px;
  background: linear-gradient(135deg, #6366f1, #8b5cf6);
  border: none;
  font-weight: 500;
}

.captcha-btn:hover:not(:disabled) {
  background: linear-gradient(135deg, #4f46e5, #7c3aed) !important;
}

.captcha-btn:disabled {
  background: #e5e7eb;
  color: #9ca3af;
}

.avatar-upload-label {
  font-size: 14px;
  color: #6b7280;
  margin-bottom: 12px;
}

.avatar-uploader {
  display: inline-block;
}

.avatar-placeholder {
  width: 100px;
  height: 100px;
  border-radius: 50%;
  border: 2px dashed #d1d5db;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  cursor: pointer;
  transition: all 0.3s;
}

.avatar-placeholder:hover {
  border-color: #6366f1;
}

.avatar-icon {
  font-size: 32px;
  color: #9ca3af;
  margin-bottom: 4px;
}

.avatar-text {
  font-size: 12px;
  color: #9ca3af;
}

.agreement-link {
  color: #6366f1;
  cursor: pointer;
  font-weight: 500;
}

.agreement-link:hover {
  color: #4f46e5;
}

.register-submit-btn {
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

.register-submit-btn:hover {
  background: linear-gradient(135deg, #4f46e5, #7c3aed) !important;
  transform: translateY(-2px);
  box-shadow: 0 8px 20px rgba(99, 102, 241, 0.5);
}
</style>
