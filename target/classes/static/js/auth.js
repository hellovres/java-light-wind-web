// ==================== Token管理器 ====================
const TokenManager = {
    // 保存Token
    save(token, refreshToken) {
        localStorage.setItem('token', token);
        localStorage.setItem('refreshToken', refreshToken);
        localStorage.setItem('tokenTime', Date.now());
    },

    // 获取Token
    getToken() {
        return localStorage.getItem('token');
    },

    // 获取RefreshToken
    getRefreshToken() {
        return localStorage.getItem('refreshToken');
    },

    // 检查Token是否有效
    isValid() {
        const token = this.getToken();
        if (!token) return false;

        try {
            const payload = this.parseToken(token);
            const exp = payload.exp * 1000; // 转换为毫秒
            return Date.now() < exp;
        } catch (e) {
            return false;
        }
    },

    // 解析Token（不验证签名）
    parseToken(token) {
        const base64Url = token.split('.')[1];
        const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
        const jsonPayload = decodeURIComponent(window.atob(base64).split('').map(function(c) {
            return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
        }).join(''));

        return JSON.parse(jsonPayload);
    },

    // 刷新Token
    async refresh() {
        const refreshToken = this.getRefreshToken();
        if (!refreshToken) return null;

        try {
            const response = await fetch('/api/auth/refresh', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ refreshToken })
            });

            if (response.ok) {
                const result = await response.json();
                this.save(result.data.token, refreshToken);
                return result.data.token;
            }
        } catch (error) {
            console.error('Token刷新失败:', error);
        }

        return null;
    },

    // 清除Token
    clear() {
        localStorage.removeItem('token');
        localStorage.removeItem('refreshToken');
        localStorage.removeItem('tokenTime');
    },

    // 获取用户信息
    getUserInfo() {
        const token = this.getToken();
        if (!token) return null;

        try {
            const payload = this.parseToken(token);
            return {
                username: payload.sub,
                userId: payload.userId
            };
        } catch (e) {
            return null;
        }
    }
};

// ==================== 统一请求 ====================
async function fetchWithAuth(url, options = {}) {
    let token = TokenManager.getToken();

    // 添加Authorization头
    options.headers = {
        ...options.headers,
        'Content-Type': 'application/json',
        'Authorization': token ? `Bearer ${token}` : ''
    };

    let response = await fetch(url, options);

    // 401错误，尝试刷新Token
    if (response.status === 401 && !url.includes('/api/auth/refresh')) {
        const newToken = await TokenManager.refresh();
        if (newToken) {
            options.headers['Authorization'] = `Bearer ${newToken}`;
            response = await fetch(url, options);
        } else {
            // Token刷新失败，跳转登录页
            TokenManager.clear();
            window.location.href = '/login?error=token_expired';
            return response;
        }
    }

    return response;
}

// ==================== 提交登录 ====================
async function submitLogin(username, password) {
    const loginBtn = document.getElementById('loginBtn');
    const originalText = loginBtn.textContent;

    // 禁用按钮，显示加载中
    loginBtn.disabled = true;
    loginBtn.innerHTML = '<span class="loading-spinner"></span>登录中...';

    try {
        const response = await fetch('/api/auth/login', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ username, password })
        });

        const result = await response.json();

        if (response.ok) {
            // 保存Token
            TokenManager.save(result.data.token, result.data.refreshToken);

            // 显示成功提示
            showToast(result.message || '登录成功', 'success');

            // 跳转到首页
            setTimeout(() => {
                window.location.href = '/';
            }, 1000);
        } else {
            // 显示错误提示
            showToast(result.message || '用户名或密码错误', 'error');
        }
    } catch (error) {
        console.error('登录错误:', error);
        showToast('网络错误，请稍后重试', 'error');
    } finally {
        // 恢复按钮状态
        loginBtn.disabled = false;
        loginBtn.textContent = originalText;
    }
}

