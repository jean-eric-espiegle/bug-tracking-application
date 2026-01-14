import { defineStore } from 'pinia';
import { handleApiError } from '~/utils/errorHandler';
import { useNotificationStore } from '~/stores/notification';

export const useAuthStore = defineStore('auth', {
	state: () => ({
		user: null,
		token: null,
		subscriptions: [],
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
		status: null,
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
				this.user = {
					username: this.loginForm.username,
					membershipStatus: response.membershipStatus,
					accountPlan: response.accountPlan,
					memberships: response.memberships,
				};
				this.loginForm.username = '';
				this.loginForm.password = '';
				notificationStore.showNotification('Login successful!', 'success');
				this.status = 'Logged In';
				return { success: true, membershipStatus: response.membershipStatus, accountPlan: response.accountPlan, memberships: response.memberships };
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

		async logout() {
			try {
				// Call the logout API endpoint
				await $fetch('/api/auth/logout', {
					method: 'POST',
					headers: {
						'Authorization': `Bearer ${this.token}`
					}
				});
			} catch (error) {
				// Even if the API call fails, we still want to clear local state
				console.warn('Logout API call failed, but clearing local state anyway:', error);
			}

			// Clear local authentication state
			this.user = null;
			this.token = null;
			this.subscriptions = [];
			this.status = null;
		},
	},
	persist: true,
});
