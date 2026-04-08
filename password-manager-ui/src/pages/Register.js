import { useState } from "react";
import API from "../services/api";

function Register({ switchToLogin }) {

  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");

  const handleRegister = async () => {
    try {
      await API.post("/auth/register", null, {
        params: { username, password }
      });

      alert("Registered! Now login.");

    } catch {
      alert("Error registering");
    }
  };

  return (
    <div className="auth-container">

      <div className="auth-left">
        <h1>🔐 Secure Vault</h1>
        <p>Your personal password manager.</p>
      </div>

      <div className="auth-card">
        <h2>Register</h2>

        <input
          placeholder="Username"
          onChange={(e) => setUsername(e.target.value)}
        />

        <input
          type="password"
          placeholder="Password"
          onChange={(e) => setPassword(e.target.value)}
        />

        <button className="btn-success" onClick={handleRegister}>
          Register
        </button>

        <p className="switch">
          Already have an account?
          <span onClick={switchToLogin}> Login</span>
        </p>
      </div>

    </div>
  );
}

export default Register;