import React, { useState, useEffect } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { refreshTokenIfNeeded } from "../utils/TokenService";

function UserProfilePage({ setIsAuthenticated }) {
  let { username } = useParams();
  const [user, setUser] = useState(null);
  const [isEditing, setIsEditing] = useState(false);
  const [updatedUser, setUpdatedUser] = useState({});
  const navigate = useNavigate();

  useEffect(() => {
    const fetchUser = async () => {
      try {
        await refreshTokenIfNeeded();
        const token = localStorage.getItem("token");
        if (!token) throw new Error("No token found");

        const response = await fetch(
          `http://localhost:8080/api/v1/users/${username}`,
          {
            headers: {
              "Content-Type": "application/json",
              Authorization: `Bearer ${token}`,
            },
          }
        );

        if (!response.ok) throw new Error("Failed to fetch");

        const userData = await response.json();
        setUser(userData);
        setUpdatedUser(userData);
      } catch (error) {
        console.error("Error:", error);
      }
    };

    fetchUser();
  }, [username]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    if (name.startsWith("shippingAddress.")) {
      const field = name.split(".")[1];
      setUpdatedUser({
        ...updatedUser,
        shippingAddress: {
          ...updatedUser.shippingAddress,
          [field]: value,
        },
      });
    } else {
      setUpdatedUser({ ...updatedUser, [name]: value });
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      await refreshTokenIfNeeded();
      const token = localStorage.getItem("token");
      if (!token) {
        throw new Error("No authentication token found");
      }

      const response = await fetch(
        `http://localhost:8080/api/v1/users/update/${username}`,
        {
          method: "PUT",
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${token}`,
          },
          body: JSON.stringify(updatedUser),
        }
      );
      if (!response.ok) throw new Error("Failed to update");
      const updatedUserData = await response.json();
      setUser(updatedUserData);
      setIsEditing(false);
    } catch (error) {
      console.error("Error:", error);
    }
  };

  const handleDeleteAccount = async () => {
    if (
      window.confirm(
        "Are you sure you want to delete your account? This action cannot be undone."
      )
    ) {
      try {
        await refreshTokenIfNeeded();
        const token = localStorage.getItem("token");
        if (!token) {
          throw new Error("No authentication token found");
        }

        const response = await fetch(
          `http://localhost:8080/api/v1/users/delete/${username}`,
          {
            method: "DELETE",
            headers: {
              "Content-Type": "application/json",
              Authorization: `Bearer ${token}`,
            },
          }
        );

        if (!response.ok) throw new Error("Failed to delete account");

        localStorage.removeItem("token");
        localStorage.removeItem("refreshToken");
        setIsAuthenticated(false);
        navigate("/login");
      } catch (error) {
        console.error("Error:", error);
      }
    }
  };

  if (!user) return <div>Loading...</div>;

  return (
    <div className="min-h-screen flex flex-col items-center justify-center bg-gray-50 py-12 px-4 sm:px-6 lg:px-8">
      <div className="max-w-md w-full space-y-8 bg-white shadow overflow-hidden sm:rounded-lg p-6">
        {!isEditing ? (
          <>
            <h2 className="text-xl leading-6 font-medium text-gray-900">
              User Profile
            </h2>
            <div className="border-t border-gray-200">
              <div>
                <div className="bg-gray-50 px-4 py-5 sm:grid sm:grid-cols-3 sm:gap-4 sm:px-6">
                  <div className="text-sm font-medium text-gray-500">
                    Username
                  </div>
                  <div className="mt-1 text-sm text-gray-900 sm:col-span-2">
                    {user.username}
                  </div>
                </div>
                <div className="bg-white px-4 py-5 sm:grid sm:grid-cols-3 sm:gap-4 sm:px-6">
                  <div className="text-sm font-medium text-gray-500">Email</div>
                  <div className="mt-1 text-sm text-gray-900 sm:col-span-2">
                    {user.email}
                  </div>
                </div>
                <div className="bg-gray-50 px-4 py-5 sm:grid sm:grid-cols-3 sm:gap-4 sm:px-6">
                  <div className="text-sm font-medium text-gray-500">
                    First Name
                  </div>
                  <div className="mt-1 text-sm text-gray-900 sm:col-span-2">
                    {user.firstName}
                  </div>
                </div>
                <div className="bg-white px-4 py-5 sm:grid sm:grid-cols-3 sm:gap-4 sm:px-6">
                  <div className="text-sm font-medium text-gray-500">
                    Last Name
                  </div>
                  <div className="mt-1 text-sm text-gray-900 sm:col-span-2">
                    {user.lastName}
                  </div>
                </div>
              </div>
            </div>
            <h2 className="text-xl leading-6 font-medium text-gray-900">
              Shipping address
            </h2>
            <div className="border-t border-gray-200">
              <div>
                <div className="bg-gray-50 px-4 py-5 sm:grid sm:grid-cols-3 sm:gap-4 sm:px-6">
                  <div className="text-sm font-medium text-gray-500">
                    Country
                  </div>
                  <div className="mt-1 text-sm text-gray-900 sm:col-span-2">
                    {user.shippingAddress.country}
                  </div>
                </div>
                <div className="bg-white px-4 py-5 sm:grid sm:grid-cols-3 sm:gap-4 sm:px-6">
                  <div className="text-sm font-medium text-gray-500">City</div>
                  <div className="mt-1 text-sm text-gray-900 sm:col-span-2">
                    {user.shippingAddress.city}
                  </div>
                </div>
                <div className="bg-gray-50 px-4 py-5 sm:grid sm:grid-cols-3 sm:gap-4 sm:px-6">
                  <div className="text-sm font-medium text-gray-500">
                    Postal Code
                  </div>
                  <div className="mt-1 text-sm text-gray-900 sm:col-span-2">
                    {user.shippingAddress.postalCode}
                  </div>
                </div>
                <div className="bg-white px-4 py-5 sm:grid sm:grid-cols-3 sm:gap-4 sm:px-6">
                  <div className="text-sm font-medium text-gray-500">
                    Street Address
                  </div>
                  <div className="mt-1 text-sm text-gray-900 sm:col-span-2">
                    {user.shippingAddress.streetAddress}
                  </div>
                </div>
                <div className="bg-gray-50 px-4 py-5 sm:grid sm:grid-cols-3 sm:gap-4 sm:px-6">
                  <div className="text-sm font-medium text-gray-500">
                    Street Number
                  </div>
                  <div className="mt-1 text-sm text-gray-900 sm:col-span-2">
                    {user.shippingAddress.streetNumber}
                  </div>
                </div>
              </div>
            </div>
            <button
              onClick={() => setIsEditing(true)}
              className="mt-4 py-2 px-4 border border-transparent text-sm font-medium rounded-md text-white bg-lime-600 hover:bg-lime-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-lime-500"
            >
              Edit Profile
            </button>
            <button
              onClick={handleDeleteAccount}
              className="mt-4 mx-4 py-2 px-4 border border-transparent text-sm font-medium rounded-md text-white bg-red-600 hover:bg-red-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-red-500"
            >
              Delete Account
            </button>
          </>
        ) : (
          <form onSubmit={handleSubmit} className="space-y-6">
            <h2 className="text-xl leading-6 font-medium text-gray-900">
              User Profile
            </h2>
            <div>
              <label
                htmlFor="email"
                className="block text-sm font-medium text-gray-700"
              >
                Email
              </label>
              <input
                id="email"
                name="email"
                type="email"
                required
                className="mt-1 block w-full pl-3 pr-10 py-2 text-base border border-gray-300 placeholder-gray-500 text-gray-900 rounded-md focus:outline-none focus:ring-lime-500 focus:border-lime-500 sm:text-sm"
                value={updatedUser.email}
                onChange={handleChange}
              />
            </div>

            <div>
              <label
                htmlFor="firstName"
                className="block text-sm font-medium text-gray-700"
              >
                First Name
              </label>
              <input
                id="firstName"
                name="firstName"
                type="firstName"
                required
                className="mt-1 block w-full pl-3 pr-10 py-2 text-base border border-gray-300 placeholder-gray-500 text-gray-900 rounded-md focus:outline-none focus:ring-lime-500 focus:border-lime-500 sm:text-sm"
                value={updatedUser.firstName}
                onChange={handleChange}
              />
            </div>

            <div>
              <label
                htmlFor="lastName"
                className="block text-sm font-medium text-gray-700"
              >
                Last Name
              </label>
              <input
                id="lastName"
                name="lastName"
                type="lastName"
                required
                className="mt-1 block w-full pl-3 pr-10 py-2 text-base border border-gray-300 placeholder-gray-500 text-gray-900 rounded-md focus:outline-none focus:ring-lime-500 focus:border-lime-500 sm:text-sm"
                value={updatedUser.lastName}
                onChange={handleChange}
              />
            </div>

            <h2 className="text-xl leading-6 font-medium text-gray-900">
              Shipping address
            </h2>

            <div>
              <label
                htmlFor="shippingAddress.country"
                className="block text-sm font-medium text-gray-700"
              >
                Country
              </label>
              <input
                id="country"
                name="shippingAddress.country"
                type="country"
                required
                className="mt-1 block w-full pl-3 pr-10 py-2 text-base border border-gray-300 placeholder-gray-500 text-gray-900 rounded-md focus:outline-none focus:ring-lime-500 focus:border-lime-500 sm:text-sm"
                value={updatedUser.shippingAddress.country}
                onChange={handleChange}
              />
            </div>

            <div>
              <label
                htmlFor="shippingAddress.city"
                className="block text-sm font-medium text-gray-700"
              >
                City
              </label>
              <input
                id="city"
                name="shippingAddress.city"
                type="city"
                required
                className="mt-1 block w-full pl-3 pr-10 py-2 text-base border border-gray-300 placeholder-gray-500 text-gray-900 rounded-md focus:outline-none focus:ring-lime-500 focus:border-lime-500 sm:text-sm"
                value={updatedUser.shippingAddress.city}
                onChange={handleChange}
              />
            </div>

            <div>
              <label
                htmlFor="shippingAddress.postalCode"
                className="block text-sm font-medium text-gray-700"
              >
                Postal Code
              </label>
              <input
                id="postalCode"
                name="shippingAddress.postalCode"
                type="postalCode"
                required
                className="mt-1 block w-full pl-3 pr-10 py-2 text-base border border-gray-300 placeholder-gray-500 text-gray-900 rounded-md focus:outline-none focus:ring-lime-500 focus:border-lime-500 sm:text-sm"
                value={updatedUser.shippingAddress.postalCode}
                onChange={handleChange}
              />
            </div>

            <div>
              <label
                htmlFor="streetAddress"
                className="block text-sm font-medium text-gray-700"
              >
                Street Address
              </label>
              <input
                id="streetAddress"
                name="shippingAddress.streetAddress"
                type="streetAddress"
                required
                className="mt-1 block w-full pl-3 pr-10 py-2 text-base border border-gray-300 placeholder-gray-500 text-gray-900 rounded-md focus:outline-none focus:ring-lime-500 focus:border-lime-500 sm:text-sm"
                value={updatedUser.shippingAddress.streetAddress}
                onChange={handleChange}
              />
            </div>

            <div>
              <label
                htmlFor="shippingAddress.streetNumber"
                className="block text-sm font-medium text-gray-700"
              >
                Street Number
              </label>
              <input
                id="streetNumber"
                name="shippingAddress.streetNumber"
                type="streetNumber"
                required
                className="mt-1 block w-full pl-3 pr-10 py-2 text-base border border-gray-300 placeholder-gray-500 text-gray-900 rounded-md focus:outline-none focus:ring-lime-500 focus:border-lime-500 sm:text-sm"
                value={updatedUser.shippingAddress.streetNumber}
                onChange={handleChange}
              />
            </div>

            <button
              type="submit"
              className="group relative w-full flex justify-center py-2 px-4 border border-transparent text-sm font-medium rounded-md text-white bg-lime-600 hover:bg-lime-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-lime-500"
            >
              Save Changes
            </button>
            <button
              onClick={() => setIsEditing(false)}
              className="mt-4 py-2 px-4 border border-transparent text-sm font-medium rounded-md text-gray-700 bg-gray-200 hover:bg-gray-400 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-lime-500"
            >
              Cancel
            </button>
          </form>
        )}
      </div>
    </div>
  );
}

export default UserProfilePage;
