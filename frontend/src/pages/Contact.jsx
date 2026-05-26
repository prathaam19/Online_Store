import React, { useState } from "react";
import { FaEnvelope, FaLocationDot, FaPhone } from "react-icons/fa6";

function Contact() {
  const [formData, setFormData] = useState({
    name: "",
    email: "",
    message: "",
  });

  const [loading, setLoading] = useState(false);

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);

    try {
      const response = await fetch("http://localhost:8081/api/contact/submit", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(formData),
      });

      if (response.ok) {
        alert("Message sent successfully!");
        setFormData({ name: "", email: "", message: "" });
      } else {
        alert("Failed to send message. Please try again.");
      }
    } catch (error) {
      console.error("Error submitting form:", error);
      alert("Something went wrong. Please try again later.");
    }

    setLoading(false);
  };

  return (
    <div className="max-w-7xl mx-auto px-4 py-12">
      <h1 className="text-3xl md:text-4xl text-center font-semibold text-orange-800">
        Contact Us
      </h1>
      <div className="h-1 w-16 bg-orange-500 mx-auto mt-1"></div>
      <div className="flex flex-col">
        <div className="grid grid-cols-1 md:grid-cols-3 gap-10 my-8 md:gap-24 md:my-16">
          <div className="flex">
            <FaLocationDot size={20} className="h-fit mt-1.5 mr-6 text-orange-600" />
            <div>
              <p className="text-lg font-semibold text-gray-800">Shop address:</p>
              <p className="text-gray-600">Mumbai </p>
            </div>
          </div>
          <div className="flex">
            <FaPhone size={20} className="h-fit mt-1.5 mr-6 text-orange-600" />
            <div>
              <p className="text-lg font-semibold text-gray-800">Phone no:</p>
              <p className="text-gray-600">+91 8369484265</p>
            </div>
          </div>
          <div className="flex">
            <FaEnvelope size={20} className="h-fit mt-1.5 mr-6 text-orange-600" />
            <div>
              <p className="text-lg font-semibold text-gray-800">Email id:</p>
              <p className="text-gray-600">info@Food-Mart.com</p>
            </div>
          </div>
        </div>
        <div className="flex md:flex-row flex-col md:mt-1 gap-8">
          <div className="md:w-1/2">
            <iframe
              src="https://www.google.com/maps/embed?pb=!1m18!1m12!1m3!1d3887.123456789!2d77.594562!3d12.971598!2m3!1f0!2f0!3f0!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0x0%3A0x0!2zMTPCsDU4JzE3LjgiTiA3N8KwMzUnNDAuNSJF!5e0!3m2!1sen!2sin!4v1234567890"
              className="block h-full w-full rounded shadow-lg border min-h-64"
              title="Food-Mart Location"
            ></iframe>
          </div>
          <div className="md:w-1/2">
            <h1 className="text-3xl font-semibold mb-4 text-orange-800">Get In Touch</h1>

            <form onSubmit={handleSubmit} className="space-y-5">
              <div>
                <label className="font-medium text-gray-700">Full name</label>
                <input
                  type="text"
                  name="name"
                  value={formData.name}
                  onChange={handleChange}
                  required
                  className="w-full mt-2 px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-orange-500"
                />
              </div>
              <div>
                <label className="font-medium text-gray-700">Email</label>
                <input
                  type="email"
                  name="email"
                  value={formData.email}
                  onChange={handleChange}
                  required
                  className="w-full mt-2 px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-orange-500"
                />
              </div>
              <div>
                <label className="font-medium text-gray-700">Message</label>
                <textarea
                  name="message"
                  value={formData.message}
                  onChange={handleChange}
                  required
                  className="w-full mt-2 h-36 px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-orange-500"
                ></textarea>
              </div>
              <button
                type="submit"
                disabled={loading}
                className="w-full px-4 py-2 text-white bg-orange-600 hover:bg-orange-700 rounded-lg transition disabled:bg-gray-400"
              >
                {loading ? "Submitting..." : "Submit"}
              </button>
            </form>
          </div>
        </div>
      </div>
    </div>
  );
}

export default Contact;
