export const handleApiError = (error) => {
  if (!error.statusCode) {
    console.error('An unexpected error occurred:', error);
    return 'An unexpected error occurred. Please try again.';
  }

  const { statusCode } = error;

  switch (statusCode) {
    case 403:
      return 'Forbidden: You do not have permission to perform this action.';
    case 404:
      return 'Not Found: The requested resource could not be found.';
    case 500:
      return 'Server Error: An internal server error occurred. Please try again later.';
    default:
      if (statusCode >= 200 && statusCode < 300) {
        return 'Success';
      }
      return `An error occurred: ${error.statusMessage || 'Please try again.'}`;
  }
};
