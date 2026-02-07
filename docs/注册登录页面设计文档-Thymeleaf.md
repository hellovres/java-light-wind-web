# 注册登录页面设计文档（Thymeleaf）

## 1. 设计目标

基于 Spring MVC + Thymeleaf 实现用户注册和登录页面，与现有 JWT 认证系统集成，提供：
- 响应式设计，支持移动端和桌面端
- 简洁现代的UI风格
- 良好的用户体验和交互反馈
- 与后端 JWT 认证无缝对接

---

## 2. 技术栈

| 组件 | 技术选型 | 版本 |
|------|---------|------|
| 模板引擎 | Thymeleaf | 3.1 |
| 前端框架 | Bootstrap | 5.3 |
| 图标库 | Bootstrap Icons | 1.11 |
| CSS预处理 | - | - |
| JS | Vanilla JS | ES6+ |
| 认证方式 | JWT | - |

---

## 3. 依赖配置

### 3.1 Maven 依赖

```xml
<!-- Thymeleaf -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-thymeleaf</artifactId>
</dependency>

<!-- Thymeleaf Layout Dialect (布局继承) -->
<dependency>
    <groupId>nz.net.ultraq.thymeleaf</groupId>
    <artifactId>thymeleaf-layout-dialect</artifactId>
</dependency>
```

### 3.2 配置文件

```yaml
spring:
  thymeleaf:
    prefix: classpath:/templates/
    suffix: .html
    mode: HTML
    encoding: UTF-8
    cache: false  # 开发环境关闭缓存
    cacheable: true
```

---

## 4. 页面架构

### 4.1 页面目录结构

```
src/main/resources/templates/
├── layout/                           # 布局模板
│   └── base.html                    # 基础布局
├── auth/                             # 认证相关页面
│   ├── login.html                   # 登录页面
│   └── register.html                # 注册页面
└── fragments/                         # 片段模板
    ├── header.html                  # 页头
    └── footer.html                  # 页脚

src/main/resources/static/
├── css/
│   └── auth.css                     # 认证页面样式
├── js/
│   └── auth.js                      # 认证页面脚本
└── img/                             # 图片资源
```

### 4.2 页面关系图

```
┌─────────────────────────────────────────────────┐
│              base.html (基础布局)                  │
│  ┌─────────────┬──────────────────────────────┐   │
│  │ header.html │     内容区域 (th:fragment)    │   │
│  │ (页头片段)   │                              │   │
│  └─────────────┴──────────────────────────────┘   │
│              footer.html (页脚片段)                 │
└─────────────────────────────────────────────────┘
                          ↑
         ┌────────────────┼────────────────┐
         │                │                │
    login.html    register.html   (其他页面)
    (登录页面)      (注册页面)
```

---

## 5. 页面设计规范

### 5.1 设计原则

| 原则 | 说明 |
|------|------|
| 简洁 | 界面简洁，去除冗余元素 |
| 一致 | 视觉风格、交互行为保持一致 |
| 响应 | 适配不同屏幕尺寸 |
| 反馈 | 提供明确的操作反馈 |
| 容错 | 友好的错误提示 |

### 5.2 视觉风格

- **配色方案**
  - 主色调：蓝色（`#0d6efd`）
  - 背景色：浅灰（`#f8f9fa`）
  - 文字色：深灰（`#212529`）
  - 错误色：红色（`#dc3545`）
  - 成功色：绿色（`#198754`）

- **字体**
  - 中文字体：思源黑体、微软雅黑
  - 英文字体：Inter、Helvetica Neue、Arial
  - 字号：14px-16px（正文），12px（辅助信息）

- **间距**
  - 卡片内边距：24px
  - 元素间距：16px
  - 页边距：40px（桌面）、20px（移动端）

### 5.3 组件规范

#### 输入框
```
- 高度：40px
- 圆角：4px
- 边框：1px solid #dee2e6
- 聚焦边框：2px solid #0d6efd
```

#### 按钮
```
- 高度：40px
- 圆角：4px
- 主按钮：蓝色背景
- 次按钮：白色背景，蓝色边框
```

#### 卡片
```
- 背景白色
- 圆角：8px
- 阴影：0 2px 12px rgba(0,0,0,0.08)
```

---

## 6. 页面详细设计

