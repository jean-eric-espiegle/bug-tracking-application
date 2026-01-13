<template>
	<div class="dashboard-container">
		<div class="panels">
			<h1>Dashboard</h1>
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
	grid-row: 1;
	grid-column: 1;
	/* display: grid; */
	/* grid-template-columns: 45% 35% 20%; */
	/* grid-template-rows: 10% 60% 30%; */
	/* max-width: 1200px; */
	/* height: 85vh; */
	/* margin: 0 auto; */
	/* padding: 1%; */
}

h1 {
	grid-row: 1;
	grid-column: 1/4;
	text-align: center;
	margin-bottom: 2rem;
}

.panels {
	display: grid;
	width: 100%;
	height: 100%;
	grid-template-columns: 49% 24.5% 24.5%;
	grid-template-rows: 10% 60% 30%;
	gap: 1%;
}
@media (min-width: 1024px) {
	.panels {
		grid-template-columns: 49% 24.5% 24.5%;
		grid-template-rows: 10% 60% 30%;
	}
}
/* Tablet: 768px and up */
@media (min-width: 768px) and (max-width: 1023px) {
	.panels {
		grid-template-columns: 100%;
		grid-template-rows: 10% 30% 30% 30%;
	}
}
</style>
