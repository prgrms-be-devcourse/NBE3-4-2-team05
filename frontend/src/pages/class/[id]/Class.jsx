import React from "react";
import { useParams } from "react-router-dom";

const Class = () => {
  const { id } = useParams();
  console.log(id);

  return (
    <div className="list-container">
      <ul className="list">{/* memberList.jsx 참고 */}</ul>
    </div>
  );
};

export default Class;
