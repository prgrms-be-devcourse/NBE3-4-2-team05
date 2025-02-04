// @ts-nocheck
import { Cookies } from "react-cookie";

const cookies = new Cookies();

const DOMAIN = process.env.REACT_APP_DEFAULT_URL;
const API_URL = process.env.REACT_APP_API_URL;
const PROJECT_ID = process.env.REACT_APP_PROJECT_ID;

const TOKEN = process.env.REACT_APP_ACCESS_TOKEN;
const REFRESH_TOKEN = process.env.REACT_APP_REFRESH_TOKEN;
const USER_SESSION = `ID_${PROJECT_ID}_SES`;

const getJwt = () => cookies.get(TOKEN);

const setJwt = (accessToken = "", refreshToken = "") => {
	cookies.set(TOKEN, accessToken, {
		path: "/",
		domain: DOMAIN,
		secure: true,
		sameSite: "Strict",
	});

	cookies.set(REFRESH_TOKEN, refreshToken, {
		path: "/",
		domain: DOMAIN,
		secure: true,
		sameSite: "Strict",
	});
};

const setUserId = (user = "") => {
	cookies.set(USER_SESSION, user, {
		path: "/",
		domain: DOMAIN,
		secure: true,
		sameSite: "strict",
	});
};

const getUserId = () => cookies.get(USER_SESSION) || "";

const loginCheck = () => !!(getJwt() && getUserId());

const removeCookie = (name = "", options = {}) => cookies.remove(name, options);

const cookieRemove = () => {
	return new Promise((resolve) => {
		removeCookie(TOKEN, { path: "/", domain: DOMAIN });
		removeCookie(USER_SESSION, { path: "/", domain: DOMAIN });
		resolve(true);
	});
};

// 로컬 스토리지 제거
const removeStorage = () => {
	localStorage.clear();
	sessionStorage.clear();
};

// 로그아웃 처리
const userLogout = () => {
	removeStorage();
	cookieRemove();
	window.location.reload();
};

const Project = {
	setJwt,
	getJwt,
	getUserId,
	setUserId,
	loginCheck,
	removeCookie,
	userLogout,
	removeStorage,
	cookieRemove,
	DOMAIN,
	API_URL,
	PROJECT_ID,
	USER_SESSION,
	REFRESH_TOKEN,
};

export { Project };
