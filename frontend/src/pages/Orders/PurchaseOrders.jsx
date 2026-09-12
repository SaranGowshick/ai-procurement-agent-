import {
  Search,
  SlidersHorizontal,
  Plus,
  MoreHorizontal,
  FileText,
  Clock3,
  CheckCircle2,
  Truck,
  ChevronDown,
  Eye,
  Pencil,
  Copy,
  XCircle,
} from "lucide-react";

import "./PurchaseOrders.css";

const purchaseOrders = [
  {
    id: "PO-2026-1048",
    supplier: "ABC Components",
    requester: "Saran Gowshick",
    items: 24,
    amount: "₹2,48,500",
    created: "12 Sep 2026",
    delivery: "18 Sep 2026",
    status: "In Transit",
  },
  {
    id: "PO-2026-1047",
    supplier: "Global Industries",
    requester: "Arun Kumar",
    items: 18,
    amount: "₹1,86,200",
    created: "11 Sep 2026",
    delivery: "16 Sep 2026",
    status: "Approved",
  },
  {
    id: "PO-2026-1046",
    supplier: "Prime Manufacturing",
    requester: "Saran Gowshick",
    items: 32,
    amount: "₹3,42,800",
    created: "10 Sep 2026",
    delivery: "15 Sep 2026",
    status: "Pending Approval",
  },
  {
    id: "PO-2026-1045",
    supplier: "TechParts India",
    requester: "Vignesh R",
    items: 15,
    amount: "₹98,400",
    created: "09 Sep 2026",
    delivery: "14 Sep 2026",
    status: "Delivered",
  },
  {
    id: "PO-2026-1044",
    supplier: "Metro Engineering",
    requester: "Arun Kumar",
    items: 12,
    amount: "₹76,900",
    created: "08 Sep 2026",
    delivery: "13 Sep 2026",
    status: "Draft",
  },
  {
    id: "PO-2026-1043",
    supplier: "ABC Components",
    requester: "Saran Gowshick",
    items: 27,
    amount: "₹1,64,300",
    created: "07 Sep 2026",
    delivery: "12 Sep 2026",
    status: "Cancelled",
  },
];

const statusClass = {
  "In Transit": "po-status transit",
  Approved: "po-status approved",
  "Pending Approval": "po-status pending",
  Delivered: "po-status delivered",
  Draft: "po-status draft",
  Cancelled: "po-status cancelled",
};

