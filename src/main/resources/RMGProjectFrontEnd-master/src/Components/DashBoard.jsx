import React from 'react';
import Header from './Header';
import SideBar from './SideBar'
import DashBoardContent from './DashBoardContent'
import {BrowserRouter as Router,Route,Switch,Link} from 'react-router-dom'
import UserComp from './Users'
import AuthenticatedRoute from './AuthenticatedRoute'
import ProfileSettings from './ProfileSettings';
import ProjectModules from './Modules';
import ExportOptionsComponent from './ExportOptionsComponent';
import DashboardChartComponent from './DashboardChartComponent';
import TransactionComp from './TransactionComponent';
import AmountTransferForm from './AmountTransfer';
import OutboundComponent from './OutboundComponent';
import InboundComponent from './InboundComponent';
import DisputeComponent from './DisputeComponent';


const DashBoard = ()=>{
    
    return(
        <Router>
        <div className="wrapper">
       
         <SideBar />
         
         <div  id="content" >
         <Header  />
         <AuthenticatedRoute path="/welcome" component={DashBoardContent} /> 
        {/* <AuthenticatedRoute path="/dashboard/projects" exact component={ProjectComp} /> 
       
        <AuthenticatedRoute path="/dashboard/overview" exact component={DashboardChartComponent} /> 
        <AuthenticatedRoute path="/dashboard/modules/:projectId"  component={ProjectModules} /> 
        <AuthenticatedRoute path="/dashboard/export" exact component={ExportOptionsComponent} />
        <AuthenticatedRoute path="/dashboard/users" exact component={UserComp} /> 
        <AuthenticatedRoute path="/dashboard/transactions" exact component={TransactionComp} /> 
        <AuthenticatedRoute path="/dashboard/settings" exact component={ProfileSettings} />  */}
        <AuthenticatedRoute path="/dashboard/transactions" exact component={TransactionComp} /> 
        <AuthenticatedRoute path="/dashboard/amount-transfer" exact component={AmountTransferForm} /> 
        <AuthenticatedRoute path="/dashboard/outbounds" exact component={OutboundComponent} />
        <AuthenticatedRoute path="/dashboard/inbounds" exact component={InboundComponent} /> 
        <AuthenticatedRoute path="/dashboard/disputes" exact component={DisputeComponent} />  
        </div>
        </div> 
        </Router>
    )
}
export default DashBoard