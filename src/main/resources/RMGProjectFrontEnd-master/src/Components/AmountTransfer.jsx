import React, { useState , useEffect} from 'react';
import '../CSS/SendAmountTransfer.css';
import { ToastContainer, toast } from 'react-toastify';
import { useHistory } from 'react-router-dom';
import { useForm } from 'react-hook-form';
import { faPlus, faSearch } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { FaArrowRight, FaRedo } from 'react-icons/fa';
import { transferAmount } from '../Services/AmountTransferService';

const AmountTransferForm = () => {
  const history = useHistory();
  const { register, errors, reset } = useForm({
    defaultValues: {
      transferDate: new Date().toLocaleDateString(),
      referenceNumber: 'System Generated',
      currency: 'BZD',
      senderAccountNumber: '',
      senderAccountName: '',
      chargesCode: 'RTCU',
      remarks: '',
      beneficiaryType: 'Bank',
      beneficiaryCreditAccountNumber: '',
      beneficiaryAccountName: '',
      transferAmount: '',
      beneficiaryMobileNumber: '',
      bankBranch: '',
      bankName: ''
    }
  });

  useEffect(() => {
    localStorage.setItem("page","/dashboard/amount-transfer");
}, [])
  const [formData, setFormData] = useState({
    transferDate: new Date().toLocaleDateString(),
    referenceNumber: 'System Generated',
    currency: 'BZD',
    senderAccountNumber: '',
    senderAccountName: '',
    chargesCode: 'RTCU',
    remarks: '',
    beneficiaryType: 'Bank',
    beneficiaryCreditAccountNumber: '',
    beneficiaryAccountName: '',
    transferAmount: '',
    beneficiaryMobileNumber: '',
    bankBranch: '',
    bankName: ''
  });

  const buttonStyle = {
    borderRadius: "25px",
    padding: "10px 20px",
    fontSize: "16px",
    display: 'flex',
    alignItems: 'center',
    marginTop: "0px"
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({
      ...formData,
      [name]: value
    });
  };

  const handleSubmit = (e) => {
    e.preventDefault(); // Prevent default form submission
    console.log(formData);
    transferAmount(formData)
      .then((resp) => {
        console.log(resp);
        alert("Success! Your reference number is " + resp.data.referenceNumber)
        history.push("/dashboard/outbounds")
        
      })
      .catch(error => {
        console.error('Error While Making the transaction:', error);
        // Optionally show an error message
        toast.error('Transfer failed!');
      });

   // history.push('/amount-transfer'); // Redirect after successful transfer
  };

  const handleReset = () => {
    reset(); // Reset the form fields to their default values
    setFormData({
      transferDate: new Date().toLocaleDateString(),
      referenceNumber: 'System Generated',
      currency: 'BZD',
      senderAccountNumber: '',
      senderAccountName: '',
      chargesCode: 'RTCU',
      remarks: '',
      beneficiaryType: 'Bank',
      beneficiaryCreditAccountNumber: '',
      beneficiaryAccountName: '',
      transferAmount: '',
      beneficiaryMobileNumber: '',
      bankBranch: '',
      bankName: ''
    });
  };

  return (
    <form onSubmit={handleSubmit}>
      <div className="form-wrapper">
        <h5 style={{ textAlign: 'left', marginLeft: "5px" }}>Single Customer To Customer Transfer</h5>

        <div className="form-container" style={{
          marginTop: "5px",
          marginBottom: "0px",
          marginLeft: "5px",
          marginRight: "0px",
          width: "100%",
          borderRadius: "10px",
          padding: "20px",
          paddingTop: "0px",
          paddingBottom: "5px",
          display: 'grid',
          gridTemplateColumns: 'repeat(4, 1fr)',
          gap: '10px'
        }}>
          <div className="form-group">
            <label>Beneficiary Type<span style={{ color: 'red' }}>*</span>:</ label>
            <select
              name="beneficiaryType"
              value={formData.beneficiaryType}
              onChange={handleChange}
            >
              <option value="Bank">Bank</option>
              <option value="Credit Union">Credit Union</option>
            </select>
          </div>
          <div className="form-group">
            <label>Reference Number<span style={{ color: 'red' }}>*</span>:</label>
            <input style={{ backgroundColor: "grey" }}
              type="text"
              name="referenceNumber"
              value="System Generated"
              onChange={handleChange}
              readOnly
            />
          </div>
          <div className="form-group">
            <label>Currency<span style={{ color: 'red' }}>*</span>:</label>
            <input style={{ backgroundColor: "grey" }}
              type="text"
              name="currency"
              value="BZD"
              onChange={handleChange}
              readOnly
            />
          </div>
          <div className="form-group">
            <label>Transfer Date<span style={{ color: 'red' }}>*</span>:</label>
            <input style={{ backgroundColor: "grey" }}
              type="text"
              name="transferDate"
              value={new Date().toLocaleDateString()}
              onChange={handleChange}
              readOnly
            />
          </div>

          <div className="form-group" style={{ gridColumn: 'span 4' }}>
            <h4>Originator Details</h4>
            <div style={{ display: 'grid', gridTemplateColumns: 'repeat(4, 1fr)', gap: '20px' }}>
              <div>
                <label>Debtor Account Number<span style={{ color: 'red' }}>*</span>:</label>
                <input
                  type="text"
                  name="senderAccountNumber"
                  value={formData.senderAccountNumber}
                  onChange={handleChange}
                  required
                />
              </div>
              <div>
                <label>Account Name<span style={{ color: 'red' }}>*</span>:</label>
                <input
                  type="text"
                  name="senderAccountName"
                  value={formData.senderAccountName}
                  onChange={handleChange}
                  required
                />
              </div>
              <div className="form-group">
                <label>Charges Code<span style={{ color: 'red' }}>*</span>:</label>
                <select
                  name="chargesCode"
                  value={formData.chargesCode}
                  onChange={handleChange}
                >
                  <option value="RTCU">RTCU</option>
                  <option value="ROICU">ROICU</option>
                  <option value="RTGSOI">RTGSOI</option>
                  <option value="STGSOC">STGSOC</option>
                </select>
              </div>
              <div>
                <label>Remarks:</label>
                <input
                  type="text"
                  name="remarks"
                  value={formData.remarks}
                  onChange={handleChange}
                />
              </div>
            </div>
          </div>

          <div className="form-group" style={{ gridColumn: 'span 4' }}>
            <h4>Beneficiary Details</h4>
            <div style={{ display: 'grid', gridTemplateColumns: 'repeat(4, 1fr)', gap: '20px' }}>
              <div>
                <label>Credit Account Number<span style={{ color: 'red' }}>*</span>:</label>
                <input
                  type="text"
                  name="beneficiaryCreditAccountNumber"
                  value={formData.beneficiaryCreditAccountNumber}
                  onChange={handleChange}
                  required
                />
              </div>
              <div>
                <label>Account Name<span style={{ color: 'red' }}>*</span>:</label>
                <input
                  type="text"
                  name="beneficiaryAccountName"
                  value={formData.beneficiaryAccountName}
                  onChange={handleChange}
                  required
                />
              </div>
              <div>
                <label>Transfer Amount<span style={{ color: 'red' }}>*</span>:</label>
                <input
                  type="number"
                  name="transferAmount"
                  min={1}
                  step="any"
                  value={formData.transferAmount}
                  onChange={handleChange}
                  required
                />
              </div>
              <div>
                <label>Mobile Number<span style={{ color: 'red' }}>*</span>:</label>
                <input
                  type="text"
                  name="beneficiaryMobileNumber"
 value={formData.beneficiaryMobileNumber}
                  onChange={handleChange}
                  required
                />
              </div>
            </div>
          </div>

          <div className="form-group">
            <label>Bank Branch<span style={{ color: 'red' }}>*</span>:</label>
            <input
              type="text"
              name="bankBranch"
              value={formData.bankBranch}
              onChange={handleChange}
              required
            />
          </div>
          <div className="form-group">
            <label>Bank Name<span style={{ color: 'red' }}>*</span>:</label>
            <input
              type="text"
              name="bankName"
              value={formData.bankName}
              onChange={handleChange}
              required
            />
          </div>
        </div>
      </div>
      <div style={{ display: 'flex', justifyContent: 'center', gap: '10px' }}>
        <button className="btn btn-primary" style={buttonStyle} type="submit">
          <FaArrowRight style={{ marginRight: '5px', marginTop: "0px" }} /> Transfer
        </button>
        <button className="btn btn-secondary" style={buttonStyle} type="button" onClick={handleReset}>
          <FaRedo style={{ marginRight: '5px', marginTop: "0px" }} /> Reset
        </button>
      </div>
      <ToastContainer />
    </form>
  );
};

export default AmountTransferForm;