# README

## 项目介绍

- 项目名称：数据同步工具（data-sync-tool）
- 组件介绍：

  - 基础组件（data-sync-common）：包含异常类、模型类、插件基类、工具类等
- 服务组件（data-sync-server）：常规的Spring Boot项目，便于初始化配置数据库与启动项目
  - 作业组件（data-sync-job）：项目核心的基类与实现类
- 插件组件（data-sync-plugin）：插件自定义开发，继承自基础组件的插件基类

### JavaDoc

- 文档位置：JavaDoc/index.html

### 同步流程

```mermaid
graph LR
采集数据源 --> 读取器 --> 映射器 --> 写入器 --> 目的数据源
```

## 同步作业（SyncJob）

- 一个同步任务对应一个同步作业
- 配置、运行各类执行器
- 配置定时增量同步

## 模块介绍

### 数据源（DataSource）

#### 数据库（DB）

- MySQL
- PostgreSQL
- SQLServer

#### 接口（API）

### 执行器（Executor）

- 分为执行器（Executor）与元件(Unit)（可选）
- 部分执行器需要更细节的元件用以执行方法
- 以内模块均为执行器的子类

### 读取器（Reader）

#### 数据库读取器（DBReader）

- 统一的连接方法
- 统一的读取方法

#### 接口读取器（APIReader）

- 需要自定义解析插件，用以解析拉取的JSON格式的数据

### 过滤器（Fliter）

- 部署在读取器内部
- 解析过滤规则
- 生成过滤语句
- 增量同步时自动生成时间过滤，需要指定同步字段（sync_field_name）

#### 数据库过滤器（DBFliter）

##### 规则语法

- 一般以sql的where语句实现

##### 数据库过滤器元件（DBFilterUnit）

- 解析单条规则

#### 接口过滤器（APIFliter）

##### 规则语法

- 一般以传入参数的形式实现

### 映射器（Mapper）

- 传入源字段、目标字段
- 检查字段名、字段类型
- 检查映射规则
- 解析映射规则

#### 规则语法

- 字段均使用大括号表示
- 目标字段在等号左侧
- 源字段在等号右侧
- 范例：

```properties
{name}={first_name} {last_name}
```

#### 映射器元件（DBFilterUnit）

- 解析、运行单条规则

### 写入器（Writer）

#### 数据库写入器（DBWriter）

- 统一的连接方法
- 统一的写入方法

## 同步数据集（SyncDataSet）

- 数据同步的存储类

### 数据格式

- List<Map<String, String> data>

### 插件（Plugin）

- 当前仅支持接口数据解析插件（ResponseParsePlugin）

## 配置规范

### 配置数据库

- 详见服务组件中的模型类

### 同步类型（SyncType）

- INCREMENTAL
- TOTAL

### 数据源类型（DataSourceType）

- DATABASE
- API

### 数据库类型（DBType）

- MYSQL
- POSTGRESQL
- SQLSERVER

### 执行器类型（ExecutorType）

- Reader
- Filter
- Mapper
- Writer

### 字段类型（FieldType）

- INT
- FLOAT
- STRING
- TIME
- BOOLEAN

## 待办事项

// TODO 执行器的基类与实现类进一步分离

- 执行器完全插件化
- 自定义处理流程