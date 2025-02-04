// @ts-nocheck
import { Project } from "src/constants/project";

const useFetch = async (method, url, requestBody) => {
	// const TOKEN = await Project.getJwt();
	// 		...(TOKEN && { Authorization: `Bearer ${TOKEN}` })
	const headers = {
		Pragma: "no-cache",
		"Cache-Control": "no-cache",
		Expires: "0",
		"Content-Type": "application/json",
		Accept: "application/json",
		"X-Requested-With": "XMLHttpRequest",
	};
	const response = await fetch(`${Project.API_URL}/${url}`, {
		method: method,
		headers: headers,
		credentials: "include",
		body: method === "GET" ? undefined : JSON.stringify(requestBody),
	});
	if (!response.ok) {
		throw new Error(`HTTP error! Status: ${response.status}`);
	}
	return await response.json();
};

export default useFetch;
