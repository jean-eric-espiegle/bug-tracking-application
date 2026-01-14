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
    // Re-throw 401 errors so the auth plugin can intercept them
    if (error?.response?.status === 401) {
      throw createError({
        statusCode: 401,
        statusMessage: 'Unauthorized'
      });
    }
    
        return { error: 'Failed to validate email' };
  }
});
