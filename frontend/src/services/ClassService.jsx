// @ts-nocheck
import axiosInstance from "src/constants/axiosInstance";
import { Project } from "src/constants/project";

// 모임 리스트 가져오기
const getClassLists = async () => {
	const response = await axiosInstance.get(`${Project.API_URL}/login`, {
		withCredentials: true,
	});
	return response;
};

// 모임 생성
const postClassLists = async (body) => {
	const response = await axiosInstance.post(
		`${Project.API_URL}/login`,
		body,
		{ withCredentials: true },
	);
	return response;
};

// 모임 수정
const putClassLists = async (body) => {
	const response = await axiosInstance.put(`${Project.API_URL}/login`, body, {
		withCredentials: true,
	});
	return response;
};

// 모임 삭제
const deleteClassLists = async (body) => {
	const response = await axiosInstance.delete(
		`${Project.API_URL}/login`,
		body,
		{
			withCredentials: true,
		},
	);
	return response;
};

const ClassService = {
	getClassLists,
	postClassLists,
	putClassLists,
	deleteClassLists,
};

export { ClassService };
