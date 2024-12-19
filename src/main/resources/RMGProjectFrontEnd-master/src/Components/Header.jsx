import React, { useState, useEffect } from 'react';
import { useHistory } from 'react-router-dom';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faClock, faSignInAlt, faUser , faUserShield, faSignOutAlt } from '@fortawesome/free-solid-svg-icons';
import '../CSS/SideBar.css';

const Header = () => {
  const [currentTime, setCurrentTime] = useState(new Date().toLocaleTimeString());
  const [loginTime, setLoginTime] = useState(localStorage.getItem('loginTime') || new Date().toLocaleString());
  const username =  localStorage.getItem("usernameDecrypt");
  const role = localStorage.getItem("roleDecrypt").replace("ROLE_","");
  const history = useHistory();

  useEffect(() => {
    const timer = setInterval(() => {
      setCurrentTime(new Date().toLocaleTimeString());
    }, 1000);

    return () => clearInterval(timer); // Cleanup on unmount
  }, []);

  const handleLogout = () => {
    localStorage.removeItem('username');
    localStorage.removeItem('role');
    localStorage.removeItem('loginTime');
    localStorage.clear();
    window.location.reload(false);
  };

  return (
    <nav className="navbar navbar-expand-lg" style={{ marginLeft: "0px", height: "60px" }}>
      <div className="container-fluid container-color">
        <div className="navbar-collapse" id="navbarNav" style={{ fontSize: '15px', display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
          <div className="nav-info">
            <span className="nav-item">
              <FontAwesomeIcon icon={faClock} /> <strong>Current Time:</strong> {currentTime}
            </span>
            <span className="nav-item">
              <FontAwesomeIcon icon={faSignInAlt} /> <strong>Login Time:</strong> {loginTime}
            </span>
            <span className="nav-item">
              <FontAwesomeIcon icon={faUser } /> <strong>Username:</strong> {username}
            </span>
            <span className="nav-item">
              <FontAwesomeIcon icon={faUserShield} /> <strong>Role:</strong> {role}
            </span>
          </div>
          <div className="nav-item" onClick={handleLogout} style={{ cursor: 'pointer' }}>
            <FontAwesomeIcon icon={faSignOutAlt} /> Logout
          </div>
        </div>
      </div>
    </nav>
  );
};

export default Header;