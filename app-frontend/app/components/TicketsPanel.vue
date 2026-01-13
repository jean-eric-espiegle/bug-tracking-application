<template>
	<div class="panel">
		<div class="panel-header">
			<h2>Tickets</h2>
			<button
				class="create-btn"
				@click="create"
				:disabled="!dashboardStore.selectedOrganizationId"
			>
				+
			</button>
		</div>
		<div v-if="dashboardStore.loading.tickets">Loading...</div>
		<ul v-else-if="dashboardStore.tickets.length > 0">
			<li v-for="ticket in dashboardStore.tickets" :key="ticket.id">
				{{ ticket.title }}
			</li>
		</ul>
		<div v-else>No tickets found.</div>
	</div>
</template>

<script setup>
import { useDashboardStore } from '~/stores/dashboard';
const dashboardStore = useDashboardStore();

function create() {
	// TODO: Implement create ticket
	console.log('Create ticket');
}
</script>

<style scoped>
.panel {
	grid-column: 1/1;
	border: 1px solid #ccc;
	border-radius: 8px;
	padding: 1rem;
}

.panel-header {
	display: flex;
	justify-content: space-between;
	align-items: center;
	margin-bottom: 1rem;
}

.create-btn {
	border: none;
	background: #28a745;
	color: white;
	padding: 0.5rem 1rem;
	border-radius: 5px;
	cursor: pointer;
	font-size: 1.5rem;
}

.create-btn:disabled {
	background: #ccc;
	cursor: not-allowed;
}
</style>
