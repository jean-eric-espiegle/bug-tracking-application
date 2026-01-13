<template>
	<div
		v-if="dashboardStore.isCreateTicketOverlayVisible"
		class="full-page-overlay"
	>
		<div class="overlay-content">
			<h2>Create New Ticket</h2>
			<form @submit.prevent="createTicket">
				<div class="form-group">
					<label for="title">Title</label>
					<input type="text" id="title" v-model="title" required />
				</div>
				<div class="form-group">
					<label for="description">Description</label>
					<textarea id="description" v-model="description" required></textarea>
				</div>
				<div class="form-group">
					<label for="assignee">Assignee</label>
					<input
						type="text"
						id="assignee"
						v-model="assigneeSearch"
						list="members-list"
					/>
					<datalist id="members-list">
						<option
							v-for="member in members"
							:key="member.id"
							:value="member.email"
						>
							{{ member.username }}
						</option>
					</datalist>
				</div>
				<div class="button-group">
					<button type="button" @click="cancel" class="cancel-button">
						Cancel
					</button>
					<button type="submit" class="create-button">Create</button>
				</div>
			</form>
		</div>
	</div>
</template>

<script setup>
import { ref, watch } from 'vue';
import { useDashboardStore } from '~/stores/dashboard';
import { storeToRefs } from 'pinia';

const dashboardStore = useDashboardStore();
const { members } = storeToRefs(dashboardStore);

const title = ref('');
const description = ref('');
const assigneeSearch = ref('');
const assigneeId = ref(null);

watch(assigneeSearch, (newValue) => {
	const selectedMember = members.value.find(
		(member) => member.email === newValue,
	);
	assigneeId.value = selectedMember ? selectedMember.id : null;
	dashboardStore.fetchMembers(newValue);
});

const createTicket = () => {
	dashboardStore.createTicket({
		title: title.value,
		description: description.value,
		assigneeId: assigneeId.value,
		versionId: dashboardStore.selectedVersionId,
	});
};

const cancel = () => {
	dashboardStore.closeCreateTicketOverlay();
};
</script>

<style scoped>
.full-page-overlay {
	position: fixed;
	top: 0;
	left: 0;
	width: 100%;
	height: 100%;
	background-color: rgba(0, 0, 0, 0.8);
	display: flex;
	justify-content: center;
	align-items: center;
	z-index: 1000;
}

.overlay-content {
	background-color: white;
	padding: 2rem;
	border-radius: 8px;
	width: 80%;
	max-width: 500px;
}

.form-group {
	margin-bottom: 1rem;
}

label {
	display: block;
	margin-bottom: 0.5rem;
}

input,
textarea {
	width: 100%;
	padding: 0.5rem;
	border-radius: 4px;
	border: 1px solid #ccc;
}

.button-group {
	margin-top: 1.5rem;
	display: flex;
	justify-content: flex-end;
}

.cancel-button,
.create-button {
	margin-left: 1rem;
	padding: 0.5rem 1rem;
	border-radius: 4px;
	border: none;
	cursor: pointer;
}

.cancel-button {
	background-color: #e74c3c;
	color: white;
}

.create-button {
	background-color: #28a745;
	color: white;
}
</style>
