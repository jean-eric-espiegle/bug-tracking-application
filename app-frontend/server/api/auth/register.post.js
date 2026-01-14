export default defineEventHandler(async (event) => {
  const body = await readBody(event);

  try {
    const backendUrl = 'http://localhost:8080';
    const response = await $fetch(`${backendUrl}/api/auth/register`, {
      method: 'POST',
      body: body,
      headers: { 'Content-Type': 'application/json' },
    });

    return response;
  } catch (error) {
    console.error('Error calling register API:', error);
    throw createError({
      statusCode: error.statusCode || 500,
      statusMessage: error.statusMessage || 'Internal Server Error',
      data: error.data,
    });
  }
});
