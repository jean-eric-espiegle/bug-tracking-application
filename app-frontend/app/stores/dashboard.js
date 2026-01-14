import { defineStore } from 'pinia';
import { useAuthStore } from './auth';
import { handleApiError } from '~/utils/errorHandler';

export const useDashboardStore = defineStore('dashboard', {
	state: () => ({
		tickets: [],
		versions: [],
		organizations: [],
		members: [],
		selectedOrganizationId: null,
		selectedVersionId: null,
		isCreateVersionOverlayVisible: false,
		isCreateTicketOverlayVisible: false,
		isCreateOrganizationOverlayVisible: false,
		isOrganizationDetailsOverlayVisible: false,
		selectedOrganizationForDetails: null,
		loading: {
			tickets: false,
			versions: false,
			organizations: false,
			members: false,
		},
	}),

	actions: {
		showCreateVersionOverlay() {
			this.isCreateVersionOverlayVisible = true;
		},

		hideCreateVersionOverlay() {
			this.isCreateVersionOverlayVisible = false;
		},

		showCreateTicketOverlay() {
			this.isCreateTicketOverlayVisible = true;
		},

		hideCreateTicketOverlay() {
			this.isCreateTicketOverlayVisible = false;
		},

		showCreateOrganizationOverlay() {
			this.isCreateOrganizationOverlayVisible = true;
		},

		hideCreateOrganizationOverlay() {
			this.isCreateOrganizationOverlayVisible = false;
		},

		showOrganizationDetailsOverlay(organization) {
			this.selectedOrganizationForDetails = organization;
			this.isOrganizationDetailsOverlayVisible = true;
		},

		hideOrganizationDetailsOverlay() {
			this.isOrganizationDetailsOverlayVisible = false;
			this.selectedOrganizationForDetails = null;
		},

		async createTicket(ticketData) {
			if (!this.selectedOrganizationId) return;

			const authStore = useAuthStore();
			try {
				await $fetch(
					`/api/organizations/${this.selectedOrganizationId}/tickets`,
					{
						method: 'POST',
						headers: {
							Authorization: `Bearer ${authStore.token}`,
							'Content-Type': 'application/json',
						},
						body: ticketData,
					},
				);
				this.hideCreateTicketOverlay();
				await this.fetchTickets(this.selectedOrganizationId);
			} catch (error) {
				const message = handleApiError(error);
				console.error('Failed to create ticket:', message);
			}
		},

		async fetchMembers(searchTerm = '') {
			if (!this.selectedOrganizationId) return;
			this.loading.members = true;
			const authStore = useAuthStore();
			try {
				const url = `/api/organizations/${this.selectedOrganizationId}/members?search=${searchTerm}`;
				this.members = await $fetch(url, {
					headers: {
						Authorization: `Bearer ${authStore.token}`,
					},
				});
			} catch (error) {
				const message = handleApiError(error);
				console.error('Failed to fetch members:', message);
			} finally {
				this.loading.members = false;
			}
		},

		async createVersion(versionData) {
			if (!this.selectedOrganizationId) return;

			const authStore = useAuthStore();
			try {
				await $fetch(
					`/api/organizations/${this.selectedOrganizationId}/versions`,
					{
						method: 'POST',
						headers: {
							Authorization: `Bearer ${authStore.token}`,
							'Content-Type': 'application/json',
						},
						body: versionData,
					},
				);
				this.hideCreateVersionOverlay();
				await this.fetchVersions(this.selectedOrganizationId);
			} catch (error) {
				const message = handleApiError(error);
				console.error('Failed to create version:', message);
			}
		},

		async createOrganization(organizationData) {
			const authStore = useAuthStore();
			try {
				await $fetch('/api/organizations', {
					method: 'POST',
					headers: {
						Authorization: `Bearer ${authStore.token}`,
						'Content-Type': 'application/json',
					},
					body: organizationData,
				});
				this.hideCreateOrganizationOverlay();
				await this.fetchOrganizations();
			} catch (error) {
				const message = handleApiError(error);
				console.error('Failed to create organization:', message);
			}
		},

		async fetchTickets(organizationId, versionId) {
			this.loading.tickets = true;
			const authStore = useAuthStore();
			try {
				let url;
				if (versionId) {
					url = `/api/versions/${versionId}/tickets`;
				} else {
					url = `/api/organizations/${organizationId}/tickets`;
				}

				this.tickets = await $fetch(url, {
					headers: {
						Authorization: `Bearer ${authStore.token}`,
					},
				});
			} catch (error) {
				const message = handleApiError(error);
				console.error('Failed to fetch tickets:', message);
				this.tickets = [];
			} finally {
				this.loading.tickets = false;
			}
		},

		async fetchVersions(organizationId) {
			this.loading.versions = true;
			const authStore = useAuthStore();
			try {
				this.versions = await $fetch(
					`/api/organizations/${organizationId}/versions`,
					{
						headers: {
							Authorization: `Bearer ${authStore.token}`,
						},
					},
				);
			} catch (error) {
				const message = handleApiError(error);
				console.error('Failed to fetch versions:', message);
			} finally {
				this.loading.versions = false;
			}
		},

		async fetchOrganizations() {
			this.loading.organizations = true;
			const authStore = useAuthStore();
			try {
				this.organizations = await $fetch('/api/organizations', {
					headers: {
						Authorization: `Bearer ${authStore.token}`,
					},
				});

				if (this.organizations.length > 0) {
					this.selectOrganization(this.organizations[0].id);
				}
			} catch (error) {
				const message = handleApiError(error);
				console.error('Failed to fetch organizations:', message);
			} finally {
				this.loading.organizations = false;
			}
		},

		selectOrganization(organizationId) {
			if (this.selectedOrganizationId === organizationId) {
				this.selectedOrganizationId = null;
				this.versions = [];
				this.tickets = [];
				this.members = [];
			} else {
				this.selectedOrganizationId = organizationId;
				this.selectedVersionId = null;
				this.fetchVersions(organizationId);
				this.fetchTickets(organizationId);
				this.fetchMembers();
			}
		},

		selectVersion(versionId) {
			if (this.selectedVersionId === versionId) {
				this.selectedVersionId = null;
				this.fetchTickets(this.selectedOrganizationId);
			} else {
				this.selectedVersionId = versionId;
				this.fetchTickets(this.selectedOrganizationId, versionId);
			}
		},
		closeCreateTicketOverlay() {
			this.isCreateTicketOverlayVisible = false;
		},
	},
});
