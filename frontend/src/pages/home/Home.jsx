import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import "./Home.css";
import { ClassService } from 'src/services/ClassService';
import Alert from "src/components/alert/Alert";
import { FavoriteService } from 'src/services/FavoriteService';
import Modal from "src/components/modal/Modal";

const Home = () => {
  const navigate = useNavigate();
  const [options, setOptions] = useState([]);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [name, setName] = useState("");
  const [description, setDescription] = useState("");
  const [favorite, setFavorite] = useState("");

  const openModal = async () => {
    await fetchFavorites();
    setIsModalOpen(true);
  };

  const closeModal = () => {
    setIsModalOpen(false);
    resetForm();
  };

  const resetForm = () => {
    setName("");
    setDescription("");
    setFavorite("");
  }

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

  // 모임 생성
  const handlerCreateClass = async () => {
    const body = {
      name: name,
      favorite: favorite,
      description: description,
    };
    try {
      const response = await ClassService.postClassLists(body);
      if (response.status === 201) {
        Alert("모임이 생성되었습니다.", "", "", () => closeModal());
      } else {
        Alert("모임 생성에 실패했습니다", "", "", () => closeModal());
      }
    } catch (error) {
      console.error("모임 생성 오류:", error);
      Alert(error.response.data.message, "", "", () => closeModal());
    }
  };

  // 관심사 목록 조회
  const fetchFavorites = async () => {
    try {
      const response = await FavoriteService.getFavoriteList();
      if (!response) {
        throw new Error("fetch 실패");
      }
      const jsonData = response.data;
      const favoriteData = jsonData?.data || [];
      setOptions(favoriteData);
    } catch (error) {
      console.error("Error fetching members:", error);
    }
  };

  return (
    <section className="home">
      <button className='custom-button' onClick={openModal}>모임 생성</button>
      <div className="class-container">
        <div className="class-info">
          <div className='class-name'>
            <p>1번 모임 이름</p>
            <p>1번 모임 설명 10글자 이상</p>
            <div className='class-flag'>
              <span>관심사</span>
            </div>
          </div>
          <button onClick={() => handlerJoin("2")}>입장</button>
        </div>
      </div>

      {/* 모임 생성 */}
      <Modal isOpen={isModalOpen} title={"모임 생성"} onClose={closeModal}>
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
          <label htmlFor="favorite">모임 관심사:</label>
          <select id="favorite" value={favorite} onChange={(e) => setFavorite(e.target.value)}>
            <option value="" disabled hidden>관심사 선택</option>
            {options.map((option) => (
              <option key={option.id} value={option.favoriteName}>{option.favoriteName}</option>
            ))}
          </select>
          <button className="custom-button" onClick={handlerCreateClass}>
            생성
          </button>
        </div>
      </Modal>
    </section>
  );
};

export default Home;
