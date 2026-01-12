export default defineEventHandler(async (event) => {
  const { ticketId } = event.context.params;
  const backendUrl = process.env.BACKEND_URL || 'http://localhost:8080';
  const headers = getHeaders(event);

  try {
    const response = await $fetch(`${backendUrl}/api/tickets/${ticketId}`, {
      method: 'GET',
      headers: {
        'Authorization': headers.authorization || ''
      },
    });
    return response;
  } catch (error) {
    console.error('Error fetching ticket:', error);
    return { error: 'Failed to fetch ticket' };
  }
});
