import { defineStore } from 'pinia';
import { useAuthStore } from '~/stores/auth';
import { handleApiError } from '~/utils/errorHandler';

export const useLogsStore = defineStore('logs', {
	state: () => ({
		filters: {
			userId: '',
			organizationId: '',
			role: '',
			action: '',
			startDate: '',
			endDate: '',
		},
		selectedOrganizationId: '',
		selectedUserId: '',
		logs: [],
		loading: false,
	}),

	actions: {
		resetFilters() {
			this.filters = {
				userId: '',
				organizationId: '',
				role: '',
				action: '',
				startDate: '',
				endDate: '',
			};
			this.selectedOrganizationId = '';
			this.selectedUserId = '';
		},

		async fetchLogs() {
			const authStore = useAuthStore();
			this.loading = true;
			try {
				const query = new URLSearchParams();
				// Map selected dropdowns into backend filters
				if (this.selectedUserId)
					query.append('userId', this.selectedUserId);
				if (this.selectedOrganizationId)
					query.append('organizationId', this.selectedOrganizationId);
				if (this.filters.role) query.append('role', this.filters.role);
				if (this.filters.action) query.append('action', this.filters.action);
				if (this.filters.startDate)
					query.append('startDate', this.filters.startDate);
				if (this.filters.endDate) query.append('endDate', this.filters.endDate);

				const url = `/api/logs${query.toString() ? `?${query}` : ''}`;

				this.logs = await $fetch(url, {
					headers: {
						Authorization: `Bearer ${authStore.token}`,
					},
				});
			} catch (error) {
				const message = handleApiError(error);
				console.error('Failed to fetch logs:', message);
				this.logs = [];
			} finally {
				this.loading = false;
			}
		},
	},
});


