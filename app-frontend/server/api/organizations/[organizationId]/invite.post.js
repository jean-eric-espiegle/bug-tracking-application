export default defineEventHandler(async (event) => {
  const { organizationId } = event.context.params;
  const request = await readBody(event);
  const backendUrl = process.env.BACKEND_URL || 'http://localhost:8080';
  const headers = getHeaders(event);

  try {
    const response = await $fetch(`${backendUrl}/api/organizations/${organizationId}/invite`, {
      method: 'POST',
      body: request,
      headers: {
        'Content-Type': 'application/json',
        'Authorization': headers.authorization || ''
      },
    });
    return response;
  } catch (error) {
    console.error('Error inviting member:', error);
    if (error?.response?.status === 401) {
      throw createError({
        statusCode: 401,
        statusMessage: 'Unauthorized'
      });
    }

    return { error: 'Failed to invite member' };
  }
});
