import { Box, Step, StepLabel, Stepper } from "@mui/material";
import React, { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { Link, useLocation } from "react-router-dom";
import { getAxiosInstance } from "../../../utility/axiosApiConfig";
import { setOrderSummary } from "../../../features/order/orderSlice";

const steps = ["Placed", "Confirmed", "Shipped", "Delivered"];

function OrderSummary() {
  const { pathname } = useLocation();
  const { orderSummary } = useSelector((state) => state.order);
  const dispatch = useDispatch();
  const axiosInstance = getAxiosInstance();
  const [step, setStep] = useState(0);
  const [loading, setLoading] = useState(true);
  const [orderNotFound, setOrderNotFound] = useState(false);

  const fetchData = async () => {
    setLoading(true);
    // Backend doesn't have single order endpoint, so fetch all orders and filter
    await axiosInstance
      .get("http://localhost:8081/api/orders")
      .then((res) => {
        const orderId = pathname.substring(8);
        const order = res.data.find(o => o.id === Number(orderId));
        if (order) {
          dispatch(setOrderSummary(order));
          if (order.orderStatus == "PLACED") {
            setStep(1);
          } else if (order.orderStatus == "CONFIRMED") {
            setStep(2);
          } else if (order.orderStatus == "SHIPPED") {
            setStep(3);
          } else if (order.orderStatus == "DELIVERED") {
            setStep(4);
          }
        } else {
          setOrderNotFound(true);
        }
      })
      .catch((err) => {
        console.log(err);
        setOrderNotFound(true);
      })
      .finally(() => {
        setLoading(false);
      });
  };

  useEffect(() => {
    fetchData();
  }, []);

  if (loading) {
    return (
      <div className="flex justify-center items-center h-64">
        <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-indigo-600"></div>
      </div>
    );
  }

  if (orderNotFound) {
    return (
      <div className="p-4 md:px-28 md:my-4 text-center">
        <h2 className="text-2xl font-bold mb-4">Order Not Found</h2>
        <p className="text-gray-600 mb-4">The order you're looking for doesn't exist or you don't have permission to view it.</p>
        <Link
          to="/orders"
          className="inline-block px-6 py-2 bg-indigo-600 text-white rounded-lg hover:bg-indigo-700"
        >
          View My Orders
        </Link>
      </div>
    );
  }

  return (
    <div className="p-4 md:px-28 md:my-4">
      <Box sx={{ width: "100%" }}>
        <div className="flex flex-col">
          <div className="py-4 border-b">
            <p className="text-sm font-medium text-indigo-500 uppercase">
              Order {orderSummary?.orderStatus} successfully
            </p>
            <h2 className="text-3xl md:text-5xl font-bold my-2">
              Track Your Order
            </h2>
            <p>
              We appreciate your order, we're currently processing it. So hang
              tight and we'll send you confirmation very soon!
            </p>
            <p className="font-medium mt-8">Tracking number</p>
            <p className="font-medium text-sm text-indigo-500">
              {orderSummary?.id}
            </p>
            <div className="hidden md:block">
              <Stepper className="my-8 flex-wrap" activeStep={step}>
                {steps.map((label, index) => {
                  const stepProps = {};
                  const labelProps = {};
                  return (
                    <Step key={label} {...stepProps}>
                      <StepLabel {...labelProps}>{label}</StepLabel>
                    </Step>
                  );
                })}
              </Stepper>
            </div>
          </div>
          {orderSummary?.items?.map((item, idx) => (
            <div
              key={idx}
              className="flex justify-between border-b py-2 lg:py-4"
            >
              <div className="flex flex-row gap-x-8">
                <div className="flex flex-col justify-start gap-y-2">
                  <p className="text-sm md:text-base font-medium">
                    Product ID: {item.productId}
                  </p>
                  <p className="text-xs md:text-sm">Quantity: {item.quantity}</p>
                </div>
              </div>
              <p className="text-sm md:text-base self-start font-medium">
                {/* Price info not available in backend order response */}
              </p>
            </div>
          ))}
          <div className="flex flex-col text-sm md:text-base">
            <div className="pb-2 pt-4 flex justify-between">
              <p>Order Date</p>
              <span className="font-semibold">{orderSummary?.orderDate?.substring(0, 10)}</span>
            </div>
          </div>
          <Link
            to={"/store"}
            className="text-base text-end font-medium text-indigo-500 hover:text-indigo-600 my-8"
          >
            Continue Shopping -&gt;
          </Link>
        </div>
      </Box>
    </div>
  );
}

export default OrderSummary;
