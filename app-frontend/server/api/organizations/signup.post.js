export default defineEventHandler(async (event) => {
  const request = await readBody(event);
  const backendUrl = process.env.BACKEND_URL || 'http://localhost:8080';
  const headers = getHeaders(event);
  
  try {
    const response = await $fetch(`${backendUrl}/api/organizations/signup`, {
      method: 'POST',
      body: request,
      headers: {
        'Content-Type': 'application/json',
        'Authorization': headers.authorization || ''
      },
    });
    return response;
  } catch (error) {
    console.error('Error in organization signup:', error);
    return { error: 'Failed to sign up organization' };
  }
});
