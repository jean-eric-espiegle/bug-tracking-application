export default defineEventHandler(async (event) => {
  const { organizationId } = event.context.params;
  const backendUrl = process.env.BACKEND_URL || 'http://localhost:8080';
  const headers = getHeaders(event);

  try {
    const response = await $fetch(`${backendUrl}/api/organizations/${organizationId}`, {
      method: 'DELETE',
      headers: {
        'Authorization': headers.authorization || ''
      },
    });
    return response;
  } catch (error) {
    console.error('Error deleting organization:', error);
    // Re-throw 401 errors so the auth plugin can intercept them
    if (error?.response?.status === 401) {
      throw createError({
        statusCode: 401,
        statusMessage: 'Unauthorized'
      });
    }

    throw createError({
      statusCode: error?.response?.status || 500,
      statusMessage: error?.response?.statusText || 'Failed to delete organization'
    });
  }
});