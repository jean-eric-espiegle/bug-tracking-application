<template>
    <div class="dashboard-container">
        <h1>Dashboard</h1>
        <div class="panels">
            <TicketsPanel />
            <VersionsPanel />
            <OrganizationsPanel />
        </div>
    </div>
</template>

<script setup>
import TicketsPanel from '~/components/TicketsPanel.vue';
import VersionsPanel from '~/components/VersionsPanel.vue';
import OrganizationsPanel from '~/components/OrganizationsPanel.vue';
import { useDashboardStore } from '~/stores/dashboard';
import { onMounted } from 'vue';

const dashboardStore = useDashboardStore();

onMounted(() => {
    // I need a way to get the current organization id
    // For now, let's assume the first organization
    dashboardStore.fetchOrganizations().then(() => {
        if (dashboardStore.organizations.length > 0) {
            const orgId = dashboardStore.organizations[0].id;
            dashboardStore.fetchTickets(orgId);
            dashboardStore.fetchVersions(orgId);
        }
    });
});
</script>

<style scoped>
.dashboard-container {
    max-width: 1200px;
    margin: 0 auto;
    padding: 2rem;
}

h1 {
    text-align: center;
    margin-bottom: 2rem;
}

.panels {
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
    gap: 2rem;
}
</style>
