import React, { useEffect } from "react";
import { Link, useNavigate } from "react-router-dom";
import bidderLogo from "../assets/images/bidder_logo.png";
import {
  getUsernameFromToken,
  getRoleFromToken,
  refreshTokenIfNeeded,
} from "../utils/TokenService";

const NavBar = ({ isAuthenticated, setIsAuthenticated }) => {
  const navigate = useNavigate();

  let token = localStorage.getItem("token");
  const username = token ? getUsernameFromToken(token) : null;
  const role = token ? getRoleFromToken(token) : null;

  console.log("role: ", role);

  useEffect(() => {
    const checkAuth = async () => {
      const isTokenRefreshed = await refreshTokenIfNeeded();
      setIsAuthenticated(!!localStorage.getItem("token") && isTokenRefreshed);
    };

    checkAuth();
  }, [setIsAuthenticated]);

  const logout = () => {
    localStorage.removeItem("token");
    localStorage.removeItem("refreshToken");
    setIsAuthenticated(false);
    navigate("/login");
  };

  return (
    <nav className="bg-lime-600 text-white p-4">
      <div className="flex justify-between items-center">
        <div className="shrink-0">
          <Link to="/">
            <img src={bidderLogo} alt="Bidder Logo" className="h-12 md:h-16" />
          </Link>
        </div>
        <ul className="flex items-center space-x-4 text-sm md:text-base">
          {isAuthenticated && (
            <>
              <li>
                <Link to="/search" className="hover:text-lime-200">
                  Search
                </Link>
              </li>
              <li>
                <Link to="/create-auction" className="hover:text-lime-200">
                  Create Auction
                </Link>
              </li>
              <li>
                <Link to={`/user/${username}`} className="hover:text-lime-200">
                  Profile
                </Link>
              </li>
              {role === "ROLE_ADMIN" && (
                <li>
                  <Link to="/admin" className="text-black hover:text-lime-200">
                    Admin
                  </Link>
                </li>
              )}
              <li>
                <button onClick={logout} className="hover:text-lime-200">
                  Logout
                </button>
              </li>
            </>
          )}
        </ul>
      </div>
    </nav>
  );
};

export default NavBar;
