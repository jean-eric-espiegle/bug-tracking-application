export default defineEventHandler(async (event) => {
  const backendUrl = process.env.BACKEND_URL || 'http://localhost:8080';
  const headers = getHeaders(event);

  try {
    const response = await $fetch(`${backendUrl}/api/auth/logout`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': headers.authorization || ''
      },
    });
    return response;
  } catch (error) {
    console.error('Error during logout:', error);
    // For logout, we still want to clear client state even if server call fails
    // So we'll return success regardless
    return { success: true };
  }
});
