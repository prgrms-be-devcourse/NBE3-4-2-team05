import React, {useEffect, useState} from "react";
import { useNavigate } from "react-router-dom";
import "./Home.css";
import { ClassService } from 'src/services/ClassService';
import Alert from "src/components/alert/Alert";
import {SearchService} from "../../services/SearchService";

const Home = () => {
  const navigate = useNavigate();
  const [ClassListData, setClassListData] = useState([]);
  const result = (id) => {
    navigate(`/classess/${id}`);
  }

  const handlerJoin = async (id) => {
    const isMember = await checkMember(id);
    const isBlackList = await checkBlackList(id);

    if (isBlackList) {
      Alert("강퇴당한 회원은 재가입 하실 수 없습니다.")
    } else if (isMember) {
      result(id);
    } else {
      Alert("가입된 회원이 아닙니다. <br>가입 하시겠습니까?", "취소", "가입", (e) => {if (e) {joinClass(id)}});
    }
  }

  // 모임 가입 확인
  const checkMember = async (id) => {
    try {
      const response = await ClassService.checkMember(id);

      const checkData = response.data;
      const getData = checkData?.data || [];
      const isMember = getData.member;

      return isMember;
    } catch (error) {
      console.error("checkMember:", error);
    }
  };

  // 블랙리스트 확인
  const checkBlackList = async (id) => {
    try {
      const response = await ClassService.checkBlackList(id);

      const checkData = response.data;
      const getData = checkData?.data || [];
      const isBlackList = getData.blackListed;
      console.log(isBlackList);

      return isBlackList;
    } catch (error) {
      console.error("checkBlackList:", error);
    }
  };

  // 모임 가입
  const joinClass = async (id) => {
    try {
      const response = await ClassService.joinClass(id);
      if (response.status === 200) {
        Alert("모임에 가입되었습니다.", "", "", () => result(id));
      } else {
        Alert("가입에 실패했습니다");
      }
    } catch (error) {
      console.error("join error:", error);
      Alert(error.response.data.message);
    }
  }

  //
  const fetchClassesList = async () => {
    try {
      const response = await SearchService.getSearchClasses();
      if (response) {
        setClassListData(response.data.data)
      } else {
        Alert("모임 조회에 실패했습니다.");
      }
    } catch (error) {
      console.error("에러:", error);
    }
  };
  console.log(ClassListData);

  useEffect(() => {
    fetchClassesList();
  }, []);

  return (
    <section className="home">
      <div className="sort-btn">
        정렬 부분
      </div>
      {ClassListData.map((element) => (
      <div className="class-container">
        <div className="class-info" key={element.id}>
          <div className='class-name'>
            <p>{element.name}</p>
            <p>{element.description}</p>
            <div className='class-flag'>
              <span>{element.favorite}</span>
            </div>
          </div>
          <button onClick={() => handlerJoin(element.id)}>입장</button>
        </div>
      </div>
      ))}

    </section>
  );
};

export default Home;
