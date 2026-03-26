<template>
  <!-- 【样式修改区域开始】全屏背景容器 -->
  <div class="register-container" :style="bgStyle">
    <!-- 【样式修改区域】表单水平垂直居中 -->
    <div class="form-wrapper">
      <div class="form-card">
        <!-- 表单头部 -->
        <div class="form-header">
          <h2 class="form-title">欢迎加入心云</h2>
          <span class="form-link" @click="goToLogin">已有账号？去登录</span>
        </div>
        
        <!-- 【布局控制】展开/收起按钮（始终可见） -->
        <div class="toggle-btn-wrapper">
          <div class="toggle-btn" @click="toggleOptional">
            <span>{{ isShowOptional ? '收起选填信息' : '填写更多选填信息' }}</span>
            <el-icon class="toggle-icon" :class="{ 'rotated': isShowOptional }">
              <ArrowRight />
            </el-icon>
          </div>
        </div>
        
        <el-form 
          :model="registerForm" 
          :rules="registerRules" 
          ref="registerFormRef" 
          class="register-form"
          label-position="top"
        >
          <!-- 【表单布局区域】横向展开布局 -->
          <div class="form-content">
            <!-- 左侧：必填项区域（固定展示） -->
            <div class="form-left">
              <div class="section-title">必填信息</div>
              
              <!-- 【接口字段：username】用户名 - 必填 -->
              <el-form-item prop="username" label="用户名">
                <el-input 
                  v-model="registerForm.username" 
                  placeholder="请设置用户名"
                  size="large"
                  prefix-icon="User"
                />
              </el-form-item>
              
              <!-- 【接口字段：password】密码 - 必填 -->
              <el-form-item prop="password" label="密码">
                <el-input 
                  v-model="registerForm.password" 
                  type="password" 
                  placeholder="请设置6-20位密码"
                  size="large"
                  prefix-icon="Lock"
                  show-password
                />
              </el-form-item>
              
              <!-- 【前端校验字段】确认密码 - 必填（不传递给后端） -->
              <el-form-item prop="confirmPassword" label="确认密码">
                <el-input 
                  v-model="registerForm.confirmPassword" 
                  type="password" 
                  placeholder="请再次输入密码"
                  size="large"
                  prefix-icon="Lock"
                  show-password
                />
              </el-form-item>
              
              <!-- 用户协议 -->
              <el-form-item prop="agreement">
                <el-checkbox v-model="registerForm.agreement">
                  我已阅读并同意
                  <span class="agreement-link">《用户服务协议》</span>
                  和
                  <span class="agreement-link">《隐私政策》</span>
                </el-checkbox>
              </el-form-item>
              
              <!-- 注册按钮 -->
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
            </div>
            
            <!-- 【修复关键】右侧：选填项区域（v-show控制显示/隐藏） -->
            <div class="form-right" v-show="isShowOptional">
              <div class="section-title">选填信息</div>
              
              <!-- 【接口字段：nickname】昵称 - 选填 -->
              <el-form-item prop="nickname" label="昵称（选填）">
                <el-input 
                  v-model="registerForm.nickname" 
                  placeholder="请设置昵称（选填）"
                  size="large"
                  prefix-icon="UserFilled"
                />
              </el-form-item>
              
              <!-- 【接口字段：avatarUrl】头像 - 选填 -->
              <el-form-item prop="avatar" label="头像（选填）">
                <div class="avatar-upload-wrapper">
                  <el-upload
                    class="avatar-uploader"
                    :show-file-list="false"
                    :before-upload="beforeAvatarUpload"
                    :http-request="handleAvatarUpload"
                    accept="image/*"
                  >
                    <el-avatar v-if="registerForm.avatarUrl" :size="80" :src="registerForm.avatarUrl" />
                    <div v-else class="avatar-placeholder">
                      <el-icon class="avatar-icon"><Plus /></el-icon>
                      <span class="avatar-text">上传</span>
                    </div>
                  </el-upload>
                  <div class="avatar-tips">
                    <span>JPG/PNG/GIF，≤2MB</span>
                  </div>
                </div>
              </el-form-item>
              
              <!-- 【接口字段：gender】性别 - 选填 -->
              <el-form-item prop="gender" label="性别（选填）">
                <el-radio-group v-model="registerForm.gender" size="large">
                  <el-radio :value="0">未知</el-radio>
                  <el-radio :value="1">男</el-radio>
                  <el-radio :value="2">女</el-radio>
                </el-radio-group>
              </el-form-item>
              
              <!-- 【接口字段：birthday】生日 - 选填 -->
              <el-form-item prop="birthday" label="生日（选填）">
                <el-date-picker
                  v-model="registerForm.birthday"
                  type="date"
                  placeholder="选择生日（选填）"
                  format="YYYY-MM-DD"
                  value-format="YYYY-MM-DD"
                  size="large"
                  style="width: 100%"
                />
              </el-form-item>
              
              <!-- 【接口字段：phone】手机号 - 选填 -->
              <el-form-item prop="phone" label="手机号（选填）">
                <el-input 
                  v-model="registerForm.phone" 
                  placeholder="请输入手机号（选填）"
                  size="large"
                  prefix-icon="Phone"
                />
              </el-form-item>
              
              <!-- 【前端可选字段】验证码 - 选填（不传递给后端） -->
              <el-form-item prop="captcha" label="验证码（选填）">
                <div class="captcha-wrapper">
                  <el-input 
                    v-model="registerForm.captcha" 
                    placeholder="验证码（选填）"
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
                    {{ countdown > 0 ? `${countdown}s` : '获取' }}
                  </el-button>
                </div>
              </el-form-item>
              
              <!-- 【接口字段：province/city/district】所在地 - 选填 -->
              <el-form-item prop="region" label="所在地（选填）">
                <el-cascader
                  v-model="registerForm.region"
                  :options="regionOptions"
                  placeholder="选择省/市/区（选填）"
                  size="large"
                  style="width: 100%"
                  @change="handleRegionChange"
                />
              </el-form-item>
            </div>
          </div>
        </el-form>
      </div>
    </div>
  </div>
