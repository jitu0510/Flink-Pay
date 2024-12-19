import React,{useEffect} from 'react';
import Header from './Header';
import SideBar from './SideBar'
import DashBoardContent from './DashBoardContent'
import {BrowserRouter as Router,Route,Switch,Link} from 'react-router-dom'
import ProjectComp from './Project'
import UserComp from './Users'
import AuthenticatedRoute from './AuthenticatedRoute'


const ProfileSettings = ()=>{
    useEffect(() => {
        localStorage.setItem("page","/dashboard/settings");
    
    }, []);
    
    return(
        <div class="container-fluid register-form">
        <div class="form">

            <div class="form-content">
                <div class="row">
                    <div class="col-md-6">
                        <div class="form-group">
                            <input type="text" class="form-control" placeholder="Your Current Password*" value=""/>
                        </div>
                    </div>
                    <div class="col-md-6">
                        <div class="form-group">
                            <input type="text" class="form-control" placeholder="Your New Password *" value=""/>
                        </div>
                        <div class="form-group">
                            <input type="text" class="form-control" placeholder="Confirm New Password *" value=""/>
                        </div>
                    </div>
                </div>
                <button  type="button" className="btn btn-primary">Submit</button>
            </div>
        </div>
    </div>
    )
}
export default ProfileSettings