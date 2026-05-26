import React from 'react'

function About() {
  return (
    <>
      <section className="bg-orange-50 py-8">
        <div className="max-w-7xl mx-auto px-4">
          <div className="text-center">
            <h2 className="text-3xl md:text-4xl font-bold text-orange-800">About Us</h2>
            <div className="w-16 h-1 bg-orange-500 mx-auto mt-2"></div>
          </div>
        </div>
      </section>

      <section className="py-16 bg-white">
        <div className="max-w-7xl mx-auto px-4">
          <div className="grid md:grid-cols-2 gap-12 items-center">
            <div>
              <h2 className="text-4xl font-bold text-orange-800 mb-4">FoodMart</h2>
              <h3 className="text-2xl font-semibold text-gray-700 mb-6">Your Everyday Online Grocery Destination</h3>
            </div>

            <div>
              <p className="text-gray-600 mb-4">
                FoodMart is an online grocery and food shopping platform 
                designed to make everyday shopping simple, fast, and convenient. 
                Our platform helps customers explore a wide variety of grocery 
                products and daily essentials from the comfort of their homes.
              </p>

              <p className="text-gray-600 mb-6">
                We focus on providing a smooth shopping experience with 
                easy product browsing, organized categories, secure ordering, 
                and reliable service. FoodMart aims to simplify online shopping 
                while ensuring quality, convenience, and customer satisfaction.
              </p>

              <ul className="space-y-3 mb-6">
                <li className="flex items-center text-gray-700">
                  <span className="text-orange-500 mr-2">✓</span> 
                  Wide range of grocery and daily essential products
                </li>

                <li className="flex items-center text-gray-700">
                  <span className="text-orange-500 mr-2">✓</span> 
                  Smooth and user-friendly shopping experience
                </li>

                <li className="flex items-center text-gray-700">
                  <span className="text-orange-500 mr-2">✓</span> 
                  Convenient and reliable online ordering platform
                </li>
              </ul>

              <p className="text-gray-600 italic">
                FoodMart is built with the vision of making online grocery 
                shopping easier, faster, and more accessible for everyone.
              </p>
            </div>
          </div>
        </div>
      </section>

      <section className="py-16 bg-orange-50">
        <div className="max-w-7xl mx-auto px-4">
          <div className="text-center mb-12">
            <h2 className="text-3xl font-bold text-orange-800">Team</h2>
            <p className="text-gray-600 mt-2">Project Developer</p>
            <div className="w-16 h-1 bg-orange-500 mx-auto mt-2"></div>
          </div>

          <div className="flex justify-center">
            <div className="bg-white rounded-lg shadow-lg p-6 max-w-md">
              <div className="flex items-center mb-4">
                <div className="w-24 h-24 bg-orange-100 rounded-full flex items-center justify-center mr-4">
                  <span className="text-3xl">👨‍💻</span>
                </div>
                <div>
                  <h4 className="text-xl font-bold text-gray-800">Prathamesh Parth Patkar</h4>
                  <span className="text-orange-600 font-medium">Developer</span>
                </div>
              </div>

              <p className="text-gray-600 mb-4">
                Passionate about creating modern, user-friendly, 
                and efficient web applications that provide seamless 
                digital experiences.
              </p>

              <div className="flex gap-4">
                <a
                  href="https://github.com/prathaam19"
                  target="_blank"
                  rel="noreferrer"
                  className="text-gray-600 hover:text-gray-900 transition"
                >
                  <i className="fab fa-github text-2xl"></i>
                </a>

                <a
                  href="https://linkedin.com/in/prathamesh-parth"
                  target="_blank"
                  rel="noreferrer"
                  className="text-gray-600 hover:text-blue-600 transition"
                >
                  <i className="fab fa-linkedin text-2xl"></i>
                </a>
              </div>
            </div>
          </div>
        </div>
      </section>
    </>
  )
}

export default About

