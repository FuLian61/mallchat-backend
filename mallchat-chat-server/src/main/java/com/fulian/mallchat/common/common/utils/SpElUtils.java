package com.fulian.mallchat.common.common.utils;

import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class SpElUtils {

    private static final ExpressionParser PARSER = new SpelExpressionParser();

    private static final DefaultParameterNameDiscoverer PARAMETER_NAME_DISCOVERER = new DefaultParameterNameDiscoverer();

    public static String getMethodKey(Method method) {
        return method.getDeclaringClass()+"#"+method.getName();
    }

    public static String parseSpEl(Method method, Object[] args, String spEl) {
        String[] params = Optional.ofNullable(PARAMETER_NAME_DISCOVERER.getParameterNames(method)).orElse(new String[]{});//解析参数名
        EvaluationContext context = new StandardEvaluationContext();//el解析需要的上下文对象
        for (int i = 0; i < params.length; i++) {
            context.setVariable(params[i], args[i]);//所有参数都作为原材料扔进去
        }
        Expression expression = PARSER.parseExpression(spEl);
        return expression.getValue(context, String.class);
    }

    public static void main(String[] args) {

        List<Integer> primes = new ArrayList<Integer>();
        primes.addAll(Arrays.asList(2,3,5,7,11,13,17));

        // 创建解析器
        ExpressionParser parser = new SpelExpressionParser();
        //构造上下文
        StandardEvaluationContext context = new StandardEvaluationContext();
        context.setVariable("primes",primes);

        //解析表达式
        Expression exp =parser.parseExpression("#primes");
        // 求值
        List<Integer> primesGreaterThanTen = (List<Integer>)exp.getValue(context);
    }
}
