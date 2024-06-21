import React, { useState, useEffect } from "react";
import { useParams, useNavigate } from "react-router-dom";

function EditAuctionPage() {
  const [auctionDetails, setAuctionDetails] = useState({
    auctionType: "",
    startTime: "",
    expiryTime: "",
    startingPrice: 0,
    purchasePrice: 0,
    shippingPrice: 0,
    expeditedShippingAddon: 0,
    status: "SCHEDULED",
  });
  const [itemDetails, setItemDetails] = useState({
    name: "",
    description: "",
    msrp: "",
    category: "",
    brand: "",
    itemCondition: "",
  });
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState("");

  const { auctionId } = useParams();
  const navigate = useNavigate();

  useEffect(() => {
    const fetchAuctionData = async () => {
      setIsLoading(true);
      try {
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
        setAuctionDetails({
          ...auctionDetails,
          auctionType: data.auctionType,
          startTime: data.startTime,
          expiryTime: data.expiryTime,
          startingPrice: data.startingPrice,
        });
        setItemDetails({
          ...itemDetails,
          name: data.item.name,
          description: data.item.description,
          msrp: data.item.msrp,
          category: data.item.category,
          brand: data.item.brand,
          itemCondition: data.item.itemCondition,
        });
      } catch (error) {
        setError(error.message);
      } finally {
        setIsLoading(false);
      }
    };

    fetchAuctionData();
  }, [auctionId]);

  const handleFormSubmit = async (e) => {
    e.preventDefault();

    try {
      const token = localStorage.getItem("token");
      if (!token) {
        throw new Error("Authentication required. Please log in.");
      }

      const response = await fetch(
        `http://localhost:8080/api/v1/catalogue/auctions/${auctionId}`,
        {
          method: "PUT",
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${token}`,
          },
          body: JSON.stringify({ auction: auctionDetails, item: itemDetails }),
        }
      );

      if (!response.ok) {
        const errorData = await response.json();
        throw new Error(errorData.message || "Failed to update auction");
      }

      alert("Auction updated successfully!");
      navigate(`/auction/${auctionId}`);
    } catch (error) {
      console.error("Error updating auction:", error);
      alert(error.message || "An error occurred while updating the auction.");
    }
  };

  const handleDeleteAuction = async () => {
    const confirmDelete = window.confirm(
      "Are you sure you want to delete this auction?"
    );
    if (!confirmDelete) return;

    const token = localStorage.getItem("token");
    if (!token) {
      alert("You must be logged in to delete an auction.");
      return;
    }

    try {
      const response = await fetch(
        `http://localhost:8080/api/v1/catalogue/auctions/${auctionId}`,
        {
          method: "DELETE",
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );

      if (!response.ok) {
        throw new Error("Failed to delete auction");
      }

      alert("Auction deleted successfully!");
      navigate("/");
    } catch (error) {
      console.error("Error deleting auction:", error);
      alert(error.message || "An error occurred while deleting the auction.");
    }
  };

  if (isLoading) return <div>Loading...</div>;
  if (error) return <div>Error: {error}</div>;

  return (
    <div className="max-w-4xl mx-auto p-5">
      <h2 className="text-2xl font-bold mb-6">Edit Auction</h2>
      <form onSubmit={handleFormSubmit} className="space-y-4">
        <div className="space-y-2">
          <h3 className="text-xl font-semibold">Auction Details</h3>
          <div className="flex flex-col">
            <label className="font-medium">Auction Type</label>
            <select
              value={auctionDetails.auctionType}
              onChange={(e) =>
                setAuctionDetails({
                  ...auctionDetails,
                  auctionType: e.target.value,
                })
              }
              className="border border-gray-300 p-2 rounded"
            >
              <option value="">Select Type</option>
              <option value="FORWARD">Forward</option>
              <option value="DUTCH">Dutch</option>
            </select>
          </div>
          <div className="flex flex-col">
            <label className="font-medium">Start Time</label>
            <input
              type="datetime-local"
              value={auctionDetails.startTime}
              onChange={(e) =>
                setAuctionDetails({
                  ...auctionDetails,
                  startTime: e.target.value,
                })
              }
              className="border border-gray-300 p-2 rounded"
            />
          </div>
          <div className="flex flex-col">
            <label className="font-medium">Expiry Time</label>
            <input
              type="datetime-local"
              value={auctionDetails.expiryTime}
              onChange={(e) =>
                setAuctionDetails({
                  ...auctionDetails,
                  expiryTime: e.target.value,
                })
              }
              className="border border-gray-300 p-2 rounded"
            />
          </div>
          <div className="flex flex-col">
            <label className="font-medium">Starting Price</label>
            <input
              type="number"
              value={auctionDetails.startingPrice}
              onChange={(e) =>
                setAuctionDetails({
                  ...auctionDetails,
                  currentHighestBid: e.target.value,
                  startingPrice: e.target.value,
                })
              }
              className="border border-gray-300 p-2 rounded"
            />
          </div>
        </div>

        <div className="space-y-2">
          <h3 className="text-xl font-semibold">Item Details</h3>
          <div className="flex flex-col">
            <label className="font-medium">Name</label>
            <input
              type="text"
              value={itemDetails.name}
              onChange={(e) =>
                setItemDetails({ ...itemDetails, name: e.target.value })
              }
              className="border border-gray-300 p-2 rounded"
            />
          </div>
          <div className="flex flex-col">
            <label className="font-medium">Description</label>
            <textarea
              value={itemDetails.description}
              onChange={(e) =>
                setItemDetails({ ...itemDetails, description: e.target.value })
              }
              className="border border-gray-300 p-2 rounded"
            />
          </div>
          <div className="flex flex-col">
            <label className="font-medium">MSRP</label>
            <input
              type="number"
              value={itemDetails.msrp}
              onChange={(e) =>
                setItemDetails({ ...itemDetails, msrp: e.target.value })
              }
              className="border border-gray-300 p-2 rounded"
            />
          </div>
          <div className="flex flex-col">
            <label className="font-medium">Category</label>
            <select
              value={itemDetails.category}
              onChange={(e) =>
                setItemDetails({ ...itemDetails, category: e.target.value })
              }
              className="border border-gray-300 p-2 rounded"
            >
              <option value="">Select Category</option>
              {/* Add more categories as necessary */}
            </select>
          </div>
          <div className="flex flex-col">
            <label className="font-medium">Brand</label>
            <input
              type="text"
              value={itemDetails.brand}
              onChange={(e) =>
                setItemDetails({ ...itemDetails, brand: e.target.value })
              }
              className="border border-gray-300 p-2 rounded"
            />
          </div>
          <div className="flex flex-col">
            <label className="font-medium">Condition</label>
            <select
              value={itemDetails.itemCondition}
              onChange={(e) =>
                setItemDetails({
                  ...itemDetails,
                  itemCondition: e.target.value,
                })
              }
              className="border border-gray-300 p-2 rounded"
            >
              <option value="">Select Condition</option>
              <option value="NEW">New</option>
              <option value="USED">Used</option>
              <option value="REFURBISHED">Refurbished</option>
            </select>
          </div>
        </div>
        <div className="flex items-center gap-4">
          <button
            type="submit"
            className="flex-1 bg-blue-500 text-white py-2 px-4 rounded hover:bg-blue-600 transition-colors duration-200"
          >
            Update Auction
          </button>
          <button
            type="button"
            onClick={handleDeleteAuction}
            className="flex-1 bg-red-500 text-white py-2 px-4 rounded hover:bg-red-600 transition-colors duration-200"
          >
            Delete Auction
          </button>
        </div>
      </form>
    </div>
  );
}

export default EditAuctionPage;
