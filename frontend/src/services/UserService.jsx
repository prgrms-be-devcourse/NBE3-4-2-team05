import axiosInstance from "../constant/axiosInstance";
import {
  API_URL,
  getJwt,
  REFRESH_TOKEN,
  removeCookie,
  setCookie,
} from "../constant/project";

// 회원가입
const signUp = async (body) => {
  try {
    const response = await axiosInstance.post(`${API_URL}/user/signup`, body);
    const res = response?.data?.value;
    if (res) {
      return res;
    } else {
      return {};
    }
  } catch (error) {
    throw new Error("회원가입 중 오류가 발생했습니다.");
  }
};

// 로그인
const signIn = async (body) => {
  try {
    const response = await axiosInstance.post(`${API_URL}/user/login`, body);
    if (response?.data?.accessToken && response?.data?.refreshToken) {
      setCookie(response.data.accessToken, response.data.refreshToken);
      return response.data;
    } else {
      return {};
    }
  } catch (error) {
    throw new Error("로그인 중 오류가 발생했습니다.");
  }
};

// 로그인
const logOut = async () => {
  const TOKEN = getJwt();
  try {
    const response = await axiosInstance.post(
      `${API_URL}/user/logout?accessToken=${TOKEN}`
    );
    if (response) {
      removeCookie("accessToken", TOKEN);
      removeCookie("refreshToken", REFRESH_TOKEN);
      return response;
    } else {
      return {};
    }
  } catch (error) {
    throw new Error("로그아웃 중 오류가 발생했습니다.");
  }
};

const UserService = {
  signUp,
  signIn,
  logOut,
};

export { UserService };
