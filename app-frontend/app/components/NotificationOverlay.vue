<template>
	<div
		v-if="notificationStore.show"
		:class="['overlay', notificationStore.type]"
	>
		<p>{{ notificationStore.message }}</p>
		<button @click="notificationStore.hideNotification()" class="close-button">
			X
		</button>
	</div>
</template>

<script setup>
import { useNotificationStore } from '~/stores/notification';

const notificationStore = useNotificationStore();
</script>

<style lang="scss" scoped>
.overlay {
	position: fixed;
	top: 20px;
	right: 20px;
	padding: 1rem 1.5rem;
	border-radius: 8px;
	color: var(--light-text-color);
	z-index: 1000;
	display: flex;
	justify-content: space-between;
	align-items: center;
	min-width: 300px;
	box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
	font-size: var(--font-size-medium);
}

.success {
	background-color: var(--success-color);
}

.error {
	background-color: var(--error-color);
}

p {
	margin: 0;
	margin-right: 1.5rem;
}

.close-button {
	background: none;
	border: none;
	color: var(--light-text-color);
	font-size: 1.2rem;
	font-weight: bold;
	cursor: pointer;
	opacity: 0.8;
	transition: opacity 0.3s ease;

	&:hover {
		opacity: 1;
	}
}
</style>
