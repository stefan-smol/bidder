import React, { useState } from "react";
import { useQuery } from "react-query";
import { refreshTokenIfNeeded } from "../utils/TokenService";
import AuctionItem from "../components/AuctionItem";

function SearchPage() {
  const [searchQuery, setSearchQuery] = useState("");

  const {
    data: results,
    refetch,
    isFetching,
  } = useQuery(
    "searchAuctions",
    async () => {
      console.log("performing search...");
      await refreshTokenIfNeeded();

      const token = localStorage.getItem("token");
      if (!token) throw new Error("No token found");

      if (!searchQuery) return [];
      const response = await fetch(
        `http://localhost:8080/api/v1/catalogue/auctions/search?keyword=${encodeURIComponent(
          searchQuery
        )}`,
        {
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${token}`,
          },
        }
      );
      if (!response.ok) throw new Error("Network response was not ok");
      return response.json();
    },
    {
      enabled: false,
    }
  );

  const handleSearch = (event) => {
    event.preventDefault();
    refetch();
  };

  return (
    <div className="min-h-screen bg-gray-50 py-12 px-4 sm:px-6 lg:px-8">
      <div className="mx-auto w-10/12">
        <div className="text-center mb-6">
          <h2 className="text-3xl font-extrabold text-gray-900">
            Search Auctions
          </h2>
        </div>
        <form className="mb-6" onSubmit={handleSearch}>
          <div className="flex items-center">
            <input
              type="text"
              className="flex-grow appearance-none rounded-l-md relative block w-full px-3 py-2 border border-gray-300 placeholder-gray-500 text-gray-900 focus:outline-none focus:ring-lime-500 focus:border-lime-500 focus:z-10 sm:text-sm"
              placeholder="Search..."
              value={searchQuery}
              onChange={(e) => setSearchQuery(e.target.value)}
            />
            <button
              type="submit"
              className="relative -ml-px inline-flex items-center px-4 py-2 border border-lime-600 text-sm font-medium rounded-r-md text-white bg-lime-600 hover:bg-lime-700 focus:outline-none focus:ring-1 focus:ring-lime-500 focus:border-lime-500"
            >
              Search
            </button>
          </div>
        </form>
        <div className="bg-white shadow rounded-md overflow-auto">
          <table className="min-w-full divide-y divide-gray-200">
            <thead className="bg-gray-50">
              <tr>
                <th
                  scope="col"
                  className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider"
                >
                  Item Name
                </th>
                <th
                  scope="col"
                  className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider"
                >
                  Starting Price
                </th>
                <th
                  scope="col"
                  className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider"
                >
                  Status
                </th>
                <th
                  scope="col"
                  className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider"
                >
                  Auction Type
                </th>
                <th
                  scope="col"
                  className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider"
                >
                  Time Left
                </th>
                <th
                  scope="col"
                  className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider"
                >
                  Action
                </th>
              </tr>
            </thead>
            <tbody className="bg-white divide-y divide-gray-200">
              {isFetching ? (
                <tr>
                  <td
                    colSpan="6"
                    className="px-6 py-4 text-center text-sm text-gray-500"
                  >
                    Loading...
                  </td>
                </tr>
              ) : results?.length > 0 ? (
                results.map((result, index) => (
                  <AuctionItem key={index} item={result} />
                ))
              ) : (
                <tr>
                  <td
                    colSpan="6"
                    className="px-6 py-4 text-center text-sm text-gray-500"
                  >
                    No results found.
                  </td>
                </tr>
              )}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  );
}

export default SearchPage;
