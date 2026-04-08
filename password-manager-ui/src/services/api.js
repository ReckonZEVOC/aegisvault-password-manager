import axios from "axios";

const API = axios.create({
  baseURL: "http://localhost:8080",
});

// 🔐 Add JWT automatically
API.interceptors.request.use((config) => {
  const token = localStorage.getItem("token");

  console.log("TOKEN:", token); // 🔥 check this

  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }

  return config;
});

export default API;