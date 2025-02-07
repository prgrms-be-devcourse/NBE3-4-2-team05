// @ts-nocheck
import React from "react";
import "./CustomList.css"

  const  DUMMY=[
    { id: 1, name: "홍길동", description: "회의 참석", date: "2025-02-06" },
    { id: 2, name: "김철수", description: "발표", date: "2025-02-07" },
  ];



const CustomList = ({check,title="",description="",sub="",button1="가입하기",onClick1, onClick2, onClick3}) => {

  return (
    <div className="list-container">
      <ul className="list">
        {DUMMY.map((item) => (
          <li key={item.id} className="list-item">
            <div className="item-info">
              <h4 className="name">{item.name}</h4>
              {description && <p className="description">{item.description}</p>}
              {sub && <span className="date">{item.sub}</span>}
            </div>
            <div className="buttons">
              <button className="custom-button" onClick={() => onClick1()}>
                참석
              </button>
              {check && (
                <button className="custom-button" onClick={() => onClick2()}>
                  불참
                </button>
              )}
              {onClick3 && (
                <button className="custom-button" onClick={() => onClick2()}>
					일정 수정
                </button>
              )}
              {/* {item.attendance && <p className="status">{item.attendance}</p>} */}
            </div>
          </li>
        ))}
      </ul>
    </div>
  );
};

export default CustomList
