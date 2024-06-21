import React, { useEffect, useState } from "react";
import { Navigate, Outlet } from "react-router-dom";
import { jwtDecode } from "jwt-decode";
import { refreshTokenIfNeeded } from "../utils/TokenService";

const ProtectedRoute = () => {
  const [isLoading, setIsLoading] = useState(true);
  const [isAuthenticated, setIsAuthenticated] = useState(false);

  useEffect(() => {
    const checkAuth = async () => {
      await refreshTokenIfNeeded();

      const token = localStorage.getItem("token");
      if (!token) {
        setIsLoading(false);
        return;
      }

      try {
        const { exp: expiration } = jwtDecode(token);
        if (expiration < new Date().getTime() / 1000) {
          setIsAuthenticated(false);
        } else {
          setIsAuthenticated(true);
        }
      } catch (error) {
        setIsAuthenticated(false);
      }
      setIsLoading(false);
    };

    checkAuth();
  }, []);

  if (isLoading) {
    return <div>Loading...</div>;
  }

  return isAuthenticated ? <Outlet /> : <Navigate to="/login" replace />;
};

export default ProtectedRoute;
