import React, { useEffect, useState } from "react";
import Slider from "react-slick";
import "slick-carousel/slick/slick.css";
import "slick-carousel/slick/slick-theme.css";
import axios from "axios";

function NextArrow(props) {
  const { onClick } = props;
  return (
    <div
      className={
        "hidden slick-arrow slick-next absolute right-5 sm:flex justify-center items-center px-5 rounded h-20 bg-black/50 hover:bg-black/50"
      }
      onClick={onClick}
    />
  );
}
function PrevArrow(props) {
  const { onClick } = props;
  return (
    <div
      className={
        "hidden slick-arrow slick-prev absolute z-10 left-5 sm:flex justify-center items-center px-5 rounded h-20 bg-black/50 hover:bg-black/50"
      }
      onClick={onClick}
    />
  );
}

function MainCarousel() {
  const [mainCarouselData, setMainCarouselData] = useState([]);

  useEffect(() => {
    const fetchRandomMeals = async () => {
      try {
        const response = await axios.get("https://www.themealdb.com/api/json/v1/1/search.php?s=");
        if (response.data.meals) {
          const randomMeals = response.data.meals
            .sort(() => Math.random() - 0.5)
            .slice(0, 3)
            .map(meal => ({
              image: meal.strMealThumb,
              name: meal.strMeal,
              category: meal.strCategory
            }));
          setMainCarouselData(randomMeals);
        }
      } catch (err) {
        console.log(err);
      }
    };
    fetchRandomMeals();
  }, []);

  var settings = {
    dots: true,
    infinite: true,
    autoplay: true,
    autoplaySpeed: 5000,
    speed: 1000,
    slidesToShow: 1,
    slidesToScroll: 1,
    nextArrow: <NextArrow />,
    prevArrow: <PrevArrow />,
    fade: true,
    cssEase: "ease-in-out",
  };
  return (
    <Slider {...settings}>
      {mainCarouselData.map((item, index) => (
        <div key={index} className="relative flex w-full h-[350px] md:h-[450px] items-center overflow-hidden rounded-xl shadow-2xl">
          <img
            className="object-cover w-full h-full hover:scale-105 transition-transform duration-700 ease-in-out"
            src={item.image}
            alt={item.name}
          />
          <div className="absolute inset-0 bg-gradient-to-t from-black/80 via-black/40 to-transparent flex items-end">
            <div className="max-w-7xl mx-auto px-4 pb-12 w-full">
              <h2 className="text-2xl md:text-4xl font-bold text-white mb-2 drop-shadow-lg">
                {item.name}
              </h2>
              <p className="text-base md:text-xl text-orange-400 font-semibold drop-shadow-md">
                {item.category}
              </p>
            </div>
          </div>
        </div>
      ))}
    </Slider>
  );
}

export default MainCarousel;
