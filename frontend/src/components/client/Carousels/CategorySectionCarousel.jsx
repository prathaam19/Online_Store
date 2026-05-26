import React, { useState } from "react";
import CategoryCard from "../Cards/CategoryCard";
import Slider from "react-slick";
import "slick-carousel/slick/slick.css";
import "slick-carousel/slick/slick-theme.css";

function CategorySectionCarousel() {
  const category = [
    {
      imageUrl: "https://images.unsplash.com/photo-1546069901-ba9599a7e63c?w=500",
      name: "Main Course",
      category: "Seafood",
    },
    {
      imageUrl: "https://images.unsplash.com/photo-1551024601-bec78aea704b?w=500",
      name: "Desserts",
      category: "Dessert",
    },
    {
      imageUrl: "https://images.unsplash.com/photo-1567620905732-2d1ec7ab7445?w=500",
      name: "Breakfast",
      category: "Breakfast",
    },
    {
      imageUrl: "https://images.unsplash.com/photo-1585937421612-70a008356fbe?w=500",
      name: "Indian Breakfast",
      category: "Breakfast",
    },
    {
      imageUrl: "https://images.unsplash.com/photo-1512621776951-a57141f2eefd?w=500",
      name: "Healthy",
      category: "Vegetarian",
    },
    {
      imageUrl: "https://images.unsplash.com/photo-1476224203421-9ac39bcb3327?w=500",
      name: "Fruits",
      category: "Seafood",
    },
    {
      imageUrl: "https://images.unsplash.com/photo-1540189549336-e6e99c3679fe?w=500",
      name: "Vegetables",
      category: "Vegetarian",
    },
    {
      imageUrl: "https://images.unsplash.com/photo-1482049016688-2d3e1b311543?w=500",
      name: "Pasta",
      category: "Pasta",
    },
    {
      imageUrl: "https://images.unsplash.com/photo-1562208067-9d5a455ab6d5?w=500",
      name: "Cold Drinks",
      category: "Seafood",
    },
    {
      imageUrl: "https://images.unsplash.com/photo-1509440159596-0249088772ff?w=500",
      name: "Bakery",
      category: "Breakfast",
    },
    {
      imageUrl: "https://images.unsplash.com/photo-1566478989037-eec170784d0b?w=500",
      name: "Snacks",
      category: "Miscellaneous",
    },
    {
      imageUrl: "https://images.unsplash.com/photo-1497034825429-c343d7c6a68f?w=500",
      name: "Icecream",
      category: "Dessert",
    },
  ];

  function NextArrow(props) {
    const { onClick, currentSlide, slideCount } = props;
    return (
      <div
        className={`${
          slideCount / currentSlide == 2 ? "hidden" : "block"
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
          infinite: false,
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
        Food Categories
      </h2>
      <div className="w-16 h-[5px] bg-orange-500 mx-auto mb-8"></div>
      <Slider {...settings}>
        {category.map((item, idx) => (
          <CategoryCard product={item} key={idx} />
        ))}
      </Slider>
    </div>
  );
}

export default CategorySectionCarousel;
