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
        <div className="class-info">
          1번 모임방 Test
          <button onClick={() => onClickExample("1")}>입장</button>
        </div>
      </div>
      <div>TOdo....</div>
    </section>
  );
};

export default Home;
