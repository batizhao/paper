# Paper  ![](https://img.shields.io/badge/build-passing-brightgreen) ![](https://img.shields.io/badge/coverage-100%25-green)

这是一个 Spring Boot 下的单元测试演示项目。

在 DAO、Service、Controller 层都实现了单元测试，类名以 UnitTest 结尾。每层会 Mock 所有依赖。

在 Controller 层实现了集成测试，类名以 IntegrationTest 结尾。在启动测试时，会实例化所有上下文。

JaCoCo 不支持接口，所以 JPA DAO 没有被统计进去。

> 在 ut 分支使用了 h2 内存数据库，可以最快捷的体验完整测试用例。
>
> 在 master 上使用了 MySQL8，方便演示 Liquibase，但是需要先创建 paper 数据库。

## 环境

* MySQL8
* JDK8
* JUnit4
* Hamcrest2
* Mockito
* JaCoCo
* Liquibase

## 测试报告

使用 JaCoCo 生成测试报告，实际使用中，可以集成到 Sonar 质量检查中。

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