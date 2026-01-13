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
	dashboardStore.fetchOrganizations();
});
</script>

<style scoped>
.dashboard-container {
	max-width: 1200px;
	height: 85vh;
	margin: 0 auto;
	padding: 1%;
}

h1 {
	text-align: center;
	margin-bottom: 2rem;
}

.panels {
	display: grid;
	height: 89%;
	grid-template-columns: 49% 24.5% 24.5%;
	gap: 1%;
}
</style>
