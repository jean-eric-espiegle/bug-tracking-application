import { $fetch } from 'ohmyfetch';

export default defineEventHandler(async (event) => {
	try {
		const { method, url, headers } = event.node.req;

		const user = event.context.user?.username || 'anonymous';

		await $fetch('/api/log-actions', {
			method: 'POST',
			body: { user, method, url, timestamp: new Date().toISOString() },
		});
	} catch (err) {
		console.error('Audit logging failed:', err);
	}
});
