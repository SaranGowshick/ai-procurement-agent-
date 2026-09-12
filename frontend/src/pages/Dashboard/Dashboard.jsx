import {
  ArrowUpRight,
  ArrowDownRight,
  Package,
  Users,
  Clock3,
  IndianRupee,
  CheckCircle2,
  AlertCircle,
  Truck,
  Sparkles,
  ArrowRight,
} from "lucide-react";

import "./Dashboard.css";

function Dashboard() {
  const stats = [
    {
      title: "Total Suppliers",
      value: "125",
      change: "8.2%",
      description: "vs last month",
      positive: true,
      icon: Users,
      iconClass: "blue",
    },
    {
      title: "Active Purchase Orders",
      value: "48",
      change: "5.4%",
      description: "vs last month",
      positive: true,
      icon: Package,
      iconClass: "purple",
    },
    {
      title: "Pending Requests",
      value: "12",
      change: "2.1%",
      description: "need attention",
      positive: false,
      icon: Clock3,
      iconClass: "orange",
    },
    {
      title: "Estimated Savings",
      value: "₹4.2L",
      change: "12.8%",
      description: "this financial year",
      positive: true,
      icon: IndianRupee,
      iconClass: "green",
    },
  ];

  const activities = [
    {
      icon: CheckCircle2,
      iconClass: "success",
      title: "Purchase order approved",
      description: "PO-10245 was approved successfully",
      time: "10 minutes ago",
    },
    {
      icon: Truck,
      iconClass: "blue",
      title: "Delivery received",
      description: "1,250 units received from ABC Components",
      time: "32 minutes ago",
    },
    {
      icon: AlertCircle,
      iconClass: "warning",
      title: "Supplier delay detected",
      description: "Global Industries delivery delayed by 2 days",
      time: "1 hour ago",
    },
    {
      icon: Users,
      iconClass: "purple",
      title: "New supplier added",
      description: "Prime Manufacturing added to supplier network",
      time: "2 hours ago",
    },
  ];

  const suppliers = [
    {
      name: "ABC Components",
      category: "Electronic Components",
      score: 96,
    },
    {
      name: "Global Industries",
      category: "Mechanical Parts",
      score: 92,
    },
    {
      name: "Prime Manufacturing",
      category: "Raw Materials",
      score: 89,
    },
    {
      name: "TechParts India",
      category: "Electrical Components",
      score: 86,
    },
  ];

  const chartData = [
    { month: "Apr", value: 42 },
    { month: "May", value: 58 },
    { month: "Jun", value: 49 },
    { month: "Jul", value: 72 },
    { month: "Aug", value: 65 },
    { month: "Sep", value: 88 },
  ];

  return (
    <div className="dashboard-page">

      {/* ================= HEADER ================= */}

      <header className="dashboard-header">

        <div>
          <div className="dashboard-eyebrow">
            PROCUREMENT OVERVIEW
          </div>

          <h1>Good morning, Saran</h1>

          <p>
            Here's what's happening with your procurement operations today.
          </p>
        </div>

        <div className="dashboard-date">
          <span>Today</span>
          <strong>12 September 2026</strong>
        </div>

      </header>


      {/* ================= KPI CARDS ================= */}

      <section className="stats-grid">

        {stats.map((stat) => {
          const Icon = stat.icon;

          return (
            <div className="stat-card" key={stat.title}>

              <div className="stat-top">

                <div className={`stat-icon ${stat.iconClass}`}>
                  <Icon size={19} />
                </div>

                <div
                  className={
                    stat.positive
                      ? "stat-change positive"
                      : "stat-change negative"
                  }
                >
                  {stat.positive ? (
                    <ArrowUpRight size={14} />
                  ) : (
                    <ArrowDownRight size={14} />
                  )}

                  {stat.change}
                </div>

              </div>

              <div className="stat-title">
                {stat.title}
              </div>

              <div className="stat-value">
                {stat.value}
              </div>

              <div className="stat-description">
                {stat.description}
              </div>

            </div>
          );
        })}

      </section>


      {/* ================= MAIN GRID ================= */}

      <section className="dashboard-main-grid">

        {/* PROCUREMENT CHART */}

        <div className="dashboard-card chart-card">

          <div className="card-header">

            <div>
              <h2>Procurement Overview</h2>

              <p>
                Purchase order activity over the last 6 months
              </p>
            </div>

            <button className="period-button">
              Last 6 months
            </button>

          </div>


          <div className="chart-wrapper">

            <div className="chart-y-axis">
              <span>100</span>
              <span>75</span>
              <span>50</span>
              <span>25</span>
              <span>0</span>
            </div>

            <div className="chart-area">

              <div className="chart-grid-lines">
                <span />
                <span />
                <span />
                <span />
                <span />
              </div>

              <div className="bars">

                {chartData.map((item) => (
                  <div className="bar-column" key={item.month}>

                    <div className="bar-value">
                      {item.value}
                    </div>

                    <div
                      className="chart-bar"
                      style={{
                        height: `${item.value}%`,
                      }}
                    />

                    <span className="bar-label">
                      {item.month}
                    </span>

                  </div>
                ))}

              </div>

            </div>

          </div>

        </div>


        {/* RECENT ACTIVITY */}

        <div className="dashboard-card activity-card">

          <div className="card-header">

            <div>
              <h2>Recent Activity</h2>

              <p>
                Latest procurement updates
              </p>
            </div>

            <button className="view-all-button">
              View all
            </button>

          </div>


          <div className="activity-list">

            {activities.map((activity) => {
              const Icon = activity.icon;

              return (
                <div className="activity-item" key={activity.title}>

                  <div
                    className={`activity-icon ${activity.iconClass}`}
                  >
                    <Icon size={16} />
                  </div>

                  <div className="activity-content">

                    <strong>
                      {activity.title}
                    </strong>

                    <p>
                      {activity.description}
                    </p>

                    <span>
                      {activity.time}
                    </span>

                  </div>

                </div>
              );
            })}

          </div>

        </div>

      </section>


      {/* ================= BOTTOM GRID ================= */}

      <section className="dashboard-bottom-grid">

        {/* SUPPLIER PERFORMANCE */}

        <div className="dashboard-card supplier-card">

          <div className="card-header">

            <div>
              <h2>Supplier Performance</h2>

              <p>
                Performance across your supplier network
              </p>
            </div>

            <button className="view-all-button">
              View suppliers
            </button>

          </div>


          <div className="supplier-list">

            {suppliers.map((supplier) => (
              <div
                className="supplier-row"
                key={supplier.name}
              >

                <div className="supplier-avatar">
                  {supplier.name.charAt(0)}
                </div>

                <div className="supplier-info">

                  <strong>
                    {supplier.name}
                  </strong>

                  <span>
                    {supplier.category}
                  </span>

                </div>

                <div className="supplier-performance">

                  <div className="performance-value">
                    {supplier.score}%
                  </div>

                  <div className="performance-bar">
                    <span
                      style={{
                        width: `${supplier.score}%`,
                      }}
                    />
                  </div>

                </div>

              </div>
            ))}

          </div>

        </div>


        {/* AI INSIGHT */}

        <div className="ai-insight-card">

          <div className="ai-card-glow" />

          <div className="ai-card-content">

            <div className="ai-label">
              <Sparkles size={15} />
              AI PROCUREMENT INSIGHT
            </div>

            <h2>
              Potential savings detected
            </h2>

            <div className="ai-saving">
              ₹1.8L
            </div>

            <p>
              Our AI identified an opportunity to consolidate
              three upcoming purchase orders and reduce supplier
              costs.
            </p>

            <div className="ai-recommendation">

              <div className="recommendation-icon">
                <Package size={17} />
              </div>

              <div>
                <strong>
                  3 purchase orders
                </strong>

                <span>
                  can be consolidated
                </span>
              </div>

            </div>

            <button className="ai-action">
              Review recommendation
              <ArrowRight size={16} />
            </button>

          </div>

        </div>

      </section>

    </div>
  );
}

export default Dashboard;