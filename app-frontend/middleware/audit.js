import { $fetch } from 'ohmyfetch'; // Nuxt 3 default fetch wrapper

export default defineEventHandler(async (event) => {
	try {
		// Get request info
		const { method, url, headers } = event.node.req;

		// Get user info if available (assume you set it via auth middleware)
		const user = event.context.user?.username || 'anonymous';

		// Send log to backend or audit endpoint
		await $fetch('/api/log-actions', {
			method: 'POST',
			body: { user, method, url, timestamp: new Date().toISOString() },
		});
	} catch (err) {
		console.error('Audit logging failed:', err);
	}
});
