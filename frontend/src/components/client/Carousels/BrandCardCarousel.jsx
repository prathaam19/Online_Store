import React from "react";
import BrandCard from "../Cards/BrandCard";
import Slider from "react-slick";
import "slick-carousel/slick/slick.css";
import "slick-carousel/slick/slick-theme.css";

function BrandCardCarousel() {
  const brands = [
    {
      imageUrl: "https://images.unsplash.com/photo-1504674900247-0877df9cc836?w=500",
      brand: "McDonald's",
    },
    {
      imageUrl: "https://images.unsplash.com/photo-1571091718767-18b5b1457add?w=500",
      brand: "KFC",
    },
    {
      imageUrl: "https://images.unsplash.com/photo-1550547660-d9450f859349?w=500",
      brand: "Pizza Hut",
    },
    {
      imageUrl: "https://images.unsplash.com/photo-1568901346375-23c9450c58cd?w=500",
      brand: "Burger King",
    },
    {
      imageUrl: "https://images.unsplash.com/photo-1594212699903-ec8a3eca50f5?w=500",
      brand: "Domino's",
    },
    {
      imageUrl: "https://images.unsplash.com/photo-1572802419224-296b0aeee0d9?w=500",
      brand: "Starbucks",
    },
  ];

  function NextArrow(props) {
    const { onClick, currentSlide, slideCount } = props;
    return (
      <div
        className={`${
          slideCount % 4 == currentSlide ? "hidden" : "block"
        } slick-arrow slick-next flex justify-center items-center px-5 rounded h-20 bg-black/50 hover:bg-black/50`}
        onClick={onClick}
      />
    );
  }
  function PrevArrow(props) {
    const { onClick, currentSlide } = props;
    return (
      <div
        className={`${
          currentSlide == 0 ? "hidden" : "block"
        } slick-arrow slick-prev z-10 flex justify-center items-center px-5 rounded h-20 bg-black/50 hover:bg-black/50`}
        onClick={onClick}
      />
    );
  }

  var settings = {
    infinite: false,
    speed: 500,
    slidesToShow: 4,
    slidesToScroll: 1,
    initialSlide: 0,
    responsive: [
      {
        breakpoint: 1024,
        settings: {
          slidesToShow: 3,
          slidesToScroll: 1,
          infinite: true,
          dots: true,
        },
      },
      {
        breakpoint: 600,
        settings: {
          slidesToShow: 2,
          slidesToScroll: 1,
          initialSlide: 2,
        },
      },
      {
        breakpoint: 480,
        settings: {
          arrows: false,
          infinite: true,
          speed: 500,
          autoplay: true,
          slidesToShow: 1,
          slidesToScroll: 1,
        },
      },
    ],
    nextArrow: <NextArrow />,
    prevArrow: <PrevArrow />,
  };
  return (
    <div className="relative px-4 lg:px-8">
      <h2 className="text-3xl font-extrabold text-center text-orange-800">
        Popular Food Brands
      </h2>
      <div className="w-16 h-[5px] bg-orange-500 mx-auto mb-8"></div>
      <Slider {...settings}>
        {brands.map((item, index) => (
          <BrandCard product={item} key={index} />
        ))}
      </Slider>
      {/* <h1 className="sm:hidden text-center mt-2 font-semibold">Swipe</h1> */}
    </div>
  );
}

export default BrandCardCarousel;
