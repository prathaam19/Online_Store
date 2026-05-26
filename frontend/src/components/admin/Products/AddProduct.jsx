import React, { useState } from "react";
import toast from "react-hot-toast";
import { getAxiosInstance } from "../../../utility/axiosApiConfig";
import { useNavigate } from "react-router-dom";

function AddProduct() {
  const axiosInstance = getAxiosInstance();
  const navigate = useNavigate();
  const [name, setName] = useState("");
  const [price, setPrice] = useState("");
  const [quantity, setQuantity] = useState("");
  const [categoryId, setCategoryId] = useState("");

  const addProductHandler = async () => {
    if (!name.trim()) {
      toast.error("Product name is required");
      return;
    }

    if (!price || Number(price) <= 0) {
      toast.error("Price must be a positive number");
      return;
    }

    if (!quantity || Number(quantity) <= 0) {
      toast.error("Quantity must be a positive number");
      return;
    }

    if (!categoryId) {
      toast.error("Category is required");
      return;
    }

    const productData = {
      name: name.trim(),
      price: Number(price),
      quantity: Number(quantity),
      categoryId: Number(categoryId),
    };

    await axiosInstance
      .post("http://localhost:8081/api/products/add", productData)
      .then((res) => {
        setName("");
        setPrice("");
        setQuantity("");
        setCategoryId("");
        toast.success("Product Added Successfully");
        navigate("/admin/products");
      })
      .catch((err) => {
        console.log(err);
        toast.error("Product Not Added");
      });
  };

  return (
    <div className="max-w-2xl mx-auto px-4 py-8">
      <h2 className="text-2xl font-bold text-gray-800 mb-6">Add New Product</h2>
      <div className="bg-white shadow-lg rounded-lg p-6">
        <form onSubmit={(e) => { e.preventDefault(); addProductHandler(); }}>
          <div className="space-y-6">
            <div>
              <label htmlFor="name" className="block text-sm font-medium text-gray-700 mb-2">
                Product Name
              </label>
              <input
                type="text"
                id="name"
                className="w-full px-4 py-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-indigo-500"
                placeholder="Enter product name"
                value={name}
                onChange={(e) => setName(e.target.value)}
              />
            </div>

            <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
              <div>
                <label htmlFor="price" className="block text-sm font-medium text-gray-700 mb-2">
                  Price (₹)
                </label>
                <input
                  type="number"
                  id="price"
                  className="w-full px-4 py-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-indigo-500"
                  placeholder="Enter price"
                  value={price}
                  onChange={(e) => setPrice(e.target.value)}
                  min="0"
                  step="0.01"
                />
              </div>

              <div>
                <label htmlFor="quantity" className="block text-sm font-medium text-gray-700 mb-2">
                  Quantity
                </label>
                <input
                  type="number"
                  id="quantity"
                  className="w-full px-4 py-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-indigo-500"
                  placeholder="Enter quantity"
                  value={quantity}
                  onChange={(e) => setQuantity(e.target.value)}
                  min="1"
                />
              </div>
            </div>

            <div>
              <label htmlFor="categoryId" className="block text-sm font-medium text-gray-700 mb-2">
                Category
              </label>
              <select
                id="categoryId"
                className="w-full px-4 py-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-orange-500"
                value={categoryId}
                onChange={(e) => setCategoryId(e.target.value)}
              >
                <option value="">Select a category</option>
                <option value="1">Main Course</option>
                <option value="2">Beverages</option>
                <option value="3">Desserts</option>
                <option value="4">Snacks</option>
                <option value="5">Breakfast</option>
                <option value="6">Cold Drinks</option>
                <option value="7">Dairy Products</option>
                <option value="8">Bakery Products</option>
                <option value="9">Chips</option>
                <option value="10">Icecream</option>
              </select>
            </div>

            <div className="flex gap-4 pt-4">
              <button
                type="submit"
                className="flex-1 px-6 py-3 bg-orange-600 text-white rounded-lg hover:bg-orange-700 transition font-medium"
              >
                Add Product
              </button>
              <button
                type="button"
                onClick={() => {
                  setName("");
                  setPrice("");
                  setQuantity("");
                  setCategoryId("");
                }}
                className="px-6 py-3 bg-gray-200 text-gray-700 rounded-lg hover:bg-gray-300 transition font-medium"
              >
                Reset
              </button>
            </div>
          </div>
        </form>
      </div>
    </div>
  );
}

export default AddProduct;