import React from 'react';
import Logo from '../logo/Logo';
import './Header.css'
import { Project } from 'src/constants/project';

const Header = () => {
	const isLogin= Project.getJwt();
	return (
    <header id="header">
      <Logo />
      <nav className='nav-wrap'>
        <button>{!isLogin ? "로그인" : "로그아웃"}</button>
        <button>{!isLogin ? "회원가입" : "마이페이지"}</button>
      </nav>
    </header>
  );
};

export default Header;