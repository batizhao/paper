# Paper  ![](https://img.shields.io/badge/build-passing-brightgreen) ![](https://img.shields.io/badge/coverage-100%25-green)

这是一个 Spring Boot 下的单元测试演示项目。

测试框架使用 JUnit、Mockito 和 Hamcrest。

## 环境

* MySQL8
* JDK8
* JUnit4
* Hamcrest2
* Mockito
* JaCoCo

## 注意事项
* 在启动之前，需要创建 paper 数据库（root/password）。
* 这里没有使用单独的数据库做单元测试，直接调用开发、测试库，所以如果是重要数据，要注意测试脏数据的回滚（在测试用例中使用 @Transactional），或者自己恢复。
* 也可以使用单独的内存数据库做单元测试。
* 数据在每次启动时会自动清除再初始化（spring.datasource.initialization-mode=always）。
* 为了省事，这里使用的 JPA，测试方法和 MyBatis 没什么区别。
* 在 DAO、Service、Controller 层都实现了单元测试，类名以 UnitTest 结尾。每层会 Mock 所有依赖。
* 在 Controller 层实现了集成测试，类名以 IntegrationTest 结尾。在启动测试时，会实例化所有上下文。

## 测试报告

这里使用 JaCoCo 生成测试报告，实际使用中，可以集成到 Sonar 质量检查中。

### 本地查看

```
# mvn clean test
```
打开 target/site/jacoco/index.html 可以看到测试报告。

### 检查预设条件

```
# mvn clean verify

Rule violated for package ***: lines covered ratio is 0.8, but expected minimum is 0.9
```
如果小于 minimum 预设值（这里是 90%），会显示上边的错误，构建失败。

## 目标

* 增加 liquibase 接管数据库管理