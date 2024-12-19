import React, { useState, useEffect } from 'react';
import { downloadSwiftFile, getAllOutbounds, getPaginatedOutbounds } from '../Services/OutboundTransactions';
import Modal from 'react-modal';
import { ToastContainer, toast } from 'react-toastify';
import { useHistory } from 'react-router-dom';
import Pagination from "react-js-pagination";
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faDownload } from '@fortawesome/free-solid-svg-icons';
import { raiseDispue, raiseDispute } from '../Services/DisputeServices';
toast.configure();
Modal.setAppElement('#root');

const OutboundComponent = () => {
  const [modelIsOpen, setmodelIsOpen] = useState(false);
  const [outbounds, setOutbounds] = useState([]);
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
    localStorage.setItem("page","/dashboard/outbounds")
  }, [activePage]);

  const refreshOutbounds = (activePage) => {
    getPaginatedOutbounds(activePage)
      .then((resp) => {
        const tp = resp.data.totalPages;
        setOutbounds(resp.data.content);
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

  const handleDownload = async (referenceNumber) => {
    try {
      const response = await downloadSwiftFile(referenceNumber);
      const contentDisposition = response.headers['content-disposition'];
      const filename = contentDisposition
        ? contentDisposition.split('filename=')[1]
        : `file_${referenceNumber}.txt`;

      const blob = new Blob([response.data], { type: 'application/octet-stream' });
      const downloadLink = document.createElement('a');
      downloadLink.href = URL.createObjectURL(blob);
      downloadLink.download = filename;
      document.body.appendChild(downloadLink);
      downloadLink.click();
      document.body.removeChild(downloadLink);
    } catch (error) {
      console.error('Error downloading the file:', error);
      alert('Error downloading the file.');
    }
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

  let filteredOutbounds = searchQuery.trim() !== ''
    ? outbounds.filter(outbound =>
      outbound[searchCriteria]?.toLowerCase().includes(searchQuery.toLowerCase())
    )
    : outbounds;

  const handleDisputeSubmit = () => {    
    if(disputeDescription.trim()===''){
      toast.error('Description cannot be empty');
    }else{
    raiseDispute(selectedTransaction.referenceNumber,disputeDescription)
          .then((resp) => {
            console.log(resp);
            toast.success('Dispute submitted successfully!');
            closeModal(); // Close modal after submission 
          })
          .catch(error => {
            // Optionally show an error message
            if(error.response.status === 400){
              toast.error('Dispute already raised with referenceNumber '+selectedTransaction.referenceNumber);
            }else
              toast.error('Dispute submission failed!');
          });
        }
  };

  return (
    <>
      <div className="container-fluid">
        <div className="table-wrapper">
          <div className="table-title">
            <div className="row">
              <div className="col-sm-5">
                <h2 style={{ color: "black" }}>List of<b> Outbound Transactions</b></h2>
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
                <th>Status</th>
                <th>Download</th>
              </tr>
            </thead>
            <tbody>
              {filteredOutbounds.map(txn => (
                <tr key={txn.Transaction_ID} onClick={() => openModal(txn)}>
                  <td style={{ cursor: 'pointer', color: 'blue' }}>{txn.referenceNumber}</td>
                  <td>{txn.currency}</td>
                  <td>{txn.transferAmount}</td>
                  <td>{txn.senderAccountNumber}</td>
                  <td>{txn.beneficiaryCreditAccountNumber}</td>
                  <td>{txn.transferDate}</td>
                  <td style={{ color: txn.passed ? 'green' : 'red' }}>
                    {txn.passed ? 'Success' : 'Failed'}
                  </td>
                  <td>
                    <FontAwesomeIcon
                      icon={faDownload}
                      style={{ cursor: 'pointer' }}
                      onClick={(e) => { e.stopPropagation(); handleDownload(txn.referenceNumber); }}
                    />
                  </td>
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
        <h2>Outbound Transaction Details</h2>
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
              <strong>Transaction File:</strong>
              <p>{"file_" + selectedTransaction.referenceNumber + ".txt "}<FontAwesomeIcon
                icon={faDownload}
                style={{ cursor: 'pointer' }}
                onClick={(e) => { e.stopPropagation(); handleDownload(selectedTransaction.referenceNumber); }}
              /></p>
            </div>
          </div>
        )}
        <div className="dispute-section" style={{ marginTop: '20px', textAlign: 'left' }}>
          <label style={{ display: 'flex', alignItems: 'center', marginBottom: '10px' }}>
            <input
              type="checkbox"
              checked={isDisputeChecked}
              onChange={() => setIsDisputeChecked(!isDisputeChecked)}
              style={{ marginRight: '-340px',marginLeft:'-350px' }} // Adjust space between checkbox and label
            />
            Raise Dispute
          </label>
          {isDisputeChecked && (
            <div>
              <textarea
                placeholder="Describe your dispute..."
                value={disputeDescription}
                onChange={(e) => setDisputeDescription(e.target.value)}
                rows="4"
                style={{ width: '100%', marginTop: '10px' }}
                required
              />
              <button onClick={handleDisputeSubmit} style={{ marginTop: '10px',backgroundColor:"red" }}>Submit Dispute</button>
            </div>
          )}
        </div>
        <div className="modal-footer">
          <button className="close-button" onClick={closeModal}>Close</button>
        </div>
      </Modal>
    </>
  );
};

export default OutboundComponent;