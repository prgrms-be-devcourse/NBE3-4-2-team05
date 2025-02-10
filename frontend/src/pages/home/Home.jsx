import React from "react";
import { useNavigate } from "react-router-dom";
import "./Home.css";
import Alert from "../../components/alert/Alert";

const Home = () => {
  const navigate = useNavigate();

  const onClickExample = (id) => {
    navigate(`/classess/${id}`);
  };
  return (
    <section>
      <div className="class-container">
        <div className="class-info">
          1번 모임방 Test
          <button onClick={() => onClickExample("1")}>입장</button>
        </div>
      </div>
    </section>
  );
};

export default Home;
