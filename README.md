# Paper  ![](https://img.shields.io/badge/build-passing-brightgreen) ![](https://img.shields.io/badge/coverage-100%25-green)

这是一个 Spring Boot 演示项目。主要目标：

* 单元测试
* 集成测试
* 测试报告
* 数据库版本管理
* 打包标记
* 统一异常处理

## 环境

* MySQL8
* JDK8
* JUnit4
* Hamcrest2

## 单元测试

* 在 DAO、Service、Controller 层都实现了单元测试，类名以 UnitTest 结尾。
* 在每层都会使用 Mockito 隔离所有依赖。
* 在 ut 分支使用了 h2 内存数据库，直接运行 *mvn test* 即可，可以最快捷的体验完整测试用例。
* 在 master 上使用了 MySQL8，方便演示 Liquibase，但是需要先创建 paper 数据库。

## 集成测试

* 在 Controller 层实现了集成测试，类名以 IntegrationTest 结尾。
* 在启动测试时，会实例化所有上下文。
* 还可以在 YApi 中跑集成测试（通过 Swagger 同步接口），并且在其中查看测试报告。

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

### Maven 插件常用命令

生成 changelog（需要指定 outputChangeLogFile 属性），默认不包括 data（通过 diffTypes 控制）

```shell
# mvn liquibase:generateChangeLog
```

执行 ChangeSet 升级数据库

```shell
# mvn liquibase:update
```

在当前版本打一个 Tag

```shell
# mvn liquibase:tag -Dliquibase.tag=v1.0
```

回滚到这个 Tag 

```shell
# mvn liquibase:rollback -Dliquibase.rollbackTag=v1.0
```

生成 *v1.0* 以后的 Rollback SQL

```shell
# mvn liquibase:rollbackSQL -Dliquibase.rollbackTag=v1.0
```

回滚 *2020-02-17* 的数据库操作

```shell
# mvn liquibase:rollback -Dliquibase.rollbackDate=2020-02-17
```

### 基本规范

* 每个模块使用自己的 Liquibase 管理（考虑 DATABASECHANGELOG 是否要独立）
* Liquibase change log 主文件目录 *src/main/resources/db/changelog-master.xml*
* 在 db 下建立子目录 changelogs，每个版本建立一个 *changelog-版本号.xml*，如 changelog-1.0.xml

```
.
├── src/main/resources/db
│   ├── changelogs                                            
│   │   ├── changelog-1.0.xml                                  
│   │   ├── changelog-1.1.xml
│   │   ├── changelog-1.2.xml
│   ├── changelog-master.xml
│   ├── liquibase.properties
```

* 在 *changelog-master.xml* 中引入目录 *includeAll path="db/changelogs/"* 或者单个文件。
* 避免对每个 ChangeSet 做多件事情，以避免失败的自动提交语句使数据库处于意外状态。
* ChangeSet id 使用 *事务描述-年月日-序号*，如 *CreateTableUser-20200214-001, InsertUser-20200214-002*
* ChangeSet 必须填写 *author*
* 所有表，列要加 *remarks* 进行注释
* 严禁修改已经执行过的 ChangeSet 
* 建议为每个 ChangeSet 编写 Rollback，虽然有些 Rollback 会自动生成，但最好自己控制其行为
* 每个版本结束打一个 Tag

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

## 统一异常处理

* 不在业务代码中捕获任何异常, 全部交由 *@RestControllerAdvice* 来处理

* *@RestControllerAdvice* 不会处理 404 异常，所以必须要单独处理，示例 *ErrorHandler*

* 统一处理返回类型和消息，示例 *ResponseInfo* 和 *ResultEnum*

* 所有返回 HTTP status code 都是 200，通过 ResponseInfo.code 区分信息类型

* Mock checked exception 要使用 BDDMockito，这里还有两种情况：
  
  1. void 方法只能使用 willAnswer.given 这种形式 stub
  ```java
  willAnswer(invocation -> {
     throw new HttpMediaTypeNotSupportedException("不支持的类型");
  }).given(xxx).voidmethod();
  ```
  2. 其它情况下，还可以使用
  ```java
  given(xxx.method()).willAnswer( invocation -> { throw new CheckedException("msg"); });
  ```



