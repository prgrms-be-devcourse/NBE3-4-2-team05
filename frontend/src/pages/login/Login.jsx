// @ts-nocheck
import React, { useState } from "react";
import { useNavigate } from "react-router-dom";

import Form from "src/components/form/Form";
import Title from "src/components/title/Title";
import Alert from "src/components/alert/Alert";

import { Element } from "src/constants/element";
import { UserService } from "src/services/UserService";
import Validate from "src/hooks/validate";

const Login = () => {
	const router = useNavigate();
	const [body, setBody] = useState({ loginId: "", password: "" });

	const [isLoading, setIsLoading] = useState(false);

	const onChangeInput = (name = "", e) => {
		setBody((prev) => ({ ...prev, [name]: e }));
	};

	const result = () => {
		setIsLoading(false);
		router("/");
		setTimeout(() => {
			window.location.reload();
		}, 100);
	};

	const onClickLogin = async (e) => {
		e.preventDefault();

		if (!Validate(body)) return;
		try {
			setIsLoading(true);
			const res = await UserService.Login(body);
			if (!res) {
				Alert("아이디와 비밀번호를 \n 정확히 입력해 주세요.", () =>
					setIsLoading(false),
				);
			} else {
				Alert("로그인 성공!", () => result());
			}
		} catch (error) {
			Alert("로그인에 실패했습니다.", "", "", () => setIsLoading(false));
		} finally {
			setIsLoading(false);
		}
	};

	return (
		<section>
			<Title type="title" text="로그인" />
			<Form
				title="login"
				element={Element.LOGIN_FORM}
				onSubmit={onClickLogin}
				onChange={onChangeInput}
				disabled={isLoading}
			/>
		</section>
	);
};

export default Login;
