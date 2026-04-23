import axios from 'axios';

const API_URL = 'http://localhost:8080/help';

// Create axios instance
const api = axios.create({
    baseURL: API_URL,
});

export const submitHelpRequest = async (userId, subject, description) => {
    // Backend expects RequestParams: userId, subject, description
    // Post as form data or params? 
    // Looking at HelpController: @RequestParam Long userId, @RequestParam String subject...
    // It expects query parameters or form-urlencoded, not JSON body by default for @RequestParam without @RequestBody.
    // However, Axios POST sends JSON by default.
    // To send as params with POST, we can use URLSearchParams or pass params config.

    const params = new URLSearchParams();
    params.append('userId', userId);
    params.append('subject', subject);
    params.append('description', description);

    const response = await api.post('', params);
    return response.data;
};

export const getUserHelpHistory = async (userId) => {
    const response = await api.get(`/user/${userId}`);
    return response.data;
};

export const getAllHelpRequests = async () => {
    const response = await api.get('/admin');
    return response.data;
};

export const replyToHelpRequest = async (adminId, helpRequestId, message) => {
    const params = new URLSearchParams();
    params.append('adminId', adminId);
    params.append('helpRequestId', helpRequestId);
    params.append('message', message);

    const response = await api.post('/admin/reply', params);
    return response.data;
};

/* 
    Mock User IDs for demonstration since we don't have a full auth system running in frontend.
    In a real app, these would come from local storage or context after login.
*/
export const MOCK_USER_ID = 11; // Registered Farmer ID
export const MOCK_ADMIN_ID = 12; // Registered Admin ID
