import request from './index'

export const getPostList = (params) => {
  return new Promise((resolve) => {
    setTimeout(() => {
      resolve({
        code: 200,
        message: 'success',
        data: {
          list: [
            {
              id: 1,
              barName: '孙笑川吧',
              barAvatar: 'https://i.pravatar.cc/48?img=12',
              followers: 752,
              posts: 22.3,
              title: '啊？这编剧的智商有超过过我吗？',
              video: true,
              thumbnail: 'https://picsum.photos/480/240?random=1',
              duration: '00:12/48',
              replyCount: 471,
              viewCount: 976,
              shareCount: 7659,
              createTime: '1小时前'
            },
            {
              id: 2,
              barName: '航空母舰吧',
              barAvatar: 'https://i.pravatar.cc/48?img=14',
              followers: 168,
              posts: 2.8,
              title: '日本国防官坠机后我潜到俄国大使馆',
              images: [],
              replyCount: 63,
              viewCount: 13,
              shareCount: 40,
              createTime: '2小时前'
            },
            {
              id: 3,
              barName: '奇迹MU全...吧',
              barAvatar: 'https://i.pravatar.cc/48?img=15',
              followers: 4,
              posts: 8.5,
              title: '小贺娜4属性卓越，1小时上600级，登录送4代翅膀！',
              images: [
                'https://picsum.photos/160/120?random=2'
              ],
              replyCount: 0,
              viewCount: 0,
              shareCount: 0,
              createTime: '广告'
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

export const likePost = (postId) => {
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

export const unlikePost = (postId) => {
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
