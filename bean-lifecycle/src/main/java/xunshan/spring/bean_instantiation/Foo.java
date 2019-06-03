package xunshan.spring.bean_instantiation;

public class Foo {
	String a;
	int b;

	public Foo() {
		System.out.println("init foo");
	}

	public String foo() {
		return a;
	}

	public void setA(String a) {
		this.a = a;
	}

	public void setB(int b) {
		this.b = b;
	}
}
