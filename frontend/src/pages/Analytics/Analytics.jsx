import {
  TrendingUp,
  TrendingDown,
  IndianRupee,
  ShoppingCart,
  Users,
  PackageCheck,
  ArrowUpRight,
  ArrowDownRight,
  Download,
  CalendarDays,
} from "lucide-react";

import "./Analytics.css";

const monthlySpend = [
  { month: "Apr", value: 58 },
  { month: "May", value: 72 },
  { month: "Jun", value: 64 },
  { month: "Jul", value: 81 },
  { month: "Aug", value: 76 },
  { month: "Sep", value: 92 },
];

const suppliers = [
  {
    name: "ABC Components",
    orders: 48,
    spend: "₹18.4L",
    performance: 96,
  },
  {
    name: "Global Industries",
    orders: 42,
    spend: "₹15.8L",
    performance: 92,
  },
  {
    name: "Prime Manufacturing",
    orders: 36,
    spend: "₹12.6L",
    performance: 89,
  },
  {
    name: "TechParts India",
    orders: 31,
    spend: "₹9.4L",
    performance: 86,
  },
];

const Analytics = () => {
  return (
    <div className="analytics-page">
      {/* Header */}
      <div className="analytics-header">
        <div>
          <p className="analytics-eyebrow">PROCUREMENT INTELLIGENCE</p>
          <h1>Analytics</h1>
          <p>
            Analyze procurement spend, supplier performance and operational
            efficiency.
          </p>
        </div>

        <div className="analytics-actions">
          <button className="analytics-date-btn">
            <CalendarDays size={16} />
            Last 6 months
          </button>

          <button className="analytics-export-btn">
            <Download size={16} />
            Export Report
          </button>
        </div>
      </div>

      {/* KPI Cards */}
      <div className="analytics-kpi-grid">
        <div className="analytics-kpi-card">
          <div className="analytics-kpi-top">
            <div className="analytics-icon blue">
              <IndianRupee size={20} />
            </div>

            <span className="positive-change">
              <ArrowUpRight size={14} />
              12.8%
            </span>
          </div>

          <span className="analytics-kpi-label">Total Procurement Spend</span>
          <strong>₹84.6L</strong>
          <small>vs ₹75.0L previous period</small>
        </div>

        <div className="analytics-kpi-card">
          <div className="analytics-kpi-top">
            <div className="analytics-icon green">
              <TrendingDown size={20} />
            </div>

            <span className="positive-change">
              <ArrowUpRight size={14} />
              8.4%
            </span>
          </div>

          <span className="analytics-kpi-label">Cost Savings</span>
          <strong>₹4.2L</strong>
          <small>Estimated savings generated</small>
        </div>

        <div className="analytics-kpi-card">
          <div className="analytics-kpi-top">
            <div className="analytics-icon purple">
              <ShoppingCart size={20} />
            </div>

            <span className="positive-change">
              <ArrowUpRight size={14} />
              6.2%
            </span>
          </div>

          <span className="analytics-kpi-label">Purchase Orders</span>
          <strong>248</strong>
          <small>Across 125 suppliers</small>
        </div>

        <div className="analytics-kpi-card">
          <div className="analytics-kpi-top">
            <div className="analytics-icon orange">
              <PackageCheck size={20} />
            </div>

            <span className="negative-change">
              <ArrowDownRight size={14} />
              2.1%
            </span>
          </div>

          <span className="analytics-kpi-label">On-Time Delivery</span>
          <strong>91.4%</strong>
          <small>Target: 95%</small>
        </div>
      </div>

      {/* Main Analytics Grid */}
      <div className="analytics-main-grid">
        {/* Spend Chart */}
        <section className="analytics-card spend-card">
          <div className="analytics-card-header">
            <div>
              <h2>Procurement Spend</h2>
              <p>Monthly procurement spend in lakhs.</p>
            </div>

            <span className="chart-period">₹ Lakhs</span>
          </div>

          <div className="spend-chart">
            <div className="chart-y-axis">
              <span>100</span>
              <span>75</span>
              <span>50</span>
              <span>25</span>
              <span>0</span>
            </div>

            <div className="chart-area">
              <div className="chart-grid-lines">
                <span></span>
                <span></span>
                <span></span>
                <span></span>
                <span></span>
              </div>

              <div className="chart-bars">
                {monthlySpend.map((item) => (
                  <div className="chart-column" key={item.month}>
                    <div
                      className="chart-bar"
                      style={{ height: `${item.value}%` }}
                    >
                      <span>{item.value}</span>
                    </div>

                    <label>{item.month}</label>
                  </div>
                ))}
              </div>
            </div>
          </div>
        </section>

        {/* Category Spend */}
        <section className="analytics-card">
          <div className="analytics-card-header">
            <div>
              <h2>Spend by Category</h2>
              <p>Distribution of procurement spend.</p>
            </div>
          </div>

          <div className="category-list">
            <div className="category-row">
              <div className="category-info">
                <span className="category-dot electronic"></span>
                <span>Electronic Components</span>
              </div>

              <strong>32%</strong>

              <div className="category-progress">
                <span style={{ width: "32%" }}></span>
              </div>
            </div>

            <div className="category-row">
              <div className="category-info">
                <span className="category-dot mechanical"></span>
                <span>Mechanical Parts</span>
              </div>

              <strong>26%</strong>

              <div className="category-progress">
                <span style={{ width: "26%" }}></span>
              </div>
            </div>

            <div className="category-row">
              <div className="category-info">
                <span className="category-dot raw"></span>
                <span>Raw Materials</span>
              </div>

              <strong>21%</strong>

              <div className="category-progress">
                <span style={{ width: "21%" }}></span>
              </div>
            </div>

            <div className="category-row">
              <div className="category-info">
                <span className="category-dot electrical"></span>
                <span>Electrical Components</span>
              </div>

              <strong>13%</strong>

              <div className="category-progress">
                <span style={{ width: "13%" }}></span>
              </div>
            </div>

            <div className="category-row">
              <div className="category-info">
                <span className="category-dot other"></span>
                <span>Others</span>
              </div>

              <strong>8%</strong>

              <div className="category-progress">
                <span style={{ width: "8%" }}></span>
              </div>
            </div>
          </div>
        </section>
      </div>

      {/* Lower Analytics */}
      <div className="analytics-lower-grid">
        {/* Supplier Performance */}
        <section className="analytics-card supplier-performance-card">
          <div className="analytics-card-header">
            <div>
              <h2>Top Supplier Performance</h2>
              <p>Suppliers ranked by procurement performance.</p>
            </div>

            <button className="view-all-btn">View all</button>
          </div>

          <div className="supplier-analytics-list">
            {suppliers.map((supplier, index) => (
              <div className="supplier-analytics-row" key={supplier.name}>
                <div className="supplier-rank">{index + 1}</div>

                <div className="supplier-analytics-name">
                  <strong>{supplier.name}</strong>
                  <span>{supplier.orders} orders</span>
                </div>

                <div className="supplier-spend">
                  <strong>{supplier.spend}</strong>
                  <span>Spend</span>
                </div>

                <div className="supplier-performance">
                  <div className="performance-value">
                    <strong>{supplier.performance}%</strong>
                  </div>

                  <div className="performance-bar">
                    <span
                      style={{ width: `${supplier.performance}%` }}
                    ></span>
                  </div>
                </div>
              </div>
            ))}
          </div>
        </section>

        {/* AI Insight */}
        <section className="analytics-card analytics-ai-card">
          <div className="ai-card-heading">
            <div className="analytics-ai-icon">
              <TrendingUp size={20} />
            </div>

            <div>
              <span>AI INSIGHT</span>
              <h2>Procurement Opportunity</h2>
            </div>
          </div>

          <p className="ai-insight-text">
            Your electronic component spend increased by 18% this month.
            AI analysis identified potential savings of approximately
            ₹72,000 by consolidating orders across 3 suppliers.
          </p>

          <div className="ai-insight-stat">
            <span>Potential Savings</span>
            <strong>₹72,000</strong>
          </div>

          <button className="ai-action-btn">
            Analyze Opportunity
            <ArrowUpRight size={16} />
          </button>
        </section>
      </div>
    </div>
  );
};

export default Analytics;