import { API_URL } from "../constant/project";
import { requestHandler } from "../hooks/requestHandler";

// 일정 가져오기
const getSchedulesLists = () => requestHandler("get", API_URL);

// 일정 생성
const postSchedulesLists = (body) => requestHandler("post", API_URL, body);

// 일정 수정
const putSchedulesLists = () => requestHandler("delete", API_URL);

// 일정 삭제
const deleteSchedulesLists = () => requestHandler("delete", API_URL);

const ScheduleService = {
  getSchedulesLists,
  postSchedulesLists,
  putSchedulesLists,
  deleteSchedulesLists,
};

export { ScheduleService };
