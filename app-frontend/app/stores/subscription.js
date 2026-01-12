import { defineStore } from 'pinia';
import { handleApiError } from '~/utils/errorHandler';
import { useNotificationStore } from '~/stores/notification';
import { useAuthStore } from './auth';

export const useSubscriptionStore = defineStore('subscription', {
    state: () => ({
        packages: [
            { "type": "FREE", "max_admins": 1, "max_support": 1, "max_users": 1, "price": 0 },
            { "type": "PRO", "max_admins": 10, "max_support": 10, "max_users": 10, "price": 15 },
            { "type": "ENTERPRISE", "max_admins": 999, "max_support": 999, "max_users": 999, "price": "Contact us" }
        ],
        selectedPlan: null,
    }),

    actions: {
        async selectPlan(planType, organizationName = null) {
            const notificationStore = useNotificationStore();
            const authStore = useAuthStore();
            try {
                const response = await $fetch('/api/subscription/select', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                        'Authorization': `Bearer ${authStore.token}`
                    },
                    body: { planType, organizationName },
                });
                this.selectedPlan = response.plan;
                notificationStore.showNotification('Plan selected successfully!', 'success');
                return { success: true };
            } catch (error) {
                const message = handleApiError(error);
                notificationStore.showNotification(message, 'error');
                console.error('Plan selection failed:', error);
                return { success: false, error: message };
            }
        },
    },
});
