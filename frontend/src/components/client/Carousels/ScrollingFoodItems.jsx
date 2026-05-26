import React, { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import axios from "axios";

function ScrollingFoodItems() {
  const [foodItems, setFoodItems] = useState([]);

  useEffect(() => {
    const fetchPopularMeals = async () => {
      try {
        const response = await axios.get("https://www.themealdb.com/api/json/v1/1/search.php?s=");
        if (response.data.meals) {
          const popularMeals = response.data.meals
            .sort(() => Math.random() - 0.5)
            .slice(0, 10)
            .map(meal => ({
              name: meal.strMeal,
              price: Math.floor(Math.random() * 500) + 200,
              image: meal.strMealThumb,
              id: meal.idMeal
            }));
          setFoodItems(popularMeals);
        }
      } catch (err) {
        console.log(err);
      }
    };
    fetchPopularMeals();
  }, []);

  return (
    <div className="bg-gradient-to-r from-orange-50 to-white py-6 overflow-hidden rounded-2xl shadow-lg">
      <div className="max-w-screen-xl mx-auto px-4">
        <h3 className="text-2xl font-bold text-gray-800 mb-4">
          Popular <span className="text-orange-600">Food Items</span>
        </h3>
        <div className="flex gap-4 overflow-x-auto pb-4 scrollbar-hide">
          {foodItems.map((item, index) => (
            <Link
              key={index}
              to={`/store/product/${item.id}`}
              className="flex-shrink-0 w-48 bg-white rounded-xl shadow-md overflow-hidden hover:shadow-xl transition-all duration-300 transform hover:-translate-y-2 hover:scale-105 border border-orange-100"
            >
              <div className="h-32 bg-gradient-to-br from-orange-100 to-orange-50 relative overflow-hidden">
                <img
                  src={item.image}
                  alt={item.name}
                  className="w-full h-full object-cover hover:scale-110 transition-transform duration-500"
                />
              </div>
              <div className="p-4">
                <p className="text-sm font-semibold text-gray-900 truncate line-clamp-2 h-10">{item.name}</p>
                <p className="text-lg font-bold text-orange-600">₹{item.price}</p>
              </div>
            </Link>
          ))}
        </div>
      </div>
    </div>
  );
}

export default ScrollingFoodItems;
