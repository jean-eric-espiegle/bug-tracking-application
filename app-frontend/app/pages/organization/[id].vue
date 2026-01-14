<template>
	<div class="organization-page">
		<OrganizationDetails :organization-id="organizationId" />
	</div>
</template>

<script setup>
import OrganizationDetails from '~/components/OrganizationDetails.vue';

const route = useRoute();
const organizationId = parseInt(route.params.id);

if (isNaN(organizationId)) {
	throw createError({
		statusCode: 404,
		statusMessage: 'Organization not found'
	});
}

// Set page title
useHead({
	title: `Organization ${organizationId}`
});
</script>

<style scoped>
.organization-page {
	min-height: 100vh;
	background-color: #f8f9fa;
	padding: 20px;
}
</style>
