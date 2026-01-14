import { defineStore } from 'pinia';
import { useAuthStore } from '~/stores/auth';
import { useDashboardStore } from '~/stores/dashboard';

export const useTicketOverlayStore = defineStore('ticketOverlay', {
	state: () => ({
		ticket: null,
		loading: false,
		error: null,
		isOverlayVisible: false,
		isEditing: false,
		editableTicket: null,
		members: [],
		versions: [],
	}),
	actions: {
		async fetchTicket(ticketId) {
			this.loading = true;
			this.error = null;
			try {
				const authStore = useAuthStore();
				const response = await fetch(`/api/tickets/${ticketId}`, {
					method: 'GET',
					headers: {
						Authorization: `Bearer ${authStore.token}`,
						'Content-Type': 'application/json',
					},
				});
				if (!response.ok) {
					throw new Error('Failed to fetch ticket');
				}
				this.ticket = await response.json();
			} catch (error) {
				this.error = error.message;
			} finally {
				this.loading = false;
			}
		},
		openOverlay(ticket) {
			this.isOverlayVisible = true;
			this.ticket = ticket; 
			this.fetchTicket(ticket.id); 
		},
		closeOverlay() {
			this.isOverlayVisible = false;
			this.ticket = null;
			this.error = null;
			this.cancelEdit();
		},
		async enterEditMode() {
			this.isEditing = true;
			this.editableTicket = { ...this.ticket };

			if (this.ticket.organizationId) {
				try {
					const authStore = useAuthStore();
					const headers = { Authorization: `Bearer ${authStore.token}` };
					const membersResponse = await fetch(
						`/api/organizations/${this.ticket.organizationId}/members`,
						{ headers }
					);
					if (!membersResponse.ok) throw new Error('Failed to fetch members');
					this.members = await membersResponse.json();

					const versionsResponse = await fetch(
						`/api/organizations/${this.ticket.organizationId}/versions`,
						{ headers }
					);
					if (!versionsResponse.ok)
						throw new Error('Failed to fetch versions');
					this.versions = await versionsResponse.json();
				} catch (err) {
					this.error = err.message;
				}
			}
		},
		cancelEdit() {
			this.isEditing = false;
			this.editableTicket = null;
			this.members = [];
			this.versions = [];
		},
		async saveTicket() {
			if (!this.editableTicket) return;
			this.loading = true;
			try {
				const authStore = useAuthStore();
				const response = await fetch(`/api/tickets/${this.ticket.id}`, {
					method: 'PUT',
					headers: {
						Authorization: `Bearer ${authStore.token}`,
						'Content-Type': 'application/json',
					},
					body: JSON.stringify(this.editableTicket),
				});
				if (!response.ok) {
					throw new Error('Failed to save ticket');
				}

				await this.fetchTicket(this.ticket.id);
				this.cancelEdit();

				const dashboardStore = useDashboardStore();
				if (dashboardStore.selectedOrganizationId) {
					await dashboardStore.fetchTickets(
						dashboardStore.selectedOrganizationId
					);
				}
			} catch (err) {
				this.error = err.message;
			} finally {
				this.loading = false;
			}
		},
		async deleteTicket() {
			this.loading = true;
			try {
				const authStore = useAuthStore();
				const response = await fetch(`/api/tickets/${this.ticket.id}`, {
					method: 'DELETE',
					headers: { Authorization: `Bearer ${authStore.token}` },
				});
				if (!response.ok) {
					throw new Error('Failed to delete ticket');
				}
				this.closeOverlay();

				const dashboardStore = useDashboardStore();
				if (dashboardStore.selectedOrganizationId) {
					await dashboardStore.fetchTickets(
						dashboardStore.selectedOrganizationId
					);
				}
			} catch (err) {
				this.error = err.message;
			} finally {
				this.loading = false;
			}
		},
	},
});
