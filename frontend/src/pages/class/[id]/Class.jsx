import React, {useCallback, useEffect, useState} from "react";
import { useParams } from "react-router-dom";
import Alert from "src/components/alert/Alert";
import { ClassService } from "src/services/ClassService";
import { useNavigate } from "react-router-dom";
import Modal from "src/components/modal/Modal";
import DateTimeInput from "../../../components/dateTimeInput/DateTimeInput";
import { ScheduleService } from "../../../services/SheduleService";
import CustomList from "../../../components/customList/CustomList";
import "./Class.css";
import {CheckInService} from "../../../services/CheckInService";

const Class = () => {
  const { id } = useParams();
  const [classInfo, setClassInfo] = useState([]);
  const [name, setName] = useState("");
  const [meetingTime, setMeetingTime] = useState("");
  const [meetingTitle, setMeetingTitle] = useState("");
  const [description, setDescription] = useState("");
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [isSchdulesModal, setIsSchedulesModal] = useState(false);
  const router = useNavigate();
  const [schedules, setSchedules] = useState([]);
  const [selectedSchedule, setSelectedSchedule] = useState(null);
  const [isDetailModal, setIsDetailModal] = useState(false);

  const handleMeetingTimeChange = useCallback((formattedDateTime) => {
    setMeetingTime(formattedDateTime);
  }, []);

  const result = () => {
    router("/");
  };

  const openModal = () => setIsModalOpen(true);
  const schedulesModal = () => setIsSchedulesModal(true);
  const closeModal = () => setIsModalOpen(false);
  const closeSchedulesModal = () => setIsSchedulesModal(false);
  const closeSchedulesDetailModal = () => setIsDetailModal(false);

  const modifyResult = () => {
    closeModal();
    setTimeout(() => {
      window.location.reload();
    }, 100);
  };

  useEffect(() => {
    const fetchClassInfo = async () => {
      try {
        const response = await ClassService.getClassInfo(id);
        const jsonData = response.data;
        const classData = jsonData?.data || [];

        setClassInfo(classData);
      } catch (error) {
        console.error("Error fetching class info:", error);
        Alert(error.response.data.message, "", "", () => result());
      }
    };

    const fetchSchedules = async () => {
      try {
        // 일정 리스트 가져오기
        const schedulesResponse = await ScheduleService.getSchedulesList(id);
        const schedulesData = schedulesResponse.data?.data || [];
        setSchedules(schedulesData);
      } catch (error) {
        console.error("일정 데이터 로딩 오류:", error);
        Alert(error.response.data.message, "", "", () => result());
      }
    }
    fetchClassInfo();
    fetchSchedules();
  }, [id]);

  // 모임 탈퇴
  const handlerResignClass = async () => {
    try {
      const response = await ClassService.resignClass(id);
      if (response.status === 200) {
        Alert("모임에서 탈퇴되었습니다.", "", "", () => result());
      } else {
        Alert("탈퇴에 실패했습니다");
      }
    } catch (error) {
      console.error("모임 탈퇴 오류:", error);
      Alert(error.response.data.message);
    }
  };

  // 모임 수정
  const handlerModifyClass = async () => {
    const body = {
      name: name,
      description: description,
    };
    try {
      const response = await ClassService.putClassLists(body, id);
      if (response.status === 200) {
        Alert("모임 정보가 수정되었습니다.", "", "", () => modifyResult());
      } else {
        Alert("모임 정보 수정에 실패했습니다");
      }
    } catch (error) {
      console.error("모임 수정 오류:", error);
      if (error.response.data.code === 9000)
        Alert(error.response.data.message);
    }
  };

  // 모임 삭제
  const handlerDeleteClass = async () => {
    try {
      const response = await ClassService.deleteClassLists(id);
      if (response.status === 200) {
        Alert("모임이 삭제되었습니다.");
      } else {
        Alert("모임 삭제에 실패했습니다");
      }
    } catch (error) {
      console.error("모임 삭제 오류:", error);
      Alert(error.response.data.message);
    }
  };

  // 일정 생성
  const handlerCreateSchedule = async () => {
    const body = {
      classId: id,
      meetingTime: meetingTime,
      meetingTitle: meetingTitle,
    };
    try {
      const response = await ScheduleService.postSchedulesLists(body);
      if (response.status === 201) {
        Alert("일정이 생성되었습니다.", "", "", () => {
          closeSchedulesModal();
          window.location.reload(); // 페이지 새로고침
        });
      } else {
        Alert("일정 생성에 실패했습니다");
      }
    } catch (error) {
      console.error("일정 생성 오류:", error);
      if (error.response.data.code === 9000)
        Alert(`${error.response.data.message}`);
    }
  };

  //일정 상세 조회
  const handlerScheduleDetail = (scheduleId) => {
    router(`/schedules/${scheduleId}/classes/${id}`);
  };

  // 투표 함수
  const handleCheckIn = async (scheduleId,checkIn) => {
      const response = await CheckInService.getMyCheckIn(scheduleId);
      if (!response) {
      const postResponse = await CheckInService.postCheckIn({scheduleId, checkIn});
      if (postResponse.status === 200) {
        Alert("투표가 되었습니다.");
      } else {
        Alert("투표에 실패했습니다.");
      }
    } else {
      const putResponse = await CheckInService.putCheckIn({scheduleId, checkIn});
      if (putResponse.status === 200) {
        Alert("투표가 변경되었습니다.");
      } else {
        Alert("투표 변경에 실패했습니다.");
      }}
  };


  return (
      <div>
        <div className="buttons">
          <button className="custom-button" onClick={schedulesModal}>
            일정 생성
          </button>
          <button className="custom-button" onClick={handlerResignClass}>
            모임 탈퇴
          </button>
          <button className="custom-button" onClick={openModal}>
            모임 수정
          </button>
          <button className="custom-button" onClick={handlerDeleteClass}>
            모임 삭제
          </button>
          <button
              className="custom-button"
              onClick={() => router(`/memberList/${id}`)}
          >
            회원 관리
          </button>
        </div>

        <div className="class-info-container">
          <p>모임 이름 : {classInfo.name}</p>
          <p>모임 관심사 : {classInfo.favorite}</p>
          <p>모임 설명 : {classInfo.description}</p>
        </div>

        {/*모임 일정 리스트*/}
        <div className="schedules-container">
          <h3>일정 목록</h3>
          {schedules.length > 0 && schedules.map((schedule) => (
              <CustomList
                  data1={schedule?.scheduleId}
                  data2={schedule?.meetingTitle}
                  data3={schedule?.meetingTime}
                  description="true"
                  button1 ="참석"
                  onClick1={()=>handleCheckIn(schedule?.scheduleId,true)}
                  button2 = "불참석"
                  onClick2={()=>handleCheckIn(schedule?.scheduleId,false)}
                  button3="상세보기"
                  check
                  onClick3={()=>handlerScheduleDetail(schedule.scheduleId)}
              />)
          )}
        </div>

        <div className="list-container">
          <ul className="list">{/* memberList.jsx 참고 */}</ul>
        </div>

        <Modal isOpen={isModalOpen} title={"모임 정보 수정"} onClose={closeModal}>
          <div className="modal-form">
            <label htmlFor="name">모임 이름:</label>
            <input
                id="name"
                type="text"
                value={name}
                onChange={(e) => setName(e.target.value)}
                placeholder="모임 이름을 입력하세요"
            />
            <label htmlFor="description">모임 설명:</label>
            <textarea
                id="description"
                value={description}
                onChange={(e) => setDescription(e.target.value)}
                placeholder="모임 설명을 입력하세요"
            />
            <button className="custom-button" onClick={handlerModifyClass}>
              수정하기
            </button>
          </div>
        </Modal>

        {/*일정 생성*/}
        <Modal isOpen={isSchdulesModal} title={"일정 생성"} onClose={closeSchedulesModal}>
          <div className="modal-form">
            <label>일정 제목:</label>
            <input
                type="text"
                value={meetingTitle}
                onChange={(e) => setMeetingTitle(e.target.value)}
                placeholder="일정 제목을 입력하세요"
            />
            <DateTimeInput onMeetingTimeChange={handleMeetingTimeChange} />
            <button className="custom-button" onClick={handlerCreateSchedule}>
              생성하기
            </button>
          </div>
        </Modal>
      </div>
  );
};

export default Class;
