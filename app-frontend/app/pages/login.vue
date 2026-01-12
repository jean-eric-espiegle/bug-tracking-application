<template>
	<div class="login-container">
		<div class="login-card">
			<h1 class="login-title">Login</h1>
			<form @submit.prevent="handleLogin" class="login-form">
				<div class="form-group">
					<label for="username" class="form-label">Username</label>
					<input
						type="text"
						id="username"
						v-model="authStore.loginForm.username"
						class="form-input"
					/>
				</div>
				<div class="form-group">
					<label for="password" class="form-label">Password</label>
					<input
						type="password"
						id="password"
						v-model="authStore.loginForm.password"
						class="form-input"
					/>
				</div>
				<button type="submit" class="btn btn-primary">Login</button>
			</form>
			<p class="register-link">
				Don't have an account? <NuxtLink to="/register">Register</NuxtLink>
			</p>
		</div>
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
		if (result.membershipStatus === 'NEW') {
			router.push('/subscribe');
		} else {
			router.push('/dashboard');
		}
	}
};
</script>

<style lang="scss" scoped>
.login-container {
	display: flex;
	justify-content: center;
	align-items: center;
	padding: 2rem;
}

.login-card {
	background-color: var(--light-text-color);
	padding: 2.5rem;
	border-radius: 8px;
	box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
	width: 100%;
	max-width: 400px;
}

.login-title {
	font-size: var(--font-size-large);
	color: var(--primary-color);
	text-align: center;
	margin-bottom: 1.5rem;
}

.login-form {
	display: flex;
	flex-direction: column;
}

.form-group {
	margin-bottom: 1.5rem;
}

.form-label {
	display: block;
	font-size: var(--font-size-small);
	color: var(--text-color);
	margin-bottom: 0.5rem;
}

.form-input {
	width: 100%;
	padding: 0.75rem;
	font-size: var(--font-size-medium);
	border: 1px solid var(--border-color);
	border-radius: 4px;
	transition: border-color 0.3s ease, box-shadow 0.3s ease;

	&:focus {
		outline: none;
		border-color: var(--primary-color);
		box-shadow: 0 0 0 3px rgba(74, 144, 226, 0.2);
	}
}

.btn {
	padding: 0.75rem 1.5rem;
	font-size: var(--font-size-medium);
	border: none;
	border-radius: 4px;
	cursor: pointer;
	transition: background-color 0.3s ease;
}

.btn-primary {
	background-color: var(--primary-color);
	color: var(--light-text-color);

	&:hover {
		background-color: color-mix(in srgb, var(--primary-color) 90%, black);
	}
}

.register-link {
	text-align: center;
	margin-top: 1.5rem;
	font-size: var(--font-size-small);

	a {
		color: var(--primary-color);
		text-decoration: none;

		&:hover {
			text-decoration: underline;
		}
	}
}
</style>
