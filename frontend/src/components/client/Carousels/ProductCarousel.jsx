import React, { useEffect, useState } from "react";
import Slider from "react-slick";
import "slick-carousel/slick/slick.css";
import "slick-carousel/slick/slick-theme.css";
import ProductCard from "../Cards/ProductCard";
import axios from "axios";

function ProductCarousel() {
  const [products, setProducts] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchNewArrivals = async () => {
      setLoading(true);
      try {
        const response = await axios.get("https://www.themealdb.com/api/json/v1/1/search.php?s=");
        if (response.data.meals) {
          const newArrivals = response.data.meals
            .sort(() => Math.random() - 0.5)
            .slice(0, 10)
            .map(meal => ({
              imageUrl: meal.strMealThumb,
              product: meal.strMeal,
              price: Math.floor(Math.random() * 500) + 200,
              id: meal.idMeal
            }));
          setProducts(newArrivals);
        }
      } catch (err) {
        console.log("Error fetching new arrivals:", err);
        // Fallback data if API fails
        const fallbackProducts = [
          { imageUrl: "https://images.unsplash.com/photo-1546069901-ba9599a7e63c?w=500", product: "Fresh Salad Bowl", price: 299, id: "1" },
          { imageUrl: "https://images.unsplash.com/photo-1568901346375-23c9450c58cd?w=500", product: "Gourmet Burger", price: 399, id: "2" },
          { imageUrl: "https://images.unsplash.com/photo-1565299624946-b28f40a0ae38?w=500", product: "Margherita Pizza", price: 449, id: "3" },
          { imageUrl: "https://images.unsplash.com/photo-1563379926898-05f4575a45d8?w=500", product: "Pasta Primavera", price: 349, id: "4" },
          { imageUrl: "https://images.unsplash.com/photo-1512621776951-a57141f2eefd?w=500", product: "Healthy Bowl", price: 279, id: "5" },
        ];
        setProducts(fallbackProducts);
      } finally {
        setLoading(false);
      }
    };
    fetchNewArrivals();
  }, []);

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
    slidesToShow: 5,
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
      <h2 className="text-3xl font-extrabold ml-10 text-orange-800">
        New <span className="text-orange-500">Arrivals</span>
      </h2>
      <div className="w-16 h-1 mt-1 bg-orange-500 ml-10 mb-8"></div>
      {loading ? (
        <div className="flex justify-center items-center h-64">
          <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-orange-600"></div>
        </div>
      ) : (
        <Slider {...settings}>
          {products.map((item, index) => (
            <ProductCard product={item} key={index} />
          ))}
        </Slider>
      )}
    </div>
  );
}

export default ProductCarousel;
