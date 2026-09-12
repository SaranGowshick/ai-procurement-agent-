import {
  Bot,
  Send,
  Sparkles,
  Package,
  Users,
  IndianRupee,
  Truck,
  BarChart3,
  Plus,
  MessageSquare,
  Clock3,
} from "lucide-react";

import "./Agent.css";

const suggestions = [
  {
    icon: Package,
    title: "Check inventory",
    description: "Check current inventory levels",
  },
  {
    icon: IndianRupee,
    title: "Find savings",
    description: "Identify potential cost savings",
  },
  {
    icon: Users,
    title: "Analyze suppliers",
    description: "Compare supplier performance",
  },
  {
    icon: Truck,
    title: "Track deliveries",
    description: "Check delayed deliveries",
  },
];

const Agent = () => {
  return (
    <div className="agent-page">
      {/* LEFT SIDE */}
      <aside className="agent-sidebar">
        <div className="agent-sidebar-header">
          <div className="agent-logo">
            <Bot size={20} />
          </div>

          <div>
            <strong>ProcureAI</strong>
            <span>AI Procurement Agent</span>
          </div>
        </div>

        <button className="new-chat-btn">
          <Plus size={17} />
          New Chat
        </button>

        <div className="agent-sidebar-section">
          <span className="sidebar-section-title">RECENT CHATS</span>

          <div className="chat-history active">
            <MessageSquare size={15} />
            <div>
              <span>Supplier performance</span>
              <small>Today</small>
            </div>
          </div>

          <div className="chat-history">
            <MessageSquare size={15} />
            <div>
              <span>Inventory analysis</span>
              <small>Yesterday</small>
            </div>
          </div>

          <div className="chat-history">
            <MessageSquare size={15} />
            <div>
              <span>Cost optimization</span>
              <small>Sep 10</small>
            </div>
          </div>

          <div className="chat-history">
            <MessageSquare size={15} />
            <div>
              <span>Delayed purchase orders</span>
              <small>Sep 8</small>
            </div>
          </div>
        </div>

        <div className="agent-sidebar-bottom">
          <div className="agent-status">
            <span></span>
            AI Agent Online
          </div>

          <small>Powered by Procurement Intelligence</small>
        </div>
      </aside>

      {/* RIGHT CHAT AREA */}
      <main className="agent-chat-area">
        {/* Chat Header */}
        <header className="agent-chat-header">
          <div>
            <div className="chat-title">
              <div className="chat-bot-icon">
                <Bot size={19} />
              </div>

              <div>
                <h1>Procurement Agent</h1>
                <span>
                  <span className="online-dot"></span>
                  Online
                </span>
              </div>
            </div>
          </div>

          <div className="agent-header-info">
            <Sparkles size={16} />
            AI-powered procurement analysis
          </div>
        </header>

        {/* SCROLLABLE CHAT */}
        <div className="agent-messages">
          <div className="chat-content">
            {/* Welcome */}
            <div className="welcome-section">
              <div className="welcome-icon">
                <Bot size={27} />
              </div>

              <h2>How can I help with procurement?</h2>

              <p>
                I can analyze suppliers, purchase orders, inventory,
                delivery performance and identify procurement opportunities.
              </p>
            </div>

            {/* Suggestions */}
            <div className="suggestions-grid">
              {suggestions.map((item) => {
                const Icon = item.icon;

                return (
                  <button className="suggestion-card" key={item.title}>
                    <div className="suggestion-icon">
                      <Icon size={18} />
                    </div>

                    <div>
                      <strong>{item.title}</strong>
                      <span>{item.description}</span>
                    </div>
                  </button>
                );
              })}
            </div>

            {/* Example Conversation */}
            <div className="conversation-area">
              <div className="conversation-date">
                <span>Today</span>
              </div>

              <div className="user-message">
                <div className="user-avatar">SG</div>

                <div className="message-bubble user-bubble">
                  Which suppliers have the best performance this month?
                </div>
              </div>

              <div className="ai-message">
                <div className="ai-message-avatar">
                  <Bot size={16} />
                </div>

                <div className="message-content">
                  <div className="message-bubble ai-bubble">
                    <p>
                      Based on the latest procurement data, these are your
                      top-performing suppliers:
                    </p>

                    <div className="supplier-result">
                      <div>
                        <strong>ABC Components</strong>
                        <span>96% performance</span>
                      </div>

                      <strong className="result-score">96%</strong>
                    </div>

                    <div className="supplier-result">
                      <div>
                        <strong>Global Industries</strong>
                        <span>92% performance</span>
                      </div>

                      <strong className="result-score">92%</strong>
                    </div>

                    <div className="supplier-result">
                      <div>
                        <strong>Prime Manufacturing</strong>
                        <span>89% performance</span>
                      </div>

                      <strong className="result-score">89%</strong>
                    </div>

                    <p className="ai-summary">
                      ABC Components is currently the strongest supplier,
                      with excellent delivery reliability and order quality.
                    </p>
                  </div>

                  <span className="message-time">
                    <Clock3 size={11} />
                    Just now
                  </span>
                </div>
              </div>
            </div>

            {/* Extra content to demonstrate scrolling */}
            <div className="conversation-area">
              <div className="user-message">
                <div className="user-avatar">SG</div>

                <div className="message-bubble user-bubble">
                  Can you identify potential savings opportunities?
                </div>
              </div>

              <div className="ai-message">
                <div className="ai-message-avatar">
                  <Bot size={16} />
                </div>

                <div className="message-content">
                  <div className="message-bubble ai-bubble">
                    <p>
                      I found 3 potential optimization opportunities based
                      on your current procurement activity.
                    </p>

                    <div className="saving-result">
                      <div className="saving-icon">
                        <IndianRupee size={15} />
                      </div>

                      <div>
                        <strong>₹72,000</strong>
                        <span>Supplier consolidation</span>
                      </div>
                    </div>

                    <div className="saving-result">
                      <div className="saving-icon">
                        <BarChart3 size={15} />
                      </div>

                      <div>
                        <strong>₹48,500</strong>
                        <span>Bulk order optimization</span>
                      </div>
                    </div>

                    <div className="saving-result">
                      <div className="saving-icon">
                        <Package size={15} />
                      </div>

                      <div>
                        <strong>₹31,200</strong>
                        <span>Inventory optimization</span>
                      </div>
                    </div>
                  </div>

                  <span className="message-time">
                    <Clock3 size={11} />
                    Just now
                  </span>
                </div>
              </div>
            </div>
          </div>
        </div>

        {/* FIXED INPUT */}
        <div className="agent-input-section">
          <div className="agent-input-wrapper">
            <input
              type="text"
              placeholder="Ask your procurement question..."
            />

            <button className="send-btn">
              <Send size={18} />
            </button>
          </div>

          <div className="input-disclaimer">
            <Sparkles size={12} />
            AI can analyze procurement data and provide recommendations.
          </div>
        </div>
      </main>
    </div>
  );
};

export default Agent;