export default defineEventHandler(async (event) => {
  const { versionId } = event.context.params;
  const backendUrl = process.env.BACKEND_URL || 'http://localhost:8080';
  const headers = getHeaders(event);

  try {
    const response = await $fetch(`${backendUrl}/api/versions/${versionId}`, {
      method: 'GET',
      headers: {
        'Authorization': headers.authorization || ''
      },
    });
    return response;
  } catch (error) {
    console.error('Error fetching version:', error);
    if (error?.response?.status === 401) {
      throw createError({
        statusCode: 401,
        statusMessage: 'Unauthorized'
      });
    }
    
        return { error: 'Failed to fetch version' };
  }
});
