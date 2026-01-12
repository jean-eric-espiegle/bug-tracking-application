<template>
  <div>
    <h1>Login</h1>
    <form @submit.prevent="handleLogin">
      <div>
        <label for="username">Username</label>
        <input type="text" id="username" v-model="authStore.loginForm.username" />
      </div>
      <div>
        <label for="password">Password</label>
        <input type="password" id="password" v-model="authStore.loginForm.password" />
      </div>
      <button type="submit">Login</button>
    </form>
    <p>
      Don't have an account? <NuxtLink to="/register">Register</NuxtLink>
    </p>
  </div>
</template>

<script setup>
import { useAuthStore } from '~/stores/auth';
import { useRouter } from 'vue-router';

const authStore = useAuthStore();
const router = useRouter();

const handleLogin = async () => {
  const result = await authStore.login();
  if (result.success) {
    router.push('/'); 
  }
};
</script>