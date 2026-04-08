import { useState } from "react";
import API from "../services/api";

function Login({ switchToRegister }) {

  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");

  const handleLogin = async () => {
    try {
      const res = await API.post("/auth/login", null, {
        params: { username, password }
      });

      localStorage.setItem("token", res.data);
      window.location.reload();

    } catch {
      alert("Login failed");
    }
  };

  return (
    <div className="auth-container">

      {/* LEFT SIDE */}
      <div className="auth-left">
        <h1>🔐 Secure Vault</h1>
        <p>Store your passwords safely with encryption.</p>
      </div>

      {/* RIGHT SIDE */}
      <div className="auth-card">
        <h2>Login</h2>

        <input
          placeholder="Username"
          onChange={(e) => setUsername(e.target.value)}
        />

        <input
          type="password"
          placeholder="Password"
          onChange={(e) => setPassword(e.target.value)}
        />

        <button className="btn-primary" onClick={handleLogin}>
          Login
        </button>

        <p className="switch">
          Don’t have an account?
          <span onClick={switchToRegister}> Register</span>
        </p>
      </div>

    </div>
  );
}

export default Login;