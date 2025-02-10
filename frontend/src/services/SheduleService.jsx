// @ts-nocheck
import axiosInstance from "src/constants/axiosInstance";
import { Project } from "src/constants/project";

// 모임 일정 전체 리스트 가져오기
const getSchedulesLists = async () => {
	const response = await axiosInstance.get(`${Project.API_URL}/schedules/classes/{classId}`, {
		withCredentials: true,
	});
	return response;
};

// 모임 일정 가져오기
const getScheduleDetail = async () => {
	const response = await axiosInstance.get(`${Project.API_URL}/schedules/{scheduleId}/classes/{classId}`, {
		withCredentials: true,
	});
	return response;
};

// 일정 생성
const postSchedulesLists = async (body) => {
	console.log(body);
	const response = await axiosInstance.post(
		`${Project.API_URL}/schedules/classes`,
		body,
		{
			withCredentials: true,
		},
	);
	return response;
};

// 일정 수정
const putSchedulesLists = async (body) => {
	const response = await axiosInstance.put(`${Project.API_URL}/schedules/{scheduleId}/classes/{classId}`, body, {
		withCredentials: true,
	});
	return response;
};

// 일정 삭제
const deleteSchedulesLists = async (body) => {
	const response = await axiosInstance.delete(
		`${Project.API_URL}/schedules/{scheduleId}/classes/{classId}`,
		body,
		{
			withCredentials: true,
		},
	);
	return response;
};

const ScheduleService = {
	getSchedulesLists,
	postSchedulesLists,
	putSchedulesLists,
	deleteSchedulesLists,
};

export { ScheduleService };
