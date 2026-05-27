import axios from "axios";

export const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || "http://localhost:8080";

export const getAxiosInstance = () => {
  const jwt = localStorage.getItem("token");
  return axios.create({
    baseURL: API_BASE_URL,
    headers: {
      Authorization: `Bearer ${jwt}`,
      "Content-Type": "application/json",
    },
  });
}