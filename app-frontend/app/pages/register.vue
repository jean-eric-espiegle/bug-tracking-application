<template>
	<div class="register-container">
		<div class="register-card">
			<h1 class="register-title">Register</h1>
			<form @submit.prevent="handleRegister" class="register-form">
				<div class="form-group">
					<label for="username" class="form-label">Username</label>
					<input
						type="text"
						id="username"
						v-model="authStore.registerForm.username"
						@input="debouncedCheckUsername"
						class="form-input"
					/>
					<p v-if="authStore.validation.usernameExists" class="error-message">
						Username already taken.
					</p>
				</div>
				<div class="form-group">
					<label for="email" class="form-label">Email</label>
					<input
						type="email"
						id="email"
						v-model="authStore.registerForm.email"
						@input="debouncedCheckEmail"
						class="form-input"
					/>
					<p v-if="authStore.validation.emailExists" class="error-message">
						Email already in use.
					</p>
				</div>
				<div class="form-group">
					<label for="password" class="form-label">Password</label>
					<input
						type="password"
						id="password"
						v-model="authStore.registerForm.password"
						class="form-input"
					/>
				</div>
				<button type="submit" class="btn btn-primary">Register</button>
			</form>
			<p class="login-link">
				Already have an account? <NuxtLink to="/login">Login</NuxtLink>
			</p>
		</div>
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

<style lang="scss" scoped>
.register-container {
	display: flex;
	justify-content: center;
	align-items: center;
	padding: 2rem;
}

.register-card {
	background-color: var(--light-text-color);
	padding: 2.5rem;
	border-radius: 8px;
	box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
	width: 100%;
	max-width: 400px;
}

.register-title {
	font-size: var(--font-size-large);
	color: var(--primary-color);
	text-align: center;
	margin-bottom: 1.5rem;
}

.register-form {
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

.login-link {
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

.error-message {
	color: var(--error-color);
	font-size: var(--font-size-small);
	margin-top: 0.25rem;
}
</style>
