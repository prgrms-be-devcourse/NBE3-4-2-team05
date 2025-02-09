import React, { useEffect, useState } from "react";
import CustomList from "src/components/customList/CustomList";
import { ClassService } from "src/services/ClassService";
import { useParams } from "react-router-dom";

const MemberList = () => {
  const { id } = useParams();
  const [members, setMembers] = useState([]);

  useEffect(() => {
    const fetchMemberList = async () => {
      try {
        const response = await ClassService.memberListByClass(id);
        if (!response) {
          throw new Error("fetch 실패");
        }
        const jsonData = response.data;
        const userList = jsonData?.data || [];

        setMembers(userList);
      } catch (error) {
        console.error("Error fetching members:", error);
      }
    };

    fetchMemberList();
  }, [id]);

  const handleTransferMaster = async (userId) => {
    try {
      const response = await ClassService.transferMaster(id, userId);
      if (response.status === 200) {
        alert("권한이 성공적으로 위임되었습니다.");
      } else {
        alert("권한 위임에 실패했습니다.");
      }
    } catch (error) {
      console.error("권한 위임 오류:", error);
      alert("권한 위임 중 오류가 발생했습니다.");
    }
  };

  if (members.length === 0) {
    return <p>회원 정보가 없습니다.</p>;
  }

  return (
    <div>
      <p>{members.name}</p>
      <div className="list-container">
        <ul className="list">
          {members?.userList.map((user) => (
            <CustomList
              data1={user.userId}
              data2={user.nickName}
              title="회원 목록"
              description="모임에 속한 회원 목록"
              check={true}
              button1="권한 위임"
              button2="강퇴"
              onClick1={() => handleTransferMaster(user.userId)}
            />
          ))}
        </ul>
      </div>
    </div>
  );
};

export default MemberList;
