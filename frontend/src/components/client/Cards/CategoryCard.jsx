import React from "react";
import { Link } from "react-router-dom";

const CategoryCard = ({ product, key }) => {
  const cardStyle = {
    backgroundImage: `url(${product.imageUrl})`,
  };

  return (
    <div key={key}>
      <Link
        to={`/store?category=${product.category}`}
        className="mx-auto cursor-pointer flex flex-col justify-center bg-white rounded-lg shadow-lg overflow-hidden w-[18rem] border border-solid border-gray-300 hover:shadow-xl transition transform hover:scale-105"
      >
        <div className="w-full h-40">
          <div
            className="relative w-full h-40 mb-6 bg-center bg-no-repeat bg-cover"
            style={cardStyle}
          >
            <div className="absolute inset-0 flex flex-col items-center justify-center bg-gray-900 bg-opacity-60 hover:bg-opacity-50 transition">
                <h2 className="text-3xl text-white font-bold mb-2">{product.name}</h2>
            </div>
          </div>
        </div>
      </Link>
    </div>
  );
};

export default CategoryCard;
