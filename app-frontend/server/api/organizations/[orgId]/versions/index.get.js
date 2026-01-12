export default defineEventHandler(async (event) => {
  const { orgId } = event.context.params;
  const backendUrl = process.env.BACKEND_URL || 'http://localhost:8080';
  const headers = getHeaders(event);

  try {
    const response = await $fetch(`${backendUrl}/api/organizations/${orgId}/versions`, {
      method: 'GET',
      headers: {
        'Authorization': headers.authorization || ''
      },
    });
    return response;
  } catch (error) {
    console.error('Error fetching versions for organization:', error);
    return { error: 'Failed to fetch versions' };
  }
});
