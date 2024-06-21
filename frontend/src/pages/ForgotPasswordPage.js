import React, { useState } from "react";
import { Link, useNavigate } from "react-router-dom";

function ForgotPasswordPage() {
  const [username, setUsername] = useState("");
  const [newPassword, setNewPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");
  const [message, setMessage] = useState("");
  const navigate = useNavigate();

  const handleSubmit = async (event) => {
    event.preventDefault();

    if (newPassword !== confirmPassword) {
      setMessage("Passwords do not match.");
      return;
    }

    try {
      const response = await fetch(
        "http://localhost:8080/api/v1/auth/resetPassword",
        {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify({ username, password: newPassword }),
        }
      );

      if (response.ok) {
        setMessage("Your password has been successfully reset.");
        navigate("/login");
      } else {
        setMessage("An error occurred. Please try again.");
      }
    } catch (error) {
      console.error("Password reset error:", error);
      setMessage("An error occurred. Please try again.");
    }
  };

  return (
    <div className="min-h-screen flex flex-col items-center justify-center bg-gray-50 py-12 px-4 sm:px-6 lg:px-8">
      <div className="max-w-md w-full space-y-8">
        <div>
          <h2 className="mt-6 text-center text-3xl font-extrabold text-gray-900">
            Reset your password
          </h2>
        </div>
        <form className="mt-8 space-y-6" onSubmit={handleSubmit}>
          <div className="rounded-md shadow-sm -space-y-px">
            <div>
              <label htmlFor="username" className="sr-only">
                Username
              </label>
              <input
                id="username"
                name="username"
                type="text"
                required
                className="appearance-none rounded-none relative block w-full px-3 py-2 border border-gray-300 placeholder-gray-500 text-gray-900 rounded-t-md focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 focus:z-10 sm:text-sm"
                placeholder="Username"
                value={username}
                onChange={(e) => setUsername(e.target.value)}
              />
            </div>
            <div>
              <label htmlFor="new-password" className="sr-only">
                New Password
              </label>
              <input
                id="new-password"
                name="new-password"
                type="password"
                required
                className="appearance-none rounded-none relative block w-full px-3 py-2 border border-gray-300 placeholder-gray-500 text-gray-900 rounded-b-md focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 focus:z-10 sm:text-sm"
                placeholder="New password"
                value={newPassword}
                onChange={(e) => setNewPassword(e.target.value)}
              />
            </div>
            <div>
              <label htmlFor="confirm-password" className="sr-only">
                Confirm New Password
              </label>
              <input
                id="confirm-password"
                name="confirm-password"
                type="password"
                required
                className="appearance-none rounded-none relative block w-full px-3 py-2 border border-gray-300 placeholder-gray-500 text-gray-900 rounded-b-md focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 focus:z-10 sm:text-sm"
                placeholder="Confirm new password"
                value={confirmPassword}
                onChange={(e) => setConfirmPassword(e.target.value)}
              />
            </div>
          </div>
          <button
            type="submit"
            className="group relative w-full flex justify-center py-2 px-4 border border-transparent text-sm font-medium rounded-md text-white bg-lime-500 hover:bg-lime-600 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500"
          >
            Reset Password
          </button>
        </form>
        <div className="mt-4">
          <Link
            to="/login"
            className="font-medium text-lime-600 hover:text-lime-500"
          >
            Remember your password? Sign in
          </Link>
        </div>
        {message && <p className="text-center text-red-500">{message}</p>}
      </div>
    </div>
  );
}

export default ForgotPasswordPage;
