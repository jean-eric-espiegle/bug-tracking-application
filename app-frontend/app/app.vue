<template>
	<div class="app-container">
		<NotificationOverlay />
		<TicketOverlay />
		<CreateTicketOverlay />
		<header class="app-header">
			<nav class="app-nav">
				<NuxtLink to="/" class="nav-link-brand">BugTracker</NuxtLink>
				<div v-if="authStore.status === null">
					<NuxtLink to="/login" class="nav-link">Login</NuxtLink>
					<NuxtLink to="/register" class="nav-link">Register</NuxtLink>
				</div>
				<div v-else>
					<NuxtLink to="/dashboard" class="nav-link">Tickets</NuxtLink>
					<NuxtLink to="/logs" class="nav-link">Logs</NuxtLink>
					<button @click="handleLogout" class="nav-link-btn">Logout</button>
				</div>
			</nav>
		</header>
		<main class="app-main">
			<NuxtPage />
		</main>
	</div>
</template>

<script setup>
import NotificationOverlay from '~/components/NotificationOverlay.vue';
import TicketOverlay from '~/components/TicketOverlay.vue';
import CreateTicketOverlay from '~/components/CreateTicketOverlay.vue';
import { useAuthStore } from '~/stores/auth';
import { useNotificationStore } from '~/stores/notification';

const authStore = useAuthStore();
const notificationStore = useNotificationStore();

async function handleLogout() {
	try {
		await authStore.logout();
		notificationStore.showNotification('Logged out successfully', 'success');
		// Redirect to home page
		await navigateTo('/');
	} catch (error) {
		console.error('Logout failed:', error);
		// Still show success since local state was cleared
		notificationStore.showNotification('Logged out successfully', 'success');
		await navigateTo('/');
	}
}
</script>

<style lang="scss">
body {
	background-color: var(--background-color);
	color: var(--text-color);
	font-family: var(--primary-font);
	margin: 0;
	padding: 0;
}

.app-container {
	display: flex;
	flex-direction: column;
	min-height: 100vh;
}

.app-header {
	background-color: var(--primary-color);
	color: var(--light-text-color);
	padding: 1rem;
	box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.app-nav {
	display: flex;
	justify-content: space-between;
	align-items: center;
	max-width: var(--desktop-breakpoint);
	margin: 0 auto;
}

.nav-link-brand {
	font-size: var(--font-size-large);
	font-weight: bold;
	color: var(--light-text-color);
	text-decoration: none;
}

.nav-link {
	color: var(--light-text-color);
	text-decoration: none;
	margin-left: 1rem;
	font-size: var(--font-size-medium);

	&:hover {
		text-decoration: underline;
	}
}

.nav-link-btn {
	background: none;
	border: none;
	color: var(--light-text-color);
	text-decoration: none;
	margin-left: 1rem;
	font-size: var(--font-size-medium);
	cursor: pointer;

	&:hover {
		text-decoration: underline;
	}
}

.app-main {
	flex-grow: 1;
	padding: 1%;
	display: grid;
	grid-template-columns: 100%;
	grid-template-rows: 100%;
}

@media (max-width: 768px) {
	.app-header {
		padding: 0.75rem;
	}

	.app-nav {
		flex-direction: column;
		align-items: flex-start;
		gap: 0.5rem;
	}

	.nav-link,
	.nav-link-btn {
		margin-left: 0;
		margin-right: 1rem;
	}

	.app-main {
		padding: 0.75rem;
	}
}
</style>
