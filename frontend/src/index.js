import React from "react";
import { BrowserRouter, Route, Routes } from "react-router-dom";
import ReactDOM from "react-dom/client";

import Home from "./pages/home/Home";
import useTokenCheck from "./hooks/useToken";
import Login from "./pages/login/Login";
import SignUp from "./pages/signUp/SignUp";

const App = () => {
	useTokenCheck();

	return (
		<BrowserRouter>
			<Routes>
				<Route path="/" element={<Home />} />
				<Route path="/login" element={<Login />} />
				<Route path="/join" element={<SignUp />} />
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
