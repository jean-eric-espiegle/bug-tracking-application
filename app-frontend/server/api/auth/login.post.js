export default defineEventHandler(async (event) => {
  const body = await readBody(event);

  try {
    // IMPORTANT: Replace with your actual backend URL
    const backendUrl = 'http://localhost:8080';
    const response = await $fetch(`${backendUrl}/api/auth/login`, {
      method: 'POST',
      body: body,
      headers: { 'Content-Type': 'application/json' },
    });

    return response;
  } catch (error) {
    console.error('Error calling login API:', error);
    // Forward the error from the backend
    throw createError({
      statusCode: error.statusCode || 500,
      statusMessage: error.statusMessage || 'Internal Server Error',
      data: error.data,
    });
  }
});
