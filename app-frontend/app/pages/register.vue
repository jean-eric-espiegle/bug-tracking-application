<template>
  <div>
    <h1>Register</h1>
    <form @submit.prevent="handleRegister">
      <div>
        <label for="username">Username</label>
        <input type="text" id="username" v-model="authStore.registerForm.username" @input="debouncedCheckUsername" />
        <p v-if="authStore.validation.usernameExists" style="color: red;">Username already taken.</p>
      </div>
      <div>
        <label for="email">Email</label>
        <input type="email" id="email" v-model="authStore.registerForm.email" @input="debouncedCheckEmail" />
        <p v-if="authStore.validation.emailExists" style="color: red;">Email already in use.</p>
      </div>
      <div>
        <label for="password">Password</label>
        <input type="password" id="password" v-model="authStore.registerForm.password" />
      </div>
      <button type="submit">Register</button>
    </form>
    <p>
      Already have an account? <NuxtLink to="/login">Login</NuxtLink>
    </p>
  </div>
</template>

<script setup>
import { useAuthStore } from '~/stores/auth';
import { useRouter } from 'vue-router';

const authStore = useAuthStore();
const router = useRouter();

const debounce = (fn, delay) => {
  let timeoutId = null;
  return (...args) => {
    clearTimeout(timeoutId);
    timeoutId = setTimeout(() => {
      fn(...args);
    }, delay);
  };
};

const debouncedCheckUsername = debounce(() => {
  authStore.checkUsername();
}, 500);

const debouncedCheckEmail = debounce(() => {
  authStore.checkEmail();
}, 500);

const handleRegister = async () => {
  await authStore.checkUsername();
  await authStore.checkEmail();
  const result = await authStore.register();
  if (result.success) {
    router.push('/login');
  }
};
</script>