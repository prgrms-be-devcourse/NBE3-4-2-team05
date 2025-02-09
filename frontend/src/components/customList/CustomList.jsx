// @ts-nocheck
import React from "react";
import "./CustomList.css";

const CustomList = ({
  data1,
  data2,
  data3,
  data4,
  check,
  title = "",
  description = "",
  sub = "",
  button1,
  button2,
  button3,
  onClick1,
  onClick2,
  onClick3,
}) => {
  return (
    <li key={data1} className="list-item">
      <div className="item-info">
        <h4 className="name">{data2}</h4>
        {description && <p className="description">{data3}</p>}
        {sub && <span className="date">{data4}</span>}
      </div>
      <div className="buttons">
        <button className="custom-button" onClick={() => onClick1()}>
          {button1}
        </button>
        {check && (
          <button className="custom-button" onClick={() => onClick2()}>
            {button2}
          </button>
        )}
        {onClick3 && (
          <button className="custom-button" onClick={() => onClick2()}>
            {button3}
          </button>
        )}
        {/* {item.attendance && <p className="status">{item.attendance}</p>} */}
      </div>
    </li>
  );
};

export default CustomList;
