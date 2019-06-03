# 玩转Spring技术栈

本项目旨在彻底，深入地学习Spring技术栈。在当今Java世界中，使用Spring框架作为基础的项目已经数不胜数，
Spring经过十几年的发展，积累了很多的宝贵经验和最佳实践，我们没有理由不深入学习它。

## 核心内容

- 基础概念与原理，如`IOC`，`AOP`，`MVC`等
- 源代码分析
- 进阶主题：SpringBoot，SpringCloud，ReactiveFramework
- 高级特性：如何定制注解，中间件如何整合Spring

## How to

本项目主要采用单元测试的办法去实践Spring中的每个单元。原因有下：

- 单元化：每个部分独立出来，研究它，把玩它，才能更好其吸收特性
- Spring启动比较耗时，而单独写个测试类，只加载特定的Configuration，大大减少启动时间。记住：反馈越快，效果越好
- Spring日志输出较多，咋们不要把时间浪费在找日志上面，直接用断言，能达到同样的验证效果

## 目录

### 第一部分：Spring IoC容器与依赖注入

- [准备一个高效的学习方法](./doc/01-prepare.md)
- [Bean定义加载与注册](./doc/02-how-to-register-bean.md)
- [Bean的实例化](./doc/03-how-to-instantiate-bean.md)
- [依赖注入](./doc/04-how-bean-injected.md)
- [Bean后置处理器](./doc/05-bean-post-processor.md)

### 第二部分：TODO AOP