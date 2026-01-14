<template>
	<div class="overlay" @click.self="cancel">
		<div class="overlay-content">
			<h2>Create New Organization</h2>
			<form @submit.prevent="submit">
				<div class="form-group">
					<label for="organizationName">Organization Name</label>
					<input
						type="text"
						id="organizationName"
						v-model="form.organizationName"
						required
						placeholder="Enter organization name"
					/>
				</div>
				<div class="form-actions">
					<button type="button" class="btn-cancel" @click="cancel">
						Cancel
					</button>
					<button type="submit" class="btn-submit" :disabled="isSubmitting">
						{{ isSubmitting ? 'Creating...' : 'Create Organization' }}
					</button>
				</div>
			</form>
		</div>
	</div>
</template>

<script setup>
import { ref } from 'vue';
import { useDashboardStore } from '~/stores/dashboard';
import { useNotificationStore } from '~/stores/notification';

const dashboardStore = useDashboardStore();
const notificationStore = useNotificationStore();

const isSubmitting = ref(false);

const form = ref({
	organizationName: '',
});

function cancel() {
	dashboardStore.hideCreateOrganizationOverlay();
	// Reset form
	form.value = {
		organizationName: '',
	};
}

async function submit() {
	if (!form.value.organizationName.trim()) {
		notificationStore.showNotification(
			'Please enter an organization name',
			'error',
		);
		return;
	}

	isSubmitting.value = true;

	try {
		await dashboardStore.createOrganization({
			organizationName: form.value.organizationName.trim(),
		});

		notificationStore.showNotification(
			'Organization created successfully!',
			'success',
		);

		// Reset form and close overlay
		form.value = {
			organizationName: '',
		};
		dashboardStore.hideCreateOrganizationOverlay();
	} catch (error) {
		// Error is already handled in the dashboard store
		console.error('Failed to create organization:', error);
	} finally {
		isSubmitting.value = false;
	}
}
</script>

<style scoped>
.overlay {
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
	padding: 2rem;
	border-radius: 8px;
	width: 90%;
	max-width: 500px;
}

.form-group {
	margin-bottom: 1.5rem;
}

.form-group label {
	display: block;
	margin-bottom: 0.5rem;
	font-weight: 500;
}

.form-group input,
.form-group select {
	width: 100%;
	padding: 0.75rem;
	border: 1px solid #ddd;
	border-radius: 4px;
	font-size: 1rem;
}

.form-group input:focus,
.form-group select:focus {
	outline: none;
	border-color: #007bff;
	box-shadow: 0 0 0 2px rgba(0, 123, 255, 0.25);
}

.form-actions {
	display: flex;
	justify-content: flex-end;
	gap: 1rem;
	margin-top: 2rem;
}

.btn-cancel {
	padding: 0.75rem 1.5rem;
	border-radius: 5px;
	border: 1px solid #ddd;
	cursor: pointer;
	background-color: #f8f9fa;
	color: #333;
}

.btn-cancel:hover {
	background-color: #e9ecef;
}

.btn-submit {
	padding: 0.75rem 1.5rem;
	border-radius: 5px;
	border: none;
	cursor: pointer;
	background-color: #28a745;
	color: white;
	font-weight: 500;
}

.btn-submit:hover:not(:disabled) {
	background-color: #218838;
}

.btn-submit:disabled {
	background-color: #6c757d;
	cursor: not-allowed;
}
</style>
