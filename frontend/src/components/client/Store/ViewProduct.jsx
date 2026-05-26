import React, { useEffect, useState } from "react";
import { TiArrowBack } from "react-icons/ti";
import { useLocation, useNavigate } from "react-router-dom";
import { getAxiosInstance } from "../../../utility/axiosApiConfig";
import { useSelector } from "react-redux";
import toast from "react-hot-toast";
import axios from "axios";

function ViewProduct() {
  const { token } = useSelector((state) => state.auth);
  const { pathname } = useLocation();
  const navigate = useNavigate();
  const axiosInstance = getAxiosInstance();
  const [product, setProduct] = useState(null);
  const [quantity, setQuantity] = useState(1);
  const [loading, setLoading] = useState(true);

  const increment = () => {
    setQuantity((prev) => Math.min(prev + 1, product?.quantity || 1));
  };

  const decrement = () => {
    setQuantity((prev) => Math.max(prev - 1, 1));
  };

  const addToCartHandler = async (productId) => {
    if (token) {
      await axiosInstance
        .post(`http://localhost:8081/api/cart/add/${productId}`, {})
        .then((res) => {
          toast.success("Added to Cart");
          navigate("/cart");
        })
        .catch((err) => {
          console.log(err);
          toast.error("Failed to add to cart");
        });
    } else {
      toast.error("Please Login First");
      navigate("/login");
    }
  };

  const fetchData = async () => {
    setLoading(true);
    const mealId = pathname.slice(15);
    try {
      const response = await axios.get(`https://www.themealdb.com/api/json/v1/1/lookup.php?i=${mealId}`);
      if (response.data.meals && response.data.meals[0]) {
        const meal = response.data.meals[0];
        const formattedProduct = {
          id: meal.idMeal,
          name: meal.strMeal,
          price: Math.floor(Math.random() * 500) + 200,
          quantity: Math.floor(Math.random() * 20) + 1,
          category: { name: meal.strCategory },
          imageFilename: meal.strMealThumb,
          instructions: meal.strInstructions,
          area: meal.strArea,
          tags: meal.strTags
        };
        setProduct(formattedProduct);
      }
    } catch (err) {
      console.log(err);
      toast.error("Failed to load product");
    }
    finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchData();
  }, []);

  return (
    <section className="min-h-screen bg-orange-50 py-8">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        {loading ? (
          <div className="flex justify-center items-center h-64">
            <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-orange-600"></div>
          </div>
        ) : !product ? (
          <div className="text-center py-12">
            <p className="text-gray-500 text-lg">Food item not found</p>
            <button
              onClick={() => navigate("/store")}
              className="mt-4 px-6 py-2 bg-orange-600 text-white rounded-lg hover:bg-orange-700"
            >
              Back to Menu
            </button>
          </div>
        ) : (
          <div className="bg-white rounded-lg shadow-lg overflow-hidden">
            <div className="flex flex-col lg:flex-row">
              <div className="lg:w-1/2">
                <img
                  alt={product.name}
                  className="w-full h-96 object-cover"
                  src={product.imageFilename || "https://images.unsplash.com/photo-1546069901-ba9599a7e63c?w=800"}
                  onError={(e) => e.target.src = "https://images.unsplash.com/photo-1546069901-ba9599a7e63c?w=800"}
                />
              </div>
              <div className="lg:w-1/2 p-8">
                <button
                  onClick={() => navigate("/store")}
                  className="flex items-center text-orange-600 hover:text-orange-800 mb-4"
                >
                  <TiArrowBack className="mr-2" />
                  Back to Menu
                </button>

                <h1 className="text-3xl font-bold text-gray-900 mb-2">{product.name}</h1>
                <p className="text-orange-600 mb-4">{product.category?.name || "Uncategorized"}</p>

                <div className="flex items-center mb-6">
                  <span className="text-3xl font-bold text-orange-800">₹{product.price}</span>
                  <span className="ml-4 text-gray-500">Available: {product.quantity}</span>
                </div>

                {product.area && (
                  <p className="text-gray-600 mb-4">
                    <span className="font-medium">Origin:</span> {product.area}
                  </p>
                )}

                {product.tags && (
                  <p className="text-gray-600 mb-4">
                    <span className="font-medium">Tags:</span> {product.tags}
                  </p>
                )}

                {product.instructions && (
                  <div className="mb-6">
                    <h3 className="font-bold text-gray-900 mb-2">Instructions</h3>
                    <p className="text-gray-600 text-sm leading-relaxed">{product.instructions}</p>
                  </div>
                )}

                <div className="mb-6">
                  <label className="block text-sm font-medium text-gray-700 mb-2">Quantity</label>
                  <div className="flex items-center gap-2">
                    <button
                      onClick={decrement}
                      className="w-10 h-10 rounded border border-orange-300 flex items-center justify-center hover:bg-orange-100"
                    >
                      -
                    </button>
                    <input
                      type="number"
                      min="1"
                      max={product.quantity}
                      value={quantity}
                      onChange={(e) => setQuantity(Math.min(Math.max(1, Number(e.target.value)), product.quantity))}
                      className="w-20 h-10 text-center border border-orange-300 rounded"
                    />
                    <button
                      onClick={increment}
                      className="w-10 h-10 rounded border border-orange-300 flex items-center justify-center hover:bg-orange-100"
                    >
                      +
                    </button>
                  </div>
                </div>

                <button
                  onClick={() => addToCartHandler(product.id)}
                  className="w-full bg-orange-600 text-white py-3 rounded-lg hover:bg-orange-700 transition font-medium"
                >
                  Add to Cart
                </button>
              </div>
            </div>
          </div>
        )}
      </div>
    </section>
  );
}

export default ViewProduct;
