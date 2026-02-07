# Light Wind Auth - 最小Session登录系统

基于Spring Boot + Session的最小化登录认证系统，使用内存存储，无需数据库。

## 技术栈

- Spring Boot 3.2
- Spring Security 6
- Session 认证
- BCrypt 密码加密
- 内存存储（ConcurrentHashMap）
- Thymeleaf 3.1 (模板引擎)
- Bootstrap 5.3 (前端框架)

## 功能特性

- ✅ 用户注册
- ✅ 用户名+密码登录
- ✅ Session 认证
- ✅ 获取当前用户信息
- ✅ 用户登出

## 项目结构

```
src/main/java/com/lightwind/web/
├── controller/
│   ├── AuthController.java           # 认证API控制器
│   └── PageController.java           # 页面控制器
├── service/
│   ├── AuthService.java              # 认证服务接口
│   └── AuthServiceImpl.java          # 认证服务实现
├── store/                            # 内存存储
│   └── InMemoryUserStore.java        # 用户数据存储
├── model/
│   ├── User.java                     # 用户实体
│   ├── dto/                          # 数据传输对象
│   │   └── RegisterRequest.java
│   └── vo/                           # 视图对象
│       └── UserInfoVO.java
├── config/
│   └── SecurityConfig.java           # Spring Security配置
└── exception/
    ├── AuthenticationException.java  # 认证异常
    ├── UserAlreadyExistsException.java # 用户已存在异常
    └── GlobalExceptionHandler.java   # 全局异常处理器
```

## 快速开始

### 前置要求

- JDK 17 或更高版本
- Maven 3.6 或更高版本

### 运行项目

```bash
# 编译项目
mvn clean compile

# 运行项目
mvn spring-boot:run

# 或者先打包再运行
mvn clean package
java -jar target/java-light-wind-web-1.0.0.jar
```

项目启动后，访问地址：`http://localhost:8080`

## 测试账号

系统初始化时会自动创建以下测试账号：

| 用户名 | 密码 |
|--------|------|
| admin  | admin123 |
| user   | admin123 |

## API 接口

### 1. 用户注册

```http
POST /api/auth/register
Content-Type: application/json

{
  "username": "newuser",
  "password": "password123"
}
```

**响应示例：**

```json
{
  "code": 200,
  "message": "注册成功",
  "data": null,
  "timestamp": 1707200000000
}
```

### 2. 用户登录

```http
POST /api/auth/login
Content-Type: application/x-www-form-urlencoded

username=admin&password=admin123
```

**登录成功后重定向到首页**

### 3. 获取当前用户信息

```http
GET /api/auth/me
Cookie: JSESSIONID=xxx
```

**响应示例：**

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "id": 1,
    "username": "admin"
  },
  "timestamp": 1707200000000
}
```

### 4. 用户登出

```http
POST /api/auth/logout
Cookie: JSESSIONID=xxx
```

**登出成功后重定向到登录页**

## 使用 cURL 测试

### 注册新用户

```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"username": "testuser", "password": "password123"}' \
  -c cookies.txt
```

### 登录

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "username=admin&password=admin123" \
  -c cookies.txt \
  -L
```

### 获取用户信息

```bash
curl -X GET http://localhost:8080/api/auth/me \
  -b cookies.txt
```

### 登出

```bash
curl -X POST http://localhost:8080/api/auth/logout \
  -b cookies.txt \
  -L
```

## 配置说明

### application.yml

```yaml
# Session 配置
session:
  timeout: 3600  # 1 hour (seconds)
```

## 注意事项

### 内存存储限制

- ⚠️ 应用重启后所有数据会丢失
- ⚠️ 不适合生产环境
- ⚠️ 仅适用于开发、测试、演示

### 线程安全

- 使用 `ConcurrentHashMap` 保证线程安全
- 适用于单机部署
- 多实例部署会导致数据不一致

### 后续扩展

如需持久化存储，可以：

1. 添加 Spring Data JPA 依赖
2. 配置数据库（PostgreSQL、MySQL等）
3. 替换 `InMemoryUserStore` 为 `UserRepository`

## 安全建议

1. **使用HTTPS**：在生产环境中启用HTTPS传输
2. **定期更新依赖**：保持Spring Boot和相关依赖为最新版本
3. **添加验证码**：防止暴力破解登录
4. **实现限流**：防止恶意请求攻击
5. **Session 安全**：配置 HTTPS-only Cookie，防止 Session 固定攻击

## 相关文档

- [Spring Security 官方文档](https://docs.spring.io/spring-security/reference/)

## License

MIT
