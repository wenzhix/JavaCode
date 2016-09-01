package com.xiaowen.redis;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

/**
 * javaʵ�ּ�Redis����
 * @author xiaowen
 * @2016��9��1��
 */
public class RedisClient {

	private Jedis jedis;//����Ƭ�ͻ�������
	private JedisPool jedisPool;//����Ƭ�����ӳ�
	private ShardedJedis shardedJedis;//��Ƭ�ͻ�������
	private ShardedJedisPool shardedJedisPool;//��Ƭ����
	public RedisClient() {
		initialPool();
		initialShardedPool();
		shardedJedis=shardedJedisPool.getResource();
		jedis=jedisPool.getResource();
	}
	
	/**
	 * ��ʼ����Ƭ��
	 */
	private void initialShardedPool() {
		//����Ƭ�ػ�������
		JedisPoolConfig config=new JedisPoolConfig();
		config.setMaxIdle(5);
		config.setMaxWaitMillis(1000);
		config.setTestOnBorrow(false);
		//slave����
		List<JedisShardInfo> shards=new ArrayList<JedisShardInfo>();
		shards.add(new JedisShardInfo("127.0.0.1", 6379, "master"));
	}
	
	/**
	 * ��ʼ������Ƭ��
	 */
	private void initialPool() {
		//����Ƭ�ػ�������
		JedisPoolConfig config=new JedisPoolConfig();
		config.setMaxIdle(5);
		config.setMaxWaitMillis(1000);
		config.setTestOnBorrow(false);
		List<JedisShardInfo> shards=new ArrayList<JedisShardInfo>();
		jedisPool=new JedisPool(config,"127.0.0.1",6379);
	}
	
	/**
	 * ��ʾ
	 */
	public void show(){
		KeyOperate(); 
	}

	/**
	 * key����
	 */
	private void KeyOperate() {
		System.out.println("========key========");
		//�������
		System.out.println("��տ�����������:"+jedis.flushDB());
		//�ж�key�Ƿ����
		System.out.println("�ж�key999���Ƿ����:"+shardedJedis.exists("key999"));
		System.out.println("����key001,value001��ֵ��:"+shardedJedis.set("key001", "value001"));
		System.out.println("�ж�key001���Ƿ����:"+shardedJedis.exists("key001"));
		//���ϵͳ�����е�key
		System.out.println("����key002,value002��ֵ��"+shardedJedis.set("key002", "value002"));
	    System.out.println("ϵͳ�����м�����:");
	    Set<String> keys=jedis.keys("*");
	    Iterator<String> it=keys.iterator();
	    while(it.hasNext()){
	    	String key=it.next();
	    	System.out.println(key);
	    }
	    //ɾ��ĳ��key����key�����ڣ�����Ը�����
	    System.out.println("ϵͳ��ɾ��key002:"+jedis.del("key002"));
	    System.out.println("�ж�key002�Ƿ����:"+shardedJedis.exists("key002"));
	    //����key001�Ĺ���ʱ��
	    System.out.println("���� key001�Ĺ���ʱ��Ϊ5��"+jedis.expire("key001", 5));
	    try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	    //�鿴ĳ��key��ʣ������ʱ�䣬��λΪ�룬����������߲����ڵĶ�����-1
	    System.out.println("�鿴key001��ʣ������ʱ��:"+jedis.ttl("key001"));
	    //�Ƴ�ĳ��key������ʱ��
	    System.out.println("�Ƴ�key001������ʱ��:"+jedis.persist("key001"));
	    System.out.println("�鿴key001��ʣ������ʱ��:"+jedis.ttl("key001"));
	    //�鿴key���д洢��ֵ������
	    System.out.println("�鿴key���д洢��ֵ������:"+jedis.type("key001"));
	     /*
         * һЩ����������1���޸ļ�����jedis.rename("key6", "key0");
         *           2������ǰdb��key�ƶ���������db���У�jedis.move("foo", 1)
         */
	}
	
	public static void main(String[] args) {
		 new RedisClient().show();
	}
	
}
