export default defineEventHandler(async (event) => {
  const { versionId } = event.context.params;
  const request = await readBody(event);
  const backendUrl = process.env.BACKEND_URL || 'http://localhost:8080';
  const headers = getHeaders(event);

  try {
    const response = await $fetch(`${backendUrl}/api/versions/${versionId}`, {
      method: 'PUT',
      body: request,
      headers: {
        'Content-Type': 'application/json',
        'Authorization': headers.authorization || ''
      },
    });
    return response;
  } catch (error) {
    console.error('Error updating version:', error);
    return { error: 'Failed to update version' };
  }
});
