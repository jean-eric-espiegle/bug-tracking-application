import { defineStore } from 'pinia';
import { useAuthStore } from './auth';
import { handleApiError } from '~/utils/errorHandler';

export const useDashboardStore = defineStore('dashboard', {
    state: () => ({
        tickets: [],
        versions: [],
        organizations: [],
        loading: {
            tickets: false,
            versions: false,
            organizations: false,
        },
    }),

    actions: {
        async fetchTickets(organizationId) {
            this.loading.tickets = true;
            const authStore = useAuthStore();
            try {
                this.tickets = await $fetch(`/api/organizations/${organizationId}/tickets`, {
                    headers: {
                        'Authorization': `Bearer ${authStore.token}`
                    }
                });
            } catch (error) {
                const message = handleApiError(error);
                console.error('Failed to fetch tickets:', message);
            } finally {
                this.loading.tickets = false;
            }
        },

        async fetchVersions(organizationId) {
            this.loading.versions = true;
            const authStore = useAuthStore();
            try {
                this.versions = await $fetch(`/api/organizations/${organizationId}/versions`, {
                    headers: {
                        'Authorization': `Bearer ${authStore.token}`
                    }
                });
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
                // Assuming there's an endpoint to get the user's organizations
                this.organizations = await $fetch('/api/organizations', {
                    headers: {
                        'Authorization': `Bearer ${authStore.token}`
                    }
                });
            } catch (error) {
                const message = handleApiError(error);
                console.error('Failed to fetch organizations:', message);
            } finally {
                this.loading.organizations = false;
            }
        },
    },
});