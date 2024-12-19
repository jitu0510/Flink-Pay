import React, { useState } from "react";
import { useForm } from 'react-hook-form';
import { login, registerSuccessfulLogin } from "../Services/AuthenticationService";
import { useHistory } from "react-router-dom";
import { FaUser , FaLock, FaSignInAlt, FaRedo } from 'react-icons/fa'; // Importing icons

const LoginComponent = () => {
  const [showError, setShowError] = useState(false);
  const { register, handleSubmit, errors, reset } = useForm();
  const history = useHistory();

  const containerStyle = {
    display: 'flex',
    justifyContent: 'center',
    alignItems: 'center',
    height: '100vh',
    backgroundColor: '#e0f7fa',
  };

  const cardStyle = {
    backgroundColor: '#ffffff',
    border: "none",
    borderRadius: "15px",
    width: "400px",
    boxShadow: "0 4px 20px rgba(0, 0, 0, 0.1)",
    padding: "30px",
    textAlign: 'center',
  };

  const titleStyle = {
    color: "#00796b",
    fontSize: "28px",
    fontWeight: "bold",
  };

  const buttonStyle = {
    borderRadius: "25px",
    padding: "10px 20px",
    fontSize: "16px",
    display: 'flex',
    alignItems: 'center',
  };

  const inputStyle = {
    border: "none",
    borderBottom: "2px solid #00796b",
    borderRadius: "0",
    outline: "none",
    padding: "10px 0",
    width: "100%",
    marginBottom: "20px",
  };

  const labelStyle = {
    textAlign: 'left',
    width: '100%',
    marginBottom: '5px',
    fontWeight: 'bold',
  };

  const onSubmit = (data) => {
    login(data)
      .then((response) => {
        const { role, username, jwtToken } = response.data;
        registerSuccessfulLogin(username, role, jwtToken);
        history.push("/dashboard");
      })
      .catch((error) => {
        window.alert("Invalid Credentials");
        console.error(error);
      });
  };

  return (
    <div style={containerStyle}>
      <section className="sign_up">
        <div className="card" style={cardStyle}>
          <div className="card-body">
            <h2 className="card-title" style={titleStyle}>Login</h2>
            <form className="form-signin" onSubmit={handleSubmit(onSubmit)}>
              <div className="form-floating mb-3">
                {/* <label htmlFor="username" style={labelStyle}>Username</label> */}
                <div style={{ display: 'flex', alignItems: 'center' }}>
                  <FaUser  style={{ marginRight: '10px', color: '#00796b' }} />
                  <input
                    type="text"
                    id="username"
                    name="username"
                    placeholder="Enter Your Username"
                    style={inputStyle}
                    ref={register({ required: true })}
                  />
                </div>
                {errors.username && (
                  <div className="invalid-feedback">Username is required</div>
                )}
              </div>

              <div className="form-floating mb-3">
                {/* <label htmlFor="inputPassword" style={labelStyle}>Password</label> */}
                <div style={{ display: 'flex', alignItems: 'center' }}>
                  <FaLock style={{ marginRight: '10px', color: '#00796b' }} />
                  <input
                    type="password"
                    id="inputPassword"
                    name="password"
                    placeholder="Enter Your Password"
                    style={inputStyle}
                    ref={register({ required: true })}
                  />
                </div>
                {errors.password && (
                  <div className="invalid-feedback">Password is required</div>
                )}
              </div>
              <a hidden href="#" className="underlined-link">Forgot password?</a>
              <div style={{ display: 'flex', justifyContent: 'center', gap: '10px' }}>
                <button className="btn btn-primary" style={buttonStyle} type="submit">
                  <FaSignInAlt style={{ marginRight: '5px' }} /> Login
                </button> <button className="btn btn-secondary" style={buttonStyle} type="button" onClick={() => reset()}>
                  <FaRedo style={{ marginRight: '5px' }} /> Reset
                </button>
              </div>
              <img src="flinkpay.png" alt="Logo" style={{ marginTop: '20px', width: '320px', height: 'auto' }} />
              <hr className="my-4" />
            </form>
          </div>
        </div>
      </section>
    </div>
  );
};

export default LoginComponent;