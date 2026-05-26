import { Fragment, useEffect, useState } from "react"
import { Dialog, Disclosure, Menu, Transition } from "@headlessui/react"
import { XMarkIcon } from "@heroicons/react/24/outline"
import { ChevronDownIcon, FunnelIcon, MinusIcon, PlusIcon, Squares2X2Icon } from "@heroicons/react/20/solid"
import axios from "axios"
import { Link, useLocation, useNavigate, useParams, useSearchParams } from "react-router-dom"
import { useDispatch, useSelector } from "react-redux"
import { filters, singleFilter, sortOptions } from "../data/FilterData"
import { FormControl, FormControlLabel, FormLabel, Radio, RadioGroup } from "@mui/material"
import { applyProductFilter, setProductItems } from "../features/product/productSlice"
import { getAxiosInstance } from "../utility/axiosApiConfig"

function Store() {
  const axiosInstance = getAxiosInstance();
  const { products } = useSelector((state) => state.product)
  const { searchProduct } = useSelector((state) => state.product)
  const [mobileFiltersOpen, setMobileFiltersOpen] = useState(false)
  const [categoryFilter, setCategoryFilter] = useState([])
  const [searchQuery, setSearchQuery] = useState("")
  const [selectedCategory, setSelectedCategory] = useState("All")
  const [meals, setMeals] = useState([])
  const [searchParams] = useSearchParams()

  const location = useLocation()
  const navigate = useNavigate()
  const param = useParams()
  const dispatch = useDispatch()

  const categories = [
    { id: "All", name: "All Items", icon: "🍽️" },
    { id: "Seafood", name: "Seafood", icon: "🦐" },
    { id: "Beef", name: "Beef", icon: "�" },
    { id: "Chicken", name: "Chicken", icon: "�" },
    { id: "Vegetarian", name: "Vegetarian", icon: "🥗" },
    { id: "Dessert", name: "Dessert", icon: "🍰" },
    { id: "Pasta", name: "Pasta", icon: "🍝" },
    { id: "Breakfast", name: "Breakfast", icon: "�" },
    { id: "Miscellaneous", name: "Miscellaneous", icon: "�️" },
    { id: "Goat", name: "Goat", icon: "🐐" },
    { id: "Lamb", name: "Lamb", icon: "�" },
  ]

  const handleFilter = (value, sectionId) => {
    if (sectionId === "category") {
      let updatedCategoryFilter
      if (categoryFilter.includes(value)) {
        updatedCategoryFilter = categoryFilter.filter((item) => item !== value)
      } else {
        updatedCategoryFilter = [...categoryFilter, value]
      }
      setCategoryFilter(updatedCategoryFilter)
    }
  }

  const handleSearch = async (e) => {
    e.preventDefault()
    if (searchQuery.trim()) {
      try {
        const response = await axios.get(`https://www.themealdb.com/api/json/v1/1/search.php?s=${searchQuery}`)
        if (response.data.meals) {
          const formattedMeals = response.data.meals.map(meal => ({
            id: meal.idMeal,
            name: meal.strMeal,
            price: Math.floor(Math.random() * 500) + 100,
            quantity: Math.floor(Math.random() * 20) + 1,
            category: { name: meal.strCategory },
            imageFilename: meal.strMealThumb
          }))
          setMeals(formattedMeals)
          dispatch(setProductItems(formattedMeals))
        } else {
          setMeals([])
        }
      } catch (err) {
        console.log("Error searching meals:", err)
      }
    } else {
      fetchMeals()
    }
  }

  function classNames(...classes) {
    return classes.filter(Boolean).join(" ")
  }

  const fetchMeals = async () => {
    try {
      const response = await axios.get("https://www.themealdb.com/api/json/v1/1/search.php?s=")
      if (response.data.meals) {
        const formattedMeals = response.data.meals.map(meal => ({
          id: meal.idMeal,
          name: meal.strMeal,
          price: Math.floor(Math.random() * 500) + 100,
          quantity: Math.floor(Math.random() * 20) + 1,
          category: { name: meal.strCategory },
          imageFilename: meal.strMealThumb
        }))
        setMeals(formattedMeals)
        dispatch(setProductItems(formattedMeals))
      }
    } catch (err) {
      console.log("Error fetching meals:", err)
    }
  }

  useEffect(() => {
    fetchMeals()
  }, [])

  useEffect(() => {
    const categoryFromUrl = searchParams.get('category')
    const searchFromUrl = searchParams.get('search')
    if (categoryFromUrl) {
      setSelectedCategory(categoryFromUrl)
    }
    if (searchFromUrl) {
      setSearchQuery(searchFromUrl)
      // Perform API search when search parameter is present
      const performSearch = async () => {
        try {
          const response = await axios.get(`https://www.themealdb.com/api/json/v1/1/search.php?s=${searchFromUrl}`)
          if (response.data.meals) {
            const formattedMeals = response.data.meals.map(meal => ({
              id: meal.idMeal,
              name: meal.strMeal,
              price: Math.floor(Math.random() * 500) + 100,
              quantity: Math.floor(Math.random() * 20) + 1,
              category: { name: meal.strCategory },
              imageFilename: meal.strMealThumb
            }))
            setMeals(formattedMeals)
            dispatch(setProductItems(formattedMeals))
          } else {
            setMeals([])
          }
        } catch (err) {
          console.log("Error searching meals:", err)
        }
      }
      performSearch()
    }
  }, [searchParams])

  const filteredProducts = selectedCategory === "All" 
    ? meals 
    : meals.filter(item => {
        const categoryName = (item.category?.name || "").toLowerCase();
        const selectedCategoryLower = selectedCategory.toLowerCase();
        
        const category = categories.find(c => c.id === selectedCategory);
        const displayName = category ? category.name.toLowerCase() : "";
        
        return categoryName === selectedCategoryLower || 
               categoryName === displayName ||
               categoryName.includes(selectedCategoryLower) || 
               selectedCategoryLower.includes(categoryName);
      })
      .filter((item) => {
        return searchQuery.trim() === "" || 
               item.name?.toLowerCase().includes(searchQuery.toLowerCase()) ||
               item.category?.name?.toLowerCase().includes(searchQuery.toLowerCase());
      })

  return (
    <div className="bg-gradient-to-b from-orange-50 to-white min-h-screen">
      <main className="mx-auto max-w-7xl px-4 sm:px-6 lg:px-8 py-8">
        {/* Header */}
        <div className="text-center mb-8">
          <h1 className="text-4xl lg:text-5xl font-bold text-orange-800 mb-2">
            🍽️ Food-Mart Store
          </h1>
          <p className="text-gray-600">Delicious food and beverages at your fingertips</p>
        </div>

        {/* Search Bar */}
        <div className="max-w-2xl mx-auto mb-8">
          <form onSubmit={handleSearch} className="flex items-center gap-2">
            <input
              type="text"
              placeholder="Search for food items, beverages..."
              value={searchQuery}
              onChange={(e) => setSearchQuery(e.target.value)}
              className="flex-1 px-6 py-3 border-2 border-orange-300 rounded-full focus:outline-none focus:ring-2 focus:ring-orange-500 focus:border-orange-500 text-lg shadow-sm"
            />
            <button
              type="submit"
              className="px-8 py-3 bg-orange-600 text-white rounded-full hover:bg-orange-700 transition font-semibold shadow-md hover:shadow-lg"
            >
              🔍 Search
            </button>
          </form>
        </div>

        {/* Category Filter */}
        <div className="mb-8">
          <h2 className="text-xl font-bold text-gray-800 mb-4">Categories</h2>
          <div className="flex flex-wrap gap-3">
            {categories.map((category) => (
              <button
                key={category.id}
                onClick={() => setSelectedCategory(category.id)}
                className={`px-4 py-2 rounded-full font-medium transition-all transform hover:scale-105 ${
                  selectedCategory === category.id
                    ? "bg-orange-600 text-white shadow-lg shadow-orange-300"
                    : "bg-white text-gray-700 border-2 border-orange-300 hover:border-orange-500 hover:bg-orange-50"
                }`}
              >
                <span className="mr-2">{category.icon}</span>
                {category.name}
              </button>
            ))}
          </div>
        </div>

        {/* Products Grid */}
        <section aria-labelledby="products-heading">
          <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6">
            {filteredProducts
              .filter((item) => {
                return searchProduct.toLowerCase() == ""
                  ? item
                  : item.name?.toLowerCase().includes(searchProduct) ||
                      item.category?.name?.toLowerCase().includes(searchProduct)
              })
              .map((item) => (
                <Link
                  key={item.id}
                  to={`/store/product/${item.id}`}
                  className="block bg-white rounded-2xl shadow-lg overflow-hidden border border-orange-100 hover:shadow-2xl transition transform hover:-translate-y-2 hover:border-orange-300"
                >
                  <div className="w-full h-56 bg-gradient-to-br from-orange-100 to-orange-50 relative overflow-hidden">
                    <img
                      className="object-cover w-full h-full hover:scale-110 transition duration-300"
                      src={item.imageFilename}
                      alt={item.name}
                      onError={(e) => {
                        e.target.src = "https://images.unsplash.com/photo-1546069901-ba9599a7e63c?w=500";
                        e.target.onerror = null;
                      }}
                    />
                    {item.quantity > 0 ? (
                      <span className="absolute top-3 right-3 bg-green-500 text-white text-xs font-bold px-3 py-1 rounded-full shadow-md">
                        ✓ In Stock
                      </span>
                    ) : (
                      <span className="absolute top-3 right-3 bg-red-500 text-white text-xs font-bold px-3 py-1 rounded-full shadow-md">
                        ✗ Out of Stock
                      </span>
                    )}
                    <div className="absolute bottom-0 left-0 right-0 bg-gradient-to-t from-black/50 to-transparent p-3">
                      <span className="text-white text-sm font-medium">{item.category?.name || "Food"}</span>
                    </div>
                  </div>

                  <div className="p-5">
                    <h3 className="text-lg font-bold text-gray-900 mb-2 line-clamp-2 h-14">{item.name}</h3>
                    <div className="flex items-center justify-between mb-3">
                      <p className="text-2xl font-bold text-orange-600">₹{item.price}</p>
                      <div className="flex items-center text-sm text-gray-500">
                        <span className="mr-1">📦</span>
                        <span>{item.quantity} left</span>
                      </div>
                    </div>
                    <button className="w-full py-2 bg-orange-500 text-white rounded-lg hover:bg-orange-600 transition font-semibold shadow-md hover:shadow-lg">
                      View Details
                    </button>
                  </div>
                </Link>
              ))}
          </div>
          
          {filteredProducts.length === 0 && (
            <div className="text-center py-16 bg-white rounded-2xl shadow-lg">
              <div className="text-6xl mb-4">🍽️</div>
              <p className="text-gray-500 text-xl font-medium">No food items found in this category</p>
              <p className="text-gray-400 mt-2">Try selecting a different category</p>
            </div>
          )}
        </section>

        {filteredProducts.length > 0 && (
          <div className="mt-8 text-center">
            <p className="text-gray-600">
              Showing {filteredProducts.length} delicious {selectedCategory === "All" ? "items" : selectedCategory.toLowerCase()}
            </p>
          </div>
        )}
      </main>
    </div>
  )
}

export default Store

