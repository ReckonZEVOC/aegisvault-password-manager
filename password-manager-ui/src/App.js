import { useState } from "react";
import Login from "./pages/Login";
import Register from "./pages/Register";
import Vault from "./pages/Vault";
import Landing from "./pages/Landing";
import "./App.css";

function App() {

  const [page, setPage] = useState("landing");
  const token = localStorage.getItem("token");

  if (token) return <Vault />;

  if (page === "landing") {
    return <Landing goToLogin={() => setPage("login")} />;
  }

  if (page === "login") {
    return (
      <Login switchToRegister={() => setPage("register")} />
    );
  }

  return (
    <Register switchToLogin={() => setPage("login")} />
  );
}

export default App;