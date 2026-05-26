import React, { useEffect } from "react";
import { useDispatch, useSelector } from "react-redux";
import {
  addCartItem,
  seTotalPrice,
  setDiscount,
  setTotalDiscountedPrice,
  setTotalItem,
} from "../../../features/cart/cartSlice";
import { getAxiosInstance } from "../../../utility/axiosApiConfig";
import { Link } from "react-router-dom";

function AddToCart() {
  const dispatch = useDispatch();
  const { cartItems, totalPrice, totalDiscountedPrice, discount } = useSelector(
    (state) => state.cart
  );
  const axiosInstance = getAxiosInstance();

  const gst = Math.round(totalPrice * 0.18);
  const deliveryFee = totalPrice >= 100 ? 0 : 50;

  const fetchCart = async () => {
    await axiosInstance
      .get("http://localhost:8081/api/cart", {})
      .then((res) => {
        const data = res.data;
        dispatch(addCartItem(data.products || []));
        // Calculate totals from products
        const totalItem = data.products ? data.products.length : 0;
        const totalPrice = data.products ? data.products.reduce((sum, item) => sum + (item.price || 0), 0) : 0;
        const gst = Math.round(totalPrice * 0.18);
        const deliveryFee = totalPrice >= 100 ? 0 : 50;
        const totalWithGstAndDelivery = totalPrice + gst + deliveryFee;
        dispatch(setTotalItem(totalItem));
        dispatch(seTotalPrice(totalPrice));
        dispatch(setTotalDiscountedPrice(totalWithGstAndDelivery));
        dispatch(setDiscount(0));
      })
      .catch((err) => {
        console.log(err);
      });
  };

  const removeItem = async (productId) => {
    await axiosInstance
      .delete(`http://localhost:8081/api/cart/remove/${productId}`, {})
      .then((res) => {
        fetchCart();
      })
      .catch((err) => {
        console.log(err);
      });
  };

  useEffect(() => {
    fetchCart();
  }, []);

  return (
    <section className="mx-auto max-w-7xl p-4 sm:px-6 lg:px-8 lg:py-8">
      <h2 className="text-2xl md:text-4xl font-bold mb-8 text-orange-800">Shopping Cart</h2>
      <div className="flex flex-col lg:flex-row gap-x-20">
        <div className="basis-[58%]">
          <hr />
          {cartItems.length != 0 ? (
            <>
              {cartItems.map((item) => (
                <div
                  key={item.id}
                  className="flex justify-between border-b py-3 lg:py-8"
                >
                  <div className="flex-1 flex flex-row gap-x-6">
                    <img
                      className="h-32 w-24 lg:h-56 lg:w-44 rounded object-cover bg-orange-50"
                      src={item.imageFilename ? `http://localhost:8081/api/products/${item.id}/image` : "https://images.unsplash.com/photo-1546069901-ba9599a7e63c?w=500"}
                      alt={item.name}
                      onError={(e) => e.target.src = "https://images.unsplash.com/photo-1546069901-ba9599a7e63c?w=500"}
                    />
                    <div className="flex flex-col justify-between w-full">
                      <div className="flex flex-col md:flex-row justify-between">
                        <div className="">
                          <p className="text-sm md:text-base font-medium">
                            {item.name}
                          </p>
                          <p className="text-xs md:text-sm text-orange-600">
                            {item.category?.name || "Uncategorized"}
                          </p>
                          <p className="text-sm md:text-base font-medium lg:mt-4">
                            ₹{item.price}
                          </p>
                        </div>
                      </div>
                      <div>
                        {item.quantity != 0 ? (
                          <>
                            {item.quantity <= 10 ? (
                              <p className="text-sm md:text-base">
                                {item.quantity} left
                              </p>
                            ) : (
                              <p className="text-sm md:text-base">✔ In stock</p>
                            )}
                          </>
                        ) : (
                          <p className="text-sm md:text-base text-red-500">
                            Out of stock
                          </p>
                        )}
                      </div>
                    </div>
                  </div>
                  <div className="basis-[20%] flex justify-end items-start">
                    <button
                      onClick={() => removeItem(item.id)}
                      className="block text-sm md:text-base px-2 py-1 rounded text-orange-500 hover:text-orange-700"
                    >
                      Remove
                    </button>
                  </div>
                </div>
              ))}
            </>
          ) : (
            <>
              <div className="flex flex-col h-full py-16 items-center justify-center">
                <h2 className="text-2xl md:text-4xl font-bold mb-2">
                  Cart is empty
                </h2>
                <p className="text-sm md:text-lg font-bold mb-8 text-zinc-500">
                  Please add food items
                </p>
                <Link
                  to={"/store"}
                  className="text-lg font-semibold text-orange-500 hover:text-orange-600"
                >
                  Continue Shopping -&gt;
                </Link>
              </div>
            </>
          )}
        </div>
        <div className="flex-1 h-fit p-4 mt-4 md:mt-0 md:p-8 bg-orange-50 border border-orange-200 rounded">
          <h4 className="text-xl font-medium mb-4 text-orange-800">Order summary</h4>
          <div className="flex flex-col text-sm md:text-base">
            <div className="py-4 border-b flex justify-between">
              <p>Subtotal</p>
              <span className="font-semibold">₹{totalPrice}</span>
            </div>
            <div className="py-4 border-b flex justify-between">
              <p>GST (18%)</p>
              <span className="font-semibold">₹{gst}</span>
            </div>
            <div className="py-4 border-b flex justify-between">
              <p>Delivery fee</p>
              <span className={`font-semibold ${deliveryFee === 0 ? 'text-green-500' : 'text-red-500'}`}>
                {deliveryFee === 0 ? 'FREE' : `₹${deliveryFee}`}
              </span>
            </div>
            <div className="py-4 border-b flex justify-between">
              <p>Discount</p>
              <span className="font-semibold text-green-500">₹{discount}</span>
            </div>
            <div className="py-4 flex justify-between font-semibold text-lg">
              <p>Order total</p>
              <span>₹{totalDiscountedPrice}</span>
            </div>
            {totalPrice < 100 && (
              <p className="text-xs text-orange-600 mt-2">
                Add ₹{100 - totalPrice} more for free delivery!
              </p>
            )}
            <Link
              to={`${cartItems.length != 0 ? "/checkout?step=1" : "/cart"} `}
              className="mt-8 text-center block bg-orange-600 hover:bg-orange-700 text-white rounded py-3"
            >
              Checkout
            </Link>
          </div>
        </div>
      </div>
    </section>
  );
}

export default AddToCart;
