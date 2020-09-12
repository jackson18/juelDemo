package com.example.jueldemo;

import de.odysseus.el.ExpressionFactoryImpl;
import de.odysseus.el.util.SimpleContext;

import javax.el.ExpressionFactory;
import javax.el.ValueExpression;

/**
 * ========================================================
 * 日 期：2020/9/12 4:19 下午
 * 作 者：jacksonqi
 * 版 本：1.0.0
 * 类说明：
 * ========================================================
 * 修订日期     修订人    描述
 */
public class QuickStartDemo {

    public static void main(String[] args) {
        //第一步、创建基本的工厂类和上下文以供下面使用
        ExpressionFactory factory = new ExpressionFactoryImpl();
        SimpleContext context = new SimpleContext();

        //第二步、设置变量值
        context.setVariable("var1", factory.createValueExpression("Hello", String.class));
        context.setVariable("var2", factory.createValueExpression("World", String.class));

        //第三步、解析字符串
        String s = "{\"argIn1\":\"${var1}\",\"argIn2\":\"${var2}\"}";
        ValueExpression e = factory.createValueExpression(context, s, String.class);
        System.out.println(e.getValue(context));// --> {"argIn1":"Hello","argIn2":"World"}
    }

}
