import { useNotificationStore } from '~/stores/notification';

export default defineNuxtPlugin((nuxtApp) => {
	const router = useRouter();
	const notificationStore = useNotificationStore();

	// Wrap the global $fetch
	const originalFetch = nuxtApp.$fetch;

	nuxtApp.$fetch = async (request, options) => {
		try {
			return await originalFetch(request, options);
		} catch (error) {
			if (error?.response?.status === 401) {
				notificationStore.showNotification(
					'Your session has expired. Please log in again.',
					'error',
				);
				router.push('/login');
			}
			throw error;
		}
	};
});
