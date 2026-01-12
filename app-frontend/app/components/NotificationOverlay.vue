<template>
  <div v-if="notificationStore.show" :class="['overlay', notificationStore.type]">
    <p>{{ notificationStore.message }}</p>
    <button @click="notificationStore.hideNotification()">Close</button>
  </div>
</template>

<script setup>
import { useNotificationStore } from '~/stores/notification';

const notificationStore = useNotificationStore();
</script>

<style scoped>
.overlay {
  position: fixed;
  top: 20px;
  right: 20px;
  padding: 1rem;
  border-radius: 5px;
  color: white;
  z-index: 1000;
  display: flex;
  justify-content: space-between;
  align-items: center;
  min-width: 250px;
}
.success {
  background-color: #4CAF50; /* Green */
}
.error {
  background-color: #f44336; /* Red */
}
p {
  margin: 0;
  margin-right: 1rem;
}
button {
  background: none;
  border: 1px solid white;
  color: white;
  border-radius: 3px;
  cursor: pointer;
}
</style>
