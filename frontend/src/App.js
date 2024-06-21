import React, { useState } from "react";
import { BrowserRouter as Router, Route, Routes } from "react-router-dom";
import { QueryClient, QueryClientProvider } from "react-query";
import LoginPage from "./pages/LoginPage";
import RegisterPage from "./pages/RegisterPage";
import ForgotPasswordPage from "./pages/ForgotPasswordPage";
import SearchPage from "./pages/SearchPage";
import UserProfilePage from "./pages/UserProfilePage";
import AuctionPage from "./pages/AuctionPage";
import CreateAuctionPage from "./pages/CreateAuctionPage";
import EditAuctionPage from "./pages/EditAuctionPage";
import PayNowPage from "./pages/PayNowPage";
import PurchasePage from "./pages/PurchasePage";
import ReceiptPage from "./pages/ReceiptPage";
import ProtectedRoute from "./components/ProtectedRoute";
import NavBar from "./components/NavBar";
import AdminPage from "./pages/AdminPage";

const queryClient = new QueryClient();

function App() {
  const [isAuthenticated, setIsAuthenticated] = useState(false);

  return (
    <Router>
      <QueryClientProvider client={queryClient}>
        <div className="App">
          <NavBar
            isAuthenticated={isAuthenticated}
            setIsAuthenticated={setIsAuthenticated}
          />
          <Routes>
            <Route
              path="/login"
              element={<LoginPage setIsAuthenticated={setIsAuthenticated} />}
            />
            <Route path="/register" element={<RegisterPage />} />
            <Route path="/forgot-password" element={<ForgotPasswordPage />} />

            <Route element={<ProtectedRoute />}>
              <Route path="/" element={<SearchPage />} />
              <Route path="/search" element={<SearchPage />} />
              <Route
                path="/user/:username"
                element={
                  <UserProfilePage setIsAuthenticated={setIsAuthenticated} />
                }
              />
              <Route path="/auction/:auctionId" element={<AuctionPage />} />
              <Route path="/create-auction" element={<CreateAuctionPage />} />
              <Route
                path="/edit-auction/:auctionId"
                element={<EditAuctionPage />}
              />
              <Route path="/pay-now/:auctionId" element={<PayNowPage />} />
              <Route path="/purchase/:auctionId" element={<PurchasePage />} />
              <Route
                path="/payment-success/:auctionId"
                element={<ReceiptPage />}
              />
              <Route path="/admin" element={<AdminPage />} />
            </Route>
          </Routes>
        </div>
      </QueryClientProvider>
    </Router>
  );
}

export default App;
