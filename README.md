# Paper  ![](https://img.shields.io/badge/build-passing-brightgreen) ![](https://img.shields.io/badge/coverage-100%25-green)

这是一个 Spring Boot 演示项目。主要目标：

* 单元测试
* 集成测试
* 测试报告

* 数据库版本管理
* 打包标记


## 环境

* MySQL8
* JDK8
* JUnit4
* Hamcrest2

## 单元测试

* 在 DAO、Service、Controller 层都实现了单元测试，类名以 UnitTest 结尾。
* 在每层都会使用 Mockito 隔所有依赖。
* 在 ut 分支使用了 h2 内存数据库，直接运行 *mvn test* 即可，可以最快捷的体验完整测试用例。
* 在 master 上使用了 MySQL8，方便演示 Liquibase，但是需要先创建 paper 数据库。

## 集成测试

* 在 Controller 层实现了集成测试，类名以 IntegrationTest 结尾。
* 在启动测试时，会实例化所有上下文。

## 测试报告

使用 JaCoCo 生成测试报告，实际使用中，可以集成到 Sonar 质量检查中。

> JaCoCo 不支持接口，所以 JPA DAO 没有被统计进去。

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

## 数据库版本管理

使用 Liquibase 对数据库版本进行管理。

ToDoList：规范

## 打包标记

使用 git-commit-id-plugin 插件，在打包时和项目启动后都可以核对打包时间戳和 git_commit_id。

具体内容可以看这个提交 [4bc02d5](https://github.com/batizhao/paper/commit/4bc02d56f08484bf1ab564797b347dd0b4862da6) 。

包名示例：paper-1.0-4bc02d5-20200214T100709Z.jar

> 项目名称-版本号-git_commit_id-打包的时间戳，通过 pom.finalName 定制。

项目启动后，可以核对打包时间戳和 Git 相关的信息。

```shell
# curl http://localhost:8080/actuator/info                                                          
{
  "app" : {
    "name" : "paper",
    "version" : "1.0",
    "build_time" : "20200214T101154Z"
  },
  "git" : {
    "commit" : {
      "time" : "20200214T101043Z",
      "id" : "f406348"
    },
    "branch" : "master"
  }
}
```



