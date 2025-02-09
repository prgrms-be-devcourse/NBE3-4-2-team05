import React from "react";
import "./Home.css";
import { useNavigate } from "react-router-dom";

const Home = () => {
  const navigate = useNavigate();

  const onClickExample = (id) => {
    navigate(`/classess/${id}`);
  };

  return (
    <section>
      <div>
        <div onClick={() => onClickExample("1")}>1번 모임방 Test</div>
      </div>
      <div>TOdo....</div>
    </section>
  );
};

export default Home;
