import React, { useState, useEffect } from 'react';
import { Formik, Form, Field, ErrorMessage } from 'formik';
import * as Yup from 'yup';
//import {getAllTransactions,getPaginatedTransactions} from './../Services/TransactionServices';
//import { downloadSwiftFile, getAllOutbounds, getPaginatedOutbounds } from '../Services/OutboundTransactions';
import { getAllInbounds, getPaginatedInbounds } from '../Services/InboundTransactionsService';
import Modal from 'react-modal';
import { ToastContainer, toast } from 'react-toastify';
import { useHistory, Link } from 'react-router-dom';
import Pagination from "react-js-pagination";
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faDownload } from '@fortawesome/free-solid-svg-icons';
toast.configure();
Modal.setAppElement('#root');

const InboundComponent  = () => {
  const [modelIsOpen, setmodelIsOpen] = useState(false);
  const [inbounds, setInbounds] = useState([]);
  const [allInbounds, setAllInbounds] = useState([]);
  const [deleteProjId, setDeleteProjId] = useState('');
  const [deleteProjName, setDeleteProjName] = useState('');
  const [selectedTransaction, setSelectedTransaction] = useState(null);
  const [editProject, setEditProject] = useState({ projectId: '', projectName: '', teamSize: '', createdBy: '', status: '' });
  const history = useHistory();
  const [searchQuery, setSearchQuery] = useState('');
  const [searchCriteria, setSearchCriteria] = useState('referenceNumber'); // Default search criteria

  // Pagination
  const [totalPages, setTotalPages] = useState(0);
  const [itemsCountPerPage, SetItemsCountPerPage] = useState(0);
  const [totalItemsCount, SetTotalItemsCount] = useState(0);
  const [activePage, SetActivePage] = useState(1);

  useEffect(() => {
    refreshInbounds(activePage);

}, [activePage]);

  const refreshInbounds = (activePage) => {
    getPaginatedInbounds(activePage)
      .then((resp) => {
        const tp = resp.data.totalPages
        setInbounds(resp.data.content);
        setTotalPages(tp);
        SetItemsCountPerPage(resp.data.size);
        SetTotalItemsCount(resp.data.totalElements);
        console.log(resp.data.content);
        
       // setProjects(response.data);
        //localStorage.setItem('currentPage', page);
      })
      .catch(error => {
        console.error('Error fetching projects:', error);
      });
  };
  const customStyles = {
	content : {
	  top                   : '50%',
	  left                  : '50%',
	  right                 : 'auto',
	  bottom                : 'auto', 
	  marginRight           : '-50%',
	  transform             : 'translate(-50%, -50%)',
	  width                 : '40%'
	}
  };

  const toastCustamize = () => {
    
  };

  const handlePageChange = (pageNumber) => {
    SetActivePage(pageNumber);
    refreshInbounds(pageNumber);
};

  const initialValues = {
    projectName: '',
    teamSize: 0,
    createdBy: '',
    status: '',
  };

  const validationSchema = Yup.object({
    projectName: Yup.string().required('Project Name is Required'),
    // createdBy: Yup.string().required('createdBy Name is Required'),
  });
  const handleSearchChange = e => {
    setSearchQuery(e.target.value);
  };


  useEffect(() => {
    localStorage.setItem("page","/dashboard/inbounds");
    refreshOutboundsList();
}, [activePage])

const refreshOutboundsList = (activePage) => {
  getAllInbounds(activePage)
        .then((resp) => {
            setAllInbounds(resp.data.content);
            console.log(resp.data.content);
            
        })
        .catch((e) => {
            console.log("something went wrong");  
        })
}
const handleSearchCriteriaChange = e => {
  setSearchCriteria(e.target.value);
};
const openModal = (transaction) => {
  setSelectedTransaction(transaction);
  setmodelIsOpen(true);
};

const closeModal = () => {
  setmodelIsOpen(false);
  setSelectedTransaction(null);
};
let filteredInbounds;
if (searchQuery.trim() !== '') {
  console.log(allInbounds);
  filteredInbounds = allInbounds.filter(inbound =>
    inbound[searchCriteria].toLowerCase().includes(searchQuery.toLowerCase())
  );
} else {
  filteredInbounds = inbounds;
}
  return (
    <>
      <div className="container-fluid">
        <div className="table-wrapper">
          <div className="table-title">
            <div className="row">
              <div className="col-sm-5">
                <h2 style={{color:"black"}}>List of<b> Inbound Transactions</b></h2>
              </div>
               <div className="col-sm-3">
                            <select
                                className="form-control"
                                value={searchCriteria}
                                onChange={handleSearchCriteriaChange}
                            >
                                <option value="referenceNumber">Search by Reference Id</option>
                                <option value="debtorAccountNumber">Search by Debtor Account No.</option>
                                <option value="creditorAccountNumber">Search by Creditor Account No.</option>
                            </select>
                        </div> 
                         <div className="col-sm-4">
                            <input
                                type="text"
                                className="form-control"
                                placeholder={`Search by ${searchCriteria === 'referenceNumber' ? 'Reference Id' : (searchCriteria === 'senderAccountNumber' ? 'Debtor Account Number' : 'Creditor Account Number')}`}
                                value={searchQuery}
                                onChange={handleSearchChange}
                            />
                        </div>
            </div>
          </div>
          <table className="table table-striped table-hover">
            <thead>
            <tr>
                <th>ReferenceId</th>
                <th>Currency</th>
                <th>Amount</th>
                <th>Debtor A/C No.</th>
                <th>Creditor A/C No.</th>
                <th>Settlement Date</th>
                <th>Remittance Info</th>
              </tr>
            </thead>
            <tbody>
              {filteredInbounds.map(txn => (
                <tr className="tr" key={txn.Transaction_ID} onClick={() => openModal(txn)}>
                  <td style={{ cursor: 'pointer', color: 'blue' }}>{txn.referenceNumber}</td>
                  <td>{txn.currency}</td>
                  <td>{txn.amount}</td>
                  <td>{txn.debtorAccountNumber}</td>
                  <td>{txn.creditorAccountNumber}</td>
                  <td>{txn.settlementDate}</td>
                  <td>{txn.remittanceInformation}</td>
                </tr>
              ))}
            </tbody>
          </table>
          <div className="pagination">
            <Pagination
              activePage={activePage}
              itemsCountPerPage={itemsCountPerPage}
              totalItemsCount={totalItemsCount}
              pageRangeDisplayed={5}
              onChange={handlePageChange}
              itemClass="page-item"
              linkClass="page-link"
            />
          </div>
        </div>    
      </div>
      <Modal
        isOpen={modelIsOpen}
        onRequestClose={closeModal}
        contentLabel="Transaction Details"
        style={{
          content: {
            top: '50%',
            left: '50%',
            right: 'auto',
            bottom: 'auto',
            marginRight: '-50%',
            transform: 'translate(-50%, -50%)',
            width: '80%', // Adjust width as needed
          }
        }}
      >
        <h2>Inbound Transaction Details</h2>
        {selectedTransaction && (
          <div className="modal-content1" style={{ display: 'grid', gridTemplateColumns: 'repeat(4, 1fr)', gap: '30px' }}>
            <div className="detail-group">
              <strong>Reference ID:</strong>
              <p>{selectedTransaction.referenceNumber}</p>
            </div>
            <div className="detail-group">
              <strong>Currency:</strong>
              <p>{selectedTransaction.currency}</p>
            </div>
            <div className="detail-group">
              <strong>Transfer Date:</strong>
              <p>{selectedTransaction.settlementDate}</p>
            </div>
            <div className="detail-group">
              <strong>Sender Account Number:</strong>
              <p>{selectedTransaction.debtorAccountNumber}</p>
            </div>
            <div className="detail-group">
              <strong>Sender Account Name:</strong>
              <p>{selectedTransaction.debtorAccountName}</p>
            </div>
            <div className="detail-group">
              <strong>Sender Agent BIC code:</strong>
              <p>{selectedTransaction.debtorAgentBICCode}</p>
            </div>
            <div className="detail-group">
              <strong>Beneficiary Credit Account Number:</strong>
              <p>{selectedTransaction.creditorAccountNumber}</p>
            </div>
            <div className="detail-group">
              <strong>Beneficiary Account Name:</strong>
              <p>{selectedTransaction.creditorAccountName}</p>
            </div>
            <div className="detail-group">
              <strong>Beneficiary Agent BIC Code:</strong>
              <p>{selectedTransaction.creditorAgentBICCode}</p>
            </div>
            <div className="detail-group">
              <strong>Transfer Amount:</strong>
              <p>{selectedTransaction.amount}</p>
            </div>
            <div className="detail-group">
              <strong>Settlement Date:</strong>
              <p>{selectedTransaction.settlementDate}</p>
            </div>
            <div className="detail-group">
              <strong>Remittance Information:</strong>
              <p>{selectedTransaction.remittanceInformation}</p>
            </div>
             
          </div>
        )}
        <div className="modal-footer">
          <button className="close-button" onClick={closeModal}>Close</button>
        </div>
      </Modal>  	  
    </>
  );
};

export default InboundComponent ;