### 6.1 基础布局 (base.html)

**功能**：
- 定义页面的公共结构
- 引入静态资源
- 提供内容插槽

**结构**：
```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org"
      xmlns:layout="http://www.ultraq.net.nz/thymeleaf/layout">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title th:text="${title} ?: 'Light Wind Auth'">Light Wind Auth</title>
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Bootstrap Icons -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.0/font/bootstrap-icons.css" rel="stylesheet">
    <!-- 自定义样式 -->
    <link th:href="@{/css/auth.css}" rel="stylesheet">
</head>
<body>
    <!-- 页头 -->
    <header th:replace="fragments/header :: header"></header>

    <!-- 内容区域 -->
    <main layout:fragment="content">
        <!-- 子页面内容将在这里渲染 -->
    </main>

    <!-- 页脚 -->
    <footer th:replace="fragments/footer :: footer"></footer>

    <!-- Bootstrap JS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <!-- 自定义脚本 -->
    <script th:src="@{/js/auth.js}"></script>
    <!-- 页面特定脚本 -->
    <th:block layout:fragment="scripts"></th:block>
</body>
</html>
```

### 6.2 登录页面 (login.html)

**功能**：
- 用户名密码登录
- 记住我选项
- 错误提示
- 跳转注册页面

**布局**：
```
┌─────────────────────────────────────────┐
│         Light Wind Auth (Logo)          │
├─────────────────────────────────────────┤
│                                         │
│           欢迎回来                       │
│        登录您的账号                     │
│                                         │
│  ┌─────────────────────────────────┐   │
│  │  👤  用户名                       │   │
│  │  ┌───────────────────────────┐   │   │
│  │  │                           │   │   │
│  │  └───────────────────────────┘   │   │
│  │                                   │   │
│  │  🔒  密码                         │   │
│  │  ┌───────────────────────────┐   │   │
│  │  │ ••••••••                   │   │   │
│  │  └───────────────────────────┘   │   │
│  │                                   │   │
│  │  ☑️  记住我    忘记密码？         │   │
│  │                                   │   │
│  │  ┌───────────────────────────┐   │   │
│  │  │      登 录                  │   │   │
│  │  └───────────────────────────┘   │   │
│  └─────────────────────────────────┘   │
│                                         │
│      还没有账号？立即注册               │
│                                         │
└─────────────────────────────────────────┘
```

**字段说明**：
| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| username | text | 是 | 用户名 |
| password | password | 是 | 密码 |
| remember | checkbox | 否 | 记住登录状态 |
| error | 提示信息 | - | 登录错误时显示 |

**交互流程**：
```
1. 用户输入用户名和密码
2. 点击"登录"按钮
3. 前端验证输入不为空
4. 发送POST请求到 /api/auth/login
5. 后端验证，返回JWT Token
6. 前端保存Token到localStorage
7. 跳转到首页或返回的redirectUrl
8. 如果登录失败，显示错误提示
```

### 6.3 注册页面 (register.html)

**功能**：
- 用户注册
- 密码确认
- 用户名格式验证
- 跳转登录页面

**布局**：
```
┌─────────────────────────────────────────┐
│         Light Wind Auth (Logo)          │
├─────────────────────────────────────────┤
│                                         │
│           创建账号                       │
│         填写信息完成注册                 │
│                                         │
│  ┌─────────────────────────────────┐   │
│  │  👤  用户名                       │   │
│  │  ┌───────────────────────────┐   │   │
│  │  │                           │   │   │
│  │  └───────────────────────────┘   │   │
│  │  用户名长度3-50个字符             │   │
│  │                                   │   │
│  │  🔒  密码                         │   │
│  │  ┌───────────────────────────┐   │   │
│  │  │ ••••••••                   │   │   │
│  │  └───────────────────────────┘   │   │
│  │  密码至少6个字符                  │   │
│  │                                   │   │
│  │  🔒  确认密码                     │   │
│  │  ┌───────────────────────────┐   │   │
│  │  │ ••••••••                   │   │   │
│  │  └───────────────────────────┘   │   │
│  │  两次密码必须一致                  │   │
│  │                                   │   │
│  │  ┌───────────────────────────┐   │   │
│  │  │      注 册                  │   │   │
│  │  └───────────────────────────┘   │   │
│  └─────────────────────────────────┘   │
│                                         │
│      已有账号？立即登录                 │
│                                         │
└─────────────────────────────────────────┘
```

