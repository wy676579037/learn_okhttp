## Retrofit源码阅读

### 代码地址：https://github.com/wy676579037/learn_okhttp

Retrofit 是由 Square 公司推出的网络请求库， Retrofit 是基于 OkHttp 实现的，它在 OkHttp 现有功能的基础上进行封装，支持通过<u><font size=4 color=#FF69B4>注解</font></u>进行网络请求参数的配置，同时对数据返回后的解析、序列化进行了统一的包装。

###  目标：

+ 掌握Retrofit的实现流程
+ 掌握Retrofit的设计模式

###  方式：项目

+  <u><font size=4 color=#FF69B4>需求</font></u>
+  <u><font size=4 color=#FF69B4>设计</font></u>
+  <u><font size=4 color=#FF69B4>实现</font></u>
+ 测试
+  <u><font size=4 color=#FF69B4>优化</font></u>
+ 上线

---

### 一.需求

+ 1）参数注解化+接口的方式进行Http请求
+ 2）基于OkHttp来做，对上层屏蔽OkHttp
+ 3）支持同步跟异步请求
+ 4）直接返回自定义Model
+ 5）开发的时候预留更换OkHttp的设计

### 二.设计

**使用迭代的模式进行实现**

#### 第一阶段：需求与设计

需求：

+ 1）参数注解化+接口 进行get请求
+ 2）基于OkHttp来做
+ 3）使用OkHttp进行同步请求
+ 4）返回OkHttp的Response

实现方案：

+ 接口：使用动态代理模式
+ 参数注解化：使用方法的运行时注解（get请求类型，请求url）
+ 使用OkHttp进行同步请求：注解转换成Request
+ 返回Okhttp的Response

我们先看下Retrofit的实现效果

---

##### 第一步：定义注解并且定义接口

##### 第二步：动态代理进行接口的实例化

##### 第三步：读取注解

##### 第四步：转换成Request

##### 第五步：使用OkHttp进行同步网络请求



