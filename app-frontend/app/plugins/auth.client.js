import { useNotificationStore } from '~/stores/notification';
import { useAuthStore } from '~/stores/auth';

export default defineNuxtPlugin(() => {
	const originalFetch = globalThis.$fetch;

	globalThis.$fetch = async (request, options = {}) => {
		try {
			console.log('fetching...');
			return await originalFetch(request, options);
		} catch (error) {
			if (error?.response?.status === 401) {
				console.log('401 error detected, handling...');
				const notificationStore = useNotificationStore();
				const authStore = useAuthStore();

				notificationStore.showNotification(
					'Your session has expired. Please log in again.',
					'error',
				);

				authStore.logout();

				await navigateTo('/login');
			}
			throw error;
		}
	};
});
