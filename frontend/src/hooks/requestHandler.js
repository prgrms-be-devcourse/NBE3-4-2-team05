import axios from "axios";
import axiosInstance from "../constants/axios";

const requestHandler = async (method, url, body = null) => {
  try {
    let response;
    switch (method.toLowerCase()) {
      case "get":
        response = await axiosInstance.get(url);
        break;
      case "post":
        response = await axiosInstance.post(url, body);
        break;
      case "delete":
        response = await axiosInstance.delete(url);
        break;
      case "put":
        response = await axiosInstance.put(url, body);
        break;
      default:
        throw new Error("지원되지 않는 HTTP 메서드입니다.");
    }
    // 응답 코드 정상
    if (response && response.status >= 200 && response.status < 300) {
      return response.data;
    } else {
      throw new Error(`Unexpected status code: ${response.status}`);
    }
  } catch (error) {
    let errorMessage = "요청 중 오류가 발생했습니다.";

    // Axios 오류 처리
    if (axios.isAxiosError(error)) {
      if (error.response) {
        // 서버가 응답을 보낸 경우
        errorMessage = `서버 오류: ${error.response.status} - ${error.response.data.message || error.response.statusText}`;
      } else if (error.request) {
        // 서버로 요청을 보냈지만 응답을 받지 못한 경우
        errorMessage = "서버 응답 없음. 네트워크를 확인해주세요.";
      } else {
        // 요청 설정 중 오류
        errorMessage = `요청 설정 오류: ${error.message}`;
      }
    } else {
      // 다른 종류의 에러
      errorMessage = error.message;
    }

    throw new Error(errorMessage);
  }
};

export { requestHandler };
