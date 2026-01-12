import { defineStore } from 'pinia';

export const useNotificationStore = defineStore('notification', {
  state: () => ({
    message: '',
    type: '', // 'success' or 'error'
    show: false,
  }),
  actions: {
    showNotification(message, type = 'success') {
      this.message = message;
      this.type = type;
      this.show = true;

      setTimeout(() => {
        this.hideNotification();
      }, 5000);
    },
    hideNotification() {
      this.show = false;
      this.message = '';
      this.type = '';
    },
  },
});
