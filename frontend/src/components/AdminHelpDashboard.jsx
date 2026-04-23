import React, { useEffect, useState } from 'react';
import { getAllHelpRequests, replyToHelpRequest, MOCK_ADMIN_ID } from '../services/api';
import './HelpCenter.css';

const AdminHelpDashboard = () => {
    const [requests, setRequests] = useState([]);
    const [loading, setLoading] = useState(true);
    const [replyMessage, setReplyMessage] = useState('');
    const [activeRequestId, setActiveRequestId] = useState(null);

    const fetchRequests = async () => {
        setLoading(true);
        try {
            const data = await getAllHelpRequests();
            // Sort: Pending first, then by date
            const sorted = data.sort((a, b) => {
                if (a.status === b.status) {
                    return new Date(b.createdAt) - new Date(a.createdAt);
                }
                return a.status === 'PENDING' ? -1 : 1;
            });
            setRequests(sorted);
        } catch (err) {
            console.error("Failed to load requests", err);
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        fetchRequests();
    }, []);

    const handleReplySubmit = async (e, helpRequestId) => {
        e.preventDefault();
        if (!replyMessage.trim()) return;

        try {
            await replyToHelpRequest(MOCK_ADMIN_ID, helpRequestId, replyMessage);
            alert('Reply sent successfully');
            setReplyMessage('');
            setActiveRequestId(null);
            fetchRequests(); // Refresh list
        } catch (err) {
            console.error("Failed to send reply", err);
            alert('Failed to send reply');
        }
    };

    if (loading) return <div className="container">Loading dashboard...</div>;

    return (
        <div className="container">
            <h1>Admin Help Dashboard</h1>
            <div className="help-grid">
                {requests.map((req) => (
                    <div key={req.id} className="card">
                        <div className="request-header">
                            <span className="request-date">ID: {req.id} • {new Date(req.createdAt).toLocaleDateString()}</span>
                            <span className={`status-badge ${req.status === 'RESOLVED' ? 'status-resolved' : 'status-pending'}`}>
                                {req.status}
                            </span>
                        </div>
                        <h3>{req.subject}</h3>
                        <p><strong>User ID:</strong> {req.user?.id} ({req.role})</p>
                        <p>{req.description}</p>

                        {req.status === 'PENDING' && (
                            <div className="action-area">
                                {activeRequestId === req.id ? (
                                    <form onSubmit={(e) => handleReplySubmit(e, req.id)}>
                                        <textarea
                                            value={replyMessage}
                                            onChange={(e) => setReplyMessage(e.target.value)}
                                            placeholder="Type your reply..."
                                            rows="3"
                                            required
                                        />
                                        <div style={{ marginTop: '10px' }}>
                                            <button type="submit" className="btn-primary">Send Reply</button>
                                            <button
                                                type="button"
                                                className="btn-text"
                                                onClick={() => setActiveRequestId(null)}
                                                style={{ marginLeft: '10px', background: 'none', border: 'none', cursor: 'pointer', textDecoration: 'underline' }}
                                            >
                                                Cancel
                                            </button>
                                        </div>
                                    </form>
                                ) : (
                                    <button
                                        className="btn-primary"
                                        onClick={() => setActiveRequestId(req.id)}
                                    >
                                        Reply
                                    </button>
                                )}
                            </div>
                        )}
                    </div>
                ))}
            </div>
        </div>
    );
};

export default AdminHelpDashboard;
