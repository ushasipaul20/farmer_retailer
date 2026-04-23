import React, { useState } from 'react';
import { BrowserRouter as Router, Routes, Route, Link, useLocation } from 'react-router-dom';
import CreateHelpRequest from './components/CreateHelpRequest';
import HelpHistory from './components/HelpHistory';
import AdminHelpDashboard from './components/AdminHelpDashboard';
import './components/HelpCenter.css';

const Navigation = () => {
  const location = useLocation();

  return (
    <nav>
      <div className="nav-content">
        <Link to="/" style={{ fontSize: '1.2rem', fontWeight: 'bold', color: '#2E7D32', textDecoration: 'none' }}>
          AgriHelp Center
        </Link>
        <div className="nav-links">
          <Link to="/" className={location.pathname === '/' ? 'active' : ''}>Farmer/Retailer Support</Link>
          <Link to="/admin" className={location.pathname === '/admin' ? 'active' : ''}>Admin Dashboard</Link>
        </div>
      </div>
    </nav>
  );
};

const UserPortal = () => {
  const [refreshHistory, setRefreshHistory] = useState(0);

  return (
    <div className="container">
      <h1>Help & Support</h1>
      <p style={{ marginBottom: '2rem', color: '#666' }}>
        Have a question or issue? Submit a request below and our team will get back to you.
      </p>

      <div className="help-grid">
        <div>
          <h3 style={{ marginBottom: '1rem' }}>Submit a Request</h3>
          <CreateHelpRequest onRequestSubmitted={() => setRefreshHistory(prev => prev + 1)} />
        </div>

        <div>
          {/* History Section */}
          <HelpHistory refreshTrigger={refreshHistory} />
        </div>
      </div>
    </div>
  );
};

function App() {
  return (
    <Router>
      <Navigation />
      <Routes>
        <Route path="/" element={<UserPortal />} />
        <Route path="/admin" element={<AdminHelpDashboard />} />
      </Routes>
    </Router>
  );
}

export default App;
