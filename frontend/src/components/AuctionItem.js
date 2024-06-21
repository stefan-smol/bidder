import React from "react";
import { useCountdown } from "../hooks/useCountdown";
import { getUsernameFromToken } from "../utils/TokenService";

function AuctionItem({ item }) {
  const timeLeft = useCountdown(item.expiryTime);

  const timeLeftDisplay =
    item.auctionType === "DUTCH"
      ? "N/A"
      : Object.keys(timeLeft).length > 0
      ? `${timeLeft.days}d ${timeLeft.hours}h ${timeLeft.minutes}m ${timeLeft.seconds}s`
      : "Expired";

  const isAuctionExpired =
    item.status === "EXPIRED" && timeLeftDisplay === "Expired";

  const isAuctionRunning =
    item.status === "RUNNING" && timeLeftDisplay !== "Expired";

  const token = localStorage.getItem("token");
  const username = token ? getUsernameFromToken(token) : null;
  const isOwner = username === item.sellerUsername;

  return (
    <tr>
      <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
        {item.item.name}
      </td>
      <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
        $
        {item.auctionType === "DUTCH" ? item.purchasePrice : item.startingPrice}
      </td>
      <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
        {item.status}
      </td>
      <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
        {item.auctionType}
      </td>
      <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
        {timeLeftDisplay}
      </td>
      <td className="px-6 py-4 whitespace-nowrap text-sm font-medium">
        {isOwner ? (
          !isAuctionExpired ? (
            <button
              onClick={() => (window.location.href = `/auction/${item.id}`)}
              className="text-lime-600 hover:text-lime-900"
            >
              Edit Auction
            </button>
          ) : (
            <span className="text-gray-400 cursor-not-allowed">
              Edit Auction
            </span>
          )
        ) : isAuctionRunning ? (
          <button
            onClick={() => (window.location.href = `/auction/${item.id}`)}
            className="text-lime-600 hover:text-lime-900"
          >
            Bid
          </button>
        ) : (
          <span className="text-gray-400 cursor-not-allowed">Bid</span>
        )}
      </td>
    </tr>
  );
}

export default AuctionItem;
