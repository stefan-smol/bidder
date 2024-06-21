import { jwtDecode } from "jwt-decode";

const refreshTokenIfNeeded = async () => {
  const accessToken = localStorage.getItem("token");
  const refreshToken = localStorage.getItem("refreshToken");

  if (accessToken && refreshToken) {
    console.log("token and refresh token exist...");

    const { exp } = jwtDecode(accessToken);

    if (exp < new Date().getTime() / 1000 + 60) {
      try {
        console.log("Refreshing token...");

        const response = await fetch(
          "http://localhost:8080/api/v1/auth/refreshToken",
          {
            method: "POST",
            headers: {
              "Content-Type": "application/json",
              Authorization: `Bearer ${refreshToken}`,
            },
          }
        );

        if (!response.ok) {
          throw new Error("Failed to refresh token");
        }

        if (response.headers.get("Content-Length") === "0") {
          throw new Error("Empty response body");
        }

        const { access_token: newAccessToken, refresh_token: newRefreshToken } =
          await response.json();

        console.log(
          "token and refresh tokens are: ",
          newAccessToken,
          newRefreshToken
        );

        localStorage.setItem("token", newAccessToken);
        if (newRefreshToken) {
          localStorage.setItem("refreshToken", newRefreshToken);
        }

        return true;
      } catch (error) {
        console.error("Error refreshing token:", error);
        return false;
      }
    }
    return true;
  }
};

export const getUsernameFromToken = (token) => {
  try {
    const decoded = jwtDecode(token);
    return decoded.sub;
  } catch (error) {
    console.error("Error decoding token:", error);
    return null;
  }
};

export const getRoleFromToken = (token) => {
  try {
    const decoded = jwtDecode(token);
    return decoded.role;
  } catch (error) {
    console.error("Error decoding token:", error);
    return null;
  }
};

export { refreshTokenIfNeeded };
