## pat-foodie-dev 电商单体项目（仅是电商功能，不包含电商后台管理功能）

此项目的核心功能包含：登陆、注册、搜索、购物车、用户收货地址、订单支付功能等。项目使用的技术有：Spring Boot（2.2.7.RELEASE）、MyBatis、MySQL、Mapper逆向工程、Swagger2、redis

### 一、登陆/注册
这是本项目等基础功能，作为一个平台使用的基本保障。

### 二、整合Swagger2
Swagger2是作为接口文档的功能，其主要是暴露接口的一些属性，方便测试使用。

### 三、页面的一些功能
主要包含：轮播图、商品分类懒加载、首页推荐商品懒加载、商品详情页、商品评论展示与分页

### 四、日志功能
包含整合log4j输出打印SQL和结合aop与日志监控service执行

### 五、搜索功能（根据分类和关键字搜索商品）
单体项目并未整合es，只是简单的sql模糊查询。

### 六、订单功能
包含用户地址，和提交订单功能。

### 七、支付功能
此功能集成了微信支付和支付宝支付功能。

