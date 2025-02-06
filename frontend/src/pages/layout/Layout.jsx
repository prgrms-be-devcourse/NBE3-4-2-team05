import React from 'react';
import { Outlet } from 'react-router-dom';
import "./Layout.css";
import Header from 'src/components/header/Header';
import Footer from 'src/components/footer/Footer';

const Layout = () => {
	return (
    <div className="layout">
    <Header/>
      <main>
        <Outlet />
      </main>
	  <Footer/>
    </div>
  );
};

export default Layout;