# Java Bytecode Introduction

1. Why Learn Bytecode?
2. Hello World
3. JVM bytecode Overview
4. case 1: StringBuilder vs "a" + "b"
5. case 2: What's the result?
6. case 3: How lambda works with local variable?
7. DepTool 是如何工作的？
7. AOP Introduce
8. Q & A
   - 如何学习 Java Bytecode？

---
# Why Learn Bytecode

1. 学习模式之：汇编思维、底层思维
   - 底层是道，上层是术。
   - 一生二、二生三、三生万物
   - 如何应对复杂的技术栈？如何学习复杂的Java技术栈？
   - 如果你觉得C语言很难，那么你应该学习了解汇编语言
1. 理解 JVM 的工作原理
   - 字节码是 JVM 的核心
   - 其他：内存管理（GC、内存模型）、Classloader、并发
1. 高级应用
   - AOP：Spring的第二个灵魂。(第一个是 Dependency Injection)
   - Java Agent 与 Instrumentation。(无侵入的监控、埋点、服务治理)
   - SmartBi 代码增强工具、性能分析工具
   - MySQL 安全补丁
   - 基于字节码的分析工具 (SmartBi-DependencyTool简介)

---
# Hello World
```java
public class Hello {
    
    public static void main(String[] args) {
        int n = 10;
        int s = sums(n);
        System.out.println("Sum of 1 to " + n + " is " + s);
    }

    static int sums(int a) {
        int r = 0;
        for(int i = 0; i < a; i++) {
            r += i;
        }
        return r;
    }

}
`javap -c Hello`
```
---
# Hello World

```
  static int sums(int);
    Code:
       0: iconst_0          // 0            //
       1: istore_1          //              // $1 = 0
       2: iconst_0          // 0            //
       3: istore_2          //              // $2 = 0
       4: iload_2           // $2
       5: iload_0           // $2 $0
       6: if_icmpge     19  //              // if $2 >= $0 goto 19
       9: iload_1           // $1
      10: iload_2           // $1 $2
      11: iadd              // $1 + $2
      12: istore_1          //              // $1 = $1 + $2
      13: iinc          2, 1//              // $2 = $2 + 1
      16: goto          4   //              // goto 4
      19: iload_1           // $1
      20: ireturn           //              // return $1
```
1. Frame
   1. stack: iconst_x, iload_x, i_store_x
   2. local: iinc
2. Heap(object fields)

---

# JVM bytecode overview

1. JVM spec: https://docs.oracle.com/javase/specs/jvms/se19/html/index.html
2. instructions:
   1. Load & Store
   2. 算数运算
   3. 类型转换
   4. new, newarray
   5. stack operation
   6. if, goto, return, throw
   7. monitorenter, monitorexit

---
# Case 1: `new StringBuilder("a").append("b")` vs `"a" + b"`

```java
public class Case1 {

    public static void main(String[] args) {
        String hello = "Hello";
        String world = "world";
        String s1 = hello + world;
        String s2 = new StringBuilder().append(hello).append(world).toString();
    }

}
```

---
# Case 2: What is the result?

```java
public class Case2 {

    int a() {
        try {
            return 0;
        } finally {
            return 1;
        }
    }
}
```

---
# Case 3: How lambda works with local variable?
```java
    public static void main(String[] args) {

        int n = 10;

        int sums = profile(() -> {
            int s = 0;
            for (int i = 0; i < n; i++) {
                s += i;
            }
            return s;
        });

        System.out.println("Sum of 1 to " + n + " is " + sums);

    }
```
- Q： 为什么 Java语言要限制 n 是 final 的？
- Q： inner class 是如何访问外部类的?
- Q： inner class 是如何访问外部类的 private field/method 的？
---

# DependencyTool 是如何工作的

DepenencyTool: https://alidocs.dingtalk.com/i/nodes/xdqZp24KneBJz6vvXg2zJvyb7RA0Nr1E

1. 查看 Smartbi-DAO.jar 的依赖关系
   - 哪些Jar包（模块）依赖了 Smartbi-DAO
   - Smartbi-DAO 中哪些 class 被外部模块依赖（模块、类）
2. Why this tool?
3. How this tool works?
---

```scala
    def checkClass(classfile: Array[Byte]): List[Dependency] =
      val cr = new ClassReader(classfile)
      val className = cr.getClassName
      val dependences = collection.mutable.ListBuffer[Dependency]()
      cr.accept( new ClassVisitor(Opcodes.ASM9) {
        override def visit(version: Int, access: Int, name: String, signature: String, superName: String, interfaces: Array[String]): Unit =
          if calleeJarDict.contains(superName) then // extends class
            dependences += Dependency.Class(callerJar.getName, className, calleeJar.getName, superName)
          interfaces.foreach { interface =>
            if calleeJarDict.contains(interface) then // implements class
              dependences += Dependency.Class(callerJar.getName, className, calleeJar.getName, interface)
          }

        override def visitMethod(access: Int, name: String, descriptor: String, signature: String, exceptions: Array[String]): MethodVisitor =
          new MethodVisitor(Opcodes.ASM9) {
            // class.method(...)
            override def visitMethodInsn(opcode: Int, owner: String, name: String, descriptor: String, isInterface: Boolean): Unit = 
              if calleeJarDict.contains(owner) then
                dependences += Dependency.Method(callerJar.getName, className, calleeJar.getName, owner, name, isInterface)

            // class.field
            override def visitFieldInsn(opcode: Int, owner: String, name: String, descriptor: String): Unit =
              if calleeJarDict.contains(owner) then
                dependences += Dependency.Field(callerJar.getName, className, callerJar.getName, owner, name)
          }
      }, 0)
      dependences.toList.toSet.toList
```

---
# AOP overview & Class Instruments
1. Spring 的灵魂 (DI, [AOP](https://docs.spring.io/spring-framework/docs/3.2.x/spring-framework-reference/html/aop.html))
2. AOP: AsepectJ
   - Aspect
   - PointCut
   - Advice
2. Java Agent 相关技术（https://docs.oracle.com/en/java/javase/17/docs/api/java.instrument/java/lang/instrument/package-summary.html）
3. 服务治理工具: arms, pinpoint, skywalking
4. Java 工具包：arthus
5. Java 字节码增强工具：ASM, javassist, bytebuddy

---
# bytecode usage in smartbi

1. 字节码增强
2. MySQL 安全补丁
3. More: [SmartBI 性能分析工具设计草案](https://alidocs.dingtalk.com/i/nodes/lo1YvX0prG98kyplvM25VPw7xzbmLdEZ)

---
# Q & A
1. 如何学习Java字节码？
2. synchronzied 关键字是如何工作的？
3. Java的接口泛化是如何工作的？
   ```  
   ```

---


