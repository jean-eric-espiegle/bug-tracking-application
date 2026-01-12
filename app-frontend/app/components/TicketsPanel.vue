<template>
    <div class="panel">
        <h2>Tickets</h2>
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
</script>

<style scoped>
.panel {
    border: 1px solid #ccc;
    border-radius: 8px;
    padding: 2rem;
}
</style>
