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
		console.log("로그아웃 성공");
		Project.removeCookie("accessToken", Project.getJwt());
		Project.removeCookie("RefreshToken", Project.REFRESH_TOKEN);
		return response;
	} else {
		return {};
	}
};

// 회원 정보 검색
const getUserInfo = async () => {
	const response = await axiosInstance.get(
		`${Project.API_URL}/users`,
		{ withCredentials: true }
	);
	if (response) {
		console.log("회원정보 조회 성공");
		return response;
	} else {
		return {};
	}
};

// 회원 모임 리스트 조회
const getUserClassInfo = async () => {
	const response = await axiosInstance.get(
		`${Project.API_URL}/users/classes`,
		{ withCredentials: true }
	);
	if (response) {
		console.log("회원 모임 리스트 조회 성공");	
		return response;
	} else {
		return {};
	}
};

// 회원 모임 리스트 조회
const getUserScheduleInfo = async () => {
	const response = await axiosInstance.get(
		`${Project.API_URL}/users/schedules`,
		{ withCredentials: true }
	);
	if (response) {
		console.log("회원 스케줄 리스트 조회 성공");	
		return response;
	} else {
		return {};
	}
};

// 회원 정보 수정
const ModifyUserInfo = async (body) => {
	const response = await axiosInstance.patch(
		`${Project.API_URL}/users/profile`,
		body,
		{ withCredentials: true },
	);
	return response;
}

// 카카오 로그인
const KakaoLogin = async (body) => {
	window.location.href = `https://kauth.kakao.com/oauth/authorize?client_id=e51b8e3f5ed968045b77ab97528fbc49&redirect_uri=http://localhost:8080/api/v1/login/kakao&response_type=code`;
};

const UserService = {
	SignUp,
	Login,
	Logout,
	getUserInfo,
	getUserClassInfo,
	getUserScheduleInfo,
	ModifyUserInfo,
	KakaoLogin,
};

export { UserService };
