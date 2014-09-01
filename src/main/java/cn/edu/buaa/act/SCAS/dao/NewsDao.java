package cn.edu.buaa.act.SCAS.dao;

import java.util.List;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.ibatis.sqlmap.client.SqlMapClient;

import cn.edu.buaa.act.SCAS.po.News;

public class NewsDao extends SqlMapClientDaoSupport{
	
	public NewsDao(SqlMapClient sqlMapClient){
		this.setSqlMapClient(sqlMapClient);
	}
	
	public List<News> getAllNews(){
		return this.getSqlMapClientTemplate().queryForList("News.getAllNews_sql");
	}
	

}
