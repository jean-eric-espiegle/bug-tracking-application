<template>
	<div class="panel">
		<div class="panel-header">
			<h2>Tickets</h2>
			<button
				class="create-btn"
				@click="create"
				:disabled="!dashboardStore.selectedVersionId"
			>
				+
			</button>
		</div>
		<div v-if="dashboardStore.loading.tickets">Loading...</div>
		<ul v-else-if="dashboardStore.tickets.length > 0">
			<li
				v-for="ticket in dashboardStore.tickets"
				:key="ticket.id"
				@click="openTicket(ticket)"
			>
				<i
					:class="
						ticket.status === 'OPEN'
							? 'open'
							: ticket.status === 'CLOSED'
							? 'closed'
							: 'blocked'
					"
					><b>{{ ticket.status }} </b></i
				>
				<p>{{ ticket.title }}</p>
			</li>
		</ul>
		<div v-else>No tickets found.</div>
	</div>
</template>

<script setup>
import { useDashboardStore } from '~/stores/dashboard';
import { useTicketOverlayStore } from '~/stores/ticket-overlay';

const dashboardStore = useDashboardStore();
const ticketOverlayStore = useTicketOverlayStore();

function create() {
	dashboardStore.showCreateTicketOverlay();
}

function openTicket(ticket) {
	ticketOverlayStore.openOverlay(ticket);
}
</script>

<style scoped>
@media (min-width: 1024px) {
	.panel {
		grid-column: 1/1;
		grid-row: 2/3;
		padding: 1rem;
	}
}

@media (min-width: 768px) and (max-width: 1023px) {
	.panel {
		grid-row: 4;
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

ul {
	list-style: none;
	padding: 0;
	margin: 0;
}

li {
	padding: 10px;
	cursor: pointer;
	display: flex;
	flex-direction: row;
	align-items: center;
}

li:hover {
	background-color: var(--light-text-color);
}
.open {
	display: block;
	width: 15%;
	color: red;
}
.closed {
	display: block;
	width: 15%;
	color: greenyellow;
}
.blocked {
	display: block;
	width: 15%;
	color: grey;
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
