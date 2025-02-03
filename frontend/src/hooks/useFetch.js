import { Project } from "@/constants/project";

const TOKEN = Project.getJwt();

const useFetch = async (
  method = "GET",
  url = "",
  requestBody = {},
  headerOptions = {},
) => {
  const headers = {
    Pragma: "no-cache",
    "Cache-Control": "no-cache",
    Expires: "0",
    Authorization: TOKEN,
    ...headerOptions,
  };

  const config = {
    method,
    headers,
    body:
      method === "GET" || Object.keys(requestBody).length === 0
        ? undefined
        : JSON.stringify(requestBody),
  };

  try {
    const response = await fetch(`${Project.API_URL}/${url}`, config);

    if (!response.ok) {
      throw new Error(
        `HTTP error! Status: ${response.status} - ${response.statusText}`,
      );
    }

    const data = await response.json();
    return data;
  } catch (error) {
    // eslint-disable-next-line no-console
    console.error("Request failed:", error);
  }
};

export default useFetch;
