import axios from "axios";
import { API_URL, getJwt } from "./project";

const TOKEN = getJwt();

const axiosInstance = axios.create({
  baseURL: API_URL,
  timeout: 1000 * 30,
  headers: {
    Pragma: "no-cache",
    CacheControl: "no-cache",
    Expires: "0",
    Authorization: TOKEN,
  },
});

axiosInstance.interceptors.request.use(
  (config) => {
    const configClone = config;
    configClone.headers.Authorization = TOKEN ? `Bearer ${TOKEN}` : "";
    configClone.headers.usertype = "user";
    return configClone;
  },
  (error) => {
    return Promise.reject(error);
  }
);

axiosInstance.interceptors.response.use(
  (response) => {
    return response;
  },
  (error) => {
    return Promise.resolve(error);
  }
);

export default axiosInstance;
