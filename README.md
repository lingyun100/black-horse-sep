# “醇香塘”加盟服务平台

## 加盟合作服务

### 快速构建

```shell
cd ./black-horse-sep
mvn clean install
```

### 本地运行
#### 启动 PostgreSQL
```shell
cd ./black-horse-sep
docker compose up
```
#### 启动程序
```shell
mvn spring-boot:run
```

本地依赖环境：

- Docker 20.10.21
- OpenJDK 18.0.2

本地初始化数据 `./docker/postgres/init.sql`

### 清理 docker compose
```shell
cd ./black-horse-sep
docker compose down
```
### Swagger

http://localhost:8080/swagger-ui/index.html
