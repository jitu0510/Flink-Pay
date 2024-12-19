import React, { useEffect } from 'react';
import '../CSS/SideBar.css';
import { BrowserRouter as Router, Route, Switch, Link } from 'react-router-dom';
import { useHistory } from 'react-router-dom';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faHome, faTachometerAlt, faFileAlt, faMoneyBillWave, faArrowUp, faArrowDown, faExchangeAlt, faCogs, faInfoCircle, faArrowRight } from '@fortawesome/free-solid-svg-icons';

const SideBar = (propsnm) => {
    const history = useHistory();
    
    useEffect(() => {
        history.push("/dashboard/transactions");
        const currentPage = localStorage.getItem('page');
        if (currentPage !== null) {
            history.push(currentPage);
        }
    }, []);

    const titleClick = () => {
        localStorage.setItem("page", "/dashboard/inbounds");
        history.push("/dashboard/inbounds");
    }

    const logoSrc = require('../flink-Photoroom.jpg');  // Adjust the path as necessary
    const logoAlt = 'Flink-Pay';
    
    return (
        <nav id="sidebar">
            <div className="sidebar-header" onClick={titleClick} style={{ marginTop: '-20px',textAlign: 'center',padding:"0px",paddingTop:"15px" }}>
                <img src={logoSrc} alt={logoAlt} style={{ width: '270px', height: '65px', maxWidth: '160px' }} />
            </div>
            <ul className="list-unstyled components">
                <li>
                    <Link onClick={titleClick}><FontAwesomeIcon icon={faHome} /> Home</Link>
                </li>
                <li>
                    <Link><FontAwesomeIcon icon={faTachometerAlt} /> Dashboard</Link>
                </li>
                <li>
                    <Link onClick={() => { history.push(`/dashboard/disputes`) }}><FontAwesomeIcon icon={faFileAlt} /> Disputes</Link>
                </li>
                <li>
                    <Link><FontAwesomeIcon icon={faMoneyBillWave} /> Settlements</Link>
                </li>
                <li>
                    <Link onClick={() => { history.push(`/dashboard/inbounds`) }}><FontAwesomeIcon icon={faArrowUp} /> Inbounds</Link>
                </li>
                <li>
                    <Link onClick={() => { history.push(`/dashboard/amount-transfer`) }}><FontAwesomeIcon icon={faArrowRight} />Amount Transfer</Link>
                </li>
                <li>
                    <Link onClick={() => { history.push(`/dashboard/outbounds`) }}><FontAwesomeIcon icon={faArrowDown}  /> Outbounds</Link>
                </li>
                <li>
                    <Link onClick={() => { history.push(`/dashboard/transactions`) }}><FontAwesomeIcon icon={faExchangeAlt} /> All Transactions</Link>
                </li>
                {/* <li>
                    <Link><FontAwesomeIcon icon={faFileAlt} /> RBI Penalties</Link>
                </li> */}
                <li>
                    <Link><FontAwesomeIcon icon={faFileAlt} /> Status</Link>
                </li>
                <li>
                    <Link><FontAwesomeIcon icon={faCogs} /> Manage</Link>
                </li>
                {/* <li>
                    <Link onClick={() => { history.push(`/dashboard/settings`) }}><FontAwesomeIcon icon={faCogs} /> Settings</Link>
                </li> */}
                <li>
                    <Link><FontAwesomeIcon icon={faInfoCircle} /> About</Link>
                </li>
            </ul>
        </nav>
    );
}

export default SideBar;