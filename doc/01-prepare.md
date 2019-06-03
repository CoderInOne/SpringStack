# 准备一个高效的学习方法

我的目的只有一个：让每个知识点的学习流程化。我的学习步骤很简单：

1. 提出问题，如Bean的定义如何加载
2. 写个简单的单元测试，见feature-bean-lifecycle分支的xunshan.spring.bean_definition包路径
3. 三件套：代码追查，debug，针对性测试
4. 总结与讨论
5. 有反馈，则回到1

流程化，即时反馈，分割问题这是我最喜爱的学习方式。流程化使得效率提高。即时反馈要求我写更多的代码，如果
我觉得这里不懂，我就尝试去写个单元测试，在写的过程中，报错/反复读文档和代码，这些让我受益良多。单元测试
总是会逼迫我们去分解问题，分解系统，没什么方式比这种更好地理解系统了。

接下来，说下我的做法。

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
- 开始编写单元测试，详细见本项目`feature-bean-lifecycle`分支
- 研究完成，合并分支到master: `git checkout master && git merge feature-bean-lifecycle`
- 记录新的，分享收获

在这里，我有两个tips：

1. 研究一个新特性后，把分支合并到master，这样方便回头review
2. 分享自己的成果总是皆大欢喜的，既帮助了别人，自己的表达能力和成就感也会提升哦

## 最后

欢迎大家参与进来，把这个项目完善起来！另外，如果有任何疑问或者想知道的特性，烦请提交issue，谢谢！