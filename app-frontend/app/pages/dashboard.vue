<template>
	<div class="dashboard-container">
		<div class="panels">
			<h1>Dashboard</h1>
			<OrganizationsPanel />
			<VersionsPanel />
			<TicketsPanel />
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
	grid-row: 1;
	grid-column: 1;
	padding: 1rem 0.75rem 1.5rem;
}

h1 {
	font-size: 1.5rem;
	font-weight: 600;
	color: #111827;
	margin: 0 0 1.25rem 0.25rem;
}

.panels {
	display: flex;
	flex-direction: column;
	width: 100%;
	gap: 1rem;
}

/* Tablet: 768px and up */
@media (min-width: 768px) and (max-width: 1023px) {
	.panels {
		height: 100%;
		display: grid;
		grid-template-columns: 1fr;
		grid-auto-rows: 10% 20% 20% 48%;
		gap: 1rem;
	}
}

/* Desktop: 1024px and up */
@media (min-width: 1024px) {
	.panels {
		height: 100%;
		display: grid;
		grid-template-columns: 27% 27% 44%;
		grid-template-rows: auto 1fr 1fr;
		gap: 1%;
	}
}
</style>
