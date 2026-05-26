import React from "react";
import { useSelector } from "react-redux";

function UserProfile() {
  const { token } = useSelector((state) => state.auth);

  if (!token) {
    return (
      <div className="max-w-screen-xl mx-auto my-8 px-4 md:px-0 text-center">
        <h2 className="text-gray-800 text-xl font-bold sm:text-2xl mb-4">Profile</h2>
        <p className="text-gray-600">Please login to view your profile</p>
      </div>
    );
  }

  return (
    <div className="max-w-screen-xl mx-auto my-8 px-4 md:px-0">
      <h2 className="text-gray-800 text-xl font-bold sm:text-2xl mb-4">Profile</h2>
      <div className="bg-white shadow rounded-lg p-6">
        <p className="text-gray-600 mb-4">
          User profile management is not currently available in the backend.
        </p>
        <p className="text-gray-500 text-sm">
          Features like profile updates and password changes require backend support.
        </p>
      </div>
    </div>
  );
}

export default UserProfile;
