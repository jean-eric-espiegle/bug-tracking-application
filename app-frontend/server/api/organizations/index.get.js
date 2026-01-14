export default defineEventHandler(async (event) => {
  const backendUrl = process.env.BACKEND_URL || 'http://localhost:8080';
  const headers = getHeaders(event);

  try {
    const response = await $fetch(`${backendUrl}/api/organizations`, {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': headers.authorization || ''
      },
    });
    return response;
  } catch (error) {
    console.error('Error fetching organizations:', error);

    // Re-throw 401 errors so the auth plugin can intercept them
    if (error?.response?.status === 401) {
      throw createError({
        statusCode: 401,
        statusMessage: 'Unauthorized'
      });
    }

    // For other errors, return the error message
    return { error: 'Failed to fetch organizations' };
  }
});