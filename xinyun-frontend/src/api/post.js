import request from './index'

// 获取动态列表
export const getPostList = (params) => {
  // TODO: 对接后端时取消注释，移除模拟数据
  // return request.get('/post/list', { params })
  
  // 模拟数据
  return new Promise((resolve) => {
    setTimeout(() => {
      resolve({
        code: 200,
        message: 'success',
        data: {
          list: [
            {
              id: 1,
              userId: 2,
              username: '小红',
              avatar: 'https://api.dicebear.com/7.x/avataaars/svg?seed=xiaohong',
              content: '今天天气真好，出来逛街啦~',
              images: [
                'https://picsum.photos/400/300?random=1',
                'https://picsum.photos/400/300?random=2'
              ],
              location: '上海市浦东新区',
              createTime: '2024-03-25 14:30:00',
              likeCount: 128,
              commentCount: 32,
              isLiked: false
            },
            {
              id: 2,
              userId: 3,
              username: '小明',
              avatar: 'https://api.dicebear.com/7.x/avataaars/svg?seed=xiaoming',
              content: '打卡广州小蛮腰，夜景真的太美了！',
              images: [
                'https://picsum.photos/400/300?random=3'
              ],
              location: '广州市天河区',
              createTime: '2024-03-25 12:15:00',
              likeCount: 256,
              commentCount: 48,
              isLiked: true
            },
            {
              id: 3,
              userId: 4,
              username: '小芳',
              avatar: 'https://api.dicebear.com/7.x/avataaars/svg?seed=xiaofang',
              content: '周末宅在家看电影，推荐一部好片~',
              images: [],
              location: '深圳市南山区',
              createTime: '2024-03-24 20:45:00',
              likeCount: 89,
              commentCount: 15,
              isLiked: false
            }
          ],
          total: 100,
          page: params?.page || 1,
          pageSize: params?.pageSize || 10
        }
      })
    }, 500)
  })
}

// 点赞
export const likePost = (postId) => {
  // TODO: 对接后端时取消注释，移除模拟数据
  // return request.post('/post/like', { postId })
  
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

// 取消点赞
export const unlikePost = (postId) => {
  // TODO: 对接后端时取消注释，移除模拟数据
  // return request.post('/post/unlike', { postId })
  
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
