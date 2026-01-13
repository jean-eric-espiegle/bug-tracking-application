export default defineEventHandler(async (event) => {
  const { organizationId } = event.context.params;
  const backendUrl = process.env.BACKEND_URL || 'http://localhost:8080';
  const headers = getHeaders(event);

  try {
    const response = await $fetch(`${backendUrl}/api/organizations/${organizationId}/tickets`, {
      method: 'GET',
      headers: {
        'Authorization': headers.authorization || ''
      },
    });
    return response;
  } catch (error) {
    console.error('Error fetching tickets by organization:', error);
    return { error: 'Failed to fetch tickets' };
  }
});
