import {
  LayoutDashboard,
  Users,
  ShoppingCart,
  BarChart3,
  Bot,
  Settings,
  Package
} from "lucide-react";

import { NavLink } from "react-router-dom";

import "./Navbar.css";

function Navbar({ sidebarOpen, setSidebarOpen }) {
  const menuItems = [
    {
      label: "Dashboard",
      path: "/",
      icon: LayoutDashboard
    },
    {
      label: "Suppliers",
      path: "/suppliers",
      icon: Users
    },
    {
      label: "Orders",
      path: "/orders",
      icon: ShoppingCart
    },
    {
      label: "Purchase Orders",
      path: "/purchase-orders",
      icon: Package
    },
    {
      label: "Analytics",
      path: "/analytics",
      icon: BarChart3
    },
    {
      label: "AI Agent",
      path: "/agent",
      icon: Bot
    },
    {
      label: "Settings",
      path: "/settings",
      icon: Settings
    }
  ];

  return (
    <>
      {/* Always keep the hover trigger */}
      <div
        className="sidebar-hover-zone"
        onMouseEnter={() => setSidebarOpen(true)}
      />

      <aside
        className={`navbar-sidebar ${
          sidebarOpen ? "navbar-sidebar-open" : ""
        }`}
        onMouseLeave={() => setSidebarOpen(false)}
      >
        <div className="navbar-logo">
          <div className="logo-icon">
            P
          </div>

          <div className="logo-text">
            <h2>ProcureAI</h2>
            <span>Procurement Platform</span>
          </div>
        </div>

        <div className="navbar-section-title">
          WORKSPACE
        </div>

        <nav className="navbar-menu">
          {menuItems.map((item) => {
            const Icon = item.icon;

            return (
              <NavLink
                key={item.path}
                to={item.path}
                className={({ isActive }) =>
                  `navbar-link ${isActive ? "active" : ""}`
                }
              >
                <Icon size={19} strokeWidth={2} />

                <span>{item.label}</span>
              </NavLink>
            );
          })}
        </nav>

        <div className="navbar-bottom">
          <div className="agent-status">
            <span className="status-dot" />

            <div>
              <strong>AI Agent Online</strong>
              <small>Ready to assist</small>
            </div>
          </div>
        </div>
      </aside>
    </>
  );
}

export default Navbar;