const PurchaseOrders = () => {
  return (
    <div className="purchase-orders-page">
      {/* Header */}
      <div className="po-header">
        <div>
          <p className="po-eyebrow">PROCUREMENT OPERATIONS</p>
          <h1>Purchase Orders</h1>
          <p>
            Create, manage and track purchase orders across your supplier
            network.
          </p>
        </div>

        <button className="create-po-btn">
          <Plus size={17} />
          Create Purchase Order
        </button>
      </div>

      {/* Summary */}
      <div className="po-summary-grid">
        <div className="po-summary-card">
          <div className="po-summary-icon blue">
            <FileText size={19} />
          </div>

          <div>
            <span>Total POs</span>
            <strong>248</strong>
            <small>All purchase orders</small>
          </div>
        </div>

        <div className="po-summary-card">
          <div className="po-summary-icon orange">
            <Clock3 size={19} />
          </div>

          <div>
            <span>Pending Approval</span>
            <strong>24</strong>
            <small>Require action</small>
          </div>
        </div>

        <div className="po-summary-card">
          <div className="po-summary-icon purple">
            <Truck size={19} />
          </div>

          <div>
            <span>In Transit</span>
            <strong>18</strong>
            <small>On the way</small>
          </div>
        </div>

        <div className="po-summary-card">
          <div className="po-summary-icon green">
            <CheckCircle2 size={19} />
          </div>

          <div>
            <span>Completed</span>
            <strong>196</strong>
            <small>Successfully delivered</small>
          </div>
        </div>
      </div>

      {/* Toolbar */}
      <div className="po-toolbar">
        <div className="po-search">
          <Search size={17} />
          <input placeholder="Search PO number, supplier or requester..." />
        </div>

        <button className="po-filter-btn">
          <SlidersHorizontal size={16} />
          Filters
        </button>

        <button className="po-select-btn">
          Status
          <ChevronDown size={15} />
        </button>

        <button className="po-select-btn">
          Supplier
          <ChevronDown size={15} />
        </button>

        <button className="po-select-btn">
          Date
          <ChevronDown size={15} />
        </button>
      </div>

      {/* Table */}
      <section className="po-table-card">
        <div className="po-table-header">
          <div>
            <h2>Purchase Order Register</h2>
            <p>Manage all purchase orders and their current lifecycle status.</p>
          </div>

          <div className="po-header-count">
            <span>248</span> total
          </div>
        </div>

        <div className="po-table-wrapper">
          <table className="po-table">
            <thead>
              <tr>
                <th>PO Number</th>
                <th>Supplier</th>
                <th>Requester</th>
                <th>Items</th>
                <th>PO Amount</th>
                <th>Created</th>
                <th>Expected Delivery</th>
                <th>Status</th>
                <th>Action</th>
              </tr>
            </thead>

            <tbody>
              {purchaseOrders.map((po) => (
                <tr key={po.id}>
                  <td>
                    <div className="po-number-cell">
                      <div className="po-file-icon">
                        <FileText size={15} />
                      </div>

                      <div>
                        <strong>{po.id}</strong>
                        <span>Purchase Order</span>
                      </div>
                    </div>
                  </td>

                  <td>
                    <div className="po-supplier">
                      <div className="po-supplier-avatar">
                        {po.supplier.charAt(0)}
                      </div>

                      <span>{po.supplier}</span>
                    </div>
                  </td>

                  <td>{po.requester}</td>

                  <td>
                    <span className="items-count">{po.items}</span>
                  </td>

                  <td>
                    <strong className="po-amount">{po.amount}</strong>
                  </td>

                  <td>{po.created}</td>

                  <td>{po.delivery}</td>

                  <td>
                    <span className={statusClass[po.status]}>
                      {po.status}
                    </span>
                  </td>

                  <td>
                    <div className="po-actions">
                      <button title="View">
                        <Eye size={15} />
                      </button>

                      <button title="More">
                        <MoreHorizontal size={17} />
                      </button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>

        {/* Footer */}
        <div className="po-table-footer">
          <span>Showing 1–6 of 248 purchase orders</span>

          <div className="po-pagination">
            <button disabled>Previous</button>
            <button className="active">1</button>
            <button>2</button>
            <button>3</button>
            <button>4</button>
            <button>...</button>
            <button>42</button>
            <button>Next</button>
          </div>
        </div>
      </section>

      {/* Approval Section */}
      <div className="po-bottom-grid">
        <section className="po-approval-card">
          <div className="po-bottom-heading">
            <div>
              <h2>Approval Queue</h2>
              <p>Purchase orders waiting for approval.</p>
            </div>

            <span>24 Pending</span>
          </div>

          <div className="approval-item">
            <div className="approval-icon">
              <Clock3 size={16} />
            </div>

            <div>
              <strong>PO-2026-1046</strong>
              <span>Prime Manufacturing · ₹3,42,800</span>
            </div>

            <button>Review</button>
          </div>

          <div className="approval-item">
            <div className="approval-icon">
              <Clock3 size={16} />
            </div>

            <div>
              <strong>PO-2026-1042</strong>
              <span>Global Industries · ₹2,18,400</span>
            </div>

            <button>Review</button>
          </div>

          <div className="approval-item">
            <div className="approval-icon">
              <Clock3 size={16} />
            </div>

            <div>
              <strong>PO-2026-1039</strong>
              <span>TechParts India · ₹1,26,800</span>
            </div>

            <button>Review</button>
          </div>
        </section>

        {/* Workflow */}
        <section className="po-workflow-card">
          <div className="po-bottom-heading">
            <div>
              <h2>PO Lifecycle</h2>
              <p>Current purchase order workflow.</p>
            </div>
          </div>

          <div className="po-workflow">
            <div className="workflow-step completed">
              <div className="workflow-circle">
                <CheckCircle2 size={15} />
              </div>
              <span>Draft</span>
              <strong>12</strong>
            </div>

            <div className="workflow-line"></div>

            <div className="workflow-step active">
              <div className="workflow-circle">
                <Clock3 size={15} />
              </div>
              <span>Approval</span>
              <strong>24</strong>
            </div>

            <div className="workflow-line"></div>

            <div className="workflow-step">
              <div className="workflow-circle">
                <Truck size={15} />
              </div>
              <span>Delivery</span>
              <strong>18</strong>
            </div>

            <div className="workflow-line"></div>

            <div className="workflow-step completed">
              <div className="workflow-circle">
                <CheckCircle2 size={15} />
              </div>
              <span>Completed</span>
              <strong>196</strong>
            </div>
          </div>
        </section>
      </div>
    </div>
  );
};

export default PurchaseOrders;