</template>

<script setup>
// 【功能代码区域：完全不修改】
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { User, Lock, Phone, Key, Plus, UserFilled, ArrowRight } from '@element-plus/icons-vue'
import { register, uploadImage, getCaptcha } from '@/api/user'
import md5 from 'blueimp-md5'
// 【关键位置】引入本地背景图片，请将 register-bg.jpg 放入 src/assets/images/ 目录
import registerBg from '@/assets/images/register-bg.jpg'

const router = useRouter()
const registerFormRef = ref(null)
const registerLoading = ref(false)
const countdown = ref(0)
let countdownTimer = null

// 【修复关键】选填信息展开/收起状态 - 使用更明确的命名
const isShowOptional = ref(false)

// 【修复关键】切换展开/收起状态
const toggleOptional = () => {
  isShowOptional.value = !isShowOptional.value
}

// 【样式修改相关】全屏背景图样式
const bgStyle = reactive({
  backgroundImage: `url(${registerBg})`
})

// 省市区模拟数据
const regionOptions = [
  {
    value: '北京市',
    label: '北京市',
    children: [
      {
        value: '北京市',
        label: '北京市',
        children: [
          { value: '东城区', label: '东城区' },
          { value: '西城区', label: '西城区' },
          { value: '朝阳区', label: '朝阳区' },
          { value: '海淀区', label: '海淀区' }
        ]
      }
    ]
  },
  {
    value: '上海市',
    label: '上海市',
    children: [
      {
        value: '上海市',
        label: '上海市',
        children: [
          { value: '黄浦区', label: '黄浦区' },
          { value: '徐汇区', label: '徐汇区' },
          { value: '长宁区', label: '长宁区' },
          { value: '静安区', label: '静安区' }
        ]
      }
    ]
  }
]

// 校验规则：确认密码
const validateConfirmPassword = (rule, value, callback) => {
  if (value === '') {
    callback(new Error('请再次输入密码'))
  } else if (value !== registerForm.password) {
    callback(new Error('两次输入的密码不一致'))
  } else {
    callback()
  }
}

