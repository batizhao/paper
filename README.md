# Paper

这是一个 Spring Boot 下的单元测试演示项目。

测试框架使用 JUnit4 和 Hamcrest2。

## 环境

* MySQL8
* JDK8

## 注意事项
* 在启动之前，需要创建 paper 数据库（root/password）。
* 这里没有使用单独的数据库做单元测试，直接调用开发、测试库，所以如果是重要数据，要注意测试脏数据的回滚（在测试用例中使用 @Transactional），或者自己恢复。
* 也可以使用单独的内存数据库做单元测试。
* 数据在每次启动时会自动清除再初始化（spring.datasource.initialization-mode=always）。
* 为了省事，这里使用的 JPA，测试方法和 MyBatis 没什么区别。

## 目标

* 增加 service、controller 的单元测试
* 增加 liquibase 接管数据库管理