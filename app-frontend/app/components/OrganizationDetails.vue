<template>
	<div class="organization-details-overlay" @click.self="closeOverlay">
		<div class="overlay-content" v-if="organization">
			<div class="overlay-header">
				<h2>{{ organization.name }}</h2>
				<button class="close-btn" @click="closeOverlay" title="Close">
					&times;
				</button>
			</div>
			<div class="header">
				<p class="organization-id">ID: {{ organization.id }}</p>
			</div>

			<div class="details-section">
				<h2>Organization Details</h2>
				<div class="detail-item">
					<strong>Plan:</strong> {{ organization.PlanType || 'FREE' }}
				</div>
				<div class="detail-item">
					<strong>Created:</strong> {{ formatDate(organization.createdAt) }}
				</div>
				<div class="detail-item">
					<strong>Your Role:</strong> {{ organization.membershipRole }}
				</div>
			</div>

			<div class="details-section">
				<h2>Owner</h2>
				<div v-if="owner" class="owner-info">
					<div class="owner-avatar">
						{{ owner.username.charAt(0).toUpperCase() }}
					</div>
					<div class="owner-details">
						<div class="owner-name">{{ owner.username }}</div>
						<div class="owner-email">{{ owner.email }}</div>
					</div>
				</div>
				<div v-else class="loading">Loading owner information...</div>
			</div>

			<div class="details-section">
				<div class="members-header">
					<h2>Members ({{ members.length }})</h2>
					<button
						v-if="canInviteMembers"
						class="btn-invite"
						@click="showInviteForm = !showInviteForm"
					>
						{{ showInviteForm ? 'Cancel' : '+ Add Member' }}
					</button>
				</div>

				<!-- Invite Form -->
				<div v-if="showInviteForm" class="invite-form">
					<div class="form-group">
						<label for="inviteEmail">Email Address</label>
						<input
							type="email"
							id="inviteEmail"
							v-model="inviteForm.email"
							required
							placeholder="Enter email address"
						/>
					</div>
					<div class="form-group">
						<label for="inviteRole">Role</label>
						<select id="inviteRole" v-model="inviteForm.role" required>
							<option value="USER">Member</option>
							<option value="ADMIN">Admin</option>
							<option value="SUPPORT">Support</option>
						</select>
					</div>
					<div class="form-actions">
						<button type="button" @click="cancelInvite" class="btn-cancel">
							Cancel
						</button>
						<button
							type="button"
							@click="sendInvite"
							class="btn-submit"
							:disabled="isInviting"
						>
							{{ isInviting ? 'Sending...' : 'Send Invite' }}
						</button>
					</div>
				</div>
				<div v-if="loadingMembers" class="loading">Loading members...</div>
				<div v-else-if="members.length > 0" class="members-list">
					<div v-for="member in members" :key="member.id" class="member-item">
						<div class="member-avatar">
							{{ member.username.charAt(0).toUpperCase() }}
						</div>
						<div class="member-details">
							<div class="member-name">{{ member.username }}</div>
							<div class="member-email">{{ member.email }}</div>
						</div>
						<div class="member-role">{{ getRoleLabel(member.role) }}</div>
					</div>
				</div>
				<div v-else class="no-members">No members found</div>
			</div>
		</div>
	</div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue';
import { useAuthStore } from '~/stores/auth';
import { useDashboardStore } from '~/stores/dashboard';
import { useNotificationStore } from '~/stores/notification';

const props = defineProps({
	organization: {
		type: Object,
		required: true,
	},
});

const authStore = useAuthStore();
const dashboardStore = useDashboardStore();
const notificationStore = useNotificationStore();

const canInviteMembers = computed(() => {
	return (
		organization.value?.membershipRole === 'OWNER' ||
		organization.value?.membershipRole === 'ADMIN'
	);
});

const organization = ref(null);
const owner = ref(null);
const members = ref([]);
const loadingMembers = ref(true);

const showInviteForm = ref(false);
const isInviting = ref(false);
const inviteForm = ref({
	email: '',
	role: 'USER',
});

onMounted(async () => {
	await loadOrganizationData();
});

async function loadOrganizationData() {
	try {
		organization.value = {
			...props.organization,
			planType: 'FREE',
			createdAt: new Date().toISOString(),
		};

		await loadMembers();
	} catch (error) {
		console.error('Failed to load organization data:', error);
	}
}

async function loadMembers() {
	try {
		loadingMembers.value = true;
		const response = await $fetch(
			`/api/organizations/${props.organization.id}/members`,
			{
				headers: {
					Authorization: `Bearer ${authStore.token}`,
				},
			},
		);
		members.value = response;

		const ownerMember = response.find((member) => member.role === 'OWNER');
		if (ownerMember) {
			owner.value = ownerMember;
		}
	} catch (error) {
		console.error('Failed to load members:', error);
		members.value = [];
	} finally {
		loadingMembers.value = false;
	}
}

function closeOverlay() {
	dashboardStore.hideOrganizationDetailsOverlay();
}

