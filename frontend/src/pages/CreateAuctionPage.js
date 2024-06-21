import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import { refreshTokenIfNeeded } from "../utils/TokenService";

function CreateAuctionPage() {
  const [auctionDetails, setAuctionDetails] = useState({
    auctionType: "",
    startTime: "",
    expiryTime: "",
    startingPrice: 1.0,
    purchasePrice: 1.0,
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

  const [errors, setErrors] = useState({});

  const navigate = useNavigate();

  function toLocalDateTimeString(date) {
    const offset = date.getTimezoneOffset();
    const adjustedDate = new Date(date.getTime() - offset * 60 * 1000);
    return adjustedDate.toISOString().slice(0, 16);
  }

  const validateForm = () => {
    const newErrors = {};

    if (!auctionDetails.auctionType)
      newErrors.auctionType = "Auction type is required.";

    if (!auctionDetails.startTime)
      newErrors.startTime = "Start time is required.";

    if (auctionDetails.auctionType === "FORWARD" && !auctionDetails.expiryTime)
      newErrors.expiryTime = "Expiry time is required.";

    if (
      isNaN(auctionDetails.startingPrice) ||
      auctionDetails.startingPrice <= 0
    ) {
      newErrors.startingPrice = "Starting price must be greater than 0.";
    }

    if (
      auctionDetails.auctionType === "DUTCH" &&
      (isNaN(auctionDetails.purchasePrice) || auctionDetails.purchasePrice <= 0)
    ) {
      newErrors.purchasePrice = "Purchase price must be greater than 0.";
    }

    if (!itemDetails.name.trim()) newErrors.name = "Item name is required.";

    if (!itemDetails.description.trim())
      newErrors.description = "Description is required.";

    if (isNaN(itemDetails.msrp) || itemDetails.msrp <= 0) {
      newErrors.msrp = "MSRP must be greater than 0.";
    }

    if (!itemDetails.category) newErrors.category = "Category is required.";

    if (!itemDetails.itemCondition)
      newErrors.itemCondition = "Item condition is required.";

    if (
      isNaN(auctionDetails.shippingPrice) ||
      auctionDetails.shippingPrice < 0
    ) {
      newErrors.shippingPrice = "Shipping cost must not be negative.";
    }

    if (
      isNaN(auctionDetails.expeditedShippingAddon) ||
      auctionDetails.expeditedShippingAddon < 0
    ) {
      newErrors.expeditedShippingAddon =
        "Expedited shipping cost must not be negative.";
    }

    setErrors(newErrors);

    return Object.keys(newErrors).length === 0;
  };

  const handleSubmit = async (e) => {
    console.log("Starting to create auction...");
    e.preventDefault();

    console.log("Still creating auction...");

    if (!validateForm()) return;

    console.log("auction information entered correctly...");

    await refreshTokenIfNeeded();
    const token = localStorage.getItem("token");
    if (!token) {
      alert("You must be logged in to create an auction.");
      return;
    }

    console.log("Debugging -->", {
      auction: auctionDetails,
      item: itemDetails,
    });

    try {
      const response = await fetch(
        "http://localhost:8080/api/v1/catalogue/auctions",
        {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${token}`,
          },
          body: JSON.stringify({ auction: auctionDetails, item: itemDetails }),
        }
      );

      if (!response.ok) {
        throw new Error("Failed to create auction");
      }

      const data = await response.json();
      console.log("Auction created successfully:", data);
      alert("Auction created successfully!");
      navigate("/search");
    } catch (error) {
      console.error("Error creating auction:", error);
      alert("Error creating auction. Please try again.");
    }
  };

  return (
    <div className="max-w-4xl mx-auto p-5">
      <h2 className="text-2xl font-bold mb-6">Create Auction</h2>
      <form onSubmit={handleSubmit} className="space-y-4">
        <h3 className="text-xl font-semibold">Auction Details</h3>
        <div className="flex flex-col space-y-2">
          <label className="font-medium">Auction Type</label>
          <select
            className="border border-gray-300 p-2 rounded"
            value={auctionDetails.auctionType}
            onChange={(e) =>
              setAuctionDetails({
                ...auctionDetails,
                auctionType: e.target.value,
              })
            }
          >
            <option value="">Select Type</option>
            <option value="FORWARD">Forward</option>
            <option value="DUTCH">Dutch</option>
          </select>
          {errors.auctionType && (
            <p className="text-red-500">{errors.auctionType}</p>
          )}

          {auctionDetails.auctionType === "FORWARD" && (
            <div className="flex flex-col space-y-2">
              <label className="font-medium">Starting Price</label>
              <input
                type="number"
                className="border border-gray-300 p-2 rounded"
                placeholder="Starting Price"
                value={auctionDetails.startingPrice}
                onChange={(e) =>
                  setAuctionDetails({
                    ...auctionDetails,
                    currentHighestBid: e.target.value,
                    startingPrice: e.target.value,
                  })
                }
              />

              {errors.startingPrice && (
                <p className="text-red-500">{errors.startingPrice}</p>
              )}
            </div>
          )}

          {auctionDetails.auctionType === "DUTCH" && (
            <div className="flex flex-col space-y-2">
              <label className="font-medium">Purchase Price</label>
              <input
                type="number"
                className="border border-gray-300 p-2 rounded"
                placeholder="Purchase Price"
                value={auctionDetails.purchasePrice}
                onChange={(e) =>
                  setAuctionDetails({
                    ...auctionDetails,
                    purchasePrice: e.target.value,
                  })
                }
              />

              {errors.purchasePrice && (
                <p className="text-red-500">{errors.purchasePrice}</p>
              )}
            </div>
          )}

          <label className="font-medium">Start Time</label>
          <input
            type="datetime-local"
            className="border border-gray-300 p-2 rounded"
            value={auctionDetails.startTime}
            onChange={(e) =>
              setAuctionDetails({
                ...auctionDetails,
                startTime: toLocalDateTimeString(new Date(e.target.value)),
              })
            }
          />

          {errors.startTime && (
            <p className="text-red-500">{errors.startTime}</p>
          )}

          {auctionDetails.auctionType === "FORWARD" && (
            <>
              <label className="font-medium">Expiry Time</label>
              <input
                type="datetime-local"
                className="border border-gray-300 p-2 rounded"
                value={auctionDetails.expiryTime}
                onChange={(e) =>
                  setAuctionDetails({
                    ...auctionDetails,
                    expiryTime: toLocalDateTimeString(new Date(e.target.value)),
                  })
                }
              />

              {errors.expiryTime && (
                <p className="text-red-500">{errors.expiryTime}</p>
              )}
            </>
          )}

          <label className="font-medium">Shipping Cost</label>
          <input
            type="number"
            className="border border-gray-300 p-2 rounded"
            placeholder="Shipping Cost"
            value={auctionDetails.shippingPrice}
            onChange={(e) =>
              setAuctionDetails({
                ...auctionDetails,
                shippingPrice: parseFloat(e.target.value),
              })
            }
          />
          {errors.shippingPrice && (
            <p className="text-red-500">{errors.shippingPrice}</p>
          )}

          <label className="font-medium">Expedited Shipping Add-on Cost</label>
          <input
            type="number"
            className="border border-gray-300 p-2 rounded"
            placeholder="Expedited Shipping Add-on Cost"
            value={auctionDetails.expeditedShippingAddon}
            onChange={(e) =>
              setAuctionDetails({
                ...auctionDetails,
                expeditedShippingAddon: parseFloat(e.target.value),
              })
            }
          />
          {errors.expeditedShippingAddon && (
            <p className="text-red-500">{errors.expeditedShippingAddon}</p>
          )}
        </div>
        <h3 className="text-xl font-semibold">Item Details</h3>
        <div className="flex flex-col space-y-2">
          <label className="font-medium">Name</label>
          <input
            type="text"
            className="border border-gray-300 p-2 rounded"
            placeholder="Name"
            value={itemDetails.name}
            onChange={(e) =>
              setItemDetails({ ...itemDetails, name: e.target.value })
            }
          />

          {errors.name && <p className="text-red-500">{errors.name}</p>}

          <label className="font-medium">Description</label>
          <textarea
            className="border border-gray-300 p-2 rounded"
            placeholder="Description"
            value={itemDetails.description}
            onChange={(e) =>
              setItemDetails({ ...itemDetails, description: e.target.value })
            }
          />

          {errors.description && (
            <p className="text-red-500">{errors.description}</p>
          )}

          <label className="font-medium">MSRP</label>
          <input
            type="number"
            className="border border-gray-300 p-2 rounded"
            placeholder="MSRP"
            value={itemDetails.msrp}
            onChange={(e) =>
              setItemDetails({ ...itemDetails, msrp: e.target.value })
            }
          />

          {errors.msrp && <p className="text-red-500">{errors.msrp}</p>}

          <label className="font-medium">Category</label>
          <select
            className="border border-gray-300 p-2 rounded"
            value={itemDetails.category}
            onChange={(e) =>
              setItemDetails({ ...itemDetails, category: e.target.value })
            }
          >
            <option value="">Select Category</option>
            <option value="APPAREL">Apparel</option>
            <option value="JEWELRY">Jewelry</option>
            <option value="ELECTRONICS">Electronics</option>
            <option value="COLLECTABLES">Collectables</option>
            <option value="FURNITURE">Furniture</option>
            <option value="HEALTH">Health</option>
            <option value="COSMETICS">Cosmetics</option>
            <option value="TOOLS">Tools</option>
            <option value="AUTOMOTIVE">Automotive</option>
            <option value="OTHER">Other</option>
          </select>

          {errors.category && <p className="text-red-500">{errors.category}</p>}

          <label className="font-medium">Brand</label>
          <input
            type="text"
            className="border border-gray-300 p-2 rounded"
            placeholder="Brand"
            value={itemDetails.brand}
            onChange={(e) =>
              setItemDetails({ ...itemDetails, brand: e.target.value })
            }
          />

          <label className="font-medium">Item Condition</label>
          <select
            className="border border-gray-300 p-2 rounded"
            value={itemDetails.itemCondition}
            onChange={(e) =>
              setItemDetails({ ...itemDetails, itemCondition: e.target.value })
            }
          >
            <option value="">Select Condition</option>
            <option value="NEW">New</option>
            <option value="USED">Used</option>
            <option value="REFURBISHED">Refurbished</option>
          </select>

          {errors.itemCondition && (
            <p className="text-red-500">{errors.itemCondition}</p>
          )}
        </div>

        <button
          type="submit"
          className="bg-lime-600 text-white py-2 px-4 rounded hover:bg-lime-700"
        >
          Create Auction
        </button>
      </form>
    </div>
  );
}

export default CreateAuctionPage;