**字段说明**：
| 字段 | 类型 | 必填 | 验证规则 |
|------|------|------|----------|
| username | text | 是 | 3-50个字符，字母数字下划线 |
| password | password | 是 | 至少6个字符 |
| confirmPassword | password | 是 | 与password一致 |

**交互流程**：
```
1. 用户输入用户名和密码
2. 输入确认密码
3. 点击"注册"按钮
4. 前端验证：
   - 用户名格式
   - 密码长度
   - 两次密码是否一致
5. 发送POST请求到 /api/auth/register
6. 后端验证并创建用户
7. 注册成功后自动登录，跳转到首页
8. 如果注册失败，显示错误提示
```

---

## 7. 前端交互逻辑

### 7.1 表单提交处理

```javascript
// 通用表单提交函数
async function submitForm(formId, apiUrl, redirectUrl) {
    const form = document.getElementById(formId);
    const formData = new FormData(form);
    const data = Object.fromEntries(formData);

    try {
        // 禁用提交按钮
        const submitBtn = form.querySelector('button[type="submit"]');
        submitBtn.disabled = true;
        submitBtn.textContent = '处理中...';

        // 发送请求
        const response = await fetch(apiUrl, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(data)
        });

        const result = await response.json();

        if (response.ok) {
            // 保存Token
            if (result.data.token) {
                localStorage.setItem('token', result.data.token);
                localStorage.setItem('refreshToken', result.data.refreshToken);
            }

            // 显示成功提示
            showToast(result.message || '操作成功', 'success');

            // 跳转
            setTimeout(() => {
                window.location.href = redirectUrl || '/';
            }, 1000);
        } else {
            // 显示错误提示
            showToast(result.message || '操作失败', 'error');
        }
    } catch (error) {
        showToast('网络错误，请稍后重试', 'error');
    } finally {
        // 恢复按钮状态
        const submitBtn = form.querySelector('button[type="submit"]');
        submitBtn.disabled = false;
        submitBtn.textContent = submitBtn.dataset.originalText || '提交';
    }
}
```

### 7.2 Token 管理

```javascript
// Token存储
const TokenManager = {
    // 保存Token
    save(token, refreshToken) {
        localStorage.setItem('token', token);
        localStorage.setItem('refreshToken', refreshToken);
    },

    // 获取Token
    getToken() {
        return localStorage.getItem('token');
    },

    // 刷新Token
    async refresh() {
        const refreshToken = localStorage.getItem('refreshToken');
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
    }
};
```

### 7.3 请求拦截器

```javascript
// 统一请求拦截
async function fetchWithAuth(url, options = {}) {
    let token = TokenManager.getToken();

    // 添加Authorization头
    options.headers = {
        ...options.headers,
        'Authorization': token ? `Bearer ${token}` : ''
    };

    let response = await fetch(url, options);

    // 401错误，尝试刷新Token
    if (response.status === 401 && !url.includes('/api/auth/refresh')) {
        const newToken = await TokenManager.refresh();
        if (newToken) {
            options.headers['Authorization'] = `Bearer ${newToken}`;
            response = await fetch(url, options);
        }
    }

    return response;
}
```

---

## 8. 与后端集成

### 8.1 Spring Security 配置调整

```java
@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
            .csrf(AbstractHttpConfigurer::disable)
            .sessionManagement(session -> session
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .authorizeHttpRequests(auth -> auth
                    .requestMatchers(
                        "/",
                        "/login",
                        "/register",
                        "/css/**",
                        "/js/**",
                        "/img/**"
                    ).permitAll()
                    .requestMatchers("/api/auth/register", "/api/auth/login").permitAll()
                    .anyRequest().authenticated()
            )
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
}
```

### 8.2 页面路由

| URL | 说明 | 认证要求 |
|-----|------|----------|
| / | 首页 | 需要登录 |
| /login | 登录页面 | 无需认证 |
| /register | 注册页面 | 无需认证 |
| /api/auth/login | 登录API | 无需认证 |
| /api/auth/register | 注册API | 无需认证 |
| /api/auth/me | 获取用户信息 | 需要认证 |
| /logout | 登出 | 需要认证 |

