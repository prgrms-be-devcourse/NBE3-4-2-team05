/**
 * Base64 URL 디코딩 함수
 * @param {string} base64Url - Base64 URL 인코딩된 문자열
 * @returns {string} 디코딩된 문자열
 */
const decodeBase64Url = (base64Url) => {
	const base64 = base64Url.replace("-", "+").replace("_", "/"); // Base64 URL을 일반 Base64로 변경
	return atob(base64); // atob()로 Base64 디코딩
};

/**
 * Base64 디코딩 함수
 * @param {string} base64 - Base64 인코딩된 문자열
 * @returns {string | null} 디코딩된 문자열
 */
const decodeBase64 = (base64) => {
	try {
		return atob(base64);
	} catch (error) {
		console.error("Base64 디코딩 오류:", error);
		return null;
	}
};

/**
 * URL 디코딩 함수 (URL에 있는 %XX 형태의 이스케이프된 문자열을 디코딩)
 * @param {string} url - URL 인코딩된 문자열
 * @returns {string | null} 디코딩된 문자열
 */
const decodeURIComponentSafe = (url) => {
	try {
		return decodeURIComponent(url);
	} catch (error) {
		console.error("URL 디코딩 오류:", error);
		return null;
	}
};

/**
 * JWT 디코딩 함수
 * @param {string} token - JWT 토큰
 * @returns {Object|null} 디코딩된 페이로드 객체 또는 null
 */
const decodeJWT = (token) => {
	try {
		const [, payload] = token.split(".");
		if (!payload) return null;

		// Base64 URL 디코딩하여 페이로드 추출
		const decodedPayload = decodeBase64Url(payload);

		// URL 디코딩 및 JSON 파싱
		const jsonPayload = decodeURIComponent(
			decodedPayload
				.split("")
				.map(
					(c) =>
						"%" + ("00" + c.charCodeAt(0).toString(16)).slice(-2),
				)
				.join(""),
		);

		return JSON.parse(jsonPayload);
	} catch (error) {
		console.error("JWT 디코딩 오류:", error);
		return null;
	}
};

const Decode = {
	jwt: decodeJWT,
	base64Url: decodeBase64Url,
	base64: decodeBase64,
	url: decodeURIComponentSafe,
};

export { Decode };
