<template>
    <div class="panel">
        <h2>Organizations</h2>
        <div v-if="dashboardStore.loading.organizations">Loading...</div>
        <ul v-else-if="dashboardStore.organizations.length > 0">
            <li v-for="org in dashboardStore.organizations" :key="org.id">
                {{ org.name }}
            </li>
        </ul>
        <div v-else>No organizations found.</div>
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
