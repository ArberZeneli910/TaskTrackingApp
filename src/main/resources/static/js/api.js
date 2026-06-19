const API_BASE = 'http://localhost:8000/api';

function authHeaders()
{
    const token = getAuthToken();
    return token ? { 'Authorization': `Bearer ${token}` } : {};
}

async function apiGet(url) {
    const response = await fetch(`${API_BASE}${url}`,
{
        headers: authHeaders()
    });
    if (!response.ok) {
        throw new Error(`Request failed: ${response.status}`);
    }
    return response.json();
}

async function apiPost(url, body)
{
    const response = await fetch(`${API_BASE}${url}`,
{
        method: 'POST',
        headers: { 'Content-Type': 'application/json', ...authHeaders() },
        body: JSON.stringify(body)
    });
    if (!response.ok)
    {
        const error = await response.json().catch(() => ({ message: 'Request failed' }));
        throw new Error(error.message || `Request failed: ${response.status}`);
    }
    return response.json();
}

async function apiPut(url, body)
{
    const response = await fetch(`${API_BASE}${url}`,
{
        method: 'PUT',
        headers: { 'Content-Type': 'application/json', ...authHeaders() },
        body: JSON.stringify(body)
    });
    if (!response.ok)
    {
        const error = await response.json().catch(() => ({ message: 'Request failed' }));
        throw new Error(error.message || `Request failed: ${response.status}`);
    }
    return response.json();
}

async function apiDelete(url)
{
    const response = await fetch(`${API_BASE}${url}`,
{
        method: 'DELETE',
        headers: authHeaders()
    });
    if (!response.ok)
    {
        throw new Error(`Request failed: ${response.status}`);
    }
}

function getCurrentUserId()
{
    return localStorage.getItem('userId');
}

function setCurrentUserId(id)
{
    localStorage.setItem('userId', id);
}

function getAuthToken()
{
    return localStorage.getItem('authToken');
}

function setAuthToken(token)
{
    localStorage.setItem('authToken', token);
}

function logout()
{
    localStorage.removeItem('userId');
    localStorage.removeItem('authToken');
}