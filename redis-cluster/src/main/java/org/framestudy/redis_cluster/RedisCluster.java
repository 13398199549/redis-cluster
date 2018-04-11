package org.framestudy.redis_cluster;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;
/**
 * Hash一致算法实现
 * @author Administrator
 *
 */
public class RedisCluster {

	 public static void main(String[] args){
		 	//定义数据池配置信息
	        GenericObjectPoolConfig config=new GenericObjectPoolConfig();
	        config.setMaxTotal(1000);
	        config.setMaxIdle(500);

	        List<JedisShardInfo> jedisShardInfoList=new ArrayList<JedisShardInfo>();
	        JedisShardInfo shardInfo1=new JedisShardInfo("192.168.199.100",6379);
	        JedisShardInfo shardInfo2=new JedisShardInfo("192.168.199.100",6380);
	        JedisShardInfo shardInfo3=new JedisShardInfo("192.168.199.100",6381);
	        jedisShardInfoList.add(shardInfo1);
	        jedisShardInfoList.add(shardInfo2);
	        jedisShardInfoList.add(shardInfo3);

	        ShardedJedisPool pool=new ShardedJedisPool(config,jedisShardInfoList);

	        set("user1","a",pool);
	        set("user12","b",pool);
	        set("user13","c",pool);
	        set("usera","d",pool);
	        set("userb","e",pool);
	        
	        System.out.println(getString("user1", pool));
	        System.out.println(getString("user12", pool));
	        System.out.println(getString("user13", pool));
	        
	    }

	 
	 	public static String getString(String key,ShardedJedisPool pool) {
	 		ShardedJedis shardedJedis=pool.getResource();
	 		return shardedJedis.get(key);
	 	}
	 	
	 
	    public static void set(String key,String value,ShardedJedisPool pool){
	        ShardedJedis shardedJedis=pool.getResource();
	        shardedJedis.set(key,value);
	        pool.returnResource(shardedJedis);
	    }
	
	    public static void set(String key,String value,int seconds,ShardedJedisPool pool){
	        ShardedJedis shardedJedis=pool.getResource();
	        shardedJedis.set(key,value);
	        shardedJedis.expire(key, seconds);
	        pool.returnResource(shardedJedis);
	    }
	
	
	
}
