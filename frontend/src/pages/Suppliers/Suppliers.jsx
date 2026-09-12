import {
  Search,
  SlidersHorizontal,
  Plus,
  MoreHorizontal,
  TrendingUp,
  Package,
  Clock3,
  AlertTriangle,
} from "lucide-react";

import "./Suppliers.css";

function Suppliers() {
  const suppliers = [
    {
      name: "ABC Components",
      code: "SUP-1001",
      category: "Electronic Components",
      location: "Chennai",
      orders: 18,
      performance: 96,
      status: "Active",
    },
    {
      name: "Global Industries",
      code: "SUP-1002",
      category: "Mechanical Parts",
      location: "Pune",
      orders: 14,
      performance: 92,
      status: "Active",
    },
    {
      name: "Prime Manufacturing",
      code: "SUP-1003",
      category: "Raw Materials",
      location: "Bangalore",
      orders: 11,
      performance: 89,
      status: "Active",
    },
    {
      name: "TechParts India",
      code: "SUP-1004",
      category: "Electrical Components",
      location: "Chennai",
      orders: 9,
      performance: 86,
      status: "Active",
    },
    {
      name: "Metro Engineering",
      code: "SUP-1005",
      category: "Fabricated Parts",
      location: "Hyderabad",
      orders: 7,
      performance: 78,
      status: "Review",
    },
  ];

  return (
    <div className="suppliers-page">

      {/* HEADER */}

      <header className="suppliers-header">

        <div>
          <div className="suppliers-eyebrow">
            SUPPLIER MANAGEMENT
          </div>

          <h1>Suppliers</h1>

          <p>
            Manage your supplier network and monitor performance.
          </p>
        </div>

        <button className="add-supplier-button">
          <Plus size={17} />
          Add Supplier
        </button>

      </header>


      {/* SUMMARY */}

      <section className="supplier-summary">

        <div className="supplier-summary-card">

          <div className="summary-icon blue">
            <Package size={18} />
          </div>

          <div>
            <span>Total Suppliers</span>
            <strong>125</strong>
          </div>

          <small>+8 this month</small>

        </div>


        <div className="supplier-summary-card">

          <div className="summary-icon green">
            <TrendingUp size={18} />
          </div>

          <div>
            <span>Active Suppliers</span>
            <strong>118</strong>
          </div>

          <small>94.4% active</small>

        </div>


        <div className="supplier-summary-card">

          <div className="summary-icon orange">
            <Clock3 size={18} />
          </div>

          <div>
            <span>Under Review</span>
            <strong>5</strong>
          </div>

          <small>Needs attention</small>

        </div>


        <div className="supplier-summary-card">

          <div className="summary-icon purple">
            <AlertTriangle size={18} />
          </div>

          <div>
            <span>At Risk</span>
            <strong>2</strong>
          </div>

          <small>Delivery issues</small>

        </div>

      </section>


      {/* TABLE CARD */}

      <section className="suppliers-table-card">

        <div className="supplier-toolbar">

          <div className="supplier-search">
            <Search size={17} />

            <input
              type="text"
              placeholder="Search suppliers..."
            />
          </div>

          <div className="supplier-filters">

            <button className="filter-button">
              <SlidersHorizontal size={15} />
              Filters
            </button>

            <select className="status-filter">
              <option>All Status</option>
              <option>Active</option>
              <option>Review</option>
              <option>At Risk</option>
            </select>

          </div>

        </div>


        <div className="table-wrapper">

          <table className="suppliers-table">

            <thead>
              <tr>
                <th>Supplier</th>
                <th>Category</th>
                <th>Location</th>
                <th>Orders</th>
                <th>Performance</th>
                <th>Status</th>
                <th></th>
              </tr>
            </thead>

            <tbody>

              {suppliers.map((supplier) => (
                <tr key={supplier.code}>

                  <td>
                    <div className="supplier-name-cell">

                      <div className="supplier-table-avatar">
                        {supplier.name.charAt(0)}
                      </div>

                      <div>
                        <strong>{supplier.name}</strong>
                        <span>{supplier.code}</span>
                      </div>

                    </div>
                  </td>


                  <td>
                    <span className="category-text">
                      {supplier.category}
                    </span>
                  </td>


                  <td>
                    <span className="location-text">
                      {supplier.location}
                    </span>
                  </td>


                  <td>
                    <strong className="order-count">
                      {supplier.orders}
                    </strong>
                  </td>


                  <td>

                    <div className="table-performance">

                      <div className="performance-top">
                        <span>{supplier.performance}%</span>
                      </div>

                      <div className="table-progress">
                        <span
                          style={{
                            width: `${supplier.performance}%`,
                          }}
                        />
                      </div>

                    </div>

                  </td>


                  <td>

                    <span
                      className={
                        supplier.status === "Active"
                          ? "supplier-status active"
                          : "supplier-status review"
                      }
                    >
                      <span className="status-dot" />
                      {supplier.status}
                    </span>

                  </td>


                  <td>
                    <button className="supplier-menu">
                      <MoreHorizontal size={18} />
                    </button>
                  </td>

                </tr>
              ))}

            </tbody>

          </table>

        </div>


        {/* FOOTER */}

        <div className="suppliers-table-footer">

          <span>
            Showing <strong>1–5</strong> of <strong>125</strong> suppliers
          </span>

          <div className="pagination">
            <button disabled>Previous</button>
            <button className="page-active">1</button>
            <button>2</button>
            <button>3</button>
            <button>Next</button>
          </div>

        </div>

      </section>

    </div>
  );
}

export default Suppliers;