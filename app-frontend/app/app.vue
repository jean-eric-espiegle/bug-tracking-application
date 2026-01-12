<template>
	<div>
		<NotificationOverlay />
		<nav>
			<NuxtLink to="/login">Login</NuxtLink> |
			<NuxtLink to="/register">Register</NuxtLink>
		</nav>
		<hr />
		<NuxtPage />
	</div>
</template>

<script setup>
import NotificationOverlay from '~/components/NotificationOverlay.vue';
</script>
