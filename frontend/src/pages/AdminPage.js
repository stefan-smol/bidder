import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { getRoleFromToken, refreshTokenIfNeeded } from "../utils/TokenService";

function AdminPage() {
  const [users, setUsers] = useState([]);
  const [auctions, setAuctions] = useState([]);
  const navigate = useNavigate();

  useEffect(() => {
    const role = getRoleFromToken(localStorage.getItem("token"));
    if (role !== "ROLE_ADMIN") {
      navigate("/");
    }

    const fetchUsers = async () => {
      await refreshTokenIfNeeded();
      const token = localStorage.getItem("token");
      if (!token) {
        throw new Error("No authentication token found. Please log in.");
      }

      const response = await fetch("http://localhost:8080/api/v1/users/all", {
        headers: {
          Authorization: `Bearer ${token}`,
          "Content-Type": "application/json",
        },
      });

      if (response.ok) {
        const data = await response.json();
        setUsers(data);
        console.log("users: ", users);
      } else {
        console.error("Failed to fetch users");
      }
    };

    const fetchAuctions = async () => {
      await refreshTokenIfNeeded();
      const token = localStorage.getItem("token");
      if (!token) {
        throw new Error("No authentication token found. Please log in.");
      }

      const response = await fetch(
        "http://localhost:8080/api/v1/catalogue/auctions",
        {
          headers: {
            Authorization: `Bearer ${token}`,
            "Content-Type": "application/json",
          },
        }
      );

      if (response.ok) {
        const data = await response.json();
        setAuctions(data);
      } else {
        console.error("Failed to fetch auctions");
      }
    };

    fetchUsers();
    fetchAuctions();
  }, [navigate]);

  const deleteUser = async (username) => {
    await fetch(`http://localhost:8080/api/v1/users/delete/${username}`, {
      method: "DELETE",
      headers: {
        Authorization: `Bearer ${localStorage.getItem("token")}`,
      },
    });
    setUsers(users.filter((user) => user.username !== username));
  };

  const deleteAuction = async (auctionId) => {
    await fetch(
      `http://localhost:8080/api/v1/catalogue/auctions/${auctionId}`,
      {
        method: "DELETE",
        headers: {
          Authorization: `Bearer ${localStorage.getItem("token")}`,
        },
      }
    );
    setAuctions(auctions.filter((auction) => auction.id !== auctionId));
  };

  return (
    <div className="admin-page-container mx-auto p-8">
      <h1 className="text-4xl font-bold text-center text-gray-800 mb-10">
        Admin Dashboard
      </h1>
      <div className="users-section mb-10">
        <h2 className="text-3xl font-semibold text-gray-700 mb-6">Users</h2>
        <div className="overflow-x-auto">
          <table className="min-w-full leading-normal">
            <thead>
              <tr className="bg-gray-100 text-left text-gray-600 uppercase text-sm leading-normal">
                <th className="py-3 px-6">Username</th>
                <th className="py-3 px-6">Role</th>
                <th className="py-3 px-6">Actions</th>
              </tr>
            </thead>
            <tbody>
              {users.map((user) => (
                <tr
                  key={user.id}
                  className="border-b border-gray-200 hover:bg-gray-100"
                >
                  <td className="py-3 px-6 text-gray-700">{user.username}</td>
                  <td className="py-3 px-6 text-gray-700">{user.role}</td>
                  <td className="py-3 px-6">
                    <button
                      className="bg-red-500 hover:bg-red-700 text-white font-semibold py-2 px-4 rounded shadow"
                      onClick={() => deleteUser(user.username)}
                    >
                      Delete
                    </button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>

      <div className="auctions-section">
        <h2 className="text-3xl font-semibold text-gray-700 mb-6">Auctions</h2>
        <div className="overflow-x-auto">
          <table className="min-w-full leading-normal">
            <thead>
              <tr className="bg-gray-100 text-left text-gray-600 uppercase text-sm leading-normal">
                <th className="py-3 px-6">ID</th>
                <th className="py-3 px-6">Item Name</th>
                <th className="py-3 px-6">Actions</th>
              </tr>
            </thead>
            <tbody>
              {auctions.map((auction) => (
                <tr
                  key={auction.id}
                  className="border-b border-gray-200 hover:bg-gray-100"
                >
                  <td className="py-3 px-6 text-gray-700">{auction.id}</td>
                  <td className="py-3 px-6 text-gray-700">
                    {auction.item.name}
                  </td>
                  <td className="py-3 px-6">
                    <button
                      className="bg-red-500 hover:bg-red-700 text-white font-semibold py-2 px-4 rounded shadow"
                      onClick={() => deleteAuction(auction.id)}
                    >
                      Delete
                    </button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  );
}

export default AdminPage;
