import useFetch from "@/hooks/useFetch";
import { Project } from "@/constants/project";

// 회원가입
const signUp = async (body = {}) => {
  try {
    const response = await useFetch("/user/signup", "POST", body);

    if (response?.value) {
      return response.value;
    } else {
      return {};
    }
  } catch (error) {
    throw new Error("회원가입 중 오류가 발생했습니다.");
  }
};

// 로그인
const signIn = async (body = {}) => {
  try {
    const response = await useFetch("POST", "/user/login", body);

    if (response?.accessToken && response?.refreshToken) {
      Project.setJwt(response.accessToken, response.refreshToken);
      return response;
    } else {
      return {};
    }
  } catch (error) {
    throw new Error("로그인 중 오류가 발생했습니다.");
  }
};
// 로그아웃
const logOut = async () => {
  const TOKEN = Project.getJwt();
  try {
    const response = await useFetch(
      "POST",
      `/user/logout?accessToken=${TOKEN}`,
    );

    if (response) {
      Project.removeCookie("accessToken", {
        path: "/",
        domain: process.env.REACT_APP_DEFAULT_UR,
      });
      Project.removeCookie("refreshToken", {
        path: "/",
        domain: process.env.REACT_APP_DEFAULT_UR,
      });
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
