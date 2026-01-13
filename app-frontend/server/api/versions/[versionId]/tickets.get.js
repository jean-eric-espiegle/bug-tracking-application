export default defineEventHandler(async (event) => {
  const { versionId } = event.context.params;
  const backendUrl = process.env.BACKEND_URL || 'http://localhost:8080';
  const headers = getHeaders(event);

  try {
    const response = await $fetch(`${backendUrl}/api/versions/${versionId}/tickets`, {
      method: 'GET',
      headers: {
        'Authorization': headers.authorization || ''
      },
    });
    return response;
  } catch (error) {
    console.error('Error fetching tickets by version:', error);
    return { error: 'Failed to fetch tickets' };
  }
});
