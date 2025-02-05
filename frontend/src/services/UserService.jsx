// @ts-nocheck
import axiosInstance from "src/constants/axiosInstance";
import { Project } from "src/constants/project";

// 회원가입
const SignUp = async (body) => {
	const response = await axiosInstance.post(
		`${Project.API_URL}/signup`,
		body,
		{ withCredentials: true },
	);
	return response;
};

// 로그인
const Login = async (body) => {
	const response = await axiosInstance.post(
		`${Project.API_URL}/login`,
		body,
		{ withCredentials: true },
	);
	const accessToken = response.headers["authorization"];
	if (accessToken) {
		Project.setJwt(accessToken);
		return response;
	}
};

// 로그아웃
const Logout = async () => {
	const response = await axiosInstance.post(`${Project.API_URL}/logout`);
	if (response) {
		Project.removeCookie("accessToken", Project.getJwt());
		Project.removeCookie("RefreshToken", Project.REFRESH_TOKEN);
		return response;
	} else {
		return {};
	}
};

const UserService = {
	SignUp,
	Login,
	Logout,
};

export { UserService };
