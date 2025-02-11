import axiosInstance from "src/constants/axiosInstance";
import { Project } from "src/constants/project";

// 해당 모임에 대한 투표 정보
const getMyCheckIn = async (schduleId) => {
    const response = await axiosInstance.get(`${Project.API_URL}/checkin/${schduleId}/my`, {
        withCredentials: true,
    });
    if(response.data.data){
        return response.data.data
    }else{
        return false
    }

};

// 투표 생성 ( 첫 투표 시 생성 )
const postCheckIn = async (body) => {
    const response = await axiosInstance.post(
        `${Project.API_URL}/checkin`,
        body,
        { withCredentials: true },
    );
    return response;
};

// 투표 수정 ( 첫 투표 이후 사용 )
const putCheckIn = async (body) => {
    const response = await axiosInstance.put(
        `${Project.API_URL}/checkin`,
        body,
        { withCredentials: true },
    );
    return response;
};


const CheckInService = {
    getMyCheckIn,
    postCheckIn,
    putCheckIn,
}

export { CheckInService };