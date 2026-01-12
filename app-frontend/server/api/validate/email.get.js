export default defineEventHandler(async (event) => {
  const { email } = getQuery(event);
  const backendUrl = process.env.BACKEND_URL || 'http://localhost:8080';

  try {
    const response = await $fetch(`${backendUrl}/api/validate/email?email=${email}`, {
      method: 'GET',
    });
    return response;
  } catch (error) {
    console.error('Error validating email:', error);
    return { error: 'Failed to validate email' };
  }
});