### 8.3 Controller 添加页面路由

```java
@Controller
@RequiredArgsConstructor
public class PageController {

    @GetMapping("/")
    public String index(Authentication authentication, Model model) {
        if (authentication == null) {
            return "redirect:/login";
        }

        model.addAttribute("username", authentication.getName());
        return "index";
    }

    @GetMapping("/login")
    public String loginPage(@RequestParam(required = false) String error, Model model) {
        if (error != null) {
            model.addAttribute("error", "用户名或密码错误");
        }
        return "auth/login";
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("registerRequest", new RegisterRequest());
        return "auth/register";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        // 清除本地Token
        return "redirect:/login";
    }
}
```

---

## 9. 样式设计 (auth.css)

```css
/* 全局样式 */
body {
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    min-height: 100vh;
    display: flex;
    align-items: center;
    justify-content: center;
    font-family: 'Inter', -apple-system, BlinkMacSystemFont, 'Segoe UI', sans-serif;
}

/* 认证卡片 */
.auth-card {
    background: white;
    border-radius: 12px;
    box-shadow: 0 8px 24px rgba(0, 0, 0, 0.1);
    max-width: 420px;
    width: 100%;
    padding: 40px;
}

/* 标题样式 */
.auth-title {
    font-size: 24px;
    font-weight: 600;
    color: #212529;
    text-align: center;
    margin-bottom: 8px;
}

.auth-subtitle {
    font-size: 14px;
    color: #6c757d;
    text-align: center;
    margin-bottom: 32px;
}

/* Logo样式 */
.auth-logo {
    text-align: center;
    margin-bottom: 24px;
}

.auth-logo h1 {
    font-size: 32px;
    font-weight: 700;
    background: linear-gradient(135deg, #667eea, #764ba2);
    -webkit-background-clip: text;
    -webkit-text-fill-color: transparent;
    background-clip: text;
}

/* 表单样式 */
.form-floating label {
    color: #6c757d;
    font-size: 14px;
}

.form-control:focus {
    border-color: #667eea;
    box-shadow: 0 0 0 0.2rem rgba(102, 126, 234, 0.25);
}

/* 按钮样式 */
.btn-primary {
    background: linear-gradient(135deg, #667eea, #764ba2);
    border: none;
    padding: 12px;
    font-size: 16px;
    font-weight: 500;
}

.btn-primary:hover {
    background: linear-gradient(135deg, #5a6fd6, #6942a3);
}

.btn-primary:disabled {
    opacity: 0.6;
    cursor: not-allowed;
}

/* 错误提示 */
.alert-error {
    background-color: #f8d7da;
    border-color: #f5c6cb;
    color: #721c24;
    padding: 12px;
    border-radius: 6px;
    margin-bottom: 20px;
}

/* 链接样式 */
.auth-link {
    display: block;
    text-align: center;
    margin-top: 16px;
    font-size: 14px;
    color: #667eea;
    text-decoration: none;
}

.auth-link:hover {
    color: #5a6fd6;
    text-decoration: underline;
}

/* 响应式 */
@media (max-width: 576px) {
    .auth-card {
        padding: 24px;
        margin: 20px;
    }

    .auth-title {
        font-size: 20px;
    }
}

/* 加载动画 */
.loading-spinner {
    width: 16px;
    height: 16px;
    border: 2px solid rgba(255, 255, 255, 0.3);
    border-top-color: white;
    border-radius: 50%;
    animation: spin 0.8s linear infinite;
    display: inline-block;
    margin-right: 8px;
}

@keyframes spin {
    to { transform: rotate(360deg); }
}
```

---

## 10. 用户体验优化

### 10.1 表单验证

