export default defineEventHandler(async (event) => {
  const { ticketId, versionId } = event.context.params;
  const backendUrl = process.env.BACKEND_URL || 'http://localhost:8080';
  const headers = getHeaders(event);

  try {
    const response = await $fetch(`${backendUrl}/api/tickets/${ticketId}/assign-version/${versionId}`, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': headers.authorization || ''
      },
    });
    return response;
  } catch (error) {
    console.error('Error assigning ticket to version:', error);
    return { error: 'Failed to assign ticket' };
  }
});
