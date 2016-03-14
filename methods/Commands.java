package methods;

import java.util.Random;


public class Commands {

	public Commands() {
	}
	
	public static int add(int x, int y) {
		return x+y;
	}

	public static int mul(int x, int y) {
		return x*y;
	}

	public static int sub(int x, int y) {
		return x-y;
	}

	public static int div(int x, int y) {
		return x/y;
	}

	public static Integer inc(Integer x) {
		return x+1;
	}

	public static Integer dec(Integer x) {
		return x-1;
	}
	
	public static float add(float x, float y) {
		return x+y;
	}

	public static float mul(float x, float y) {
		return x*y;
	}

	public static float sub(float x, float y) {
		return x-y;
	}

	public static float div(float x, float y) {
		return x/y;
	}

	public static Float inc(Float x) {
		return x+1;
	}

	public static Float dec(Float x) {
		return x-1;
	}
	
	public static String add(String x, String y) {
		return x+y;
	}
	
	public static int len(String x) {
		return x.length();
	}
	
	static Random rand = new Random(System.currentTimeMillis());
	public static int rand() {
		return rand.nextInt();
	}
	
	public static int rand(int bound) {
		return rand.nextInt(bound);
	}

	public static float randFloat() {
		return rand.nextFloat();
	}

	public static String javaVersion() {
		return "java.version:                  "+System.getProperty("java.version")
				+"\njava.vm.specification.version: "+System.getProperty("java.vm.specification.version")
				+"\njava.specification.version:    "+System.getProperty("java.specification.version")
				+"\njava.class.version:            "+System.getProperty("java.class.version")
				+"\njava.compiler:                 "+System.getProperty("java.compiler");
	}
}