```javascript
// 登录表单验证
function validateLoginForm() {
    const username = document.getElementById('username').value.trim();
    const password = document.getElementById('password').value;

    if (!username) {
        showToast('请输入用户名', 'error');
        return false;
    }

    if (!password) {
        showToast('请输入密码', 'error');
        return false;
    }

    return true;
}

// 注册表单验证
function validateRegisterForm() {
    const username = document.getElementById('username').value.trim();
    const password = document.getElementById('password').value;
    const confirmPassword = document.getElementById('confirmPassword').value;

    if (!username) {
        showToast('请输入用户名', 'error');
        return false;
    }

    if (username.length < 3 || username.length > 50) {
        showToast('用户名长度必须在3-50个字符之间', 'error');
        return false;
    }

    if (!/^[a-zA-Z0-9_]+$/.test(username)) {
        showToast('用户名只能包含字母、数字和下划线', 'error');
        return false;
    }

    if (!password) {
        showToast('请输入密码', 'error');
        return false;
    }

    if (password.length < 6) {
        showToast('密码长度至少为6个字符', 'error');
        return false;
    }

    if (password !== confirmPassword) {
        showToast('两次密码不一致', 'error');
        return false;
    }

    return true;
}
```

### 10.2 Toast 提示

```javascript
// 显示Toast提示
function showToast(message, type = 'success') {
    // 移除已存在的Toast
    const existingToast = document.querySelector('.custom-toast');
    if (existingToast) {
        existingToast.remove();
    }

    // 创建新Toast
    const toast = document.createElement('div');
    toast.className = `custom-toast toast-${type}`;
    toast.innerHTML = `
        <i class="bi bi-${type === 'success' ? 'check-circle' : 'x-circle'}"></i>
        <span>${message}</span>
    `;

    document.body.appendChild(toast);

    // 3秒后自动消失
    setTimeout(() => {
        toast.classList.add('fade-out');
        setTimeout(() => toast.remove(), 300);
    }, 3000);
}
```

---

## 11. 实施步骤

### Step 1: 添加依赖
- 在 pom.xml 中添加 Thymeleaf 依赖

### Step 2: 配置 Thymeleaf
- 在 application.yml 中配置模板引擎

### Step 3: 创建布局模板
- base.html
- header.html
- footer.html

### Step 4: 创建认证页面
- login.html
- register.html

### Step 5: 创建静态资源
- auth.css
- auth.js

### Step 6: 修改 Security 配置
- 允许访问静态资源
- 配置页面路由

### Step 7: 创建页面控制器
- PageController.java
- 添加页面路由

### Step 8: 测试
- 测试页面渲染
- 测试表单提交
- 测试认证流程

---

## 12. 后续扩展

### 12.1 功能扩展

- [ ] 添加"记住我"功能持久化
- [ ] 添加忘记密码页面
- [ ] 添加密码强度指示器
- [ ] 添加邮箱验证码注册
- [ ] 添加OAuth2第三方登录

### 12.2 UI优化

- [ ] 添加页面加载动画
- [ ] 添加表单验证实时反馈
- [ ] 优化移动端体验
- [ ] 添加深色模式

### 12.3 安全增强

- [ ] 添加CSRF保护
- [ ] 添加验证码防刷
- [ ] 添加登录日志记录
- [ ] 添加IP限流

---

## 附录

### A. Thymeleaf 常用语法

| 语法 | 说明 | 示例 |
|------|------|------|
| th:text | 设置文本 | `<span th:text="${title}"></span>` |
| th:value | 设置值 | `<input th:value="${username}">` |
| th:href | 设置链接 | `<a th:href="@{/login}"></a>` |
| th:action | 表单提交 | `<form th:action="@{/login}">` |
| th:if | 条件渲染 | `<div th:if="${error}"></div>` |
| th:each | 循环 | `<li th:each="item : ${list}"></li>` |
| th:fragment | 定义片段 | `<div th:fragment="header"></div>` |
| th:replace | 替换片段 | `<div th:replace="fragments/header"></div>` |
| th:class | 条件类名 | `<div th:class="${active} ? 'active' : ''"></div>` |

### B. Bootstrap 5 常用类名

| 类名 | 说明 |
|------|------|
| .container | 容器 |
| .card | 卡片 |
| .form-control | 表单控件 |
| .btn-primary | 主按钮 |
| .form-floating | 浮动标签表单 |
| .alert | 提示框 |
| .d-flex | Flex布局 |
| .justify-content-center | 居中对齐 |
| .mt-3, .mb-3 | 间距 |

### C. 项目依赖版本

- Spring Boot: 3.2.0
- Thymeleaf: 3.1.2
- Thymeleaf Layout Dialect: 3.2.1
- Bootstrap: 5.3.0
- Bootstrap Icons: 1.11.0
