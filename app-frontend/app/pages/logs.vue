<template>
	<div class="logs-page">
		<h1>Audit Logs</h1>

		<div v-if="!hasPermission" class="no-permission">
			You do not have permission to view this module.
		</div>

		<div v-else>
			<section class="filters">
				<div class="filters-header">
					<h2>Filters</h2>
					<button
						type="button"
						class="btn-toggle-filters"
						@click="toggleFilters"
					>
						{{ showFilters ? 'Hide Filters' : 'Show Filters' }}
					</button>
				</div>
				<form
					v-if="showFilters"
					@submit.prevent="onSearch"
					class="filters-form"
				>
					<div class="form-row">
						<div class="form-group">
							<label for="organizationId">Organization</label>
							<select
								id="organizationId"
								v-model="logsStore.selectedOrganizationId"
								@change="onOrganizationChange"
							>
								<option value="">Any</option>
								<option
									v-for="org in userOrganizations"
									:key="org.organizationId"
									:value="org.organizationId"
								>
									{{ org.organizationName }}
								</option>
							</select>
						</div>
						<div class="form-group">
							<label for="userId">User</label>
							<select
								id="userId"
								v-model="logsStore.selectedUserId"
								:disabled="availableUsers.length === 0"
							>
								<option value="">Any</option>
								<option
									v-for="user in availableUsers"
									:key="user.id"
									:value="user.id"
								>
									{{ user.username }} ({{ user.email }})
								</option>
							</select>
						</div>
						<div class="form-group">
							<label for="role">Role</label>
							<select id="role" v-model="logsStore.filters.role">
								<option value="">Any</option>
								<option value="OWNER">Owner</option>
								<option value="ADMIN">Admin</option>
								<option value="SUPPORT">Support</option>
								<option value="USER">User</option>
							</select>
						</div>
						<div class="form-group">
							<label for="action">Action</label>
							<select id="action" v-model="logsStore.filters.action">
								<option value="">Any</option>
								<option
									v-for="option in actionOptions"
									:key="option"
									:value="option"
								>
									{{ option }}
								</option>
							</select>
						</div>
					</div>

					<div class="form-row">
						<div class="form-group">
							<label for="startDate">Start Date</label>
							<input
								id="startDate"
								type="datetime-local"
								v-model="logsStore.filters.startDate"
							/>
						</div>
						<div class="form-group">
							<label for="endDate">End Date</label>
							<input
								id="endDate"
								type="datetime-local"
								v-model="logsStore.filters.endDate"
							/>
						</div>
						<div class="form-actions">
							<button type="button" class="btn-secondary" @click="onReset">
								Reset
							</button>
							<button type="submit" class="btn-primary">
								Search
							</button>
						</div>
					</div>
				</form>
			</section>

			<section class="logs-results">
				<h2>Results</h2>
				<div v-if="logsStore.loading" class="loading">Loading logs...</div>
				<div v-else-if="logsStore.logs.length === 0" class="no-results">
					No logs found for the selected filters.
				</div>
				<table v-else class="logs-table">
					<thead>
						<tr>
							<th>ID</th>
							<th>User ID</th>
							<th>Organization ID</th>
							<th>Role</th>
							<th>Action</th>
							<th>Created At</th>
						</tr>
					</thead>
					<tbody>
						<tr v-for="log in logsStore.logs" :key="log.id">
							<td>{{ log.id }}</td>
							<td>{{ log.userId }}</td>
							<td>{{ log.organizationId }}</td>
							<td>{{ log.role }}</td>
							<td>{{ log.action }}</td>
							<td>{{ formatDate(log.createdAt) }}</td>
						</tr>
					</tbody>
				</table>
			</section>
		</div>
	</div>
</template>

<script setup>
import { computed, onMounted, ref } from 'vue';
import { useAuthStore } from '~/stores/auth';
import { useLogsStore } from '~/stores/logs';
import { useDashboardStore } from '~/stores/dashboard';

const authStore = useAuthStore();
const logsStore = useLogsStore();
const dashboardStore = useDashboardStore();
const showFilters = ref(true);

