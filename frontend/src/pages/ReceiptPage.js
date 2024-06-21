import React, { useState, useEffect } from "react";
import { useParams, useLocation } from "react-router-dom";
import { refreshTokenIfNeeded } from "../utils/TokenService";

function ReceiptPage() {
  const { orderId } = useParams();
  const [payment, setPayment] = useState(null);
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  const { auctionId } = useParams();

  const location = useLocation();
  const state = location.state;

  console.log("state:", state);

  useEffect(() => {
    const fetchPaymentAndUserDetails = async () => {
      try {
        await refreshTokenIfNeeded();
        const token = localStorage.getItem("token");
        if (!token)
          throw new Error("No authentication token found. Please log in.");

        const paymentResponse = await fetch(
          `http://localhost:8080/api/v1/payments/auction/${auctionId}`,
          {
            headers: {
              "Content-Type": "application/json",
              Authorization: `Bearer ${token}`,
            },
          }
        );
        if (!paymentResponse.ok) throw new Error("Payment data fetch failed.");
        const paymentData = await paymentResponse.json();
        setPayment(paymentData);

        if (paymentData) {
          const userResponse = await fetch(
            `http://localhost:8080/api/v1/users/${paymentData.username}`,
            {
              headers: {
                "Content-Type": "application/json",
                Authorization: `Bearer ${token}`,
              },
            }
          );
          if (!userResponse.ok) throw new Error("User data fetch failed.");
          const userData = await userResponse.json();
          setUser(userData);

          console.log("payment data:", paymentData);
          console.log("user data:", userData);
        }
      } catch (error) {
        console.error("Error:", error);
        setError(error.message);
      } finally {
        setLoading(false);
      }
    };

    fetchPaymentAndUserDetails();
  }, [auctionId]);

  const getShippingDays = () => {
    const maxNumberOfDaysRegular = 20;
    const maxNumberOfDaysExpedited = 3;

    const days = state.expeditedShipping
      ? Math.floor(Math.random() * (maxNumberOfDaysExpedited - 1 + 1)) + 1 // for expedited shipping (1-3 days)
      : Math.floor(Math.random() * (maxNumberOfDaysRegular - 4 + 1)) + 4; // for standard shipping (4-20 days)
    return days;
  };

  if (loading) return <div>Loading...</div>;
  if (error) return <div>Error: {error}</div>;

  return (
    <>
      <div className="max-w-lg mx-auto bg-white p-8 mt-10 rounded-lg shadow-md">
        <h2 className="text-2xl font-bold mb-6">Receipt</h2>
        <p className="text-gray-700 mb-4">
          <strong>Full Name:</strong>{" "}
          {user?.fullName || `${user?.firstName} ${user?.lastName}`}
        </p>
        <p className="text-gray-700 mb-4">
          <strong>Shipping Address:</strong>{" "}
          {`${user?.shippingAddress.streetNumber} ${user?.shippingAddress.streetAddress}, ${user?.shippingAddress.city}, ${user?.shippingAddress.postalCode}, ${user?.shippingAddress.country}`}
        </p>
        <p className="text-gray-700 mb-4">
          <strong>Item ID:</strong> {payment?.auctionId}
        </p>
        <p className="text-gray-700 mb-4">
          <strong>Amount Paid:</strong> ${payment?.totalAmount}
        </p>
        <p className="text-gray-700">
          <strong>Payment Date:</strong> {new Date().toLocaleDateString()}
        </p>
      </div>
      <div className="max-w-lg mx-auto bg-white p-8 mt-10 rounded-lg shadow-md">
        <h2 className="text-2xl font-bold mb-6">Shipping details</h2>
        <p className="text-gray-700 mb-4">
          The item will be shipped in {getShippingDays()} days.
        </p>
      </div>
    </>
  );
}

export default ReceiptPage;
