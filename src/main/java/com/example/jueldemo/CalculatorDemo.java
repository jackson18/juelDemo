package com.example.jueldemo;

import de.odysseus.el.ExpressionFactoryImpl;
import de.odysseus.el.util.SimpleContext;

import javax.el.ExpressionFactory;
import javax.el.ValueExpression;

/**
 * ========================================================
 * 日 期：2020/9/12 4:27 下午
 * 作 者：jacksonqi
 * 版 本：1.0.0
 * 类说明：分别用context和factory两种方式来给变量赋值，最后将${math:max(foo,bar)}成功解析为取foo和bar更大的一个
 * ========================================================
 * 修订日期     修订人    描述
 */
public class CalculatorDemo {

    public static void main(String[] args) throws NoSuchMethodException {
        // the ExpressionFactory implementation is de.odysseus.el.ExpressionFactoryImpl
        ExpressionFactory factory = new ExpressionFactoryImpl();

        // package de.odysseus.el.util provides a ready-to-use subclass of ELContext
        SimpleContext context = new SimpleContext();

        // map function math:max(int, int) to java.lang.Math.max(int, int)
        // 函数的前缀 函数的名称 ，运行的方法  三个參数的含义
        context.setFunction("math", "max", Math.class.getMethod("max", int.class, int.class));

        // map variable foo to 0
        context.setVariable("foo", factory.createValueExpression(0, int.class));

        // set value for top-level property "bar" to 1
        factory.createValueExpression(context, "${bar}", int.class).setValue(context, 1);

        // parse our expression
        ValueExpression e = factory.createValueExpression(context, "${math:max(foo,bar)}", int.class);

        // get value for our expression
        System.out.println(e.getValue(context)); // --> 1
    }

}
