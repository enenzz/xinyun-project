import request from './index'

// 登录
export const login = (data) => {
  return request.post('/login', data)
}

// 注册
export const register = (data) => {
  return request.post('/login/register', data)
}

// 上传图片
export const uploadImage = (formData) => {
  return request.post('/common/upload/image', formData, {
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}

// 获取验证码
export const getCaptcha = (username) => {
  // TODO: 对接后端时取消注释，移除模拟数据
  // return request.get('/login/captcha', { params: { username } })
  
  // 模拟数据
  return new Promise((resolve) => {
    setTimeout(() => {
      resolve({
        code: 200,
        message: 'success',
        data: '123456'
      })
    }, 300)
  })
}

// 退出登录
export const logout = () => {
  // TODO: 对接后端时取消注释，移除模拟数据
  // return request.post('/login/logout')
  
  // 模拟数据
  return new Promise((resolve) => {
    setTimeout(() => {
      resolve({
        code: 200,
        message: 'success',
        data: null
      })
    }, 200)
  })
}

// 刷新 Token
export const refreshToken = () => {
  // TODO: 对接后端时取消注释，移除模拟数据
  // return request.post('/login/refresh')
  
  // 模拟数据
  return new Promise((resolve) => {
    setTimeout(() => {
      resolve({
        code: 200,
        message: 'success',
        data: {
          token: 'mock-token-' + Date.now(),
          expiresIn: 1800
        }
      })
    }, 200)
  })
}

// 获取用户信息
export const getUserProfile = () => {
  // TODO: 对接后端时取消注释，移除模拟数据
  // return request.get('/user/profile')
  
  // 模拟数据
  return new Promise((resolve) => {
    setTimeout(() => {
      resolve({
        code: 200,
        message: 'success',
        data: {
          id: 1,
          username: 'admin',
          nickname: '心云用户',
          avatar: 'https://api.dicebear.com/7.x/avataaars/svg?seed=admin',
          gender: 1,
          age: 25,
          phone: '138****8000',
          city: '北京'
        }
      })
    }, 300)
  })
}

// 获取推荐用户
export const getRecommendUsers = () => {
  // TODO: 对接后端时取消注释，移除模拟数据
  // return request.get('/user/recommend')
  
  // 模拟数据
  return new Promise((resolve) => {
    setTimeout(() => {
      resolve({
        code: 200,
        message: 'success',
        data: [
          {
            id: 2,
            username: 'xiaohong',
            nickname: '小红',
            avatar: 'https://api.dicebear.com/7.x/avataaars/svg?seed=xiaohong',
            city: '上海'
          },
          {
            id: 3,
            username: 'xiaoming',
            nickname: '小明',
            avatar: 'https://api.dicebear.com/7.x/avataaars/svg?seed=xiaoming',
            city: '广州'
          },
          {
            id: 4,
            username: 'xiaofang',
            nickname: '小芳',
            avatar: 'https://api.dicebear.com/7.x/avataaars/svg?seed=xiaofang',
            city: '深圳'
          }
        ]
      })
    }, 400)
  })
}
