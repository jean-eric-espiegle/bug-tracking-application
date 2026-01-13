<template>
	<div v-if="isOverlayVisible" class="full-page-overlay">
		<div class="overlay-content">
			<button @click="closeOverlay" class="close-button">X</button>
			<div v-if="loading">Loading...</div>
			<div v-if="error">{{ error }}</div>
			<div v-if="ticket">
				<!-- View Mode -->
				<div v-if="!isEditing">
					<h1>{{ ticket.title }}</h1>
					<p>Description: {{ ticket.description }}</p>
					<p>Status: {{ ticket.status }}</p>
					<p>Assignee: {{ ticket.assigneeUsername || 'Not assigned' }}</p>
					<p>Version: {{ ticket.versionId || 'Not assigned' }}</p>
					<div class="button-group">
						<button @click="enterEditMode" class="edit-button">Edit</button>
						<button @click="handleDelete" class="delete-button">
							Delete
						</button>
					</div>
				</div>

				<!-- Edit Mode -->
				<div v-if="isEditing && editableTicket">
					<label for="title">Title</label>
					<input id="title" type="text" v-model="editableTicket.title" />

					<label for="description">Description</label>
					<textarea
						id="description"
						v-model="editableTicket.description"
					></textarea>

					<label for="status">Status</label>
					<select id="status" v-model="editableTicket.status">
						<option>OPEN</option>
						<option>CLOSED</option>
						<option>BLOCKED</option>
					</select>

					<label for="assignee">Assignee</label>
					<select id="assignee" v-model="editableTicket.assigneeId">
						<option :value="null">Not assigned</option>
						<option
							v-for="member in members"
							:key="member.id"
							:value="member.id"
						>
							{{ member.username }}
						</option>
					</select>

					<label for="version">Version</label>
					<select id="version" v-model="editableTicket.versionId">
						<option :value="null">Not assigned</option>
						<option
							v-for="version in versions"
							:key="version.id"
							:value="version.id"
						>
							{{ version.name }}
						</option>
					</select>

					<div class="button-group">
						<button @click="saveTicket" class="save-button">Save</button>
						<button @click="cancelEdit" class="cancel-button">Cancel</button>
					</div>
				</div>
			</div>
		</div>
	</div>
</template>

<script setup>
import { storeToRefs } from 'pinia';
import { useTicketOverlayStore } from '~/stores/ticket-overlay';

const ticketOverlayStore = useTicketOverlayStore();
const {
	ticket,
	loading,
	error,
	isOverlayVisible,
	isEditing,
	editableTicket,
	members,
	versions,
} = storeToRefs(ticketOverlayStore);

const {
	closeOverlay,
	enterEditMode,
	cancelEdit,
	saveTicket,
	deleteTicket,
} = ticketOverlayStore;

const handleDelete = () => {
	if (confirm('Are you sure you want to delete this ticket?')) {
		deleteTicket();
	}
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
	max-width: 800px;
	position: relative;
}

.close-button {
	position: absolute;
	top: 1rem;
	right: 1rem;
	background: none;
	border: none;
	font-size: 1.5rem;
	cursor: pointer;
}

.button-group {
	margin-top: 1rem;
}

.edit-button,
.delete-button,
.save-button,
.cancel-button {
	margin-right: 1rem;
	padding: 0.5rem 1rem;
	border-radius: 4px;
	border: none;
	cursor: pointer;
}

.edit-button {
	background-color: #3498db;
	color: white;
}

.delete-button {
	background-color: #e74c3c;
	color: white;
}

.save-button {
	background-color: #2ecc71;
	color: white;
}

.cancel-button {
	background-color: #95a5a6;
	color: white;
}

label {
	display: block;
	margin-bottom: 0.5rem;
	font-weight: bold;
}

input[type='text'],
textarea,
select {
	width: 100%;
	padding: 0.5rem;
	margin-bottom: 1rem;
	border: 1px solid #ccc;
	border-radius: 4px;
	box-sizing: border-box; /* Ensures padding doesn't affect width */
}
</style>