const hasPermission = computed(() => {
	const memberships = authStore.user?.memberships || [];
	return memberships.some(
		(m) => m.membershipRole === 'OWNER' || m.membershipRole === 'ADMIN',
	);
});

const userOrganizations = computed(() => authStore.user?.memberships || []);

const availableUsers = computed(() => {
	// Use members from dashboard store when an organization is selected
	if (!logsStore.selectedOrganizationId) return [];
	return dashboardStore.members || [];
});

const actionOptions = [
	'User Login',
	'User Logout',
	'Created Ticket',
	'Updated Ticket',
	'Deleted Ticket',
	'Assigned Ticket Version',
	'Transferred Ownership',
	'Added New Member',
	'Invited New Member',
	'Deleted Organization',
];

function formatDate(value) {
	if (!value) return '';
	// value is ISO string from backend; keep it simple for now
	return new Date(value).toLocaleString();
}

function onReset() {
	logsStore.resetFilters();
}

async function onSearch() {
	await logsStore.fetchLogs();
}

function toggleFilters() {
	showFilters.value = !showFilters.value;
}

async function onOrganizationChange() {
	// When organization changes, update dashboardStore and fetch members
	const orgId = logsStore.selectedOrganizationId;
	if (orgId) {
		dashboardStore.selectedOrganizationId = Number(orgId);
		await dashboardStore.fetchMembers();
	} else {
		dashboardStore.members = [];
		logsStore.selectedUserId = '';
	}
}

onMounted(async () => {
	if (hasPermission.value) {
		await logsStore.fetchLogs();
	}
});
</script>

<style scoped>
.logs-page {
	max-width: 1200px;
	margin: 0 auto;
	padding: 24px;
}

.no-permission {
	padding: 16px;
	border-radius: 8px;
	background-color: #fff3cd;
	color: #856404;
	border: 1px solid #ffeeba;
}

.filters {
	margin-bottom: 24px;
	padding: 16px;
	border-radius: 8px;
	background-color: #f8f9fa;
	border: 1px solid #dee2e6;
}

.filters-header {
	display: flex;
	justify-content: space-between;
	align-items: center;
	margin-bottom: 8px;
}

.filters-form {
	display: flex;
	flex-direction: column;
	gap: 16px;
}

.form-row {
	display: flex;
	flex-wrap: wrap;
	gap: 16px;
}

.form-group {
	display: flex;
	flex-direction: column;
	flex: 1 1 200px;
}

.form-group label {
	font-weight: 600;
	margin-bottom: 4px;
}

.form-group input,
.form-group select {
	padding: 6px 8px;
	border-radius: 4px;
	border: 1px solid #ced4da;
	font-size: 14px;
}

.form-actions {
	display: flex;
	align-items: flex-end;
	gap: 8px;
}

.btn-primary,
.btn-secondary {
	padding: 8px 16px;
	border-radius: 4px;
	border: none;
	cursor: pointer;
	font-size: 14px;
}

.btn-primary {
	background-color: #007bff;
	color: white;
}

.btn-toggle-filters {
	padding: 6px 12px;
	border-radius: 4px;
	border: 1px solid #6c757d;
	background-color: #ffffff;
	color: #6c757d;
	cursor: pointer;
	font-size: 13px;
}

.btn-toggle-filters:hover {
	background-color: #6c757d;
	color: #ffffff;
}

.btn-secondary {
	background-color: #6c757d;
	color: white;
}

.logs-results {
	padding: 16px;
	border-radius: 8px;
	background-color: #ffffff;
	border: 1px solid #dee2e6;
}

.logs-table {
	width: 100%;
	border-collapse: collapse;
	margin-top: 12px;
	font-size: 14px;
}

.logs-table th,
.logs-table td {
	border: 1px solid #dee2e6;
	padding: 8px;
	text-align: left;
}

.logs-table th {
	background-color: #f1f3f5;
}

.loading,
.no-results {
	margin-top: 8px;
	font-style: italic;
	color: #6c757d;
}
</style>


