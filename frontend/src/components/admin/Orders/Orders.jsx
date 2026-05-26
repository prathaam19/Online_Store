import React from "react";

function Orders() {
  return (
    <div className="max-w-screen-xl mx-auto px-4 md:px-0 py-8">
      <h2 className="text-gray-800 text-xl font-bold sm:text-2xl mb-4">All Orders</h2>
      <div className="bg-white shadow rounded-lg p-6">
        <p className="text-gray-600 mb-4">
          Admin order management is not currently available in the backend.
        </p>
        <p className="text-gray-500 text-sm">
          Features like viewing all orders and updating order status require backend support.
        </p>
      </div>
    </div>
  );
}

export default Orders;
