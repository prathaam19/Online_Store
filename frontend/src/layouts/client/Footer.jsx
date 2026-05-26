// eslint-disable-next-line no-unused-vars
import React from "react";
import { Link } from "react-router-dom";
import { FaFacebook, FaInstagram, FaTwitter, FaWhatsapp } from "react-icons/fa";

function Footer() {
  const year = new Date().getFullYear();
  return (
    <footer className="text-base lg:text-sm bg-slate-800 text-slate-300">
      <div className="sm:flex py-6 max-w-screen-xl mx-auto">
        <div className="basis-[40%]">
          <Link className="flex items-center" to="/">
            <span className="sm:text-xl font-extrabold pl-1 text-white">
              Food<span className="text-orange-500">-Mart</span>
            </span>
          </Link>
          <p className="mt-2 mb-8 px-2 leading-relaxed tracking- lg:pr-24 text-justify">
            Welcome to Food-Mart, your ultimate destination for delicious food.
            With a wide selection of high-quality meals to choose from, you will find
            the perfect dishes to suit your taste and budget. Explore our menu today
            and enjoy tasty meals delivered to your doorstep with Food-Mart.
          </p>
        </div>
        <div className="sm:grid grid-cols-3 gap-5 sm:ml-8 ml-2 basis-[60%]">
          <div className="text-slate-300">
            <h1 className="mt-2 mb-1 font-bold text-lg">Company</h1>
            <div className="mb-4 w-8 h-1 bg-orange-500"></div>
            <ul className="leading-loose">
              <li>
                <Link to="/" className="hover:text-slate-400">
                  Home
                </Link>
              </li>
              <li>
                <Link to="/store" className="hover:text-slate-400">
                  Store
                </Link>
              </li>
              <li>
                <Link to="/about" className="hover:text-slate-400">
                  About Us
                </Link>
              </li>
              <li>
                <Link to="/contact" className="hover:text-slate-400">
                  Contact
                </Link>
              </li>
            </ul>
          </div>
          <div className="text-slate-300">
            <h1 className="mt-2 mb-1 font-bold text-lg">Resources</h1>
            <div className="mb-4 w-8 h-1 bg-orange-500"></div>
            <ul className="leading-loose">
              <li>
                <Link to="/contact" className="hover:text-slate-400">
                  Contact
                </Link>
              </li>
              <li>
                <Link to="/store" className="hover:text-slate-400">
                  Products
                </Link>
              </li>
              <li>
                <Link to="/about" className="hover:text-slate-400">
                  About
                </Link>
              </li>
            </ul>
          </div>
          <div className="text-slate-300">
            <h1 className="mt-2 mb-1 font-bold text-lg">About</h1>
            <div className="mb-4 w-8 h-1 bg-orange-500"></div>
            <ul className="leading-loose">
              <li>
                <Link to="/about" className="hover:text-slate-400">
                  About Us
                </Link>
              </li>
              <li>
                <Link to="/contact" className="hover:text-slate-400">
                  Contact Us
                </Link>
              </li>
            </ul>
          </div>
        </div>
      </div>
      <div className="px-4 py-2 max-w-screen-xl mx-auto text-sm">
        <hr className="border-slate-600" />
        <div className="lg:flex items-center justify-between">
          <p className="my-6 text-center lg:text-start">
            &copy; 2024-{year}{" "}
            <span className="text-orange-500 hover:text-orange-600 font-semibold cursor-pointer">
              Food-Mart.
            </span>{" "}
            All rights reserved @2025 Food-Mart
          </p>
          <div className="flex items-center justify-center gap-5 text-xl lg:mb-0 mb-4">
            <a
              href="https://www.instagram.com"
              target="_blank"
              rel="noopener noreferrer"
              className="hover:bg-gradient-to-tr from-[#f9ce34] via-[#ee2a7b] to-[#6228d7] p-1 rounded-md"
            >
              <FaInstagram />
            </a>
            <a
              href="https://www.facebook.com"
              target="_blank"
              rel="noopener noreferrer"
              className="hover:text-[#316FF6] hover:bg-white p-1 rounded-md"
            >
              <FaFacebook />
            </a>
            <a
              href="https://www.whatsapp.com"
              target="_blank"
              rel="noopener noreferrer"
              className="hover:text-[#25D366] hover:bg-white p-1 rounded-md"
            >
              <FaWhatsapp />
            </a>
            <a
              href="https://www.twitter.com"
              target="_blank"
              rel="noopener noreferrer"
              className="hover:text-[#1DA1F2] hover:bg-white p-1 rounded-md"
            >
              <FaTwitter />
            </a>
          </div>
        </div>
      </div>
    </footer>
  );
}

export default Footer;
