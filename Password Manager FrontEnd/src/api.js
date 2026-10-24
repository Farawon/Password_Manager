export const API_BASE = "https://caviar-cupped-resilient.ngrok-free.dev";

const TOKEN_KEY = "token";

function headers(extra = {}) {
    return {
        "Content-Type": "application/json",
        "ngrok-skip-browser-warning": "true",
        ...extra,
    };
}

export function getAccessToken() {
    return localStorage.getItem(TOKEN_KEY);
}

export function setAccessToken(token) {
    if (token) {
        localStorage.setItem(TOKEN_KEY, token);
    } else {
        localStorage.removeItem(TOKEN_KEY);
    }
}

export async function refreshAccessToken() {
    const response = await fetch(`${API_BASE}/auth/refresh`, {
        method: "POST",
        credentials: "include",
        headers: headers(),
    });
    if (!response.ok) {
        setAccessToken(null);
        throw new Error("Refresh failed");
    }
    const data = await response.json();
    setAccessToken(data.accessToken);
    return data.accessToken;
}

export async function ensureAccessToken() {
    const existing = getAccessToken();
    if (existing) {
        return existing;
    }
    return refreshAccessToken();
}

export async function apiFetch(path, options = {}) {
    const url = path.startsWith("http") ? path : `${API_BASE}${path}`;
    const requestHeaders = headers(options.headers);
    const token = getAccessToken();
    if (token) {
        requestHeaders.Authorization = `Bearer ${token}`;
    }

    const init = {
        ...options,
        headers: requestHeaders,
        credentials: "include",
    };

    let response = await fetch(url, init);
    const isAuthCall = url.includes("/auth/login")
        || url.includes("/auth/register")
        || url.includes("/auth/refresh");

    if (response.status === 401 && !isAuthCall) {
        const nextToken = await refreshAccessToken();
        requestHeaders.Authorization = `Bearer ${nextToken}`;
        response = await fetch(url, { ...init, headers: requestHeaders });
    }

    return response;
}

export async function logout() {
    try {
        await fetch(`${API_BASE}/auth/logout`, {
            method: "POST",
            credentials: "include",
            headers: headers(),
        });
    }
    finally {
        setAccessToken(null);
        window.location.href = "login.html";
    }
}
