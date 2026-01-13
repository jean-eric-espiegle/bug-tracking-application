<template>
    <div class="overlay" @click.self="cancel">
        <div class="overlay-content">
            <h2>Create New Version</h2>
            <form @submit.prevent="submit">
                <div class="form-group">
                    <label for="name">Version Name</label>
                    <input type="text" id="name" v-model="name" required />
                </div>
                <div class="form-group">
                    <label for="releaseDate">Release Date</label>
                    <input type="date" id="releaseDate" v-model="releaseDate" />
                </div>
                <div class="form-actions">
                    <button type="button" class="btn-cancel" @click="cancel">Cancel</button>
                    <button type="submit" class="btn-submit">Save</button>
                </div>
            </form>
        </div>
    </div>
</template>

<script setup>
import { ref } from 'vue';
import { useDashboardStore } from '~/stores/dashboard';

const dashboardStore = useDashboardStore();
const name = ref('');
const releaseDate = ref('');

function cancel() {
    dashboardStore.hideCreateVersionOverlay();
}

async function submit() {
    const versionData = {
        name: name.value,
        releaseDate: releaseDate.value ? `${releaseDate.value}T00:00:00` : null,
    };
    await dashboardStore.createVersion(versionData);
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
    margin-bottom: 1rem;
}

.form-group label {
    display: block;
    margin-bottom: 0.5rem;
}

.form-group input {
    width: 100%;
    padding: 0.5rem;
    border: 1px solid #ccc;
    border-radius: 4px;
}

.form-actions {
    display: flex;
    justify-content: flex-end;
    gap: 1rem;
    margin-top: 2rem;
}

.btn-cancel, .btn-submit {
    padding: 0.5rem 1rem;
    border-radius: 5px;
    border: none;
    cursor: pointer;
}

.btn-cancel {
    background-color: #f0f0f0;
}

.btn-submit {
    background-color: #28a745;
    color: white;
}
</style>
