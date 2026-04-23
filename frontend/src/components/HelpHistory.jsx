import React, { useEffect, useState } from 'react';
import { getUserHelpHistory, MOCK_USER_ID } from '../services/api';
import './HelpCenter.css';

const HelpHistory = ({ refreshTrigger }) => {
    const [requests, setRequests] = useState([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const fetchHistory = async () => {
            try {
                const data = await getUserHelpHistory(MOCK_USER_ID);
                // Sort by date descending
                const sorted = data.sort((a, b) => new Date(b.createdAt) - new Date(a.createdAt));
                setRequests(sorted);
            } catch (err) {
                console.error("Failed to load history", err);
            } finally {
                setLoading(false);
            }
        };

        fetchHistory();
    }, [refreshTrigger]);

    if (loading) return <div>Loading history...</div>;

    return (
        <div className="card">
            <h2>My Request History</h2>
            {requests.length === 0 ? (
                <p>No help requests found.</p>
            ) : (
                <div className="request-list">
                    {requests.map((req) => (
                        <div key={req.id} className="request-item">
                            <div className="request-header">
                                <h3>{req.subject}</h3>
                                <span className={`status-badge ${req.status === 'RESOLVED' ? 'status-resolved' : 'status-pending'}`}>
                                    {req.status}
                                </span>
                            </div>
                            <div className="request-date">
                                {new Date(req.createdAt).toLocaleString()}
                            </div>
                            <p>{req.description}</p>

                            {/* Check if there is a response - In a real app we might fetch responses separately or include them in the DTO */}
                            {/* Assuming the Backend might not be returning responses joined yet, or we need to handle it.
                                Looking at HelpController, it returns HelpRequest. 
                                HelpRequest entity has no mapping to HelpResponse (HelpResponse has link to HelpRequest).
                                So we might not see the reply here unless we update the backend to Include it or fetch it.
                                For now, relying on status.
                            */}
                            {req.status === 'RESOLVED' && (
                                <div className="admin-reply">
                                    <strong>Support Team:</strong>
                                    <p>Your issue has been resolved. (Check your email or call us for more details if not visible here)</p>
                                    {/* Ideally we should show the actual reply message here */}
                                </div>
                            )}
                        </div>
                    ))}
                </div>
            )}
        </div>
    );
};

export default HelpHistory;
