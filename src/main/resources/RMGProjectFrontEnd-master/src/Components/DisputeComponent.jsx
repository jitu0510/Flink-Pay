import React, { useState, useEffect } from 'react';
import Modal from 'react-modal';
import { toast } from 'react-toastify';
import { useHistory } from 'react-router-dom';
import Pagination from "react-js-pagination";
import { getPaginatedDisputes } from '../Services/DisputeServices';
toast.configure();
Modal.setAppElement('#root');

const DisputeComponent = () => {
  const [modelIsOpen, setmodelIsOpen] = useState(false);
  const [disputes, setDisputes] = useState([]);
  const [allOutbounds, setAllOutbounds] = useState([]);
  const [selectedTransaction, setSelectedTransaction] = useState(null);
  const [disputeDescription, setDisputeDescription] = useState('');
  const [isDisputeChecked, setIsDisputeChecked] = useState(false);
  const history = useHistory();
  const [searchQuery, setSearchQuery] = useState('');
  const [searchCriteria, setSearchCriteria] = useState('referenceNumber');

  // Pagination
  const [totalPages, setTotalPages] = useState(0);
  const [itemsCountPerPage, SetItemsCountPerPage] = useState(0);
  const [totalItemsCount, SetTotalItemsCount] = useState(0);
  const [activePage, SetActivePage] = useState(1);

  useEffect(() => {
    refreshOutbounds(activePage);
    localStorage.setItem("page","/dashboard/disputes")
  }, [activePage]);

  const refreshOutbounds = (activePage) => {
    getPaginatedDisputes(activePage)
      .then((resp) => {
        const tp = resp.data.totalPages;
        setDisputes(resp.data.content);
        setTotalPages(tp);
        SetItemsCountPerPage(resp.data.size);
        SetTotalItemsCount(resp.data.totalElements);
      })
      .catch(error => {
        console.error('Error fetching projects:', error);
      });
  };

  const handlePageChange = (pageNumber) => {
    SetActivePage(pageNumber);
    refreshOutbounds(pageNumber);
  };

 

  const openModal = (transaction) => {
    setSelectedTransaction(transaction);
    setmodelIsOpen(true);
    setIsDisputeChecked(false); // Reset dispute checkbox
    setDisputeDescription(''); // Reset dispute description
  };

  const closeModal = () => {
    setmodelIsOpen(false);
    setSelectedTransaction(null);
  };
  const handleSearchChange = (e) => {
    setSearchQuery(e.target.value);
  };

  const handleSearchCriteriaChange = (e) => {
    setSearchCriteria(e.target.value);
  };

  let filteredDisputes = searchQuery.trim() !== ''
    ? disputes.filter(outbound =>
      outbound[searchCriteria]?.toLowerCase().includes(searchQuery.toLowerCase())
    )
    : disputes;

  return (
    <>
      <div className="container-fluid">
        <div className="table-wrapper">
          <div className="table-title">
            <div className="row">
              <div className="col-sm-5">
                <h2 style={{ color: "black" }}>List of<b> Dispute Transactions</b></h2>
              </div>
              <div className="col-sm-3">
                <select
                  className="form-control"
                  value={searchCriteria}
                  onChange={handleSearchCriteriaChange}
                >
                  <option value="referenceNumber">Search by Reference Id</option>
                  <option value="senderAccountNumber">Search by Debtor Account No.</option>
                  <option value="beneficiaryCreditAccountNumber">Search by Creditor Account No.</option>
                </select>
              </div>
              <div className="col-sm-4">
                <input
                  type="text"
                  className="form-control"
                  placeholder={`Search by ${searchCriteria}`}
                  value={searchQuery}
                  onChange={handleSearchChange}
                />
              </div>
              {/* Search and filter components here */}
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
                <th>Transfer Date</th>
                <th>Dispute Raised On</th>
                <th>Status</th>
              </tr>
            </thead>
            <tbody>
              {filteredDisputes.map(txn => (
                <tr key={txn.Transaction_ID} onClick={() => openModal(txn)}>
                  <td style={{ cursor: 'pointer', color: 'blue' }}>{txn.referenceNumber}</td>
                  <td>{txn.currency}</td>
                  <td>{txn.transferAmount}</td>
                  <td>{txn.senderAccountNumber}</td>
                  <td>{txn.beneficiaryCreditAccountNumber}</td>
                  <td>{txn.transferDate}</td>
                  <td>{txn.disputeRaiseTimeStamp}</td>
                  <td>{txn.status}</td>
                 
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
            width: '80%',
            height:'80%'
          }
        }}
      >
        <h2>Dispute Transaction Details</h2>
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
              <p>{selectedTransaction.transferDate}</p>
            </div>
            <div className="detail-group">
              <strong>Sender Account Number:</strong>
              <p>{selectedTransaction.senderAccountNumber}</p>
            </div>
            <div className="detail-group">
              <strong>Sender Account Name:</strong>
              <p>{selectedTransaction.senderAccountName}</p>
            </div>
            <div className="detail-group">
              <strong>Charges Code:</strong>
              <p>{selectedTransaction.chargesCode}</p>
            </div>
            <div className="detail-group">
              <strong>Beneficiary Type:</strong>
              <p>{selectedTransaction.beneficiaryType}</p>
            </div>
            <div className="detail-group">
              <strong>Beneficiary Credit Account Number:</strong>
              <p>{selectedTransaction.beneficiaryCreditAccountNumber}</p>
            </div>
            <div className="detail-group">
              <strong>Beneficiary Account Name:</strong>
              <p>{selectedTransaction.beneficiaryAccountName}</p>
            </div>
            <div className="detail-group">
              <strong>Transfer Amount:</strong>
              <p>{selectedTransaction.transferAmount}</p>
            </div>
            <div className="detail-group">
              <strong>Beneficiary Mobile Number:</strong>
              <p>{selectedTransaction.beneficiaryMobileNumber}</p>
            </div>
            <div className="detail-group">
              <strong>Bank Branch:</strong>
              <p>{selectedTransaction.bankBranch}</p>
            </div>
            <div className="detail-group">
              <strong>Bank Name:</strong>
              <p>{selectedTransaction.bankName}</p>
            </div>  
            <div className="detail-group">
              <strong>Dispute Description:</strong>
              <p>{selectedTransaction.disputeDescription}</p>
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

export default DisputeComponent;