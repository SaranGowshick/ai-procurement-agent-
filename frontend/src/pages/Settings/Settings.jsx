import {
  User,
  Bell,
  ShieldCheck,
  Database,
  Bot,
  Palette,
  Save,
  CheckCircle2,
} from "lucide-react";

import "./Settings.css";

const Settings = () => {
  return (
    <div className="settings-page">
      <div className="settings-header">
        <div>
          <p className="settings-eyebrow">ADMINISTRATION</p>
          <h1>Settings</h1>
          <p>Manage your procurement platform preferences and configurations.</p>
        </div>

        <button className="settings-save-btn">
          <Save size={17} />
          Save Changes
        </button>
      </div>

      <div className="settings-layout">
        {/* Sidebar */}
        <aside className="settings-sidebar">
          <div className="settings-menu active">
            <User size={18} />
            <span>Profile</span>
          </div>

          <div className="settings-menu">
            <Bell size={18} />
            <span>Notifications</span>
          </div>

          <div className="settings-menu">
            <ShieldCheck size={18} />
            <span>Security</span>
          </div>

          <div className="settings-menu">
            <Bot size={18} />
            <span>AI Agent</span>
          </div>

          <div className="settings-menu">
            <Database size={18} />
            <span>Data & Integrations</span>
          </div>

          <div className="settings-menu">
            <Palette size={18} />
            <span>Appearance</span>
          </div>
        </aside>

        {/* Content */}
        <main className="settings-content">
          {/* Profile */}
          <section className="settings-card">
            <div className="settings-card-header">
              <div>
                <h2>Profile Information</h2>
                <p>Update your account information and profile details.</p>
              </div>
            </div>

            <div className="profile-section">
              <div className="profile-avatar">SG</div>

              <div>
                <h3>Saran Gowshick</h3>
                <p>Software Engineer</p>
                <button className="change-avatar-btn">
                  Change avatar
                </button>
              </div>
            </div>

            <div className="form-grid">
              <div className="form-group">
                <label>First Name</label>
                <input type="text" value="Saran" readOnly />
              </div>

              <div className="form-group">
                <label>Last Name</label>
                <input type="text" value="Gowshick" readOnly />
              </div>

              <div className="form-group">
                <label>Email Address</label>
                <input
                  type="email"
                  value="saran@example.com"
                  readOnly
                />
              </div>

              <div className="form-group">
                <label>Role</label>
                <input
                  type="text"
                  value="Procurement Manager"
                  readOnly
                />
              </div>
            </div>
          </section>

          {/* Notifications */}
          <section className="settings-card">
            <div className="settings-card-header">
              <div>
                <h2>Notifications</h2>
                <p>Choose which procurement updates you want to receive.</p>
              </div>
            </div>

            <div className="settings-option">
              <div>
                <h3>Order Updates</h3>
                <p>Get notified when an order changes status.</p>
              </div>

              <label className="switch">
                <input type="checkbox" defaultChecked />
                <span></span>
              </label>
            </div>

            <div className="settings-option">
              <div>
                <h3>Supplier Alerts</h3>
                <p>Receive alerts about supplier performance and risks.</p>
              </div>

              <label className="switch">
                <input type="checkbox" defaultChecked />
                <span></span>
              </label>
            </div>

            <div className="settings-option">
              <div>
                <h3>AI Recommendations</h3>
                <p>Receive AI-generated procurement recommendations.</p>
              </div>

              <label className="switch">
                <input type="checkbox" defaultChecked />
                <span></span>
              </label>
            </div>
          </section>

          {/* AI */}
          <section className="settings-card ai-settings-card">
            <div className="settings-card-header">
              <div className="ai-title">
                <div className="ai-icon">
                  <Bot size={20} />
                </div>

                <div>
                  <h2>Procurement AI</h2>
                  <p>Configure how your AI procurement assistant operates.</p>
                </div>
              </div>

              <span className="status-badge">
                <CheckCircle2 size={14} />
                Active
              </span>
            </div>

            <div className="form-group">
              <label>AI Recommendation Mode</label>

              <select defaultValue="balanced">
                <option value="balanced">Balanced</option>
                <option value="cost">Cost Optimization</option>
                <option value="speed">Delivery Speed</option>
                <option value="quality">Supplier Quality</option>
              </select>
            </div>
          </section>
        </main>
      </div>
    </div>
  );
};

export default Settings;