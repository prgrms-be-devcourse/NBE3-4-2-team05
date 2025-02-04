const LOGIN_FORM = [
	{
		id: "loginId",
		label: "이메일",
		name: "loginId",
		type: "text",
		placeholder: "example@example.com",
	},
	{
		id: "password",
		label: "비밀번호",
		name: "password",
		type: "password",
		placeholder:
			"비밀번호, 대문자+소문자 조합, 특수문자 + 숫자 조합, 연속된 문자 3개이상 금지",
	},
];

const Element = {
	LOGIN_FORM,
};

export { Element };
