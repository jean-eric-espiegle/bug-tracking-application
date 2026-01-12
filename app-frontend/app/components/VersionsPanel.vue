<template>
    <div class="panel">
        <h2>Versions</h2>
        <div v-if="dashboardStore.loading.versions">Loading...</div>
        <ul v-else-if="dashboardStore.versions.length > 0">
            <li v-for="version in dashboardStore.versions" :key="version.id">
                {{ version.name }}
            </li>
        </ul>
        <div v-else>No versions found.</div>
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
