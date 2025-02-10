import React, {useCallback, useEffect, useState} from "react";
import { useParams } from "react-router-dom";
import Alert from "src/components/alert/Alert";
import { ClassService } from "src/services/ClassService";
import { useNavigate } from "react-router-dom";
import Modal from "src/components/modal/Modal";
import DateTimeInput from "../../../components/dateTimeInput/DateTimeInput";
import { ScheduleService } from "../../../services/SheduleService";

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
      }
    };

    fetchClassInfo();
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
      Alert("모임 탈퇴 오류가 발생했습니다.");
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
        Alert(`${error.response.data.message}`);
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
      Alert("모임 삭제 오류가 발생했습니다.");
    }
  };

  // 일정 생성
  const handlerCreateSchedule = async () => {
    const body = {
      classId: id,
      meetingTime: meetingTime,
      meetingTitle: meetingTitle,
    };
    console.log(body);
    try {
      const response = await ScheduleService.postSchedulesLists(body);
      console.log(response);
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
      <div className="list-container">
        <ul className="list">{/* memberList.jsx 참고 */}</ul>
      </div>
      <Modal isOpen={isModalOpen} title={"모임 정보 수정"} onClose={closeModal}>
        <div className="modal-form">
          <label>모임 이름:</label>
          <input
            type="text"
            value={name}
            onChange={(e) => setName(e.target.value)}
            placeholder="모임 이름을 입력하세요"
          />
          <label>모임 설명:</label>
          <textarea
            value={description}
            onChange={(e) => setDescription(e.target.value)}
            placeholder="모임 설명을 입력하세요"
          />
          <button className="custom-button" onClick={handlerModifyClass}>
            수정하기
          </button>
        </div>
      </Modal>
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
