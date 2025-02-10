import React, { useState, useEffect } from "react";
import "./DateTimeInput.css";

function DateTimeInput({ onMeetingTimeChange }) {
  const today = new Date();

  const [year, setYear] = useState("");
  const [month, setMonth] = useState("");
  const [day, setDay] = useState("");
  const [hour, setHour] = useState("");
  const [minute, setMinute] = useState("");
  const [second, setSecond] = useState("");

  useEffect(() => {
    if (year && month && day && hour && minute && second) {
      const dateTime = new Date(year, month - 1, day, hour, minute, second);
      const formattedDateTime = `${dateTime.getFullYear()}-${String(dateTime.getMonth() + 1).padStart(2, "0")}-${String(dateTime.getDate()).padStart(2, "0")} ${String(dateTime.getHours()).padStart(2, "0")}:${String(dateTime.getMinutes()).padStart(2, "0")}:${String(dateTime.getSeconds()).padStart(2, "0")}`;

      onMeetingTimeChange(formattedDateTime);
    }
  }, [year, month, day, hour, minute, second, onMeetingTimeChange]);

  const generateOptions = (start, end, type) => {
    let options = [];
    for (let i = start; i <= end; i++) {
      options.push(i);
    }

    return options.map((i) => {
      let isDisabled = false;
      if (type === "year" && i < today.getFullYear()) isDisabled = true;
      if (
        type === "month" &&
        year === today.getFullYear() &&
        i < today.getMonth() + 1
      )
        isDisabled = true;
      if (
        type === "day" &&
        year === today.getFullYear() &&
        month === today.getMonth() + 1 &&
        i < today.getDate()
      )
        isDisabled = true;

      return (
        <option key={i} value={i} disabled={isDisabled}>
          {i}
        </option>
      );
    });
  };

  return (
    <form className="datetime-form">
      <div className="input-group">
        <label>년:</label>
        <select value={year} onChange={(e) => setYear(e.target.value)}>
          <option value="">선택</option>
          {generateOptions(2025, today.getFullYear(), "year")}
        </select>
      </div>
      <div className="input-group">
        <label>월:</label>
        <select
          value={month}
          onChange={(e) => setMonth(e.target.value)}
          disabled={!year}
        >
          <option value="">선택</option>
          {generateOptions(1, 12, "month")}
        </select>
      </div>
      <div className="input-group">
        <label>일:</label>
        <select
          value={day}
          onChange={(e) => setDay(e.target.value)}
          disabled={!month}
        >
          <option value="">선택</option>
          {generateOptions(1, 31, "day")}
        </select>
      </div>
      <div className="input-group">
        <label>시:</label>
        <select value={hour} onChange={(e) => setHour(e.target.value)}>
          <option value="">선택</option>
          {generateOptions(0, 23, "hour")}
        </select>
      </div>
      <div className="input-group">
        <label>분:</label>
        <select value={minute} onChange={(e) => setMinute(e.target.value)}>
          <option value="">선택</option>
          {generateOptions(0, 59, "minute")}
        </select>
      </div>
      <div className="input-group">
        <label>초:</label>
        <select value={second} onChange={(e) => setSecond(e.target.value)}>
          <option value="">선택</option>
          {generateOptions(0, 59, "second")}
        </select>
      </div>
    </form>
  );
}

export default DateTimeInput;
