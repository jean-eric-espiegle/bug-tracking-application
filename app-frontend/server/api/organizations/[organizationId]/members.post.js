export default defineEventHandler(async (event) => {
  const { organizationId } = event.context.params;
  const request = await readBody(event);
  const backendUrl = process.env.BACKEND_URL || 'http://localhost:8080';
  const headers = getHeaders(event);

  try {
    const response = await $fetch(`${backendUrl}/api/organizations/${organizationId}/members`, {
      method: 'POST',
      body: request,
      headers: {
        'Content-Type': 'application/json',
        'Authorization': headers.authorization || ''
      },
    });
    return response;
  } catch (error) {
    console.error('Error adding member:', error);
    // Re-throw 401 errors so the auth plugin can intercept them
    if (error?.response?.status === 401) {
      throw createError({
        statusCode: 401,
        statusMessage: 'Unauthorized'
      });
    }
    
        return { error: 'Failed to add member' };
  }
});
