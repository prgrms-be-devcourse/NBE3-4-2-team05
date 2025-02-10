import React from "react";
import { BrowserRouter, Route, Routes } from "react-router-dom";
import ReactDOM from "react-dom/client";

import Home from "./pages/home/Home";
import Login from "./pages/login/Login";
import SignUp from "./pages/signUp/SignUp";
import Class from "./pages/class/[id]/Class";
import Schedule from "./pages/schedule/Schedule";
import MemberList from "./pages/class/[id]/MemberList";

import Layout from "./pages/layout/Layout";

const App = () => {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<Layout />}>
          <Route index element={<Home />} />
          <Route path="/login" element={<Login />} />
          <Route path="/join" element={<SignUp />} />
          <Route path="/classess/:id" element={<Class />} />
          <Route path="/schedules" element={<Schedule />} />
          <Route path="/memberList/:id" element={<MemberList />} />
        </Route>
      </Routes>
    </BrowserRouter>
  );
};

// @ts-ignore
const root = ReactDOM.createRoot(document.getElementById("root"));
root.render(
  <React.StrictMode>
    <App />
  </React.StrictMode>,
);
