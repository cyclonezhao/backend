package com.backend.common.mybatis.interceptor;

import com.backend.common.base.BaseEntity;
import com.backend.common.mybatis.mybatisplus.Sequence;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;

@Intercepts(
        {
                @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class}),
        }
)
public class GenIdInterceptor implements Interceptor {
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object target = invocation.getTarget();
        Object[] args = invocation.getArgs();
        if (target instanceof Executor) {
            MappedStatement ms = (MappedStatement) args[0];
            if (ms.getSqlCommandType() == SqlCommandType.INSERT) {
                Object parameter = args[1];
                if(parameter instanceof BaseEntity){
                    ((BaseEntity)parameter).setId(new Sequence().nextId());
                }
            }
        }

        return invocation.proceed();
    }
}