// ==================== 提交注册 ====================
async function submitRegister(username, password) {
    const registerBtn = document.getElementById('registerBtn');
    const originalText = registerBtn.textContent;

    // 禁用按钮，显示加载中
    registerBtn.disabled = true;
    registerBtn.innerHTML = '<span class="loading-spinner"></span>注册中...';

    try {
        const response = await fetch('/api/auth/register', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ username, password })
        });

        const result = await response.json();

        if (response.ok) {
            // 保存Token
            TokenManager.save(result.data.token, result.data.refreshToken);

            // 显示成功提示
            showToast(result.message || '注册成功', 'success');

            // 跳转到首页
            setTimeout(() => {
                window.location.href = '/';
            }, 1000);
        } else {
            // 显示错误提示
            showToast(result.message || '注册失败', 'error');
        }
    } catch (error) {
        console.error('注册错误:', error);
        showToast('网络错误，请稍后重试', 'error');
    } finally {
        // 恢复按钮状态
        registerBtn.disabled = false;
        registerBtn.textContent = originalText;
    }
}

// ==================== 登出 ====================
async function logout() {
    try {
        const token = TokenManager.getToken();

        if (token) {
            // 调用登出API
            await fetch('/api/auth/logout', {
                method: 'POST',
                headers: {
                    'Authorization': `Bearer ${token}`
                }
            });
        }

        // 清除本地Token
        TokenManager.clear();

        // 显示提示
        showToast('已退出登录', 'success');

        // 跳转到登录页
        setTimeout(() => {
            window.location.href = '/login';
        }, 500);
    } catch (error) {
        console.error('登出错误:', error);
        // 即使出错也要清除Token并跳转
        TokenManager.clear();
        window.location.href = '/login';
    }
}

// ==================== Toast提示 ====================
function showToast(message, type = 'success') {
    // 移除已存在的Toast
    const existingToast = document.querySelector('.custom-toast');
    if (existingToast) {
        existingToast.remove();
    }

    // 创建新Toast
    const toast = document.createElement('div');
    toast.className = `custom-toast toast-${type}`;

    const icon = type === 'success' ? 'check-circle' :
                 type === 'error' ? 'x-circle' : 'info-circle';

    toast.innerHTML = `
        <i class="bi bi-${icon}"></i>
        <span>${message}</span>
    `;

    document.body.appendChild(toast);

    // 3秒后自动消失
    setTimeout(() => {
        toast.classList.add('fade-out');
        setTimeout(() => toast.remove(), 300);
    }, 3000);
}

// ==================== 页面加载初始化 ====================
document.addEventListener('DOMContentLoaded', function() {
    // 检查Token是否过期
    if (!TokenManager.isValid() && TokenManager.getToken()) {
        // Token已过期，尝试刷新
        TokenManager.refresh().then(newToken => {
            if (!newToken) {
                TokenManager.clear();
                console.warn('Token已过期且无法刷新');
            }
        });
    }

    // 如果在登录/注册页面，已登录则跳转首页
    const currentPath = window.location.pathname;
    if ((currentPath === '/login' || currentPath === '/register') && TokenManager.isValid()) {
        window.location.href = '/';
    }

    // 如果在首页，未登录则跳转登录页（可选）
    // if (currentPath === '/' && !TokenManager.isValid()) {
    //     window.location.href = '/login';
    // }
});

// ==================== 工具函数 ====================

// 格式化时间
function formatTime(timestamp) {
    const date = new Date(timestamp);
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0');
    const hours = String(date.getHours()).padStart(2, '0');
    const minutes = String(date.getMinutes()).padStart(2, '0');
    const seconds = String(date.getSeconds()).padStart(2, '0');
    return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
}

// 防抖函数
function debounce(func, wait) {
    let timeout;
    return function executedFunction(...args) {
        const later = () => {
            clearTimeout(timeout);
            func(...args);
        };
        clearTimeout(timeout);
        timeout = setTimeout(later, wait);
    };
}

// 节流函数
function throttle(func, limit) {
    let inThrottle;
    return function(...args) {
        if (!inThrottle) {
            func.apply(this, args);
            inThrottle = true;
            setTimeout(() => inThrottle = false, limit);
        }
    };
}
