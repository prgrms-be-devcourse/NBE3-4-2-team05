import React, { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import Alert from "src/components/alert/Alert";
import { ClassService } from "src/services/ClassService";
import { useNavigate } from "react-router-dom";

const Class = () => {
  const { id } = useParams();
  const [classInfo, setClassInfo] = useState([]);
  const router = useNavigate();

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
        Alert("모임에서 탈퇴되었습니다.");
      } else {
        Alert("탈퇴에 실패했습니다");
      }
    }catch (error) {
      console.error("모임 탈퇴 오류:", error);
      Alert("모임 탈퇴 오류가 발생했습니다.");
    }
  };

  // 모임 수정
  const handlerModifyClass = async () => {
    try {
      const response = await ClassService.putClassLists(body, id);
      if (response.status === 200) {
        Alert("모임 정보가 수정되었습니다.");
      } else {
        Alert("모임 정보 수정에 실패했습니다");
      }
    }catch (error) {
      console.error("모임 수정 오류:", error);
      Alert("모임 수정 오류가 발생했습니다.");
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
    }catch (error) {
      console.error("모임 삭제 오류:", error);
      Alert("모임 삭제 오류가 발생했습니다.");
    }
  };


  return (
    <div>
      <div className="buttons">
        <button className="custom-button">일정 생성</button>
        <button className="custom-button" onClick={() => handlerResignClass()}>모임 탈퇴</button>
        <button className="custom-button">모임 수정</button>
        <button className="custom-button" onClick={() => handlerDeleteClass()}>모임 삭제</button>
        <button className="custom-button" onClick={() => router("/")}>회원 관리</button>
      </div>

      <div className="class-info-container">
        <p>{classInfo.name}</p>
        <p>{classInfo.favorite}</p>
        <p>{classInfo.description}</p>
      </div>
      <div className="list-container">
        <ul className="list">{/* memberList.jsx 참고 */}</ul>
      </div>
    </div>
  );
};

export default Class;