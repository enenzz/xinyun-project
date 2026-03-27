<template>
  <div class="chat-window">
    <div class="chat-header">
      <div class="chat-user-info">
        <img :src="chatUser.avatar" :alt="chatUser.nickname" class="chat-user-avatar">
        <div class="chat-user-details">
          <span class="chat-user-name">{{ chatUser.nickname }}</span>
          <span class="chat-user-status">在线</span>
        </div>
      </div>
      <el-button circle size="small" @click="handleClose" class="close-btn">
        <el-icon><Close /></el-icon>
      </el-button>
    </div>

    <div class="chat-messages" ref="messagesRef">
      <div
        v-for="(msg, index) in messages"
        :key="index"
        class="message-item"
        :class="{ 'message-self': msg.isSelf }"
      >
        <img v-if="!msg.isSelf" :src="chatUser.avatar" :alt="chatUser.nickname" class="message-avatar">
        <div class="message-bubble">
          <span class="message-text">{{ msg.content }}</span>
        </div>
        <img v-if="msg.isSelf" :src="selfAvatar" alt="我" class="message-avatar">
      </div>
    </div>

    <div class="chat-input-wrapper">
      <div class="chat-input">
        <el-input
          v-model="inputText"
          type="textarea"
          :rows="2"
          placeholder="输入消息..."
          @keydown.enter="handleSend"
        />
      </div>
      <el-button type="primary" class="send-btn" @click="handleSend">
        发送
      </el-button>
    </div>
  </div>
</template>

<script setup>
import { ref, nextTick, watch } from 'vue'
import { Close } from '@element-plus/icons-vue'

const props = defineProps({
  chatUser: {
    type: Object,
    required: true
  }
})

const emit = defineEmits(['close'])

const messagesRef = ref(null)
const inputText = ref('')
const selfAvatar = ref('https://i.pravatar.cc/40?img=1')

const messages = ref([
  {
    content: '你好呀！',
    isSelf: false
  },
  {
    content: '你好，很高兴认识你！',
    isSelf: true
  },
  {
    content: '最近在忙什么呢？',
    isSelf: false
  }
])

const scrollToBottom = () => {
  nextTick(() => {
    if (messagesRef.value) {
      messagesRef.value.scrollTop = messagesRef.value.scrollHeight
    }
  })
}

const handleSend = () => {
  if (!inputText.value.trim()) {
    messages.value.push({
      content: inputText.value,
      isSelf: true
    })
    inputText.value = ''
    scrollToBottom()

    setTimeout(() => {
      const replies = [
        '收到！',
        '好的，我知道了',
        '嗯嗯',
        '真的吗？',
        '好有趣！',
        '没问题'
      ]
      messages.value.push({
        content: replies[Math.floor(Math.random() * replies.length)],
        isSelf: false
      })
      scrollToBottom()
    }, 1000)
  }
}

const handleClose = () => {
  emit('close')
}

watch(() => props.chatUser, () => {
  inputText.value = ''
  scrollToBottom()
}, { immediate: true })
</script>

<style scoped>
.chat-window {
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
  background: #ffffff;
  border-radius: 16px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
  overflow: hidden;
}

.chat-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px 20px;
  border-bottom: 1px solid #f0f0f0;
  flex-shrink: 0;
}

.chat-user-info {
  display: flex;
  align-items: center;
  gap: 12px;
}

.chat-user-avatar {
  width: 44px;
  height: 44px;
  border-radius: 12px;
  object-fit: cover;
}

.chat-user-details {
  display: flex;
  flex-direction: column;
  gap: 2px;
}

.chat-user-name {
  font-size: 15px;
  font-weight: 600;
  color: #333333;
}

.chat-user-status {
  font-size: 12px;
  color: #52c41a;
}

.close-btn {
  background: #f5f5f5;
  border: none;
  color: #666666;
}

.close-btn:hover {
  background: #e8e8e8;
  color: #333333;
}

.chat-messages {
  flex: 1;
  overflow-y: auto;
  padding: 16px 20px;
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.chat-messages::-webkit-scrollbar {
  width: 4px;
}

.chat-messages::-webkit-scrollbar-track {
  background: transparent;
}

.chat-messages::-webkit-scrollbar-thumb {
  background: rgba(0, 0, 0, 0.1);
  border-radius: 2px;
}

.message-item {
  display: flex;
  align-items: flex-start;
  gap: 10px;
}

.message-item.message-self {
  flex-direction: row-reverse;
}

.message-avatar {
  width: 36px;
  height: 36px;
  border-radius: 10px;
  object-fit: cover;
  flex-shrink: 0;
}

.message-bubble {
  max-width: 70%;
  padding: 10px 14px;
  background: #f5f5f5;
  border-radius: 12px;
  border-top-left-radius: 4px;
}

.message-item.message-self .message-bubble {
  background: linear-gradient(135deg, #98E4D0, #87CEEB);
  border-radius: 12px;
  border-top-right-radius: 4px;
  border-top-left-radius: 12px;
}

.message-text {
  font-size: 14px;
  color: #333333;
  line-height: 1.5;
}

.message-item.message-self .message-text {
  color: #ffffff;
}

.chat-input-wrapper {
  padding: 16px 20px;
  border-top: 1px solid #f0f0f0;
  flex-shrink: 0;
  display: flex;
  gap: 12px;
  align-items: flex-end;
}

.chat-input {
  flex: 1;
}

.chat-input :deep(.el-textarea__inner) {
  border-radius: 12px;
  resize: none;
}

.send-btn {
  background: linear-gradient(135deg, #98E4D0, #87CEEB);
  border: none;
  border-radius: 12px;
  padding: 0 20px;
  height: 72px;
  font-weight: 600;
}

.send-btn:hover {
  background: linear-gradient(135deg, #7dd4c0, #72c4e0);
}
</style>
