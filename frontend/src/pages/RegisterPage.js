import React, { useState } from "react";
import { useMutation } from "react-query";
import { Link, useNavigate } from "react-router-dom";

function RegisterPage() {
  const [userData, setUserData] = useState({
    username: "",
    email: "",
    password: "",
    firstName: "",
    lastName: "",
    role: "USER",
    country: "",
    city: "",
    postalCode: "",
    streetAddress: "",
    streetNumber: "",
  });
  const navigate = useNavigate();

  const {
    mutate: register,
    isError,
    error,
  } = useMutation(
    async (newUserData) => {
      const response = await fetch(
        "http://localhost:8080/api/v1/auth/register",
        {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify(newUserData),
        }
      );

      if (!response.ok) {
        throw new Error("Registration failed");
      }

      return response.json();
    },
    {
      onSuccess: () => {
        navigate("/");
      },
    }
  );

  const handleChange = (event) => {
    const { name, value } = event.target;
    setUserData({ ...userData, [name]: value });
  };

  const handleRegister = async (event) => {
    event.preventDefault();
    register(userData);
  };

  return (
    <div className="min-h-screen flex flex-col items-center justify-center bg-gray-50 py-12 px-4 sm:px-6 lg:px-8">
      <div className="max-w-md w-full space-y-8">
        <div>
          <h2 className="mt-6 text-center text-3xl font-extrabold text-gray-900">
            Create your account
          </h2>
          {isError && <p className="text-red-500">{error.message}</p>}
        </div>
        <form className="mt-8 space-y-6" onSubmit={handleRegister}>
          {Object.keys(userData).map((key) => {
            if (key !== "role") {
              return (
                <div key={key}>
                  <label htmlFor={key} className="sr-only">
                    {key.charAt(0).toUpperCase() + key.slice(1)}:
                  </label>
                  <input
                    id={key}
                    name={key}
                    type={key === "password" ? "password" : "text"}
                    required
                    className="appearance-none relative block w-full my-2 px-3 py-2 border border-gray-300 placeholder-gray-500 text-gray-900 rounded-md focus:outline-none focus:ring-limne-500 focus:border-lime-500 focus:z-10 sm:text-sm"
                    placeholder={key.charAt(0).toUpperCase() + key.slice(1)}
                    value={userData[key]}
                    onChange={handleChange}
                  />
                </div>
              );
            }
            return null;
          })}

          <div className="relative my-2">
            <select
              id="role"
              name="role"
              required
              className="mt-1 block w-full pl-3 pr-10 py-2 text-base border border-gray-300 placeholder-gray-500 text-gray-900 rounded-md focus:outline-none focus:ring-limne-500 focus:border-lime-500 focus:z-10 sm:text-sm"
              value={userData.role}
              onChange={handleChange}
            >
              <option value="USER">User</option>
              <option value="ADMIN">Admin</option>
            </select>
          </div>

          <button
            type="submit"
            className="group relative w-full flex justify-center py-2 px-4 border border-transparent text-sm font-medium rounded-md text-white bg-lime-500 hover:bg-lime-600 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500"
          >
            Register
          </button>
        </form>
        <div className="mt-4">
          <Link
            to="/login"
            className="font-medium text-lime-600 hover:text-lime-500"
          >
            Already have an account? Sign in
          </Link>
        </div>
      </div>
    </div>
  );
}

export default RegisterPage;
