// @ts-nocheck
import { Project } from "src/constants/project";
import useFetch from "src/hooks/useFetch";
// 회원가입
const SignUp = async (body) => {
	try {
		const response = await useFetch("/user/signup", "POST", body);
		if (response?.value) {
			return response.value;
		}
	} catch (error) {
		throw new Error("회원가입 중 오류가 발생했습니다.");
	}
};
// 로그인
const Login = async (body) => {
	try {
		const response = await useFetch("POST", "login", body);
		if (!response.ok) {
			throw new Error(`HTTP error! Status: ${response.status}`);
		}
		return await response.json();
	} catch (error) {
		console.error("Request failed:", error);
		return null;
	}
};
// 로그아웃
const LogOut = async () => {
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
	SignUp,
	Login,
	LogOut,
};

export { UserService };
