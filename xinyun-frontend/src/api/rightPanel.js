import request from './index'

export const getActiveUsers = () => {
  return new Promise((resolve) => {
    setTimeout(() => {
      resolve({
        code: 200,
        message: 'success',
        data: [
          {
            id: 1,
            nickname: '小明同学',
            avatar: 'https://i.pravatar.cc/48?img=68',
            isOnline: true,
            isFollowed: false
          },
          {
            id: 2,
            nickname: '小红',
            avatar: 'https://i.pravatar.cc/48?img=69',
            isOnline: true,
            isFollowed: true
          },
          {
            id: 3,
            nickname: '小刚',
            avatar: 'https://i.pravatar.cc/48?img=70',
            isOnline: true,
            isFollowed: false
          },
          {
            id: 4,
            nickname: '小美',
            avatar: 'https://i.pravatar.cc/48?img=71',
            isOnline: false,
            isFollowed: false
          },
          {
            id: 5,
            nickname: '小强',
            avatar: 'https://i.pravatar.cc/48?img=72',
            isOnline: true,
            isFollowed: true
          },
          {
            id: 6,
            nickname: '小丽',
            avatar: 'https://i.pravatar.cc/48?img=73',
            isOnline: true,
            isFollowed: false
          }
        ]
      })
    }, 300)
  })
}

export const followUser = (userId) => {
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

export const unfollowUser = (userId) => {
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

export const getHotTopics = () => {
  return new Promise((resolve) => {
    setTimeout(() => {
      resolve({
        code: 200,
        message: 'success',
        data: [
          {
            id: 1,
            title: '啊？这编剧的智商有超过过我吗？',
            heat: 9876,
            trend: 'up'
          },
          {
            id: 2,
            title: '日本国防官坠机后我潜到俄国大使馆',
            heat: 7654,
            trend: 'up'
          },
          {
            id: 3,
            title: '今天吃什么？全网最全美食推荐',
            heat: 5432,
            trend: 'down'
          },
          {
            id: 4,
            title: '程序员的一天：从早到晚都在写Bug',
            heat: 4321,
            trend: 'up'
          },
          {
            id: 5,
            title: '周末去哪玩？这些地方人少景美',
            heat: 3210,
            trend: 'stable'
          }
        ]
      })
    }, 300)
  })
}

export const getActiveBars = () => {
  return new Promise((resolve) => {
    setTimeout(() => {
      resolve({
        code: 200,
        message: 'success',
        data: [
          {
            id: 1,
            name: '孙笑川吧',
            avatar: 'https://i.pravatar.cc/48?img=12',
            memberCount: 125000,
            isFollowed: false
          },
          {
            id: 2,
            name: '航空母舰吧',
            avatar: 'https://i.pravatar.cc/48?img=14',
            memberCount: 89000,
            isFollowed: true
          },
          {
            id: 3,
            name: '美食分享吧',
            avatar: 'https://i.pravatar.cc/48?img=15',
            memberCount: 67000,
            isFollowed: false
          },
          {
            id: 4,
            name: '程序员日常吧',
            avatar: 'https://i.pravatar.cc/48?img=16',
            memberCount: 54000,
            isFollowed: false
          }
        ]
      })
    }, 300)
  })
}

export const followBar = (barId) => {
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

export const unfollowBar = (barId) => {
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
