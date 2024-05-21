# BI

## 修订内容

- 在前端引入脚本 `/de2api/init.js`
- 支持通过配置环境变量，重写API URL到新的API服务器
- 支持 URL 传入 user.token 进行单点登录

## 环境变量

```shell
# JWT加密密钥, 默认：83d923c9f1d8fcaa46cae0ed2aaa81b5
DE_JWT_SECRET=
# Cloud API 服务器
VITE_CLOUD_API_URL="https://hub.aws.steedos.cn/bi"
# 重写的API清单，用逗号分隔
VITE_CLOUD_API_LIST="/sysParameter/ui,/user/info"
```