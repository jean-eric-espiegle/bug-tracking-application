<template>
	<div class="panel">
		<div class="panel-header">
			<h2>Organizations</h2>
			<button
				class="create-btn"
				@click="create"
				:disabled="isCreateDisabled"
				title="Create new organization"
			>
				+
			</button>
		</div>
		<div v-if="dashboardStore.loading.organizations">Loading...</div>
		<div v-else-if="dashboardStore.organizations.length > 0">
			<div
				v-for="org in dashboardStore.organizations"
				:key="org.id"
				class="organization-item"
			>
				<div
					class="organization-switch"
					:class="{
						selected: dashboardStore.selectedOrganizationId === org.id,
					}"
					@click="dashboardStore.selectOrganization(org.id)"
				>
					{{ org.name }}
				</div>
				<div class="organization-actions">
					<button
						v-if="canEditOrganization(org.membershipRole)"
						class="btn-edit"
						@click.stop="editOrganization(org)"
						title="Edit organization details"
					>
						<svg
							width="16"
							height="16"
							viewBox="0 0 24 24"
							fill="none"
							stroke="currentColor"
							stroke-width="2"
							stroke-linecap="round"
							stroke-linejoin="round"
						>
							<path
								d="M11 4H4a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2v-7"
							/>
							<path
								d="M18.5 2.5a2.121 2.121 0 0 1 3 3L12 15l-4 1 1-4 9.5-9.5z"
							/>
						</svg>
					</button>
					<button
						v-if="canDeleteOrganization(org.membershipRole)"
						class="btn-delete"
						@click.stop="deleteOrganization(org)"
						title="Delete organization"
					>
						<svg
							width="16"
							height="16"
							viewBox="0 0 24 24"
							fill="none"
							stroke="currentColor"
							stroke-width="2"
							stroke-linecap="round"
							stroke-linejoin="round"
						>
							<polyline points="3,6 5,6 21,6" />
							<path
								d="M19,6v14a2,2 0 0,1-2,2H7a2,2 0 0,1-2-2V6m3,0V4a2,2 0 0,1,2-2h4a2,2 0 0,1,2,2v2"
							/>
							<line x1="10" y1="11" x2="10" y2="17" />
							<line x1="14" y1="11" x2="14" y2="17" />
						</svg>
					</button>
				</div>
			</div>
		</div>
		<div v-else>No organizations found.</div>
		<CreateOrganizationOverlay
			v-if="dashboardStore.isCreateOrganizationOverlayVisible"
		/>
		<OrganizationDetails
			v-if="
				dashboardStore.isOrganizationDetailsOverlayVisible &&
				dashboardStore.selectedOrganizationForDetails
			"
			:organization="dashboardStore.selectedOrganizationForDetails"
		/>
	</div>
</template>

<script setup>
import { useDashboardStore } from '~/stores/dashboard';
import CreateOrganizationOverlay from './CreateOrganizationOverlay.vue';
import OrganizationDetails from './OrganizationDetails.vue';
import { useAuthStore } from '~/stores/auth';
import { useNotificationStore } from '~/stores/notification';
import { computed } from 'vue';

const dashboardStore = useDashboardStore();
const authStore = useAuthStore();
const notificationStore = useNotificationStore();

const isCreateDisabled = computed(() => {
	return false;
});

function create() {
	if (!isCreateDisabled.value) {
		dashboardStore.showCreateOrganizationOverlay();
	}
}

function canEditOrganization(membershipRole) {
	return membershipRole === 'OWNER' || membershipRole === 'ADMIN';
}

function canDeleteOrganization(membershipRole) {
	return membershipRole === 'OWNER' || membershipRole === 'ADMIN';
}

function editOrganization(org) {
	dashboardStore.showOrganizationDetailsOverlay(org);
}

async function deleteOrganization(org) {
	if (
		confirm(
			`Are you sure you want to delete "${org.name}"? This action cannot be undone and will remove the organization for all members.`,
		)
	) {
		try {
			await $fetch(`/api/organizations/${org.id}`, {
				method: 'DELETE',
				headers: {
					Authorization: `Bearer ${authStore.token}`,
				},
			});

			notificationStore.showNotification(
				`Organization "${org.name}" has been deleted successfully.`,
				'success',
			);

			await dashboardStore.fetchOrganizations();
		} catch (error) {
			console.error('Failed to delete organization:', error);
		}
	}
}
</script>

<style scoped>
.panel {
	background-color: #ffffff;
	border-radius: 8px;
	border: 1px solid #e5e7eb;
	box-shadow: 0 2px 6px rgba(15, 23, 42, 0.06);
	display: flex;
	flex-direction: column;
	min-height: 0;
	padding: 10px;
	overflow-y: auto;
}

@media (min-width: 1024px) {
	.panel {
		grid-row: 2/4;
		grid-column: 1;
		border-right: 1px solid #ccc;
		padding: 1rem;
	}
}

.panel-header {
	display: flex;
	justify-content: space-between;
	align-items: center;
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
	color: #666;
	cursor: not-allowed;
	opacity: 0.6;
}

.organization-item {
	display: flex;
	align-items: center;
	border-bottom: 1px solid #eee;
}

.organization-item:last-child {
	border-bottom: none;
}

.organization-switch {
	flex: 1;
	padding: 10px;
	cursor: pointer;
}

.organization-switch.selected {
	background-color: #e0e0e0;
}

.organization-actions {
	display: flex;
	gap: 8px;
	padding: 10px;
}

.btn-edit,
.btn-delete {
	width: 32px;
	height: 32px;
	border: none;
	border-radius: 4px;
	cursor: pointer;
	background-color: transparent;
	display: flex;
	align-items: center;
	justify-content: center;
	transition: all 0.2s ease;
	color: #6c757d;
}

.btn-edit:hover {
	background-color: rgba(0, 123, 255, 0.1);
	color: #007bff;
	transform: scale(1.05);
}

.btn-delete:hover {
	background-color: rgba(220, 53, 69, 0.1);
	color: #dc3545;
	transform: scale(1.05);
}
</style>
