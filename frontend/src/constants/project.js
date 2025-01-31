import { Cookies } from "react-cookie";

const DOMAIN = process.env.REACT_APP_DEFAULT_UR;
const API_URL = process.env.REACT_APP_API_URL;
const PROJECT_ID = process.env.REACT_APP_PROJECT_ID;
const TOKEN = `accessToken`;
const REFRESH_TOKEN = `refreshToken`;
const USER_SESSION = `ID_${PROJECT_ID}_SES`;

const cookies = new Cookies();

const getJwt = () => {
  return getCookie(TOKEN);
};
const setJwt = (token) => {
  return setCookie(TOKEN, token, { path: "/", domain: DOMAIN });
};
const setUserId = (user) => {
  return setCookie(USER_SESSION, user, { path: "/", domain: DOMAIN });
};
const loginCheck = () => {
  return !!(getJwt() && getUserId());
};

const removeCookie = (name, option) => {
  cookies.remove(name, option);
};

const setCookie = (accessToken, refreshToken) => {
  cookies.set("accessToken", accessToken, {
    path: "/",
    maxAge: 60 * 60,
    secure: true,
    sameSite: "Strict",
  });

  cookies.set("refreshToken", refreshToken, {
    path: "/",
    maxAge: 60 * 60 * 24 * 7,
    secure: true,
    sameSite: "Strict",
  });
};

const getCookie = (name) => {
  return cookies.get(name);
};
const getUserId = () => {
  return getCookie(USER_SESSION) || "";
};
const cookieRemove = () => {
  return new Promise((resolve) => {
    removeCookie(TOKEN, { path: "/", domain: DOMAIN });
    removeCookie(USER_SESSION, { path: "/", domain: DOMAIN });
    resolve(true);
  });
};
const removeStorage = () => {
  localStorage.clear();
  sessionStorage.clear();
};
const userLogout = () => {
  removeStorage();
  cookieRemove();
  window.location.reload();
};

export {
  setJwt,
  getJwt,
  getUserId,
  setUserId,
  setCookie,
  getCookie,
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