async function sendInvite() {
	if (!inviteForm.value.email.trim()) {
		notificationStore.showNotification(
			'Please enter an email address',
			'error',
		);
		return;
	}

	const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
	if (!emailRegex.test(inviteForm.value.email)) {
		notificationStore.showNotification(
			'Please enter a valid email address',
			'error',
		);
		return;
	}

	isInviting.value = true;

	try {
		await $fetch(`/api/organizations/${props.organization.id}/invite`, {
			method: 'POST',
			headers: {
				Authorization: `Bearer ${authStore.token}`,
				'Content-Type': 'application/json',
			},
			body: {
				email: inviteForm.value.email.trim(),
				role: inviteForm.value.role,
			},
		});

		notificationStore.showNotification(
			'Invitation sent successfully!',
			'success',
		);

		inviteForm.value = {
			email: '',
			role: 'USER',
		};
		showInviteForm.value = false;

		await loadMembers();
	} catch (error) {
		console.error('Failed to send invite:', error);
	} finally {
		isInviting.value = false;
	}
}

function cancelInvite() {
	inviteForm.value = {
		email: '',
		role: 'USER',
	};
	showInviteForm.value = false;
}

function formatDate(dateString) {
	if (!dateString) return 'Unknown';
	return new Date(dateString).toLocaleDateString();
}

function getRoleLabel(role) {
	switch (role) {
		case 'OWNER':
			return 'Owner';
		case 'ADMIN':
			return 'Admin';
		case 'SUPPORT':
			return 'Support';
		case 'USER':
			return 'Member';
		default:
			return role;
	}
}
</script>

<style scoped>
.organization-details-overlay {
	position: fixed;
	top: 0;
	left: 0;
	right: 0;
	bottom: 0;
	background-color: rgba(0, 0, 0, 0.5);
	display: flex;
	justify-content: center;
	align-items: center;
	z-index: 1000;
}

.overlay-content {
	background: white;
	border-radius: 8px;
	width: 90%;
	max-width: 800px;
	max-height: 90vh;
	overflow-y: auto;
	box-shadow: 0 4px 20px rgba(0, 0, 0, 0.3);
	padding: 1%;
}

.overlay-header {
	display: flex;
	justify-content: space-between;
	align-items: center;
	padding: 20px 0px;
	border-bottom: 1px solid #eee;
	margin-bottom: 20px;
}

.overlay-header h2 {
	margin: 0;
	color: #333;
}

.close-btn {
	background: none;
	border: none;
	font-size: 24px;
	cursor: pointer;
	color: #666;
	padding: 0;
	width: 30px;
	height: 30px;
	display: flex;
	align-items: center;
	justify-content: center;
	border-radius: 50%;
	transition: background-color 0.2s;
}

.close-btn:hover {
	background-color: #f0f0f0;
	color: #333;
}

.organization-details {
	padding: 0 20px 20px 20px;
}

.header {
	margin-bottom: 30px;
}

.organization-id {
	color: #666;
	font-size: 14px;
}

.details-section {
	margin-bottom: 30px;
}

.members-header {
	display: flex;
	justify-content: space-between;
	align-items: center;
	margin-bottom: 15px;
}

.members-header h2 {
	margin: 0;
	color: #333;
	border-bottom: 1px solid #eee;
	padding-bottom: 5px;
}

.btn-invite {
	padding: 8px 16px;
	border: 1px solid #28a745;
	border-radius: 4px;
	cursor: pointer;
	background-color: #f8f9fa;
	color: #28a745;
	font-size: 14px;
	transition: all 0.2s ease;
}

.btn-invite:hover {
	background-color: #28a745;
	color: white;
}

.details-section h2 {
	margin: 0 0 15px 0;
	color: #333;
	border-bottom: 1px solid #eee;
	padding-bottom: 5px;
}

.invite-form {
	background-color: #f8f9fa;
	padding: 20px;
	border-radius: 8px;
	margin-bottom: 20px;
	border: 1px solid #dee2e6;
}

.detail-item {
	margin-bottom: 10px;
	padding: 8px 0;
}

.owner-info,
.member-item {
	display: flex;
	align-items: center;
	padding: 15px;
	border: 1px solid #eee;
	border-radius: 8px;
	margin-bottom: 10px;
	background-color: #fafafa;
}

.owner-avatar,
.member-avatar {
	width: 40px;
	height: 40px;
	border-radius: 50%;
	background-color: #007bff;
	color: white;
	display: flex;
	align-items: center;
	justify-content: center;
	font-weight: bold;
	margin-right: 15px;
	flex-shrink: 0;
}

.owner-details,
.member-details {
	flex: 1;
}

.owner-name,
.member-name {
	font-weight: bold;
	color: #333;
	margin-bottom: 2px;
}

.owner-email,
.member-email {
	color: #666;
	font-size: 14px;
}

.member-role {
	padding: 4px 8px;
	border-radius: 4px;
	font-size: 12px;
	font-weight: bold;
	text-transform: uppercase;
	background-color: #e9ecef;
	color: #495057;
}

.loading {
	color: #666;
	font-style: italic;
	padding: 20px;
	text-align: center;
}

.no-members {
	color: #666;
	font-style: italic;
	padding: 20px;
	text-align: center;
}

.members-list {
	max-height: 400px;
	overflow-y: auto;
}
</style>
