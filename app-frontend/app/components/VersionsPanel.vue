<template>
	<div class="panel">
		<div class="panel-header">
			<h2>Versions</h2>
			<button
				class="create-btn"
				@click="create"
				:disabled="!dashboardStore.selectedOrganizationId"
			>
				+
			</button>
		</div>
		<div v-if="dashboardStore.loading.versions">Loading...</div>
		<ul v-else-if="dashboardStore.versions.length > 0">
			<li
				v-for="version in dashboardStore.versions"
				:key="version.id"
				@click="selectVersion(version.id)"
				class="version-switch"
				:class="{ selected: dashboardStore.selectedVersionId === version.id }"
			>
				{{ version.name }}
			</li>
		</ul>
		<div v-else>No versions found.</div>
		<CreateVersionOverlay v-if="dashboardStore.isCreateVersionOverlayVisible" />
	</div>
</template>

<script setup>
import { useDashboardStore } from '~/stores/dashboard';
import CreateVersionOverlay from './CreateVersionOverlay.vue';
const dashboardStore = useDashboardStore();

function create() {
	dashboardStore.showCreateVersionOverlay();
}

function selectVersion(versionId) {
	dashboardStore.selectVersion(versionId);
}
</script>

<style scoped>
@media (min-width: 1024px) {
	.panel {
		grid-row: 2/3;
		grid-column: 2;
		border-left: 1px solid #ccc;
		border-radius: 8px;
		padding: 1rem;
	}
}

@media (min-width: 768px) and (max-width: 1023px) {
	.panel {
		grid-row: 3;
		grid-column: 1;
		overflow-y: auto;
	}
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

ul {
	list-style: none;
	padding: 0;
	margin: 0;
}

li {
	padding: 10px;
}

li.selected {
	background-color: #e0e0e0;
}
</style>
