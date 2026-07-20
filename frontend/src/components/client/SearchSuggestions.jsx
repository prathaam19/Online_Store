import { useEffect, useMemo, useState } from "react";

const defaultSuggestions = [
  "Biryani",
  "Pizza",
  "Pasta",
  "Burger",
  "Salad",
  "Dessert",
  "Coffee",
  "Chicken",
  "Seafood",
  "Beverage",
];

function buildSuggestions(query, products = []) {
  const normalizedQuery = query.trim().toLowerCase();

  if (!normalizedQuery) {
    return defaultSuggestions.slice(0, 6);
  }

  const terms = [
    ...defaultSuggestions,
    ...products.flatMap((item) => [item.name, item.category?.name]),
  ].filter(Boolean);

  const suggestions = [];
  const seen = new Set();

  terms.forEach((term) => {
    const normalizedTerm = term.toLowerCase();
    if (!normalizedTerm.includes(normalizedQuery)) {
      return;
    }

    if (!seen.has(normalizedTerm)) {
      seen.add(normalizedTerm);
      suggestions.push(term);
    }
  });

  if (suggestions.length === 0) {
    return [`Search for “${query.trim()}”`];
  }

  return suggestions.slice(0, 6);
}

function SearchSuggestions({
  value,
  onChange,
  onSelect,
  placeholder,
  inputClassName = "",
  wrapperClassName = "",
  products = [],
}) {
  const [isOpen, setIsOpen] = useState(false);

  const suggestions = useMemo(() => buildSuggestions(value, products), [value, products]);

  useEffect(() => {
    if (value.trim()) {
      setIsOpen(true);
    } else {
      setIsOpen(false);
    }
  }, [value]);

  const handleSelect = (suggestion) => {
    onSelect?.(suggestion);
    setIsOpen(false);
  };

  return (
    <div className={`relative flex-1 ${wrapperClassName}`}>
      <input
        type="text"
        placeholder={placeholder}
        value={value}
        onChange={onChange}
        onFocus={() => value.trim() && setIsOpen(true)}
        onBlur={() => setTimeout(() => setIsOpen(false), 120)}
        className={`w-full bg-transparent outline-none ${inputClassName}`}
      />

      {isOpen && suggestions.length > 0 && (
        <div className="absolute left-0 right-0 top-full z-50 mt-2 rounded-xl border border-orange-200 bg-white shadow-xl">
          <div className="px-3 py-2 text-[11px] font-semibold uppercase tracking-[0.2em] text-orange-500">
            Smart suggestions
          </div>
          <ul className="max-h-56 overflow-y-auto py-1">
            {suggestions.map((suggestion, index) => (
              <li key={`${suggestion}-${index}`}>
                <button
                  type="button"
                  onMouseDown={(event) => event.preventDefault()}
                  onClick={() => handleSelect(suggestion)}
                  className="flex w-full items-center justify-between px-3 py-2 text-left text-sm text-gray-700 transition hover:bg-orange-50 hover:text-orange-700"
                >
                  <span>{suggestion}</span>
                  <span className="text-xs text-orange-400">↵</span>
                </button>
              </li>
            ))}
          </ul>
        </div>
      )}
    </div>
  );
}

export default SearchSuggestions;
