import React, { useState, useEffect, useRef, useCallback } from "react";
import { useParams, useNavigate } from "react-router-dom";
import SockJS from "sockjs-client";
import { Stomp } from "@stomp/stompjs";

import {
  getUsernameFromToken,
  refreshTokenIfNeeded,
} from "../utils/TokenService";

function AuctionPage() {
  const [bid, setBid] = useState("");
  const [newPrice, setNewPrice] = useState("");
  const [auctionData, setAuctionData] = useState(null);
  const [timeLeft, setTimeLeft] = useState("");
  const [isOwner, setIsOwner] = useState(false);
  const [error, setError] = useState("");
  const { auctionId } = useParams();
  const navigate = useNavigate();

  const stompClientRef = useRef(null);

  const [auctionDetails, setAuctionDetails] = useState(null);
  const [itemDetails, setItemDetails] = useState(null);

  const maxReconnectAttempts = 3;
  const reconnectInterval = 5000;

  const connectToWebSocket = useCallback(() => {
    let reconnectAttempts = 0;
    const attemptReconnect = () => {
      if (reconnectAttempts < maxReconnectAttempts) {
        setTimeout(() => {
          console.log(`Attempt ${reconnectAttempts + 1} to reconnect`);
          reconnectAttempts++;
          if (reconnectAttempts < maxReconnectAttempts) {
            connectToWebSocket();
          } else {
            console.log("Max reconnection attempts reached. Giving up.");
          }
        }, reconnectInterval);
      }
    };

    const onError = (err) => {
      console.error("WebSocket Error:", err);
      attemptReconnect();
    };

    const socket = new SockJS("http://localhost:8083/ws");
    const client = Stomp.over(() => socket);
    client.debug = () => {};

    client.connect(
      {},
      () => {
        client.subscribe(`/topic/auction.${auctionId}`, (message) => {
          const notification = JSON.parse(message.body);

          console.log("Received notification:", notification);
          console.log("Notification Type:", notification.notifcationType);

          if (
            notification.notifcationType === "bid" ||
            notification.notifcationType === "purchase"
          ) {
            setAuctionData((prevData) => ({
              ...prevData,
              currentHighestBid: notification.bidAmount,
              highestBidder: notification.bidderUsername,
            }));
            console.log("updated bid or purchase!");
          } else if (notification.notifcationType === "update-price") {
            setAuctionData((prevData) => ({
              ...prevData,
              purchasePrice: notification.bidAmount,
            }));
            console.log("updated dutch auction price!");
          }
        });

        reconnectAttempts = 0;
      },
      onError
    );

    stompClientRef.current = client;
  }, [auctionId]);

  useEffect(() => {
    if (!stompClientRef.current) {
      connectToWebSocket();
    }

    return () => {
      if (stompClientRef.current) {
        stompClientRef.current.disconnect();
        stompClientRef.current = null;
      }
    };
  }, [auctionId, connectToWebSocket]);

  useEffect(() => {
    const fetchAuctionData = async () => {
      try {
        await refreshTokenIfNeeded();
        const token = localStorage.getItem("token");
        if (!token) {
          throw new Error("No authentication token found. Please log in.");
        }

        const response = await fetch(
          `http://localhost:8080/api/v1/catalogue/auctions/${auctionId}`,
          {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          }
        );

        if (!response.ok) {
          throw new Error("Failed to fetch auction data");
        }

        const data = await response.json();

        console.log("Auction data:", data);

        setAuctionData(data);
        setAuctionDetails(data);
        setItemDetails(data.item);

        const username = token ? getUsernameFromToken(token) : null;
        setIsOwner(username === data.sellerUsername);

        if (data.auctionType === "FORWARD" && data.expiryTime) {
          const expiryTime = new Date(data.expiryTime);
          const interval = setInterval(() => {
            const now = new Date();
            const distance = expiryTime.getTime() - now.getTime();

            if (distance > 0) {
              const days = Math.floor(distance / (1000 * 60 * 60 * 24));
              const hours = Math.floor(
                (distance % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60)
              );
              const minutes = Math.floor(
                (distance % (1000 * 60 * 60)) / (1000 * 60)
              );
              const seconds = Math.floor((distance % (1000 * 60)) / 1000);

              setTimeLeft(`${days}d ${hours}h ${minutes}m ${seconds}s`);
            } else {
              clearInterval(interval);
              setTimeLeft("Auction ended");
            }
          }, 1000);

          return () => clearInterval(interval);
        } else {
          setTimeLeft("No expiry for this auction");
        }
      } catch (error) {
        console.error("Error fetching auction data:", error);
      }
    };

    fetchAuctionData();
  }, [auctionId, navigate]);

  useEffect(() => {
    let redirectTimeout;

    if (
      auctionData?.auctionType === "FORWARD" &&
      timeLeft === "Auction ended"
    ) {
      redirectTimeout = setTimeout(() => {
        navigate(`/pay-now/${auctionId}`);
      }, 5000);
    }

    if (
      auctionData?.auctionType === "DUTCH" &&
      (auctionData?.highestBidder || auctionData?.status === "EXPIRED")
    ) {
      redirectTimeout = setTimeout(() => {
        navigate(`/pay-now/${auctionId}`);
      }, 5000);
    }

    return () => clearTimeout(redirectTimeout);
  }, [auctionData, timeLeft, navigate]);

  const handleBidSubmit = async (event) => {
    event.preventDefault();
    setError("");

    try {
      await refreshTokenIfNeeded();
      const token = localStorage.getItem("token");
      if (!token) {
        throw new Error("No authentication token found. Please log in.");
      }

      const bidTime = new Date().toISOString();

      const response = await fetch("http://localhost:8080/api/v1/process/bid", {
        method: "POST",
        headers: {
          Authorization: `Bearer ${token}`,
          "Content-Type": "application/json",
        },
        body: JSON.stringify({ auctionId, bidAmount: bid, bidTime }),
      });

      const result = await response.json();

      console.log("result: ", result);

      if (!response.ok) {
        throw new Error(result.error || "Bid submission failed.");
      }

      setBid("");
    } catch (error) {
      console.error("Bid submission error:", error.message);
      setError(error.message);
    }
  };

  const handlePurchaseSubmit = async (event) => {
    event.preventDefault();
    setError("");

    try {
      await refreshTokenIfNeeded();
      const token = localStorage.getItem("token");
      if (!token) {
        throw new Error("No authentication token found. Please log in.");
      }

      const purchaseAmount = auctionData.purchasePrice;

      const purchaseTime = new Date().toISOString();

      const response = await fetch(
        "http://localhost:8080/api/v1/process/purchase",
        {
          method: "POST",
          headers: {
            Authorization: `Bearer ${token}`,
            "Content-Type": "application/json",
          },
          body: JSON.stringify({
            auctionId,
            purchaseAmount,
            purchaseTime,
          }),
        }
      );

      const result = await response.json();

      if (!response.ok) {
        throw new Error(result.error || "Purchase submission failed.");
      }

      alert("Purchase successful!");
    } catch (error) {
      console.error("Purchase submission error:", error.message);
      setError(error.message);
    }
  };

  const handlePriceReductionSubmit = async (event) => {
    event.preventDefault();
    setError("");

    try {
      await refreshTokenIfNeeded();
      const token = localStorage.getItem("token");
      if (!token) {
        throw new Error("No authentication token found. Please log in.");
      }

      const updatedAuctionDetails = {
        ...auctionDetails,
        startingPrice: newPrice,
        purchasePrice: newPrice,
      };

      console.log("newPrice: ", newPrice);

      const response = await fetch(
        `http://localhost:8080/api/v1/catalogue/auctions/decrease-price/${auctionId}`,
        {
          method: "PUT",
          headers: {
            Authorization: `Bearer ${token}`,
            "Content-Type": "application/json",
          },
          body: JSON.stringify({
            auction: updatedAuctionDetails,
            item: itemDetails,
          }),
        }
      );

      if (!response.ok) {
        const errorResponse = await response.json();
        throw new Error(
          errorResponse.message || "Failed to reduce auction price"
        );
      }

      const updatedAuctionData = await response.json();
      setAuctionData(updatedAuctionData);
      console.log("updatedAuctionDetails: ", updatedAuctionDetails);
      alert("Price reduced successfully!");
      setNewPrice("");
    } catch (error) {
      console.error("Error reducing auction price:", error.message);
      setError(error.message);
    }
  };

  const handleEditAuction = () => {
    navigate(`/edit-auction/${auctionId}`);
  };

  const renderError = () => {
    if (!error) return null;
    return <div className="text-red-600">{error}</div>;
  };

  if (!auctionData) return <div>Loading...</div>;

  return (
    <div className="max-w-4xl mx-auto bg-white p-8 mt-10 rounded-lg shadow">
      {renderError()}

      <div className="text-left mb-6">
        <h2 className="text-3xl font-bold text-gray-800 mb-2">
          {auctionData.item.name}
        </h2>
        <p className="text-gray-800">
          <strong>Description:</strong> {auctionData.item.description}
        </p>
        <p className="text-gray-800">
          <strong>Condition: </strong>
          {auctionData.item.itemCondition.toLowerCase()}
        </p>
        <p className="text-gray-800">
          <strong>MSRP: </strong>${auctionData.item.msrp}
        </p>
      </div>

      <div className="mb-6">
        <div className="flex justify-between items-center">
          <div>
            <span className="text-gray-800 font-semibold">
              {auctionData.auctionType === "DUTCH"
                ? "Purchase Price"
                : "Current Bid"}
              :
            </span>
            <span className="ml-2 text-lg text-lime-600">
              $
              {auctionData.auctionType === "FORWARD"
                ? auctionData.currentHighestBid || auctionData.startingPrice
                : auctionData.purchasePrice}
            </span>
          </div>
          {auctionData.auctionType === "FORWARD" && (
            <div>
              <span className="text-gray-800 font-semibold">Time Left:</span>
              <span className="ml-2 text-lg text-red-500">{timeLeft}</span>
            </div>
          )}
        </div>
        <div className="flex justify-between items-center">
          <div>
            <span className="text-gray-800 font-semibold">
              {auctionData.auctionType === "DUTCH"
                ? "Purchaser"
                : "Highest Bidder"}
              :
            </span>
            <span className="text-gray-800 font-semibold ml-2 text-lg">
              {auctionData.highestBidder}
            </span>
          </div>
          {auctionData.auctionType === "DUTCH" &&
            auctionData.highestBidder !== null && (
              <div>
                <span className="ml-2 text-lg text-red-500">
                  Auction has ended!
                </span>
              </div>
            )}
        </div>
      </div>

      {isOwner && auctionData?.auctionType === "DUTCH" && (
        <div className="mt-6">
          <h3 className="text-xl font-semibold">Reduce Item Price</h3>
          <form onSubmit={handlePriceReductionSubmit} className="mt-4">
            <div className="mb-4">
              <label
                htmlFor="newPrice"
                className="block text-sm font-medium text-gray-700"
              >
                New Price ($)
              </label>
              <input
                type="number"
                id="newPrice"
                value={newPrice}
                onChange={(e) => setNewPrice(e.target.value)}
                className="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-lime-500 focus:ring-lime-500 sm:text-sm"
                placeholder="Enter new price"
                min="0.01"
                step="0.01"
              />
            </div>
            <button
              type="submit"
              className="inline-flex justify-center py-2 px-4 border border-transparent shadow-sm text-sm font-medium rounded-md text-white bg-lime-600 hover:bg-lime-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-lime-500"
            >
              Reduce Price
            </button>
          </form>
        </div>
      )}

      {!isOwner && auctionData.auctionType === "DUTCH" && (
        <form
          onSubmit={handlePurchaseSubmit}
          className="flex flex-col items-center mt-4"
        >
          <p className="text-lg mb-2">
            Purchase at{" "}
            <span className="font-semibold">${auctionData.purchasePrice}</span>
          </p>
          <div className="text-center">
            <button
              type="submit"
              className="mt-4 px-6 py-2 border border-transparent text-sm font-medium rounded-md shadow-sm text-white bg-lime-600 hover:bg-lime-700 focus:outline-none"
            >
              Buy Now
            </button>
          </div>
        </form>
      )}

      {!isOwner && auctionData.auctionType === "FORWARD" && (
        <form onSubmit={handleBidSubmit} className="flex flex-col items-left">
          <label
            htmlFor="bid"
            className="block text-sm font-medium text-gray-700"
          >
            Your Bid ($)
          </label>
          <div className="mt-1 relative rounded-md w-full">
            <input
              type="number"
              id="bid"
              className="border-solid border-2 border-lime-500 focus:ring-lime-500 focus:border-lime-500 block w-full pl-7 pr-12 sm:text-sm rounded-md"
              placeholder="Enter your bid"
              value={bid}
              onChange={(e) => setBid(e.target.value)}
              min={
                auctionData.currentHighestBid
                  ? auctionData.currentHighestBid + 1
                  : 1
              }
            />
          </div>
          <button
            type="submit"
            className="mt-4 px-4 py-2 border border-transparent text-sm font-medium rounded-md shadow-sm text-white bg-lime-600 hover:bg-lime-700 focus:outline-none"
          >
            Submit Bid
          </button>
        </form>
      )}

      {isOwner && (
        <div className="flex justify-left mt-4">
          <button
            onClick={handleEditAuction}
            className={`py-2 px-4 rounded ${
              new Date(auctionData.startTime) <= new Date()
                ? "bg-gray-400 cursor-not-allowed"
                : "bg-blue-500 hover:bg-blue-700 text-white"
            }`}
            disabled={new Date(auctionData.startTime) <= new Date()}
          >
            Edit Auction
          </button>
        </div>
      )}
    </div>
  );
}

export default AuctionPage;
