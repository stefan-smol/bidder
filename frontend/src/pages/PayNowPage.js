import React, { useState, useEffect } from "react";
import { useParams, useNavigate } from "react-router-dom";
import {
  getUsernameFromToken,
  refreshTokenIfNeeded,
} from "../utils/TokenService";

function PayNowPage() {
  const [auctionDetails, setAuctionDetails] = useState(null);
  const [isWinner, setIsWinner] = useState(false);
  const [expeditedShipping, setExpeditedShipping] = useState(false);
  const [error, setError] = useState("");

  const { auctionId } = useParams();
  const navigate = useNavigate();

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

        const username = getUsernameFromToken(localStorage.getItem("token"));
        const isWinner = username === data.highestBidder;

        setAuctionDetails({
          ...data,
        });

        setIsWinner(isWinner);
        console.log("Auction data:", auctionDetails);
      } catch (error) {
        console.error("Error fetching auction data:", error);
      }
    };

    fetchAuctionData();
  }, [auctionId]);

  const handlePayNow = () => {
    const totalAmount = expeditedShipping
      ? auctionDetails.currentHighestBid +
        auctionDetails.shippingPrice +
        auctionDetails.expeditedShippingAddon
      : auctionDetails.currentHighestBid + auctionDetails.shippingPrice;

    navigate(`/purchase/${auctionId}`, {
      state: {
        auctionId,
        totalAmount,
        expeditedShipping,
        winnerUsername: auctionDetails.highestBidder,
      },
    });
  };

  if (!auctionDetails) return <div>Loading...</div>;

  if (error) return <div>Error: {error}</div>;

  return (
    <div className="max-w-2xl mx-auto p-6 bg-white shadow-md rounded-lg">
      <h1 className="text-2xl font-bold text-center text-gray-800 mb-6">
        Auction has ended!
      </h1>
      {auctionDetails && (
        <div className="auction-details space-y-4">
          <p>
            <span className="font-semibold">Item name:</span>{" "}
            {auctionDetails.item.name}
          </p>
          <p>
            <span className="font-semibold">Item Description:</span>{" "}
            {auctionDetails.item.description}
          </p>
          <p>
            <span className="font-semibold">Shipping cost:</span> $
            {auctionDetails.shippingPrice}
          </p>
          {expeditedShipping && (
            <p>
              <span className="font-semibold">Expedited Shipping:</span> $
              {auctionDetails.expeditedShippingAddon}
            </p>
          )}
          <p>
            <span className="font-semibold">Winning Price:</span> $
            {auctionDetails.currentHighestBid}
          </p>
          <p>
            <span className="font-semibold">Highest Bidder:</span>{" "}
            {auctionDetails.highestBidder}
          </p>
        </div>
      )}
      {isWinner ? (
        <div className="mt-6">
          <label className="flex items-center space-x-2">
            <input
              type="checkbox"
              className="form-checkbox h-5 w-5 text-lime-600"
              checked={expeditedShipping}
              onChange={(e) => setExpeditedShipping(e.target.checked)}
            />
            <span>
              Add Expedited Shipping (${auctionDetails.expeditedShippingAddon})
            </span>
          </label>
          <button
            onClick={handlePayNow}
            className="mt-6 w-full bg-lime-600 text-white py-2 px-4 rounded hover:bg-lime-700 focus:outline-none focus:ring-2 focus:ring-lime-500 focus:ring-opacity-50"
          >
            Pay Now
          </button>
        </div>
      ) : (
        <p className="text-center text-gray-600 mt-6">
          You did not win this auction.
        </p>
      )}
    </div>
  );
}

export default PayNowPage;
