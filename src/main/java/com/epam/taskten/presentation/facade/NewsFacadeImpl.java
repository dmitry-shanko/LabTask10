package com.epam.taskten.presentation.facade;

import java.util.Collections;
import java.util.List;

import com.epam.taskten.database.GeneralDao;
import com.epam.taskten.database.exception.DaoException;
import com.epam.taskten.model.News;

public class NewsFacadeImpl implements NewsFacade
{
	private GeneralDao<News, Integer> newsDao;
	
	public void setNewsDao(GeneralDao<News, Integer> newsDao)
	{
		this.newsDao = newsDao;
	}
	
	public GeneralDao<News,Integer> getNewsDao()
	{
		return this.newsDao;
	}

	@Override
	public List<News> getNewsList() throws DaoException 
	{
		List<News> news = newsDao.getList();
		if (null != news)
		{
			Collections.sort(news);
		}
		return news;
	}

	@Override
	public News fetchById(Integer id) throws DaoException 
	{
		return newsDao.fetchById(id);
	}

	@Override
	public News save(News entity) throws DaoException 
	{
		return newsDao.save(entity);
	}

	@Override
	public void remove(Integer... ids) throws DaoException 
	{
		newsDao.remove(ids);		
	}

	@Override
	public News updateNews(News entity) throws DaoException 
	{
		return newsDao.update(entity);
	}
}