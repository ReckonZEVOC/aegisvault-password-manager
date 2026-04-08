import { useEffect, useState } from "react";
import API from "../services/api";
import { motion } from "framer-motion";

function Vault() {

  const [entries, setEntries] = useState([]);

  const [siteName, setSiteName] = useState("");
  const [siteUsername, setSiteUsername] = useState("");
  const [password, setPassword] = useState("");
  const [accountPassword, setAccountPassword] = useState("");

  const [decryptedPasswords, setDecryptedPasswords] = useState({});
  const [editingId, setEditingId] = useState(null);

  const fetchVault = async () => {
    try {
      const res = await API.get("/vault/list");
      setEntries(res.data);
    } catch {
      alert("Error fetching vault");
    }
  };

  useEffect(() => {
    fetchVault();
  }, []);

  const handleAdd = async () => {
    try {
      await API.post("/vault/add", {
        siteName,
        siteUsername,
        password,
        accountPassword
      });
      fetchVault();
      clearForm();
    } catch {
      alert("Error adding password");
    }
  };

  const handleDecrypt = async (id) => {
    try {
      const res = await API.post(`/vault/decrypt/${id}`, null, {
        params: { accountPassword }
      });

      setDecryptedPasswords(prev => ({
        ...prev,
        [id]: res.data
      }));
    } catch {
      alert("Wrong account password");
    }
  };

  const handleEdit = (entry) => {
    setEditingId(entry.id);
    setSiteName(entry.siteName);
    setSiteUsername(entry.siteUsername);
    setPassword("");
  };

  const handleUpdate = async () => {
    try {
      await API.put(`/vault/update/${editingId}`, null, {
        params: {
          siteName,
          siteUsername,
          newPassword: password,
          accountPassword
        }
      });

      setEditingId(null);
      fetchVault();
      clearForm();
    } catch {
      alert("Error updating");
    }
  };

  const handleDelete = async (id) => {
    try {
      await API.delete(`/vault/delete/${id}`);
      fetchVault();
    } catch {
      alert("Error deleting");
    }
  };

  const clearForm = () => {
    setSiteName("");
    setSiteUsername("");
    setPassword("");
    setAccountPassword("");
  };

  const handleLogout = () => {
    localStorage.removeItem("token");
    window.location.reload();
  };

  return (
    <div className="container">

      {/* 🔥 NAVBAR */}
      <div className="navbar">
        <h1>🔐 Secure Vault</h1>
        <button className="btn-danger" onClick={handleLogout}>Logout</button>
      </div>

      {/* 🔐 FORM */}
      <div className="glass">
        <h2>{editingId ? "Update Password" : "Add Password"}</h2>

        <input
          placeholder="Site Name"
          value={siteName}
          onChange={(e) => setSiteName(e.target.value)}
        />

        <input
          placeholder="Site Username"
          value={siteUsername}
          onChange={(e) => setSiteUsername(e.target.value)}
        />

        <input
          type="password"
          placeholder="Password"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
        />

        <input
          type="password"
          placeholder="Account Password"
          value={accountPassword}
          onChange={(e) => setAccountPassword(e.target.value)}
        />

        {editingId ? (
          <button className="btn-primary" onClick={handleUpdate}>
            Update
          </button>
        ) : (
          <button className="btn-success" onClick={handleAdd}>
            Add Password
          </button>
        )}
      </div>

      {/* 🔐 VAULT LIST */}
      {entries.length === 0 ? (
        <p className="empty">No passwords saved yet 🔐</p>
      ) : (
        entries.map((entry) => (
          <motion.div
            key={entry.id}
            className="card"
            initial={{ opacity: 0, y: 30 }}
            animate={{ opacity: 1, y: 0 }}
          >
            <h3>{entry.siteName}</h3>
            <p>👤 {entry.siteUsername}</p>
            <p>🔑 {decryptedPasswords[entry.id] || entry.password}</p>

            <div className="btn-group">
              <button className="btn-primary" onClick={() => handleDecrypt(entry.id)}>Decrypt</button>
              <button className="btn-success" onClick={() => handleEdit(entry)}>Edit</button>
              <button className="btn-danger" onClick={() => handleDelete(entry.id)}>Delete</button>
            </div>
          </motion.div>
        ))
      )}

    </div>
  );
}

export default Vault;