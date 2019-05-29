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

### 基本分支

项目中有个`base`分支，是所有子项目的母体，里面包含了最基本的Spring core和context模块，以及相应的测试组件。接下来
我们看看如何构建`base`项目。

先创建一个Maven项目，引入依赖：

```
<properties>
        <spring-base>5.0.8.RELEASE</spring-base>
    </properties>

    <dependencies>
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-context</artifactId>
            <version>${spring-base}</version>
        </dependency>

        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-core</artifactId>
            <version>${spring-base}</version>
        </dependency>

        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-test</artifactId>
            <version>${spring-base}</version>
        </dependency>

        <dependency>
            <groupId>junit</groupId>
            <artifactId>junit</artifactId>
            <version>4.12</version>
        </dependency>

        <dependency>
            <groupId>org.mockito</groupId>
            <artifactId>mockito-all</artifactId>
            <version>1.9.5</version>
        </dependency>
    </dependencies>
```

写个测试类：

```
@Configuration
public class TestConfig {
	class Pojo {
		String a = "a";
	}

	@Bean
	Pojo pojo() {
		return new Pojo();
	}
}
```

测试代码：

```
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class TestConfigTest {
	@Autowired
	private TestConfig.Pojo pojo;

	@Test
	public void pojo() {
		assertNotNull(pojo);
		assertEquals("a", pojo.a);
	}
}
```

运行上面的测试类，运行通过，恭喜你，项目搭建完成。

### 如何研究Spring的某个特性

这里以Bean生命周期为例

- checkout到base分支: `git checkout base`
- 创建分支：`git checkout -b feature-bean-lifecycle`
- 开始编码，详细见本项目`feature-bean-lifecycle`分支
- 研究完成，合并分支到master: `git checkout master && git merge feature-bean-lifecycle`
- 记录新的，分享收获

在这里，我有两个tips：

1. 研究一个新特性后，把分支合并到master，这样方便回头review
2. 分享自己的成果总是皆大欢喜的，既帮助了别人，自己的表达能力和成就感也会提升哦

## 最后

欢迎大家参与进来，把这个项目完善起来！另外，如果有任何疑问或者想知道的特性，烦请提交issue，谢谢！