import { Project } from "src/constants/project";

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
		"Content-Type": "application/json",
		Accept: "application/json",
		"X-Requested-With": "XMLHttpRequest",
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
		console.error("Request failed:", error);
	}
};

export default useFetch;
