export default defineEventHandler(async (event) => {
  const { userId, ...dto } = await readBody(event);
  const backendUrl = process.env.BACKEND_URL || 'http://localhost:8080';
  
  try {
    const response = await $fetch(`${backendUrl}/api/enterprise/request?userId=${userId}`, {
      method: 'POST',
      body: dto,
      headers: {
        'Content-Type': 'application/json',
      },
    });
    return response;
  } catch (error) {
    console.error('Error submitting enterprise request:', error);
    return { error: 'Failed to submit enterprise request' };
  }
});
