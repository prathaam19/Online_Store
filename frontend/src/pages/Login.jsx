import React, { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { useDispatch } from "react-redux";
import axios from "axios";
import toast from "react-hot-toast";
import { FaEye, FaEyeSlash } from "react-icons/fa";
import { setRole, setToken, setUser } from "../features/auth/authSlice";
import InputField from "../components/InputField";
import PasswordField from "../components/PasswordField";
import { GoogleLogin } from "@react-oauth/google";

function Login() {
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const [passwordVisible, setPasswordVisible] = useState(false);
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [emailError, setEmailError] = useState("");
  const [passwordError, setPasswordError] = useState("");

  const validateEmail = (email) => {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return emailRegex.test(email);
  };

  const validatePassword = (password) => {
    const passwordRegex = /^(?=.*[0-9])(?=.*[!@#$%^&*])[A-Za-z\d!@#$%^&*]{8,}$/;
    return passwordRegex.test(password);
  };

  const inputChangeHandler = (id, value) => {
    if (id === "email") {
      setEmail(value);
      setEmailError(validateEmail(value) ? "" : "Invalid email format. Must contain '@' and a domain, and end with a valid domain like .com, .net, .org, etc.");
    } else if (id === "password") {
      setPassword(value);
      setPasswordError(
        validatePassword(value)
          ? ""
          : "Password must be at least 8 characters long, contain at least one number and one special character"
      );
    }
  };

  const loginHandler = async () => {
    if (!validateEmail(email)) {
      setEmailError("Invalid email format. Must contain '@' and a domain, and end with a valid domain like .com.");
      return;
    }
    if (!validatePassword(password)) {
      setPasswordError(
        "Password must be at least 8 characters long, contain at least one number and one special character"
      );
      return;
    }

    await axios
      .post("http://localhost:8081/api/auth/login", {
        username: email,
        password,
      })
      .then(async (res) => {
        const { token } = res.data;
        try {
          const [, payloadBase64] = token.split(".");
          const payloadJson = atob(payloadBase64);
          const payload = JSON.parse(payloadJson);
          localStorage.setItem("auth", payload.authorities);
          dispatch(setRole(payload.authorities));

          localStorage.setItem("user", email);
          dispatch(setUser(email));
          localStorage.setItem("token", token);
          dispatch(setToken(token));
          toast.success("Login Success");
          navigate("/");
        } catch (error) {
          console.error("Error decoding JWT:", error);
          toast.error("Invalid Username or Password");
        }
      })
      .catch((err) => {
        console.log(err);
        toast.error("Server Error");
      });
  };

  const handleGoogleLogin = async (credentialResponse) => {
    try {
      // Send the Google credential to your backend for verification
      await axios.post("http://localhost:8081/api/auth/google", {
        token: credentialResponse.credential,
      })
      .then((res) => {
        const { token } = res.data;
        localStorage.setItem("token", token);
        dispatch(setToken(token));
        localStorage.setItem("user", "google_user");
        dispatch(setUser("google_user"));
        localStorage.setItem("auth", "USER");
        dispatch(setRole("USER"));
        toast.success("Google Login Success");
        navigate("/");
      })
      .catch((err) => {
        console.log(err);
        toast.error("Google Login Failed");
      });
    } catch (error) {
      console.error("Google login error:", error);
      toast.error("Google Login Failed");
    }
  };

  return (
    <main className="w-full h-[80vh] md:h-[65vh] flex flex-col items-center justify-center px-4">
      <div className="max-w-sm w-full text-gray-600 space-y-5">
        <div className="text-center pb-8">
          <div className="mt-5">
            <h3 className="text-gray-800 text-3xl font-bold">Login</h3>
          </div>
        </div>
        <form onSubmit={(e) => e.preventDefault()} className="space-y-5">
          <InputField
            label="Email"
            type="email"
            value={email}
            onChange={(e) => inputChangeHandler("email", e.target.value)}
          />
          {emailError && <p className="text-red-600 text-sm">{emailError}</p>}
          <PasswordField
            label="Password"
            value={password}
            onChange={(e) => inputChangeHandler("password", e.target.value)}
            passwordVisible={passwordVisible}
            toggleVisibility={() => setPasswordVisible(!passwordVisible)}
          />
          {passwordError && (
            <p className="text-red-600 text-sm">{passwordError}</p>
          )}
          <div className="flex items-center justify-end text-sm">
            <Link
              to="/Forgot-Password"
              className="text-center text-red-600 hover:text-red-500"
            >
              Forgot password?
            </Link>
          </div>
          <button
            onClick={loginHandler}
            className="w-full px-4 py-2 text-white font-medium bg-red-600 hover:bg-red-500 active:bg-red-600 rounded-lg duration-150"
          >
            Login
          </button>
        </form>
        
        <div className="flex items-center my-4">
          <div className="flex-1 border-t border-gray-300"></div>
          <span className="px-4 text-gray-500 text-sm">OR</span>
          <div className="flex-1 border-t border-gray-300"></div>
        </div>

        <div className="flex justify-center">
          <GoogleLogin
            onSuccess={handleGoogleLogin}
            onError={() => toast.error("Google Login Failed")}
            useOneTap
            text="signin_with"
            shape="rectangular"
            theme="outline"
            size="large"
            width="100%"
          />
        </div>
        <p className="text-center">
          Don't have an account?{" "}
          <Link to="/signup" className="font-medium text-red-600 hover:text-red-500">
            Sign up
          </Link>
        </p>
      </div>
      <div>
      <Link to="/admin" className="font-medium text-red-600 hover:text-red-500">
            Seller login
      </Link>
      </div>
    </main>
  );
}

export default Login;