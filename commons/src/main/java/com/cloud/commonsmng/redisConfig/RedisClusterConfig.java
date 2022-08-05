package com.cloud.commonsmng.redisConfig;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

//@Configuration
public class RedisClusterConfig {

    //@Autowired
    private ClusterConfigurationProperties clusterProperties;

    //@Bean
//    public RedisConnectionFactory connectionFactory() {
//        //集群的项目配置
//        RedisClusterConfiguration configuration = new
//                RedisClusterConfiguration(clusterProperties.getNodes());
//        configuration.setMaxRedirects(clusterProperties.getMaxRedirects());
//        //设置 客户端Jedis的连接
//        return new JedisConnectionFactory(configuration);
//    }


    //@Bean
    public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory factory){
        RedisTemplate<String, String> redisTemplate = new RedisTemplate<String,String>();
        //使用刚才设置好的ConnectionFactory
        redisTemplate.setConnectionFactory(factory);

        /**
         * 定义好三种序列化方式：Jackson序列化、Jdk序列化、String序列化
         */

        /**Jackson序列化  json占用的内存最小 */
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);

        /**Jdk序列化   JdkSerializationRedisSerializer是最高效的*/
        // JdkSerializationRedisSerializer jdkSerializationRedisSerializer = new JdkSerializationRedisSerializer();

        /**String序列化*/
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();

        /**
         * 设置不同数据类型的序列化方式
         * /

         /**将key value 进行stringRedisSerializer序列化*/
        redisTemplate.setKeySerializer(stringRedisSerializer);
        redisTemplate.setValueSerializer(stringRedisSerializer);

        /**将HashKey HashValue 进行序列化*/
        redisTemplate.setHashKeySerializer(stringRedisSerializer);
        redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);

        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

}

