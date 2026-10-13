const API_BASE_URL = import.meta.env.VITE_API_LOCAL_BASE_URL;

const apiUrl = async (payload, pathToApi) => {
  try {
    const response = await fetch(`${API_BASE_URL}${pathToApi}`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(payload),
    });

    const data = await response.json();

    if (!response.ok) {
      throw new Error(
        data?.message || `Request failed with status ${response.status}`
      );
    }

    return data;
  } catch (error) {
    if (error instanceof TypeError) {
      throw new Error(
        "Unable to connect to the backend. Check the backend status and CORS configuration."
      );
    }

    throw error;
  }
};

export default apiUrl;