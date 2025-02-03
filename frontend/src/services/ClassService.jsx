import useFetch from "@/hooks/useFetch";

// 모임 리스트 가져오기
const getClassLists = () => useFetch("GET", "classes/");

// 모임 생성
const postClassLists = (body = {}) => useFetch("POST", "schedules/", body);

// 모임 수정
const putClassLists = () => useFetch("PUT", "schedules/");

// 모임 삭제
const deleteClassLists = () => useFetch("DELETE", "schedules/");

const ClassService = {
  getClassLists,
  postClassLists,
  putClassLists,
  deleteClassLists,
};

export { ClassService };
