import React, { useState, useEffect } from "react";
import { useParams, useNavigate, useLocation } from "react-router-dom";
import { refreshTokenIfNeeded } from "../utils/TokenService";

function PurchasePage() {
  const location = useLocation();
  const state = location.state;
  const [user, setUser] = useState(null);
  const [error, setError] = useState("");

  const { auctionId } = useParams();
  const navigate = useNavigate();

  const [paymentDetails, setPaymentDetails] = useState({
    auctionId: state.auctionId,
    username: "",
    fullname: "",
    creditCard: "",
    csc: "",
    expiryDate: null,
    totalAmount: state.totalAmount,
  });

  useEffect(() => {
    console.log(
      "useEffect triggered for winnerUsername:",
      state.winnerUsername
    );
    const fetchUser = async () => {
      try {
        console.log("Fetching user...");
        await refreshTokenIfNeeded();
        const token = localStorage.getItem("token");
        if (!token) throw new Error("No token found");

        const response = await fetch(
          `http://localhost:8080/api/v1/users/${state.winnerUsername}`,
          {
            headers: {
              "Content-Type": "application/json",
              Authorization: `Bearer ${token}`,
            },
          }
        );

        if (!response.ok) throw new Error("Failed to fetch user");

        const userData = await response.json();
        setUser(userData);
        console.log("User data fetched:", userData);
      } catch (error) {
        console.error("Error fetching user:", error);
        setError(error.message);
      }
    };

    if (state && state.winnerUsername) {
      fetchUser();
    }
  }, [state.winnerUsername]);

  const handleChange = (event) => {
    const { name, value } = event.target;
    setPaymentDetails((prevDetails) => {
      const updatedDetails = { ...prevDetails, [name]: value };
      return updatedDetails;
    });
  };

  const handleSubmit = async (event) => {
    event.preventDefault();
    console.log("Submitting payment details...");

    try {
      await refreshTokenIfNeeded();
      const token = localStorage.getItem("token");
      if (!token) {
        throw new Error("No authentication token found. Please log in.");
      }

      const [month, year] = paymentDetails.expiryDate.split("/"); // expiryDate is in MM/YY format
      console.log("month, year", month, year);
      const expiryDate = new Date(parseInt(year) + 2000, parseInt(month) - 1);

      const paymentPayload = {
        ...paymentDetails,
        auctionId: state.auctionId,
        username: user.username,
        expiryDate: expiryDate,
      };

      console.log("Sending payment payload:", paymentPayload);

      const response = await fetch("http://localhost:8080/api/v1/payments", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify(paymentPayload),
      });

      if (!response.ok) {
        const errorData = await response.json();
        throw new Error(errorData.message || "Payment processing failed.");
      }

      console.log("Payment successful!");
      alert("Payment successful!");

      navigate(`/payment-success/${state.auctionId}`, {
        state: {
          username: user.username,
          expeditedShipping: state.expeditedShipping,
        },
      });
    } catch (error) {
      console.error("Payment submission error:", error);
      setError(error.message);
    }
  };

  return (
    <div className="max-w-4xl mx-auto mt-10 flex gap-4">
      <div className="max-w-lg mx-auto bg-white p-8 mt-10 rounded-lg shadow-md">
        <h2 className="text-2xl font-bold mb-6">Winning Bidder</h2>
        <div className="border-t border-gray-200">
          <div>
            <div className="bg-gray-50 px-4 py-5 sm:grid sm:grid-cols-3 sm:gap-4 sm:px-6">
              <div className="text-sm font-medium text-gray-500">
                First Name
              </div>
              <div className="text-sm text-gray-900 sm:col-span-2">
                {user?.firstName}
              </div>
            </div>
            <div className="bg-white px-4 py-5 sm:grid sm:grid-cols-3 sm:gap-4 sm:px-6">
              <div className="text-sm font-medium text-gray-500">Last Name</div>
              <div className="text-sm text-gray-900 sm:col-span-2">
                {user?.lastName}
              </div>
            </div>
          </div>
        </div>
        <div>
          <div className="bg-gray-50 px-4 py-5 sm:grid sm:grid-cols-3 sm:gap-4 sm:px-6">
            <div className="text-sm font-medium text-gray-500">Country</div>
            <div className="text-sm text-gray-900 sm:col-span-2">
              {user?.shippingAddress.country}
            </div>
          </div>
          <div className="bg-white px-4 py-5 sm:grid sm:grid-cols-3 sm:gap-4 sm:px-6">
            <div className="text-sm font-medium text-gray-500">City</div>
            <div className="text-sm text-gray-900 sm:col-span-2">
              {user?.shippingAddress.city}
            </div>
          </div>
          <div className="bg-gray-50 px-4 py-5 sm:grid sm:grid-cols-3 sm:gap-4 sm:px-6">
            <div className="text-sm font-medium text-gray-500">Postal Code</div>
            <div className="text-sm text-gray-900 sm:col-span-2">
              {user?.shippingAddress.postalCode}
            </div>
          </div>
          <div className="bg-white px-4 py-5 sm:grid sm:grid-cols-3 sm:gap-4 sm:px-6">
            <div className="text-sm font-medium text-gray-500">
              Street Address
            </div>
            <div className="text-sm text-gray-900 sm:col-span-2">
              {user?.shippingAddress.streetAddress}
            </div>
          </div>
          <div className="bg-gray-50 px-4 py-5 sm:grid sm:grid-cols-3 sm:gap-4 sm:px-6">
            <div className="text-sm font-medium text-gray-500">
              Street Number
            </div>
            <div className="text-sm text-gray-900 sm:col-span-2">
              {user?.shippingAddress.streetNumber}
            </div>
          </div>
        </div>

        <h2 className="text-xl mt-6 leading-6 font-medium text-gray-900">
          Total Price
        </h2>
        <div className="border-t border-gray-200">
          <div>
            <div className="bg-gray-50 px-4 py-5 sm:grid sm:grid-cols-3 sm:gap-4 sm:px-6">
              <div className="text-sm font-medium text-gray-500">$</div>
              <div className="mt-1 text-sm text-gray-900 sm:col-span-2">
                {state.totalAmount}
              </div>
            </div>
          </div>
        </div>
      </div>

      <div className="w-96 mx-auto bg-white p-8 mt-10 rounded-lg shadow-md">
        <h2 className="text-2xl font-bold mb-6">Payment Details</h2>
        <form onSubmit={handleSubmit}>
          <div className="mb-4">
            <label
              htmlFor="creditCard"
              className="block text-sm font-medium text-gray-700"
            >
              Card Number
            </label>
            <input
              type="text"
              id="creditCard"
              name="creditCard"
              value={paymentDetails.creditCard}
              onChange={handleChange}
              className="mt-1 focus:ring-lime-500 focus:border-lime-500 block w-full shadow-sm sm:text-sm border-gray-300 rounded-md"
              placeholder="1234 1234 1234 1234"
              required
            />
          </div>
          <div className="mb-4">
            <label
              htmlFor="fullname"
              className="block text-sm font-medium text-gray-700"
            >
              Cardholder Name
            </label>
            <input
              type="text"
              id="fullname"
              name="fullname"
              value={paymentDetails.fullname}
              onChange={handleChange}
              className="mt-1 focus:ring-lime-500 focus:border-lime-500 block w-full shadow-sm sm:text-sm border-gray-300 rounded-md"
              placeholder="John Doe"
              required
            />
          </div>
          <div className="mb-4">
            <label
              htmlFor="expiryDate"
              className="block text-sm font-medium text-gray-700"
            >
              Expiry Date
            </label>
            <input
              type="text"
              id="expiryDate"
              name="expiryDate"
              value={paymentDetails.expiryDate}
              onChange={handleChange}
              className="mt-1 focus:ring-lime-500 focus:border-lime-500 block w-full shadow-sm sm:text-sm border-gray-300 rounded-md"
              placeholder="MM/YY"
              required
            />
          </div>
          <div className="mb-6">
            <label
              htmlFor="csc"
              className="block text-sm font-medium text-gray-700"
            >
              CSC
            </label>
            <input
              type="text"
              id="csc"
              name="csc"
              value={paymentDetails.csc}
              onChange={handleChange}
              className="mt-1 focus:ring-lime-500 focus:border-lime-500 block w-full shadow-sm sm:text-sm border-gray-300 rounded-md"
              placeholder="123"
              required
            />
          </div>
          <button
            type="submit"
            className="w-full bg-lime-600 hover:bg-lime-700 text-white font-medium mt-32 py-2 px-4 border border-transparent rounded-md shadow-sm text-sm"
          >
            Submit Payment
          </button>
        </form>
      </div>
    </div>
  );
}

export default PurchasePage;
