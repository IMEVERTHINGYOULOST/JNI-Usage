#eclipse 用阿里云Hte建立的例子
用maven创建项目
package co.hxgroup.hitsdbclient;

import java.io.IOException;
import java.util.List;

import com.aliyun.hitsdb.client.HiTSDB;
import com.aliyun.hitsdb.client.HiTSDBClientFactory;
import com.aliyun.hitsdb.client.HiTSDBConfig;
import com.aliyun.hitsdb.client.value.request.Query;
import com.aliyun.hitsdb.client.value.request.SubQuery;
import com.aliyun.hitsdb.client.value.response.QueryResult;
import com.aliyun.hitsdb.client.value.type.Aggregator;
import com.aliyun.hitsdb.client.value.request.Point;

public class App {
	HiTSDBConfig config = HiTSDBConfig.address("101.37.0.196", 3242).config();
	HiTSDB tsdb = HiTSDBClientFactory.connect(config);
	
	public void pushdata(long[] oid) throws IOException, InterruptedException {
		System.out.println("ready to pushdata into hitsdb");	
		//long[] oids;
		for (int i = 0; i < 5; i++) {
			System.out.println(oid[i]);		
		}
		for (int i = 0; i < 5; i++) {
			System.out.println(i);			
			Point point = Point.metric("test").tag("V", "1.0").value(System.currentTimeMillis(), oid[i]).build();
			System.out.println(point.getMetric());	
			System.out.println(point.getValue());	
			//Thread.sleep(1000); // 1秒提交1次
			tsdb.put(point);
			System.out.println("really put point into tsdb?");
		}
	}

	public void querydata(long oid) throws IOException, InterruptedException {
		System.out.println("ready to querydata from hitsdb");
		
		long now = System.currentTimeMillis();
		System.out.println(now);
		Query query = Query.timeRange(now - 3600 * 1000, now)
				.sub(SubQuery.metric("test").aggregator(Aggregator.NONE).tag("V", "1.0").build()).build();
		List<QueryResult> result = tsdb.query(query);
		
		
		System.out.println("calei");
		System.out.println(result);
		System.out.println("CLOSE");
		tsdb.close();
	}
	
	public void pushfunc(long[] oid) throws IOException, InterruptedException {
		System.out.println("ready to get push function");
		for (int i = 0; i < 5; i++) {
			System.out.println(oid[i]);		
		}
		App pushfunc = new App();			
		try {
			pushfunc.pushdata(oid);	
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void queryfunc(long oid) throws IOException, InterruptedException {
		System.out.println("query function:");
		App pushfunc = new App();			
		try {
			pushfunc.querydata(oid);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		//System.load("/users/hxdms/vobs/hx_platform/dbms_tools/pdb2rdb/hitsdb/libtest.so");
		App apptest = new App();
		long potval =  5066549582823426L;
		System.out.println(potval);
		long[] tmp = new long[5];
		for(int i = 0 ;i< 5; i++) {
			tmp[i] = 222222222222L;
		}
		
		try {
			apptest.pushfunc(tmp);
			apptest.queryfunc(potval);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
