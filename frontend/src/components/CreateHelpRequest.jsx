import React, { useState } from 'react';
import { submitHelpRequest, MOCK_USER_ID } from '../services/api';
import './HelpCenter.css';

const CreateHelpRequest = ({ onRequestSubmitted }) => {
    const [subject, setSubject] = useState('');
    const [description, setDescription] = useState('');
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);
    const [success, setSuccess] = useState(null);

    const handleSubmit = async (e) => {
        e.preventDefault();
        setLoading(true);
        setError(null);
        setSuccess(null);

        try {
            await submitHelpRequest(MOCK_USER_ID, subject, description);
            setSuccess('Help request submitted successfully!');
            setSubject('');
            setDescription('');
            if (onRequestSubmitted) onRequestSubmitted();
        } catch (err) {
            console.error(err);
            setError('Failed to submit request. Please try again.');
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="card help-form-card">
            <h2>Contact Support</h2>
            <form onSubmit={handleSubmit}>
                <div className="form-group">
                    <label htmlFor="subject">Subject</label>
                    <input
                        type="text"
                        id="subject"
                        value={subject}
                        onChange={(e) => setSubject(e.target.value)}
                        placeholder="What needs help?"
                        required // HTML5 validation checks for value presence
                    />
                </div>
                <div className="form-group">
                    <label htmlFor="description">Description</label>
                    <textarea
                        id="description"
                        value={description}
                        onChange={(e) => setDescription(e.target.value)}
                        placeholder="Describe your issue in detail..."
                        rows="4"
                        required
                    ></textarea>
                </div>
                {error && <div className="error-message">{error}</div>}
                {success && <div className="success-message">{success}</div>}
                <button type="submit" className="btn-primary" disabled={loading}>
                    {loading ? 'Submitting...' : 'Submit Request'}
                </button>
            </form>
        </div>
    );
};

export default CreateHelpRequest;
