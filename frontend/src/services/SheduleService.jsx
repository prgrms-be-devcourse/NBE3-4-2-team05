import useFetch from "src/hooks/useFetch";
// 일정 가져오기
const getSchedulesLists = () => useFetch("GET", "schedules/");

// 일정 생성
const postSchedulesLists = (body = {}) => useFetch("POST", "schedules/", body);

// 일정 수정
const putSchedulesLists = () => useFetch("PUT", "schedules/");

// 일정 삭제
const deleteSchedulesLists = () => useFetch("DELETE", "schedules/");

const ScheduleService = {
	getSchedulesLists,
	postSchedulesLists,
	putSchedulesLists,
	deleteSchedulesLists,
};

export { ScheduleService };
