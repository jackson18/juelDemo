package com.example.jueldemo;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.SimpleEvaluationContext;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * ========================================================
 * 日 期：2020/9/12 5:38 下午
 * 作 者：jacksonqi
 * 版 本：1.0.0
 * 类说明：
 * ========================================================
 * 修订日期     修订人    描述
 */
public class SpelDemo implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    public static void main(String[] args) throws NoSuchMethodException {
        ExpressionParser parser = new SpelExpressionParser();

        // 1.字面表达式
        // Spel 支持strings, numeric values (int, real, hex), boolean, and null等基本类型
        String helloWorld = (String) parser.parseExpression("'Hello World'").getValue();
        System.out.println(helloWorld);

        // 2.Inline List
        // 通过{}来表明 List 表达式，一个空的列表直接用{}表示
        // Integer列表
        List numbers = (List) parser.parseExpression("{1,2,3,4}").getValue();
        System.out.println("list: " + numbers);

        // 3.Inline map
        // {key:value}来表示 map 表达式，空 Map 直接用{:}表示
        Map map = (Map) parser.parseExpression("{txt:'Nikola',dob:'10-July-1856'}").getValue();
        System.out.println("map: " + map);

        // 4.数组
        // 数组可以借助new构造方法来实现，通过下标ary[index]的方式访问数组中的元素
        int[] numbers2 = (int[]) parser.parseExpression("new int[]{1,2,3}").getValue();

        int[] nums = new int[]{1, 3, 5};
        EvaluationContext context = new StandardEvaluationContext();
        context.setVariable("num", nums);
        // 通过下标访问数组中的元素
        Integer numVal = parser.parseExpression("#num[1]").getValue(context, Integer.class);
        System.out.println("numVal in array: " + numVal);

        // 5.表达式
        // Spel 支持一些 Java 语法中常规的比较判断，算数运算，三元表达式，类型判断，matches正则匹配等基表表达式
        // 运算
        System.out.println("1+2= " + parser.parseExpression("1+2").getValue());
        // 比较
        System.out.println("1<2= " + parser.parseExpression("1<2").getValue());
        System.out.println("true ? hello : false > " + parser.parseExpression("3 > 2 ? 'hello': 'false' ").getValue());

        // 6.变量引用
        /**
         * 细心的小伙伴，在上面介绍数组的成员演示的实例中，写法如"#num[1]"，这个 num 前面有一个#，这是一个语法定义，有#修饰的表示变量访问
         *
         * 要理解这一小节，首先得理解EvaluationContext, 在我们的 SpEL 表达式的解析中，getValue有一个参数就是这个 Context，你可以将他简单理解为包含一些对象的上下文，我们可以通过 SpEL 的语法，来访问操作 Context 中的某些成员、成员方法属性等
         *
         * 一般的操作过程如下:
         *
         * context.setVariable("person", person); 向EvaluationContext中塞入成员变量
         * parser.parseExpression(xxx).getValue(context) 解析 SpEL 表达式，context 必须作为传参丢进去哦
         */
        Person person = new Person("一灰灰blog", 18);
        context.setVariable("person", person);

        String name = parser.parseExpression("#person.getName()").getValue(context, String.class);
        System.out.println("variable name: " + name);

        Integer age = parser.parseExpression("#person.age").getValue(context, Integer.class);
        System.out.println("variable age: " + age);

        // 7.函数
        // Context 中的变量，除了是我们常见的基本类型，普通的对象之外，还可以是方法，在setVariable时，设置的成员类型为method即可
        // 注册一个方法变量，参数为method类型
        context.setVariable("hello", StaClz.class.getDeclaredMethod("hello", String.class));

        String ans = parser.parseExpression("#hello('一灰灰')").getValue(context, String.class);
        System.out.println("function call: " + ans);

        // 8.bean 访问
        /**
         * 在 Spring 中，什么对象最常见？当然是 bean, 那么我们可以直接通过 SpEL 访问 bean 的属性、调用方法么？
         *
         * 要访问 bean 对象，所以我们的EvaluationContext中需要包含 bean 对象才行
         *
         * 借助BeanResolver来实现，如context.setBeanResolver(new BeanFactoryResolver(applicationContext));
         * 其次访问 bean 的前缀修饰为@符号
         * 为了演示这种场景，首先创建一个普通的 Bean 对象
         */

    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public void bean() {
        ExpressionParser parser = new SpelExpressionParser();
        StandardEvaluationContext context = new StandardEvaluationContext();
        context.setBeanResolver(new BeanFactoryResolver(applicationContext));

        // 获取bean对象
        BeanDemo beanDemo = parser.parseExpression("@beanDemo").getValue(context, BeanDemo.class);
        System.out.println("bean: " + beanDemo);

        // 访问bean方法
        String ans = parser.parseExpression("@beanDemo.hello('一灰灰blog')").getValue(context, String.class);
        System.out.println("bean method return: " + ans);
    }

}
