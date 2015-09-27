package com.song.common.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class Bdao {
	@Autowired
	private JdbcTemplate mysqlJdbcTemplate;
	@Autowired
	private DataSource dataSource;

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
		mysqlJdbcTemplate = new JdbcTemplate(dataSource);
	}

	public JdbcTemplate getMysqlJdbcTemplate() {
		return mysqlJdbcTemplate;
	}

	public void setMysqlJdbcTemplate(JdbcTemplate mysqlJdbcTemplate) {
		this.mysqlJdbcTemplate = mysqlJdbcTemplate;
	}

	public Map<String, Object> get(String sql) {
		return mysqlJdbcTemplate.queryForMap(sql);
	}

	public <T> List<T> get(Class<T> clazz) {
		Table table = clazz.getAnnotation(Table.class);
		if (null != table) {
			String tableName = table.tableName();
			return query("select * from " + tableName, new Object[0], clazz);
		} else {
			return new ArrayList<T>();
		}
	}

	public <T> List<T> get(String sql, Class<T> clazz) {
		return query(sql, new Object[0], clazz);
	}

	public <T> List<T> get(String sql, List<String> params, Class<T> clazz) {
		return query(sql, params.toArray(), clazz);
	}

	public <T> List<T> query(String sql, Object[] params, Class<T> clazz) {
		return mysqlJdbcTemplate.query(sql, params,
				new BeanPropertyRowMapper<T>(clazz));
	}

	public int update(String sql) {
		return mysqlJdbcTemplate.update(sql);
	}

	public int update(String sql, List<String> params) {
		return mysqlJdbcTemplate.update(sql, params.toArray());
	}

	public int update(String sql, String... params) {
		return mysqlJdbcTemplate.update(sql, Arrays.asList(params));
	}

	public int[] batchUpdate(String sql, List<Object[]> params) {
		return mysqlJdbcTemplate.batchUpdate(sql, params);
	}

	/**
	 * 查询单行单列
	 * 
	 * @param sql
	 * @param requiredType
	 * @return
	 */
	public <T> T queryForSingle(String sql, Class<T> requiredType) {
		T t = null;
		try {
			t = mysqlJdbcTemplate.queryForObject(sql, requiredType);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return t;
	}

	// 自动update，inert

}