// 校验规则：用户协议
const validateAgreement = (rule, value, callback) => {
  if (!value) {
    callback(new Error('请先同意用户协议'))
  } else {
    callback()
  }
}

// 校验规则：手机号
const validatePhone = (rule, value, callback) => {
  if (!value) {
    callback() // 可选字段，不填直接通过
  } else {
    const phoneReg = /^1[3-9]\d{9}$/
    if (!phoneReg.test(value)) {
      callback(new Error('请输入正确的手机号'))
    } else {
      callback()
    }
  }
}

// 校验规则：昵称
const validateNickname = (rule, value, callback) => {
  if (!value) {
    callback() // 可选字段，不填直接通过
  } else {
    if (value.length < 1 || value.length > 20) {
      callback(new Error('昵称长度为1-20位'))
    } else {
      callback()
    }
  }
}

// 注册表单数据
const registerForm = reactive({
  username: '',
  password: '',
  confirmPassword: '',
  phone: '',
  captcha: '',
  nickname: '',
  avatarUrl: '',
  gender: 0,
  birthday: '',
  region: [],
  province: '',
  city: '',
  district: '',
  agreement: false
})

// 注册表单校验规则
const registerRules = {
  username: [
    { required: true, message: '请设置用户名', trigger: 'blur' },
    { min: 4, max: 16, message: '用户名长度为4-16位', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请设置密码', trigger: 'blur' },
    { min: 6, max: 20, message: '密码长度为6-20位', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, validator: validateConfirmPassword, trigger: 'blur' }
  ],
  phone: [
    { validator: validatePhone, trigger: 'blur' }
  ],
  nickname: [
    { validator: validateNickname, trigger: 'blur' }
  ],
  captcha: [
    // 可选字段，不设置必填校验
  ],
  agreement: [
    { required: true, validator: validateAgreement, trigger: 'change' }
  ]
}

// 处理省市区选择
const handleRegionChange = (value) => {
  if (value && value.length >= 1) {
    registerForm.province = value[0]
  }
  if (value && value.length >= 2) {
    registerForm.city = value[1]
  }
  if (value && value.length >= 3) {
    registerForm.district = value[2]
  }
}

// 获取验证码
const handleGetCaptcha = async () => {
  if (!registerForm.phone) {
    ElMessage.warning('请先输入手机号')
    return
  }
  
  try {
    const res = await getCaptcha(registerForm.phone)
    if (res.code === 200) {
      ElMessage.success('验证码已发送')
      // 启动倒计时
      countdown.value = 60
      countdownTimer = setInterval(() => {
        countdown.value--
        if (countdown.value <= 0) {
          clearInterval(countdownTimer)
        }
      }, 1000)
    } else {
      ElMessage.error(res.message || '获取验证码失败')
    }
  } catch (error) {
    console.error('获取验证码失败:', error)
    ElMessage.error(error.response?.data?.message || '获取验证码失败，请重试')
  }
}

// 头像上传前校验
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

// 头像上传处理
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

// 注册提交
const handleRegister = async () => {
  if (!registerFormRef.value) return
  
  try {
    await registerFormRef.value.validate()
  } catch {
    return
  }

  try {
    registerLoading.value = true
    
    // 【关键位置】整理注册接口数据，严格对齐接口文档
    const data = {
      username: registerForm.username,
      password: md5(registerForm.password)
    }
    
    // 可选字段，有值才添加
    if (registerForm.nickname) {
      data.nickname = registerForm.nickname
    }
    if (registerForm.avatarUrl) {
      data.avatarUrl = registerForm.avatarUrl
    }
    if (registerForm.gender !== undefined && registerForm.gender !== null) {
      data.gender = registerForm.gender
    }
    if (registerForm.birthday) {
      data.birthday = registerForm.birthday
    }
    if (registerForm.phone) {
      data.phone = registerForm.phone
    }
    if (registerForm.province) {
      data.province = registerForm.province
    }
    if (registerForm.city) {
      data.city = registerForm.city
    }
    if (registerForm.district) {
      data.district = registerForm.district
    }

    // 【关键位置】调用后端注册接口 POST /api/login/register
    const res = await register(data)

    if (res.code === 200) {
      ElMessage.success('注册成功，请登录')
      // 延迟1秒后跳转到首页，携带 openLogin=true 参数
      setTimeout(() => {
        router.push({
          path: '/',
          query: { openLogin: 'true' }
        })
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

// 去登录
const goToLogin = () => {
  router.push({
    path: '/',
    query: { openLogin: 'true' }
  })
}
</script>

<style scoped>
/* 【样式修改区域开始】全屏背景图容器 */
.register-container {
  width: 100vw;
  height: 100vh;
  position: relative;
  overflow: hidden;
  background-size: cover;
  background-position: center;
  background-repeat: no-repeat;
}

/* 【样式修改区域】表单水平垂直居中 */
.form-wrapper {
  width: 100%;
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
}

/* 【样式修改区域】表单卡片 - 半透明白色背景 */
.form-card {
  background: rgba(255, 255, 255, 0.8);
  border-radius: 16px;
  padding: 40px 36px;
  display: flex;
  flex-direction: column;
}

/* 表单头部 */
.form-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
}

.form-title {
  font-size: 28px;
  font-weight: 700;
  color: #1f2937;
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

/* 【修复关键】展开/收起按钮（始终可见，不受折叠影响） */
.toggle-btn-wrapper {
  margin-bottom: 20px;
}

.toggle-btn {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  padding: 10px 20px;
  background: linear-gradient(135deg, #6366f1, #8b5cf6);
  color: #fff;
  border-radius: 12px;
  cursor: pointer;
  font-size: 14px;
  font-weight: 500;
  transition: all 0.3s;
}

.toggle-btn:hover {
  background: linear-gradient(135deg, #4f46e5, #7c3aed);
  transform: translateY(-1px);
}

.toggle-icon {
  font-size: 16px;
  transition: transform 0.3s;
}

.toggle-icon.rotated {
  transform: rotate(90deg);
}

/* 【布局核心】表单内容区域 - 横向布局 */
.form-content {
  display: flex;
  gap: 32px;
}

/* 左侧必填项区域 */
.form-left {
  width: 320px;
  flex-shrink: 0;
}

/* 【修复关键】右侧选填项区域 - 默认完全隐藏，展开时显示 */
.form-right {
  width: 320px;
  flex-shrink: 0;
  display: none;  /* 默认隐藏 */
}

.form-right[v-show="true"],
.form-right:not([v-show]) {
  display: block; /* 展开时显示 */
}

/* 分区标题 */
.section-title {
  font-size: 14px;
  font-weight: 600;
  color: #374151;
  margin-bottom: 16px;
  padding-left: 8px;
  border-left: 3px solid #6366f1;
}

/* 表单项样式 */
.register-form :deep(.el-form-item) {
  margin-bottom: 20px;
}

.register-form :deep(.el-form-item__label) {
  font-size: 14px;
  font-weight: 500;
  color: #374151;
  padding-bottom: 6px;
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
  box-shadow: 0 0 0 2px rgba(99, 102, 241, 0.3);
}

/* 验证码 */
.captcha-wrapper {
  display: flex;
  gap: 12px;
}

.captcha-input {
  flex: 1;
}

.captcha-btn {
  width: 80px;
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

/* 头像上传 */
.avatar-upload-wrapper {
  width: 100%;
}

.avatar-uploader {
  display: inline-block;
}

.avatar-placeholder {
  width: 80px;
  height: 80px;
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
  font-size: 24px;
  color: #9ca3af;
  margin-bottom: 4px;
}

.avatar-text {
  font-size: 12px;
  color: #9ca3af;
}

.avatar-tips {
  margin-top: 8px;
  font-size: 12px;
  color: #9ca3af;
}

/* 用户协议 */
.agreement-link {
  color: #6366f1;
  cursor: pointer;
  font-weight: 500;
}

.agreement-link:hover {
  color: #4f46e5;
}

.register-form :deep(.el-checkbox__label) {
  color: #374151;
}

.register-form :deep(.el-radio__label) {
  color: #374151;
}

.register-form :deep(.el-cascader) {
  width: 100%;
}

.register-form :deep(.el-date-editor) {
  width: 100%;
}

/* 注册按钮 */
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
