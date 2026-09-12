import { BrowserRouter, Routes, Route } from "react-router-dom";
import { useState } from "react";

import Navbar from "./components/navigation/Navbar";

import Dashboard from "./pages/Dashboard/Dashboard";
import Suppliers from "./pages/Suppliers/Suppliers";
import Agent from "./pages/Agent/Agent";
import PurchaseOrders from "./pages/Orders/PurchaseOrders";
import Analytics from "./pages/Analytics/Analytics";
import Settings from "./pages/Settings/Settings";
// import PurchaseOrders from "./pages/PurchaseOrders/PurchaseOrders";

import "./styles/global.css";

function App() {
  const [sidebarOpen, setSidebarOpen] = useState(false);

  return (
    <BrowserRouter basename="/ai-procurement-agent-">
      <div className={`app-layout ${sidebarOpen ? "sidebar-open" : ""}`}>
        
        <Navbar
          sidebarOpen={sidebarOpen}
          setSidebarOpen={setSidebarOpen}
        />

        <main className="main-content">
          <Routes>
            <Route path="/" element={<Dashboard />} />
            <Route path="/suppliers" element={<Suppliers />} />
            <Route path="/agent" element={<Agent />} />
            <Route path="/orders" element={<PurchaseOrders />} />
            <Route path="/analytics" element={<Analytics />} />
            <Route path="/settings" element={<Settings />} />
            <Route path="/purchase-orders" element={<PurchaseOrders />} />
          </Routes>
        </main>

      </div>
    </BrowserRouter>
  );
}

export default App;