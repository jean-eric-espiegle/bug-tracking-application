import { defineStore } from 'pinia';
import { handleApiError } from '~/utils/errorHandler';
import { useNotificationStore } from '~/stores/notification';

export const useAuthStore = defineStore('auth', {
	state: () => ({
		user: null,
		token: null,
		loginForm: {
			username: '',
			password: '',
		},
		registerForm: {
			username: '',
			email: '',
			password: '',
		},
		validation: {
			usernameExists: false,
			emailExists: false,
		},
	}),

	actions: {
		async login() {
			const notificationStore = useNotificationStore();
			try {
				const response = await $fetch('/api/auth/login', {
					method: 'POST',
					body: { ...this.loginForm },
				});
				this.token = response.token;
				this.loginForm.username = '';
				this.loginForm.password = '';
				notificationStore.showNotification('Login successful!', 'success');
				return { success: true };
			} catch (error) {
				const message = handleApiError(error);
				notificationStore.showNotification(message, 'error');
				console.error('Login failed:', error);
				return { success: false, error: message };
			}
		},

		async register() {
			const notificationStore = useNotificationStore();
			if (this.validation.usernameExists || this.validation.emailExists) {
				const message = 'Please fix validation errors.';
				notificationStore.showNotification(message, 'error');
				return { success: false, error: message };
			}
			try {
				await $fetch('/api/auth/register', {
					method: 'POST',
					body: { ...this.registerForm },
				});
				this.registerForm.username = '';
				this.registerForm.email = '';
				this.registerForm.password = '';
				notificationStore.showNotification(
					'Registration successful! Please log in.',
					'success',
				);
				return { success: true };
			} catch (error) {
				const message = handleApiError(error);
				notificationStore.showNotification(message, 'error');
				console.error('Registration failed:', error);
				return { success: false, error: message };
			}
		},

		async checkUsername() {
			if (!this.registerForm.username) return;
			try {
				const response = await $fetch(
					`/api/validate/username?username=${this.registerForm.username}`,
				);
				this.validation.usernameExists = response.exists;
			} catch (error) {
				console.error('Error checking username:', error);
			}
		},

		async checkEmail() {
			if (!this.registerForm.email) return;
			try {
				const response = await $fetch(
					`/api/validate/email?email=${this.registerForm.email}`,
				);
				this.validation.emailExists = response.exists;
			} catch (error) {
				console.error('Error checking email:', error);
			}
		},

		logout() {
			this.user = null;
			this.token = null;
		},
	},
});
