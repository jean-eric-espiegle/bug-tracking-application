export default defineEventHandler(async (event) => {
    const organizationId = event.context.params.organizationId;
    const { search } = getQuery(event);
    const searchTerm = search || '';

    try {
        const response = await $fetch(`http://localhost:8080/api/organizations/${organizationId}/members?search=${searchTerm}`, {
            method: 'GET',
            headers: {
                'Authorization': event.headers.get('Authorization'),
            },
        });
        return response;
    } catch (error) {
        throw createError({
            statusCode: error.statusCode || 500,
            statusMessage: error.statusMessage || 'Internal Server Error',
            data: error.data,
        });
    }
});
