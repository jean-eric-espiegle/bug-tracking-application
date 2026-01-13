<template>
	<div class="panel">
		<div class="panel-header">
			<h2>Organizations</h2>
			<button class="create-btn" @click="create">+</button>
		</div>
		<div v-if="dashboardStore.loading.organizations">Loading...</div>
		<div v-else-if="dashboardStore.organizations.length > 0">
			<div
				v-for="org in dashboardStore.organizations"
				:key="org.id"
				class="organization-switch"
				:class="{ selected: dashboardStore.selectedOrganizationId === org.id }"
				@click="dashboardStore.selectOrganization(org.id)"
			>
				{{ org.name }}
			</div>
		</div>
		<div v-else>No organizations found.</div>
	</div>
</template>

<script setup>
import { useDashboardStore } from '~/stores/dashboard';
const dashboardStore = useDashboardStore();

function create() {
	// TODO: Implement create organization
	console.log('Create organization');
}
</script>

<style scoped>
.panel {
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

.organization-switch {
	padding: 10px;
	cursor: pointer;
	border-bottom: 1px solid #eee;
}

.organization-switch:last-child {
	border-bottom: none;
}

.organization-switch.selected {
	background-color: #e0e0e0;
}
</style>
